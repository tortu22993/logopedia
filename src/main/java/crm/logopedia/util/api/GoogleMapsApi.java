package crm.logopedia.util.api;

import crm.logopedia.util.ExtendedStringUtils;
import org.springframework.lang.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Contiene endpoints y datos relativos a la API de Google Maps.
 *
 * @author Enrique Escalante
 */
public abstract class GoogleMapsApi {

    /**
     * El endpoint raíz de la API.
     */
    public static final String ROOT_ENDPOINT = "https://www.google.com/maps";

    /**
     * El endpoint de búsqueda de la API (utilizado para enlaces).
     */
    public static final String SEARCH_ENDPOINT = ROOT_ENDPOINT + "/search/";

    /**
     * El endpoint de incrustación de mapas de la API (utilizado para iframes).
     */
    public static final String EMBED_ENDPOINT = ROOT_ENDPOINT + "?output=embed&q=";

    /**
     * Obtiene la ruta completa para realizar una búsqueda con base en
     * un texto.
     *
     * @param searchText El texto a utilizar para la búsqueda
     * @return La ruta URL completa para realizar la búsqueda
     */
    public static String search(@NonNull final String searchText) {
        return ExtendedStringUtils.concat(
            SEARCH_ENDPOINT,
            URLEncoder.encode(searchText, StandardCharsets.UTF_8)
        );
    }

    /**
     * Obtiene la ruta completa para realizar una incrustación de
     * un mapa con base en una localización.
     *
     * @param location La localización a incrustar en el mapa
     * @return La ruta URL completa para incrustar un mapa
     */
    public static String embed(@NonNull final String location) {
        return ExtendedStringUtils.concat(EMBED_ENDPOINT, location);
    }

}
