package jsonvalues.gen;


import fun.gen.DoubleGen;
import fun.gen.Gen;
import jsonvalues.JsDouble;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsDoubleGen implements Gen<JsDouble> {
    private static final Gen<JsDouble> biased = new JsDoubleGen(DoubleGen.biased);
    private static final Gen<JsDouble> arbitrary = new JsDoubleGen(DoubleGen.arbitrary);
    private final Gen<Double> gen;

    /**
     * @param gen
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
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsDouble> apply(Random seed) {
        return gen.map(JsDouble::of).apply(seed);
    }

}
