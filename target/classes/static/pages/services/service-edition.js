/**
 * Lógica de la vista 'services-edition.html'.
 * 
 * @author Enrique Escalante
 */
import { CONTEXT_PATH } from "../../js/global.js";
import { changeControlsAppearanceOnEdit } from "../../js/theme/mandatory.js";
import { MESSAGE_TYPES, showMessage } from "../../js/util/confirm-message.js";



/**
 * Acción que se ejecuta cuando se hace clic sobre el botón
 * de cancelar los cambios realizados sobre el servicio.
 */
document.querySelector('#cancelButton')?.addEventListener('click', () => {
    showMessage({
        title: 'Cancelar cambios',
        message: 'Se cancelarán los cambios realizados sobre el puesto de trabajo. ¿Desea continuar?',
        type: MESSAGE_TYPES.QUESTION,
        showMessage: true,
        confirmButtonText: 'Continuar',
        actionConfirmButton: () => {
            const id = document.querySelector('#id')?.value;
            const rootUrl = `${CONTEXT_PATH}/services`;
            const redirectUrl = id
                ? `${rootUrl}/${id}`
                : rootUrl;

            window.location.replace(redirectUrl);
        }
    });
});

window.addEventListener('load', () => changeControlsAppearanceOnEdit());