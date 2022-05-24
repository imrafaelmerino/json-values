package jsonvalues.gen;


import fun.gen.DoubleGen;
import fun.gen.Gen;
import jsonvalues.JsDouble;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsDouble generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a double generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsDouble;
 *
 *      Gen<Double> doubleGen = seed -> () -> {...};
 *      Gen<JsDouble> jsDoubleGen = gen.map(JsDouble::of)
 *      }
 *  </pre>
 *  <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsDoubleGen implements Gen<JsDouble> {
    private static final Gen<JsDouble> biased = new JsDoubleGen(DoubleGen.biased());
    private static final Gen<JsDouble> arbitrary = new JsDoubleGen(DoubleGen.arbitrary());
    private final Gen<Double> gen;

    /**
     * Creates a JsDouble generator from a specified double generator
     *
     * @param gen the double generator
     */
    private JsDoubleGen(Gen<Double> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * returns a biased generators that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * -  0
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     *
     * @return a biased JsDouble generator
     */
    public static Gen<JsDouble> biased() {
        return biased;
    }

    /**
     * Returns a generator that produces values uniformly distributed
     * @return a JsDouble generator
     */
    public static Gen<JsDouble> arbitrary() {
        return arbitrary;
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
     * @return a biased JsDouble generator
     */
    public static Gen<JsDouble> biased(double min,
                                       double max) {
        return new JsDoubleGen(DoubleGen.biased(min,
                                                max));
    }


    /**
     * Returns a generator that produces values uniformly distributed over a specified interval
     *
     * @param min lower bound of the interval (inclusive)
     * @param max upper bound of the interval (inclusive)
     *
     * @return a biased JsDouble generator
     */
    public static Gen<JsDouble> arbitrary(double min,
                                          double max) {
        return new JsDoubleGen(DoubleGen.arbitrary(min,
                                                   max));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsDouble each time it's called
     * @param seed the generator seed
     * @return a JsDouble supplier
     */
    @Override
    public Supplier<JsDouble> apply(final Random seed) {
        return gen.map(JsDouble::of)
                  .apply(requireNonNull(seed));
    }

}
