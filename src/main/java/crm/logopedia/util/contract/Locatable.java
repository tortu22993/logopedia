package crm.logopedia.util.contract;

/**
 * Establece una serie de funciones relativas a las
 * localizaciones de lugares, junto con métodos que
 * ayudan a abstraer llamadas a la API de Google Maps.
 *
 * @author Enrique Escalante
 */
public interface Locatable {

    /**
     * Obtiene la localización completa en forma de cadena de caracteres.
     *
     * @return La localización completa
     */
    String getLocation();

    /**
     * Obtiene la localización completa formateada en forma de caracteres.
     *
     * @return La localización completa formateada
     */
    String getFormattedLocation();

    /**
     * Obtiene el enlace para realizar búsquedas en la API de Google Maps.
     *
     * @return El enlace para realizar búsquedas
     */
    String getGoogleMapsLocationSearchLink();

    /**
     * Obtiene el enlace para realizar incrustaciones de mapas de Google Maps.
     *
     * @return El enlace para realizar incrustaciones
     */
    String getGoogleMapsLocationEmbedLink();

}
