package jsonvalues;


/**
 * Represent a Lens which focus is a boolean located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsBoolLens<S extends Json<S>> extends Lens<S, Boolean> {
  JsBoolLens(final JsPath path) {
    super(json -> json.getBool(path),
      n -> json -> json.set(path, JsBool.of(n))
    );
  }
}
