package jsonvalues;

import io.vavr.Tuple2;
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

@Test
public void testPut() throws ExecutionException, InterruptedException
{

  final JsFuture<JsValue> a = () -> completedFuture(JsStr.of("a"));
  final JsObjFuture futA = JsObjFuture.empty()
                                      .put("a",
                                           a
                                          );

  final JsObjFuture futB = JsObjFuture.of("a",
                                        a).executor(Executors.newSingleThreadExecutor());


  final JsObjFuture futC = JsObjFuture.of(new Tuple2<>("a",a));

  Assertions.assertEquals(futA.get().get(), futB.get().get());
  Assertions.assertEquals(futA.get().get(), futC.get().get());
}

  @Test
  public void testAppend() throws ExecutionException, InterruptedException
  {

    final JsFuture<JsValue> a = () -> completedFuture(JsStr.of("a"));
    final JsArrayFuture futA = JsArrayFuture.empty()
                                        .append(
                                             a
                                            );

    final JsArrayFuture futB = JsArrayFuture.of(a).executor(Executors.newSingleThreadExecutor());



    Assertions.assertEquals(futA.get().get(), futB.get().get());
  }

  @Test
  public void testJsObjFuturesConstructors() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    Assertions.assertEquals(JsObjFuture.of("a",
                                     one,
                                     "b",
                                     one,
                                     "c",
                                     one
                                    )
                                 .get()
                                 .get(),JsObj.of("a",JsInt.of(1),
                                                 "b",JsInt.of(1),
                                                 "c",JsInt.of(1)
                                                )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors4() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           one,
                                           "c",
                                           one,
                                           "d",
                                           one
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(1),
                                                       "c",JsInt.of(1),
                                                       "d",JsInt.of(1)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors5() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors6() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6)
                                                       )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors7() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors8() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors9() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors10() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
    final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine,
                                           "j",
                                           ten
                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9),
                                                       "j",JsInt.of(10)
                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors11() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
    final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));
    final JsFuture<JsValue> eleven = () -> completedFuture(JsInt.of(11));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine,
                                           "j",
                                           ten,
                                           "k",
                                           eleven

                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9),
                                                       "j",JsInt.of(10),
                                                       "k",JsInt.of(11)

                                                       )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors12() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
    final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));
    final JsFuture<JsValue> eleven = () -> completedFuture(JsInt.of(11));
    final JsFuture<JsValue> twelve = () -> completedFuture(JsInt.of(12));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine,
                                           "j",
                                           ten,
                                           "k",
                                           eleven,
                                           "l",
                                           twelve

                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9),
                                                       "j",JsInt.of(10),
                                                       "k",JsInt.of(11),
                                                       "l",JsInt.of(12)

                                                      )
                           );
  }

  @Test
  public void testJsObjFuturesConstructors13() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
    final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));
    final JsFuture<JsValue> eleven = () -> completedFuture(JsInt.of(11));
    final JsFuture<JsValue> twelve = () -> completedFuture(JsInt.of(12));
    final JsFuture<JsValue> thirteen = () -> completedFuture(JsInt.of(13));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine,
                                           "j",
                                           ten,
                                           "k",
                                           eleven,
                                           "l",
                                           twelve,
                                           "m",
                                           thirteen

                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9),
                                                       "j",JsInt.of(10),
                                                       "k",JsInt.of(11),
                                                       "l",JsInt.of(12),
                                                       "m",JsInt.of(13)
                                                      )
                           );
  }

  @Test
