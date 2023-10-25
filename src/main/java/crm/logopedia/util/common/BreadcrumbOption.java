package crm.logopedia.util.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que sirve como plantilla para definir las diferentes migas de pan
 * de una vista específica.
 * 
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BreadcrumbOption {
    
    /**
     * El título de la opción de las migas de pan.
     */
    private String title;

    /**
     * Indica si la opción de las migas de pan está activa o no.
     */
    private boolean active;

    /**
     * Indica la URL a la que redirige la opción de las migas de pan.
     */
    private String url;

}
