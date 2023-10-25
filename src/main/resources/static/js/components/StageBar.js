import { CONTEXT_PATH } from "../global.js";
import { StageWorkflowValue } from "../model/StageWorkflowValue.js";
import { StageWorkflowValueList } from "../model/StageWorkflowValueList.js";
import { MESSAGE_TYPES, showMessage } from "../util/confirm-message.js";
import { searchStageWorkflowValues } from "../util/fetching.js";
import { emptyElementContent, generateRandomId } from "../util/util.js";
import { ControlContainer } from "./ControlContainer.js";
import { SpinnerLoader } from "./SpinnerLoader.js";

/**
 * Permite inicializar y configurar una barra de estados.
 * @class
 * 
 * @author Enrique Escalante
 */
export class StageBar {

    /**
     * La barra de etapas (elemento HTML en el DOM).
     * @private
     * @type {HTMLElement}
     */
    #stageBar;

    /**
     * El contenedor de las validaciones de los campos clave configurados para una
     * determinada etapa.
     * @private
     * @type {HTMLElement}
     */
    #keyFieldsValidationsContainer;

    /**
     * El contenedor de los campos clave configurados para una determinada etapa.
     * Contendrá todos aquellos campos clave a renderizar con base a la configuración
     * de la etapa seleccionada en la barra.
     * @private
     * @type {HTMLElement}
     */
    #keyFieldsContainer;

    /**
     * El ID del flujo de trabajo configurado en la barra de etapas.
     * @private
     * @type {String | Number}
     */
    #workflowId;

    /**
     * La etapa activa en la barra.
     * @private
     * @type {HTMLElement}
     */
    #activeStage;

    /**
     * Las etapas que conforman la barra.
     * @private
     * @type {HTMLElement[]}
     */
    #stages;

    /**
     * Las etapas completadas en la barra.
     * @private
     * @type {HTMLElement[]}
     */
    #completedStages;

    /**
     * Constructor con parámetros.
     * @constructor
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.stageBarSelector El selector para la barra de etapas a establecer
     */
    constructor({ stageBarSelector }) {
        this.#stageBar = document.querySelector(stageBarSelector);
        this.#keyFieldsValidationsContainer = this.#stageBar.querySelector('.list-box-validations-container');
        this.#keyFieldsContainer = this.#stageBar.querySelector('.list-box-data-container');
        this.#workflowId = this.#stageBar.getAttribute('data-workflow');
        this.#activeStage = this.getActiveStage();
        this.#stages = this.getStages();
        this.#completedStages = this.getCompletedStagesFrom(this.#stages);
    }

