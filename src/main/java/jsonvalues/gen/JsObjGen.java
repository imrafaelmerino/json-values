package jsonvalues.gen;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Supplier;

public class JsObjGen implements JsGen<JsObj>
{

  private JsObjGen(final Map<String, JsGen<?>> bindings)
  {
    this.bindings = bindings;
  }

  private Map<String, JsGen<?>> bindings = HashMap.empty();

  private JsObjGen(final String key,
                   final JsGen<?> gen
                  )
  {
    bindings = bindings.put(key,
                            gen
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1
                  )
  {
    this(key,
         gen
        );
    bindings = bindings.put(key1,
                            gen1
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2
                  )
  {
    this(key,
         gen,
         key1,
         gen1
        );
    bindings = bindings.put(key2,
                            gen2
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2
        );
    bindings = bindings.put(key3,
                            gen3
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3
        );
    bindings = bindings.put(key4,
                            gen4
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4
        );
    bindings = bindings.put(key5,
                            gen5
                           );
  }


  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5,
                   final String key6,
                   final JsGen<?> gen6
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4,
         key5,
         gen5
        );
    bindings = bindings.put(key6,
                            gen6
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5,
                   final String key6,
                   final JsGen<?> gen6,
                   final String key7,
                   final JsGen<?> gen7
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4,
         key5,
         gen5,
         key6,
         gen6
        );
    bindings = bindings.put(key7,
                            gen7
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5,
                   final String key6,
                   final JsGen<?> gen6,
                   final String key7,
                   final JsGen<?> gen7,
                   final String key8,
                   final JsGen<?> gen8
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4,
         key5,
         gen5,
         key6,
         gen6,
         key7,
         gen7
        );
    bindings = bindings.put(key8,
                            gen8
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5,
                   final String key6,
                   final JsGen<?> gen6,
                   final String key7,
                   final JsGen<?> gen7,
                   final String key8,
                   final JsGen<?> gen8,
                   final String key9,
                   final JsGen<?> gen9
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4,
         key5,
         gen5,
         key6,
         gen6,
         key7,
         gen7,
         key8,
         gen8
        );
    bindings = bindings.put(key9,
                            gen9
                           );
  }

  private JsObjGen(final String key,
                   final JsGen<?> gen,
                   final String key1,
                   final JsGen<?> gen1,
                   final String key2,
                   final JsGen<?> gen2,
                   final String key3,
                   final JsGen<?> gen3,
                   final String key4,
                   final JsGen<?> gen4,
                   final String key5,
                   final JsGen<?> gen5,
                   final String key6,
                   final JsGen<?> gen6,
                   final String key7,
                   final JsGen<?> gen7,
                   final String key8,
                   final JsGen<?> gen8,
                   final String key9,
                   final JsGen<?> gen9,
                   final String key10,
                   final JsGen<?> gen10
                  )
  {
    this(key,
         gen,
         key1,
         gen1,
         key2,
         gen2,
         key3,
         gen3,
         key4,
         gen4,
         key5,
         gen5,
         key6,
         gen6,
         key7,
         gen7,
         key8,
         gen8,
         key9,
         gen9
        );
    bindings = bindings.put(key10,
                            gen10
                           );
  }
  @Override
  public Supplier<JsObj> apply(final Random random)
  {
    return () ->
    {
      JsObj obj = JsObj.empty();
      for (final Tuple2<String, JsGen<?>> pair : bindings)
      {
        final JsValue value = pair._2.apply(random)
                                     .get();
        obj = obj.put(pair._1,
                      value
                     );
      }
      return obj;
    };
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen
                           )
  {
    return new JsObjGen(key,
                        gen
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5,
                            final String key6,
                            final JsGen<?> gen6
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5,
                        key6,
                        gen6
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5,
                            final String key6,
                            final JsGen<?> gen6,
                            final String key7,
                            final JsGen<?> gen7
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5,
                        key6,
                        gen6,
                        key7,
                        gen7
    );

  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5,
                            final String key6,
                            final JsGen<?> gen6,
                            final String key7,
                            final JsGen<?> gen7,
                            final String key8,
                            final JsGen<?> gen8
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5,
                        key6,
                        gen6,
                        key7,
                        gen7,
                        key8,
                        gen8
    );
  }

  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5,
                            final String key6,
                            final JsGen<?> gen6,
                            final String key7,
                            final JsGen<?> gen7,
                            final String key8,
                            final JsGen<?> gen8,
                            final String key9,
                            final JsGen<?> gen9
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5,
                        key6,
                        gen6,
                        key7,
                        gen7,
                        key8,
                        gen8,
                        key9,
                        gen9
    );
  }


  public static JsObjGen of(final String key,
                            final JsGen<?> gen,
                            final String key1,
                            final JsGen<?> gen1,
                            final String key2,
                            final JsGen<?> gen2,
                            final String key3,
                            final JsGen<?> gen3,
                            final String key4,
                            final JsGen<?> gen4,
                            final String key5,
                            final JsGen<?> gen5,
                            final String key6,
                            final JsGen<?> gen6,
                            final String key7,
                            final JsGen<?> gen7,
                            final String key8,
                            final JsGen<?> gen8,
                            final String key9,
                            final JsGen<?> gen9,
                            final String key10,
                            final JsGen<?> gen10
                           )
  {
    return new JsObjGen(key,
                        gen,
                        key1,
                        gen1,
                        key2,
                        gen2,
                        key3,
                        gen3,
                        key4,
                        gen4,
                        key5,
                        gen5,
                        key6,
                        gen6,
                        key7,
                        gen7,
                        key8,
                        gen8,
                        key9,
                        gen9,
                        key10,
                        gen10
    );
  }

  public JsObjGen put(final String key,
                      final JsGen<?> gen
                     )
  {
    return new JsObjGen(bindings.put(key,
                                     gen
                                    ));
  }

  public static JsObjGen of(final Tuple2<String,JsGen<?>> pair,
                            final Tuple2<String,JsGen<?>>... others
                           )
  {

    JsObjGen gen = new JsObjGen(pair._1,
                                pair._2
    );
    for (final Tuple2<String,JsGen<?>> other : others)
      gen = gen.put(other._1,
                    other._2
                   );
    return gen;
  }
}
