/**
 * Contiene funcionalidades relativas a la inyección
 * de estilos CSS a través de JavaScript.
 * 
 * @author Enrique Escalante
 */

/**
 * La URL raíz de los ficheros.
 * @constant BASE_PATH
 */
const BASE_PATH = '/pages';

/**
 * Inyecta estilos CSS contenidos en un fichero que
 * recibe como parámetro a través de JavaScript.
 * El parámetro que recibe es una ruta relativa, la
 * cual es concatenada a la ruta raíz '/pages'.
 * @method injectCSS
 * 
 * @param {String} cssFilepath La ruta del fichero a inyectar 
 */
export function injectCSS(cssFilepath) {
    const $link = document.createElement('link');

    $link.rel = 'stylesheet';
    $link.href = `${BASE_PATH}/${cssFilepath}`;

    document.querySelector('head').appendChild($link);
};