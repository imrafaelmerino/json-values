package jsonvalues.gen;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.util.Random;
import java.util.function.Supplier;
import static java.util.Objects.requireNonNull;

/**
 represent a generator of Json objects.
 */
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
                   final JsGen<?> gen10,
                   final String key11,
                   final JsGen<?> gen11
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
         gen9,
         key10,
         gen10
        );
    bindings = bindings.put(key11,
                            gen11
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
                   final JsGen<?> gen10,
                   final String key11,
                   final JsGen<?> gen11,
                   final String key12,
                   final JsGen<?> gen12
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
         gen9,
         key10,
         gen10,
         key11,
         gen11
        );
    bindings = bindings.put(key12,
                            gen12
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
                   final JsGen<?> gen10,
                   final String key11,
                   final JsGen<?> gen11,
                   final String key12,
                   final JsGen<?> gen12,
                   final String key13,
                   final JsGen<?> gen13
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
         gen9,
         key10,
         gen10,
         key11,
         gen11,
         key12,
         gen12
        );
    bindings = bindings.put(key13,
                            gen13
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
                   final JsGen<?> gen10,
                   final String key11,
                   final JsGen<?> gen11,
                   final String key12,
                   final JsGen<?> gen12,
                   final String key13,
                   final JsGen<?> gen13,
                   final String key14,
                   final JsGen<?> gen14
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
         gen9,
         key10,
         gen10,
         key11,
         gen11,
         key12,
         gen12,
         key13,
         gen13
        );
    bindings = bindings.put(key14,
                            gen14
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

  /**
   static factory method to create a JsObGen of ten mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
    return new JsObjGen(key1,
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


  /**
   static factory method to create a JsObGen of eleven mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @param key11 the eleventh key
   @param gen11 the mapping associated to the eleventh key
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
                            final JsGen<?> gen10,
                            final String key11,
                            final JsGen<?> gen11
                           )
  {
    return new JsObjGen(key1,
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
                        gen10,
                        key11,
                        gen11
    );
  }

  /**
   static factory method to create a JsObGen of twelve mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @param key11 the eleventh key
   @param gen11 the mapping associated to the eleventh key
   @param key12 the twelfth key
   @param gen12 the mapping associated to the twelfth key,
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
                            final JsGen<?> gen10,
                            final String key11,
                            final JsGen<?> gen11,
                            final String key12,
                            final JsGen<?> gen12
                           )
  {
    return new JsObjGen(key1,
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
                        gen10,
                        key11,
                        gen11,
                        key12,
                        gen12
    );
  }
  /**
   static factory method to create a JsObGen of thirteen mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @param key11 the eleventh key
   @param gen11 the mapping associated to the eleventh key
   @param key12 the twelfth key
   @param gen12 the mapping associated to the twelfth key,
   @param key13 the thirteenth key
   @param gen13 the mapping associated to the thirteenth key
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
                            final JsGen<?> gen10,
                            final String key11,
                            final JsGen<?> gen11,
                            final String key12,
                            final JsGen<?> gen12,
                            final String key13,
                            final JsGen<?> gen13
                           )
  {
    return new JsObjGen(key1,
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
                        gen10,
                        key11,
                        gen11,
                        key12,
                        gen12,
                        key13,
                        gen13
    );
  }
  /**
   static factory method to create a JsObGen of fourteen mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @param key11 the eleventh key
   @param gen11 the mapping associated to the eleventh key
   @param key12 the twelfth key
   @param gen12 the mapping associated to the twelfth key,
   @param key13 the thirteenth key
   @param gen13 the mapping associated to the thirteenth key
   @param key14 the fourteenth key
   @param gen14 the mapping associated to the fourteenth key
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
                            final JsGen<?> gen10,
                            final String key11,
                            final JsGen<?> gen11,
                            final String key12,
                            final JsGen<?> gen12,
                            final String key13,
                            final JsGen<?> gen13,
                            final String key14,
                            final JsGen<?> gen14
                           )
  {
    return new JsObjGen(key1,
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
                        gen10,
                        key11,
                        gen11,
                        key12,
                        gen12,
                        key13,
                        gen13,
                        key14,
                        gen14
    );
  }

  /**
   static factory method to create a JsObGen of fifteen mappings
   @param key1 the first key
   @param gen1 the mapping associated to the first key
   @param key2 the second key
   @param gen2 the mapping associated to the second key
   @param key3 the third key
   @param gen3 the mapping associated to the third key
   @param key4 the fourth key
   @param gen4 the mapping associated to the fourth key
   @param key5 the fifth key
   @param gen5 the mapping associated to the fifth key
   @param key6 the sixth key
   @param gen6 the mapping associated to the sixth key
   @param key7 the seventh key
   @param gen7 the mapping associated to the seventh key
   @param key8 the eighth key
   @param gen8 the mapping associated to the eighth key
   @param key9 the ninth key
   @param gen9 the mapping associated to the ninth key
   @param key10 the tenth key
   @param gen10 the mapping associated to the eleventh key
   @param key11 the eleventh key
   @param gen11 the mapping associated to the eleventh key
   @param key12 the twelfth key
   @param gen12 the mapping associated to the twelfth key,
   @param key13 the thirteenth key
   @param gen13 the mapping associated to the thirteenth key
   @param key14 the fourteenth key
   @param gen14 the mapping associated to the fourteenth key
   @param key15 the fifteenth key
   @param gen15 the mapping associated to the fifteenth key
   @return a JsObjGen
   */
  public static JsObjGen of(final String key1,
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
                            final JsGen<?> gen10,
                            final String key11,
                            final JsGen<?> gen11,
                            final String key12,
                            final JsGen<?> gen12,
                            final String key13,
                            final JsGen<?> gen13,
                            final String key14,
                            final JsGen<?> gen14,
                            final String key15,
                            final JsGen<?> gen15
                           )
  {
    return new JsObjGen(key1,
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
                        gen10,
                        key11,
                        gen11,
                        key12,
                        gen12,
                        key13,
                        gen13,
                        key14,
                        gen14,
                        key15,
                        gen15
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

  @SafeVarargs
  public static JsObjGen of(final Tuple2<String,JsGen<?>> pair,
                            final Tuple2<String,JsGen<?>>... others
                           )
  {

    JsObjGen gen = new JsObjGen(requireNonNull(pair)._1,
                                pair._2
    );
    for (final Tuple2<String,JsGen<?>> other : requireNonNull(others))
      gen = gen.put(other._1,
                    other._2
                   );
    return gen;
  }

}
