/**
 * Lógica de la vista 'customer-detail.html' para la pestaña
 * de 'Contactos', solo cuando muestra una lista paginada.
 * 
 * @author Enrique Escalante
 */
import { configDynamicTable } from "../../../js/util/dynamic-table.js";

/**
 * Los nombres de los campos de la tabla de contactos a gestionar.
 * @constant valueNames
 */
const valueNames = ['fullName', 'parentesque', 'email', 'mobilePhone', 'telephone'];

/**
 * Las opciones de configuración para la tabla dinámica de contactos.
 * @constant options
 */
const options = {
    valueNames,
    sortClass: 'sort'
};

configDynamicTable({
    targetElementId: 'contacts',
    options
});

/**
 * Acción que se ejecuta cuando cambia el valor del elemento
 * cuyo ID es 'recordsPerPage'.
 */
document.querySelector('#recordsPerPage')?.addEventListener('change', event => {
    const $target = event.currentTarget;
    const $form = $target.closest('form');

    $form.submit();
});