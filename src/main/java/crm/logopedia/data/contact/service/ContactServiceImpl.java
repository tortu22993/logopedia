package crm.logopedia.data.contact.service;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.contact.model.dto.ContactListDto;
import crm.logopedia.data.contact.model.entity.Contact;
import crm.logopedia.data.contact.repository.ContactRepository;
import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de las funcionalidades relativas a
 * la entidad {@link Contact} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    /**
     * El repositorio de la entidad {@link Contact}.
     */
    private final ContactRepository CONTACT_REPOSITORY;

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

    /**
     * Ejecuta una serie de instrucciones justo después de construir el componente.
     */
    @PostConstruct
    private void onPostConstruct() {
        // Entidad a DetailDTO
        MODEL_MAPPER.typeMap(Contact.class, ContactDetailDto.class).addMappings(mapper -> {
            mapper.map(source -> source.getPatient().getId(), ContactDetailDto::setPatientId);
            mapper.map(source -> source.getPatient().getName(), ContactDetailDto::setPatientName);

            mapper.<String>map(
                    Contact::getCountry,
                    (destination, value) -> destination.getLocation().setCountry(value)
            );

            mapper.<String>map(
                    Contact::getProvince,
                    (destination, value) -> destination.getLocation().setProvince(value)
            );

            mapper.<String>map(
                    Contact::getMunicipality,
                    (destination, value) -> destination.getLocation().setMunicipality(value)
            );

            mapper.<String>map(
                    Contact::getZipCode,
                    (destination, value) -> destination.getLocation().setZipCode(value)
            );

            mapper.<String>map(
                    Contact::getAddress,
                    (destination, value) -> destination.getLocation().setAddress(value)
            );

            mapper.map(
                    source -> source.getCreatedBy().getUsername(),
                    ContactDetailDto::setCreatedByUsername
            );

            mapper.map(
                    source -> source.getCreatedBy().getProfile().getFullName(),
                    ContactDetailDto::setCreatedByFullName
            );

            mapper.map(
                    source -> source.getLastModifiedBy().getUsername(),
                    ContactDetailDto::setLastModifiedByUsername
            );

            mapper.map(
                    source -> source.getLastModifiedBy().getProfile().getFullName(),
                    ContactDetailDto::setLastModifiedByFullName
            );
        });

        // DetailDTO a Entidad
        MODEL_MAPPER.typeMap(ContactDetailDto.class, Contact.class).addMappings(mapper -> {
            final Converter<String, String> toUpperCaseConverter = context -> context.getSource().toUpperCase();

            mapper.using(toUpperCaseConverter).map(ContactDetailDto::getName, Contact::setName);
            mapper.using(toUpperCaseConverter).map(ContactDetailDto::getMiddleName, Contact::setMiddleName);
            mapper.using(toUpperCaseConverter).map(ContactDetailDto::getLastName, Contact::setLastName);

            mapper.<Long>map(
                    ContactDetailDto::getPatientId,
                    (destination, value) -> destination.getPatient().setId(value)
            );

            mapper.using(toUpperCaseConverter).map(
                    source -> source.getLocation().getCountry(),
                    Contact::setCountry
            );

            mapper.using(toUpperCaseConverter).map(
                    source -> source.getLocation().getProvince(),
                    Contact::setProvince
            );

            mapper.using(toUpperCaseConverter).map(
                    source -> source.getLocation().getMunicipality(),
                    Contact::setMunicipality
            );

            mapper.using(toUpperCaseConverter).map(
                    source -> source.getLocation().getZipCode(),
                    Contact::setZipCode
            );

            mapper.using(toUpperCaseConverter).map(
                    source -> source.getLocation().getAddress(),
                    Contact::setAddress
            );
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDetailDto findById(Long id) {
        return CONTACT_REPOSITORY.findById(id)
                .map(this::convertToDetailDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactListDto> findByPatientId(Long patientId, Pageable pageable) {
        final var pagination = CONTACT_REPOSITORY.findByPatientId(patientId, pageable);
        final var contacts = pagination.stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());

        return new PageImpl<>(contacts, pageable, pagination.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDetailDto getTemplateToCreateNew(PatientDetailDto patientDetailDto) {

        return ContactDetailDto.builder()
                .patientId(patientDetailDto.getId())
                .patientName(patientDetailDto.getName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactListDto> search(String text) {
        final var pageRequest = PageRequest.of(0, maxResultsPerPage);

        return CONTACT_REPOSITORY.search(text, pageRequest).stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactDetailDto save(ContactDetailDto contactDetailDto) {
        final var contact = convertToEntity(contactDetailDto);
        final var savedContact = CONTACT_REPOSITORY.save(contact);

        return convertToDetailDto(savedContact);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        /*
         * Una vez eliminadas las referencias con otras entidades, podemos
         * eliminar el registro de la base de datos.
         */
        CONTACT_REPOSITORY.findById(id).ifPresent(CONTACT_REPOSITORY::delete);
    }

    /**
     * Transforma una instancia de la entidad {@link Contact}
     * en el DTO {@link ContactListDto}.
     *
     * @param contact La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private ContactListDto convertToListDto(final Contact contact) {
        return MODEL_MAPPER.map(contact, ContactListDto.class);
    }

    /**
     * Transforma una instancia de la entidad {@link Contact}
     * en el DTO {@link ContactDetailDto}.
     *
     * @param contact La entidad a convertir
     * @return Una nueva instancia del DTO con los datos de la entidad
     */
    private ContactDetailDto convertToDetailDto(final Contact contact) {
        return MODEL_MAPPER.map(contact, ContactDetailDto.class);
    }

    /**
     * Transforma una instancia del DTO {@link ContactDetailDto}
     * en la entidad {@link Contact}.
     *
     * @param contactDetailDto El DTO a convertir
     * @return Una instancia de la entidad con los datos del DTO
     */
    private Contact convertToEntity(final ContactDetailDto contactDetailDto) {
        return MODEL_MAPPER.map(contactDetailDto, Contact.class);
    }

}
