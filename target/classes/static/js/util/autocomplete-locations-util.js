/**
 * Contiene funcionalidades útiles que extienden y centralizan
 * las características del plugin autoCompleteDirections.js.
 * 
 * @author Enrique Escalante
 */

/**
 * Crea un nuevo buscador a partir del plugin autoCompleteDirections.js.
 * @function createAutocomplete
 * 
 * @param {Object} wrapper El objeto contenedor de los parámetros
 * @param {String} wrapper.inputId El elemento input HTML del campo que actuará como buscador de direcciones
 * @param {String} wrapper.inputIdCountry El elemento input HTML del País
 * @param {String} wrapper.inputIdProvince El elemento input HTML de la provincia
 * @param {String} wrapper.inputIdMunicipality El elemento input HTML del municipio
 * @param {String} wrapper.inputIdZipCode El elemento input HTML del código postal
 * @param {String} wrapper.inputIdAddress El elemento input HTML de la dirección
 * @returns {Object} Una nueva instancia contenedora del buscador autoCompleteLocations.js
 */
export function createAutocomplete({inputId, inputIdCountry, inputIdProvince, inputIdMunicipality, inputIdZipCode,inputIdAddress }) {
    const apiUrl = 'https://nominatim.openstreetmap.org/search?format=geojson&limit=5&addressdetails=1&countrycodes=es&q=';
    const inputCountry = document.getElementById(inputIdCountry);
    const inputProvince = document.getElementById(inputIdProvince);
    const inputMunicipality = document.getElementById(inputIdMunicipality);
    const inputZipCode = document.getElementById(inputIdZipCode);
    const inputAddress = document.getElementById(inputIdAddress);
    const onChangeEvent = new Event('change');
    
    return new autoCompleteLocations(inputId, {
        selectFirst: true,
        insertToInput: true,
        cache: true,
        howManyCharacters: 2,
        ariaLabelClear : 'Limpiar campos de dirección',
        onSearch: async ({ currentValue }) => {
            const encodedValue = encodeURI(currentValue);
            const apiEndpoint = apiUrl + encodedValue;
            let results;

            try {
                const response = await fetch(apiEndpoint);
                const data = await response.json();
                
                results = data.features;
            } catch(error) {
                results = [];
            }

            return results;
        },
        onResults: ({ currentValue, matches, template }) => {
            const regex = new RegExp(currentValue, 'gi');

            return matches === 0
                ? template
                : matches.map(element => {
                    const { properties } = element;
                    const { display_name: displayName } = properties;
                    const value = displayName.replace(regex, s => `<strong>${s}</strong>`)

                    return `
                        <li>
                            <p>${value}</p>
                        </li> 
                    `;
                }).join('');
        },
        onSubmit: ({ object }) => {
            const { properties } = object;
            const { address: addressProperties } = properties;

            inputCountry.value = addressProperties.country ?? '';
            inputCountry.dispatchEvent(onChangeEvent);

            inputProvince.value = addressProperties.state_district ?? addressProperties.state;
            inputProvince.dispatchEvent(onChangeEvent);

            inputMunicipality.value = addressProperties.town ?? addressProperties.city
                ?? addressProperties.village
                ?? addressProperties.municipality
                ?? '';
            inputMunicipality.dispatchEvent(onChangeEvent);

            inputZipCode.value = addressProperties.postcode ?? '';
            inputZipCode.dispatchEvent(onChangeEvent);

            inputAddress.value = (addressProperties.road && addressProperties.house_number)
                ? `${addressProperties.road}, ${addressProperties.house_number}`
                : addressProperties.road ?? '';
            inputAddress.dispatchEvent(onChangeEvent);
        },
        noResults: ({ currentValue, template }) => template(`
            <li class="no-results">No se han encontrado resultados para: '${currentValue}'</li>
        `),
        onReset: () => {
            inputCountry.value = '';
            inputCountry.dispatchEvent(onChangeEvent);

            inputProvince.value = '';
            inputProvince.dispatchEvent(onChangeEvent);

            inputMunicipality.value = '';
            inputMunicipality.dispatchEvent(onChangeEvent);

            inputZipCode.value = '';
            inputZipCode.dispatchEvent(onChangeEvent);

            inputAddress.value = '';
            inputAddress.dispatchEvent(onChangeEvent);
        }
    });
}