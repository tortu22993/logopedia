package crm.logopedia.data.patient.service;

import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.patient.model.dto.PatientListDto;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {

    /**
     * Devuelve una lista paginada de pacientes en función de un filtro.
     *
     //* @param patientListDto La plantilla que se utiliza como filtro
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada de DTO de usuarios filtrados
     */
    //Page<PatientListDto> findByFilter(PatientListDto patientListDto, Pageable pageable);

    Page<PatientListDto> findByFilter( String name, String school, Pageable pageable);

    /**
     * Obtiene la lista completa de pacientes.
     *
     * @return La lista completa de usuarios
     */
    List<PatientListDto> findAll();

    PatientDetailDto findById(Long id);

    PatientDetailDto save(PatientDetailDto patientDetailDto) throws MessagingException;
}
