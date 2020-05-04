package jsonvalues;

import jsonvalues.future.JsFuture;
import jsonvalues.future.JsObjFuture;
import jsonvalues.future.JsTupleFuture;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsFutures
{


  public static JsFuture<?> log(JsFuture<?> future){
    return () -> future.get().thenApply(it->
                                        {
                                           System.out.println(String.format("Returned %s by %s",it,Thread.currentThread().getName()));
                                           return it;
                                        });
  }

  @Test
  public void test_obj_future() throws ExecutionException, InterruptedException
  {

    Executor io = Executors.newCachedThreadPool(getThreadFactory("io"));
    Executor computational = Executors.newFixedThreadPool(4,
                                                          getThreadFactory("computational"));
    Executor eventLoop = Executors.newFixedThreadPool(8,
                                                      getThreadFactory("event-loop")
                                                     );

    JsObjFuture future = JsObjFuture.of("a",
                                        log(() -> completedFuture(JsInt.of(1))),
                                        "b",
                                        log(() -> completedFuture(JsStr.of("a"))),
                                        "c",
                                        log(() -> supplyAsync(() -> FALSE, io)),
                                        "d",
                                        log(JsTupleFuture.of(log(() -> supplyAsync(() -> FALSE, computational)),
                                                             log(() -> supplyAsync(() -> FALSE, computational))
                                                        )),
                                        "e",
                                        log(JsObjFuture.of("a",log(() -> supplyAsync(() -> JsNull.NULL, eventLoop))))
                                       );

    final CompletableFuture<JsObj> completableFuture = future.get();

    final JsObj obj = completableFuture.get();

    final Set<JsErrorPair> errors = JsObjSpec.strict("a",
                                                   integer,
                                                   "b",
                                                   str,
                                                   "c",
                                                   bool,
                                                   "d",
                                                   tuple(bool,
                                                         bool),
                                                   "e",JsObjSpec.strict("a",any(it -> it == JsNull.NULL))
                                                  )
                                           .test(obj);

    Assertions.assertTrue(errors
                                   .isEmpty()
                         );


  }

  private ThreadFactory getThreadFactory(final String s)
  {
    return r ->
    {

      final Thread thread = new Thread(r);
      thread.setName(s);
      return thread;
    };
  }
}
