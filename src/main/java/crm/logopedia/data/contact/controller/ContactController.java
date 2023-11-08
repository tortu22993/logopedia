package crm.logopedia.data.contact.controller;



import crm.logopedia.data.contact.model.dto.ContactDetailDto;
import crm.logopedia.data.contact.model.dto.ContactListDto;
import crm.logopedia.data.contact.model.entity.Contact;
import crm.logopedia.data.contact.service.ContactService;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * El controlador de peticiones HTTP relacionadas con
 * los traspasos de datos de la entidad {@link Contact}.
 *
 * @author Enrique Escalante
 */
@Controller
@RequestMapping(RequestMappings.CONTACTS)
@SessionAttributes({ "contactDetailDto" })
@RequiredArgsConstructor
@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
public class ContactController {

    /**
     * El servicio de la entidad {@link Contact}.
     */
    private final ContactService CONTACT_SERVICE;


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
    @Value("${titles.management.contacts}")
    protected String viewListTitle;

    /**
     * Valor del título de la vista del listado de contactos.
     */
    @Value("${titles.management.patients}")
    protected String customersViewListTile;

    /**
     * Valor de la página seleccionada en el menú.
     */
    @Value("${params.menu.pages.management.patients}")
    protected String activeMenuPage;

    /**
     * Renderiza la vista del detalle de un contacto según su ID.
     *
     * @param id El ID del contacto
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el contacto solicitado
     */
    @GetMapping("/{id}")
    public String renderDetailViewToSee(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var contactDetailDto = CONTACT_SERVICE.findById(id);

        if(contactDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                contactDetailDto,
                model,
                contactDetailDto.getFullName(),
                "details"
        );

        return ViewNames.CONTACT_DETAIL;
    }

    /**
     * Renderiza la vista de edición de un contacto según su ID.
     *
     * @param id El ID del contacto
     * @param model El modelo de datos para añadir a la vista
     * @param request El objeto contenedor de los datos de la petición HTTP
     * @return El nombre de la vista a renderizar
     * @exception NoHandlerFoundException Si no se encuentra el contacto solicitado
     */
    @GetMapping("/{id}/action/edit")
    public String renderDetailViewToEdit(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
        final var contactDetailDto = CONTACT_SERVICE.findById(id);

        if(contactDetailDto == null) {
            throw new NoHandlerFoundException(
                    HttpMethod.GET.name(),
                    request.getRequestURI(),
                    new HttpHeaders()
            );
        }

        setDetailPageData(
                contactDetailDto,
                model,
                contactDetailDto.getFullName(),
                null
        );

        return ViewNames.CONTACT_EDITION;
    }


    /**
     * Valida la estructura del DTO de un contacto y, si es correcta, la guarda
     * en la base de datos.
     *
     * @param contactDetailDto El DTO del contacto a guardar
     * @param result El resultado de la validación de la estructura del DTO
     * @param model El modelo de datos para añadir a la vista
     * @param sessionStatus El estado de la sesión
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/action/save")
    public String save(@Valid ContactDetailDto contactDetailDto, BindingResult result,
                       Model model, SessionStatus sessionStatus,
                       RedirectAttributes redirect) {
        if(result.hasErrors()) {
            setDetailPageData(
                    contactDetailDto,
                    model,
                    contactDetailDto.getFullName(),
                    null
            );

            return ViewNames.CONTACT_EDITION;
        }

        final var savedContactDetailDto = CONTACT_SERVICE.save(contactDetailDto);
        sessionStatus.setComplete();

        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "El contacto ha sido guardado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.CONTACTS,
                "/",
                savedContactDetailDto.getId().toString()
        );
    }

    /**
     * Elimina un contacto de la base de datos según su ID.
     *
     * @param id El ID del contacto a eliminar
     * @param customerIdToRedirect El ID del cliente asociado al contacto que se utilizará para redireccionar
     * @param sessionStatus El estado de la sesión
     * @param redirect Permite redirigir atributos a una URL específica
     * @return La URL de redirección
     */
    @PostMapping("/action/delete/{id}/{customerIdToRedirect}")
    public String delete(@PathVariable Long id, @PathVariable(required = false) Long customerIdToRedirect, SessionStatus sessionStatus, RedirectAttributes redirect) {
        CONTACT_SERVICE.deleteById(id);

        sessionStatus.setComplete();
        COMMON_DATA_COMPONENT.setHttpDataResponse(
                "El contacto ha sido eliminado.",
                HttpDataResponseType.SUCCESS,
                redirect
        );

        return ExtendedStringUtils.concat(
                "redirect:",
                RequestMappings.PATIENTS,
                "/",
                customerIdToRedirect.toString(),
                RequestMappings.CONTACTS
        );
    }

    /**
     * Busca una serie de contactos en función de un texto introducido por
     * el usuario.
     *
     * @param text El texto que se utiliza como filtro
     * @return Una lista de DTO de contactos cuyo nombre coincide con el filtro
     * en formato JSON
     */
    @GetMapping("/search/{text}")
    public @ResponseBody List<ContactListDto> search(@PathVariable String text) {
        return CONTACT_SERVICE.search(text);
    }


    /**
     * Rellena los datos comunes a la vista de detalle de un contacto.
     *
     * @param contactDetailDto El DTO del contacto a renderizar
     * @param model El modelo de datos a renderizar
     * @param viewTitle El título de la vista a renderizar
     * @param selectedTab La pestaña de la vista a mostrar
     */
    private void setDetailPageData(ContactDetailDto contactDetailDto, Model model, String viewTitle, String selectedTab) {
        final var contactId = contactDetailDto.getId().toString();
        final var title = !viewTitle.isBlank()
                ? viewTitle
                : "Editar contacto";
        final var separator = "/";
        final var patientId = contactDetailDto.getId().toString();

        final var breadcrumbOptions = List.of(
                new BreadcrumbOption(menuSectionTitle, false, null),
                new BreadcrumbOption(customersViewListTile, false, RequestMappings.PATIENTS),
                new BreadcrumbOption(contactDetailDto.getPatientName(), false, ExtendedStringUtils.concat(
                        RequestMappings.PATIENTS,
                        separator,
                        patientId
                )),
                new BreadcrumbOption(viewListTitle, false, ExtendedStringUtils.concat(
                        RequestMappings.PATIENTS,
                        separator,
                        patientId,
                        RequestMappings.CONTACTS
                )),
                new BreadcrumbOption(title, true, ExtendedStringUtils.concat(
                        RequestMappings.PATIENTS,
                        separator,
                        patientId,
                        separator,
                        RequestMappings.CONTACTS,
                        contactId
                ))
        );

        COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
        COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

        model.addAttribute("contactDetailDto", contactDetailDto);
        model.addAttribute("title", title.toUpperCase());
        model.addAttribute("selectedTab", selectedTab);
    }

}
