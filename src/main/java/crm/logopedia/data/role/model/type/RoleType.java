package crm.logopedia.data.role.model.type;

import crm.logopedia.data.role.model.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;

/**
 * Representa la entidad {@link Role} a través de un tipo
 * o enumerador, el cual contiene todos los valores posibles
 * de forma estática y centralizada.
 * <br>
 * Para utilizar este enumerador junto con Spring Security a través
 * de la anotación {@link Secured}, lo haremos de esta forma:
 * <pre>{@code @Secured(RoleType.Code.ADMIN)}</pre>
 *
 * @author Enrique Escalante
 */
@RequiredArgsConstructor
public enum RoleType implements GrantedAuthority {

	/**
	 * El tipo de rol para usuarios maestros.
	 */
	MASTER(Code.MASTER),
	/**
	 * El tipo de rol para usuarios administradores.
	 */
	ADMIN(Code.ADMIN),
	/**
	 * El tipo de rol para usuarios gestores.
	 */
	MANAGER(Code.MANAGER),
	/**
	 * El tipo de rol para usuarios responsables.
	 */
	RESPONSIBLE(Code.RESPONSIBLE),
	/**
	 * El tipo de rol para usuarios normales.
	 */
	USER(Code.USER);

	/**
	 * La autoridad del usuario.
	 */
	private final String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

	/**
	 * Contiene, de forma estática, todos los valores posibles almacenados
	 * y mapeados contra la entidad {@link Role} en forma de literales, concretamente
	 * en forma de cadenas de caracteres.
	 *
	 * @author Enrique Escalante
	 */
	public static final class Code {

		/**
		 * El valor del tipo de rol para usuarios maestros.
		 */
		public static final String MASTER = "ROLE_MASTER";

		/**
		 * El valor del tipo de rol para usuarios administradores.
		 */
		public static final String ADMIN = "ROLE_ADMIN";

		/**
		 * El valor del tipo de rol para usuarios gestores.
		 */
		public static final String MANAGER = "ROLE_MANAGER";

		/**
		 * El valor del tipo de rol para usuarios responsables.
		 */
		public static final String RESPONSIBLE = "ROLE_RESPONSIBLE";

		/**
		 * El valor del tipo de rol para usuarios normales.
		 */
		public static final String USER = "ROLE_USER";

	}

}
