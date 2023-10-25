/**
 * Contiene funcionalidades útiles que extienden y centralizan
 * las características del plugin autoComplete.js.
 * 
 * @author Enrique Escalante
 */

/**
 * Crea un nuevo buscador a partir del plugin AutoComplete.js.
 * @function createAutocomplete
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.selector El selector del elemento HTML
 * @param {Array<any> | String | Function} wrapper.dataSource La fuente de los datos a mostrar
 * @param {Map<String, String | Number | Boolean>} [wrapper.urlParams] Los parámetros a enviar en la fuente de los datos
 * @param {Array<String>} [wrapper.keys] Las llaves o identificadores en caso de que la fuente de datos sea un array de objetos
 * @param {String} wrapper.displayKey La llave o identificador a utilizar para mostrar en las opciones resultantes
 * @param {Boolean} [wrapper.showId] Indica si se muestra el ID en la parte derecha por cada resultado
 * @returns {Object} Una nueva instancia contenedora del buscador autoComplete.js
 */
export function createAutocomplete({ selector, dataSource, urlParams = null, keys = [], displayKey, showId = false }) {
    const isAwaitableDataSource = typeof dataSource === 'function' || dataSource instanceof Promise;

    const searcher = new autoComplete({
        selector: selector,
        debounce: 300,
        data: {
            src: isAwaitableDataSource ? async (query) => await dataSource(query, urlParams) : dataSource,
            keys
        },
        wrapper: true,
        diacritics: true,
        resultsList: {
            element: (list, data) => {
                list.classList.add('autocomplete_result_list');

                if(!data.results.length) {
                    const message = document.createElement('li');

                    message.setAttribute('class', 'no_result');
                    message.innerHTML = `No se encontraron resultados para '${data.query}'.`;

                    list.prepend(message);
                } else {
                    const $info = document.createElement('p');
                    const { results: displayingResults, matches: totalResults } = data;

                    $info.classList.add('text-center', 'fs--1', 'mb-0');
                    $info.innerHTML = `Mostrando <strong>${displayingResults.length}</strong> de <strong>${totalResults.length}</strong> resultados.`;

                    list.prepend($info);
                }
            },
            tabSelect: true,
            maxResults: 20,
            noResults: true
        },
        resultItem: {
            element: (item, data) => {
                const { value } = data;
                const keyContent = showId
                    ? `<span>${value.id}</span>`
                    : '';

                item.classList.add('d-flex', 'justify-content-between', 'align-items-center');
                item.innerHTML = `
                    <span class="text-overflow text-uppercase">${data.match}</span>
                    ${keyContent}
                `;
            },
            highlight: true
        }
    });

    searcher.input.addEventListener('results', event => {
        const { results } = event.detail;

        if(results?.length) {
            setTimeout(() => searcher.goTo(0), 5);
        }
    });

    return searcher;
}