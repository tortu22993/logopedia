package crm.logopedia.util.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Plantilla que sirve como contenedor de filtros solicitados
 * para realizar consultas de forma genérica y parametrizable
 * a la base de datos, retornando los resultados de forma paginada.
 * 
 * @author Enrique Escalante
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {
    
    /**
     * La lista de filtros a inyectar en la consulta.
     */
    @Builder.Default
    private List<FilterRequest> filters = new ArrayList<>();

    /**
     * La lista de ordenamientos a inyectar en la consulta.
     */
    @Builder.Default
    private List<SortRequest> sorts = new ArrayList<>();

    /**
     * El número de la página que limita los resultados dentro
     * de la paginación.
     */
    private Integer page;

    /**
     * El número de resultados a mostrar.
     */
    private Integer size;

}
