/**
 * Contiene funcionalidades relativas a la carga dinámica
 * y en tiempo real de productos.
 *
 * @author Enrique Escalante
 */
import { searchProducts } from "../../util/fetching.js";
import { createAutocomplete } from "../../util/autocomplete-util.js";

/**
 * Contiene métodos para la configuración e inicialización
 * de campos que actúan como buscadores de productos.
 *
 * @author Enrique Escalante
 */
export class ProductSearchEngine {

    /**
     * Configura e inicializa los campos que actúan como buscadores de productos.
     * @method init
     *
     * @param {Object} wrapper El objeto contenedor de los parámetros
     * @param {String} wrapper.productInputId El ID del elemento input HTML del campo del producto
     * @param {String} wrapper.productInputHiddenId El elemento input hidden HTML del ID del producto
     * @param {Map<String, String | Number | Boolean} [wrapper.urlParams] Los parámetros a enviar en la URL
     */
    static init({ productInputId, productInputHiddenId, urlParams = null }) {
        const $productInputHidden = document.getElementById(productInputHiddenId);
        const { input: $productInput } = createAutocomplete({
            selector: `#${productInputId}`,
            dataSource: searchProducts,
            keys: ['name'],
            displayKey: 'name',
            showId: true
        });

        $productInput.addEventListener('selection', event => {
            const { currentTarget: $target, detail: feedback } = event;
            const { id: productId, name: productName } = feedback.selection.value;

            $productInputHidden.value = productId;
            $target.value = productName;
        });

        const onClearValue = event => {
            const $target = event.currentTarget;
            const value = $target.value;

            if(!value?.trim()) {
                $productInputHidden.value = '';
            }
        };

        $productInput.addEventListener('blur', event => onClearValue(event));
        $productInput.addEventListener('search', event => onClearValue(event));
    }

}