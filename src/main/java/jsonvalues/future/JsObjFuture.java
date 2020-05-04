package jsonvalues.future;

import jsonvalues.JsObj;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class JsObjFuture implements JsFuture<JsObj>
{

  private Map<String, JsFuture<?>> bindings = new HashMap<>();
  private Executor executor = ForkJoinPool.commonPool();

  public void executor(final Executor executor)
  {
    this.executor = executor;

  }
  private JsObjFuture(final String key,
                      final JsFuture<?> fut
                     )
  {
    bindings.put(key,
                 fut
                );

  }


  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1
                     )
  {
    this(key,
         fut
        );
    bindings.put(key1,
                 fut1
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2
                     )
  {
    this(key,
         fut,
         key1,
         fut1
        );
    bindings.put(key2,
                 fut2
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2
        );
    bindings.put(key3,
                 fut3
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3
        );
    bindings.put(key4,
                 fut4
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4
        );
    bindings.put(key5,
                 fut5
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5,
                      final String key6,
                      final JsFuture<?> fut6
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4,
         key5,
         fut5
        );
    bindings.put(key6,
                 fut6
                );
  }


  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5,
                      final String key6,
                      final JsFuture<?> fut6,
                      final String key7,
                      final JsFuture<?> fut7
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4,
         key5,
         fut5,
         key6,
         fut6
        );
    bindings.put(key7,
                 fut7
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5,
                      final String key6,
                      final JsFuture<?> fut6,
                      final String key7,
                      final JsFuture<?> fut7,
                      final String key8,
                      final JsFuture<?> fut8
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4,
         key5,
         fut5,
         key6,
         fut6,
         key7,
         fut7
        );
    bindings.put(key8,
                 fut8
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5,
                      final String key6,
                      final JsFuture<?> fut6,
                      final String key7,
                      final JsFuture<?> fut7,
                      final String key8,
                      final JsFuture<?> fut8,
                      final String key9,
                      final JsFuture<?> fut9
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4,
         key5,
         fut5,
         key6,
         fut6,
         key7,
         fut7,
         key8,
         fut8
        );
    bindings.put(key9,
                 fut9
                );
  }

  private JsObjFuture(final String key,
                      final JsFuture<?> fut,
                      final String key1,
                      final JsFuture<?> fut1,
                      final String key2,
                      final JsFuture<?> fut2,
                      final String key3,
                      final JsFuture<?> fut3,
                      final String key4,
                      final JsFuture<?> fut4,
                      final String key5,
                      final JsFuture<?> fut5,
                      final String key6,
                      final JsFuture<?> fut6,
                      final String key7,
                      final JsFuture<?> fut7,
                      final String key8,
                      final JsFuture<?> fut8,
                      final String key9,
                      final JsFuture<?> fut9,
                      final String key10,
                      final JsFuture<?> fut10
                     )
  {
    this(key,
         fut,
         key1,
         fut1,
         key2,
         fut2,
         key3,
         fut3,
         key4,
         fut4,
         key5,
         fut5,
         key6,
         fut6,
         key7,
         fut7,
         key8,
         fut8,
         key9,
         fut9
        );
    bindings.put(key10,
                 fut10
                );
  }

  JsObjFuture(final Map<String, JsFuture<?>> bindings)
  {
   this.bindings = bindings;
  }

  @Override
  public CompletableFuture<JsObj> get()
  {

    CompletableFuture<JsObj> result = CompletableFuture.completedFuture(JsObj.empty());

    for (Map.Entry<String, JsFuture<?>> entry : bindings.entrySet())
    {
      result = result.thenCombineAsync(entry.getValue()
                                            .get(),
                                       (obj, value) -> obj.put(entry.getKey(),
                                                               value
                                                              ),
                                       executor
                                      );
    }

    return result;
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut
                              )
  {
    return new JsObjFuture(key,
                           fut
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5,
                               final String key6,
                               final JsFuture<?> fut6
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5,
                           key6,
                           fut6
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5,
                               final String key6,
                               final JsFuture<?> fut6,
                               final String key7,
                               final JsFuture<?> fut7
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5,
                           key6,
                           fut6,
                           key7,
                           fut7
    );

  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5,
                               final String key6,
                               final JsFuture<?> fut6,
                               final String key7,
                               final JsFuture<?> fut7,
                               final String key8,
                               final JsFuture<?> fut8
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5,
                           key6,
                           fut6,
                           key7,
                           fut7,
                           key8,
                           fut8
    );
  }

  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5,
                               final String key6,
                               final JsFuture<?> fut6,
                               final String key7,
                               final JsFuture<?> fut7,
                               final String key8,
                               final JsFuture<?> fut8,
                               final String key9,
                               final JsFuture<?> fut9
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5,
                           key6,
                           fut6,
                           key7,
                           fut7,
                           key8,
                           fut8,
                           key9,
                           fut9
    );
  }


  public static JsObjFuture of(final String key,
                               final JsFuture<?> fut,
                               final String key1,
                               final JsFuture<?> fut1,
                               final String key2,
                               final JsFuture<?> fut2,
                               final String key3,
                               final JsFuture<?> fut3,
                               final String key4,
                               final JsFuture<?> fut4,
                               final String key5,
                               final JsFuture<?> fut5,
                               final String key6,
                               final JsFuture<?> fut6,
                               final String key7,
                               final JsFuture<?> fut7,
                               final String key8,
                               final JsFuture<?> fut8,
                               final String key9,
                               final JsFuture<?> fut9,
                               final String key10,
                               final JsFuture<?> fut10
                              )
  {
    return new JsObjFuture(key,
                           fut,
                           key1,
                           fut1,
                           key2,
                           fut2,
                           key3,
                           fut3,
                           key4,
                           fut4,
                           key5,
                           fut5,
                           key6,
                           fut6,
                           key7,
                           fut7,
                           key8,
                           fut8,
                           key9,
                           fut9,
                           key10,
                           fut10
    );
  }
  public JsObjFuture put(final String key,
                      final JsFuture<?> future
                     )
  {
    bindings.put(key,
                 future
                );
    return new JsObjFuture(bindings);
  }

  public static JsObjFuture of(final JsFuturePair<?> pair,
                            final JsFuturePair<?>... others
                           )
  {

    JsObjFuture fut = new JsObjFuture(pair.key,
                                pair.future
    );
    for (final JsFuturePair<?> other : others)
      fut = fut.put(other.key,
                    other.future
                   );
    return fut;
  }

}
