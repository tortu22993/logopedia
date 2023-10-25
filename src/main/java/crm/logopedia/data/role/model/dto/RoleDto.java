package crm.logopedia.data.role.model.dto;

import crm.logopedia.data.role.model.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * de la entidad {@link Role}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

	/**
	 * El ID del rol.
	 */
	private Long id;

	/**
	 * El nombre del rol.
	 */
	@NotBlank
	@Size(max = 30)
	private String plainName;

}
