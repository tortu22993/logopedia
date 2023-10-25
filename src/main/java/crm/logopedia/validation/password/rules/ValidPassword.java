package crm.logopedia.validation.password.rules;

import crm.logopedia.validation.password.validators.ValidPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Valida si el formato de una contraseña es correcta. Dicho formato
 * está descrito por la clase {@link ValidPasswordValidator}.
 * En caso de que la contraseña no sea correcta, devolverá un mensaje
 * establecido en el fichero de configuración <strong>messages.properties</strong>.
 * 
 * @author Enrique Escalante
 */
@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidPassword {
    
    /**
     * Devuelve el mensaje de validación.
     * 
     * @return El mensaje de validación
     */
    String message() default "{messages.validation.valid-password}";

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
