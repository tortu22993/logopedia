package crm.logopedia.util.specification;


import crm.logopedia.util.http.request.FilterRequest;
import crm.logopedia.util.http.request.SortRequest;
import crm.logopedia.util.http.request.SearchRequest;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Establece una consulta parametrizable contra la base de datos,
 * conteniendo datos de filtros y ordenamientos indicados por
 * el usuario.
 * 
 * @author Enrique Escalante
 */
@AllArgsConstructor
public class SearchSpecification<T> implements Specification<T> {
    
    /**
     * La solicitud de búsqueda de datos.
     */
    private final transient SearchRequest request;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final List<Order> orders = new ArrayList<>();
        var predicate = builder.equal(
            builder.literal(Boolean.TRUE),
            Boolean.TRUE
        );

        for(final FilterRequest filter : request.getFilters()) {
            predicate = filter.getOperator().build(root, builder, filter, predicate);
        }

        for(final SortRequest sort : request.getSorts()) {
            orders.add(sort.getDirection().build(root, builder, sort));
        }

        query.orderBy(orders);

        return predicate;
    }

    /**
     * Obtiene el paginador de los resultados obtenidos de la consulta
     * construida a partir de los parámetros y filtros establecidos.
     * 
     * @param page El número de página contenedor de los resultados
     * @param size El tamaño de la página, es decir, el número de registros máximo por página
     */
    public static Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(
            Objects.requireNonNullElse(page, 0),
            Objects.requireNonNullElse(size, 25)
        );
    }

}
