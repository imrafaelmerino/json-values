package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.LongGen;
import jsonvalues.JsLong;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsInstant generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a long generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsLong;
 *
 *      Gen<Long> longGen = seed -> () -> {...};
 *      Gen<JsLong> jsLongGen = gen.map(JsLong::of)
 *      }
 *  </pre>
 *  <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsLongGen implements Gen<JsLong> {
    private static final Gen<JsLong> biased = new JsLongGen(LongGen.biased());
    private static final Gen<JsLong> arbitrary = new JsLongGen(LongGen.arbitrary());
    private final Gen<Long> gen;

    /**
     * Creates a JsLong generator from a specified long generator
     *
     * @param gen the long generator
     */
    private JsLongGen(final Gen<Long> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     *
     * @return a biased JsBigDec generator
     */
    public static Gen<JsLong> biased() {
        return biased;
    }

    /**
     * Returns a generator that produces values uniformly distributed
     * @return a JsLong generator
     */
    public static Gen<JsLong> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval
     *
     * @param min lower bound of the interval (inclusive)
     * @param max upper bound of the interval (inclusive)
     *
     * @return a biased JsLong generator
     */
    public static Gen<JsLong> arbitrary(long min,
                                        long max) {
        return new JsLongGen(LongGen.arbitrary(min,
                                               max));
    }

    /**
     * returns a biased generators that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - the lower bound of the interval
     * - the upper bound of the interval
     * </pre>
     *
     * and the following numbers provided that they are between the specified interval:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * - {@link Long#MAX_VALUE}
     * </pre>
     *
     * @param min lower bound of the interval (inclusive)
     * @param max upper bound of the interval (inclusive)
     *
     *
     * @return a biased JsLong generator
     */
    public static Gen<JsLong> biased(final long min,
                                     final long max) {
        return new JsLongGen(LongGen.biased(min,
                                            max));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsLong each time it's called
     * @param seed the generator seed
     * @return a JsLong supplier
     */
    @Override
    public Supplier<JsLong> apply(Random seed) {
        return gen.map(JsLong::of).apply(requireNonNull(seed));
    }
}
