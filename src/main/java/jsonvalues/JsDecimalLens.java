package jsonvalues;

import java.math.BigDecimal;

public class JsDecimalLens<S extends Json<S>> extends JsAbstractLens<S, BigDecimal>
{
  JsDecimalLens(final JsPath path)
  {
    super(json -> json.getBigDec(path),
          n -> json -> json.set(path,JsBigDec.of(n))
         );
  }
}
