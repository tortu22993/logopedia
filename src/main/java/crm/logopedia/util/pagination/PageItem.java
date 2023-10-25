package crm.logopedia.util.pagination;

/**
 * Clase que hace referencia a un objeto de una página.
 *
 * @param pageNumber El número de página del objeto.
 * @param current Indica si el número de página del objeto es el número de página actual.
 * @author Enrique Escalante
 * @see PageRender
 */
public record PageItem(int pageNumber, boolean current) {

}
