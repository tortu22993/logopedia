/**
 * Lógica de la vista 'job-position-list.html'.
 * 
 * @author Enrique Escalante
 */
import { configDynamicTable } from '../../js/util/dynamic-table.js';

/**
 * El formulario de filtros.
 * @constant $formFilters
 */
const $formFilters = document.querySelector('#formFilters');

/**
 * Los nombres de los campos de la tabla a gestionar.
 * @constant valueNames
 */
const valueNames = [
    'name'
];

/**
 * Las opciones de configuración para la tabla dinámica.
 * @constant options
 */
const options = {
    valueNames,
    sortClass: 'sort'
};

configDynamicTable({
    targetElementId: 'job-positions',
    options
});

/**
 * Acción que se ejecuta cuando cambia el valor del elemento
 * cuyo ID es 'recordsPerPage'.
 */
document.querySelector('#recordsPerPage').addEventListener('change', () => {
    $formFilters.submit();
});

/**
 * Acción que se ejecuta cuando se hace clic sobre el botón cuyo
 * ID es 'btnFilters'.
 */
document.querySelector('#btnFilters').addEventListener('click', () => {
    const openedFiltersSection = document.querySelector('#openedFiltersSection');
    const opened = openedFiltersSection.valueAsBoolean;

    openedFiltersSection.value = !opened;
});

/**
 * Acción que se ejecuta cuando se hace clic sobre el botón cuyo
 * ID es 'btnClearFilters'.
 */
document.querySelector('#btnClearFilters').addEventListener('click', () => {
    $formFilters.querySelectorAll('[data-clean-value]').forEach($element => {
        const cleanValue = $element.getAttribute('data-clean-value');
        $element.value = cleanValue || null;
    });
    $formFilters.submit();
});