package crm.logopedia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Clase que configura la gestión y el uso de correos electrónicos en el contexto
 * de la aplicación. Por ejemplo, aquí se definen los motores de plantillas a
 * renderizar cuando se envían correos electrónicos. De la misma forma, la configuración
 * establecida en los ficheros application.properties, podría ser extraída de allí y
 * parametrizarla a través de esta clase.
 *
 * @author Enrique Escalante
 */
@Configuration
public class MailConfiguration {

    /**
     * La ruta relativa que actúa como prefijo para establecer las plantillas que
     * se quieran renderizar.
     */
    private static final String EMAIL_TEMPLATE_PREFIX = "mail/templates/";

    /**
     * El juego de caracteres a establecer en las plantillas de los correos electrónicos.
     */
    private static final String EMAIL_TEMPLATE_ENCODING = StandardCharsets.UTF_8.name();

    /**
     * Crea y obtiene la instancia del motor de plantillas para renderizar correos
     * electrónicos.
     *
     * @return La instancia del motor de plantillas
     */
    @Bean
    public TemplateEngine emailTemplateEngine() {
        final var templateEngine = new SpringTemplateEngine();

        templateEngine.addTemplateResolver(textTemplateResolver());
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());

        return templateEngine;
    }

    /**
     * Crea y obtiene la fuente de mensajes a enviar a las plantillas de
     * correos electrónicos.
     *
     * @return La fuente de mensajes para las plantillas
     */
    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final var messageSource = new ResourceBundleMessageSource();

        messageSource.setBasenames("mail/mail-messages");
        messageSource.setDefaultEncoding(EMAIL_TEMPLATE_ENCODING);
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }

    /**
     * Crea y establece el gestor de resolución de plantillas en texto
     * para correos electrónicos.
     *
     * @return El gestor de resolución de plantillas en texto
     */
    private ITemplateResolver textTemplateResolver() {
        final var templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setOrder(1);
        templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
        templateResolver.setPrefix(EMAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /**
     * Crea y establece el gestor de resolución de plantillas en HTML
     * para correos electrónicos.
     *
     * @return El gestor de resolución de plantillas en HTML
     */
    private ITemplateResolver htmlTemplateResolver() {
        final var templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setOrder(2);
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix(EMAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /**
     * Crea y establece el gestor de resolución de plantillas en texto
     * plano para correos electrónicos.
     *
     * @return El gestor de resolución de plantillas en texto plano
     */
    private ITemplateResolver stringTemplateResolver() {
        final var templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setOrder(3);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

}
