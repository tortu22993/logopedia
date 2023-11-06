/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de contactos.
 *
 * @author Enrique Escalante
 */
import { searchContacts } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de contactos.
 *
 * @author Enrique Escalante
 */
export class ContactSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de contactos.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.contactInputId El ID del elemento input HTML del campo del contacto
     * @param {String} wrapper.contactInputHiddenId El elemento input hidden HTML del ID del contacto
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ contactInputId, contactInputHiddenId, urlParams = null }) {
        const $contactInputHidden = document.getElementById(contactInputHiddenId);
        const { input: $contactInput } = createAutocomplete({
            selector: `#${contactInputId}`,
            dataSource: searchContacts,
            urlParams,
            keys: ['fullName'],
            displayKey: 'fullName'
        });

        $contactInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: contactId, fullName: contactFullName } = feedback.selection.value;

            $contactInputHidden.value = contactId;
            $target.value = contactFullName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $contactInputHidden.value = '';
            }
        };

        $contactInput.addEventListener('blur', event => onClearValue(event));
        $contactInput.addEventListener('search', event => onClearValue(event));
    }

}