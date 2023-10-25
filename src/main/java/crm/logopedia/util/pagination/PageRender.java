package crm.logopedia.util.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Renderiza un listado de objetos de forma paginada, de forma que controla el número
 * de resultados por página y el número de páginas totales a mostrar.
 * 
 * @author Enrique Escalante
 *
 * @param <T> Parámetro genérico que hace referencia a la clase a procesar
 */
@Getter
@Setter
public class PageRender<T> {
    
    /**
     * La URL desde la que se ha realizado la petición de obtener una lista paginada.
     */
    private String url;

    /**
	 * La lista paginada de objetos resultante.
	 */
    private Page<T> page;

    /**
	 * El número total de páginas obtenidas.
	 */
    private int totalPages;

    /**
	 * El número de elementos por página a mostrar.
	 */
    private int numberOfElementsPerPage;

    /**
	 * El número de la página actual.
	 */
    private int currentPage;

    /**
	 * Las páginas de la lista paginada.
	 */
    private List<PageItem> pages;

    /**
	 * Constructor con parámetros.
	 * 
	 * @param url La URL de la petición
	 * @param page La lista paginada de objetos obtenida
	 */
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;

        pages = new ArrayList<>();
        numberOfElementsPerPage = page.getSize();
        totalPages = page.getTotalPages();
        currentPage = page.getNumber() + 1;

        int from;
        int to;

        if(totalPages <= numberOfElementsPerPage) {
            from = 1;
            to = totalPages;
        } else {
            final var halfOfElementsPerPage = numberOfElementsPerPage / 2;

            if(currentPage <= halfOfElementsPerPage) {
                from = 1;
                to = numberOfElementsPerPage;
            } else if(currentPage >= totalPages - halfOfElementsPerPage) {
                from = totalPages - numberOfElementsPerPage + 1;
                to = numberOfElementsPerPage;
            } else {
                from = currentPage - halfOfElementsPerPage;
                to = numberOfElementsPerPage;
            }
        }

        for(int i = 0; i < to; i++) {
            final var value = from + i;
            final var pageItem = new PageItem(value, currentPage == value);

            pages.add(pageItem);
        }
    }

    /**
	 * Devuelve el valor de la propiedad {@link PageRender#url}.
	 * 
	 * @return La URL de la petición
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * Devuelve el valor de la propiedad {@link PageRender#totalPages}.
	 * 
	 * @return El número total de páginas
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * Devuelve el valor de la propiedad {@link PageRender#numberOfElementsPerPage}.
	 * 
	 * @return El número de elementos a mostrar por página
	 */
	public int getNumberOfElementsPerPage() {
		return numberOfElementsPerPage;
	}

    /**
	 * Devuelve el número de elementos de la página actual.
	 * 
	 * @return El número de elementos contenidos en la página actual
	 */
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    /**
	 * Devuelve el número total de elementos contenidos en todas las páginas.
	 * 
	 * @return El número total de elementos
	 */
    public long getTotalElements() {
        return page.getTotalElements();
    }

    /**
	 * Devuelve el valor de la propiedad {@link PageRender#currentPage}.
	 * 
	 * @return El número de la página actual
	 */
	public int getCurrentPage() {
		return currentPage;
	}

    /**
	 * Obtiene el número del primer elemento de la página actual con respecto
	 * a su posición en la lista completa.
	 * 
	 * @return El número del primer elemento de la página actual
	 */
    public int getFirstElementNumberOfCurrentPage() {
        final var elements = page.getContent();
        var result = 0;

        if(!elements.isEmpty()) {
            final var firstElement = elements.get(0);
            result = (elements.indexOf(firstElement) + 1) + page.getNumber() * numberOfElementsPerPage;
        }

        return result;
    }

    /**
	 * Obtiene el número del último elemento de la página actual con respecto
	 * a su posición en la lista completa.
	 * 
	 * @return El número del último elemento de la página actual
	 */
    public int getLastElementNumberOfCurrentPage() {
        final var elements = page.getContent();
        var result = 0;

        if(!elements.isEmpty()) {
            final var lastElement = elements.get(elements.size() - 1);
            result = (elements.indexOf(lastElement) + 1) + page.getNumber() * numberOfElementsPerPage;
        }

        return result;
    }

    /**
	 * Devuelve el valor de la propiedad {@link PageRender#pages}.
	 * 
	 * @return Las páginas de la lista paginada
	 */
	public List<PageItem> getPages() {
		return pages;
	}

    /**
	 * Indica si la página actual es la primera página.
	 * 
	 * @return Verdadero si lo es y falso en caso contrario
	 */
    public boolean isFirst() {
        return page.isFirst();
    }

    /**
	 * Indica si la página actual es la última página.
	 * 
	 * @return Verdadero si lo es y falso en caso contrario
	 */
    public boolean isLast() {
        return page.isLast();
    }

    /**
	 * Indica si la página actual tiene una página anterior.
	 * 
	 * @return Verdadero si la tiene y falso en caso contrario
	 */
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    /**
	 * Indica si la página actual tiene una página siguiente.
	 * 
	 * @return Verdadero si la tiene y falso en caso contrario
	 */
    public boolean hasNext() {
        return page.hasNext();
    }

    /**
     * Obtiene un objeto para renderizar una vista específica con
     * base a una URL dada y una serie de parámetros relativos
     * a la paginación de registros.
     *
     * @param <S> Parametriza el tipo de objeto a renderizar
     * @param urlToRender La URL que renderiza la vista deseada
     * @param collection La colección de datos a renderizar
     * @return Un objeto contenedor de los registros paginados para la URL indicada
     */
    public static <S> PageRender<S> newInstance(@NonNull String urlToRender, @NonNull Page<S> collection) {
        return new PageRender<>(urlToRender, collection);
    }

}
