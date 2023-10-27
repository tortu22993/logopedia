/**
 * Lógica de la vista 'user-detail.html'.
 *
 * @author Enrique Escalante
 */
import { MESSAGE_TYPES, showMessage } from "../../js/util/confirm-message.js";

/**
 * El botón que permite enviar un nuevo correo electrónico de invitación al usuario.
 * @constant $sendButton
 */
const $sendButton = document.querySelector('#sendNewEmailInvitationButton');

/**
 * El formulario que permite enviar un nuevo correo electrónico de invitación al usuario.
 * @constant $sendForm
 */
const $sendForm = document.querySelector('#sendNewEmailInvitationForm');

$sendButton.addEventListener('click', () => {
    const userEmail = document.querySelector('meta[name = "user-email"]')?.getAttribute('content') ?? '';

    if(userEmail) {
        showMessage({
            title: 'Crear usuario',
            message: `Se reenviará un correo electrónico a <strong>${userEmail}</strong> para confirmar su cuenta. ¿Desea continuar?`,
            type: MESSAGE_TYPES.QUESTION,
            showMessage: true,
            confirmButtonText: 'Continuar',
            actionConfirmButton: () => {
                $sendForm.submit();
            }
        });

        return;
    }
});