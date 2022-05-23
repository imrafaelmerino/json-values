package jsonvalues.gen;

import fun.gen.*;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


/**
 *
 * Represents a JsObj generator. It can be created using the static factory methods
 * <code>of</code> or inserting to an existing generator new key-value pairs with the method
 * {@link JsObjGen#set(String, Gen)}. Each element of the Json is generated with a new
 * seed that is calculated passing the original seed to the {@link SplitGen#DEFAULT default split generator }
 *
 * There are factory methods to create up to 20-key generators. For bigger Json you
 * can use the {@link JsObjGen#set(String, Gen) set} method.
 *
 * Optional and nullable keys are specified with the
 * methods <code>setOptionals</code> and <code>setNullable</code>.
 *
 * Given the following optional fields a,b and c, all the possible permutations
 * (2^n = 8) are generated with the same probability:
 * <pre>
 *  - a, b and c missing
 *  - a and b missing
 *  - a and c missing
 *  - b and c missing
 *  - a missing
 *  - b missing
 *  - c missing
 *  - none of the missing
 * </pre>
 *
 * The same applies for nullable fields.
 *
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
    public static JsObjGen of(){
        Map<String, Gen<? extends JsValue>> map = new HashMap<>();
        return  new JsObjGen(map);
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
    /**
     * Returns a supplier from the specified seed that generates a new JsObj each time it's called
     * @param seed the generator seed
     * @return a JsObj supplier
     */
    @Override
    public Supplier<JsObj> apply(final Random seed) {
        requireNonNull(seed);
        Supplier<List<String>> optionalCombinations =
                Combinators.permutations(optionals)
                           .apply(SplitGen.DEFAULT.apply(seed));

        Supplier<Boolean> isRemoveOptionals =
                optionals.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(SplitGen.DEFAULT.apply(seed));

        Supplier<List<String>> nullableCombinations =
                Combinators.permutations(nullables)
                           .apply(SplitGen.DEFAULT.apply(seed));

        Supplier<Boolean> isRemoveNullables =
                nullables.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(SplitGen.DEFAULT.apply(seed));
        return () ->
        {
            JsObj obj = JsObj.empty();
            for (Map.Entry<String, Gen<? extends JsValue>> pair : bindings.entrySet()) {
                final JsValue value = pair.getValue().apply(seed)
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
