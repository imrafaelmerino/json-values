package jsonvalues;


/**
 * Represents any element in a JSON object which can be modeled with a key and the associated element. Any JSON object
 * can be modeled as a {@link JsObj#streamOfKeys() stream}  of JsObjPair
 *
 * @param key   the key
 * @param value the value associated to the key
 */
public record JsObjPair(String key, JsValue value) {
}
