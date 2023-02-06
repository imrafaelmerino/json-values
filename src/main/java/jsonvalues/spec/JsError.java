package jsonvalues.spec;

import jsonvalues.JsValue;

public record JsError(JsValue value, ERROR_CODE error) {
}
