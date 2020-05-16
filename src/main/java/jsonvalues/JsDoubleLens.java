package jsonvalues;


public class JsDoubleLens<S extends Json<S>> extends JsAbstractLens<S, Double>
{
  JsDoubleLens(final JsPath path)
  {
    super(json -> json.getDouble(path),
          n -> json -> json.set(path,JsDouble.of(n))
         );
  }
}
