package jsonvalues;


public class JsBoolLens<S extends Json<S>> extends Lens<S, Boolean>
{
  JsBoolLens(final JsPath path)
  {
    super(json -> json.getBool(path),
          n -> json -> json.set(path,JsBool.of(n))
         );
  }
}
