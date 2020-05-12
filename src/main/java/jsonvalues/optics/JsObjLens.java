package jsonvalues.optics;

import jsonvalues.JsObj;
import jsonvalues.JsPath;

import java.util.Objects;

public class JsObjLens extends JsLens<JsObj>
{

  public static JsObjLens key(final String key){
    return new JsObjLens(JsPath.fromKey(Objects.requireNonNull(key)));
  }
  public static JsObjLens path(final JsPath path){
    return new JsObjLens(path);
  }

   JsObjLens(final JsPath path)
  {
    super(path);
  }
}
