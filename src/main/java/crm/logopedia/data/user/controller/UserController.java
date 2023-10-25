package crm.logopedia.data.user.controller;


import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.service.JobPositionService;
import crm.logopedia.data.role.model.entity.Role;
import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.role.service.RoleService;
import crm.logopedia.data.user.model.dto.UserDetailDto;
import crm.logopedia.data.user.model.dto.UserListDto;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.service.UserService;
import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.common.BreadcrumbOption;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import crm.logopedia.util.pagination.PageRender;
import crm.logopedia.util.provider.mail.MailProvider;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * El controlador de peticiones HTTP relacionadas con
 * los traspasos de datos de la entidad {@link User}.
 *
 * @author Enrique Escalante
 */
@Controller
@RequestMapping(RequestMappings.USERS)
@SessionAttributes({ "userDetailDto" })
@RequiredArgsConstructor
public class UserController {

	/**
	 * El servicio de la entidad {@link User}.
	 */
	private final UserService USER_SERVICE;

	/**
	 * El servicio de la entidad {@link Role}.
	 */
	private final RoleService ROLE_SERVICE;


	/**
	 * El servicio de la entidad {@link JobPosition}.
	 */
	private final JobPositionService JOB_POSITION_SERVICE;


	/**
	 * El componente de datos comunes de la aplicación.
	 */
	private final CommonDataComponent COMMON_DATA_COMPONENT;

	/**
	 * El proveedor de correos electrónicos.
	 */
	private final MailProvider MAIL_PROVIDER;

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
	@Value("${sections.administration}")
	protected String menuSectionTitle;

	/**
	 * Valor del título de la vista del listado de usuarios.
	 */
	@Value("${titles.administration.users}")
	protected String viewListTitle;

	/**
	 * Valor de la página seleccionada en el menú.
	 */
	@Value("${params.menu.pages.administration.users}")
	protected String activeMenuPage;

	/**
	 * Renderiza la vista del listado de usuarios de forma paginada.
	 *
	 * @param userListDtoFilter Un filtro para limitar los resultados
	 * @param page El número de página a renderizar
	 * @param recordsPerPage El número de registros por página a renderizar
	 * @param model El modelo de datos para añadir a la vista
	 * @return El nombre de la vista a renderizar
	 */
	@GetMapping({ "", "/" })
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
	public String renderListView(UserListDto userListDtoFilter,
								 @RequestParam(defaultValue = "0") Integer page,
								 @RequestParam(required = false) Integer recordsPerPage, Model model) {
		if(recordsPerPage == null || recordsPerPage <= 0) {
			recordsPerPage = this.recordsPerPage;
		} else if(recordsPerPage > maxRecordsPerPage) {
			recordsPerPage = maxRecordsPerPage;
		}

		final var url = RequestMappings.USERS;
		final var pageRequest = PageRequest.of(page, recordsPerPage);
		final var users = USER_SERVICE.findByFilter(userListDtoFilter, pageRequest);
		final var pageRender = PageRender.newInstance(url, users);

		final var breadcrumbOptions = List.of(
			new BreadcrumbOption(menuSectionTitle, false, null),
			new BreadcrumbOption(viewListTitle, true, url)
		);

		COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);
		COMMON_DATA_COMPONENT.setData(model, activeMenuPage);

		model.addAttribute("userListDtoFilter", userListDtoFilter);
		model.addAttribute("page", pageRender);
		model.addAttribute("users", pageRender.getPage().getContent());
		model.addAttribute("title", viewListTitle);

