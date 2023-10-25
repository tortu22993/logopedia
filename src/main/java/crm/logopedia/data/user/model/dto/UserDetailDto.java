package crm.logopedia.data.user.model.dto;

import crm.logopedia.data.user.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en la vista de detalle de la entidad {@link User}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@UniqueEmail
public class UserDetailDto {

	/**
	 * El nombre de usuario del usuario.
	 */
	private String username;

	/**
	 * El correo electrónico del usuario.
	 */
	@NotBlank
	@Email
	@Size(max = 255)
	private String email;

	/**
	 * El nombre del usuario.
	 */
	@NotBlank
	@Size(max = 30)
	private String name;

	/**
	 * El primer apellido del usuario.
	 */
	@NotBlank
	@Size(max = 30)
	private String middleName;

	/**
	 * El segundo apellido del usuario.
	 */
	@Size(max = 30)
	private String lastName;

	/**
	 * El nombre completo del usuario.
	 */
	private String fullName;

	/**
	 * Las funciones del usuario.
	 */
	@Size(max = 200)
	private String duties;

	/**
	 * El número de teléfono móvil (trabajo) del usuario.
	 */
	@Size(max = 15)
	private String phoneNumber;

	/**
	 * El N.A.F. del usuario.
	 */
	@Size(max = 12)
	// TODO: NAF validation ?
	private String naf;

	/**
	 * El D.N.I. del usuario.
	 */
	// TODO: DNI validation ?
	private String dni;

	// TODO: image

	/**
	 * El ID del puesto de trabajo al que pertenece el usuario.
	 */
	private Long jobPositionId;

	/**
	 * El nombre del puesto de trabajo al que pertenece el usuario.
	 */
	private String jobPositionName;

	/**
	 * Indica si el usuario está habilitado o no.
	 */
	private boolean enabled;

	/**
	 * Indica si la cuenta del usuario está bloqueada o no.
	 */
	private boolean lockedAccount;

	/**
	 * La fecha en la que el usuario queda bloqueado por superar
	 * el número máximo de intentos de inicio de sesión permitidos.
	 */
	private Date lockedDate;

	/**
	 * Indica si la cuenta del usuario ha sido verificada por correo electrónico.
	 * Esto significa que la configuración mínima requerida para que el usuario
	 * pueda utilizar la aplicación está finalizada.
	 */
	private boolean verifiedAccount;

	/**
	 * Los ID de los roles del usuario.
	 */
	@NotEmpty
	@Builder.Default
	private Long[] rolesId = new Long[] {};

	/**
	 * Los roles (en texto plano) del usuario, es decir, todos los nombres
	 * de los roles del usuario contenidos en una cadena de texto.
	 */
	private String plainRoles;

	/**
	 * El nombre de usuario del usuario creador del usuario.
	 */
	private String createdByUsername;

	/**
	 * El nombre completo del usuario creador del usuario.
	 */
	private String createdByFullName;

	/**
	 * El nombre de usuario del último usuario modificador del usuario.
	 */
	private String lastModifiedByUsername;

	/**
	 * El nombre completo del último usuario modificador del usuario.
	 */
	private String lastModifiedByFullName;

	/**
	 * La fecha de creación del usuario.
	 */
	private Date createdAt;

	/**
	 * La última fecha de modificación del usuario.
	 */
	private Date lastModifiedAt;

}
