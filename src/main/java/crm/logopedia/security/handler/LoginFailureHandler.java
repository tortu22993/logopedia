package crm.logopedia.security.handler;

import crm.logopedia.security.context.AuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestiona los datos de un usuario que ha iniciado sesión
 * de forma incorrecta, pudiendo modificar el estado de su
 * cuenta si se cumplen las condiciones necesarias.
 * 
 * @author Enrique Escalante
 */
@Component
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    /**
     * El servicio del usuario que intenta iniciar sesión.
     */
    private final AuthUserService AUTH_USER_SERVICE;

    /**
     * El mensaje relacionado con intentos fallidos de inicio de
     * sesión por parte del usuario con datos incorrectos.
     */
    @Value("${messages.login.bad-credentials}")
    protected String badCredentialsLoginMessage;

    /**
     * El mensaje relacionado con el bloqueo de cuentas de usuarios.
     */
    @Value("${messages.account.locked}")
    protected String lockedAccountMessage;

    /**
     * El mensaje relacionado con el bloqueo de cuentas de usuarios.
     */
    @Value("${messages.account.disabled}")
    protected String disabledAccountMessage;

    /**
     * El mensaje relacionado con el desbloqueo de cuentas de usuarios.
     */
    @Value("${messages.account.unlocked}")
    protected String unlockedAccountMessage;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        final var email = request.getParameter("email");
        final var user = AUTH_USER_SERVICE.findByEmail(email);

        if(user != null) {
            if(!user.getEnabled()) {
                exception = new LockedException(disabledAccountMessage);
            } else if(!user.getLockedAccount()) {
                final int maxFailAttempts = AuthUserService.MAX_FAILED_ATTEMPTS;

                if(user.getLoginAttempts() < maxFailAttempts - 1) {
                    AUTH_USER_SERVICE.increaseFailedLoginAttempts(user);
                    exception = new LockedException(badCredentialsLoginMessage);
                } else {
                    AUTH_USER_SERVICE.lock(user);
                    exception = new LockedException(lockedAccountMessage);
                }
            } else {
                if(AUTH_USER_SERVICE.unlockWhenTimeExpires(user)) {
                    exception = new LockedException(unlockedAccountMessage);
                } else {
                    exception = new LockedException(lockedAccountMessage);
                }
            }
        } else {
            exception = new LockedException("");
        }

        super.setDefaultFailureUrl("/auth/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
