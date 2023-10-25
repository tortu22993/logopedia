package crm.logopedia.util.http.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Establece los diferentes tipos de respuesta HTTP
 * que se utilizarán para dar feedback al usuario
 * tras realizar alguna operación.
 * 
 * @author Enrique Escalante
 */
@AllArgsConstructor
@Getter
public enum HttpDataResponseType {
    
    /**
     * El tipo de respuesta con éxito.
     */
    SUCCESS("success", "circle-check"),

    /**
     * El tipo de respuesta con error.
     */
    ERROR("danger", "circle-xmark"),

    /**
     * El tipo de respuesta con aviso.
     */
    WARNING("warning", "circle-exclamation"),

    /**
     * El tipo de repuesta con información.
     */
    INFO("info", "circle-info");

    /**
     * El valor del tipo de la respuesta.
     */
    private String type;

    /**
     * El icono a mostrar en el feedback de la respuesta.
     */
    private String icon;

}
