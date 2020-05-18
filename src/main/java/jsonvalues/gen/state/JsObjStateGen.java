package jsonvalues.gen.state;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.gen.JsGen;

public class JsObjStateGen implements JsStateGen {

    private Map<String, JsStateGen> bindings = HashMap.empty();

    private JsObjStateGen(final Map<String, JsStateGen> bindings) {
        this.bindings = bindings;
    }

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5,
                          final String key6,
                          final JsStateGen gen6,
                          final String key7,
                          final JsStateGen gen7,
                          final String key8,
                          final JsStateGen gen8,
                          final String key9,
                          final JsStateGen gen9,
                          final String key10,
                          final JsStateGen gen10
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5,
                          final String key6,
                          final JsStateGen gen6,
                          final String key7,
                          final JsStateGen gen7,
                          final String key8,
                          final JsStateGen gen8,
                          final String key9,
                          final JsStateGen gen9
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5,
                          final String key6,
                          final JsStateGen gen6,
                          final String key7,
                          final JsStateGen gen7,
                          final String key8,
                          final JsStateGen gen8
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5,
                          final String key6,
                          final JsStateGen gen6,
                          final String key7,
                          final JsStateGen gen7
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5,
                          final String key6,
                          final JsStateGen gen6
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4,
                          final String key5,
                          final JsStateGen gen5
                         ) {
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


    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3,
                          final String key4,
                          final JsStateGen gen4
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2,
                          final String key3,
                          final JsStateGen gen3
                         ) {
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

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1,
                          final String key2,
                          final JsStateGen gen2
                         ) {
        this(key,
             gen,
             key1,
             gen1
            );
        bindings = bindings.put(key2,
                                gen2
                               );
    }

    private JsObjStateGen(final String key,
                          final JsStateGen gen,
                          final String key1,
                          final JsStateGen gen1
                         ) {
        this(key,
             gen
            );
        bindings = bindings.put(key1,
                                gen1
                               );
    }

    private JsObjStateGen(final String key,
                          final JsStateGen gen
                         ) {
        bindings = bindings.put(key,
                                gen
                               );
    }

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen
                                  ) {
        return new JsObjStateGen(key,
                                 gen
        );
    }

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1
                                  ) {
        return new JsObjStateGen(key,
                                 gen,
                                 key1,
                                 gen1
        );
    }

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2
                                  ) {
        return new JsObjStateGen(key,
                                 gen,
                                 key1,
                                 gen1,
                                 key2,
                                 gen2
        );
    }

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3
                                  ) {
        return new JsObjStateGen(key,
                                 gen,
                                 key1,
                                 gen1,
                                 key2,
                                 gen2,
                                 key3,
                                 gen3
        );
    }

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5,
                                   final String key6,
                                   final JsStateGen gen6
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5,
                                   final String key6,
                                   final JsStateGen gen6,
                                   final String key7,
                                   final JsStateGen gen7
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5,
                                   final String key6,
                                   final JsStateGen gen6,
                                   final String key7,
                                   final JsStateGen gen7,
                                   final String key8,
                                   final JsStateGen gen8
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5,
                                   final String key6,
                                   final JsStateGen gen6,
                                   final String key7,
                                   final JsStateGen gen7,
                                   final String key8,
                                   final JsStateGen gen8,
                                   final String key9,
                                   final JsStateGen gen9
                                  ) {
        return new JsObjStateGen(key,
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

    public static JsObjStateGen of(final String key,
                                   final JsStateGen gen,
                                   final String key1,
                                   final JsStateGen gen1,
                                   final String key2,
                                   final JsStateGen gen2,
                                   final String key3,
                                   final JsStateGen gen3,
                                   final String key4,
                                   final JsStateGen gen4,
                                   final String key5,
                                   final JsStateGen gen5,
                                   final String key6,
                                   final JsStateGen gen6,
                                   final String key7,
                                   final JsStateGen gen7,
                                   final String key8,
                                   final JsStateGen gen8,
                                   final String key9,
                                   final JsStateGen gen9,
                                   final String key10,
                                   final JsStateGen gen10
                                  ) {
        return new JsObjStateGen(key,
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

    @Override
    public JsGen<JsObj> apply(final JsObj current) {

        return random -> () ->
        {
            JsObj obj = JsObj.empty();

            for (final Tuple2<String, JsStateGen> pair : bindings) {
                final JsValue value = pair._2.apply(obj)
                                             .apply(random)
                                             .get();
                obj = obj.set(pair._1,
                              value
                             );
            }
            return obj;
        };
    }

    public JsObjStateGen put(final String key,
                             final JsStateGen gen
                            ) {
        return new JsObjStateGen(bindings.put(key,
                                              gen
                                             ));
    }


}
