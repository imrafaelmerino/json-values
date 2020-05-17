package jsonvalues;


public class JsArrayLens<S extends Json<S>> extends Lens<S, JsArray>
{
  JsArrayLens(final JsPath path)
  {
    super(json -> json.getArray(path),
          a -> json -> json.set(path,a)
         );
  }
}
