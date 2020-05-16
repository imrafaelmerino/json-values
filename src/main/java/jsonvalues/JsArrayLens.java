package jsonvalues;



class JsArrayLens<S extends Json<S>> extends JsAbstractLens<S, JsArray>
{
  JsArrayLens(final JsPath path)
  {
    super(json -> json.getArray(path),
          (json,a) -> json.set(path,a)
         );
  }
}
