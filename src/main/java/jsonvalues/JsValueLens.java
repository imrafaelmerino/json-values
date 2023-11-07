package jsonvalues;


import fun.optic.Lens;

import static java.util.Objects.requireNonNull;

/**
 * Represent a Lens which focus is the value of a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsValueLens<S extends Json<S>> extends Lens<S, JsValue> {

    JsValueLens(JsPath path) {
        super(json -> requireNonNull(json).get(path),
              value -> json -> requireNonNull(json).set(path,
                                                        requireNonNull(value)
                                                       )
             );


    }


}
