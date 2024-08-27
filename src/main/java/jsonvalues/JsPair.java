package jsonvalues;

/**
 * Represents any element in a JSON which can be modeled with a path location the associated element. Any JSON can be
 * modeled as a {@link Json#stream() stream} of JsPair
 *
 * @param path  the location of the value
 * @param value the value itself
 */
public record JsPair(JsPath path,
                     JsValue value) {

}
