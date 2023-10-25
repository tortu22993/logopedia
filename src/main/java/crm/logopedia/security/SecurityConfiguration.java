package crm.logopedia.security;

import crm.logopedia.security.handler.LoginFailureHandler;
import crm.logopedia.security.handler.LoginSuccessHandler;
import crm.logopedia.security.service.JpaUserDetailsService;
import crm.logopedia.util.environment.RequestMappings;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractCondition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.util.StringUtils;

/**
 * Configura la seguridad de la aplicación estableciendo diversos parámetros
 * como el acceso restringido a URL específicas, la configuración de las páginas
 * de inicio y cierre de sesión o el encriptado de contraseñas.
 * 
 * @author Enrique Escalante
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    /**
     * El servicio de los datos del usuario que ha iniciado sesión.
     */
    private final JpaUserDetailsService JPA_USER_DETAILS_SERVICE;

    /**
     * El gestor de autenticación correcta.
     */
    private final LoginSuccessHandler LOGIN_SUCCESS_HANDLER;

    /**
     * El gestor de autenticación incorrecta.
     */
    private final LoginFailureHandler LOGIN_FAILURE_HANDLER;

    /**
     * La configuración del contenido de política de seguridad.
     */
    @Value("${params.security.content-policy}")
    protected String contentSecurityPolicy;

    /**
     * La duración en segundos del token a guardar en una cookie
     * para el parámetro de 'Recordarme'.
     */
    @Value("${params.security.remember-me.token-validity-seconds}")
    protected Integer tokenValiditySeconds;
    
    /**
     * Establece la configuración global de seguridad en la aplicación,
     * estableciendo el servicio de los datos del usuario que inicia sesión
     * y el método para encriptar contraseñas como gestores a utilizar en
     * cualquier parte de la aplicación o módulo.
     * 
     * @param builder El constructor de la configuración
     * @throws Exception Si ocurre algún error
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(JPA_USER_DETAILS_SERVICE).passwordEncoder(passwordEncoder());
    }

    /**
     * Devuelve una instancia del gestor criptográfico de datos que
     * utiliza la aplicación.
     * El gestor utiliza el método BCrypt para el encriptado de datos
     * 
     * @return Una instancia del gestor criptográfico
     */
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Devuelve una instancia del gestor de mapeado de objetos. Es muy útil para
     * transformar DTO a entidad y viceversa.
     *
     * @return Una instancia del gestor de mapeado de objetos
     */
    @Bean
    public static ModelMapper modelMapper() {
        final var modelMapper = new ModelMapper();
        final var isBlankString = new AbstractCondition<>() {
            @Override
            public boolean applies(MappingContext<Object, Object> context) {
                final var contextSource = context.getSource();

                if(contextSource instanceof String source) {
                    return StringUtils.hasLength(source);
                }

                return contextSource != null;
            }
        };

        modelMapper.getConfiguration()
            .setPropertyCondition(isBlankString)
            .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

    /**
     * Configura un filtro que actúa como interceptor de las peticiones
     * entrantes realizadas por los usuarios, de forma que restringe el
     * acceso a determinados recursos. Además, establece la configuración
     * de las páginas de inicio y cierre de sesión, entre otros.
     * 
     * @param http La instancia de configuración del filtro
     * @return Una instancia del filtro configurado
     * @throws Exception Si ocurre algún error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> registry.requestMatchers(
            "/resources/**",
            "/css/**",
            "/js/**",
            "/pages/**",
            "/plugins/**",
            "/auth/login",
            "/auth/finish-account-configuration"
        ).permitAll().anyRequest().authenticated());

        http.formLogin(loginConfigurator -> loginConfigurator.loginPage(RequestMappings.AUTH + "/login")
            .usernameParameter("email")
            .failureHandler(LOGIN_FAILURE_HANDLER)
            .successHandler(LOGIN_SUCCESS_HANDLER)
            .permitAll()
        );

        http.rememberMe(rememberMeConfigurator -> rememberMeConfigurator.rememberMeParameter("rememberMe")
            .key("uniqueAndSecret")
            .tokenValiditySeconds(tokenValiditySeconds)
        );

        http.logout(logoutConfigurator -> logoutConfigurator.logoutUrl(RequestMappings.AUTH + "/logout")
            .logoutSuccessUrl(RequestMappings.AUTH + "/login")
            .deleteCookies("JSESSIONID")
        );

        http.headers(headersConfigurator -> headersConfigurator.contentSecurityPolicy(config -> config.policyDirectives(contentSecurityPolicy))
            .xssProtection(config -> config.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED))
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        http.sessionManagement(sessionManagementConfigurator -> sessionManagementConfigurator.maximumSessions(1));

        return http.build();
    }

    /**
     * Devuelve una instancia del gestor de autenticación utilizado por la aplicación.
     * 
     * @param configuration La configuración global de la aplicación
     * @return Una instancia del gestor de autenticación
     * @throws Exception Si ocurre algún error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
