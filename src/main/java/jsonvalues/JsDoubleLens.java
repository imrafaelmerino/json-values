package jsonvalues;


class JsDoubleLens<S extends Json<S>> extends JsAbstractLens<S, Double>
{
  JsDoubleLens(final JsPath path)
  {
    super(json -> json.getDouble(path),
          (json,n) -> json.set(path,JsDouble.of(n))
         );
  }
}
