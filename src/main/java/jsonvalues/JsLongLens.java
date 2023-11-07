package jsonvalues;

import fun.optic.Lens;

import static java.util.Objects.requireNonNull;

/**
 * Represent a Lens which focus is a long number located at a path is a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsLongLens<S extends Json<S>> extends Lens<S, Long> {
    JsLongLens(final JsPath path) {
        super(json -> requireNonNull(json).getLong(path),
              n -> json -> requireNonNull(json).set(path,
                                                    JsLong.of(n))
             );
    }
}
