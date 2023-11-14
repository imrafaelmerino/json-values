package jsonvalues.spec;

import jsonvalues.JsValue;

/**
 * Represents a code validating a value of a JSON
 *
 * @param value the value
 * @param code  the code
 */
public record JsError(JsValue value, ERROR_CODE code) {

    /**
     * Constructor to validate that fields are not null
     * @param value the value that is not valid
     * @param code the error code
     */
    public JsError {
        if (value == null) throw new IllegalArgumentException("value is null");
        if (code == null) throw new IllegalArgumentException("code is null");
    }
}
