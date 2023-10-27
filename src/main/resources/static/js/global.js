/**
 * Contiene constantes y variables de entorno.
 * 
 * @author Enrique Escalante
 */

/**
 * La ruta raíz del contexto de la aplicación.
 * @constant CONTEXT_PATH
 */
export const CONTEXT_PATH = document.querySelector('meta[name = "context-path"]')?.getAttribute('content') ?? '';