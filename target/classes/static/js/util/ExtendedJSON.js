import { NotInstantiableError } from '../errors/NotInstantiableError.js';

/**
 * Extiende o añade funcionalidades a la clase {@link JSON} propia
 * del motor de JavaScript.
 * @abstract
 * @class
 * 
 * @author Enrique Escalante
 */
export class ExtendedJSON {

    /**
     * Constructor vacío.
     * @constructor
     */
    constructor() {
        throw new NotInstantiableError();
    }

    /**
     * Formatea un JSON contenido en una cadena de caracteres a un objeto JSON
     * propiamente dicho. El JSON contenido está formateado con comillas simples
     * en lugar de dobles, dando lugar a un JSON inválido. No obstante, esta función
     * arregla exactamente eso.
     * @static
     * @function
     * 
     * @param {String} value El valor a formatear
     * @returns {JSON} Un objeto JSON formado a partir de la cadena de caracteres
     */
    static parseWithSingleQuotes(value) {
        const regexp = /\'/g;
        const replacement = '"';
        const fixedValue = value?.replace(regexp, replacement) ?? '{}';

        return JSON.parse(fixedValue);
    }

}