/**
 * Contiene funcionalidades extra del plugin Choices.js.
 * 
 * @author Enrique Escalante.
 */
import { isObjectEmpty } from "../../js/util/util.js";

/**
 * Inicializa un select a través del plugin Choices.js.
 * @function
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.selector El selector del select a inicializar
 * @param {Object} [wrapper.options] El objeto contenedor de las opciones a establecer
 * @returns {Choices} Una nueva instancia generada por el plugin Choices
 */
export function initializeChoicesSelect({ selector, options = {} }) {
    const element = document.querySelector(selector);
    const defaultParameters = {
        shouldSort: false,
        itemSelectText: ''
    };

    if(isObjectEmpty(options)) {
        return new Choices(element, {
            choices: [],
            ...defaultParameters
        });
    }

    const { currentValue, items } = options;
    const defaultChoice = {
        value: '',
        label: '--- Seleccionar ---',
        selected: !currentValue
    };
    const choices = items?.map(option => ({
        value: option,
        label: option,
        selected: (option === currentValue)
    }));

    return new Choices(element, {
        choices: [defaultChoice, ...choices],
        ...defaultParameters
    });
}