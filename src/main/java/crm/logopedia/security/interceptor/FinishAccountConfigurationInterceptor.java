package crm.logopedia.security.interceptor;


import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.repository.UserRepository;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.provider.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Permite interceptar peticiones HTTP para algunas o todas las rutas
 * de la aplicación, permitiendo añadir funcionalidades o comprobaciones
 * antes, después y al completar dichas peticiones.
 * Este interceptor está destinado a la validación de la finalización de
 * la configuración de las cuentas de nuevos usuarios.
 *
 * @author Enrique Escalante
 */
@Component
@RequiredArgsConstructor
public class FinishAccountConfigurationInterceptor implements HandlerInterceptor {

	/**
	 * El proveedor o servicio de JWT.
	 */
	private final JwtProvider JWT_PROVIDER;

	/**
	 * El servicio de la entidad {@link User}.
	 */
	private final UserRepository USER_REPOSITORY;

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws ServletException {
		final var token = request.getParameter("token");

		/*
		 * Si el token es válido y la cuenta del usuario no ha sido verificada, entonces se
		 * puede acceder a la ruta. En caso contrario, se redirigirá a la página de error 404.
		 */
		if(JWT_PROVIDER.isValid(token)) {
			final var userEmail = JWT_PROVIDER.getSubjectFrom(token);

			if(StringUtils.hasLength(userEmail)) {
				final var existingUser = USER_REPOSITORY.findByEmail(userEmail).orElse(null);

				if(existingUser != null && !existingUser.getVerifiedAccount()) {
					return true;
				}
			}
		}

		final var redirectView = new RedirectView("/" + ViewNames.ERROR_404, true);
		final var modelAndView = new ModelAndView(redirectView);

		modelAndView.setViewName(ViewNames.ERROR_404);

		throw new ModelAndViewDefiningException(modelAndView);
	}

	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
		if(modelAndView != null) {
			modelAndView.addObject("token", request.getParameter("token"));
		}
	}
}
