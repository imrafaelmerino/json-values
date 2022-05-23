package jsonvalues.gen;


import fun.gen.BigIntGen;
import fun.gen.DoubleGen;
import fun.gen.Gen;
import jsonvalues.JsDouble;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsDouble generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or passing a double {@link DoubleGen generator}
 * to the constructor. Arbitrary generators generate uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 * </pre>
 *
 */
public final class JsDoubleGen implements Gen<JsDouble> {
    private static final Gen<JsDouble> biased = new JsDoubleGen(DoubleGen.biased());
    private static final Gen<JsDouble> arbitrary = new JsDoubleGen(DoubleGen.arbitrary());
    private final Gen<Double> gen;

    /**
     * Returns a JsDouble generator from a double generator
     * @param gen the double generator
     */
    public JsDoubleGen(Gen<Double> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * @return
     */
    public static Gen<JsDouble> biased() {
        return biased;
    }

    /**
     * @return
     */
    public static Gen<JsDouble> arbitrary() {
        return arbitrary;
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsDouble> biased(double min,
                                       double max) {
        return new JsDoubleGen(DoubleGen.biased(min,
                                                max));
    }


    /**
     * @param min
     * @param max
     * @return
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
