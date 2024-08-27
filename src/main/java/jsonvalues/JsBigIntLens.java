package jsonvalues;


import static java.util.Objects.requireNonNull;

import fun.optic.Lens;
import java.math.BigInteger;

/**
 * Represent a Lens which focus is a biginteger number located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsBigIntLens<S extends Json<S>> extends Lens<S, BigInteger> {

  JsBigIntLens(final JsPath path) {
    super(json -> requireNonNull(json).getBigInt(path),
          n -> json -> requireNonNull(json).set(path,
                                                JsBigInt.of(n))
         );
  }
}
