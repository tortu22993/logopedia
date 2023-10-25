package crm.logopedia.util.specification.enums;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contiene los diferentes tipos de campos que una
 * entidad puede contener.
 * 
 * @author Enrique Escalante
 */
public enum FieldType {
    
    /**
     * El tipo de dato booleano.
     */
    BOOLEAN {
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    },

    /**
     * El tipo de dato carácter.
     */
    CHAR {
        public Object parse(String value) {
            return value.charAt(0);
        }
    },

    /**
     * El tipo de dato fecha.
     */
    DATE {
        public Object parse(String value) {
            Object date;

            try {
                final var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                date = LocalDateTime.parse(value, formatter);
            } catch(Exception e) {
                date = null;
            }

            return date;
        }
    },

    /**
     * El tipo de dato coma flotante simple.
     */
    FLOAT {
        public Object parse(String value) {
            return Float.valueOf(value);
        }
    },

    /**
     * El tipo de dato coma flotante doble.
     */
    DOUBLE {
        public Object parse(String value) {
            return Double.valueOf(value);
        }
    },

    /**
     * El tipo de dato número entero.
     */
    INTEGER {
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },

    /**
     * El tipo de dato número entero muy pequeño.
     */
    BYTE {
        public Object parse(String value) {
            return Byte.valueOf(value);
        }
    },

    /**
     * El tipo de dato número entero pequeño.
     */
    SHORT {
        public Object parse(String value) {
            return Short.valueOf(value);
        }
    },

    /**
     * El tipo de dato número entero grande.
     */
    LONG {
        public Object parse(String value) {
            return Long.valueOf(value);
        }
    },

    /**
     * El tipo de dato número decimal preciso.
     */
    BIG_DECIMAL {
        public Object parse(String value) {
            BigDecimal bigDecimalValue;

            try {
                final var doubleValue = Double.parseDouble(value);
                bigDecimalValue = BigDecimal.valueOf(doubleValue);
            } catch(Exception e) {
                bigDecimalValue = BigDecimal.ZERO;
            }

            return bigDecimalValue;
        }
    },

    /**
     * El tipo de dato cadena de caracteres.
     */
    STRING {
        public Object parse(String value) {
            return value;
        }
    };

    /**
     * Convierte un valor contenido en una cadena de
     * caracteres en un objeto genérico.
     * 
     * @param value El valor a convertir
     * @return El valor convertido
     */
    public abstract Object parse(String value);

}
