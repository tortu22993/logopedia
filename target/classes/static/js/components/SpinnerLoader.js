import { NotInstantiableError } from "../errors/NotInstantiableError.js";

/**
 * Permite crear un cargador o loader.
 * @class
 * 
 * @author Enrique Escalante
 */
export class SpinnerLoader {

    /**
     * Constructor vacío.
     * @constructor
     */
    constructor() {
        throw new NotInstantiableError();
    }

    /**
     * Representa los tipos de cargadores.
     * @static
     * @readonly
     */
    static Type = Object.freeze({
        BORDER: 'border',
        GROW: 'grow'
    });

    /**
     * Crea una nueva instancia.
     * @static
     * @function
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {Boolean} [wrapper.wrapper] Indica si el loader estará contenido en otro elemento
     * @param {Array<String>} [wrapper.wrapperClasses] Indica las clases CSS del elemento contenedor del loader
     * @param {Boolean} [wrapper.small] Indica si el tamaño del loader será pequeño o no
     * @param {SpinnerLoader.Type} [wrapper.type] Indica el tipo del loader
     * @param {HTMLElement} wrapper.parent El elemento HTML padre del contenedor
     * @returns 
     */
    static newInstance({ wrapper = false, wrapperClasses = [], small = false, type = this.Type.BORDER, parent }) {
        const loaderContainer = document.createElement('div');
        const loader = document.createElement('span');
        const loaderContent = document.createTextNode('Cargando...');

        loaderContainer.classList.add(`spinner-${type}`, (small ? `spinner-${type}-sm` : ''));
        loaderContainer.setAttribute('role', 'status');

        loader.classList.add('visually-hidden');
        loader.appendChild(loaderContent);

        loaderContainer.appendChild(loader);

        if(!wrapper) {
            parent.appendChild(loaderContainer);

            return loaderContainer;
        }

        const loaderWrapper = document.createElement('div');

        loaderWrapper.classList.add(...wrapperClasses);
        loaderWrapper.appendChild(loaderContainer);
        parent.appendChild(loaderWrapper);

        return loaderWrapper;
    }

}