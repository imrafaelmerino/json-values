package jsonvalues.console;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.future.JsFuture;
import java.util.function.Consumer;
import java.util.function.Function;


import static jsonvalues.console.JsIOs.indent;

/**
 Represents a future that prints out in the standard console an index or key, waiting for the user
 to type in the associated value
 @param <T>
 */
public interface JsIO<T extends JsValue> extends Function<JsPath, JsFuture<T>>
{

  /**

   @return consumer that accepts a path and prints out a message indicating to the user that they must
   introduce a value
   */
  default Consumer<JsPath> promptMessage()
  {
    return path -> System.out.print(indent(path) + path + " -> ");
  }




}
