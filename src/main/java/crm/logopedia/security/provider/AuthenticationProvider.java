package crm.logopedia.security.provider;


import crm.logopedia.data.role.model.type.RoleType;
import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.model.entity.UserProfile;
import crm.logopedia.security.context.AuthenticatedUser;
import crm.logopedia.security.service.JpaUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {

	/**
	 * El gestor de autenticación de usuarios.
	 */
	private final AuthenticationManager AUTHENTICATION_MANAGER;

	/**
	 * El servicio de los datos del usuario que ha iniciado sesión.
	 */
	private final JpaUserDetailsService JPA_USER_DETAILS_SERVICE;

	/**
	 * Autentica a un usuario.
	 *
	 * @param email El correo electrónico del usuario a autenticar
	 */
	public void authenticate(final String email, final String password, final HttpServletRequest request) {
		final var user = JPA_USER_DETAILS_SERVICE.loadUserByUsername(email);
		final Authentication authentication = new UsernamePasswordAuthenticationToken(
			email,
			password,
			user.getAuthorities()
		);

		final var authenticatedUser = AUTHENTICATION_MANAGER.authenticate(authentication);
		final var securityContext = SecurityContextHolder.getContext();

		securityContext.setAuthentication(authenticatedUser);

		final var session = request.getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}

	/**
	 * Obtiene el nombre de usuario (campo {@link User#getUsername}) del usuario que
	 * ha iniciado sesión.
	 *
	 * @return El nombre de usuario del usuario que ha iniciado sesión
	 */
	public User getAuthenticatedUser() {
		return ((AuthenticatedUser) getAuthentication().getPrincipal()).getUser();
	}

	/**
	 * Obtiene el nombre de usuario (campo {@link User#getUsername}) del usuario que
	 * ha iniciado sesión.
	 *
	 * @return El nombre de usuario del usuario que ha iniciado sesión
	 */
	public String getAuthenticatedUsername() {
		return getAuthenticatedUser().getUsername();
	}

	/**
	 * Obtiene el nombre completo (campo {@link UserProfile#getFullName}) del usuario que
	 * ha iniciado sesión.
	 *
	 * @return El nombre completo del usuario que ha iniciado sesión
	 */
	public String getAuthenticatedUserFullName() {
		return getAuthenticatedUser().getProfile().getFullName();
	}

	/**
	 * Comprueba si el nombre de usuario del usuario autenticado coincide
	 * con el recibido como parámetro.
	 *
	 * @param username El nombre de usuario a comprobar
	 * @return Verdadero si coinciden o falso en caso contrario
	 */
	public boolean isAuthenticatedUser(@NonNull final String username) {
		return getAuthenticatedUsername().equalsIgnoreCase(username);
	}

	/**
	 * Comprueba si el usuario autenticado posee un rol específico.
	 *
	 * @param role El rol a verificar si el usuario lo posee
	 * @return Verdadero si el usuario autenticado posee el rol o falso en caso contrario
	 */
	public boolean hasRole(@NonNull RoleType role) {
		final var auth = getAuthentication();

		if(auth != null) {
			return auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(a -> a.equalsIgnoreCase(role.getAuthority()));
		}

		return false;
	}

	/**
	 * Comprueba si el usuario autenticado posee algún rol.
	 *
	 * @param roles Los roles a verificar si el usuario posee alguno de ellos
	 * @return Verdadero si el usuario autenticado posee algún rol o falso en caso contrario
	 */
	public boolean hasAnyRole(@NonNull RoleType... roles) {
		final var auth = getAuthentication();

		if(auth != null) {
			final var rolesList = Arrays.stream(roles)
				.map(RoleType::getAuthority)
				.toList();

			return auth.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(rolesList::contains);
		}

		return false;
	}

	/**
	 * Obtiene la autenticación actual, la cual contiene diversa información entre la que
	 * se incluyen los datos del usuario autenticado.
	 *
	 * @return La autenticación actual
	 */
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
