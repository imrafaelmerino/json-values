package jsonvalues;

import static java.util.Objects.requireNonNull;

/**
 * Represent a Lens which focus is an integer number located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsIntLens<S extends Json<S>> extends Lens<S, Integer> {
  JsIntLens(final JsPath path) {
    super(json -> requireNonNull(json).getInt(path),
      n -> json -> requireNonNull(json).set(path, JsInt.of(n))
    );
  }
}
