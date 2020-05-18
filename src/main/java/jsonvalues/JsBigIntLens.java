package jsonvalues;


import java.math.BigInteger;

/**
 Represent a Lens which focus is a biginteger number located at a path in a Json

 @param <S> the type of the whole part, an array or an object */
public class JsBigIntLens<S extends Json<S>> extends Lens<S, BigInteger> {
    JsBigIntLens(final JsPath path) {
        super(json -> json.getBigInt(path),
              n -> json -> json.set(path,
                                    JsBigInt.of(n))
             );
    }
}
