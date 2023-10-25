import { createAutocomplete } from "../../util/autocomplete-locations-util.js";

/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de localizaciones
 * 
 * @author Enrique Escalante
 */
export class ApiAutocompleteLocations {
    
    /**
     * Configura e inicializa los campos que actúan como buscadores de direcciones.
     * Establece el campo que le pasemos por parámetro como un buscador en tiempo real
     * @method init
     * 
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.inputId El elemento input HTML del campo que actuará como buscador de direcciones
     * @param {String} wrapper.inputIdCountry El elemento input HTML del País
     * @param {String} wrapper.inputIdProvince El elemento input HTML de la provincia
     * @param {String} wrapper.inputIdMunicipality El elemento input HTML del municipio
     * @param {String} wrapper.inputIdZipCode El elemento input HTML del código postal
     * @param {String} wrapper.inputIdAddress El elemento input HTML de la dirección 
     */
    static init({ inputId, inputIdCountry, inputIdProvince, inputIdMunicipality, inputIdZipCode, inputIdAddress }) {
        createAutocomplete({
            inputId,
            inputIdCountry,
            inputIdProvince,
            inputIdMunicipality,
            inputIdZipCode,
            inputIdAddress
        });
    }
    
}