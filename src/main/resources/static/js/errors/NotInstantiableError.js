/**
 * Error o excepción que maneja la generación de instancias desde clases abstractas.
 * @class
 * 
 * @author Enrique Escalante
 */
export class NotInstantiableError extends Error {

    /**
     * Constructor con parámetros.
     * @constructor
     * 
     * @param {String} [message] El mensaje de error a establecer
     */
    constructor(message = 'Abstract classes can not be instantiated.') {
        super(message);
        this.name = 'NotInstantiableError';
    }

}