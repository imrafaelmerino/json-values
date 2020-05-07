package jsonvalues.console;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static jsonvalues.console.JsIOs.indent;

/**
 represents a supplier of a completable future than composes a json array from the user inputs in
 the standard console. It has the same recursive structure as a json array. Each index has a completable
 future associated that prints it out in the standard console, waiting for the user to type in
 its associated value.  When all the futures are completed, the json array is composed and returned
 */
public class JsArrayIO implements JsIO<JsArray>
{

  private List<JsIO<?>> seq = new ArrayList<>();


  public static JsArrayIO of(final JsIO<?> console,
                             final JsIO<?>... others
                            )
  {
    final JsArrayIO array = new JsArrayIO();
    array.seq.add(console);
    array.seq.addAll(Arrays.asList(others));
    return array;
  }

  @Override
  public JsFuture<JsArray> apply(JsPath path)
  {
    return () ->
    {

      CompletableFuture<JsArray> result = CompletableFuture.completedFuture(JsArray.empty());

      for (int i = 0; i < seq.size(); i++)

      {
        JsPath p = path.index(i);
        final JsIO<?> jsIO = seq.get(i);
        result = result.thenApply(array ->
                                  {
                                    jsIO
                                      .promptMessage()
                                      .accept(p);
                                    return array;

                                  })
                       .thenCombine(jsIO.apply(p)
                                        .get(),
                                    (array, value) -> array.append(value)
                                   );
      }

      return result;
    };
  }

  @Override
  public Consumer<JsPath> promptMessage()
  {
    return path -> System.out.println(indent(path) + path);
  }


}
