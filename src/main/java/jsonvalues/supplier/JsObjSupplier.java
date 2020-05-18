package jsonvalues.supplier;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class JsObjSupplier implements Supplier<JsObj> {


    private Map<String, Supplier<? extends JsValue>> bindings = HashMap.empty();

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
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9,
                          final String key10,
                          final Supplier<? extends JsValue> fut10,
                          final String key11,
                          final Supplier<? extends JsValue> fut11,
                          final String key12,
                          final Supplier<? extends JsValue> fut12,
                          final String key13,
                          final Supplier<? extends JsValue> fut13,
                          final String key14,
                          final Supplier<? extends JsValue> fut14
                         ) {
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
             fut9,
             key10,
             fut10,
             key11,
             fut11,
             key12,
             fut12,
             key13,
             fut13
            );
        bindings = bindings.put(key14,
                                fut14
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9,
                          final String key10,
                          final Supplier<? extends JsValue> fut10,
                          final String key11,
                          final Supplier<? extends JsValue> fut11,
                          final String key12,
                          final Supplier<? extends JsValue> fut12,
                          final String key13,
                          final Supplier<? extends JsValue> fut13
                         ) {
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
             fut9,
             key10,
             fut10,
             key11,
             fut11,
             key12,
             fut12
            );
        bindings = bindings.put(key13,
                                fut13
                               );
    }


    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9,
                          final String key10,
                          final Supplier<? extends JsValue> fut10,
                          final String key11,
                          final Supplier<? extends JsValue> fut11,
                          final String key12,
                          final Supplier<? extends JsValue> fut12
                         ) {
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
             fut9,
             key10,
             fut10,
             key11,
             fut11
            );
        bindings = bindings.put(key12,
                                fut12
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9,
                          final String key10,
                          final Supplier<? extends JsValue> fut10,
                          final String key11,
                          final Supplier<? extends JsValue> fut11
                         ) {
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
             fut9,
             key10,
             fut10
            );
        bindings = bindings.put(key11,
                                fut11
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9,
                          final String key10,
                          final Supplier<? extends JsValue> fut10
                         ) {
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
        bindings = bindings.put(key10,
                                fut10
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8,
                          final String key9,
                          final Supplier<? extends JsValue> fut9
                         ) {
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
        bindings = bindings.put(key9,
                                fut9
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7,
                          final String key8,
                          final Supplier<? extends JsValue> fut8
                         ) {
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
        bindings = bindings.put(key8,
                                fut8
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6,
                          final String key7,
                          final Supplier<? extends JsValue> fut7
                         ) {
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
        bindings = bindings.put(key7,
                                fut7
                               );
    }


    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5,
                          final String key6,
                          final Supplier<? extends JsValue> fut6
                         ) {
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
        bindings = bindings.put(key6,
                                fut6
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4,
                          final String key5,
                          final Supplier<? extends JsValue> fut5
                         ) {
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
        bindings = bindings.put(key5,
                                fut5
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3,
                          final String key4,
                          final Supplier<? extends JsValue> fut4
                         ) {
        this(key,
             fut,
             key1,
             fut1,
             key2,
             fut2,
             key3,
             fut3
            );
        bindings = bindings.put(key4,
                                fut4
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2,
                          final String key3,
                          final Supplier<? extends JsValue> fut3
                         ) {
        this(key,
             fut,
             key1,
             fut1,
             key2,
             fut2
            );
        bindings = bindings.put(key3,
                                fut3
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1,
                          final String key2,
                          final Supplier<? extends JsValue> fut2
                         ) {
        this(key,
             fut,
             key1,
             fut1
            );
        bindings = bindings.put(key2,
                                fut2
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut,
                          final String key1,
                          final Supplier<? extends JsValue> fut1
                         ) {
        this(key,
             fut
            );
        bindings = bindings.put(key1,
                                fut1
                               );
    }

    private JsObjSupplier(final String key,
                          final Supplier<? extends JsValue> fut
                         ) {
        bindings = bindings.put(key,
                                fut
                               );

    }

    private JsObjSupplier(final Map<String, Supplier<? extends JsValue>> bindings) {
        this.bindings = bindings;
    }

    /**
     static factory method to create a JsObjSupplier of one mapping

     @param key the key
     @param fut the mapping associated to the key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key,
                                   final Supplier<? extends JsValue> fut
                                  ) {
        return new JsObjSupplier(requireNonNull(key),
                                 requireNonNull(fut)
        );
    }

    /**
     static factory method to create a JsObjSupplier of one mapping

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2)
        );
    }

    /**
     static factory method to create a JsObjSupplier of three mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3)
        );
    }

    /**
     static factory method to create a JsObjSupplier of four mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4)
        );
    }

    /**
     static factory method to create a JsObjSupplier of five mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param fut5 the mapping associated to the fifth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5)
        );
    }

    /**
     static factory method to create a JsObjSupplier of six mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param fut5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param fut6 the mapping associated to the sixth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6)
        );
    }

    /**
     static factory method to create a JsObjSupplier of seven mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param fut5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param fut6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param fut7 the mapping associated to the seventh key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7)
        );
    }

    /**
     static factory method to create a JsObjSupplier of eight mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param fut5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param fut6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param fut7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param fut8 the mapping associated to the eighth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8)
        );

    }

    /**
     static factory method to create a JsObjSupplier of nine mappings

     @param key1 the first key
     @param fut1 the mapping associated to the first key
     @param key2 the second key
     @param fut2 the mapping associated to the second key
     @param key3 the third key
     @param fut3 the mapping associated to the third key
     @param key4 the fourth key
     @param fut4 the mapping associated to the fourth key
     @param key5 the fifth key
     @param fut5 the mapping associated to the fifth key
     @param key6 the sixth key
     @param fut6 the mapping associated to the sixth key
     @param key7 the seventh key
     @param fut7 the mapping associated to the seventh key
     @param key8 the eighth key
     @param fut8 the mapping associated to the eighth key
     @param key9 the ninth key
     @param fut9 the mapping associated to the ninth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9)
        );

    }

    /**
     static factory method to create a JsObjSupplier of ten mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the tenth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10)
        );

    }

    /**
     static factory method to create a JsObjSupplier of eleven mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the tenth key
     @param fut11 the mapping associated to the eleventh key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10,
                                   final String key11,
                                   final Supplier<? extends JsValue> fut11
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10),
                                 requireNonNull(key11),
                                 requireNonNull(fut11)
        );

    }

    /**
     static factory method to create a JsObjSupplier of twelve mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param fut11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param fut12 the mapping associated to the twelfth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10,
                                   final String key11,
                                   final Supplier<? extends JsValue> fut11,
                                   final String key12,
                                   final Supplier<? extends JsValue> fut12
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10),
                                 requireNonNull(key11),
                                 requireNonNull(fut11),
                                 requireNonNull(key12),
                                 requireNonNull(fut12)
        );

    }

    /**
     static factory method to create a JsObjSupplier of thirteen mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param fut11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param fut12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param fut13 the mapping associated to the thirteenth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10,
                                   final String key11,
                                   final Supplier<? extends JsValue> fut11,
                                   final String key12,
                                   final Supplier<? extends JsValue> fut12,
                                   final String key13,
                                   final Supplier<? extends JsValue> fut13
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10),
                                 requireNonNull(key11),
                                 requireNonNull(fut11),
                                 requireNonNull(key12),
                                 requireNonNull(fut12),
                                 requireNonNull(key13),
                                 requireNonNull(fut13)

        );

    }

    /**
     static factory method to create a JsObjSupplier of fourteen mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param fut11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param fut12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param fut13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param fut14 the mapping associated to the fourteenth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10,
                                   final String key11,
                                   final Supplier<? extends JsValue> fut11,
                                   final String key12,
                                   final Supplier<? extends JsValue> fut12,
                                   final String key13,
                                   final Supplier<? extends JsValue> fut13,
                                   final String key14,
                                   final Supplier<? extends JsValue> fut14
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10),
                                 requireNonNull(key11),
                                 requireNonNull(fut11),
                                 requireNonNull(key12),
                                 requireNonNull(fut12),
                                 requireNonNull(key13),
                                 requireNonNull(fut13),
                                 requireNonNull(key14),
                                 requireNonNull(fut14)

        );

    }

    /**
     static factory method to create a JsObjSupplier of fifteen mappings

     @param key1  the first key
     @param fut1  the mapping associated to the first key
     @param key2  the second key
     @param fut2  the mapping associated to the second key
     @param key3  the third key
     @param fut3  the mapping associated to the third key
     @param key4  the fourth key
     @param fut4  the mapping associated to the fourth key
     @param key5  the fifth key
     @param fut5  the mapping associated to the fifth key
     @param key6  the sixth key
     @param fut6  the mapping associated to the sixth key
     @param key7  the seventh key
     @param fut7  the mapping associated to the seventh key
     @param key8  the eighth key
     @param fut8  the mapping associated to the eighth key
     @param key9  the ninth key
     @param fut9  the mapping associated to the ninth key
     @param key10 the tenth key
     @param fut10 the mapping associated to the eleventh key
     @param key11 the eleventh key
     @param fut11 the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param fut12 the mapping associated to the twelfth key,
     @param key13 the thirteenth key
     @param fut13 the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param fut14 the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param fut15 the mapping associated to the fifteenth key
     @return a JsObjSupplier
     */
    public static JsObjSupplier of(final String key1,
                                   final Supplier<? extends JsValue> fut1,
                                   final String key2,
                                   final Supplier<? extends JsValue> fut2,
                                   final String key3,
                                   final Supplier<? extends JsValue> fut3,
                                   final String key4,
                                   final Supplier<? extends JsValue> fut4,
                                   final String key5,
                                   final Supplier<? extends JsValue> fut5,
                                   final String key6,
                                   final Supplier<? extends JsValue> fut6,
                                   final String key7,
                                   final Supplier<? extends JsValue> fut7,
                                   final String key8,
                                   final Supplier<? extends JsValue> fut8,
                                   final String key9,
                                   final Supplier<? extends JsValue> fut9,
                                   final String key10,
                                   final Supplier<? extends JsValue> fut10,
                                   final String key11,
                                   final Supplier<? extends JsValue> fut11,
                                   final String key12,
                                   final Supplier<? extends JsValue> fut12,
                                   final String key13,
                                   final Supplier<? extends JsValue> fut13,
                                   final String key14,
                                   final Supplier<? extends JsValue> fut14,
                                   final String key15,
                                   final Supplier<? extends JsValue> fut15
                                  ) {
        return new JsObjSupplier(requireNonNull(key1),
                                 requireNonNull(fut1),
                                 requireNonNull(key2),
                                 requireNonNull(fut2),
                                 requireNonNull(key3),
                                 requireNonNull(fut3),
                                 requireNonNull(key4),
                                 requireNonNull(fut4),
                                 requireNonNull(key5),
                                 requireNonNull(fut5),
                                 requireNonNull(key6),
                                 requireNonNull(fut6),
                                 requireNonNull(key7),
                                 requireNonNull(fut7),
                                 requireNonNull(key8),
                                 requireNonNull(fut8),
                                 requireNonNull(key9),
                                 requireNonNull(fut9),
                                 requireNonNull(key10),
                                 requireNonNull(fut10),
                                 requireNonNull(key11),
                                 requireNonNull(fut11),
                                 requireNonNull(key12),
                                 requireNonNull(fut12),
                                 requireNonNull(key13),
                                 requireNonNull(fut13),
                                 requireNonNull(key14),
                                 requireNonNull(fut14),
                                 requireNonNull(key15),
                                 requireNonNull(fut15)

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
     returns a JsObjSupplier from the list of given bindings

     @param head the first key,future pair
     @param tail the rest of the key,future  pairs
     @return a new JsObjSupplier
     */
    @SafeVarargs
    public static JsObjSupplier of(final Tuple2<String, Supplier<? extends JsValue>> head,
                                   final Tuple2<String, Supplier<? extends JsValue>>... tail
                                  ) {
        requireNonNull(head);
        requireNonNull(tail);

        JsObjSupplier fut = new JsObjSupplier(head._1,
                                              head._2
        );
        for (final Tuple2<String, Supplier<? extends JsValue>> other : tail)
            fut = fut.put(other._1,
                          other._2
                         );
        return fut;
    }

    /**
     returns a new object future inserting the given future at the given key

     @param key    the given key
     @param future the given future
     @return a new JsObjFuture
     */
    public JsObjSupplier put(final String key,
                             final Supplier<? extends JsValue> future
                            ) {
        final Map<String, Supplier<? extends JsValue>> a = bindings.put(requireNonNull(key),
                                                                        requireNonNull(future)
                                                                       );
        return new JsObjSupplier(a);
    }
}
