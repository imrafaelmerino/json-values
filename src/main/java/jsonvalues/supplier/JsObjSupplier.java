package jsonvalues.supplier;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.util.function.Supplier;
import static java.util.Objects.requireNonNull;

/**
 Represents a Json object of suppliers that combines every supplier
 and produces as a result a Json object
 */
public class JsObjSupplier implements Supplier<JsObj> {


    private Map<String, Supplier<? extends JsValue>> bindings = LinkedHashMap.empty();

    @Override
    public JsObj get() {
        JsObj obj = JsObj.empty();
        for (final Tuple2<String, Supplier<? extends JsValue>> tuple2 : bindings) {
            obj = obj.set(tuple2._1,
                          tuple2._2.get());
        }
        return obj;
    }


    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9,
                          final String key10,
                          final Supplier<? extends JsValue> supplier10,
                          final String key11,
                          final Supplier<? extends JsValue> supplier11,
                          final String key12,
                          final Supplier<? extends JsValue> supplier12,
                          final String key13,
                          final Supplier<? extends JsValue> supplier13,
                          final String key14,
                          final Supplier<? extends JsValue> supplier14
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8,
             key9,
             supplier9,
             key10,
             supplier10,
             key11,
             supplier11,
             key12,
             supplier12,
             key13,
             supplier13
            );
        bindings = bindings.put(key14,
                                supplier14
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9,
                          final String key10,
                          final Supplier<? extends JsValue> supplier10,
                          final String key11,
                          final Supplier<? extends JsValue> supplier11,
                          final String key12,
                          final Supplier<? extends JsValue> supplier12,
                          final String key13,
                          final Supplier<? extends JsValue> supplier13
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8,
             key9,
             supplier9,
             key10,
             supplier10,
             key11,
             supplier11,
             key12,
             supplier12
            );
        bindings = bindings.put(key13,
                                supplier13
                               );
    }


    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9,
                          final String key10,
                          final Supplier<? extends JsValue> supplier10,
                          final String key11,
                          final Supplier<? extends JsValue> supplier11,
                          final String key12,
                          final Supplier<? extends JsValue> supplier12
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8,
             key9,
             supplier9,
             key10,
             supplier10,
             key11,
             supplier11
            );
        bindings = bindings.put(key12,
                                supplier12
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9,
                          final String key10,
                          final Supplier<? extends JsValue> supplier10,
                          final String key11,
                          final Supplier<? extends JsValue> supplier11
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8,
             key9,
             supplier9,
             key10,
             supplier10
            );
        bindings = bindings.put(key11,
                                supplier11
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9,
                          final String key10,
                          final Supplier<? extends JsValue> supplier10
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8,
             key9,
             supplier9
            );
        bindings = bindings.put(key10,
                                supplier10
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8,
                          final String key9,
                          final Supplier<? extends JsValue> supplier9
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7,
             key8,
             supplier8
            );
        bindings = bindings.put(key9,
                                supplier9
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7,
                          final String key8,
                          final Supplier<? extends JsValue> supplier8
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6,
             key7,
             supplier7
            );
        bindings = bindings.put(key8,
                                supplier8
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6,
                          final String key7,
                          final Supplier<? extends JsValue> supplier7
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5,
             key6,
             supplier6
            );
        bindings = bindings.put(key7,
                                supplier7
                               );
    }


    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5,
                          final String key6,
                          final Supplier<? extends JsValue> supplier6
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4,
             key5,
             supplier5
            );
        bindings = bindings.put(key6,
                                supplier6
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4,
                          final String key5,
                          final Supplier<? extends JsValue> supplier5
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3,
             key4,
             supplier4
            );
        bindings = bindings.put(key5,
                                supplier5
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3,
                          final String key4,
                          final Supplier<? extends JsValue> supplier4
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2,
             key3,
             supplier3
            );
        bindings = bindings.put(key4,
                                supplier4
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2,
                          final String key3,
                          final Supplier<? extends JsValue> supplier3
                         ) {
        this(key,
             supplier,
             key1,
             supplier1,
             key2,
             supplier2
            );
        bindings = bindings.put(key3,
                                supplier3
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1,
                          final String key2,
                          final Supplier<? extends JsValue> supplier2
                         ) {
        this(key,
             supplier,
             key1,
             supplier1
            );
        bindings = bindings.put(key2,
                                supplier2
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier,
                          final String key1,
                          final Supplier<? extends JsValue> supplier1
                         ) {
        this(key,
             supplier
            );
        bindings = bindings.put(key1,
                                supplier1
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> supplier
                         ) {
        bindings = bindings.put(key,
                                supplier
                               );

    }

    private JsObjSupplier(final Map<String, Supplier<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }

    /**
     static factory method to create a JsObjSupplier of one mapping

     @param key the key
     @param supplier the mapping associated to the key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key,
                                   final Supplier<? extends JsValue> supplier
                                  ) {
        return new JsObjSupplier(requireNonNull(key),
                                 requireNonNull(supplier)
        );
    }

    /**
     static factory method to create a JsObjSupplier of one mapping

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2)
        );
    }

    /**
     static factory method to create a JsObjSupplier of three mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3)
        );
    }

    /**
     static factory method to create a JsObjSupplier of four mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4)
        );
    }

    /**
     static factory method to create a JsObjSupplier of five mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param supplier5 the mapping associated to the fifth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5)
        );
    }

    /**
     static factory method to create a JsObjSupplier of six mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param supplier5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param supplier6 the mapping associated to the sixth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6)
        );
    }

    /**
     static factory method to create a JsObjSupplier of seven mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param supplier5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param supplier6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param supplier7 the mapping associated to the seventh key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7)
        );
    }

    /**
     static factory method to create a JsObjSupplier of eight mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param supplier5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param supplier6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param supplier7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param supplier8 the mapping associated to the eighth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8)
        );

    }

    /**
     static factory method to create a JsObjSupplier of nine mappings

     @param key1 the first key
     @param supplier1 the mapping associated to the first key
     @param key2 the second key
     @param supplier2 the mapping associated to the second key
     @param key3 the third key
     @param supplier3 the mapping associated to the third key
     @param key4 the fourth key
     @param supplier4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param supplier5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param supplier6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param supplier7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param supplier8 the mapping associated to the eighth key
     @param key9 the ninth key
     @param supplier9 the mapping associated to the ninth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9)
        );

    }

    /**
     static factory method to create a JsObjSupplier of ten mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the tenth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10)
        );

    }

    /**
     static factory method to create a JsObjSupplier of eleven mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the eleventh key
     @param key11 the tenth key
     @param supplier11 the mapping associated to the eleventh key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10,
                                   final String key11,
                                   final Supplier<? extends JsValue> supplier11
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10),
                                 requireNonNull(key11),
                                 requireNonNull(supplier11)
        );

    }

    /**
     static factory method to create a JsObjSupplier of twelve mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param supplier11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param supplier12 the mapping associated to the twelfth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10,
                                   final String key11,
                                   final Supplier<? extends JsValue> supplier11,
                                   final String key12,
                                   final Supplier<? extends JsValue> supplier12
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10),
                                 requireNonNull(key11),
                                 requireNonNull(supplier11),
                                 requireNonNull(key12),
                                 requireNonNull(supplier12)
        );

    }

    /**
     static factory method to create a JsObjSupplier of thirteen mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param supplier11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param supplier12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param supplier13 the mapping associated to the thirteenth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10,
                                   final String key11,
                                   final Supplier<? extends JsValue> supplier11,
                                   final String key12,
                                   final Supplier<? extends JsValue> supplier12,
                                   final String key13,
                                   final Supplier<? extends JsValue> supplier13
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10),
                                 requireNonNull(key11),
                                 requireNonNull(supplier11),
                                 requireNonNull(key12),
                                 requireNonNull(supplier12),
                                 requireNonNull(key13),
                                 requireNonNull(supplier13)

        );

    }

    /**
     static factory method to create a JsObjSupplier of fourteen mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param supplier11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param supplier12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param supplier13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param supplier14 the mapping associated to the fourteenth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10,
                                   final String key11,
                                   final Supplier<? extends JsValue> supplier11,
                                   final String key12,
                                   final Supplier<? extends JsValue> supplier12,
                                   final String key13,
                                   final Supplier<? extends JsValue> supplier13,
                                   final String key14,
                                   final Supplier<? extends JsValue> supplier14
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10),
                                 requireNonNull(key11),
                                 requireNonNull(supplier11),
                                 requireNonNull(key12),
                                 requireNonNull(supplier12),
                                 requireNonNull(key13),
                                 requireNonNull(supplier13),
                                 requireNonNull(key14),
                                 requireNonNull(supplier14)

        );

    }

    /**
     static factory method to create a JsObjSupplier of fifteen mappings

     @param key1  the first key
     @param supplier1  the mapping associated to the first key
     @param key2  the second key
     @param supplier2  the mapping associated to the second key
     @param key3  the third key
     @param supplier3  the mapping associated to the third key
     @param key4  the fourth key
     @param supplier4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param supplier5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param supplier6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param supplier7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param supplier8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param supplier9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param supplier10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param supplier11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param supplier12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param supplier13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param supplier14 the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param supplier15 the mapping associated to the fifteenth key
     @return a JsObjSupplier
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> supplier1,
                                   final String key2,
                                   final Supplier<? extends JsValue> supplier2,
                                   final String key3,
                                   final Supplier<? extends JsValue> supplier3,
                                   final String key4,
                                   final Supplier<? extends JsValue> supplier4,
                                   final String key5,
                                   final Supplier<? extends JsValue> supplier5,
                                   final String key6,
                                   final Supplier<? extends JsValue> supplier6,
                                   final String key7,
                                   final Supplier<? extends JsValue> supplier7,
                                   final String key8,
                                   final Supplier<? extends JsValue> supplier8,
                                   final String key9,
                                   final Supplier<? extends JsValue> supplier9,
                                   final String key10,
                                   final Supplier<? extends JsValue> supplier10,
                                   final String key11,
                                   final Supplier<? extends JsValue> supplier11,
                                   final String key12,
                                   final Supplier<? extends JsValue> supplier12,
                                   final String key13,
                                   final Supplier<? extends JsValue> supplier13,
                                   final String key14,
                                   final Supplier<? extends JsValue> supplier14,
                                   final String key15,
                                   final Supplier<? extends JsValue> supplier15
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(supplier1),
                                 requireNonNull(key2),
                                 requireNonNull(supplier2),
                                 requireNonNull(key3),
                                 requireNonNull(supplier3),
                                 requireNonNull(key4),
                                 requireNonNull(supplier4),
                                 requireNonNull(key5),
                                 requireNonNull(supplier5),
                                 requireNonNull(key6),
                                 requireNonNull(supplier6),
                                 requireNonNull(key7),
                                 requireNonNull(supplier7),
                                 requireNonNull(key8),
                                 requireNonNull(supplier8),
                                 requireNonNull(key9),
                                 requireNonNull(supplier9),
                                 requireNonNull(key10),
                                 requireNonNull(supplier10),
                                 requireNonNull(key11),
                                 requireNonNull(supplier11),
                                 requireNonNull(key12),
                                 requireNonNull(supplier12),
                                 requireNonNull(key13),
                                 requireNonNull(supplier13),
                                 requireNonNull(key14),
                                 requireNonNull(supplier14),
                                 requireNonNull(key15),
                                 requireNonNull(supplier15)

        );

    }

    /**
     returns a JsObjSupplier that is completed returning the empty Json object

     @return a JsObjSupplier
     */
    public static JsObjSupplier empty() {
        return new JsObjSupplier(HashMap.empty());
    }

    /**
     returns a new object supplier inserting the given supplier at the given key
     @param key    the given key
     @param supplier the given supplier
     @return a new JsObjSupplier
     */
    public JsObjSupplier set(final String key,
                             final Supplier<? extends JsValue> supplier
                            ) {
        final Map<String, Supplier<? extends JsValue>> a = bindings.put(requireNonNull(key),
                                                                        requireNonNull(supplier)
                                                                       );
        return new JsObjSupplier(a);
    }
}

