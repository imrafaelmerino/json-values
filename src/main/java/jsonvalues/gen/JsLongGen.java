package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.LongGen;
import jsonvalues.JsLong;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsLongGen implements Gen<JsLong> {
    private static final Gen<JsLong> biased = new JsLongGen(LongGen.biased());
    private static final Gen<JsLong> arbitrary = new JsLongGen(LongGen.arbitrary());
    private final Gen<Long> gen;

    /**
     *
     * @param gen
     */
    public JsLongGen(Gen<Long> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     *
     * @return
     */
    public static Gen<JsLong> biased() {
        return biased;
    }

    /**
     *
     * @return
     */
    public static Gen<JsLong> arbitrary() {
        return arbitrary;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsLong> arbitrary(long min,
                                        long max) {
        return new JsLongGen(LongGen.arbitrary(min,
                                               max));
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsLong> biased(long min,
                                     long max) {
        return new JsLongGen(LongGen.biased(min,
                                            max));
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsLong> apply(Random seed) {
        return gen.map(JsLong::of).apply(seed);
    }
}
