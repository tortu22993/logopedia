package crm.logopedia.util.specification.enums;

import crm.logopedia.util.http.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;

/**
 * Contiene los diferentes tipos de operadores que
 * se pueden utilizar dentro de una consulta contra
 * la base de datos.
 * 
 * @author Enrique Escalante
 */
public enum Operator {
    
    /**
     * El operador EQUAL (=).
     */
    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final var value = request.getFieldType().parse(request.getValue().toString());
            final Expression<?> key = root.get(request.getKey());

            return builder.and(builder.equal(key, value), predicate);
        }
    },

    /**
     * El operador NOT EQUAL (<>).
     */
    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final var value = request.getFieldType().parse(request.getValue().toString());
            final Expression<?> key = root.get(request.getKey());

            return builder.and(builder.notEqual(key, value), predicate);
        }
    },

    /**
     * El operador LIKE (con contención).
     */
    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final Expression<String> key = root.get(request.getKey());

            return builder.and(
                builder.like(
                    builder.upper(key),
                    "%" + request.getValue().toString().toUpperCase() + "%"
                ),
                predicate
            );
        }
    },

    /**
     * El operador NOT LIKE (con contención).
     */
    NOT_LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final Expression<String> key = root.get(request.getKey());

            return builder.and(
                builder.notLike(
                    builder.upper(key),
                    "%" + request.getValue().toString().toUpperCase() + "%"
                ),
                predicate
            );
        }
    },

    /**
     * El operador IN.
     */
    IN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final var values = request.getValues();
            final CriteriaBuilder.In<Object> inClause = builder.in(root.get(request.getKey()));

            for(final var value : values) {
                inClause.value(request.getFieldType().parse(value.toString()));
            }

            return builder.and(inClause, predicate);
        }
    },

    /**
     * El operador NOT IN.
     */
    NOT_IN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final var values = request.getValues();
            final CriteriaBuilder.In<Object> inClause = builder.in(root.get(request.getKey()));

            for(final var value : values) {
                inClause.value(request.getFieldType().parse(value.toString()));
            }

            return builder.and(inClause.not(), predicate);
        }
    },

    /**
     * El operador BETWEEN.
     */
    BETWEEN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate) {
            final var value = request.getFieldType().parse(request.getValue().toString());
            final var valueTo = request.getFieldType().parse(request.getValueTo().toString());

            if(request.getFieldType() == FieldType.DATE) {
                final var startDate = (LocalDateTime) value;
                final var endDate = (LocalDateTime) valueTo;
                final Expression<LocalDateTime> key = root.get(request.getKey());

                return builder.and(
                    builder.and(
                        builder.greaterThanOrEqualTo(key, startDate),
                        builder.lessThanOrEqualTo(key, endDate)
                    ),
                    predicate
                );
            }

            if(request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                final var start = (Number) value;
                final var end = (Number) valueTo;
                final Expression<Number> key = root.get(request.getKey());

                return builder.and(
                    builder.and(
                        builder.ge(key, start),
                        builder.le(key, end)
                    ),
                    predicate
                );
            }

            return predicate;
        }
    };

    /**
     * Construye una consulta parametrizable contra la base de datos en base al
     * nombre de la columna, su tipo y el operador.
     * 
     * @param <T> El tipo parametrizable de la consulta
     * @param root El contenedor de la entidad sobre la que se formará la consulta
     * @param builder El constructor de la consulta
     * @param request El contenedor de los datos de la petición
     * @param predicate El contenedor de restricciones a tener en cuenta en la consulta
     * @return  El contenedor del predicado de la consulta generada
     */
    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder builder, FilterRequest request, Predicate predicate);

}
