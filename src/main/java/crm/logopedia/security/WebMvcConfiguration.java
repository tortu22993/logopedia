package crm.logopedia.security;

import crm.logopedia.security.interceptor.FinishAccountConfigurationInterceptor;
import crm.logopedia.util.environment.RequestMappings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configura métodos de seguridad para Spring MVC, como por ejemplo
 * la adición o exclusión de interceptores a rutas específicas.
 *
 * @author Enrique Escalante
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

	/**
	 * El interceptor de validaciones de tokens JWT.
	 */
	private final FinishAccountConfigurationInterceptor finishAccountConfigurationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(finishAccountConfigurationInterceptor).addPathPatterns(
			RequestMappings.AUTH + "/finish-account-configuration"
		);
	}

}
