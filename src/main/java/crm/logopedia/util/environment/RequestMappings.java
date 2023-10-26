package crm.logopedia.util.environment;


import crm.logopedia.data.jobposition.controller.JobPositionController;
import crm.logopedia.data.services.controller.ServicesController;
import crm.logopedia.data.user.controller.UserController;
import crm.logopedia.security.controller.AuthController;

/**
 * Contiene todos los endpoints de la aplicación, de forma
 * que se pueden reutilizar y refactorizar de manera sencilla.
 * 
 * @author Enrique Escalante
 */
public abstract class RequestMappings {

    /**
     * El endpoint del controlador {@link AuthController}.
     */
    public static final String AUTH = "/auth";


    /**
     * El endpoint del controlador {@link UserController}.
     */
    public static final String USERS = "/users";

    /**
     * El endpoint del controlador {@link JobPositionController}.
     */
    public static final String JOB_POSITIONS = "/job-positions";

    /**
     * El endpoint del controlador {@link ServicesController}.
     */
    public static final String SERVICES = "/services";



}
