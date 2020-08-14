package jsonvalues;


import static java.util.Objects.requireNonNull;

/**
 Represent a Lens which focus is a double number located at a path in a Json

 @param <S> the type of the whole part, an array or an object */
class JsDoubleLens<S extends Json<S>> extends Lens<S, Double> {
    JsDoubleLens(final JsPath path) {
        super(json -> requireNonNull(json).getDouble(path),
              n -> json -> requireNonNull(json).set(path,
                                                    JsDouble.of(n)
                                                   )
             );
    }
}
