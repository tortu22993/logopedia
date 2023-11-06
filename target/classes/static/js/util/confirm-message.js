/**
 * Contiene funcionalidades relativas a mostrar
 * mensajes de información a través del plugin
 * Sweet Alert 2.
 * La función <i>Swal</i> proviene de dicho plugin.
 * @author Enrique Escalante
 */

/**
 * Los tipos de mensajes.
 * @constant MESSAGE_TYPES
 */
export const MESSAGE_TYPES = {
    SUCCESS: { icon: 'success', bootstrapClass: 'success' },
    ERROR: { icon: 'error', bootstrapClass: 'danger' },
    WARNING: { icon: 'warning', bootstrapClass: 'warning' },
    INFO: { icon: 'info', bootstrapClass: 'info' },
    QUESTION: { icon: 'question', bootstrapClass: 'primary' },
};

/**
 * Muestra un mensaje informativo al usuario.
 * @method showMessage
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.title El título del mensaje
 * @param {String} wrapper.message El contenido del mensaje
 * @param {String} [wrapper.type] El tipo de mensaje
 * @param {Boolean} [wrapper.showCancelButton] Indica si muestra el botón de cancelar
 * @param {String} [wrapper.confirmButtonText] El texto del botón de confirmación
 * @param {String} [wrapper.cancelButtonText] El texto del botón de cancelación
 * @param {Function} [wrapper.actionConfirmButton] La función a ejecutar tras confirmar el mensaje
 */
export function showMessage({ title, message, type = MESSAGE_TYPES.INFO, showCancelButton = true, confirmButtonText = '', cancelButtonText = 'Cancelar', actionConfirmButton = () => {} }) {
    Swal.fire({
        title,
        html: message,
        showCancelButton,
        confirmButtonText,
        cancelButtonText,
        customClass: {
            confirmButton: `btn btn-${type.bootstrapClass} me-2`,
            cancelButton: 'btn btn-phoenix-secondary'
        },
        buttonsStyling: false,
        allowOutsideClick: false,
        heightAuto: false,
        focusCancel: true
    }).then(result => {
        if(result.isConfirmed) {
            actionConfirmButton();
        }
    });
}