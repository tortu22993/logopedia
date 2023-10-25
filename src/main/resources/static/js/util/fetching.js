/**
 * Contiene lógica relativa a fetching de datos.
 *
 * @author Enrique Escalante
 * @author Enrique Escalante
 */
import { CONTEXT_PATH } from "../global.js";
import { StageWorkflowConfiguration } from "../model/StageWorkflowConfiguration.js";

/**
 * El endpoint de búsqueda de clientes.
 */
const CUSTOMER_SEARCH_ENDPOINT = `${CONTEXT_PATH}/customers/search`;

/**
 * El endpoint de búsqueda de contactos.
 */
const CONTACT_SEARCH_ENDPOINT = `${CONTEXT_PATH}/contacts/search`;

/**
 * El endpoint de búsqueda de proyectos.
 */
const PROJECT_SEARCH_ENDPOINT = `${CONTEXT_PATH}/projects/search`;

/**
 * El endpoint de búsqueda de servicios.
 */
const SERVICE_SEARCH_ENDPOINT = `${CONTEXT_PATH}/services/search`;

/**
 * El endpoint de búsqueda de activos.
 */
const ASSET_SEARCH_ENDPOINT = `${CONTEXT_PATH}/assets/search`;

/**
 * El endpoint de búsqueda de productos.
 */
const PRODUCT_SEARCH_ENDPOINT = `${CONTEXT_PATH}/products/search`;

/**
 * El endpoint de búsqueda de usuarios.
 */
const USER_SEARCH_ENDPOINT = `${CONTEXT_PATH}/users/search`;

/**
 * El endpoint de búsqueda de colas de trabajo.
 */
const WORK_QUEUE_SEARCH_ENDPOINT = `${CONTEXT_PATH}/work-queues/search`;

/**
 * El endpoint de búsqueda de valores de flujos de trabajo de configuraciones de etapas.
 */
const STAGE_WORKFLOW_VALUE_SEARCH_ENDPOINT = `${CONTEXT_PATH}/stages-configurations`;

/*
 * -----------------------------------------------------------------------------------------
 * -----------------------------------------------------------------------------------------
 */

/**
 * Realiza una búsqueda de datos.
 * @function search
 *
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.url El endpoint al que se realiza el fetch
 * @param {Map<String, String | Number | Boolean>} [wrapper.urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Una promesa que contiene los datos obtenidos como resultado del fetch
 */
async function search({ endpoint, urlParams = null }) {
    let url = endpoint;
    let data;

    if(urlParams && urlParams.size > 0) {
        let index = 0;

        for(const [key, value] of urlParams) {
            const operator = (index === 0)
                ? '?'
                : '&';

            url += `${operator}${key}=${value}`;
            index++;
        }
    }

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        data = await response.json();
    } catch(error) {
        data = [];
    }

    return data;
}

/**
 * Carga los clientes en base a un texto introducido por el usuario.
 * @function searchCustomers
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los clientes
 */
export async function searchCustomers(text, urlParams) {
    return await search({
        endpoint: `${CUSTOMER_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los contactos en base a un texto introducido por el usuario.
 * @function searchContacts
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los contactos
 */
export async function searchContacts(text, urlParams) {
    return await search({
        endpoint: `${CONTACT_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los servicios en base a un texto introducido por el usuario.
 * @function searchServices
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los servicios
 */
export async function searchServices(text, urlParams) {
    return await search({
        endpoint: `${SERVICE_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los proyectos en base a un texto introducido por el usuario.
 * @function searchServices
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los proyectos
 */
export async function searchProjects(text, urlParams) {
    return await search({
        endpoint: `${PROJECT_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los activos en base a un texto introducido por el usuario.
 * @function searchAssets
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los activos
 */
export async function searchAssets(text, urlParams) {
    return await search({
        endpoint: `${ASSET_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los productos en base a un texto introducido por el usuario.
 * @function searchProducts
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los productos
 */
export async function searchProducts(text, urlParams) {
    return await search({
        endpoint: `${PRODUCT_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los usuarios en base a un texto introducido por el usuario.
 * @function searchProducts
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de los usuarios
 */
export async function searchUsers(text, urlParams) {
    return await search({
        endpoint: `${USER_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga las colas de trabajo en base a un texto introducido por el usuario.
 * @function searchWorkQueues
 *
 * @param {String} text El texto introducido por el usuario
 * @param {Map<String, String | Number | Boolean>} [urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<Object>>} Un objeto json contenedor de las colas de trabajo
 */
export async function searchWorkQueues(text, urlParams) {
    return await search({
        endpoint: `${WORK_QUEUE_SEARCH_ENDPOINT}/${text}`,
        urlParams
    });
}

/**
 * Carga los valores de los campos configurados en el flujo de trabajo
 * de una determinada etapa.
 * @function searchStageWorkflowValues
 *
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String | Number} wrapper.stageConfigurationId El ID de la configuración de la etapa
 * @param {String} wrapper.workflowId El ID del flujo de trabajo de la configuración de la etapa
 * @param {Map<String, String | Number | Boolean>} [wrapper.urlParams] Los parámetros a enviar en la URL
 * @returns {Promise<Array<StageWorkflowConfiguration>>} Un objeto json contenedor de los valores de los flujos de trabajo
 */
export async function searchStageWorkflowValues({ stageConfigurationId, workflowId, urlParams }) {
    return await search({
        endpoint: `${STAGE_WORKFLOW_VALUE_SEARCH_ENDPOINT}/${stageConfigurationId}/by-workflow/${workflowId}`,
        urlParams
    });
}