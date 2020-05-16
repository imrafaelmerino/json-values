package jsonvalues;

public class JsStrLens<S extends Json<S>> extends JsAbstractLens<S,String>
{
  JsStrLens(final JsPath path)
  {
    super(json -> json.getStr(path),
          (json,str) -> json.set(path,JsStr.of(str))
         );
  }
}
