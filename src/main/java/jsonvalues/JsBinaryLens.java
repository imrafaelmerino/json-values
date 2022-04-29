package jsonvalues;


import static java.util.Objects.requireNonNull;

/**
 Represent a Lens which focus is an array of bytes located at a path in  a Json

 @param <S> the type of the whole part, an array or an object */
public final class JsBinaryLens<S extends Json<S>> extends Lens<S, byte[]> {
    JsBinaryLens(final JsPath path) {
        super(json -> requireNonNull(json).getBinary(path),
              o -> json -> requireNonNull(json).set(path,
                                    JsBinary.of(o))
             );
    }
}
