package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.InstantGen;
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsInstant generator. It can be created using the static factory methods
 * {@link #biased()} and {@link #arbitrary()} or, if none of the previous suits your
 * needs, from an instant generator and the function map:
 *
 * <pre>{@code
 * import fun.gen.Gen;
 * import jsonvalues.JsInstant;
 *
 * Gen<Instant> instantGen = seed -> () -> {...};
 * Gen<JsInstant> jsInstantGen = gen.map(JsInstant::of);
 * }</pre>
 *
 * Arbitrary generators produce values with a uniform distribution.
 * Biased generators produce potential problematic values with a higher probability, which can help
 * identify and test edge cases.
 */
public final class JsInstantGen implements Gen<JsInstant> {
    private static final Gen<JsInstant> biased = new JsInstantGen(InstantGen.biased());
    private static final Gen<JsInstant> arbitrary = new JsInstantGen(InstantGen.arbitrary());
    private final Gen<Instant> gen;


    private JsInstantGen(final Gen<Instant> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - The minimum instant value
     * - The maximum instant value
     *
     * @return A biased JsInstant generator.
     */
    public static Gen<JsInstant> biased() {
        return biased;
    }
    /**
     * Returns a generator that produces values with a uniform distribution.
     *
     * @return A JsInstant generator.
     */
    public static Gen<JsInstant> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a generator that produces values uniformly distributed over a specified time range.
     *
     * @param min The lower bound of the time range (inclusive).
     * @param max The upper bound of the time range (inclusive).
     * @return A JsInstant generator.
     */
    public static Gen<JsInstant> arbitrary(long min,
                                           long max) {
        return new JsInstantGen(InstantGen.arbitrary(min,
                max));
    }
    /**
     * Returns a generator that produces values uniformly distributed over a specified time range.
     *
     * @param min The lower bound of the time range (inclusive).
     * @param max The upper bound of the time range (inclusive).
     * @return A JsInstant generator.
     */
    public static Gen<JsInstant> arbitrary(final ZonedDateTime min,
                                           final ZonedDateTime max) {
        return new JsInstantGen(InstantGen.arbitrary(min,
                max));
    }
    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - The lower bound of the time range
     * - The upper bound of the time range
     *
     * @param min The lower bound of the time range (inclusive).
     * @param max The upper bound of the time range (inclusive).
     * @return A biased JsInstant generator.
     */
    public static Gen<JsInstant> biased(long min,
                                        long max) {
        return new JsInstantGen(InstantGen.biased(min,
                max));
    }
    /**
     * Returns a biased generator that produces potential problematic values with a higher probability.
     * These values include:
     * - The lower bound of the time range
     * - The upper bound of the time range
     *
     * @param min The lower bound of the time range (inclusive).
     * @param max The upper bound of the time range (inclusive).
     * @return A biased JsInstant generator.
     */
    public static Gen<JsInstant> biased(final ZonedDateTime min,
                                        final ZonedDateTime max) {
        return new JsInstantGen(InstantGen.biased(min, max));
    }


    @Override
    public Supplier<JsInstant> apply(final RandomGenerator seed) {
        return gen.map(JsInstant::of)
                .apply(requireNonNull(seed));
    }
}
