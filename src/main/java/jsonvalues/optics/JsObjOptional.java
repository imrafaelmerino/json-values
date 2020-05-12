package jsonvalues.optics;

import jsonvalues.JsObj;
import jsonvalues.JsPath;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class JsObjOptional extends JsOptional<JsObj>
{

  public static JsObjOptional key(final String key){
    return new JsObjOptional(JsPath.fromKey(requireNonNull(key)));
  }
  public static JsObjOptional path(final JsPath path){
    return new JsObjOptional(path);
  }

   JsObjOptional(final JsPath path)
  {
    super(path);
  }
}
