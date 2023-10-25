package crm.logopedia.data.user.model.dto;

import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en la vista del listado de la entidad {@link User}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListDto extends AbstractListDto {

	/**
	 * Indica si la sección de filtros está abierta. Si su valor es
	 * verdadero, entonces en el listado de activos aparecerá la
	 * sección de filtros desplegada.
	 */
	private boolean openedFiltersSection;

	/**
	 * El nombre de usuario del usuario.
	 */
	private String username;

	/**
	 * La imagen del perfil del usuario.
	 */
	private String profileImage;

	/**
	 * El nombre completo del usuario.
	 */
	private String fullName;

	/**
	 * El correo electrónico del usuario.
	 */
	private String email;

	/**
	 * El ID del puesto de trabajo del usuario.
	 */
	private String jobPositionId;

	/**
	 * El puesto de trabajo del usuario.
	 */
	private String jobPositionName;

	/**
	 * Indica si la cuenta del usuario está bloqueada o no.
	 */
	private Boolean lockedAccount;

	/**
	 * Indica si el usuario está habilitado o no.
	 */
	private Boolean enabled;

}
