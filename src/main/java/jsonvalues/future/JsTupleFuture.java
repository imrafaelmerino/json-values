package jsonvalues.future;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class JsTupleFuture implements JsFuture<JsArray>
{
  private List<JsFuture<?>> tuple = new ArrayList<>();
  private Executor executor = ForkJoinPool.commonPool();

  public void executor(final Executor executor)
  {
    this.executor = executor;

  }

  JsTupleFuture(final JsFuture<?> fut,
                final JsFuture<?>... others
               )
  {
    tuple.add(fut);
    tuple.addAll(Arrays.asList(others));
  }

  @Override
  public CompletableFuture<JsArray> get()
  {
    CompletableFuture<JsArray> result = CompletableFuture.completedFuture(JsArray.empty());

    for (final JsFuture<? extends JsValue> future : tuple)
    {
      result = result.thenCombineAsync(future.get(),
                                       (arr, value) -> arr.append(value),
                                       executor);
    }

    return result;
  }

  public static JsTupleFuture of(final JsFuture<?> fut,
                                     final JsFuture<?>... others
                                    )
  {
    return new JsTupleFuture(fut,others);
  }
}
