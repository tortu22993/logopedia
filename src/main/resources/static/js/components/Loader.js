import { NotInstantiableError } from "../errors/NotInstantiableError.js";

/**
 * Permite crear el cargador o loader principal de la aplicación (marca de empresa).
 * @class
 * 
 * @author Enrique Escalante
 */
export class Loader {

    /**
     * Constructor vacío.
     * @constructor
     */
    constructor() {
        throw new NotInstantiableError();
    }
    
    /**
     * Devuelve una estructura HTML de un loader contenida
     * en una cadena de caracteres.
     * @static
     * @function
     * 
     * @returns Una estructura HTML de un loader contenida en una cadena de caracteres
     */
    static create() {
        return `
            <div class="loader-container">
                <div class="loadingio-spinner-pulse-f8yc493qfr5">
                    <div class="ldio-h5w7hnvgni">
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                </div>
            </div>
        `;
    }

}