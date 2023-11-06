/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de clientes.
 *
 * @author Enrique Escalante
 */
import { searchCustomers } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de clientes.
 *
 * @author Enrique Escalante
 */
export class CustomerSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de clientes.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.customerInputId El ID del elemento input HTML del campo del cliente
     * @param {String} wrapper.customerInputHiddenId El elemento input hidden HTML del ID del cliente
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ customerInputId, customerInputHiddenId, urlParams = null }) {
        const $customerInputHidden = document.getElementById(customerInputHiddenId);
        const { input: $customerInput } = createAutocomplete({
            selector: `#${customerInputId}`,
            dataSource: searchCustomers,
            keys: ['name'],
            displayKey: 'name'
        });

        $customerInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: customerId, name: customerName } = feedback.selection.value;

            $customerInputHidden.value = customerId;
            $target.value = customerName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $customerInputHidden.value = '';
            }
        };

        $customerInput.addEventListener('blur', event => onClearValue(event));
        $customerInput.addEventListener('search', event => onClearValue(event));
    }

}