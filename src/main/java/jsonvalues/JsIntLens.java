package jsonvalues;

public class JsIntLens<S extends Json<S>> extends JsAbstractLens<S,Integer>
{
  JsIntLens(final JsPath path)
  {
    super(json -> json.getInt(path),
          (json,n) -> json.set(path,JsInt.of(n))
         );
  }
}
