package jsonvalues;

import jsonvalues.console.JsArrayIO;
import jsonvalues.future.JsFuture;
import jsonvalues.future.JsObjFuture;
import jsonvalues.future.JsArrayFuture;
import jsonvalues.console.JsIOs;
import jsonvalues.console.JsObjIO;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsFutures
{


  public static JsFuture<?> log(JsFuture<?> future)
  {
    return () -> future.get()
                       .thenApply(it ->
                                  {
                                    System.out.println(String.format("Returned %s by %s",
                                                                     it,
                                                                     Thread.currentThread()
                                                                           .getName()
                                                                    ));
                                    return it;
                                  });
  }

  @Test
  public void test_obj_future() throws ExecutionException, InterruptedException
  {

    Executor io = Executors.newCachedThreadPool(getThreadFactory("io"));
    Executor computational = Executors.newFixedThreadPool(4,
                                                          getThreadFactory("computational")
                                                         );
    Executor eventLoop = Executors.newFixedThreadPool(8,
                                                      getThreadFactory("event-loop")
                                                     );

    JsObjFuture future = JsObjFuture.of("a",
                                        log(() -> completedFuture(JsInt.of(1))),
                                        "b",
                                        log(() -> completedFuture(JsStr.of("a"))),
                                        "c",
                                        log(() -> supplyAsync(() -> FALSE,
                                                              io
                                                             )),
                                        "d",
                                        log(JsArrayFuture.of(log(() -> supplyAsync(() -> FALSE,
                                                                                   computational
                                                                                  )),
                                                             log(() -> supplyAsync(() -> FALSE,
                                                                                   computational
                                                                                  ))
                                                            )),
                                        "e",
                                        log(JsObjFuture.of("a",
                                                           log(() -> supplyAsync(() -> JsNull.NULL,
                                                                                 eventLoop
                                                                                ))
                                                          ))
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
                                                           bool
                                                          ),
                                                     "e",
                                                     JsObjSpec.strict("a",
                                                                      any(it -> it == JsNull.NULL)
                                                                     )
                                                    )
                                             .test(obj);

    Assertions.assertTrue(errors
                            .isEmpty()
                         );


  }

  private static CompletableFuture<JsValue> readNullFromConsole(final String text)
  {
    return readValue(text,
                     s ->
                     {
                       if ("null".equalsIgnoreCase(s)) return JsNull.NULL;
                       throw new RuntimeException("null expected");
                     });
  }

  private static CompletableFuture<JsValue> readBooleanFromConsole(final String text)
  {
    return readValue(text,
                     s -> JsBool.of(Boolean.parseBoolean(s)));
  }

  private static CompletableFuture<JsValue> readDoubleFromConsole(final String text)
  {
    return readValue(text,
                     s -> JsDouble.of(Double.parseDouble(s)));
  }

  private static CompletableFuture<JsValue> readLongFromConsole(final String text)
  {
    return readValue(text,
                     s -> JsLong.of(Long.parseLong(s)));
  }

  private static CompletableFuture<JsValue> readIntFromConsole(final String text)
  {
    return readValue(text,
                     s -> JsInt.of(Integer.parseInt(s)));
  }

  private static CompletableFuture<JsValue> readStrFromConsole(final String text)
  {
    return readValue(text,
                     JsStr::of);
  }

  static <T extends JsValue> CompletableFuture<T> readValue(String text,
                                                            Function<String, T> fn
                                                           )
  {

    return completedFuture(readFromConsole(text,
                                           fn
                                          ));

  }

  private static <T extends JsValue> T readFromConsole(final String text,
                                                       Function<String, T> fn
                                                      )
  {
    Scanner in = new Scanner(System.in);

    System.out.println(text);

    String s = in.nextLine();

    return fn.apply(s);
  }


  public static void main(String[] args) throws ExecutionException, InterruptedException
  {
    /*JsObjFuture future = JsObjFuture.of("a",
                                        ()->readStrFromConsole("a value: "),
                                        "b",
                                        ()-> readStrFromConsole("a value: ")
                                       );

    System.out.println(future.get()
                             .get());*/

/*    JsObjIO obj = JsObjIO.of("a",
                                       readStr,
                                       "b",
                                       JsObjIO.of("c",readInt,"d",readBool) );

    System.out.println(obj.get().get());*/


    System.out.println("Let's create a Json object interactively! \nJust type in the values and press Enter");
    System.out.print("\n");

    JsObjIO obj = JsObjIO.of("a",
                             JsIOs.read(str),
                             "b",
                             JsObjIO.of("c",
                                        JsIOs.read(integer),
                                        "d",
                                        JsIOs.read(bool),
                                        "e",
                                        JsObjIO.of("f",
                                                   JsIOs.read(arrayOfInt)
                                                  )
                                       ),
                             "g",
                             JsArrayIO.of(JsIOs.read(integer),JsIOs.read(str))
                            );

    final JsObj x = obj.apply(JsPath.empty())
                       .get()
                       .get();
    System.out.print("\n");
    System.out.println("And the result is:");
    System.out.print("\n");
    System.out.println(x);

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
