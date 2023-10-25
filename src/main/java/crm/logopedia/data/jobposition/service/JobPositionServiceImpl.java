package crm.logopedia.data.jobposition.service;

import crm.logopedia.data.jobposition.model.dto.JobPositionDetailDto;
import crm.logopedia.data.jobposition.model.dto.JobPositionListDto;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.repository.JobPositionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de las funcionalidades relativas a
 * la entidad {@link JobPosition} que conectan la capa
 * de peticiones HTTP con las consultas
 * a la base de datos.
 *
 * @author Enrique Escalante
 */
@Service
@RequiredArgsConstructor
public class JobPositionServiceImpl implements JobPositionService {

	/**
	 * El repositorio de la entidad {@link JobPosition}.
	 */
	private final JobPositionRepository JOB_POSITION_REPOSITORY;

	/**
	 * El gestor de mapeado de objetos.
	 */
	private final ModelMapper MODEL_MAPPER;

	/**
	 * Ejecuta una serie de instrucciones justo después de construir el componente.
	 * TODO: entidad a DetailDto y viceversa
	 */
	@PostConstruct
	private void onPostConstruct() {
		// Entidad a ListDTO
		MODEL_MAPPER.map(JobPosition.class, JobPositionListDto.class);

		// Entidad a DetailDTO
		MODEL_MAPPER.typeMap(JobPosition.class, JobPositionDetailDto.class).addMappings(mapper -> {
			mapper.map(source -> source.getCreatedBy().getUsername(), JobPositionDetailDto::setCreatedByUsername);
			mapper.map(source -> source.getCreatedBy().getProfile().getFullName(), JobPositionDetailDto::setCreatedByFullName);
			mapper.map(source -> source.getLastModifiedBy().getUsername(), JobPositionDetailDto::setLastModifiedByUsername);
			mapper.map(source -> source.getLastModifiedBy().getProfile().getFullName(), JobPositionDetailDto::setLastModifiedByFullName);
		});

		//DetailDTO a Entidad
		MODEL_MAPPER.map(JobPositionDetailDto.class, JobPosition.class);
	}

	@Override
	@Transactional(readOnly = true)
	public JobPositionDetailDto findById(Long id) {
		return JOB_POSITION_REPOSITORY.findById(id)
			.map(this::convertToDetailDto)
			.orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobPositionListDto> findAll() {
		return JOB_POSITION_REPOSITORY.findAll().stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());
	}

	@Override
	public Page<JobPositionListDto> findByFilter(JobPositionListDto jobPositionListDto, Pageable pageable) {
		final var example = Example.of(
			convertToEntity(jobPositionListDto),
			ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
		);

		final var pagination = JOB_POSITION_REPOSITORY.findAll(example, pageable);
		final var jobPositions = pagination.getContent().stream()
			.map(this::convertToListDto)
			.collect(Collectors.toList());

		return new PageImpl<>(jobPositions, pageable, pagination.getTotalElements());
	}

	@Override
	public JobPositionDetailDto save(JobPositionDetailDto jobPositionDetailDto) {
		final var jobPosition = convertToEntity(jobPositionDetailDto);
		final var savedJobPosition = JOB_POSITION_REPOSITORY.save(jobPosition);

		return convertToDetailDto(savedJobPosition);
	}

	/**
	 * Transforma una instancia de la entidad {@link JobPosition}
	 * en el DTO {@link JobPositionListDto}.
	 *
	 * @param jobPosition La entidad a convertir
	 * @return Una nueva instancia del DTO con los datos de la entidad
	 */
	private JobPositionListDto convertToListDto(final JobPosition jobPosition) {
		return MODEL_MAPPER.map(jobPosition, JobPositionListDto.class);
	}

	/**
	 * Transforma una instancia del DTO {@link JobPositionListDto}
	 * en la entidad {@link JobPosition}.
	 *
	 * @param jobPositionListDto El DTO a convertir
	 * @return Una instancia de la entidad con los datos del DTO
	 */
	private JobPosition convertToEntity(final JobPositionListDto jobPositionListDto) {
		return MODEL_MAPPER.map(jobPositionListDto, JobPosition.class);
	}

	/**
	 * Transforma una instancia de la entidad {@link JobPosition}
	 * en el DTO {@link JobPositionDetailDto}.
	 *
	 * @param jobPosition La entidad a convertir
	 * @return Una nueva instancia del DTO con los datos de la entidad
	 */
	private JobPositionDetailDto convertToDetailDto(final JobPosition jobPosition) {
		return MODEL_MAPPER.map(jobPosition, JobPositionDetailDto.class);
	}

	/**
	 * Transforma una instancia del DTO {@link JobPositionDetailDto}
	 * en la entidad {@link JobPosition}.
	 *
	 * @param jobPositionDetailDto El DTO a convertir
	 * @return Una instancia de la entidad con los datos del DTO
	 */
	private JobPosition convertToEntity(final JobPositionDetailDto jobPositionDetailDto) {
		return MODEL_MAPPER.map(jobPositionDetailDto, JobPosition.class);
	}

}
