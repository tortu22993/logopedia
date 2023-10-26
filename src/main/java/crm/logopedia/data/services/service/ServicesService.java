package crm.logopedia.data.services.service;

import crm.logopedia.data.services.model.dto.ServicesDetailDto;
import crm.logopedia.data.services.model.dto.ServicesListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicesService {
    /**
     * Obtiene un servicio según su ID.
     *
     * @param id El ID del servicio a obtener
     * @return El DTO del servicio con dicho ID
     */
    ServicesDetailDto findById(Long id);

    /**
     * Obtiene la lista completa de servicio.
     *
     * @return La lista completa de servicio
     */
    List<ServicesListDto> findAll();

    /**
     * Devuelve una lista paginada de servicio en función de un filtro.
     *
     * @param servicesListDto La plantilla que se utiliza como filtro
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada de DTO de servicios filtradas
     */
    Page<ServicesListDto> findByFilter(ServicesListDto servicesListDto, Pageable pageable);

    /**
     * Guarda un servicio en función de los datos rellenados en un DTO.
     *
     * @param servicesDetailDto EL DTO del servicio a partir del cual se guardará la entidad
     * @return El DTO del servicio guardado
     */
    ServicesDetailDto save(ServicesDetailDto servicesDetailDto);
}
