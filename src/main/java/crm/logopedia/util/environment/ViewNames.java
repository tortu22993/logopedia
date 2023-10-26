package crm.logopedia.util.environment;


import crm.logopedia.data.jobposition.controller.JobPositionController;
import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.data.services.controller.ServicesController;
import crm.logopedia.data.services.model.entity.Services;
import crm.logopedia.data.user.controller.UserController;
import crm.logopedia.security.controller.AuthController;

/**
 * <p>
 * Contiene todas las rutas relativas de las vistas
 * de la aplicación, de forma que se pueden reutilizar
 * y refactorizar de manera sencilla.
 * </p>
 * 
 * <p>
 * <strong>IMPORTANTE</strong>: si se cambia el nombre
 * del fichero de una vista .html, se deberá también
 * cambiar en este fichero, y viceversa.
 * </p>
 * 
 * @author Enrique Escalante
 */
public abstract class ViewNames {

    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * relacionados con la autenticación y seguridad.
     */
    private static final String ROOT_AUTH = "auth";

    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * relacionados con vistas de errores.
     */
    private static final String ROOT_ERROR = "error";

    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * relacionados con entidades o lógica de la aplicación.
     */
    private static final String ROOT_PAGES = "pages";


    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * de las vistas relativas a los usuarios.
     *
     * @see UserController
     */
    private static final String ROOT_USERS = ROOT_PAGES + "/users";


    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * de las vistas relativas a los puestos de trabajo.
     *
     * @see JobPosition
     */
    private static final String ROOT_JOB_POSITIONS = ROOT_PAGES + "/job-positions";

    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * de las vistas relativas a los servicios.
     *
     * @see Services
     */
    private static final String ROOT_SERVICES = ROOT_PAGES + "/services";


    /*
    /**
     * La carpeta raíz contenedora de todos los ficheros .html
     * de las vistas relativas a los campos maestros.
     *
     * @see DashboardController
     *//*
    private static final String ROOT_DASHBOARDS = ROOT_PAGES + "/dashboards";*/

    /* --------------------------------------------------------------------------------------- */

    /**
     * El nombre del fichero .html de la vista de inicio de sesión.
     *
     * @see AuthController
     */
    public static final String LOGIN = ROOT_AUTH + "/login";

    /**
     * El nombre del fichero .html de la vista de verificación de correos
     * electrónicos.
     */
    public static final String FINISH_ACCOUNT_CONFIGURATION = ROOT_AUTH + "/finish-account-configuration";

    /**
     * El nombre del fichero .html de la vista de error 400.
     */
    public static final String ERROR_400 = ROOT_ERROR + "/400";

    /**
     * El nombre del fichero .html de la vista de error 401.
     */
    public static final String ERROR_401 = ROOT_ERROR + "/401";

    /**
     * El nombre del fichero .html de la vista de error 402.
     */
    public static final String ERROR_402 = ROOT_ERROR + "/402";

    /**
     * El nombre del fichero .html de la vista de error 403.
     */
    public static final String ERROR_403 = ROOT_ERROR + "/403";

    /**
     * El nombre del fichero .html de la vista de error 404.
     */
    public static final String ERROR_404 = ROOT_ERROR + "/404";

    /**
     * El nombre del fichero .html de la vista de error 500.
     */
    public static final String ERROR_500 = ROOT_ERROR + "/500";


    /**
     * El nombre del fichero .html de la vista del listado
     * de usuarios.
     *
     * @see UserController
     */
    public static final String USER_LIST = ROOT_USERS + "/user-list";

    /**
     * El nombre del fichero .html de la vista del detalle
     * de usuarios.
     *
     * @see UserController
     */
    public static final String USER_DETAIL = ROOT_USERS + "/user-detail";

    /**
     * El nombre del fichero .html de la vista de edición
     * de usuarios.
     *
     * @see UserController
     */
    public static final String USER_EDITION = ROOT_USERS + "/user-edition";


    /**
     * El nombre del fichero .html de la vista del listado
     * de puestos de trabajo.
     *
     * @see JobPositionController
     */
    public static final String JOB_POSITION_LIST = ROOT_JOB_POSITIONS + "/job-position-list";

    /**
     * El nombre del fichero .html de la vista del detalle
     * de puestos de trabajo.
     *
     * @see JobPositionController
     */
    public static final String JOB_POSITION_DETAIL = ROOT_JOB_POSITIONS + "/job-position-detail";

    /**
     * El nombre del fichero .html de la vista del detalle
     * de puestos de trabajo.
     *
     * @see JobPositionController
     */
    public static final String JOB_POSITION_EDITION = ROOT_JOB_POSITIONS + "/job-position-edition";


    /**
     * El nombre del fichero .html de la vista del listado
     * de servicios.
     *
     * @see ServicesController
     */
    public static final String SERVICE_LIST = ROOT_SERVICES + "/services-list";

    /**
     * El nombre del fichero .html de la vista del detalle
     * de servicios.
     *
     * @see ServicesController
     */
    public static final String SERVICE_DETAIL = ROOT_SERVICES + "/services-detail";

    /**
     * El nombre del fichero .html de la vista del detalle
     * de servicios.
     *
     * @see ServicesController
     */
    public static final String SERVICE_EDITION = ROOT_SERVICES + "/services-edition";

}
