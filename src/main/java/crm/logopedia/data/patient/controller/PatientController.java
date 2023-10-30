package crm.logopedia.data.patient.controller;

import crm.logopedia.data.patient.model.dto.PatientListDto;
import crm.logopedia.data.patient.service.PatientService;
import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.role.service.RoleService;
import crm.logopedia.data.user.model.entity.User;
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
@RequestMapping(RequestMappings.PATIENTS)
@SessionAttributes({ "patientDetailDto" })
@RequiredArgsConstructor
public class PatientController {
    /**
     * El servicio de la entidad {@link User}.
     */
    private final PatientService PATIENT_SERVICE;

    /**
     * El servicio de la entidad {@link Role}.
     */
    private final RoleService ROLE_SERVICE;

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
    @Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
    public String renderListView(PatientListDto patientListDtoFilter,
                                 @RequestParam(defaultValue = "0") Integer page,
                                 @RequestParam(required = false) Integer recordsPerPage, Model model) {
        if(recordsPerPage == null || recordsPerPage <= 0) {
            recordsPerPage = this.recordsPerPage;
        } else if(recordsPerPage > maxRecordsPerPage) {
            recordsPerPage = maxRecordsPerPage;
        }

        final var url = RequestMappings.PATIENTS;
        final var pageRequest = PageRequest.of(page, recordsPerPage);
        final var patients = PATIENT_SERVICE.findByFilter(patientListDtoFilter, pageRequest);
        final var pageRender = PageRender.newInstance(url, patients);

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
}
