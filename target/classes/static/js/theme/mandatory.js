/**
 * Este fichero contiene lógica necesaria y obligatoria para
 * el correcto funcionamiento de la aplicación.
 * 
 * @author Enrique Escalante
 */
import { Loader } from "../components/Loader.js";

/**
 * Crea un loader y lo añade al cuerpo de la página.
 * @function insertLoaderIntoBody
 */
export function insertLoaderIntoBody() {
    document.querySelector('body').insertAdjacentHTML('beforeend', Loader.create());
}

/**
 * Hace que la apariencia de los campos editables o controles cambie cuando
 * su valor es modificado. Esto hace mejorar la accesibilidad y el feedback
 * proporcionado al usuario.
 * @method changeControlsAppearanceOnEdit
 */
export function changeControlsAppearanceOnEdit() {
    const controls = '.form-control, .form-select, .form-check-input, .flatpickr-input-container input[type = "hidden"]';

    document.querySelectorAll(controls)?.forEach($element => {
        const isAutocompleteLocation = $element => {
            return $element.closest('.auto-search-wrapper');
        };

        if(isAutocompleteLocation($element)) {
            return;
        };

        const originalValue = $element.value;
        const isChoicesSelect = $element => {
            return $element.parentNode.classList.contains('choices__inner');
        };

        const isCheckbox = $element => {
            return $element.classList.contains('form-check-input');
        };

        const isFlatpickrInput = $element => {
            return $element.closest('.flatpickr-input-container');
        };

        $element.addEventListener('change', event => {
            const $target = event.currentTarget;
            const changedValue = $target.value;
            const className = 'control-changed-value';
            
            if(!isCheckbox($target)) {
                const $flatpickrContainer = isFlatpickrInput($target);
                const $container = $flatpickrContainer
                    ? $flatpickrContainer.querySelector('.form-control')
                    : isChoicesSelect($target)
                        ? $target.parentNode
                        : $target;

                $container.classList.toggle(className, originalValue !== changedValue);
            } else {
                $target.parentNode.classList.toggle(className);
            }
        });
    });
}

/**
 * Acción que se ejecuta cuando se hace clic en enlaces que no abren nuevas pestañas.
 */
document.querySelectorAll('a:not([href ^= "mailto:"]):not([href ^= "tel:"]):not([target = "_blank"]):not([role = "button"]):not([data-show-message])')?.forEach($element => {
    $element.addEventListener('click', () => insertLoaderIntoBody());
});

/**
 * Acción que se ejecuta cuando se ejecutan formularios que no abren
 * nuevas pestañas.
 */
document.querySelectorAll('form:not([target = "_blank"])')?.forEach($element => {
    $element.addEventListener('submit', () => insertLoaderIntoBody());
});

/**
 * Acción que se ejecuta cuando se carga la página.
 */
window.addEventListener('load', () => {
    const hiddenIframeName = 'hidden-iframe-protocols';
    const $protocolLinks = document.querySelectorAll('a[href ^= "mailto:"], a[href ^= "tel:"]');

    $protocolLinks?.forEach($element => $element.target = hiddenIframeName);
    document.body.insertAdjacentHTML('beforeend', `<iframe name="${hiddenIframeName}" title="${hiddenIframeName}" class="d-none position-absolute"></iframe>`);
});

/**
 * Acción que se ejecuta cuando se recarga la página.
 */
window.onbeforeunload = () => insertLoaderIntoBody();