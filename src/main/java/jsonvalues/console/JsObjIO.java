package jsonvalues.console;

import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


import static jsonvalues.console.JsIOs.indent;

/**
represents a supplier of a completable future than composes a json object from the user inputs in
the standard console. It has the same recursive structure as a json object. Each key has a completable
 future associated that prints it out in the standard console, waiting for the user to type in
 its associated value. When all the futures are completed, the json object is composed and returned
 */
public class JsObjIO implements JsIO<JsObj>
{

  private Map<String, JsIO<?>> bindings = new HashMap<>();


  @Override
  public Consumer<JsPath> promptMessage()
  {
    return path -> System.out.println(indent(path) + path);
  }

  @Override
  public JsFuture<JsObj> apply(JsPath path)
  {
   return () ->
   {
     CompletableFuture<JsObj> result = CompletableFuture.completedFuture(JsObj.empty());

     for (Map.Entry<String, JsIO<?>> entry : bindings.entrySet())
     {
       JsPath currentPath = path.append(JsPath.fromKey(entry.getKey()));
       final JsIO<?> nextValue = entry.getValue();
       result = result.thenApply(o ->
                                 {
                                   nextValue
                                        .promptMessage().accept(currentPath);

                                   return o;
                                 })
                      .thenCombine(nextValue
                                        .apply(currentPath)
                                        .get(),
                                   (obj, value) -> obj.put(entry.getKey(),
                                                           value
                                                          )
                                  );
     }
     return result;
   };


  }

  public static JsObjIO of(final String key,
                           final JsIO<?> io
                          )
  {

    final JsObjIO console = new JsObjIO();

    console.bindings.put(key,
                         io
                        );

    return console;

  }

  public static JsObjIO of(final String key,
                           final JsIO<?> io,
                           final String key1,
                           final JsIO<?> io1
                          )
  {

    final JsObjIO console = JsObjIO.of(key,
                                       io
                                      );

    console.bindings.put(key1,
                         io1
                        );

    return console;

  }

  public static JsObjIO of(final String key,
                           final JsIO<?> io,
                           final String key1,
                           final JsIO<?> io1,
                           final String key2,
                           final JsIO<?> io2
                          )
  {

    final JsObjIO console = JsObjIO.of(key,
                                       io,
                                       key1,
                                       io1
                                      );

    console.bindings.put(key2,
                         io2
                        );

    return console;

  }


}
