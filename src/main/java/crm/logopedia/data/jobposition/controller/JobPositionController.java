package crm.logopedia.data.jobposition.controller;


import crm.logopedia.data.jobposition.model.dto.JobPositionDetailDto;
import crm.logopedia.data.jobposition.model.dto.JobPositionListDto;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.jobposition.service.JobPositionService;
import crm.logopedia.data.role.model.type.RoleType;
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
@RequestMapping(RequestMappings.JOB_POSITIONS)
@SessionAttributes({ "jobPositionDetailDto" })
@RequiredArgsConstructor
@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER, RoleType.Code.RESPONSIBLE })
public class JobPositionController {

	/**
	 * El servicio de la entidad {@link JobPosition}.
	 */
	private final JobPositionService JOB_POSITION_SERVICE;

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
	 * Valor del título de la vista del listado de puestos de trabajo.
	 */
	@Value("${titles.administration.job-positions}")
	protected String viewListTitle;

	/**
	 * Valor de la página seleccionada en el menú.
	 */
	@Value("${params.menu.pages.administration.job-positions}")
	protected String activeMenuPage;

	/**
	 * Renderiza la vista del listado de puestos de trabajo de forma paginada.
	 *
	 * @param jobPositionListDtoFilter Un filtro para limitar los resultados
	 * @param page El número de página a renderizar
	 * @param recordsPerPage El número de registros por página a renderizar
	 * @param model El modelo de datos para añadir a la vista
	 * @return El nombre de la vista a renderizar
	 */
	@GetMapping({ "", "/" })
	public String renderListView(JobPositionListDto jobPositionListDtoFilter, Model model,
								 @RequestParam(defaultValue = "0") Integer page,
								 @RequestParam(required = false) Integer recordsPerPage) {
		if(recordsPerPage == null || recordsPerPage <= 0) {
			recordsPerPage = this.recordsPerPage;
		} else if(recordsPerPage > maxRecordsPerPage) {
			recordsPerPage = maxRecordsPerPage;
		}

		final var url = RequestMappings.JOB_POSITIONS;
		final var pageRequest = PageRequest.of(page, recordsPerPage);
		final var jobPositions = JOB_POSITION_SERVICE.findByFilter(jobPositionListDtoFilter, pageRequest);
		final var pageRender = PageRender.newInstance(url, jobPositions);

		final var breadcrumbOptions = List.of(
			new BreadcrumbOption(menuSectionTitle, false, null),
			new BreadcrumbOption(viewListTitle, true, url)
		);

		COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);
		COMMON_DATA_COMPONENT.setData(model, activeMenuPage);

		model.addAttribute("jobPositionListDtoFilter", jobPositionListDtoFilter);
		model.addAttribute("page", pageRender);
		model.addAttribute("jobPositions", pageRender.getPage().getContent());
		model.addAttribute("title", viewListTitle);

