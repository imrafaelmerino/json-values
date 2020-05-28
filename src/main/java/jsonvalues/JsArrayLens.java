package jsonvalues;


/**
 Represent a Lens which focus is an array located at a path in a Json

 @param <S> the type of the whole part, an array or an object */
class JsArrayLens<S extends Json<S>> extends Lens<S, JsArray> {
    JsArrayLens(final JsPath path) {
        super(json -> json.getArray(path),
              a -> json -> json.set(path,
                                    a)
             );
    }
}