public void testJsObjFuturesConstructors14() throws ExecutionException, InterruptedException
{
  final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
  final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
  final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
  final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
  final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
  final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
  final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
  final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
  final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
  final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));
  final JsFuture<JsValue> eleven = () -> completedFuture(JsInt.of(11));
  final JsFuture<JsValue> twelve = () -> completedFuture(JsInt.of(12));
  final JsFuture<JsValue> thirteen = () -> completedFuture(JsInt.of(13));
  final JsFuture<JsValue> fourteen = () -> completedFuture(JsInt.of(14));

  Assertions.assertEquals(JsObjFuture.of("a",
                                         one,
                                         "b",
                                         two,
                                         "c",
                                         three,
                                         "d",
                                         four,
                                         "e",
                                         five,
                                         "f",
                                         six,
                                         "g",
                                         seven,
                                         "h",
                                         eight,
                                         "i",
                                         nine,
                                         "j",
                                         ten,
                                         "k",
                                         eleven,
                                         "l",
                                         twelve,
                                         "m",
                                         thirteen,
                                         "n",
                                         fourteen

                                        )
                                     .get()
                                     .get(),JsObj.of("a",JsInt.of(1),
                                                     "b",JsInt.of(2),
                                                     "c",JsInt.of(3),
                                                     "d",JsInt.of(4),
                                                     "e",JsInt.of(5),
                                                     "f",JsInt.of(6),
                                                     "g",JsInt.of(7),
                                                     "h",JsInt.of(8),
                                                     "i",JsInt.of(9),
                                                     "j",JsInt.of(10),
                                                     "k",JsInt.of(11),
                                                     "l",JsInt.of(12),
                                                     "m",JsInt.of(13),
                                                     "n",JsInt.of(14)
                                                    )
                         );
}

  @Test
  public void testJsObjFuturesConstructors15() throws ExecutionException, InterruptedException
  {
    final JsFuture<JsValue> one = () -> completedFuture(JsInt.of(1));
    final JsFuture<JsValue> two = () -> completedFuture(JsInt.of(2));
    final JsFuture<JsValue> three = () -> completedFuture(JsInt.of(3));
    final JsFuture<JsValue> four = () -> completedFuture(JsInt.of(4));
    final JsFuture<JsValue> five = () -> completedFuture(JsInt.of(5));
    final JsFuture<JsValue> six = () -> completedFuture(JsInt.of(6));
    final JsFuture<JsValue> seven = () -> completedFuture(JsInt.of(7));
    final JsFuture<JsValue> eight = () -> completedFuture(JsInt.of(8));
    final JsFuture<JsValue> nine = () -> completedFuture(JsInt.of(9));
    final JsFuture<JsValue> ten = () -> completedFuture(JsInt.of(10));
    final JsFuture<JsValue> eleven = () -> completedFuture(JsInt.of(11));
    final JsFuture<JsValue> twelve = () -> completedFuture(JsInt.of(12));
    final JsFuture<JsValue> thirteen = () -> completedFuture(JsInt.of(13));
    final JsFuture<JsValue> fourteen = () -> completedFuture(JsInt.of(14));
    final JsFuture<JsValue> fifteen = () -> completedFuture(JsInt.of(15));

    Assertions.assertEquals(JsObjFuture.of("a",
                                           one,
                                           "b",
                                           two,
                                           "c",
                                           three,
                                           "d",
                                           four,
                                           "e",
                                           five,
                                           "f",
                                           six,
                                           "g",
                                           seven,
                                           "h",
                                           eight,
                                           "i",
                                           nine,
                                           "j",
                                           ten,
                                           "k",
                                           eleven,
                                           "l",
                                           twelve,
                                           "m",
                                           thirteen,
                                           "n",
                                           fourteen,
                                           "o",
                                           fifteen

                                          )
                                       .get()
                                       .get(),JsObj.of("a",JsInt.of(1),
                                                       "b",JsInt.of(2),
                                                       "c",JsInt.of(3),
                                                       "d",JsInt.of(4),
                                                       "e",JsInt.of(5),
                                                       "f",JsInt.of(6),
                                                       "g",JsInt.of(7),
                                                       "h",JsInt.of(8),
                                                       "i",JsInt.of(9),
                                                       "j",JsInt.of(10),
                                                       "k",JsInt.of(11),
                                                       "l",JsInt.of(12),
                                                       "m",JsInt.of(13),
                                                       "n",JsInt.of(14),
                                                       "o",JsInt.of(15)
                                                      )
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
