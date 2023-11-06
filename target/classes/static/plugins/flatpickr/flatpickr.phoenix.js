/**
 * Contiene funcionalidades extra del plugin Flatpickr.
 * 
 * @author Enrique Escalante.
 */
import { wrapElement } from '../../js/util/util.js';
import { Spanish } from './locale/es.js';

/**
 * La configuración para campos de tipo fecha.
 * @constant
 */
const dateConfig = {
    enableTime: false,
    altInput: true,
    altFormat: 'j F, Y',
    dateFormat: 'Y-m-d',
    locale: Spanish,
    disableMobile: true,
    prevArrow: '<i class="fa fa-chevron-left"></i>',
	nextArrow: '<i class="fa fa-chevron-right"></i>',
    onDayCreate: (_dateObject, _dateString, _fp, dayElement) => onDayCreate({ dayElement }),
    onReady: (_selectedDates, _strDate, instance) => {
        onReady({
            instance,
            iconName: 'calendar',
            deleteClass: true
        });
    },
    onOpen: (_selectedDates, _strDate, instance) => onOpen({ instance })
};

/**
 * La configuración para campos de tipo tiempo.
 * @constant
 */
const timeConfig = {
    enableTime: true,
    noCalendar: true,
    dateFormat: 'H:i',
    locale: Spanish,
    disableMobile: true,
    prevArrow: '<i class="fa fa-chevron-left"></i>',
    nextArrow: '<i class="fa fa-chevron-right"></i>',
    onReady: (_selectedDates, _strDate, instance) => {
        onReady({ instance, iconName: 'clock' });
    },
    onOpen: (_selectedDates, _strDate, instance) => onOpen({ instance })
};

/**
 * La configuración para campos de tipo fecha con tiempo local.
 * @constant
 */
const dateTimeLocalConfig = {
    enableTime: true,
    altInput: true,
    altFormat: 'j F, Y H:i',
    dateFormat: 'Y-m-d H:i',
    locale: Spanish,
    disableMobile: true,
    prevArrow: '<i class="fa fa-chevron-left"></i>',
    nextArrow: '<i class="fa fa-chevron-right"></i>',
    onDayCreate: (_dateObject, _dateString, _fp, dayElement) => onDayCreate({ dayElement }),
    onReady: (_selectedDates, _strDate, instance) => onReady({ instance, iconName: 'calendar-check' }),
    onOpen: (_selectedDates, _strDate, instance) => onOpen({ instance })
};

/**
 * Establece los campos de fecha con la configuración y estilos
 * del plugin Flatpickr.
 */
flatpickr('input[type = "date"]', dateConfig);

/**
 * Establece los campos de tiempo con la configuración y estilos
 * del plugin Flatpickr.
 */
flatpickr('input[type = "time"]', timeConfig);

/**
 * Establece los campos de fecha con tiempo con la configuración y estilos
 * del plugin Flatpickr.
 */
flatpickr('input[type = "datetime-local"]', dateTimeLocalConfig);

/**
 * Inicializa un campo de tipo fecha con Flatpickr.
 * @method
 * 
 * @param {String} selector El selector del campo a inicializar
 */
export function initializeFlatpickrDate(selector) {
    flatpickr(selector, dateConfig);
}

/**
 * Inicializa un campo de tipo tiempo con Flatpickr.
 * @method
 * 
 * @param {String} selector El selector del campo a inicializar
 */
export function initializeFlatpickrTime(selector) {
    flatpickr(selector, timeConfig);
}

/**
 * Inicializa un campo de tipo fecha con tiempo local con Flatpickr.
 * @method
 * 
 * @param {String} selector El selector del campo a inicializar
 */
export function initializeFlatpickrDateTimeLocal(selector) {
    flatpickr(selector, dateTimeLocalConfig);
}

/**
 * Establece los parámetros y lógica a realizar al inicializar
 * un campo a través del plugin Flatpickr.
 * @method onReady
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {Object} wrapper.instance La instancia de Flatpickr
 * @param {String} wrapper.iconName El nombre del icono a mostrar 
 * @param {Boolean} [wrapper.deleteClass] Indica si se eliminan las clases del elemento
 */
function onReady({ instance, iconName, deleteClass = false }) {
    const { element: $element } = instance;
    const $parentNode = $element.parentNode;
    const $icon = document.createElement('span');

    if(deleteClass) {
        $element.removeAttribute('class');
    }

    $icon.classList.add('far', `fa-${iconName}`, 'flatpickr-icon', 'text-700');
    $parentNode.appendChild($icon);

    wrapElement({
        $element: $parentNode,
        classes: ['flatpickr-input-container']
    });
}

/**
 * Comprueba una serie de datos y realiza operaciones al abrir
 * el componente de Flatpickr.
 * @method onOpen
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {Object} wrapper.instance La instancia de Flatpickr
 */
function onOpen({ instance }) {
    const { element: $element } = instance;
    const disabled = $element.hasAttribute('data-disabled');

    if(disabled) instance.close();
}

/**
 * Realiza una serie de acciones por cada día a mostrar en el
 * calendario de Flatpickr.
 * @method onDayCreate
 *
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {Object} wrapper.dayElement El elemento del día a procesar
 */
function onDayCreate({ dayElement }) {
    const { dateObj: dateObject } = dayElement;
    const day = dateObject.getDay();

    if (day === 6 || day === 0) {
        dayElement.classList.add('weekend-days');
    }
}