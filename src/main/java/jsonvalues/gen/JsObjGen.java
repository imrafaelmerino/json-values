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
        return JsObjGen.of(key,
                           gen).set(key1,
                                    gen1);

    }

    @SuppressWarnings("squid:S00107")
    public static JsObjGen of(final String key,
                              final Gen<? extends JsValue> gen,
                              final String key1,
                              final Gen<? extends JsValue> gen1,
                              final String key2,
                              final Gen<? extends JsValue> gen2
    ) {
        return JsObjGen.of(key,
                           gen,
                           key1,
                           gen1).set(key2,
                                     gen2);
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
        return JsObjGen.of(key,
                           gen,
                           key1,
                           gen1,
                           key2,
                           gen2).set(key3,
                                     gen3);
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
        return JsObjGen.of(key,
                           gen,
                           key1,
                           gen1,
                           key2,
                           gen2,
                           key3,
                           gen3).set(key4,
                                     gen4);
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
        return JsObjGen.of(key,
                           gen,
                           key1,
                           gen1,
                           key2,
                           gen2,
                           key3,
                           gen3,
                           key4,
                           gen4).set(key5,
                                     gen5);
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
        return JsObjGen.of(key,
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
                           gen5).set(key6,
                                     gen6);
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
        return JsObjGen.of(key,
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
                           gen6).set(key7,
                                     gen7);
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
        return JsObjGen.of(key,
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
                           gen7).set(key8,
                                     gen8);
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
        return JsObjGen.of(key1,
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
                           gen9).set(key10,
                                     gen10);
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
        return JsObjGen.of(key1,
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
                           gen10).set(key11,
                                      gen11);
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
        return JsObjGen.of(key1,
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
                           gen11).set(key12,
                                      gen12);
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
        return JsObjGen.of(key1,
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
                           gen12).set(key13,
                                      gen13);
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
        return JsObjGen.of(key1,
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
                           gen13).set(key14,
                                      gen14);
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
        return JsObjGen.of(key1,
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
                           gen14).set(key15,
                                      gen15);
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
        return JsObjGen.of(key1,
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
                           gen15).set(key16,
                                      gen16);
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
        return JsObjGen.of(key1,
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
                           gen15,
                           key16,
                           gen16).set(key17,
                                      gen17);
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
        return JsObjGen.of(key1,
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
                           gen15,
                           key16,
                           gen16,
                           key17,
                           gen17).set(key18,
                                      gen18);
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
        return JsObjGen.of(key1,
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
                           gen15,
                           key16,
                           gen16,
                           key17,
                           gen17,
                           key18,
                           gen18).set(key19,
                                      gen19);
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
        return JsObjGen.of(key1,
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
                           gen15,
                           key16,
                           gen16,
                           key17,
                           gen17,
                           key18,
                           gen18,
                           key19,
                           gen19).set(key20,
                                      gen20);
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
        map.put(requireNonNull(key),
                requireNonNull(gen));
        return new JsObjGen(map,
                            optionals,
                            nullables);

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
