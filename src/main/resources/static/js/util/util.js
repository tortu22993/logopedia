/**
 * Contiene lógica recurrente en prácticamente en cualquier
 * fichero de JavaScript de la aplicación.
 * 
 * @author Enrique Escalante
 */

/**
 * Envuelve un elemento HTML en un nuevo elemento.
 * @method wrapElement
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {HTMLElement} wrapper.$element El elemento HTML a ser envuelto
 * @param {Array<String>} wrapper.classes Las clases CSS a añadir al nuevo elemento
 */
export function wrapElement({ $element, classes }) {
    const $wrapper = document.createElement('div');
    
    $wrapper.classList.add(...classes);
    $element.parentNode.insertBefore($wrapper, $element);

    $wrapper.appendChild($element);
}

/**
 * Permite vincular un elemento HTML añadido de forma dinámica a un evento determinado.
 * @method
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.eventType El tipo de evento a vincular
 * @param {HTMLElement} wrapper.ancestorElement El elemento HTML padre del elemento HTML a vincular
 * @param {String} wrapper.targetElementSelector El selector del elemento HTML a vincular
 * @param {Function} wrapper.listenerFunction La función a ejecutar en el evento 
 */
export function delegateEvent({ eventType, ancestorElement, targetElementSelector, listenerFunction }) {
    ancestorElement.addEventListener(eventType, event => {
        const eventTarget = event.target;

        if(eventTarget?.matches(targetElementSelector)) {
            (listenerFunction)(event);
        }
    });
}

/**
 * Genera una ID aleatorio.
 * @function
 * 
 * @returns {String} Un ID aleatorio
 */
export function generateRandomId() {
    const radix = 36;
    const currentDate = Date.now().toString(radix);
    const randomness = Math.random().toString(radix).substring(2);

    return currentDate + randomness;
}

/**
 * Limpia el contenido de un elemento HTML.
 * @method
 * 
 * @param {HTMLElement} element El elemento HTML cuyo contenido será borrado
 */
export function emptyElementContent(element) {
    element.innerHTML = '';
}

/**
 * Comprueba si un objeto está vacío.
 * @function
 * 
 * @param {Object} object El objeto a comprobar
 * @returns {Boolean} Verdadero si el objeto está vacío o falso en caso contrario
 */
export function isObjectEmpty(object) {
    if(!object) {
        return true;
    }

    return Object.keys(object).length === 0;
}