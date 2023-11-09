package crm.logopedia.data.patient.service;

import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.patient.model.dto.PatientListDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.repository.PatientRepository;
import crm.logopedia.data.patient.repository.specification.PatientSpecification;
import crm.logopedia.data.services.model.entity.Services;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

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
        MODEL_MAPPER.typeMap(Patient.class, PatientListDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getFullName(), PatientListDto::setFullName);
            mapper.map(source -> source.getAge(), PatientListDto::setAge);
            // TODO: profile image
        });

        // Entidad a DetailDTO
        MODEL_MAPPER.typeMap(Patient.class, PatientDetailDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getCreatedBy().getUsername(), PatientDetailDto::setCreatedByUsername);
            mapper.map(source -> source.getCreatedBy().getProfile().getFullName(), PatientDetailDto::setCreatedByFullName);
            mapper.map(source -> source.getLastModifiedBy().getUsername(), PatientDetailDto::setLastModifiedByUsername);
            mapper.map(source -> source.getLastModifiedBy().getProfile().getFullName(), PatientDetailDto::setLastModifiedByFullName);

            final Converter<Set<Services>, String> serviceToPlainNameConverter = context -> context.getSource().stream()
                    .map(Services::getName)
                    .collect(Collectors.joining(", "));
            final Converter<Set<Services>, Long[]> serviceToLongArrayConverter = context -> context.getSource().stream()
                    .map(Services::getId)
                    .toArray(Long[]::new);

            mapper.using(serviceToPlainNameConverter).map(Patient::getServices, PatientDetailDto::setPlainServices);
            mapper.using(serviceToLongArrayConverter).map(Patient::getServices, PatientDetailDto::setServicesId);
        });
    }



    /*@Override
    public Page<PatientListDto> findByFilter(PatientListDto patientListDto, Pageable pageable) {
        final var example = Example.of(
                convertToEntity(patientListDto),
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );

        final var pagination = PATIENT_REPOSITORY.findAll(example, pageable);
        final var patients = pagination.getContent().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());

        return new PageImpl<>(patients, pageable, pagination.getTotalElements());
    }*/
    @Override
    @Transactional(readOnly = true)
    public PatientDetailDto findById(Long id) {
        return PATIENT_REPOSITORY.findById(id)
                .map(this::convertToDetailDto)
                .orElse(null);
    }

    @Override
    public PatientDetailDto save(PatientDetailDto patientDetailDto) throws MessagingException {
        final var patient = patientDetailDto.getFullName();
        final var savedPatient = StringUtils.hasLength(patient)
                ? updatePatient(patientDetailDto)
                : createPatient(patientDetailDto);

        return convertToDetailDto(savedPatient);
    }

    @Override
    public Page<PatientListDto> findByFilter(String name, String school, Pageable pageable) {
        Specification<Patient> spec = new PatientSpecification(name, school);
        Page<Patient> result = PATIENT_REPOSITORY.findAll(spec, pageable);
        return result.map(this::convertToListDto);
    }

    @Override
    public List<PatientListDto> findAll() {
        return PATIENT_REPOSITORY.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Transforma una instancia de la entidad {@link Patient}
     * en el DTO {@link PatientListDto}.
     *
     * @param patient La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private PatientListDto convertToListDto(final Patient patient) {
        return MODEL_MAPPER.map(patient, PatientListDto.class);
    }

    private PatientDetailDto convertToDetailDto(final Patient patient) {
        return MODEL_MAPPER.map(patient, PatientDetailDto.class);
    }

    /**
     * Transforma una instancia del DTO {@link PatientListDto}
     * en la entidad {@link Patient}.
     *
     * @param patientListDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Patient convertToEntity(final PatientListDto patientListDto) {
        return MODEL_MAPPER.map(patientListDto, Patient.class);
    }

    private Patient convertToEntity(final PatientDetailDto patientDetailDto) {
        return MODEL_MAPPER.map(patientDetailDto, Patient.class);
    }

    /**
     * Crea un nuevo paciente en la base de datos.
     *
     * @param patientDetailDto El DTO del detalle del paciente que contiene los datos
     * @return El àciente creado
     * @exception MessagingException Si ocurre alguna excepción al enviar el correo electrónico
     */
    private Patient createPatient(final PatientDetailDto patientDetailDto) throws MessagingException {
        final var createdUser = PATIENT_REPOSITORY.save(convertToEntity(patientDetailDto));

        Arrays.stream(patientDetailDto.getServicesId()).forEach(serviceId -> {
            final var service = Services.builder()
                    .id(serviceId)
                    .build();

            createdUser.addService(service);
        });


        return createdUser;
    }

    /**
     * Actualiza un usuario en la base de datos con base a su nombre de usuario.
     *
     * @param patientDetailDto El DTO de detalle del usuario que contiene los datos a actualizar
     * @return El usuario actualizado
     */
    private Patient updatePatient(final PatientDetailDto patientDetailDto) {
        final var patient = patientDetailDto.getId();
        final var existingPatientOptional = PATIENT_REPOSITORY.findById(patient);

        if(existingPatientOptional.isEmpty()) {
            throw new EntityNotFoundException("Could not find a user with the id: " + patient);
        }

        final var existingPatient = existingPatientOptional.get();


        existingPatient.setName(patientDetailDto.getName());
        existingPatient.setMiddleName(patientDetailDto.getMiddleName());
        existingPatient.setLastName(patientDetailDto.getLastName());
        existingPatient.setSchool(patientDetailDto.getSchool());
        existingPatient.setBirthdate(patientDetailDto.getBirthdate());
        existingPatient.setObservations(patientDetailDto.getObservations());
        existingPatient.setLopd(patientDetailDto.isLopd());
        existingPatient.setEnabled(patientDetailDto.isEnabled());
        existingPatient.setSchoolCordination(patientDetailDto.isSchoolCordination());

        existingPatient.setServices(new HashSet<>());
        Arrays.stream(patientDetailDto.getServicesId()).forEach(serviceId -> {
            final var service = Services.builder()
                    .id(serviceId)
                    .build();

            existingPatient.addService(service);
        });

        return PATIENT_REPOSITORY.save(existingPatient);
    }
}
