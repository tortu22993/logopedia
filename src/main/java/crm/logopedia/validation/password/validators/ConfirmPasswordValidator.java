package crm.logopedia.validation.password.validators;


import crm.logopedia.security.dto.PasswordChangeDto;
import crm.logopedia.validation.password.rules.ConfirmPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;

/**
 * Validador de confirmación de contraseñas:
 * Una contraseña es correcta si el valor introducir y el valor de la confirmación
 * coinciden.
 * Si la validación no es correcta, entonces se añade una restricción de valores
 * que se añade cuando se utiliza la anotación {@link Valid}.
 *
 * @author Enrique Escalante
 */
public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, PasswordChangeDto> {

	/**
	 * El mensaje por defecto a mostrar si el resultado de la validación no
	 * es correcto.
	 */
	private String defaultMessage;

	@Override
	public void initialize(ConfirmPassword constraintAnnotation) {
		defaultMessage = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(PasswordChangeDto finishAccountConfigurationDto, ConstraintValidatorContext context) {
		final var newPassword = finishAccountConfigurationDto.getNewPassword();
		final var confirmNewPassword = finishAccountConfigurationDto.getConfirmNewPassword();
		final var passwordsMatch = newPassword.equalsIgnoreCase(confirmNewPassword);

		if(passwordsMatch) {
			return true;
		}

		context.buildConstraintViolationWithTemplate(defaultMessage)
			.addPropertyNode("confirmNewPassword")
			.addConstraintViolation()
			.disableDefaultConstraintViolation();

		return false;
	}

}
