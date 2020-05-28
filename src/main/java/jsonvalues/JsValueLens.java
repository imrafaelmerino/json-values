package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 Represent a Lens which focus is the value of a Json

 @param <S> the type of the whole part, an array or an object */
class JsValueLens<S extends Json<S>> extends Lens<S, JsValue> {



    JsValueLens(final JsPath path) {
        super(json -> requireNonNull(json).get(path),
              value -> json -> requireNonNull(json).set(path,
                                                        requireNonNull(value)
                                                       )
             );



    }

    static JsValueLens<JsObj> of(final String key) {
        return new JsValueLens<>(JsPath.fromKey(requireNonNull(key)));
    }

    static JsValueLens<JsArray> of(int index) {
        return new JsValueLens<>(JsPath.fromIndex(index));
    }

    static <R extends Json<R>> JsValueLens<R> of(JsPath path) {
        return new JsValueLens<>(path);
    }




}
