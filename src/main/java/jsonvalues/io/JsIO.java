package jsonvalues.io;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.future.JsFuture;
import java.util.function.Consumer;
import java.util.function.Function;


import static jsonvalues.io.JsIOs.indent;


public interface JsIO<T extends JsValue> extends Function<JsPath, JsFuture<T>>
{

  default Consumer<JsPath> promptMessage()
  {
    return path -> System.out.print(indent(path) + path + " -> ");
  }
}
