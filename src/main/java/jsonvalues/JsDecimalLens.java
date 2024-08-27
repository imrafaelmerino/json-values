package jsonvalues;

import static java.util.Objects.requireNonNull;

import fun.optic.Lens;
import java.math.BigDecimal;

/**
 * Represent a Lens which focus is a decimal number located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsDecimalLens<S extends Json<S>> extends Lens<S, BigDecimal> {

  JsDecimalLens(final JsPath path) {
    super(json -> requireNonNull(json).getBigDec(path),
          n -> json -> requireNonNull(json).set(path,
                                                JsBigDec.of(n))
         );
  }
}
