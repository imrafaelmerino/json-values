package jsonvalues.gen;

import fun.gen.BigDecGen;
import fun.gen.Gen;
import jsonvalues.JsBigDec;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsBigDecGen implements Gen<JsBigDec> {

    private static final Gen<JsBigDec> biased = new JsBigDecGen(BigDecGen.biased());
    private static final Gen<JsBigDec> arbitrary = new JsBigDecGen(BigDecGen.arbitrary());
    private final Gen<BigDecimal> gen;

    /**
     *
     * @param gen
     */
    public JsBigDecGen(Gen<BigDecimal> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     *
     * @return
     */
    public static Gen<JsBigDec> biased() {
        return biased;
    }

    /**
     *
     * @return
     */
    public static Gen<JsBigDec> arbitrary() {
        return arbitrary;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsBigDec> arbitrary(final BigDecimal min,
                                          final BigDecimal max) {
        return new JsBigDecGen(BigDecGen.arbitrary(min,
                                                   max));
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsBigDec> biased(final BigDecimal min,
                                       final BigDecimal max) {
        return new JsBigDecGen(BigDecGen.biased(min,
                                                max));
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsBigDec> biased(final long min,
                                       final long max) {
        return biased(BigDecimal.valueOf(min),
                      BigDecimal.valueOf(max));
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsBigDec> apply(Random seed) {
        return gen.map(JsBigDec::of).apply(seed);
    }

}
