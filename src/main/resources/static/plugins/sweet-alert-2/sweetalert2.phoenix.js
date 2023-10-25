/**
 * Fichero de lógica relativo al plugin SweetAlert2 con funcionalidades
 * únicas para la plantilla de Bootstrap Phoenix.
 * 
 * @author Enrique Escalante
 */
(() => {

    /**
     * Actualiza los estilos de CSS del plugin SweetAlert2 en función
     * de la configuración del usuario sobre el tema (claro u oscuro)
     * de la aplicación.
     * @method updateThemeStyles
     */
    function updateThemeStyles() {
        const isDarkTheme = localStorage.getItem('phoenixTheme') === 'dark';
        const sweetAlert2Styles = document.querySelector('#sweetAlert2Styles');
        const href = sweetAlert2Styles.getAttribute('href');
        const hrefLight = href.replace('dark.css', 'default.css');
        const hrefDark = href.replace('default.css', 'dark.css');

        sweetAlert2Styles.setAttribute('href', isDarkTheme ? hrefDark : hrefLight);
    }

    /**
     * Acción que se ejecuta cuando se hace clic sobre el botón cuyo ID es
     * 'themeToggler'.
     */
    document.querySelector('#themeToggler')?.addEventListener('click', () => {
        setTimeout(() => updateThemeStyles(), 300);
    });

    updateThemeStyles();

})();