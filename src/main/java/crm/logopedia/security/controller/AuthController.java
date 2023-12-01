package crm.logopedia.security.controller;


import crm.logopedia.data.user.model.entity.User;
import crm.logopedia.data.user.service.UserService;
import crm.logopedia.security.dto.PasswordChangeDto;
import crm.logopedia.security.provider.AuthenticationProvider;
import crm.logopedia.util.component.CommonDataComponent;
import crm.logopedia.util.environment.RequestMappings;
import crm.logopedia.util.environment.ViewNames;
import crm.logopedia.util.http.enums.HttpDataResponseType;
import crm.logopedia.util.provider.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * Controla las peticiones HTTP relativas al inicio y cierre
 * de sesiones de los usuarios.
 * 
 * @author Enrique Escalante
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * El servicio de la entidad {@link User}.
     */
    private final UserService USER_SERVICE;

    /**
     * El proveedor de JWT.
     */
    private final JwtProvider JWT_PROVIDER;

    /**
     * El proveedor de autenticación de usuarios.
     */
    private final AuthenticationProvider AUTHENTICATION_PROVIDER;

    /**
     * El componente de datos comunes de la aplicación.
     */
    private final CommonDataComponent COMMON_DATA_COMPONENT;

    /**
     * Valor del título de la vista de la vista de iniciar sesión.
     */
    @Value("${titles.security.login}")
    protected String loginTitle;

    /**
     * Valor del título de la vista de la vista de finalizar la configuración
     * de la cuenta de un usuario.
     */
    @Value("${titles.security.finish-account-configuration}")
    protected String finishAccountConfigurationTitle;
    
    /**
     * Devuelve la vista de inicio de sesión, solo en caso de que el usuario
     * no tenga una sesión activa.
     * 
     * @param error El mensaje de error en caso de que las credenciales no sean correctas
     * @param logout Indica si se cierra sesión o no
     * @param model El modelo de datos que contiene toda la información a usar en la vista
     * @param principal El objeto que contiene información relativa a la sesión de un usuario
     * @param redirect Permite redirigir atributos a una URL específica
     * @return El nombre de la vista a renderizar
     */
    @GetMapping("/login")
    public String renderLoginView(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            Model model, Principal principal, RedirectAttributes redirect) {
        if(principal != null) {
            return "redirect:" + RequestMappings.USERS;
        }

        model.addAttribute("title", loginTitle);
        COMMON_DATA_COMPONENT.setData(model);

        return ViewNames.LOGIN;
    }

    /**
     * Devuelve la vista de finalización de la configuración de la cuenta de un usuario con base
     * a un token temporal. Si el token no es válido, entonces se retorna la página de error 404
     * junto con su código de respuesta HTTP.
     *
     * @param model El model de datos a asociar a la vista
     * @param response La respuesta HTTP a retornar
     * @return El nombre de la vista a renderizar
     */
    @GetMapping("/finish-account-configuration")
    public String renderFinishAccountConfigurationView(Model model, HttpServletResponse response) {
        COMMON_DATA_COMPONENT.setData(model);

        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        model.addAttribute("title", finishAccountConfigurationTitle);

        return ViewNames.FINISH_ACCOUNT_CONFIGURATION;
    }

    /**
     * Finaliza la configuración de la cuenta de un nuevo usuario. Tras ello,
     * crea una nueva sesión para dicho usuario, de forma que no es necesario
     * iniciar sesión de forma manual.
     *
     * @param token El token temporal validado
     * @param passwordChangeDto El DTO del cambio de contraseña
     * @param result El resultado de la validación del DTO
     * @param model El modelo de datos a asociar a la vista
     * @param request La solicitud HTTP realizada
     * @param response La respuesta HTTP a retornar
     * @param redirect Permite redirigir atributos a una URL específica
     * @return El nombre de la vista a renderizar
     */
    @PostMapping("/finish-account-configuration")
    public String finishAccountConfiguration(@RequestParam String token, @Valid PasswordChangeDto passwordChangeDto,
                                             BindingResult result, Model model, HttpServletRequest request,
                                             HttpServletResponse response, RedirectAttributes redirect) {
        if(result.hasErrors()) {
            COMMON_DATA_COMPONENT.setData(model);
            model.addAttribute("title", finishAccountConfigurationTitle);

            return ViewNames.FINISH_ACCOUNT_CONFIGURATION;
        }

        final var userEmail = JWT_PROVIDER.getSubjectFrom(token);
        final var newPassword = passwordChangeDto.getNewPassword();

        USER_SERVICE.finishAccountConfiguration(userEmail, newPassword);
        AUTHENTICATION_PROVIDER.authenticate(userEmail, newPassword, request);

        COMMON_DATA_COMPONENT.setHttpDataResponse(
            "¡Bienvenido/a!",
            HttpDataResponseType.INFO,
            redirect
        );

        return "redirect:" + RequestMappings.PATIENTS;
    }

}
