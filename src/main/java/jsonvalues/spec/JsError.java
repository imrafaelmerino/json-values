package jsonvalues.spec;

import jsonvalues.JsValue;

/**
 * Represents an error validating a value of a JSON
 * @param value the value
 * @param error the error code
 */
public record JsError(JsValue value, ERROR_CODE error) {
}
