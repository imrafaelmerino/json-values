package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.LongGen;
import jsonvalues.JsLong;

import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsLong generator. It can be created using the static factory methods
 * {@link #biased()} and {@link #arbitrary()} or, if none of the previous suit your
 * needs, from a long generator and the function map:
 *
 * <pre>{@code
 * import fun.gen.Gen;
 * import jsonvalues.JsLong;
 *
 * Gen<Long> longGen = seed -> () -> {...};
 * Gen<JsLong> jsLongGen = gen.map(JsLong::of);
 * }</pre>
 *
 * Arbitrary generators produce values with a uniform distribution.
 * Biased generators produce potential problematic values with a higher probability, which can help
 * identify and test edge cases.
 */
public final class JsLongGen implements Gen<JsLong> {
    private static final Gen<JsLong> biased = new JsLongGen(LongGen.biased());
    private static final Gen<JsLong> arbitrary = new JsLongGen(LongGen.arbitrary());
    private final Gen<Long> gen;


    private JsLongGen(final Gen<Long> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     *
     * @return A biased JsLong generator.
     */
    public static Gen<JsLong> biased() {
        return biased;
    }
    /**
     * Returns a generator that produces values with a uniform distribution.
     *
     * @return A JsLong generator.
     */
    public static Gen<JsLong> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval.
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A JsLong generator.
     */
    public static Gen<JsLong> arbitrary(long min,
                                        long max) {
        return new JsLongGen(LongGen.arbitrary(min,
                                               max));
    }

    /**
     * Returns a generator that produces values uniformly distributed over the interval [min,Integer.MAX_VALUE].
     *
     * @param min The lower bound of the interval (inclusive).
     * @return A JsLong generator.
     */
    public static Gen<JsLong> arbitrary(long min) {
        return new JsLongGen(LongGen.arbitrary(min));
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - The lower bound of the interval
     * - The upper bound of the interval
     * And the following numbers provided that they are between the specified interval:
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * - {@link Long#MAX_VALUE}
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsLong generator.
     */
    public static Gen<JsLong> biased(final long min,
                                     final long max) {
        return new JsLongGen(LongGen.biased(min,
                                            max));
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - The lower bound of the interval
     * - The upper bound of the interval {@link Long#MAX_VALUE}
     * And the following numbers provided that they are between the specified interval:
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * - {@link Long#MAX_VALUE}
     *
     * @param min The lower bound of the interval (inclusive).
     * @return A biased JsLong generator.
     */
    public static Gen<JsLong> biased(final long min) {
        return new JsLongGen(LongGen.biased(min));
    }


    @Override
    public Supplier<JsLong> apply(final RandomGenerator seed) {
        return gen.map(JsLong::of).apply(requireNonNull(seed));
    }
}
