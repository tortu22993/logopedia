package crm.logopedia.security.context;

import crm.logopedia.data.user.model.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Clase personalizada utilizada para añadir información extra de un
 * usuario a la hora de iniciar sesión, la cual será almacenada por
 * Spring Security en una sesión y podrá ser reutilizada en cualquier
 * parte de la aplicación mientras la sesión no caduque o finalice.
 * 
 * @author Enrique Escalante
 */
@Getter
public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {
    
    /**
	 * Constructor con parámetros.
	 * 
	 * @param username El nombre de usuario del usuario
	 * @param password La contraseña
	 * @param enabled El estado del usuario (si está activo)
	 * @param accountNonExpired Indica si la cuenta ha expirado
	 * @param credentialsNonExpired Indica si los credenciales han expirado
	 * @param accountNonLocked Indica si la cuenta está bloqueada
	 * @param authorities Las autorizaciones del usuario
	 * @param user El usuario que inicia sesión
	 */
    public AuthenticatedUser(String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities, User user) {
        super(
            username,
            password,
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        );

        final var profile = user.getProfile();
        
        this.user = user;
		this.id = user.getUsername();
        this.name = profile.getName();
        this.middleName = profile.getMiddleName();
        this.lastName = profile.getLastName();
        this.fullName = profile.getFullName();
		this.jobPositionName = profile.getJobPosition().getName();
    }

    /**
     * El usuario que ha iniciado sesión.
     */
    private final User user;

	/**
	 * El ID (nombre de usuario) del usuario.
	 */
	private final String id;

    /**
     * El nombre del usuario.
     */
    private final String name;

    /**
     * El primer apellido del usuario.
     */
    private final String middleName;

    /**
     * El segundo apellido del usuario.
     */
    private final String lastName;

    /**
     * El nombre completo del usuario.
     */
    private final String fullName;

	/**
	 * El nombre del puesto de trabajo del usuario.
	 */
	private final String jobPositionName;

    // TODO: imagen del perfil del usuario

}
