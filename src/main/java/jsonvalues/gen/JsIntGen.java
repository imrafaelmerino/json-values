package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.IntGen;
import jsonvalues.JsInt;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsInt generator. It can be created using the static factory methods
 * biased  and arbitrary  or from an integer generator using the constructor.
 */
public final class JsIntGen implements Gen<JsInt> {
    private static final Gen<JsInt> biased = new JsIntGen(IntGen.biased());
    private static final Gen<JsInt> arbitrary = new JsIntGen(IntGen.arbitrary());
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
