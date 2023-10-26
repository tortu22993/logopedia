package crm.logopedia.data.services.controller;

import crm.logopedia.data.jobposition.model.dto.JobPositionListDto;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.service.JobPositionService;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.services.model.dto.ServicesListDto;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.services.service.ServicesService;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.pagination.PageRender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping(RequestMappings.SERVICES)
@SessionAttributes({ "servicesDetailDto" })
@RequiredArgsConstructor
@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
public class ServicesController {

    /**
     * El servicio de la entidad {@link Services}.
     */
    private final ServicesService SERVICES_SERVICE;

    /**
     * El componente de datos comunes de la aplicación.
     */
    private final CommonDataComponent COMMON_DATA_COMPONENT;

    /**
     * Valor de configuración de los registros por página a mostrar
     * en los listados.
     */
    @Value("${params.pagination.records-per-page}")
    protected Integer recordsPerPage;

    /**
     * Valor de configuración de los registros máximos que
     * se pueden mostrar por página en los listados.
     */
    @Value("${params.pagination.max-records-per-page}")
    protected Integer maxRecordsPerPage;

    /**
     * Valor del título de la sección del menú de activos.
     */
    @Value("${sections.administration}")
    protected String menuSectionTitle;

    /**
     * Valor del título de la vista del listado de servicios.
     */
    @Value("${titles.administration.services}")
    protected String viewListTitle;

    /**
     * Valor de la página seleccionada en el menú.
     */
    @Value("${params.menu.pages.administration.services}")
    protected String activeMenuPage;

    /**
     * Renderiza la vista del listado de servicios de forma paginada.
     *
     * @param serviceListDtoFilter Un filtro para limitar los resultados
     * @param page El número de página a renderizar
     * @param recordsPerPage El número de registros por página a renderizar
     * @param model El modelo de datos para añadir a la vista
     * @return El nombre de la vista a renderizar
     */
    @GetMapping({ "", "/" })
    public String renderListView(ServicesListDto serviceListDtoFilter, Model model,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(required = false) Integer recordsPerPage) {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var url = RequestMappings.SERVICES;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var services = SERVICES_SERVICE.findByFilter(serviceListDtoFilter, pageRequest);
        final var pageRender = PageRender.newInstance(url, services);

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, true, url)
        );

        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);
        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);

        model.addAttribute("serviceListDtoFilter", serviceListDtoFilter);
        model.addAttribute("page", pageRender);
        model.addAttribute("services", pageRender.getPage().getContent());
        model.addAttribute("title", viewListTitle);

        return ViewNames.SERVICE_LIST;
    }
}
