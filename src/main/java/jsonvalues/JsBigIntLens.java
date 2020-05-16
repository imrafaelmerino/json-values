package jsonvalues;


import java.math.BigInteger;

public class JsBigIntLens<S extends Json<S>> extends JsAbstractLens<S, BigInteger>
{
  JsBigIntLens(final JsPath path)
  {
    super(json -> json.getBigInt(path),
          (json,n) -> json.set(path,JsBigInt.of(n))
         );
  }
}
