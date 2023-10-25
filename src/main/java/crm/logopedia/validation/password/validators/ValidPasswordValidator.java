package crm.logopedia.validation.password.validators;


import crm.logopedia.validation.password.rules.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.passay.*;

import java.util.List;

/**
 * Validador de contraseñas que verifica que un valor contenga el siguiente formato:
 * <ul>
 *      <li>Mínimo 8 caracteres y 500 como máximo</li>
 *      <li>Mínimo 1 carácter</li>
 *      <li>Mínimo 1 número</li>
 *      <li>Mínimo 1 carácter especial</li>
 *      <li>Mínimo 1 letra mayúscula</li>
 *      <li>Mínimo 1 letra minúscula</li>
 *      <li>Que no contenga espacios en blanco</li>
 * </ul>
 * Si la contraseña no es correcta, entonces se añade una restricción de valores
 * que se añade cuando se utiliza la anotación {@link Valid}.
 * 
 * @author Enrique Escalante
 */
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * El mensaje por defecto a mostrar si el resultado de la validación no
     * es correcto.
     */
    private String defaultMessage;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        final var validator = new PasswordValidator(List.of(
            new LengthRule(8, 255),
            new CharacterRule(EnglishCharacterData.Alphabetical, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new CharacterRule(EnglishCharacterData.Special, 1),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new WhitespaceRule()
        ));
        final var result = validator.validate(new PasswordData(password));

        if(result.isValid()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(defaultMessage)
            .addConstraintViolation()
            .disableDefaultConstraintViolation();

        return false;
    }

}
