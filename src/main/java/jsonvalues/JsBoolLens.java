package jsonvalues;


class JsBoolLens<S extends Json<S>> extends JsAbstractLens<S, Boolean>
{
  JsBoolLens(final JsPath path)
  {
    super(json -> json.getBool(path),
          (json,n) -> json.set(path,JsBool.of(n))
         );
  }
}
