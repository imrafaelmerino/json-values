package jsonvalues;


/**
 Represent a Lens which focus is an array of bytes located at a path in  a Json

 @param <S> the type of the whole part, an array or an object */
public class JsBinaryLens<S extends Json<S>> extends Lens<S, byte[]> {
    JsBinaryLens(final JsPath path) {
        super(json -> json.getBinary(path),
              o -> json -> json.set(path,
                                    JsBinary.of(o))
             );
    }
}
