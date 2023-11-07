package jsonvalues.spec;

import jsonvalues.JsValue;

/**
 * Represents an code validating a value of a JSON
 *
 * @param value the value
 * @param code  the code
 */
public record JsError(JsValue value, ERROR_CODE code) {
}
