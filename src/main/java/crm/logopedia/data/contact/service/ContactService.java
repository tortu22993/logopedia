package crm.logopedia.data.contact.service;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.contact.model.dto.ContactListDto;
import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    /**
     * Obtiene el DTO de un contacto según su ID.
     *
     * @param id El ID del contacto a obtener
     * @return El DTO del contacto con dicho ID
     */
    ContactDetailDto findById(Long id);

    /**
     * Obtiene una lista paginada de DTO de contactos según el ID del paciente
     * al que están asociados.
     *
     * @param patientId El ID del cliente al que pertenece el contacto
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada de DTO con los contactos relacionados al paciente con dicho ID
     */
    Page<ContactListDto> findByPatientId(Long patientId, Pageable pageable);

    /**
     * Obtiene la plantilla de un DTO para crear un nuevo contacto a partir de un
     * DTO del paciente al cual estará asociado.
     *
     * @param patientDetailDto El DTO de detalle del paciente al que se asociará el contacto
     * @return El DTO rellenado con datos necesarios para crear el contacto
     */
    ContactDetailDto getTemplateToCreateNew(PatientDetailDto patientDetailDto);

    /**
     * Busca una serie de contactos en función de un texto introducido por
     * el usuario.
     *
     * @param text El texto que se utiliza como filtro
     * @return Una lista de DTO de contactos cuyo nombre coincide con el filtro
     */
    List<ContactListDto> search(String text);

    /**
     * Guarda un contacto en función de los datos rellenados en un DTO.
     *
     * @param contactDetailDto EL DTO del contacto a partir del cual se guardará la entidad
     * @return El DTO del contacto guardado
     */
    ContactDetailDto save(ContactDetailDto contactDetailDto);

    /**
     * Elimina un contacto según su ID.
     *
     * @param id El ID del contacto a eliminar
     */
    void deleteById(Long id);
}
