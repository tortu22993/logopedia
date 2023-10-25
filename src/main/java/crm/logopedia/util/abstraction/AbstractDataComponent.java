package crm.logopedia.util.abstraction;

import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Contiene métodos para establecer datos.
 * 
 * @author Enrique Escalante
 */
public abstract class AbstractDataComponent {
    
    /**
     * Establece una serie de datos estáticos a un modelo.
     * 
     * @param model El modelo en el que se establecerán los datos
     */
    protected abstract void setData(final Model model);

    /**
     * Establece una serie de datos estáticos a un modelo.
     * 
     * @param model El modelo en el que se establecerán los datos
     * @param activeMenuPage El valor de la página seleccionada en el menú
     */
    protected abstract void setData(final Model model, String activeMenuPage);

    /**
     * Establece una respuesta HTTP para dar feedback al usuario
     * dentro de un modelo de datos.
     *
     * @param httpResponseMessage El mensaje de la respuesta HTTP
     * @param httpResponseType El tipo de respuesta HTTP
     * @param model El modelo de datos que contendrá la respuesta HTTP
     */
    protected abstract void setHttpDataResponse(
        final String httpResponseMessage,
        final HttpDataResponseType httpResponseType,
        final Model model
    );

    /**
     * Establece una respuesta HTTP para dar feedback al usuario
     * tras realizar una redirección a una URL.
     * 
     * @param httpResponseMessage El mensaje de la respuesta HTTP
     * @param httpResponseType El tipo de respuesta HTTP
     * @param redirect El gestor de redirección de atributos
     */
    protected abstract void setHttpDataResponse(
        final String httpResponseMessage,
        final HttpDataResponseType httpResponseType,
        final RedirectAttributes redirect
    );

    /**
     * Estable las migas de pan de una vista concreta a un modelo.
     * 
     * @param options Las opciones de migas de pan a establecer
     * @param model El modelo en el que se establecerán los datos
     */
    protected abstract void setBreadcrumbOptions(
        final List<BreadcrumbOption> options,
        final Model model
    );

    /**
     * Obtiene la URL base en la que se ejecuta el contexto de la aplicación.
     *
     * @return La URL base
     */
    public final String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .build()
            .toUriString();
    }

}
