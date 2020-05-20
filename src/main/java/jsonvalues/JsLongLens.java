package jsonvalues;

/**
 Represent a Lens which focus is a long number located at a path is a Json

 @param <S> the type of the whole part, an array or an object */
public class JsLongLens<S extends Json<S>> extends Lens<S, Long> {
    JsLongLens(final JsPath path) {
        super(json -> json.getLong(path),
              n -> json -> json.set(path,
                                    JsLong.of(n))
             );
    }
}
