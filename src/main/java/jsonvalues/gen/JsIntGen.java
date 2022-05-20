package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsInt;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 *
 */
public final class JsIntGen implements Gen<JsInt> {
    private static final Gen<JsInt> biased = new JsIntGen(IntGen.biased);
    private static final Gen<JsInt> arbitrary = new JsIntGen(IntGen.arbitrary);
    private final Gen<Integer> gen;

    /**
     *
     * @param gen
     */
    public JsIntGen(Gen<Integer> gen) {
        this.gen = Objects.requireNonNull(gen);
    }

    /**
     *
     * @return
     */
    public static Gen<JsInt> biased() {
        return biased;
    }

    /**
     *
     * @return
     */
    public static Gen<JsInt> arbitrary() {
        return arbitrary;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsInt> arbitrary(int min,
                                       int max) {
        return new JsIntGen(IntGen.arbitrary(min,
                                             max));
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsInt> biased(int min,
                                    int max) {
        return new JsIntGen(IntGen.biased(min,
                                          max));
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsInt> apply(Random seed) {
        return gen.map(JsInt::of).apply(seed);
    }
}
