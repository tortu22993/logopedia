package crm.logopedia.data.role.service;

import crm.logopedia.data.role.model.dto.RoleDto;
import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.data.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de las funcionalidades relativas a
 * la entidad {@link Role} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	/**
	 * El repositorio de la entidad {@link Role}.
	 */
	private final RoleRepository ROLE_REPOSITORY;

	/**
	 * El gestor de mapeado de objetos.
	 */
	private final ModelMapper MODEL_MAPPER;

	@Override
	@Transactional(readOnly = true)
	public List<RoleDto> findAll() {
		return ROLE_REPOSITORY.findAll().stream()
			.map(this::convertToDto)
			.collect(Collectors.toList());
	}

	/**
	 * Transforma una instancia de la entidad {@link Role}
	 * en el DTO {@link RoleDto}.
	 *
	 * @param role La entidad a convertir
	 * @return Una nueva instancia del DTO con los datos de la entidad
	 */
	private RoleDto convertToDto(final Role role) {
		return MODEL_MAPPER.map(role, RoleDto.class);
	}

}
