package crm.logopedia.util.contract;

import java.time.Instant;
import java.util.Date;

/**
 * Establece métodos y funciones relacionadas con la gestión de tokens.
 *
 * @author Enrique Escalante
 */
public interface TokenManagement {

	/**
	 * Obtiene el asunto o valor asociado a un token.
	 *
	 * @param token El token a partir del cual se obtendrá el asunto
	 * @return El asunto o valor asociado al token
	 */
	String getSubjectFrom(String token);

	/**
	 * Obtiene el tiempo en el que expira un token.
	 *
	 * @param token El token a partir del cual se obtendrá el tiempo en el que expira
	 * @return El tiempo en el que expira el token
	 */
	Date getExpirationDateFrom(String token);

	/**
	 * Obtiene el tiempo en el que expira un token.
	 *
	 * @param token El token a partir del cual se obtendrá el tiempo en el que expira
	 * @return El tiempo en el que expira el token
	 */
	Instant getExpirationTimeFrom(String token);

	/**
	 * Crea un nuevo token.
	 *
	 * @param subject El asunto o valor a asociar en el token
	 * @param expirationTime El tiempo en el que expirará el token
	 * @return El token generado
	 */
	String create(String subject, Instant expirationTime);

	/**
	 * Indica si un token es válido o no.
	 *
	 * @param token El token a validar
	 * @return Verdadero si el token es válido o falso en caso contrario
	 */
	boolean isValid(String token);

}
