package crm.logopedia.data.patient.service;

import crm.logopedia.data.patient.model.dto.PatientListDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.repository.PatientRepository;
import crm.logopedia.data.patient.repository.specification.PatientSpecification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
