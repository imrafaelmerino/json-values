package jsonvalues;

public class JsLongLens<S extends Json<S>> extends JsAbstractLens<S,Long>
{
  JsLongLens(final JsPath path)
  {
    super(json -> json.getLong(path),
          n -> json -> json.set(path,JsLong.of(n))
         );
  }
}
