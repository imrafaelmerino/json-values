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
 Represents a json future which result type is a json array.
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
   returns a new CompletionFuture that, when all the futures of the array complete normally,
   is executed using the supplied executor appending each value in order to an empty array:

   JsArray(CompletableFuture(1),CompletableFuture("a"),CompletableFuture(true)) = CompletableFuture(JsArray(1,"a",true))

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

  /**
   returns a json future which result type is a json array. Homogenous finite arrays and heterogeneous
   finite arrays or tuples can be defined, being the latest the most common case.
   @param fut the first future of the array
   @param others others futures of the array
   @return a new json array future
   */
  public static JsArrayFuture of(final JsFuture<?> fut,
                                 final JsFuture<?>... others
                                )
  {
    return new JsArrayFuture(fut, others);
  }
}
