/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de servicios.
 *
 * @author Enrique Escalante
 */
import { searchServices } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de servicios.
 *
 * @author Enrique Escalante
 */
export class ServiceSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de servicios.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.serviceInputId El ID del elemento input HTML del campo del servicio
     * @param {String} wrapper.serviceInputHiddenId El elemento input hidden HTML del ID del servicio
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ serviceInputId, serviceInputHiddenId, urlParams = null }) {
        const $serviceInputHidden = document.getElementById(serviceInputHiddenId);
        const { input: $serviceInput } = createAutocomplete({
            selector: `#${serviceInputId}`,
            dataSource: searchServices,
            urlParams,
            keys: ['name'],
            displayKey: 'name',
            showId: true
        });

        $serviceInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: serviceId, name: serviceName } = feedback.selection.value;

            $serviceInputHidden.value = serviceId;
            $target.value = serviceName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $serviceInputHidden.value = '';
            }
        };

        $serviceInput.addEventListener('blur', event => onClearValue(event));
        $serviceInput.addEventListener('search', event => onClearValue(event));
    }

}