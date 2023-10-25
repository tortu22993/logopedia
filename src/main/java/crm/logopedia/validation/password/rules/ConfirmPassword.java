package crm.logopedia.validation.password.rules;


import crm.logopedia.validation.password.validators.ConfirmPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valida la confirmación de una contraseña. Dicha validación
 * está descrita por la clase {@link ConfirmPasswordValidator}.
 * En caso de que la contraseña y su confirmación no coincidan, devolverá un mensaje
 * establecido en el fichero de configuración <strong>messages.properties</strong>.
 *
 * @author Enrique Escalante
 */
@Documented
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ConfirmPassword {

	/**
	 * Devuelve el mensaje de validación.
	 *
	 * @return El mensaje de validación
	 */
	String message() default "{messages.validation.confirm-password}";

	/**
	 * Devuelve los grupos asociados a la validación.
	 *
	 * @return Los grupos asociados a la validación
	 */
	Class<?>[] groups() default {};

	/**
	 * Devuelve la carga útil asociada a la validación.
	 *
	 * @return La carga útil asociada a la validación
	 */
	Class<? extends Payload>[] payload() default {};

}
