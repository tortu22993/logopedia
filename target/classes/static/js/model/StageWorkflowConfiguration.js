/**
 * Establece la estructura del valor de un campo clave dentro de un flujo
 * de trabajo para la configuración de una etapa.
 * @class
 * 
 * @author Enrique Escalante
 */
export class StageWorkflowConfiguration {

    /**
     * Constructor con parámetros.
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros 
     * @param {String | Number} wrapper.keyFieldId El ID del campo clave
     * @param {String} wrapper.keyFieldName El nombre del campo clave
     * @param {String} wrapper.keyFieldHtmlInputType El nombre del tipo de campo HTML del campo clave
     * @param {String} wrapper.keyFieldHtmlInputTypeDescription El nombre del tipo de campo HTML del campo clave
     * @param {Boolean} [wrapper.mandatory] Indica si el campo es obligatorio o no
     * @param {String} [wrapper.keyFieldValue] El valor del campo
     */
    constructor({ keyFieldId, keyFieldName, keyFieldHtmlInputType, keyFieldHtmlInputTypeDescription, mandatory = false, keyFieldValue = '' }) {
        this._keyFieldId = keyFieldId;
        this._keyFieldName = keyFieldName;
        this._keyFieldHtmlInputType = keyFieldHtmlInputType;
        this._keyFieldHtmlInputTypeDescription = keyFieldHtmlInputTypeDescription;
        this._mandatory = mandatory ?? false;
        this._keyFieldValue = keyFieldValue ?? '';
    }

    /**
     * Obtiene el ID del campo clave.
     * @function
     * 
     * @returns {String | Number} El ID del campo clave
     */
    get keyFieldId() {
        return this._keyFieldId;
    }

    /**
     * Establece el valor del ID del campo clave.
     * @method
     * 
     * @param {String | Number} keyFieldId El ID del campo clave a establecer
     */
    set keyFieldId(keyFieldId) {
        this._keyFieldId = keyFieldId;
    }

    /**
     * Obtiene el nombre del campo clave.
     * @function
     * 
     * @returns {String} El nombre del campo clave
     */
    get keyFieldName() {
        return this._keyFieldName;
    }

    /**
     * Establece el valor del nombre del campo clave.
     * @method
     * 
     * @param {String} keyFieldName El nombre del campo clave a establecer
     */
    set keyFieldName(keyFieldName) {
        this._keyFieldName = keyFieldName;
    }

    /**
     * Obtiene el tipo del campo HTML del campo clave.
     * @function
     * 
     * @returns {String} El tipo del campo HTML del campo clave
     */
    get keyFieldHtmlInputType() {
        return this._keyFieldHtmlInputType;
    }

    /**
     * Establece el valor del tipo del campo HTML del campo clave.
     * @method
     * 
     * @param {String} keyFieldHtmlInputType El tipo del campo HTML del campo clave a establecer
     */
    set keyFieldHtmlInputType(keyFieldHtmlInputType) {
        this._keyFieldHtmlInputType = keyFieldHtmlInputType;
    }

    /**
     * Obtiene la descripción del tipo del campo HTML del campo clave.
     * @function
     * 
     * @returns {String} La descripción del tipo del campo HTML del campo clave
     */
    get keyFieldHtmlInputTypeDescription() {
        return this._keyFieldHtmlInputTypeDescription;
    }

    /**
     * Establece el valor de la descripción del tipo del campo HTML del campo clave.
     * @method
     * 
     * @param {String} keyFieldHtmlInputTypeDescription La descripción del tipo del campo HTML del campo clave a establecer
     */
    set keyFieldHtmlInputTypeDescription(keyFieldHtmlInputTypeDescription) {
        this._keyFieldHtmlInputTypeDescription = keyFieldHtmlInputTypeDescription;
    }

    /**
     * Obtiene el valor de la obligatoriedad del campo.
     * @function
     * 
     * @returns {Boolean} El valor de la obligatoriedad del campo
     */
    get mandatory() {
        return this._mandatory;
    }

    /**
     * Establece el valor de la obligatoriedad del campo.
     * @method
     * 
     * @param {Boolean} mandatory El valor de la obligatoriedad del campo a establecer
     */
    set mandatory(mandatory) {
        this._mandatory = mandatory;
    }

    /**
     * Obtiene el valor del campo clave.
     * @function
     * 
     * @returns {String} El valor del campo clave
     */
    get keyFieldValue() {
        return this._keyFieldValue;
    }

    /**
     * Establece el valor del valor del campo clave.
     * @method
     * 
     * @param {String} keyFieldValue El valor del campo clave a establecer
     */
    set keyFieldValue(keyFieldValue) {
        this._keyFieldValue = keyFieldValue;
    }

}