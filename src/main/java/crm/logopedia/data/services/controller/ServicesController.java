package crm.logopedia.data.services.controller;

import crm.logopedia.data.jobposition.model.dto.JobPositionDetailDto;
import crm.logopedia.data.jobposition.model.dto.JobPositionListDto;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.service.JobPositionService;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.services.model.dto.ServicesDetailDto;
import crm.logopedia.data.services.model.dto.ServicesListDto;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.services.service.ServicesService;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import crm.logopedia.util.pagination.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * Renderiza la vista del detalle de un servicio según su ID.
     *
     * @param id El ID del servicio
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el puesto de trabajo solicitado
     */
    @GetMapping("/{id}")
    public String renderDetailViewToSee(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var servicesDetailDto = SERVICES_SERVICE.findById(id);

        if (servicesDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                servicesDetailDto,
                model,
                servicesDetailDto.getName()
        );

        return ViewNames.SERVICE_DETAIL;
    }

    /**
     * Renderiza la vista del detalle para crear un nuevo servicio.
     *
     * @param model El modelo de datos para añadir a la vista
     * @return El nombre de la vista a renderizar
     */
    @GetMapping("/action/new")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
    public String renderDetailViewToCreate(Model model) {
        final var servicesDetailDto = new ServicesDetailDto();

        setDetailPageData(
                servicesDetailDto,
                model,
                "Nuevo servicio"
        );

        return ViewNames.SERVICE_EDITION;
    }

    /**
     * Renderiza la vista de edición de un servicio según su ID.
     *
     * @param id El ID del servicio
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el puesto de trabajo solicitado
     */
    @GetMapping("/{id}/action/edit")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
    public String renderDetailViewToEdit(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var serviceDetailDto = SERVICES_SERVICE.findById(id);

        if(serviceDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                serviceDetailDto,
                model,
                serviceDetailDto.getName()
        );

        return ViewNames.SERVICE_EDITION;
    }

    /**
     * Valida la estructura del DTO de un servicio y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param servicesDetailDto El DTO del servicio a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param sessionStatus El estado de la sesión
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/action/save")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
    public String save(@Valid ServicesDetailDto servicesDetailDto, BindingResult result,
                       Model model, SessionStatus sessionStatus, RedirectAttributes redirect) {
        if(result.hasErrors()) {
            setDetailPageData(
                    servicesDetailDto,
                    model,
                    servicesDetailDto.getName()
            );

            return ViewNames.SERVICE_EDITION;
        }

        final var savedServiceDetailDto = SERVICES_SERVICE.save(servicesDetailDto);
        sessionStatus.setComplete();

        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "El servicio ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.SERVICES,
                "/",
                savedServiceDetailDto.getId().toString()
        );
    }

    /**
     * Rellena los datos comunes a la vista de detalle de un servicio.
     *
     * @param servicesDetailDto El DTO del servicio a renderizar
     * @param model El modelo de datos a renderizar
     * @param viewTitle El título de la vista a renderizar
     */
    private void setDetailPageData(ServicesDetailDto servicesDetailDto, Model model, String viewTitle) {
        final var rootEndpoint = RequestMappings.SERVICES;
        final var title = servicesDetailDto.getId() != null
                ? (!viewTitle.isBlank()
                ? viewTitle
                : "Editar servicio"
        )
                : "Nuevo servicio";

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, false, rootEndpoint),
                new BreadcrumbOption(title, true, rootEndpoint + "/" + servicesDetailDto.getId())
        );

        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

        model.addAttribute("servicesDetailDto", servicesDetailDto);
        model.addAttribute("title", title.toUpperCase());
    }
}
