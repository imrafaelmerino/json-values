package jsonvalues;


public class JsDoubleLens<S extends Json<S>> extends Lens<S, Double>
{
  JsDoubleLens(final JsPath path)
  {
    super(json -> json.getDouble(path),
          n -> json -> json.set(path,JsDouble.of(n))
         );
  }
}
