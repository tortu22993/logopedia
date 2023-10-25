package crm.logopedia.util.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import crm.logopedia.util.specification.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Plantilla que sirve como contenedor de ordenamientos
 * a realizar en consultas contra la base de datos.
 * 
 * @author Enrique Escalante
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SortRequest implements Serializable {
    
    /**
     * El nombre de la columna de la tabla.
     */
    private String key;

    /**
     * La dirección de ordenamiento.
     */
    private SortDirection direction;

}
