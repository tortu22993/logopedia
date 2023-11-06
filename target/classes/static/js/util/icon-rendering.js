/**
 * Contiene funciones relativas a la renderización de iconos
 * de forma dinámica.
 * 
 * @author Enrique Escalante
 */

/**
 * Permite renderizar un icono en la etiqueta del campo o grupo, siempre
 * que éstos sean modificables.
 * @method renderWithinFormGroup
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.iconName El nombre del icono de FontAwesome a renderizar
 * @param {Boolean} wrapper.prepend Indica si el icono será añadido a la izquierda de la etiqueta
 */
export function renderWithinFormGroup({ iconName, prepend = false }) {
    const groups = [...document.querySelectorAll('.form-group, .form-floating')];
    const editableGroups = groups.filter(group => {
        const datetimeInputWrapper = group.closest('.flatpickr-input-container');
        let editableInput = false;
        let editableSelect = group.querySelector('.form-select:not([disabled])');

        if(!datetimeInputWrapper) {
            editableInput = group.querySelector('.form-control:not([disabled]):not([readonly])');
            editableSelect = group.querySelector('.form-select:not([disabled])');

            return editableInput || editableSelect;
        }

        const editableDateInput = datetimeInputWrapper.querySelector('input[type = "hidden"]:not([data-disabled])');
        const editableTimeInput = datetimeInputWrapper.querySelector('.flatpickr-input:not([data-disabled])');

        return editableDateInput || editableTimeInput;
    });
    const labels = editableGroups.map(group => group.querySelector('label'));

    labels.forEach(label => {
        const $icon = document.createElement('i');
        $icon.classList.add('fa', `fa-${iconName}`, prepend ? 'me-2' : 'ms-2');

        prepend
            ? label.prepend($icon)
            : label.append($icon);
    });
};