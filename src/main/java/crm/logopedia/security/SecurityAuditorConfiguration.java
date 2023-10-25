package crm.logopedia.security;


import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.security.context.AuthenticatedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Clase de seguridad que permite propagar una serie de datos relacionados
 * con una clase concreta (en este caso, de la entidad {@link crm.logopedia.data.user.model.entity.User})
 * a otras clases a la hora de persistir datos en la base de datos.
 * Por ejemplo, se utiliza para propagar el usuario creador y/o último
 * modificador de datos en las entidades.
 * 
 * @author Enrique Escalante
 */
@Configuration
@EnableJpaAuditing
public class SecurityAuditorConfiguration {

    /**
     * Proporciona una instancia del usuario que ha iniciado sesión para
     * la propagación de sus datos a otras instancias, ya sean las fechas
     * de creación y última modificación o el usuario creador o último
     * modificador de los mismos.
     * 
     * @return Una instancia del auditor del usuario que ha iniciado sesión
     */
    @Bean
    AuditorAware<User> auditorProvider() {
        return new AuditorAware<>() {

            @Override
            public @NonNull Optional<User> getCurrentAuditor() {
                return Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .map(AuthenticatedUser.class::cast)
                    .map(AuthenticatedUser::getUser);
            }
            
        };
    }

}
