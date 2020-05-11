package jsonvalues.optics;

import jsonvalues.JsObj;
import jsonvalues.JsPath;

public class JsObjLens extends Lens<JsObj>
{

  public static JsObjLens of(final JsPath path){
    return new JsObjLens(path);
  }

   JsObjLens(final JsPath path)
  {
    super(path);
  }
}
