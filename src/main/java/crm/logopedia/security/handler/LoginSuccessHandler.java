package crm.logopedia.security.handler;

import crm.logopedia.security.context.AuthUserService;
import crm.logopedia.security.context.AuthenticatedUser;
import crm.logopedia.util.environment.RequestMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestiona los datos de un usuario que ha iniciado sesión
 * de forma correcta.
 * 
 * @author Enrique Escalante
 */
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    /**
     * El servicio del usuario que intenta iniciar sesión.
     */
    private final AuthUserService AUTH_USER_SERVICE;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        final var loggedUser = (AuthenticatedUser) authentication.getPrincipal();
        final var user = AUTH_USER_SERVICE.findByEmail(loggedUser.getUsername());

        if(user.getLoginAttempts() > 0) {
            AUTH_USER_SERVICE.resetLoginAttempts(user.getEmail());
        }

        setDefaultTargetUrl(RequestMappings.PATIENTS);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
