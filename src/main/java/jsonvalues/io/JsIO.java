package jsonvalues.io;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.future.JsFuture;

import java.util.function.Function;


public interface JsIO<T extends JsValue> extends Function<JsPath,JsFuture<T>>
{

}
