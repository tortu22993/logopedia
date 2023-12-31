package crm.logopedia.data.patient.controller;

import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.contact.model.entity.Contact;
import crm.logopedia.data.contact.service.ContactService;
import crm.logopedia.data.patient.model.dto.PatientDetailDto;
import crm.logopedia.data.patient.model.dto.PatientListDto;
import crm.logopedia.data.patient.model.entity.Patient;
import crm.logopedia.data.patient.service.PatientService;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.services.service.ServicesService;
import crm.logopedia.data.session.model.dto.SessionDetailDto;
import crm.logopedia.data.session.model.entity.Session;
import crm.logopedia.data.session.service.SessionService;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.service.UserService;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import crm.logopedia.util.pagination.PageRender;
import jakarta.mail.MessagingException;
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
import java.util.Map;

@Controller
@RequestMapping(RequestMappings.PATIENTS)
@SessionAttributes({ "patientDetailDto" })
@RequiredArgsConstructor
public class PatientController {
    /**
     * El servicio de la entidad {@link Patient}.
     */
    private final PatientService PATIENT_SERVICE;

    /**
     * El servicio de la entidad {@link Contact}.
     */
    private final ContactService CONTACT_SERVICE;

    /**
     * El servicio de la entidad {@link Session}.
     */
    private final SessionService SESSION_SERVICE;

    /**
     * El servicio de la entidad {@link User}.
     */
    private final UserService USER_SERVICE;

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
     * Valor del título de la sección del menú de usuarios.
     */
    @Value("${sections.management}")
    protected String menuSectionTitle;

    /**
     * Valor del título de la vista del listado de usuarios.
     */
    @Value("${titles.management.patients}")
    protected String viewListTitle;

    /**
     * Valor de la página seleccionada en el menú.
     */
    @Value("${params.menu.pages.management.patients}")
    protected String activeMenuPage;

