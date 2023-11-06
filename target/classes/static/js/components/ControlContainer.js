import { NotInstantiableError } from "../errors/NotInstantiableError.js";
import { HtmlInputType } from "../model/HtmlInputType.js";

/**
 * Representa un contenedor de controles o campos. La estructura común es:
 * 1. Un contenedor que representa la columna flexible en una fila.
 * 2. Un contenedor que representa el grupo del campo (label + input, etc.).
 * 3. Una etiqueta <label> para mejorar la accesibilidad del campo.
 * 4. El propio campo.
 * @abstract
 * @class
 * 
 * @author Enrique Escalante
 */
export class ControlContainer {

    /**
     * Constructor vacío.
     * @constructor
     */
    constructor() {
        throw new NotInstantiableError();
    }

    /**
     * Crea una nueva instancia.
     * @static
     * @function
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros 
     * @param {String} wrapper.inputType El nombre del tipo de campo HTML a establecer
     * @param {String} wrapper.label La etiqueta que mejora la accesibilidad del campo HTML a establecer
     * @param {String} wrapper.inputId El ID del campo HTML a establecer
     * @param {String} wrapper.keyFieldIdentifier El Identificador del campo clave a establecer
     * @param {Boolean} wrapper.mandatory Indica si el campo HTML a establecer es obligatorio o no
     * @param {String} [wrapper.value] El valor del campo HTML a establecer
     * @returns {Object} Un objeto contenedor del HTML a renderizar y el tipo de campo HTML a inicializar
     */
    static newInstance({ inputType, label, inputId, keyFieldIdentifier, mandatory, value = '' }) {
        const htmlInputType = HtmlInputType.getTagFromInputType(inputType);
        const { template, tag, hasClosingTag } = htmlInputType;
        const labelToRender = (mandatory ? `<span class="text-danger">*</span> ${label}` : label);
        const closingTag = `${hasClosingTag ? `${value ?? ''}</${tag}>` : ''}`;
        const templateToRender = `${template} data-key-field="${keyFieldIdentifier}" ${value ? `value="${value}"` : ''}`;

        return {
            controlHTML: `
                <div class="col-xs-12">
                    <div class="form-group">
                        <label for="${inputId}" class="form-label" title="${label}">${labelToRender}</label>
                        <${tag} id="${inputId}" ${templateToRender}>${closingTag}
                    </div>
                </div>
            `,
            htmlInputType
        };
    }

    /**
     * Crea una nueva instancia y la añade como nodo hijo a un elemento determinado.
     * @static
     * @method
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros 
     * @param {String} wrapper.inputType El nombre del tipo de campo HTML a establecer
     * @param {String} wrapper.label La etiqueta que mejora la accesibilidad del campo HTML a establecer
     * @param {String} wrapper.inputId El ID del campo HTML a establecer
     * @param {String} wrapper.keyFieldIdentifier El Identificador del campo clave a establecer
     * @param {Boolean} wrapper.mandatory Indica si el campo HTML a establecer es obligatorio o no
     * @param {String} [wrapper.value] El valor del campo HTML a establecer
     * @param {Object} [wrapper.options] Las opciones de un campo selector a establecer
     * @param {HTMLElement} wrapper.wrapperElement El objeto padre al que se añadirá el campo
     */
    static appendNewInstanceTo({ inputType, label, inputId, keyFieldIdentifier, mandatory, value = '', options = {}, wrapperElement }) {
        const { controlHTML, htmlInputType } = ControlContainer.newInstance({
            inputType,
            label,
            inputId,
            keyFieldIdentifier,
            mandatory,
            value
        });

        wrapperElement?.insertAdjacentHTML('beforeend', controlHTML);
        htmlInputType?.init({
            selector: `#${inputId}`,
            options
        });
    }

}