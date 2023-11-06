/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de colas de trabajo.
 *
 * @author Enrique Escalante
 */
import { searchWorkQueues } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de colas de trabajo.
 *
 * @author Enrique Escalante
 */
export class WorkQueuesSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de colas de trabajo.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.workQueueInputId El ID del elemento input HTML del campo del usuario
     * @param {String} wrapper.workQueueInputHiddenId El elemento input hidden HTML del ID del usuario
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ workQueueInputId, workQueueInputHiddenId, urlParams = null }) {
        const $workQueueInputHidden = document.getElementById(workQueueInputHiddenId);
        const { input: $workQueueInput } = createAutocomplete({
            selector: `#${workQueueInputId}`,
            dataSource: searchWorkQueues,
            keys: ['name'],
            displayKey: 'name',
            showId: false
        });

        $workQueueInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: workQueueId, name: workQueueName } = feedback.selection.value;

            $workQueueInputHidden.value = workQueueId;
            $target.value = workQueueName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $workQueueInputHidden.value = '';
            }
        };

        $workQueueInput.addEventListener('blur', event => onClearValue(event));
        $workQueueInput.addEventListener('search', event => onClearValue(event));
    }

}