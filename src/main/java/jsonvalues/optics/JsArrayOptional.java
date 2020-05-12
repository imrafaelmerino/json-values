package jsonvalues.optics;

import jsonvalues.JsArray;
import jsonvalues.JsPath;

public class JsArrayOptional extends JsOptional<JsArray>
{

  public static JsArrayOptional index(final int index){
    return new JsArrayOptional(JsPath.fromIndex(index));
  }
  public static JsArrayOptional path(final JsPath path){
    return new JsArrayOptional(path);
  }

   JsArrayOptional(final JsPath path)
  {
    super(path);
  }
}
