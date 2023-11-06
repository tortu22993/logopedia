/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de usuarios.
 *
 * @author Enrique Escalante
 */
import { searchUsers } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de usuarios.
 *
 * @author Enrique Escalante
 */
export class UserSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de usuarios.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.userInputId El ID del elemento input HTML del campo del usuario
     * @param {String} wrapper.userInputHiddenId El elemento input hidden HTML del ID del usuario
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ userInputId, userInputHiddenId, urlParams = null }) {
        const $userInputHidden = document.getElementById(userInputHiddenId);
        const { input: $userInput } = createAutocomplete({
            selector: `#${userInputId}`,
            dataSource: searchUsers,
            keys: ['fullName'],
            displayKey: 'fullName',
            showId: false
        });

        $userInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { username: username, fullName: fullName } = feedback.selection.value;
           
            $userInputHidden.value = username;
            $target.value = fullName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $userInputHidden.value = '';
            }
        };

        $userInput.addEventListener('blur', event => onClearValue(event));
        $userInput.addEventListener('search', event => onClearValue(event));
    }

}