package jsonvalues;



public class JsObjLens<S extends Json<S>> extends Lens<S, JsObj>
{
  JsObjLens(final JsPath path)
  {
    super(json -> json.getObj(path),
          o -> json ->  json.set(path,o)
         );
  }
}
