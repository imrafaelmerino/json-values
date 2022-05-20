package jsonvalues.gen;

import fun.gen.BoolGen;
import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.SplitGen;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


/**
 * represent a generator of Json objects.
 */
public final class JsObjGen implements Gen<JsObj> {

    private final Map<String, Gen<? extends JsValue>> bindings;

    private final List<String> optionals;
    private final List<String> nullables;

    private JsObjGen(Map<String, Gen<? extends JsValue>> bindings,
                     List<String> optionals,
                     List<String> nullables) {
        this.optionals = optionals;
        this.nullables = nullables;
        this.bindings = bindings;
    }

    private JsObjGen(Map<String, Gen<? extends JsValue>> bindings) {
        this.optionals = new ArrayList<>();
        this.nullables = new ArrayList<>();
        this.bindings = bindings;
    }


    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen
    ) {

        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        return new JsObjGen(map);

    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));

        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));

        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));

        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));

        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));

        return new JsObjGen(map);
    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));

        return new JsObjGen(map);

    }

    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key),
                requireNonNull(gen));
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of ten mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of eleven mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of twelve mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of thirteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of fourteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of fifteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of sixteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15,
                              final String key16,
                              final Gen<? extends JsValue> gen16
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        map.put(requireNonNull(key16),
                requireNonNull(gen16));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of seventeen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15,
                              final String key16,
                              final Gen<? extends JsValue> gen16,
                              final String key17,
                              final Gen<? extends JsValue> gen17
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        map.put(requireNonNull(key16),
                requireNonNull(gen16));
        map.put(requireNonNull(key17),
                requireNonNull(gen17));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of eighteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15,
                              final String key16,
                              final Gen<? extends JsValue> gen16,
                              final String key17,
                              final Gen<? extends JsValue> gen17,
                              final String key18,
                              final Gen<? extends JsValue> gen18
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        map.put(requireNonNull(key16),
                requireNonNull(gen16));
        map.put(requireNonNull(key17),
                requireNonNull(gen17));
        map.put(requireNonNull(key18),
                requireNonNull(gen18));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of nineteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @param key19 the nineteenth key
     * @param gen19 the mapping associated to the nineteenth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15,
                              final String key16,
                              final Gen<? extends JsValue> gen16,
                              final String key17,
                              final Gen<? extends JsValue> gen17,
                              final String key18,
                              final Gen<? extends JsValue> gen18,
                              final String key19,
                              final Gen<? extends JsValue> gen19
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        map.put(requireNonNull(key16),
                requireNonNull(gen16));
        map.put(requireNonNull(key17),
                requireNonNull(gen17));
        map.put(requireNonNull(key18),
                requireNonNull(gen18));
        map.put(requireNonNull(key19),
                requireNonNull(gen19));

        return new JsObjGen(map);
    }

    /**
     * static factory method to create a JsObGen of twenty mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @param key19 the nineteenth key
     * @param gen19 the mapping associated to the nineteenth key
     * @param key20 the twentieth key
     * @param gen20 the mapping associated to the twentieth key
     * @return a JsObjGen
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2,
                              final String key3,
                              final Gen<? extends JsValue> gen3,
                              final String key4,
                              final Gen<? extends JsValue> gen4,
                              final String key5,
                              final Gen<? extends JsValue> gen5,
                              final String key6,
                              final Gen<? extends JsValue> gen6,
                              final String key7,
                              final Gen<? extends JsValue> gen7,
                              final String key8,
                              final Gen<? extends JsValue> gen8,
                              final String key9,
                              final Gen<? extends JsValue> gen9,
                              final String key10,
                              final Gen<? extends JsValue> gen10,
                              final String key11,
                              final Gen<? extends JsValue> gen11,
                              final String key12,
                              final Gen<? extends JsValue> gen12,
                              final String key13,
                              final Gen<? extends JsValue> gen13,
                              final String key14,
                              final Gen<? extends JsValue> gen14,
                              final String key15,
                              final Gen<? extends JsValue> gen15,
                              final String key16,
                              final Gen<? extends JsValue> gen16,
                              final String key17,
                              final Gen<? extends JsValue> gen17,
                              final String key18,
                              final Gen<? extends JsValue> gen18,
                              final String key19,
                              final Gen<? extends JsValue> gen19,
                              final String key20,
                              final Gen<? extends JsValue> gen20
    ) {
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        map.put(requireNonNull(key1),
                requireNonNull(gen1));
        map.put(requireNonNull(key2),
                requireNonNull(gen2));
        map.put(requireNonNull(key3),
                requireNonNull(gen3));
        map.put(requireNonNull(key4),
                requireNonNull(gen4));
        map.put(requireNonNull(key5),
                requireNonNull(gen5));
        map.put(requireNonNull(key6),
                requireNonNull(gen6));
        map.put(requireNonNull(key7),
                requireNonNull(gen7));
        map.put(requireNonNull(key8),
                requireNonNull(gen8));
        map.put(requireNonNull(key9),
                requireNonNull(gen9));
        map.put(requireNonNull(key10),
                requireNonNull(gen10));
        map.put(requireNonNull(key11),
                requireNonNull(gen11));
        map.put(requireNonNull(key12),
                requireNonNull(gen12));
        map.put(requireNonNull(key13),
                requireNonNull(gen13));
        map.put(requireNonNull(key14),
                requireNonNull(gen14));
        map.put(requireNonNull(key15),
                requireNonNull(gen15));
        map.put(requireNonNull(key16),
                requireNonNull(gen16));
        map.put(requireNonNull(key17),
                requireNonNull(gen17));
        map.put(requireNonNull(key18),
                requireNonNull(gen18));
        map.put(requireNonNull(key19),
                requireNonNull(gen19));
        map.put(requireNonNull(key20),
                requireNonNull(gen20));

        return new JsObjGen(map);
    }

    public JsObjGen setNullables(final List<String> nullables) {
        return new JsObjGen(bindings,
                            optionals,
                            requireNonNull(nullables)
                            );
    }

    public JsObjGen setNullables(final String... nullables) {
        return setNullables(Arrays.stream(requireNonNull(nullables))
                                  .collect(Collectors.toList()));
    }

    /**
     * @param optionals
     * @return
     */
    public JsObjGen setOptionals(final List<String> optionals) {
        return new JsObjGen(bindings,
                            requireNonNull(optionals),
                            nullables);
    }

    /**
     * @param optional
     * @return
     */
    public JsObjGen setOptionals(final String... optional) {
        return setOptionals(Arrays.stream(requireNonNull(optional))
                                  .collect(Collectors.toList()));
    }

    /**
     * @param key
     * @param gen
     * @return
     */
    public JsObjGen set(final String key,
                        final Gen<? extends JsValue> gen
    ) {
        LinkedHashMap<String, Gen<? extends JsValue>> map = new LinkedHashMap<>(bindings);
        map.put(requireNonNull(key),requireNonNull(gen));
        return new JsObjGen(map,optionals,nullables);

    }

    @Override
    public Supplier<JsObj> apply(final Random gen) {
        Supplier<List<String>> optionalCombinations =
                Combinators.permutations(optionals)
                           .apply(SplitGen.DEFAULT.apply(gen));

        Supplier<Boolean> isRemoveOptionals =
                optionals.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(SplitGen.DEFAULT.apply(gen));

        Supplier<List<String>> nullableCombinations =
                Combinators.permutations(nullables)
                           .apply(SplitGen.DEFAULT.apply(gen));

        Supplier<Boolean> isRemoveNullables =
                nullables.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(SplitGen.DEFAULT.apply(gen));
        return () ->
        {
            JsObj obj = JsObj.empty();
            for (Map.Entry<String, Gen<? extends JsValue>> pair : bindings.entrySet()) {
                final JsValue value = pair.getValue().apply(gen)
                                          .get();
                obj = obj.set(pair.getKey(),
                              value
                );
            }
            if (Boolean.TRUE.equals(isRemoveOptionals.get())) {
                final List<String> r = optionalCombinations.get();
                for (String s : r) obj = obj.delete(s);
            }
            if (Boolean.TRUE.equals(isRemoveNullables.get())) {
                final List<String> r = nullableCombinations.get();
                for (String s : r)
                    obj = obj.set(s,
                                  JsNull.NULL);
            }
            return obj;
        };
    }

}
