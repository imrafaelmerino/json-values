package jsonvalues.io;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

      for (JsIO<?> entry : seq)
      {
        final JsPath currentPath = path.inc();
        result = result.thenApply(array ->
                                  {
                                    System.out.println(currentPath + ": ");
                                    return array;
                                  })
                       .thenCombine(entry.apply(currentPath)
                                         .get(),
                                    (array, value) -> array.append(value)
                                   );
      }

      return result;
    };
  }


}
