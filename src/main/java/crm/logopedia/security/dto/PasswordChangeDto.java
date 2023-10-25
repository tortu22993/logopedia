package crm.logopedia.security.dto;


import crm.logopedia.validation.password.rules.ConfirmPassword;
import crm.logopedia.validation.password.rules.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en las vistas relacionadas con el establecimiento o cambio
 * de contraseñas.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfirmPassword
public class PasswordChangeDto {

	/**
	 * La nueva contraseña a establecer.
	 */
	@NotBlank
	@Size(max = 255)
	@ValidPassword
	private String newPassword;

	/**
	 * La confirmación de la nueva contraseña a establecer.
	 */
	@NotBlank
	private String confirmNewPassword;

}
