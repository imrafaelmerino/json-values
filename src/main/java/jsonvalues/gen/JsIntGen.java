package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsInt;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsInt generator. It can be created using the static factory methods {@link #biased()} and
 * {@link #arbitrary()} or, if none of the previous suit your needs, from an integer generator and the function map:
 *
 * <pre>{@code
 * import fun.gen.Gen;
 * import jsonvalues.JsInt;
 *
 * Gen<Integer> intGen = seed -> () -> {...};
 * Gen<JsInt> jsIntGen = gen.map(JsInt::of);
 * }</pre>
 * <p>
 * Arbitrary generators produce values with a uniform distribution. Biased generators produce potential problematic
 * values with a higher probability, which can help identify and test edge cases.
 */
public final class JsIntGen implements Gen<JsInt> {
    private static final Gen<JsInt> biased = new JsIntGen(IntGen.biased());
    private static final Gen<JsInt> arbitrary = new JsIntGen(IntGen.arbitrary());
    private final Gen<Integer> gen;


    private JsIntGen(Gen<Integer> gen) {
        this.gen = Objects.requireNonNull(gen);
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability. These values
     * include: - {@link Integer#MIN_VALUE} - {@link Short#MIN_VALUE} - {@link Byte#MIN_VALUE} - 0 -
     * {@link Integer#MAX_VALUE} - {@link Short#MAX_VALUE} - {@link Byte#MAX_VALUE}
     *
     * @return A biased JsInt generator.
     */
    public static Gen<JsInt> biased() {
        return biased;
    }

    /**
     * Returns a generator that produces values with a uniform distribution.
     *
     * @return A JsInt generator.
     */
    public static Gen<JsInt> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval.
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A JsInt generator.
     */
    public static Gen<JsInt> arbitrary(int min,
                                       int max
                                      ) {
        return new JsIntGen(IntGen.arbitrary(min,
                                             max));
    }

    /**
     * Returns a generator that produces values uniformly distributed over the interval [min,Integer.MAX_VALUE].
     *
     * @param min The lower bound of the interval (inclusive).
     * @return A JsInt generator.
     */
    public static Gen<JsInt> arbitrary(int min) {
        return new JsIntGen(IntGen.arbitrary(min));
    }


    /**
     * Returns a biased generator that produces potential problematic values with a higher probability. These values
     * include:
     * - The lower bound of the interval
     * - The upper bound of the interval
     * And the following numbers, provided that they are between the specified interval:
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsInt generator.
     */
    public static Gen<JsInt> biased(int min,
                                    int max
                                   ) {
        return new JsIntGen(IntGen.biased(min,
                                          max));
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability. These values
     * include:
     * - The lower bound of the interval
     * - The upper bound of the interval {@link Integer#MAX_VALUE}
     * And the following numbers, provided that they are between the specified interval:
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     *
     * @param min The lower bound of the interval (inclusive).
     * @return A biased JsInt generator.
     */
    public static Gen<JsInt> biased(int min) {
        return new JsIntGen(IntGen.biased(min));
    }


    @Override
    public Supplier<JsInt> apply(final Random seed) {
        return gen.map(JsInt::of)
                  .apply(requireNonNull(seed));
    }
}
