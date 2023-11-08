/**
 * Lógica de la vista 'customer-detail.html' para la pestaña
 * de 'Contactos', solo para cuando se va a crear un nuevo contacto.
 *
 * @author Enrique Escalante
 */
import { ApiAutocompleteLocations } from "../../../js/components/autocomplete-locations/ApiAutocompleteLocations.js";
import { ContactSearchEngine } from "../../../js/components/search-engine/ContactSearchEngine.js";
import { changeControlsAppearanceOnEdit } from "../../../js/theme/mandatory.js";



ApiAutocompleteLocations.init({
    inputId : 'search',
    inputIdCountry : 'contactCountry',
    inputIdProvince : 'contactProvince',
    inputIdMunicipality : 'contactMunicipality',
    inputIdZipCode : 'contactZipCode',
    inputIdAddress : 'contactAddress'
});

window.addEventListener('load', () => changeControlsAppearanceOnEdit());
