package crm.logopedia.data.jobposition.service;


import crm.logopedia.data.jobposition.model.dto.JobPositionDetailDto;
import crm.logopedia.data.jobposition.model.dto.JobPositionListDto;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Declaración de las funcionalidades relativas a
 * la entidad {@link JobPosition} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
public interface JobPositionService {

	/**
	 * Obtiene un puesto de trabajo según su ID.
	 *
	 * @param id El ID del puesto de trabajo a obtener
	 * @return El DTO del puesto de trabajo con dicho ID
	 */
	JobPositionDetailDto findById(Long id);

	/**
	 * Obtiene la lista completa de puestos de trabajo.
	 *
	 * @return La lista completa de puestos de trabajo
	 */
	List<JobPositionListDto> findAll();

	/**
	 * Devuelve una lista paginada de puestos de trabajo en función de un filtro.
	 *
	 * @param jobPositionListDto La plantilla que se utiliza como filtro
	 * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
	 * @return Una lista paginada de DTO de equipos filtradas
	 */
	Page<JobPositionListDto> findByFilter(JobPositionListDto jobPositionListDto, Pageable pageable);

	/**
	 * Guarda un puesto de trabajo en función de los datos rellenados en un DTO.
	 *
	 * @param jobPositionDetailDto EL DTO del puesto de trabajo a partir del cual se guardará la entidad
	 * @return El DTO del puesto de trabajo guardado
	 */
	JobPositionDetailDto save(JobPositionDetailDto jobPositionDetailDto);

}
