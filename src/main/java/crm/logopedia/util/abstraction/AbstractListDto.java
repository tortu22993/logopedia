package crm.logopedia.util.abstraction;

/**
 * Clase genérica para los DTO destinados a mostrar información
 * o datos de listados.
 * 
 * @author Enrique Escalante
 */
public abstract class AbstractListDto {
    
    /**
     * Convierte los valores de las cadenas de texto
     * vacías en nulas.
     * 
     * @return La instancia con sus propiedades con valor vacío convertidas en nulos
     */
    protected AbstractListDto convertBlankToNull() {
        final var fields = getClass().getDeclaredFields();

        for(final var field : fields) {
            field.setAccessible(true);

            if(field.getType().equals(String.class)) {
                try {
                    final var fieldValue = (String) field.get(this);

                    if(fieldValue != null && fieldValue.isBlank()) {
                        field.set(this, null);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return this;
    }

}
