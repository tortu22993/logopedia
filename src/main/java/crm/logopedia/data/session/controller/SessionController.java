package crm.logopedia.data.session.controller;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.patient.model.dto.PatientListDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.service.PatientService;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.services.service.ServicesService;
import crm.logopedia.data.session.model.dto.SessionDetailDto;
import crm.logopedia.data.session.model.dto.SessionListDto;
import crm.logopedia.data.session.model.entity.Session;
import crm.logopedia.data.session.service.SessionService;
import crm.logopedia.data.user.model.dto.UserDetailDto;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.service.UserService;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(RequestMappings.SESSIONS)
@SessionAttributes({ "sessionDetailDto" })
@RequiredArgsConstructor
@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
public class SessionController {

    /**
     * El servicio de la entidad {@link Session}.
     */
    private final SessionService SESSION_SERVICE;

    /**
     * El servicio de la entidad {@link Services}.
     */
    private final ServicesService SERVICES_SERVICE;

    /**
     * El servicio de la entidad {@link User}.
     */
    private final UserService USER_SERVICE;

    /**
     * El servicio de la entidad {@link Patient}.
     */
    private final PatientService PATIENT_SERVICE;


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
     * Valor del título de la sección del menú de contactos.
     */
    @Value("${sections.management}")
    protected String menuSectionTitle;

    /**
     * Valor del título de la vista del listado de contactos.
     */
    @Value("${titles.management.sessions}")
    protected String viewListTitle;

    /**
     * Valor del título de la vista del listado de contactos.
     */
    @Value("${titles.management.sessions}")
    protected String customersViewListTile;

    /**
     * Valor de la página seleccionada en el menú.
     */
    @Value("${params.menu.pages.management.sessions}")
    protected String activeMenuPage;

    @GetMapping({ "", "/" })
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
    public String renderListView(SessionListDto sessionListDtoFilter,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(required = false) Integer recordsPerPage, Model model) {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var url = RequestMappings.SESSIONS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var sessions = SESSION_SERVICE.findByFilter(sessionListDtoFilter,  pageRequest);
        final var pageRender = PageRender.newInstance(url, sessions);

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, true, url)
        );

        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);
        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);

        model.addAttribute("sessionListDtoFilter", sessionListDtoFilter);
        model.addAttribute("page", pageRender);
        model.addAttribute("sessions", pageRender.getPage().getContent());
        model.addAttribute("title", viewListTitle);

        return ViewNames.SESSION_LIST;
    }

    @GetMapping("/{id}")
    public String renderDetailViewToSee(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var sessionDetailDto = SESSION_SERVICE.findById(id);

        if(sessionDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                sessionDetailDto,
                model,
                String.valueOf(sessionDetailDto.getId()),
                null
        );

        return ViewNames.SESSION_DETAIL;
    }

    @GetMapping("/action/new")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
    public String renderDetailViewToCreate(Model model) {
        final var sessionDetailDto = new SessionDetailDto();

        setDetailPageData(
                sessionDetailDto,
                model,
                "Nueva sesion",
                getEditionParams()
        );

        return ViewNames.SESSION_EDITION;
    }

    /**
     * Renderiza la vista de edición de un session según su ID.
     *
     * @param id El ID del session
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el session solicitado
     */
    @GetMapping("/{id}/action/edit")
    public String renderDetailViewToEdit(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var sessionDetailDto = SESSION_SERVICE.findById(id);

        if(sessionDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                sessionDetailDto,
                model,
                String.valueOf(sessionDetailDto.getId()),
                getEditionParams()
        );

        return ViewNames.SESSION_EDITION;
    }


    /**
     * Valida la estructura del DTO de un SESSION y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param sessionDetailDto El DTO del SESSION a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param sessionStatus El estado de la sesión
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/action/save")
    public String save(@Valid SessionDetailDto sessionDetailDto, BindingResult result,
                       Model model, SessionStatus sessionStatus,
                       RedirectAttributes redirect) {
        if(result.hasErrors()) {
            setDetailPageData(
                    sessionDetailDto,
                    model,
                    String.valueOf(sessionDetailDto.getId()),
                    getEditionParams()
            );

            return ViewNames.SESSION_EDITION;
        }

        final var savedSessionDetailDto = SESSION_SERVICE.save(sessionDetailDto);
        sessionStatus.setComplete();

        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "La session ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.SESSIONS,
                "/",
                savedSessionDetailDto.getId().toString()
        );
    }

    private void setDetailPageData(SessionDetailDto sessionDetailDto, Model model, String viewTitle, Map<String, ?> params) {
        final var rootEndpoint = RequestMappings.SESSIONS;
        final var title = StringUtils.hasLength(String.valueOf(sessionDetailDto.getId()))
                ? (!viewTitle.isBlank()
                ? viewTitle
                : "Editar session"
        )
                : "Nueva session";

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, false, rootEndpoint),
                new BreadcrumbOption(title, true, rootEndpoint + "/" + sessionDetailDto.getId())
        );

        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

        model.addAttribute("sessionDetailDto", sessionDetailDto);
        model.addAttribute("title", title.toUpperCase());

        if(params != null) {
            params.forEach(model::addAttribute);
        }
    }

    /**
     * Obtiene una colección de parámetros relativos a la
     * pantalla de edición.
     *
     * @return Una colección de parámetros relativos a la pantalla de edición.
     */
    private Map<String, ?> getEditionParams() {
        return Map.of(
                "services",
                SERVICES_SERVICE.findAll(),
                "users",
                USER_SERVICE.findAll(),
                "patients",
                PATIENT_SERVICE.findAll()
        );
    }
}
