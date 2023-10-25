package crm.logopedia.util.component;


import crm.logopedia.util.abstraction.AbstractDataComponent;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.http.HttpDataResponse;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Clase contenedora de funciones y métodos para el tratamiento
 * de datos comunes de la aplicación.
 * 
 * @author Enrique Escalante
 */
@Component
public class CommonDataComponent extends AbstractDataComponent {
    
    /**
     * El nombre de la aplicación.
     */
    @Value("${server.servlet.application-display-name}")
    protected String applicationName;

    /**
     * La ruta raíz del contexto de la aplicación.
     */
    @Value("${server.servlet.context-path}")
    protected String contextPath;

    @Override
    public void setData(final Model model) {
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("baseUrl", getBaseUrl());
    }

    @Override
    public void setData(final Model model, String activeMenuPage) {
        setData(model);
        model.addAttribute("activeMenuPage", activeMenuPage);
    }

    @Override
    public void setHttpDataResponse(final String httpResponseMessage, final HttpDataResponseType httpResponseType, final Model model) {
        final var httpResponse = HttpDataResponse.builder()
            .message(httpResponseMessage)
            .type(httpResponseType)
            .build();

        model.addAttribute("httpResponse", httpResponse);
    }

    @Override
    public void setHttpDataResponse(final String httpResponseMessage, final HttpDataResponseType httpResponseType, final RedirectAttributes redirect) {
        final var httpResponse = HttpDataResponse.builder()
            .message(httpResponseMessage)
            .type(httpResponseType)
            .build();

        redirect.addFlashAttribute("httpResponse", httpResponse);
    }

    @Override
    public void setBreadcrumbOptions(final List<BreadcrumbOption> options, final Model model) {
        model.addAttribute("breadcrumbOptions", options);
    }

}
