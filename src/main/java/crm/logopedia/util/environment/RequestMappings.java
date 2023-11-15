package crm.logopedia.util.environment;


import crm.logopedia.data.contact.controller.ContactController;
import crm.logopedia.data.document.controller.DocumentController;
import crm.logopedia.data.jobposition.controller.JobPositionController;
import crm.logopedia.data.patient.controller.PatientController;
import crm.logopedia.data.services.controller.ServicesController;
import crm.logopedia.data.session.controller.SessionController;
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
     * El endpoint del controlador {@link ContactController}.
     */
    public static final String CONTACTS = "/contacts";

    /**
     * El endpoint del controlador {@link SessionController}.
     */
    public static final String SESSIONS = "/sessions";

    /**
     * El endpoint del controlador {@link PatientController}.
     */
    public static final String PATIENTS = "/patients";
    /**
     * El endpoint del controlador {@link JobPositionController}.
     */
    public static final String JOB_POSITIONS = "/job-positions";

    /**
     * El endpoint del controlador {@link ServicesController}.
     */
    public static final String SERVICES = "/services";

    /**
     * El endpoint del controlador {@link DocumentController}.
     */
    public static final String DOCUMENTS = "/documents";


}
