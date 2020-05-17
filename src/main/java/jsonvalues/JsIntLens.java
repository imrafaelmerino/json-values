package jsonvalues;

public class JsIntLens<S extends Json<S>> extends Lens<S,Integer>
{
  JsIntLens(final JsPath path)
  {
    super(json -> json.getInt(path),
          n -> json -> json.set(path,JsInt.of(n))
         );
  }
}
