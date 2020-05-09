package jsonvalues.future;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import static java.util.Objects.requireNonNull;


/**
 Represents a supplier of a completable future which result is a json array. It has the same
 recursive structure as a json array. Each index of the array is a completable future that it's
 executed asynchronously. When all the futures are completed, all the results are combined into
 a json array.

  JsArrayFuture(CompletableFuture(1),
                CompletableFuture("a"),
                CompletableFuture(true)
               ) =

 CompletableFuture(JsArray(1,"a",true))

  */

public class JsArrayFuture implements JsFuture<JsArray>
{
  private List<JsFuture<?>> array = new ArrayList<>();
  private Executor executor = ForkJoinPool.commonPool();

  private JsArrayFuture() { }

  /**
   the executor to use for the asynchronous operation assigned to this future.
   By default the ForkJoinPool.commonPool() will be used. Notice that any future of
   the array can run on threads froms different executors. The job to do by this JsArrayFuture is
   to iterate the array of futures, trigger every future and append the result of every completed
   future to the final array that will be returned.
   @param executor the executor
   */
  public void executor(final Executor executor)
  {
    this.executor = requireNonNull(executor);

  }

  private JsArrayFuture(final JsFuture<?> fut,
                        final JsFuture<?>... others
                       )
  {
    array.add(fut);
    array.addAll(Arrays.asList(others));
  }

  /**
   it triggers the execution of all the completable futures, combining the results into a JsArray
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
   returns a JsArrayFuture from the given head and the tail
   @param head the head
   @param tail the tail
   @return a new JsArrayFuture
   */
  public static JsArrayFuture of(final JsFuture<?> head,
                                 final JsFuture<?>... tail
                                )
  {
    return new JsArrayFuture(requireNonNull(head), requireNonNull(tail));
  }

  /**
   returns a JsArrayFuture that is completed returning the empty Json array
   @return a JsArrayFuture
   */
  public static JsArrayFuture empty()
  {
    return new JsArrayFuture();
  }
}
