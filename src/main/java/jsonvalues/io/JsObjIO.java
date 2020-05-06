package jsonvalues.io;

import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class JsObjIO implements JsIO<JsObj>
{

  private Map<String, JsIO<?>> bindings = new HashMap<>();


  @Override
  public JsFuture<JsObj> apply(JsPath path)
  {
   return () ->
   {
     CompletableFuture<JsObj> result = CompletableFuture.completedFuture(JsObj.empty());

     for (Map.Entry<String, JsIO<?>> entry : bindings.entrySet())
     {
       JsPath currentPath = path.append(JsPath.fromKey(entry.getKey()));
       result = result.thenApply(o ->
                                 {
                                   System.out.println(currentPath + " -> ");
                                   return o;
                                 })
                      .thenCombine(entry.getValue()
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


}
