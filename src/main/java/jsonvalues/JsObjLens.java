package jsonvalues;



public class JsObjLens<S extends Json<S>> extends JsAbstractLens<S, JsObj>
{
  JsObjLens(final JsPath path)
  {
    super(json -> json.getObj(path),
          (json,o) -> json.set(path,o)
         );
  }
}
