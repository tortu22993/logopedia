/**
 * Contiene funciones útiles a procesar tras la
 * ejecuciones de formularios.
 * 
 * @author Enrique Escalante
 */

/**
 * Deshabilita un botón específico y lo establece en estado
 * de carga. Es muy útil para cuando, por ejemplo, se ejecuta
 * un formulario.
 * @method setLoadingButton
 * 
 * @param {HTMLButtonElement} $button El botón en el que se establecerá la carga
 */
export function setLoadingButton($button) {
    $button.disabled = true;
    $button.innerHTML = `
        <div class="spinner-grow spinner-grow-sm" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    `;
};
