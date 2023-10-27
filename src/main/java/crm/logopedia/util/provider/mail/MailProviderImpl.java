package crm.logopedia.util.provider.mail;

import crm.logopedia.util.ExtendedStringUtils;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.provider.jwt.JwtProvider;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Implementa las funcionalidades de un gestor de correos electrónicos.
 *
 * @author Enrique Escalante
 * @see JavaMailSender
 */
@Component
public class MailProviderImpl implements MailProvider {

	/**
	 * El gestor de envío de correos electrónicos.
	 */
	private final JavaMailSender JAVA_MAIL_SENDER;

	/**
	 * El motor de renderizado de plantillas.
	 */
	private final TemplateEngine EMAIL_TEMPLATE_ENGINE;

	/**
	 * El proveedor de JWT.
	 */
	private final JwtProvider JWT_PROVIDER;

	/**
	 * El componente de datos comunes de la aplicación.
	 */
	private final CommonDataComponent COMMON_DATA_COMPONENT;

	/**
	 * El nombre de la aplicación.
	 */
	@Value("${server.servlet.application-display-name}")
	protected String applicationName;

	/**
	 * El logo de la aplicación.
	 */
	@Value("classpath:static/resources/images/logos/linkedin.svg")
	protected Resource applicationLogo;

	/**
	 * El logo de Logopedia.
	 */
	@Value("classpath:static/resources/images/customers/Logopedia.png")
	protected Resource logopediaLogo;

	/**
	 * Constructor con parámetros.
	 *
	 * @param javaMailSender El gestor de envío de correos electrónicos a establecer
	 * @param emailTemplateEngine El motor de renderizado de plantillas a establecer
	 * @param jwtProvider El proveedor de JWT
	 * @param commonDataComponent El componente de datos comunes de la aplicación
	 */
	public MailProviderImpl(JavaMailSender javaMailSender, @Qualifier("emailTemplateEngine") TemplateEngine emailTemplateEngine, JwtProvider jwtProvider, CommonDataComponent commonDataComponent) {
		JAVA_MAIL_SENDER = javaMailSender;
		EMAIL_TEMPLATE_ENGINE = emailTemplateEngine;
		JWT_PROVIDER = jwtProvider;
		COMMON_DATA_COMPONENT = commonDataComponent;
	}

	@Override
	public void sendHtmlEmail(String to, String subject, String htmlBody, Map<String, Resource> inlineResources) throws MessagingException {
		final var message = JAVA_MAIL_SENDER.createMimeMessage();
		final var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);

		if(inlineResources != null) {
			for(final var resource : inlineResources.entrySet()) {
				helper.addInline(resource.getKey(), resource.getValue());
			}
		}

		JAVA_MAIL_SENDER.send(message);
	}

	@Override
	public void sendHtmlEmail(String to, String subject, String htmlBody, int priority, Map<String, Resource> inlineResources) throws MessagingException {
		final var message = JAVA_MAIL_SENDER.createMimeMessage();
		final var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		helper.setPriority(priority);

		if(inlineResources != null) {
			for(final var resource : inlineResources.entrySet()) {
				helper.addInline(resource.getKey(), resource.getValue());
			}
		}

		JAVA_MAIL_SENDER.send(message);
	}

	@Override
	public void sendWelcomeUserEmail(String to, Map<String, Object> templateModel) throws MessagingException {
		final var thymeleafContext = new Context();
		final var temporalToken = JWT_PROVIDER.create(
			to,
			Instant.now().plus(24, ChronoUnit.HOURS)
		);
		final var baseUrl = COMMON_DATA_COMPONENT.getBaseUrl();
		final var authRootEndpoint = baseUrl + RequestMappings.AUTH;
		final var subject = ExtendedStringUtils.concatWithSpaces(
			"Le damos la bienvenida a",
			applicationName,
			": Finalizar configuración de la cuenta"
		);

		thymeleafContext.setVariable("userEmail", to);
		thymeleafContext.setVariable("loginUrl", authRootEndpoint + "/login");
		thymeleafContext.setVariable("finishAccountConfigurationUrl", authRootEndpoint + "/finish-account-configuration?token=" + temporalToken);
		thymeleafContext.setVariables(templateModel);

		final var htmlBody = EMAIL_TEMPLATE_ENGINE.process("html/welcome-email", thymeleafContext);
		final var inlineResources = Map.of("applicationLogo", applicationLogo, "logopediaLogo", logopediaLogo);
		final var priority = 1;

		sendHtmlEmail(to, subject, htmlBody, priority, inlineResources);
	}

}
