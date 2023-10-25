package crm.logopedia.data.user.service;


import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.repository.JobPositionRepository;
import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.data.user.model.dto.UserDetailDto;
import crm.logopedia.data.user.model.dto.UserListDto;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.repository.UserRepository;
import crm.logopedia.util.provider.mail.MailProvider;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de las funcionalidades relativas a
 * la entidad {@link User} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	/**
	 * El repositorio de la entidad {@link User}.
	 */
	private final UserRepository USER_REPOSITORY;


	/**
	 * El repositorio de la entidad {@link JobPosition}.
	 */
	private final JobPositionRepository JOB_POSITION_REPOSITORY;

	/**
	 * El gestor de mapeado de objetos.
	 */
	private final ModelMapper MODEL_MAPPER;

	/**
	 * El proveedor de envío de correos electrónicos.
	 */
	private final MailProvider MAIL_PROVIDER;

	/**
	 * El gestor criptográfico de datos.
	 */
	private final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER;

	/**
	 * El número máximo de resultados que una búsqueda puede tener.
	 */
	@Value("${params.sql.query-params.max-results-per-search}")
	protected Integer maxResultsPerPage;

	/**
	 * Ejecuta una serie de instrucciones justo después de construir el componente.
	 */
	@PostConstruct
	private void onPostConstruct() {
		// Entidad a ListDTO
		MODEL_MAPPER.typeMap(User.class, UserListDto.class).addMappings(mapper -> {
			mapper.map(source -> source.getProfile().getFullName(), UserListDto::setFullName);
			mapper.map(source -> source.getProfile().getJobPosition().getId(), UserListDto::setJobPositionId);
			mapper.map(source -> source.getProfile().getJobPosition().getName(), UserListDto::setJobPositionName);
			// TODO: profile image
		});

		// DetailDTO a Entidad
		MODEL_MAPPER.typeMap(UserDetailDto.class, User.class).addMappings(mapper -> {
			mapper.<String>map(
				UserDetailDto::getName,
				(destination, value) -> destination.getProfile().setName(value)
			);

			mapper.<String>map(
				UserDetailDto::getMiddleName,
				(destination, value) -> destination.getProfile().setMiddleName(value)
			);

			mapper.<String>map(
				UserDetailDto::getLastName,
				(destination, value) -> destination.getProfile().setLastName(value)
			);

			mapper.<String>map(
				UserDetailDto::getDuties,
				(destination, value) -> destination.getProfile().setDuties(value)
			);

			mapper.<String>map(
				UserDetailDto::getPhoneNumber,
				(destination, value) -> destination.getProfile().setPhoneNumber(value)
			);

			mapper.<String>map(
				UserDetailDto::getNaf,
				(destination, value) -> destination.getProfile().setNaf(value)
			);

			mapper.<String>map(
				UserDetailDto::getDni,
				(destination, value) -> destination.getProfile().setDni(value)
			);



			mapper.<Long>map(
				UserDetailDto::getJobPositionId,
				(destination, value) -> destination.getProfile().getJobPosition().setId(value)
			);
		});

		// Entidad a DetailDTO
		MODEL_MAPPER.typeMap(User.class, UserDetailDto.class).addMappings(mapper -> {
			mapper.map(source -> source.getProfile().getName(), UserDetailDto::setName);
			mapper.map(source -> source.getProfile().getMiddleName(), UserDetailDto::setMiddleName);
			mapper.map(source -> source.getProfile().getLastName(), UserDetailDto::setLastName);
			mapper.map(source -> source.getProfile().getFullName(), UserDetailDto::setFullName);
			mapper.map(source -> source.getProfile().getDuties(), UserDetailDto::setDuties);
			mapper.map(source -> source.getProfile().getPhoneNumber(), UserDetailDto::setPhoneNumber);
			mapper.map(source -> source.getProfile().getNaf(), UserDetailDto::setNaf);
			mapper.map(source -> source.getProfile().getDni(), UserDetailDto::setDni);
			mapper.map(source -> source.getProfile().getJobPosition().getId(), UserDetailDto::setJobPositionId);
			mapper.map(source -> source.getProfile().getJobPosition().getName(), UserDetailDto::setJobPositionName);
			mapper.map(source -> source.getCreatedBy().getUsername(), UserDetailDto::setCreatedByUsername);
			mapper.map(source -> source.getCreatedBy().getProfile().getFullName(), UserDetailDto::setCreatedByFullName);
			mapper.map(source -> source.getLastModifiedBy().getUsername(), UserDetailDto::setLastModifiedByUsername);
			mapper.map(source -> source.getLastModifiedBy().getProfile().getFullName(), UserDetailDto::setLastModifiedByFullName);

			final Converter<Set<Role>, String> rolesToPlainNameConverter = context -> context.getSource().stream()
				.map(Role::getPlainName)
				.collect(Collectors.joining(", "));
			final Converter<Set<Role>, Long[]> rolesToLongArrayConverter = context -> context.getSource().stream()
				.map(Role::getId)
				.toArray(Long[]::new);

			mapper.using(rolesToPlainNameConverter).map(User::getRoles, UserDetailDto::setPlainRoles);
			mapper.using(rolesToLongArrayConverter).map(User::getRoles, UserDetailDto::setRolesId);
		});
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetailDto findByUsername(String username) {
		return USER_REPOSITORY.findByUsername(username)
			.map(this::convertToDetailDto)
			.orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetailDto findNotVerifiedByUsername(String username) {
		return USER_REPOSITORY.findNotVerifiedByUsername(username)
			.map(this::convertToDetailDto)
			.orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserListDto> findByFilter(UserListDto userListDto, Pageable pageable) {
		final var example = Example.of(
			convertToEntity(userListDto),
			ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
		);

		final var pagination = USER_REPOSITORY.findAll(example, pageable);
		final var users = pagination.getContent().stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());

		return new PageImpl<>(users, pageable, pagination.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserListDto> search(String text) {
		final var pageRequest = PageRequest.of(0, maxResultsPerPage);

		return USER_REPOSITORY.search(text, pageRequest).stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserDetailDto save(UserDetailDto userDetailDto) throws MessagingException {
		final var username = userDetailDto.getUsername();
		final var savedUser = StringUtils.hasLength(username)
			? updateUser(userDetailDto)
			: createUser(userDetailDto);

		return convertToDetailDto(savedUser);
	}

	@Override
	@Transactional
	public void finishAccountConfiguration(String email, String password) {
		USER_REPOSITORY.finishAccountConfiguration(email, BCRYPT_PASSWORD_ENCODER.encode(password));
	}

	/*@Override
	public Page<UserListDto> findByUsernameWorkQueue(Long workQueueId, Pageable pageable) {
		final var pagination = USER_REPOSITORY.findByWorkQueueId(workQueueId, pageable);
		final var users = pagination.stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());

		return new PageImpl<>(users, pageable, pagination.getTotalElements());
	}*/

	@Override
	public List<UserListDto> findAll() {
		return USER_REPOSITORY.findAll().stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());
	}

	/**
	 * Transforma una instancia de la entidad {@link User}
	 * en el DTO {@link UserListDto}.
	 *
	 * @param user La entidad a convertir
	 * @return Una nueva instancia del DTO con los datos de la entidad
	 */
	private UserListDto convertToListDto(final User user) {
		return MODEL_MAPPER.map(user, UserListDto.class);
	}

	/**
	 * Transforma una instancia de la entidad {@link User}
	 * en el DTO {@link UserDetailDto}.
	 *
	 * @param user La entidad a convertir
	 * @return Una nueva instancia del DTO con los datos de la entidad
	 */
	private UserDetailDto convertToDetailDto(final User user) {
		return MODEL_MAPPER.map(user, UserDetailDto.class);
	}

	/**
	 * Transforma una instancia del DTO {@link UserListDto}
	 * en la entidad {@link User}.
	 *
	 * @param userListDto El DTO a convertir
	 * @return Una instancia de la entidad con los datos del DTO
	 */
	private User convertToEntity(final UserListDto userListDto) {
		return MODEL_MAPPER.map(userListDto, User.class);
	}

	/**
	 * Transforma una instancia del DTO {@link UserDetailDto}
	 * en la entidad {@link User}.
	 *
	 * @param userDetailDto El DTO a convertir
	 * @return Una instancia de la entidad con los datos del DTO
	 */
	private User convertToEntity(final UserDetailDto userDetailDto) {
		return MODEL_MAPPER.map(userDetailDto, User.class);
	}

	/**
	 * Crea un nuevo usuario en la base de datos.
	 *
	 * @param userDetailDto El DTO del detalle del usuario que contiene los datos
	 * @return El usuario creado
	 * @exception MessagingException Si ocurre alguna excepción al enviar el correo electrónico
	 */
	private User createUser(final UserDetailDto userDetailDto) throws MessagingException {
		final var createdUser = USER_REPOSITORY.save(convertToEntity(userDetailDto));

		Arrays.stream(userDetailDto.getRolesId()).forEach(roleId -> {
			final var role = Role.builder()
				.id(roleId)
				.build();

			createdUser.addRole(role);
		});

		MAIL_PROVIDER.sendWelcomeUserEmail(userDetailDto.getEmail(), null);

		return createdUser;
	}

	/**
	 * Actualiza un usuario en la base de datos con base a su nombre de usuario.
	 *
	 * @param userDetailDto El DTO de detalle del usuario que contiene los datos a actualizar
	 * @return El usuario actualizado
	 */
	private User updateUser(final UserDetailDto userDetailDto) {
		final var username = userDetailDto.getUsername();
		final var existingUserOptional = USER_REPOSITORY.findByUsername(username);

		if(existingUserOptional.isEmpty()) {
			throw new EntityNotFoundException("Could not find a user with the username: " + username);
		}

		final var existingUser = existingUserOptional.get();
		final var existingUserProfile = existingUser.getProfile();
		final var newJobPositionId = userDetailDto.getJobPositionId();

		if(null != existingUserProfile.getJobPosition() && !existingUserProfile.getJobPosition().getId().equals(newJobPositionId)) {
			final var jobPosition = JOB_POSITION_REPOSITORY.findById(newJobPositionId).orElse(null);
			existingUserProfile.setJobPosition(jobPosition);
		}


		existingUserProfile.setName(userDetailDto.getName());
		existingUserProfile.setMiddleName(userDetailDto.getMiddleName());
		existingUserProfile.setLastName(userDetailDto.getLastName());
		existingUserProfile.setDni(userDetailDto.getDni());
		existingUserProfile.setNaf(userDetailDto.getNaf());
		existingUserProfile.setPhoneNumber(userDetailDto.getPhoneNumber());
		existingUserProfile.setDuties(userDetailDto.getDuties());
		existingUser.setEnabled(userDetailDto.isEnabled());
		existingUser.setProfile(existingUserProfile);

		existingUser.setRoles(new HashSet<>());
		Arrays.stream(userDetailDto.getRolesId()).forEach(roleId -> {
			final var role = Role.builder()
				.id(roleId)
				.build();

			existingUser.addRole(role);
		});

		return USER_REPOSITORY.save(existingUser);
	}

}
