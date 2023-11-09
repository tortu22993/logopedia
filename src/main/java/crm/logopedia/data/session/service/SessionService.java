package crm.logopedia.data.session.service;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.session.model.dto.SessionDetailDto;
import crm.logopedia.data.session.model.dto.SessionListDto;
import crm.logopedia.data.user.model.dto.UserListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionService {

    /**
     * Obtiene el DTO de una session según su ID.
     *
     * @param id El ID del session a obtener
     * @return El DTO del session con dicho ID
     */
    SessionDetailDto findById(Long id);

    Page<SessionListDto> findByFilter(SessionListDto sessionListDto, Pageable pageable);

    /**
     * Obtiene una lista paginada de DTO de sessions según el ID del paciente
     * al que están asociados.
     *
     * @param patientId El ID del cliente al que pertenece la session
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada de DTO con los session relacionados al paciente con dicho ID
     */
    Page<SessionListDto> findByPatientId(Long patientId, Pageable pageable);

    /**
     * Guarda una session en función de los datos rellenados en un DTO.
     *
     * @param sessionDetailDto EL DTO del session a partir del cual se guardará la entidad
     * @return El DTO del session guardado
     */
    SessionDetailDto save(SessionDetailDto sessionDetailDto);

    /**
     * Obtiene la plantilla de un DTO para crear un nueva session a partir de un
     * DTO del paciente al cual estará asociado.
     *
     * @param patientDetailDto El DTO de detalle del paciente al que se asociará el contacto
     * @return El DTO rellenado con datos necesarios para crear el contacto
     */
    SessionDetailDto getTemplateToCreateNew(PatientDetailDto patientDetailDto);


}
