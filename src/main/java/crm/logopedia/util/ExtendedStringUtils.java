package crm.logopedia.util;

import com.github.slugify.Slugify;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Contiene funciones útiles sobre cadenas de texto cuyas
 * funcionalidades suelen ser recurrentes.
 * 
 * @author Enrique Escalante
 */
public abstract class ExtendedStringUtils extends StringUtils {
    
    /**
     * Concatena una serie de cadenas de caracteres y los separa
     * por espacios.
     * 
     * @param values Los valores a concatenar
     * @return Los valores concatenados y separados por un espacio
     */
    public static String concatWithSpaces(@NonNull String... values) {
        final var valuesList = Arrays.asList(values);
        
        return valuesList.stream()
            .filter(StringUtils::hasLength)
            .collect(Collectors.joining(" "))
            .trim();
    }

    /**
     * Concatena una serie de cadenas de caracteres y los separa
     * por saltos de línea.
     *
     * @param values Los valores a concatenar
     * @return Los valores concatenados y separados por un salto de línea
     */
    public static String concatWithBreakLines(@NonNull String... values) {
        final var valuesList = Arrays.asList(values);

        return valuesList.stream()
            .filter(StringUtils::hasLength)
            .collect(Collectors.joining(System.lineSeparator()))
            .trim();
    }

    /**
     * Concatena una serie de cadenas de caracteres.
     * 
     * @param values Los valores a concatenar
     * @return Los valores concatenados
     */
    public static String concat(@NonNull String... values) {
        final var valuesList = Arrays.asList(values);
        
        return valuesList.stream()
            .filter(StringUtils::hasLength)
            .collect(Collectors.joining())
            .trim();
    }

    /**
	 * Formatea una cadena de texto a un valor alfanumérico.
	 * 
	 * @param value El valor a formatear
	 * @return La cadena de texto formateada
	 */
    public static String formatToAlphanumeric(String value) {
        return Normalizer.normalize(
            value.replaceAll("[^A-Za-z0-9_.]", "").toLowerCase(),
            Form.NFD
        ).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Genera una cadena de textos única y con un formato específico
     * a partir de otra. Este método es conocido como <i>slugificar</i>.
     * 
     * @param value La cadena de texto a slugificar
     * @return La cadena de texto slugificada
     */
    public static String slugify(String value) {
        final var builder = Slugify.builder()
            .customReplacement(".", "")
            .customReplacement(",", "")
            .customReplacement("_", "")
            .build();

        return builder.slugify(value);
    }

    /**
	 * Añade al principio de la cadena un determinado carácter repetido un
	 * determinado número de veces.
	 * 
	 * @param value La cadena a tratar
	 * @param totalWidth El número de veces a repetir el carácter
	 * @param paddingChar El carácter que será repetido
	 * @return La cadena con los valores añadidos al principio
	 */
    public static String padLeft(String value, int totalWidth, String paddingChar) {
        return String.format("%" + totalWidth + "s", value).replace(" ", paddingChar);
    }

    /**
	 * Añade al final de la cadena un determinado carácter repetido un
	 * determinado número de veces.
	 * 
	 * @param value La cadena a tratar
	 * @param totalWidth El número de veces a repetir el carácter
	 * @param paddingChar El carácter que será repetido
	 * @return La cadena con los valores añadidos al final
	 */
    public static String padRight(String value, int totalWidth, String paddingChar) {
        return String.format("%-" + totalWidth + "s", value).replace(" ", paddingChar);
    }

	/**
	 * Obtiene una parte de una cadena de caracteres delimitada por una secuencia de caracteres.
	 * La parte extraída es previa al delimitador.
	 *
	 * @param value La cadena de caracteres a partir de la cual se obtendrá el resultado
	 * @param delimiter La cadena de caracteres que actúa como delimitador
	 * @return La parte delimitada y extraída de la cadena de caracteres original
	 */
	public static String substringBefore(@NonNull String value, @NonNull String delimiter) {
		return value.substring(0, value.indexOf(delimiter));
	}

	/**
	 * Obtiene una parte de una cadena de caracteres delimitada por una secuencia de caracteres.
	 * La parte extraída es posterior al delimitador.
	 *
	 * @param value La cadena de caracteres a partir de la cual se obtendrá el resultado
	 * @param delimiter La cadena de caracteres que actúa como delimitador
	 * @return La parte delimitada y extraída de la cadena de caracteres original
	 */
	public static String substringAfter(@NonNull String value, @NonNull String delimiter) {
		return value.substring(value.indexOf(delimiter) + 1);
	}

}
