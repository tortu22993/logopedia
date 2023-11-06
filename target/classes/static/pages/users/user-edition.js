/**
 * Lógica de la vista 'user-edition.html'.
 *
 * @author Enrique Escalante
 */
import { MESSAGE_TYPES, showMessage } from "../../js/util/confirm-message.js";
import { CONTEXT_PATH } from "../../js/global.js";
import { changeControlsAppearanceOnEdit } from "../../js/theme/mandatory.js";

/**
 * El nombre del usuario.
 * @constant username
 */
const username = document.querySelector('#username')?.value;

if(!username?.trim().length) {
    document.querySelector('#saveButton').addEventListener('click', () => {
        const userEmail = document.querySelector('#email')?.value;
        const $formSave = document.querySelector('#formSave');

        if(userEmail) {
            showMessage({
                title: 'Crear usuario',
                message: `Se creará el usuario y se enviará un correo electrónico a <strong>${userEmail}</strong> para confirmar su cuenta. ¿Desea continuar?`,
                type: MESSAGE_TYPES.QUESTION,
                showMessage: true,
                confirmButtonText: 'Continuar',
                actionConfirmButton: () => {
                    $formSave.submit();
                }
            });

            return;
        }

        $formSave.submit();
    });
} else {
    document.querySelector('#saveButton').addEventListener('click', () => {
        document.querySelector('#formSave').submit();
    });
}

/**
 * Acción que se ejecuta cuando se hace clic sobre el botón
 * de cancelar los cambios realizados sobre el contacto.
 */
document.querySelector('#cancelButton')?.addEventListener('click', () => {
    showMessage({
        title: 'Cancelar cambios',
        message: 'Se cancelarán los cambios realizados sobre el usuario. ¿Desea continuar?',
        type: MESSAGE_TYPES.QUESTION,
        showMessage: true,
        confirmButtonText: 'Continuar',
        actionConfirmButton: () => {
            const redirectUrl = `${CONTEXT_PATH}/users/${username}`;

            window.location.replace(redirectUrl);
        }
    });
});

window.addEventListener('load', () => changeControlsAppearanceOnEdit());