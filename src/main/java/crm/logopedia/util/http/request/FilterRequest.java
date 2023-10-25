package crm.logopedia.util.http.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import crm.logopedia.util.specification.enums.FieldType;
import crm.logopedia.util.specification.enums.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Plantilla que sirve como contenedor de datos solicitados
 * para realizar consultas de forma genérica y parametrizable
 * a la base de datos.
 * 
 * @author Enrique Escalante
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilterRequest implements Serializable {
    
    /**
     * El nombre de la columna de la tabla.
     */
    private String key;

    /**
     * El operador a utilizar en la consulta.
     */
    private Operator operator;

    /**
     * El tipo de dato a manejar.
     */
    private FieldType fieldType;

    /**
     * El valor del campo a filtrar.
     */
    private transient Object value;

    /**
     * El valor del campo a filtrar que limita hasta un rango.
     */
    private transient Object valueTo;

    /**
     * La lista de valores del campo a filtrar.
     */
    private transient List<Object> values;

}