    /**
     * Los tipos de estados que pueden ser asignados a las etapas.
     * @static
     * @constant
     */
    static #statusTypes = Object.freeze({
        ACTIVE: 'active',
        SELECTED: 'selected',
        COMPLETED: 'completed'
    });

    /**
     * Hace que una barra de etapas con un selector determinado sea interactiva, es decir,
     * que el usuario pueda hacer clic, cambiar la etapa, cargar componentes o datos
     * relacionados, etc.
     * @method
     */
    makeInteractive() {
        if(this.#isClosed()) {
            return;
        }

        const stages = this.getStages();
        const workflowId = this.#workflowId;

        stages?.forEach(stage => {
            stage.addEventListener('click', async (event) => {
                event.preventDefault();

                await this.renderStageConfiguration({
                    workflowId,
                    stage
                });

                this.#changeStatus({
                    currentStage: stage
                });
            });
        });
    }

    /**
     * Obtiene la lista de etapas que están contenidas en la barra.
     * @function
     * 
     * @returns {HtmlElement[]} Un array contenedor de las etapas de la barra
     */
    getStages() {
        return this.#stageBar.querySelectorAll('.list-box-item');
    }

    /**
     * Obtiene la lista de etapas completadas que están contenidas en la barra.
     * @function
     * 
     * @returns {HtmlElement[]} Un array contenedor de las etapas completadas de la barra
     */
    getCompletedStages() {
        return this.getCompletedStagesFrom(getStages());
    }

    /**
     * Obtiene la lista de etapas completadas en una lista determinada de etapas.
     * @function
     * 
     * @param {HtmlElement[]} stages La lista contenedora de las etapas
     * @returns {HtmlElement[]} Un array contenedor de las etapas completadas en la lista
     */
    getCompletedStagesFrom(stages) {
        const stagesToUse = (stages instanceof NodeList)
            ? Array.from(stages)
            : stages;

        return stagesToUse?.filter(stage => stage.classList.contains('is-completed'));
    }

    /**
     * Cambia la etapa actual en una barra de etapas.
     * @private
     * @method
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {HTMLElement} wrapper.currentStage La etapa actual (seleccionado) en la barra de etapas
     */
    #changeStatus({ currentStage }) {
        if(this.isActive(currentStage)) {
            return;
        }

        this.#reset();

        if(currentStage !== this.#activeStage) {
            this.#setStatus({
                stage: this.#activeStage,
                statusType: StageBar.#statusTypes.SELECTED,
                statusValue: true
            });

            this.#setStatus({
                stage: currentStage,
                statusType: StageBar.#statusTypes.ACTIVE,
                statusValue: true
            });

            this.#setStatus({
                stage: currentStage,
                statusType: StageBar.#statusTypes.COMPLETED,
                statusValue: false
            });

            return;
        }

        if(!this.isSelected(currentStage)) {
            this.#setStatus({
                stage: this.#activeStage,
                statusType: StageBar.#statusTypes.SELECTED,
                statusValue: true
            });

            this.#setStatus({
                stage: this.#activeStage,
                statusType: StageBar.#statusTypes.ACTIVE,
                statusValue: false
            });

            return;
        }

        this.#setStatus({
            stage: this.#activeStage,
            statusType: StageBar.#statusTypes.SELECTED,
            statusValue: false
        });

        this.#setStatus({
            stage: this.#activeStage,
            statusType: StageBar.#statusTypes.ACTIVE,
            statusValue: true
        });
    }

    /**
     * Comprueba si una etapa está activa o no.
     * @function
     * 
     * @param {HTMLElement} stage La etapa a comprobar 
     * @returns {Boolean} Verdadero si la etapa está activa o falso en caso contrario
     */
    isActive(stage) {
        return stage?.classList.contains('is-active') ?? false;
    }

    /**
     * Comprueba si una etapa está seleccionada o no.
     * @function
     * 
     * @param {HTMLElement} stage La etapa a comprobar 
     * @returns {Boolean} Verdadero si la etapa está seleccionada o falso en caso contrario
     */
    isSelected(stage) {
        return stage?.classList.contains('is-selected') ?? false;
    }

    /**
     * Comprueba si la barra de etapas está cerrada o no.
     * @private
     * @function
     * 
     * @returns {Boolean} Verdadero si la barra de etapas está cerrada o falso en caso contrario
     */
    #isClosed() {
        return this.#stageBar.hasAttribute('data-closed');
    }

    /**
     * Restablece la barra de etapas según su configuración inicial.
     * @private
     * @method
     */
    #reset() {
        this.#emptyValidationsErrors();

        this.#setStatus({
            stage: this.#activeStage,
            statusType: StageBar.#statusTypes.ACTIVE,
            statusValue: true
        });

        this.#completedStages?.forEach(completedStage => this.#setStatus({
            stage: completedStage,
            statusType: StageBar.#statusTypes.COMPLETED,
            statusValue: true
        }));

        this.#stages?.forEach(stage => this.#setStatus({
            stage,
            statusType: StageBar.#statusTypes.ACTIVE,
            statusValue: false
        }));
    }

    /**
     * Comprueba si una etapa tiene configuración o no.
     * @private
     * @function
     * 
     * @param {HTMLElement} stage La etapa a comprobar
     * @returns {Boolean} Verdadero si la etapa tiene configuración o falso en caso contrario
     */
    #hasStageConfiguration(stage) {
        return this.#getStageConfiguration(stage) ?? false;
    }

    /**
     * Obtiene la configuración de una etapa, si tiene.
     * @private
     * @function
     * 
     * @param {HTMLElement} stage La etapa a comprobar
     * @returns {String} El valor de la configuración de la etapa
     */
    #getStageConfiguration(stage) {
        return stage?.getAttribute('data-stage-configuration')?.trim();
    }

    /**
     * Obtiene la etapa activa en la barra.
     * @function
     * 
     * @returns {HTMLElement} La etapa activa
     */
    getActiveStage() {
        return this.#stageBar?.querySelector('.list-box-item.is-active');
    }

    /**
     * Establece un estado para una etapa determinada.
     * @private
     * @method
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {HTMLElement} wrapper.stage La etapa cuyo estado cambiará
     * @param {String} wrapper.statusType = El tipo de estado a establecer
     * @param {Boolean} wrapper.statusValue El valor del estado a establecer
     */
    #setStatus({ stage, statusType, statusValue }) {
        stage?.classList.toggle(`is-${statusType}`, statusValue);
    }

    /**
     * Renderiza la configuración de una etapa determinada para un flujo de trabajo específico.
     * @async
     * @method
     *  
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {HTMLElement} wrapper.stage La etapa que formará el filtro 
     * @param {Boolean} [wrapper.forceRendering] Indica si se debe forzar la renderización o no
     * @returns {Promise<void>} Una promesa contenedora de la acción ejecutada
     */
    async renderStageConfiguration({ stage, forceRendering = false }) {
        const isActive = this.isActive(stage);
        const hasConfiguration = this.#hasStageConfiguration(stage);
        const renderControls = async () => {
            const loader = SpinnerLoader.newInstance({
                wrapper: true,
                wrapperClasses: ['col-xs-12', 'd-flex', 'justify-content-center', 'align-items-center'],
                small: true,
                parent: this.#keyFieldsContainer
            });
            const workflowId = this.#workflowId;
            const stageConfigurationId = this.#getStageConfiguration(stage);
            const workflowValues = await searchStageWorkflowValues({
                workflowId,
                stageConfigurationId
            });

            workflowValues?.forEach(workflowValue => {
                const options = workflowValue.keyFieldHtmlInputMultiple
                    ? {
                        items: workflowValue.keyFieldHtmlInputPossibleValues,
                        currentValue: workflowValue.keyFieldValue
                    }
                    : {};

                ControlContainer.appendNewInstanceTo({
                    inputType: workflowValue.keyFieldHtmlInputType,
                    label: workflowValue.keyFieldName,
                    inputId: `workflowValue_${generateRandomId()}`,
                    keyFieldIdentifier: workflowValue.keyFieldId,
                    mandatory: workflowValue.mandatory,
                    value: workflowValue.keyFieldValue,
                    options,
                    wrapperElement: this.#keyFieldsContainer
                });
            });

            loader.remove();
        };

        if(!forceRendering && !isActive) {
            emptyElementContent(this.#keyFieldsContainer);
        }

        if((forceRendering && hasConfiguration) || (!isActive && hasConfiguration)) {
            return await renderControls();
        }

        return Promise.resolve();
    }

    /**
     * Obtiene el ID de una etapa.
     * @private
     * @function
     * 
     * @param {HTMLElement} stage La etapa a partir de la cual se obtendrá su ID
     * @returns {String | Number} El ID de la etapa
     */
    #getStageId(stage) {
        return stage?.getAttribute('data-stage');
    }

    /**
     * Obtiene la información o datos de los campos configurados para la etapa,
     * incluyendo aquellos datos modificados por el usuario.
     * @private
     * @function
     * 
     * @returns {StageWorkflowValueList} Una estructura contenedora de los datos
     */
    #getKeyFieldsData() {
        const fields = this.#keyFieldsContainer.querySelectorAll('[data-key-field]');
        const stageId = this.#getStageId(this.getActiveStage());

        if(!fields?.length) {
            return new StageWorkflowValueList({
                stageId,
                workflowId: this.#workflowId
            });
        }

        const fieldsData = Array.from(fields)?.map(field => {
            return new StageWorkflowValue({
                stageConfigurationId: this.#getStageConfiguration(this.getActiveStage()),
                keyFieldId: field.getAttribute('data-key-field'),
                value: field.value
            });
        });

        return new StageWorkflowValueList({
            stageId,
            workflowId: this.#workflowId,
            stageWorkflowValues: fieldsData
        });
    }

    /**
     * Añade una lista de errores al contenedor de mensajes de validación.
     * @private
     * @method
     * 
     * @param {Array<String>} errors La lista de errores a añadir
     */
    #addValidationsErrors(errors) {
        errors?.forEach(error => {
            const errorContainer = document.createElement('p');
            const errorContent = document.createTextNode(error);

            errorContainer.classList.add('mb-0');
            errorContainer.appendChild(errorContent);

            this.#keyFieldsValidationsContainer.appendChild(errorContainer);
        });
    }

    /**
     * Vacía o limpia los errores de validación.
     * @private
     * @method
     */
    #emptyValidationsErrors() {
        this.#keyFieldsValidationsContainer.innerHTML = '';
    }

    /**
     * Cambia el estado de un flujo de trabajo.
     * @async
     * @function
     * 
     * @param {Object} [wrapper] El objeto contenedor de los parámetros 
     * @param {Function} [wrapper.onComplete] La función a ejecutar tras cambiar el estado
     * @returns {Promise<void>} El resultado de la promesa
     */
    async changeStage({ onComplete = () => location.reload() } = {}) {
        const url = `${CONTEXT_PATH}/stages-workflows/change-stage`;
        const csrfHeader = document.querySelector('meta[name = "csrf-header"]')?.getAttribute('content');
        const csrfToken = document.querySelector('meta[name = "csrf-token"]')?.getAttribute('content');
    
        try {
            const values = this.#getKeyFieldsData();

            const headerParameters = { 'content-type': 'application/json' };
            headerParameters[csrfHeader] = csrfToken;

            this.#emptyValidationsErrors();

            const response = await fetch(url, {
                method: 'POST',
                headers: new Headers(headerParameters),
                body: JSON.stringify(values)
            });

            const defaultAction = () => onComplete();
            const actions = {
                200: () => defaultAction(),
                400: async () => {
                    const data = await response.json();
                    const { validationResults } = data;
                    
                    this.#addValidationsErrors(validationResults);

                    return Promise.resolve();
                },
                403: () => {
                    showMessage({
                        title: 'Acción no permitida',
                        message: `No tiene acceso a este recurso.`,
                        type: MESSAGE_TYPES.ERROR,
                        showMessage: true,
                        confirmButtonText: 'Entendido',
                        showCancelButton: false
                    });

                    return Promise.resolve();
                }
            };
            const actionToExecute = actions[response.status];

            actionToExecute ? actionToExecute() : defaultAction();
        } catch(error) {
            showMessage({
                title: 'Error',
                message: `Lo sentimos, ha ocurrido un problema al cambiar el estado. Contacte con soporte.`,
                type: MESSAGE_TYPES.ERROR,
                showMessage: true,
                confirmButtonText: 'Entendido',
                showCancelButton: false
            });

            return Promise.resolve();
        }
    }

    /**
     * Inicializa la barra de etapas.
     * @method
     */
    init() {
        this.makeInteractive();
        this.renderStageConfiguration({
            stage: this.getActiveStage(),
            forceRendering: true
        });
    }

}