package jsonvalues.optics;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsPath;

public class JsArrayLens extends Lens<JsArray>
{

  public static JsArrayLens of(final JsPath path){
    return new JsArrayLens(path);
  }

   JsArrayLens(final JsPath path)
  {
    super(path);
  }
}
