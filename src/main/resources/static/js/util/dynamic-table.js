/**
 * Contiene funcionalidades relativas a la configuración
 * de tablas dinámicas a través del plugin List.js.
 * 
 * @author Enrique Escalante
 */
import { List } from '../../plugins/list.js/list.min.js';

/**
 * Establece los parámetros de configuración de una tabla dinámica.
 * @function configDynamicTable
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.targetElementId El ID del elemento HTML contenedor de la tabla
 * @param {Object} wrapper.options El objeto de configuración
 * @param {Array<Object>} [wrapper.values] Los valores a mostrar en la tabla
 * @returns {List} Una instancia de la lista generada con la configuración establecida
 */
export function configDynamicTable({ targetElementId, options, values = [] }) {
    options ??= { sortClass: 'sort' };
    const hasData = document.querySelectorAll(`#${targetElementId} tbody tr`).length;

    if(!hasData) {
        return false;
    }

    if(values) {
        return new List(targetElementId, options);
    }

    return new List(targetElementId, options, values);
}