    /**
     * Renderiza la vista del listado de usuarios de forma paginada.
     *
     * @param patientListDtoFilter Un filtro para limitar los resultados
     * @param page El número de página a renderizar
     * @param recordsPerPage El número de registros por página a renderizar
     * @param model El modelo de datos para añadir a la vista
     * @return El nombre de la vista a renderizar
     */
    @GetMapping({ "", "/" })
    public String renderListView(PatientListDto patientListDtoFilter,
                                 @RequestParam(name = "school", required = false) String school,
                                 @RequestParam(name = "name", required = false) String name,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(required = false) Integer recordsPerPage, Model model) {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var url = RequestMappings.PATIENTS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var patientsCrit = PATIENT_SERVICE.findByFilter(name, school,  pageRequest);
        final var pageRender = PageRender.newInstance(url, patientsCrit);

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, true, url)
        );

        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);
        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);

        model.addAttribute("patientListDtoFilter", patientListDtoFilter);
        model.addAttribute("page", pageRender);
        model.addAttribute("patients", pageRender.getPage().getContent());
        model.addAttribute("title", viewListTitle);

        return ViewNames.PATIENT_LIST;
    }

    /**
     * Renderiza la vista del detalle de un paciebte según su id
     *
     * @param id El id del paciente
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el usuario solicitado
     */
    @GetMapping("/{id}")
    public String renderDetailViewToSee(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getFullName(),
                null,
                null
        );

        return ViewNames.PATIENT_DETAIL;
    }

    /**
     * Renderiza la vista del detalle para crear un nuevo paciente.
     *
     * @param model El modelo de datos para añadir a la vista
     * @return El nombre de la vista a renderizar
     */
    @GetMapping("/action/new")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
    public String renderDetailViewToCreate(Model model) {
        final var patientDetailDto = new PatientDetailDto();

        setDetailPageData(
                patientDetailDto,
                model,
                "Nuevo Paciente",
                getEditionParams(),
                null
        );

        return ViewNames.PATIENT_EDITION;
    }

    /**
     * Renderiza la vista de edición de un paciente según su ID.
     *
     * @param id El ID del paciente
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el puesto de trabajo solicitado
     */
    @GetMapping("/{id}/action/edit")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
    public String renderDetailViewToEdit(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "details"
        );

        return ViewNames.PATIENT_EDITION;
    }

    /**
     * Valida la estructura del DTO de un PACIENTE y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param patientDetailDto El DTO del paciente a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param sessionStatus El estado de la sesión
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/action/save")
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
    public String save(@Valid PatientDetailDto patientDetailDto, BindingResult result,
                       Model model, SessionStatus sessionStatus, RedirectAttributes redirect) throws MessagingException {
        if(result.hasErrors()) {
            setDetailPageData(
                    patientDetailDto,
                    model,
                    patientDetailDto.getName(),
                    getEditionParams(),
                    "details"
            );

            return ViewNames.PATIENT_EDITION;
        }

        final var savedPatientDetailDto = PATIENT_SERVICE.save(patientDetailDto);
        sessionStatus.setComplete();

        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "El paciente ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.PATIENTS,
                "/",
                savedPatientDetailDto.getId().toString()
        );
    }

    /**
     * Renderiza la vista del detalle de un paciente según su ID
     * junto con una lista paginada de sus contactos.
     *
     * @param id El ID del paciente
     * @param page El número de página a renderizar
     * @param recordsPerPage El número de registros por página a renderizar
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el cliente solicitado
     */
    @GetMapping("/{id}/contacts")
    public String renderDetailViewWithContactsTab(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(required = false) Integer recordsPerPage, Model model,
                                                  HttpServletRequest request) throws NoHandlerFoundException {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        final var url = RequestMappings.PATIENTS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var contacts = CONTACT_SERVICE.findByPatientId(patientDetailDto.getId(), pageRequest);
        final var pageRender = PageRender.newInstance(url, contacts);

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "contacts"
        );

        model.addAttribute("page", pageRender);
        model.addAttribute("contacts", pageRender.getPage().getContent());

        return ViewNames.PATIENT_DETAIL;
    }

    /**
     * Renderiza la vista del detalle de un paciente según su
     * ID junto con una plantilla vacía para crear un nuevo contacto.
     *
     * @param id El ID del cliente
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el cliente solicitado
     */
    @GetMapping("/{id}/contacts/new")
    public String renderDetailViewToCreateContact(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "contacts"
        );

        model.addAttribute("contactDetailDto", CONTACT_SERVICE.getTemplateToCreateNew(patientDetailDto));

        return ViewNames.PATIENT_DETAIL;
    }

    /**
     * Valida la estructura del DTO de un contacto y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param contactDetailDto El DTO del contacto a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/{id}/contacts/save")
    public String saveContact(@Valid ContactDetailDto contactDetailDto, BindingResult result,
                              Model model, RedirectAttributes redirect) {
        final var patientId = contactDetailDto.getPatientId();

        if(result.hasErrors()) {
            final var patientDetailDto = PATIENT_SERVICE.findById(patientId);

            setDetailPageData(
                    patientDetailDto,
                    model,
                    patientDetailDto.getName(),
                    getEditionParams(),
                    "contacts"
            );

            return ViewNames.PATIENT_DETAIL;
        }

        CONTACT_SERVICE.save(contactDetailDto);
        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "El contacto ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.PATIENTS,
                "/",
                contactDetailDto.getPatientId().toString(),
                RequestMappings.CONTACTS
        );
    }

    @GetMapping("/{id}/sessions")
    public String renderDetailViewWithSessionsTab(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(required = false) Integer recordsPerPage, Model model,
                                                  HttpServletRequest request) throws NoHandlerFoundException {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        final var url = RequestMappings.PATIENTS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var sessions = SESSION_SERVICE.findByPatientId(patientDetailDto.getId(), pageRequest);
        final var pageRender = PageRender.newInstance(url, sessions);

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "sessions"
        );

        model.addAttribute("page", pageRender);
        model.addAttribute("sessions", pageRender.getPage().getContent());

        return ViewNames.PATIENT_DETAIL;
    }

    /**
     * Renderiza la vista del detalle de un paciente según su
     * ID junto con una plantilla vacía para crear un nuevo contacto.
     *
     * @param id El ID del cliente
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el cliente solicitado
     */
    @GetMapping("/{id}/sessions/new")
    public String renderDetailViewToCreateSession(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "sessions"
        );

        model.addAttribute("sessionDetailDto", SESSION_SERVICE.getTemplateToCreateNew(patientDetailDto));

        return ViewNames.PATIENT_DETAIL;
    }

    /**
     * Valida la estructura del DTO de un contacto y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param sessionDetailDto El DTO del contacto a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/{id}/sessions/save")
    public String saveSession(@Valid SessionDetailDto sessionDetailDto, BindingResult result,
                              Model model, RedirectAttributes redirect) {
        final var patientId = sessionDetailDto.getPatientId();

        if(result.hasErrors()) {
            final var patientDetailDto = PATIENT_SERVICE.findById(patientId);

            setDetailPageData(
                    patientDetailDto,
                    model,
                    patientDetailDto.getName(),
                    getEditionParams(),
                    "sessions"
            );

            return ViewNames.PATIENT_DETAIL;
        }

        SESSION_SERVICE.save(sessionDetailDto);
        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "La sesion ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.PATIENTS,
                "/",
                sessionDetailDto.getPatientId().toString(),
                RequestMappings.SESSIONS
        );
    }

    @GetMapping("/{id}/documents")
    public String renderDetailViewWithDocumentsTab(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(required = false) Integer recordsPerPage, Model model,
                                                  HttpServletRequest request) throws NoHandlerFoundException {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var patientDetailDto = PATIENT_SERVICE.findById(id);

        if(patientDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        final var url = RequestMappings.PATIENTS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var documents = SESSION_SERVICE.findByPatientId(patientDetailDto.getId(), pageRequest);
        final var pageRender = PageRender.newInstance(url, documents);

        setDetailPageData(
                patientDetailDto,
                model,
                patientDetailDto.getName(),
                getEditionParams(),
                "documents"
        );

        model.addAttribute("page", pageRender);
        model.addAttribute("documents", pageRender.getPage().getContent());

        return ViewNames.PATIENT_DETAIL;
    }


    private void setDetailPageData(PatientDetailDto patientDetailDto, Model model, String viewTitle, Map<String, ?> params, String selectedTab) {
        final var rootEndpoint = RequestMappings.PATIENTS;
        final var title = patientDetailDto.getFullName() != null
                ? (!viewTitle.isBlank()
                ? viewTitle
                : "Editar Paciente"
        )
                : "Nuevo paciente";

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(viewListTitle, false, rootEndpoint),
                new BreadcrumbOption(title, true, rootEndpoint + "/" + patientDetailDto.getFullName())
        );

        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

        model.addAttribute("patientDetailDto", patientDetailDto);
        model.addAttribute("title", title.toUpperCase());
        model.addAttribute("selectedTab", selectedTab);

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
                USER_SERVICE.findAll()
        );
    }
}
