package jsonvalues.gen;

import fun.gen.BigDecGen;
import fun.gen.Gen;
import jsonvalues.JsBigDec;

import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsBigDec generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a decimal generator and the function map:
 *
 * <pre>{@code
 *     import fun.gen.Gen;
 *     import jsonvalues.JsBigDec;
 *
 *     Gen<BigDecimal> decGen = seed -> () -> {...};
 *     Gen<JsBigDec> jsDecGen = gen.map(JsBigDec::of)
 * }
 * </pre>
 * <p>
 * Arbitrary generators produce uniformly distributed values.
 * Biased generators produce, with higher probability, potential problematic values that
 * usually cause more bugs.
 */
public final class JsBigDecGen implements Gen<JsBigDec> {

    private static final Gen<JsBigDec> biased = new JsBigDecGen(BigDecGen.biased());
    private static final Gen<JsBigDec> arbitrary = new JsBigDecGen(BigDecGen.arbitrary());
    private final Gen<BigDecimal> gen;


    private JsBigDecGen(final Gen<BigDecimal> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - {@link BigDecimal#ZERO}
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     * @return A biased JsBigDec generator.
     */
    public static Gen<JsBigDec> biased() {
        return biased;
    }


    /**
     * Returns a generator that produces values uniformly distributed.
     *
     * @return A JsBigDec generator.
     */
    public static Gen<JsBigDec> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval.
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsBigDec generator.
     */
    public static Gen<JsBigDec> arbitrary(final BigDecimal min,
                                          final BigDecimal max) {
        return new JsBigDecGen(BigDecGen.arbitrary(min,
                                                   max));
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified interval.
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsBigDec generator.
     */
    public static Gen<JsBigDec> arbitrary(final long min,
                                          final long max) {
         return arbitrary(BigDecimal.valueOf(min),
                          BigDecimal.valueOf(max));
    }

    /**
     * Returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - The lower bound of the interval
     * - The upper bound of the interval
     * </pre>
     *
     * and the following numbers provided that they are between the specified interval:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - {@link BigDecimal#ZERO}
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsBigDec generator.
     */
    public static Gen<JsBigDec> biased(final BigDecimal min,
                                       final BigDecimal max) {
        return new JsBigDecGen(BigDecGen.biased(min,
                                                max));
    }

    /**
     * Returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     *
     * <pre>
     * - The lower bound of the interval
     * - The upper bound of the interval
     * </pre>
     *
     * and the following numbers provided that they are between the specified interval:
     *
     * <pre>
     * - {@link Long#MIN_VALUE}
     * - {@link Integer#MIN_VALUE}
     * - {@link Short#MIN_VALUE}
     * - {@link Byte#MIN_VALUE}
     * - {@link BigDecimal#ZERO}
     * - {@link Long#MAX_VALUE}
     * - {@link Integer#MAX_VALUE}
     * - {@link Short#MAX_VALUE}
     * - {@link Byte#MAX_VALUE}
     * </pre>
     *
     * @param min The lower bound of the interval (inclusive).
     * @param max The upper bound of the interval (inclusive).
     * @return A biased JsBigDec generator.
     */
    public static Gen<JsBigDec> biased(final long min,
                                       final long max) {
        return biased(BigDecimal.valueOf(min),
                      BigDecimal.valueOf(max));
    }


    @Override
    public Supplier<JsBigDec> apply(final RandomGenerator seed) {
        return gen.map(JsBigDec::of).apply(requireNonNull(seed));
    }

}