		return ViewNames.USER_LIST;
	}

	/**
	 * Renderiza la vista del detalle de un usuario según su
	 * nombre de usuario.
	 *
	 * @param username El nombre de usuario del usuario
	 * @param model El modelo de datos para añadir a la vista
	 * @param request El objeto contenedor de los datos de la petición HTTP
	 * @return El nombre de la vista a renderizar
	 * @exception NoHandlerFoundException Si no se encuentra el usuario solicitado
	 */
	@GetMapping("/{username}")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
	public String renderDetailViewToSee(@PathVariable String username, Model model, HttpServletRequest request) throws NoHandlerFoundException {
		final var userDetailDto = USER_SERVICE.findByUsername(username);

		if(userDetailDto == null) {
			throw new NoHandlerFoundException(
				HttpMethod.GET.name(),
				request.getRequestURI(),
				new HttpHeaders()
			);
		}

		setDetailPageData(
			userDetailDto,
			model,
			userDetailDto.getFullName(),
			null
		);

		return ViewNames.USER_DETAIL;
	}

	/**
	 * Renderiza la vista del detalle para crear un nuevo usuario.
	 *
	 * @param model El modelo de datos para añadir a la vista
	 * @return El nombre de la vista a renderizar
	 */
	@GetMapping("/action/new")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
	public String renderDetailViewToCreate(Model model) {
		final var userDetailDto = new UserDetailDto();

		setDetailPageData(
			userDetailDto,
			model,
			"Nuevo usuario",
			getEditionParams()
		);

		return ViewNames.USER_EDITION;
	}

	/**
	 * Renderiza la vista de edición de un usuario según su
	 * nombre de usuario.
	 *
	 * @param username El nombre de usuario del usuario
	 * @param model El modelo de datos para añadir a la vista
	 * @param request El objeto contenedor de los datos de la petición HTTP
	 * @return El nombre de la vista a renderizar
	 * @exception NoHandlerFoundException Si no se encuentra el usuario solicitado
	 */
	@GetMapping("{username}/action/edit")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
	public String renderDetailViewToEdit(@PathVariable String username, Model model, HttpServletRequest request) throws NoHandlerFoundException {
		final var userDetailDto = USER_SERVICE.findByUsername(username);

		if(userDetailDto == null) {
			throw new NoHandlerFoundException(
				HttpMethod.GET.name(),
				request.getRequestURI(),
				new HttpHeaders()
			);
		}

		setDetailPageData(
			userDetailDto,
			model,
			userDetailDto.getFullName(),
			getEditionParams()
		);

		return ViewNames.USER_EDITION;
	}

	/**
	 * Valida la estructura del DTO de un usuario y, si es correcta, la guarda
	 * en la base de datos.
	 *
	 * @param userDetailDto El DTO del usuario a guardar
	 * @param result El resultado de la validación de la estructura del DTO
	 * @param model El modelo de datos para añadir a la vista
	 * @param sessionStatus El estado de la sesión
	 * @param redirect Permite redirigir atributos a una URL específica
	 * @return La URL de redirección
	 */
	@PostMapping("/action/save")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
	public String save(@Valid UserDetailDto userDetailDto, BindingResult result,
					   Model model, SessionStatus sessionStatus,
					   RedirectAttributes redirect) {
		final Supplier<String> backToForm = () -> {
			setDetailPageData(
				userDetailDto,
				model,
				userDetailDto.getFullName(),
				getEditionParams()
			);

			return ViewNames.USER_EDITION;
		};

		if(result.hasErrors()) {
			return backToForm.get();
		}

		var detailDto = userDetailDto;

		try {
			detailDto = USER_SERVICE.save(userDetailDto);
			sessionStatus.setComplete();

			COMMON_DATA_COMPONENT.setHttpDataResponse(
				"El usuario ha sido guardado.",
				HttpDataResponseType.SUCCESS,
				redirect
			);
		} catch (MessagingException e) {
			COMMON_DATA_COMPONENT.setHttpDataResponse(
				"Ha ocurrido un error al enviar el correo electrónico.",
				HttpDataResponseType.ERROR,
				model
			);

			return backToForm.get();
		}

		return ExtendedStringUtils.concat(
			"redirect:",
			RequestMappings.USERS,
			"/",
			detailDto.getUsername()
		);
	}

	/**
	 * Permite enviar el correo electrónico de bienvenida a un usuario para poder acceder a la aplicación.
	 *
	 * @param username El nombre de usuario del usuario al que se enviará el correo
	 * @param redirect Permite redirigir atributos a una URL específica
	 * @return La URL de redirección
	 */
	@PostMapping("/{username}/send-email-to-access")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
	public String sendEmailToAccess(@PathVariable String username, RedirectAttributes redirect) {
		final var user = USER_SERVICE.findNotVerifiedByUsername(username);

		if(user == null) {
			COMMON_DATA_COMPONENT.setHttpDataResponse(
				ExtendedStringUtils.concatWithSpaces(
					"No existe un usuario con el nombre de usuario: ",
					username,
					" o ya está verificado."
				),
				HttpDataResponseType.ERROR,
				redirect
			);

			return "redirect:" + RequestMappings.USERS;
		}

		try {
			MAIL_PROVIDER.sendWelcomeUserEmail(user.getEmail(), null);

			COMMON_DATA_COMPONENT.setHttpDataResponse(
				"Se ha enviado una invitación de acceso.",
				HttpDataResponseType.INFO,
				redirect
			);
		} catch(MessagingException e) {
			COMMON_DATA_COMPONENT.setHttpDataResponse(
				"Ha ocurrido un error.",
				HttpDataResponseType.ERROR,
				redirect
			);
		}

		return ExtendedStringUtils.concat(
			"redirect:",
			RequestMappings.USERS,
			"/",
			username
		);
	}

	/**
	 * Busca una serie de usuarios en función de un texto introducido por
	 * el usuario.
	 *
	 * @param text El texto que se utiliza como filtro
	 * @return Una lista de DTO de usuarios cuyo nombre coincide con el filtro
	 * en formato JSON
	 */
	@GetMapping("/search/{text}")
	public @ResponseBody List<UserListDto> search(@PathVariable String text) {
		return USER_SERVICE.search(text);
	}

	/**
	 * Rellena los datos comunes a la vista de detalle de un usuario.
	 *
	 * @param userDetailDto El DTO del usuario a renderizar
	 * @param model El modelo de datos a renderizar
	 * @param viewTitle El título de la vista a renderizar
	 * @param params Los parámetros adicionales a enviar a la vista
	 */
	private void setDetailPageData(UserDetailDto userDetailDto, Model model, String viewTitle, Map<String, ?> params) {
		final var rootEndpoint = RequestMappings.USERS;
		final var title = StringUtils.hasLength(userDetailDto.getUsername())
			? (!viewTitle.isBlank()
				? viewTitle
				: "Editar usuario"
			)
			: "Nuevo usuario";

		final var breadcrumbOptions = List.of(
			new BreadcrumbOption(menuSectionTitle, false, null),
			new BreadcrumbOption(viewListTitle, false, rootEndpoint),
			new BreadcrumbOption(title, true, rootEndpoint + "/" + userDetailDto.getUsername())
		);

		COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
		COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

		model.addAttribute("userDetailDto", userDetailDto);
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
			"roles",
			ROLE_SERVICE.findAll(),
			"jobPositions",
			JOB_POSITION_SERVICE.findAll()
		);
	}

}