		return ViewNames.JOB_POSITION_LIST;
	}

	/**
	 * Renderiza la vista del detalle de un puesto de trabajo según su ID.
	 *
	 * @param id El ID del puesto de trabajo
	 * @param model El modelo de datos para añadir a la vista
	 * @param request El objeto contenedor de los datos de la petición HTTP
	 * @return El nombre de la vista a renderizar
	 * @exception NoHandlerFoundException Si no se encuentra el puesto de trabajo solicitado
	 */
	@GetMapping("/{id}")
	public String renderDetailViewToSee(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
		final var jobPositionDetailDto = JOB_POSITION_SERVICE.findById(id);

		if (jobPositionDetailDto == null) {
			throw new NoHandlerFoundException(
				HttpMethod.GET.name(),
				request.getRequestURI(),
				new HttpHeaders()
			);
		}

		setDetailPageData(
			jobPositionDetailDto,
			model,
			jobPositionDetailDto.getName()
		);

		return ViewNames.JOB_POSITION_DETAIL;
	}

	/**
	 * Renderiza la vista del detalle para crear un nuevo puesto de trabajo.
	 *
	 * @param model El modelo de datos para añadir a la vista
	 * @return El nombre de la vista a renderizar
	 */
	@GetMapping("/action/new")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN })
	public String renderDetailViewToCreate(Model model) {
		final var jobPositionDetailDto = new JobPositionDetailDto();

		setDetailPageData(
			jobPositionDetailDto,
			model,
			"Nuevo puesto de trabajo"
		);

		return ViewNames.JOB_POSITION_EDITION;
	}

	/**
	 * Renderiza la vista de edición de un puesto de trabajo según su ID.
	 *
	 * @param id El ID del puesto de trabajo
	 * @param model El modelo de datos para añadir a la vista
	 * @param request El objeto contenedor de los datos de la petición HTTP
	 * @return El nombre de la vista a renderizar
	 * @exception NoHandlerFoundException Si no se encuentra el puesto de trabajo solicitado
	 */
	@GetMapping("/{id}/action/edit")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
	public String renderDetailViewToEdit(@PathVariable Long id, Model model, HttpServletRequest request) throws NoHandlerFoundException {
		final var jobPositionDetailDto = JOB_POSITION_SERVICE.findById(id);

		if(jobPositionDetailDto == null) {
			throw new NoHandlerFoundException(
				HttpMethod.GET.name(),
				request.getRequestURI(),
				new HttpHeaders()
			);
		}

		setDetailPageData(
			jobPositionDetailDto,
			model,
			jobPositionDetailDto.getName()
		);

		return ViewNames.JOB_POSITION_EDITION;
	}

	/**
	 * Valida la estructura del DTO de un puesto de trabajo y, si es correcta, la guarda
	 * en la base de datos.
	 *
	 * @param jobPositionDetailDto El DTO del puesto de trabajo a guardar
	 * @param result El resultado de la validación de la estructura del DTO
	 * @param model El modelo de datos para añadir a la vista
	 * @param sessionStatus El estado de la sesión
	 * @param redirect Permite redirigir atributos a una URL específica
	 * @return La URL de redirección
	 */
	@PostMapping("/action/save")
	@Secured({ RoleType.Code.MASTER, RoleType.Code.ADMIN, RoleType.Code.MANAGER })
	public String save(@Valid JobPositionDetailDto jobPositionDetailDto, BindingResult result,
					   Model model, SessionStatus sessionStatus, RedirectAttributes redirect) {
		if(result.hasErrors()) {
			setDetailPageData(
				jobPositionDetailDto,
				model,
				jobPositionDetailDto.getName()
			);

			return ViewNames.JOB_POSITION_EDITION;
		}

		final var savedJobPositionDetailDto = JOB_POSITION_SERVICE.save(jobPositionDetailDto);
		sessionStatus.setComplete();

		COMMON_DATA_COMPONENT.setHttpDataResponse(
			"El puesto de trabajo ha sido guardado.",
			HttpDataResponseType.SUCCESS,
			redirect
		);

		return ExtendedStringUtils.concat(
			"redirect:",
			RequestMappings.JOB_POSITIONS,
			"/",
			savedJobPositionDetailDto.getId().toString()
		);
	}

	/**
	 * Rellena los datos comunes a la vista de detalle de un puesto de trabajo.
	 *
	 * @param jobPositionDetailDto El DTO del puesto de trabajo a renderizar
	 * @param model El modelo de datos a renderizar
	 * @param viewTitle El título de la vista a renderizar
	 */
	private void setDetailPageData(JobPositionDetailDto jobPositionDetailDto, Model model, String viewTitle) {
		final var rootEndpoint = RequestMappings.JOB_POSITIONS;
		final var title = jobPositionDetailDto.getId() != null
			? (!viewTitle.isBlank()
			? viewTitle
			: "Editar puesto de trabajo"
		)
			: "Nuevo puesto de trabajo";

		final var breadcrumbOptions = List.of(
			new BreadcrumbOption(menuSectionTitle, false, null),
			new BreadcrumbOption(viewListTitle, false, rootEndpoint),
			new BreadcrumbOption(title, true, rootEndpoint + "/" + jobPositionDetailDto.getId())
		);

		COMMON_DATA_COMPONENT.setData(model, activeMenuPage);
		COMMON_DATA_COMPONENT.setBreadcrumbOptions(breadcrumbOptions, model);

		model.addAttribute("jobPositionDetailDto", jobPositionDetailDto);
		model.addAttribute("title", title.toUpperCase());
	}

}
