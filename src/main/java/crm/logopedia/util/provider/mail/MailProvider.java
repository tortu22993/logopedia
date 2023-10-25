package crm.logopedia.util.provider.mail;

import jakarta.mail.MessagingException;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

/**
 * Declara las funcionalidades de un gestor de correos electrónicos.
 *
 * @author Enrique Escalante
 * @see JavaMailSender
 */
public interface MailProvider {

	/**
	 * Envía un correo electrónico con formato HTML.
	 *
	 * @param to La dirección de correo del receptor
	 * @param subject El asunto del mensaje
	 * @param htmlBody El contenido HTML del mensaje
	 * @param inlineResources Los recursos en línea a mostrar en el mensaje
	 * @throws MessagingException Si ocurre alguna excepción
	 */
	void sendHtmlEmail(String to, String subject, String htmlBody, Map<String, Resource> inlineResources) throws MessagingException;

	/**
	 * Envía un correo electrónico con formato HTML.
	 *
	 * @param to La dirección de correo del receptor
	 * @param subject El asunto del mensaje
	 * @param htmlBody El contenido HTML del mensaje
	 * @param priority La prioridad del mensaje (1 más alto; 5 más bajo)
	 * @param inlineResources Los recursos en línea a mostrar en el mensaje
	 * @throws MessagingException Si ocurre alguna excepción
	 */
	void sendHtmlEmail(String to, String subject, String htmlBody, int priority, Map<String, Resource> inlineResources) throws MessagingException;

	/**
	 * Envía un correo electrónico con formato HTML con la plantilla de bienvenida
	 * a nuevos usuarios.
	 *
	 * @param to La dirección de correo del receptor
	 * @param templateModel Los parámetros a enviar a la plantilla
	 * @throws MessagingException Si ocurre alguna excepción
	 */
	void sendWelcomeUserEmail(String to, Map<String, Object> templateModel) throws MessagingException;

}
