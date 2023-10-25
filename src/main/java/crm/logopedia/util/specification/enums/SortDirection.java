package crm.logopedia.util.specification.enums;

import crm.logopedia.util.http.request.SortRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

/**
 * Contiene los diferentes tipos de ordenamientos que
 * se pueden utilizar dentro de una consulta contra
 * la base de datos.
 * 
 * @author Enrique Escalante
 */
public enum SortDirection {
    
    /**
     * El tipo de ordenamiento ascendente.
     */
    ASC {
        public <T> Order build(Root<T> root, CriteriaBuilder builder, SortRequest request) {
            return builder.asc(root.get(request.getKey()));
        }
    },

    /**
     * El tipo de ordenamiento descendente.
     */
    DESC {
        public <T> Order build(Root<T> root, CriteriaBuilder builder, SortRequest request) {
            return builder.desc(root.get(request.getKey()));
        }
    };

    /**
     * Construye la parte de ordenamiento de una consulta contra la base de datos.
     * 
     * @param <T> El tipo parametrizable de la consulta
     * @param root El contenedor de la entidad sobre la que se formará la consulta
     * @param builder El constructor de la consulta
     * @param request El contenedor de los datos de la petición
     * @return El objeto definidor del ordenamiento de la consulta generada
     */
    public abstract <T> Order build(Root<T> root, CriteriaBuilder builder, SortRequest request);

}
