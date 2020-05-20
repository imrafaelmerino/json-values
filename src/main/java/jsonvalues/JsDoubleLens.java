package jsonvalues;


/**
 Represent a Lens which focus is a double number located at a path in a Json

 @param <S> the type of the whole part, an array or an object */
public class JsDoubleLens<S extends Json<S>> extends Lens<S, Double> {
    JsDoubleLens(final JsPath path) {
        super(json -> json.getDouble(path),
              n -> json -> json.set(path,
                                    JsDouble.of(n))
             );
    }
}
