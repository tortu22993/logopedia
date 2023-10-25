package crm.logopedia.util.http;

import crm.logopedia.util.http.enums.HttpDataResponseType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene los datos a mostrar como feedback al usuario
 * en una respuesta HTTP.
 * 
 * @author Enrique Escalante
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HttpDataResponse {
    
    /**
     * El mensaje de la respuesta HTTP a enviar.
     */
    private String message;

    /**
     * La lista de errores de la respuesta HTTP a enviar.
     */
    @Builder.Default
    private List<String> validationResults = new ArrayList<>();

    /**
     * El tipo de respuesta HTTP a enviar.
     */
    private HttpDataResponseType type;

    /**
     * Obtiene el valor del tipo de respuesta HTTP.
     * 
     * @return El valor del tipo de respuesta HTTP
     */
    public String getType() {
        return type.getType();
    }

    /**
     * Obtiene el valor del icono a mostrar de la respuesta HTTP.
     * 
     * @return El valor del icono a mostrar de la respuesta HTTP
     */
    public String getIcon() {
        return type.getIcon();
    }

}
