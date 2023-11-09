package crm.logopedia.data.session.service;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.repository.PatientRepository;
import crm.logopedia.data.session.model.dto.SessionDetailDto;
import crm.logopedia.data.session.model.dto.SessionListDto;
import crm.logopedia.data.session.model.entity.Session;
import crm.logopedia.data.session.repository.SessionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService{

    /**
     * El repositorio de la entidad {@link Session}.
     */
    private final SessionRepository SESSION_REPOSITORY;

    /**
     * El repositorio de la entidad {@link Patient}.
     */
    private final PatientRepository PATIENT_REPOSITORY;

    /**
     * El gestor de mapeado de objetos.
     */
    private final ModelMapper MODEL_MAPPER;

    /**
     * El número máximo de resultados que una búsqueda puede tener.
     */
    @Value("${params.sql.query-params.max-results-per-search}")
    protected Integer maxResultsPerPage;

    @PostConstruct
    private void onPostConstruct() {

        // Entidad a ListDTO
        MODEL_MAPPER.typeMap(Session.class, SessionListDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getPatient().getId(), SessionListDto::setPatientId);
            mapper.map(source -> source.getPatient().getFullName(), SessionListDto::setPatientName);
            mapper.map(source -> source.getService().getId(), SessionListDto::setServiceId);
            mapper.map(source -> source.getService().getName(), SessionListDto::setServiceName);
            mapper.map(source -> source.getOwner().getUsername(), SessionListDto::setOwnerUsername);
            mapper.map(source -> source.getOwner().getProfile().getFullName(), SessionListDto::setOwnerFullName);
            // TODO: profile image
        });
        // Entidad a DetailDTO
        MODEL_MAPPER.typeMap(Session.class, SessionDetailDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getPatient().getId(), SessionDetailDto::setPatientId);
            mapper.map(source -> source.getPatient().getFullName(), SessionDetailDto::setPatientName);
            mapper.map(source -> source.getService().getId(), SessionDetailDto::setServiceId);
            mapper.map(source -> source.getService().getName(), SessionDetailDto::setServiceName);
            mapper.map(source -> source.getOwner().getUsername(), SessionDetailDto::setOwnerUsername);
            mapper.map(source -> source.getOwner().getProfile().getFullName(), SessionDetailDto::setOwnerFullName);

            mapper.map(
                    source -> source.getCreatedBy().getUsername(),
                    SessionDetailDto::setCreatedByUsername
            );

            mapper.map(
                    source -> source.getCreatedBy().getProfile().getFullName(),
                    SessionDetailDto::setCreatedByFullName
            );

            mapper.map(
                    source -> source.getLastModifiedBy().getUsername(),
                    SessionDetailDto::setLastModifiedByUsername
            );

            mapper.map(
                    source -> source.getLastModifiedBy().getProfile().getFullName(),
                    SessionDetailDto::setLastModifiedByFullName
            );


        });

        // DetailDTO a Entidad
        MODEL_MAPPER.typeMap(SessionDetailDto.class, Session.class).addMappings(mapper -> {
            mapper.<Long>map(
                    SessionDetailDto::getPatientId,
                    (destination, value) -> destination.getPatient().setId(value)
            );
            mapper.<Long>map(
                    SessionDetailDto::getServiceId,
                    (destination, value) -> destination.getService().setId(value)
            );
            mapper.<String>map(
                    SessionDetailDto::getOwnerUsername,
                    (destination, value) -> destination.getOwner().setUsername(value)
            );

        });
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDetailDto findById(Long id) {
        return SESSION_REPOSITORY.findById(id)
                .map(this::convertToDetailDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionListDto> findByFilter(SessionListDto sessionListDto, Pageable pageable) {
        final var example = Example.of(
                convertToEntity(sessionListDto),
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        final var pagination = SESSION_REPOSITORY.findAll(example, pageable);
        final var sessions = pagination.getContent().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());

        return new PageImpl<>(sessions, pageable, pagination.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SessionListDto> findByPatientId(Long patientId, Pageable pageable) {
        final var pagination = SESSION_REPOSITORY.findByPatientId(patientId, pageable);
        final var sessions = pagination.stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());

        return new PageImpl<>(sessions, pageable, pagination.getTotalElements());
    }

    @Override
    @Transactional
    public SessionDetailDto save(SessionDetailDto sessionDetailDto) {
        final var session = convertToEntity(sessionDetailDto);
        final var savedSession = SESSION_REPOSITORY.save(session);

        return convertToDetailDto(savedSession);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDetailDto getTemplateToCreateNew(PatientDetailDto patientDetailDto) {
        return SessionDetailDto.builder()
                .patientId(patientDetailDto.getId())
                .patientName(patientDetailDto.getName())
                .build();
    }

    /**
     * Transforma una instancia de la entidad {@link Session}
     * en el DTO {@link SessionListDto}.
     *
     * @param session La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private SessionListDto convertToListDto(final Session session) {
        return MODEL_MAPPER.map(session, SessionListDto.class);
    }

    /**
     * Transforma una instancia de la entidad {@link Session}
     * en el DTO {@link SessionDetailDto}.
     *
     * @param session La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private SessionDetailDto convertToDetailDto(final Session session) {
        return MODEL_MAPPER.map(session, SessionDetailDto.class);
    }

    /**
     * Transforma una instancia del DTO {@link SessionDetailDto}
     * en la entidad {@link Session}.
     *
     * @param sessionDetailDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Session convertToEntity(final SessionDetailDto sessionDetailDto) {
        return MODEL_MAPPER.map(sessionDetailDto, Session.class);
    }

    /**
     * Transforma una instancia del DTO {@link SessionListDto}
     * en la entidad {@link Session}.
     *
     * @param sessionListDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Session convertToEntity(final SessionListDto sessionListDto) {
        return MODEL_MAPPER.map(sessionListDto, Session.class);
    }
}
