package jsonvalues.optics;

import jsonvalues.JsArray;
import jsonvalues.JsPath;

public class JsArrayLens extends JsLens<JsArray>
{

  public static JsArrayLens index(final int index){
    return new JsArrayLens(JsPath.fromIndex(index));
  }
  public static JsArrayLens path(final JsPath path){
    return new JsArrayLens(path);
  }

   JsArrayLens(final JsPath path)
  {
    super(path);
  }
}
