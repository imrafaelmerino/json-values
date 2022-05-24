package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsInt;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsInstant generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from an integer generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsInt;
 *
 *      Gen<Integer> intGen = seed -> () -> {...};
 *      Gen<JsInteger> jsIntGen = gen.map(JsInt::of)
 *      }
 *  </pre>
 *  <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsIntGen implements Gen<JsInt> {
    private static final Gen<JsInt> biased = new JsIntGen(IntGen.biased());
    private static final Gen<JsInt> arbitrary = new JsIntGen(IntGen.arbitrary());
    private final Gen<Integer> gen;

    /**
     * Creates a JsInt generator from a specified integer generator
     *
     * @param gen the integer generator
     */
    private JsIntGen(Gen<Integer> gen) {
        this.gen = Objects.requireNonNull(gen);
    }

    /**
     * returns a biased generators that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     *
     * @return a biased JsInt generator
     */
    public static Gen<JsInt> biased() {
        return biased;
    }

    /**
     * Returns a generator that produces values uniformly distributed
     * @return a JsInt generator
     */
    public static Gen<JsInt> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval
     *
     * @param min lower bound of the interval (inclusive)
     * @param max upper bound of the interval (inclusive)
     *
     * @return a biased JsInt generator
     */
    public static Gen<JsInt> arbitrary(int min,
                                       int max) {
        return new JsIntGen(IntGen.arbitrary(min,
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
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - 0
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     * @param min lower bound of the interval (inclusive)
     * @param max upper bound of the interval (inclusive)
     *
     * @return a biased JsInt generator
     */
    public static Gen<JsInt> biased(int min,
                                    int max) {
        return new JsIntGen(IntGen.biased(min,
                                          max));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsInt each time it's called
     * @param seed the generator seed
     * @return a JsInt supplier
     */
    @Override
    public Supplier<JsInt> apply(final Random seed) {
        return gen.map(JsInt::of)
                  .apply(requireNonNull(seed));
    }
}
