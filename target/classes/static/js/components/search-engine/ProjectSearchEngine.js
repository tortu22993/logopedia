/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de proyectos.
 *
 * @author Enrique Escalante
 */
import { searchProjects } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de proyectos.
 *
 * @author Enrique Escalante
 */
export class ProjectSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de proyectos.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.projectInputId El ID del elemento input HTML del campo del proyecto
     * @param {String} wrapper.projectInputHiddenId El elemento input hidden HTML del ID del proyecto
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ projectInputId, projectInputHiddenId, urlParams = null }) {
        const $projectInputHidden = document.getElementById(projectInputHiddenId);
        const { input: $projectInput } = createAutocomplete({
            selector: `#${projectInputId}`,
            dataSource: searchProjects,
            urlParams,
            keys: ['subject'],
            displayKey: 'subject',
            showId: true
        });

        $projectInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: projectId, subject: projectSubject } = feedback.selection.value;

            $projectInputHidden.value = projectId;
            $target.value = projectSubject;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $projectInputHidden.value = '';
            }
        };

        $projectInput.addEventListener('blur', event => onClearValue(event));
        $projectInput.addEventListener('search', event => onClearValue(event));
    }

}