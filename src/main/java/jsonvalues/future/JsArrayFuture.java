package jsonvalues.future;

import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;


 /**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future. When all the
 futures are completed, all the results are combined into a json array.

  JsArrayFuture(CompletableFuture(1),CompletableFuture("a"),CompletableFuture(true)) = CompletableFuture(JsArray(1,"a",true))

  */

public class JsArrayFuture implements JsFuture<JsArray>
{
  private List<JsFuture<?>> array = new ArrayList<>();
  private Executor executor = ForkJoinPool.commonPool();

  /**
   the executor to use for the asynchronous operation assigned to the future.
   By default the ForkJoinPool.commonPool() will be used.
   @param executor the executor
   */
  public void executor(final Executor executor)
  {
    this.executor = executor;

  }

  JsArrayFuture(final JsFuture<?> fut,
                final JsFuture<?>... others
               )
  {
    array.add(fut);
    array.addAll(Arrays.asList(others));
  }

  /**
  it triggers the execution of all the completable futures, appending the results into a JsArray

   @return a CompletableFuture of a json array
   */
  @Override
  public CompletableFuture<JsArray> get()
  {
    CompletableFuture<JsArray> result = CompletableFuture.completedFuture(JsArray.empty());

    for (final JsFuture<? extends JsValue> future : array)
    {
      result = result.thenCombineAsync(future.get(),
                                       (arr, value) -> arr.append(value),
                                       executor);
    }

    return result;
  }

  public static JsArrayFuture of(final JsFuture<?> fut,
                                 final JsFuture<?>... others
                                )
  {
    return new JsArrayFuture(fut, others);
  }
}
