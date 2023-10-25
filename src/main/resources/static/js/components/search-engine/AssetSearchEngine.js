/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de activos.
 *
 * @author Enrique Escalante
 */
import { searchAssets } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de activos.
 *
 * @author Enrique Escalante
 */
export class AssetSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de activos.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.assetInputId El ID del elemento input HTML del campo del activo
     * @param {String} wrapper.assetInputHiddenId El elemento input hidden HTML del ID del activo
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ assetInputId, assetInputHiddenId, urlParams = null }) {
        const $assetInputHidden = document.getElementById(assetInputHiddenId);
        const { input: $assetInput } = createAutocomplete({
            selector: `#${assetInputId}`,
            dataSource: searchAssets,
            keys: ['name'],
            displayKey: 'name',
            showId: true
        });

        $assetInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: assetId, name: assetName } = feedback.selection.value;

            $assetInputHidden.value = assetId;
            $target.value = assetName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $assetInputHidden.value = '';
            }
        };

        $assetInput.addEventListener('blur', event => onClearValue(event));
        $assetInput.addEventListener('search', event => onClearValue(event));
    }

}