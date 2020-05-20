package jsonvalues;

/**
 * Represent a Lens which focus is an integer number located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
public class JsIntLens<S extends Json<S>> extends Lens<S, Integer> {
  JsIntLens(final JsPath path) {
    super(json -> json.getInt(path),
      n -> json -> json.set(path, JsInt.of(n))
    );
  }
}
