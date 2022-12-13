package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.InstantGen;
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsInstant generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a double generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsInstant;
 *
 *      Gen<Instant> instantGen = seed -> () -> {...};
 *      Gen<JsInstant> jsInstantGen = gen.map(JsInstant::of)
 *      }
 *  </pre>
 * <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 */
public final class JsInstantGen implements Gen<JsInstant> {
    private static final Gen<JsInstant> biased = new JsInstantGen(InstantGen.biased());
    private static final Gen<JsInstant> arbitrary = new JsInstantGen(InstantGen.arbitrary());
    private final Gen<Instant> gen;

    /**
     * Creates a JsInstant generator from a specified instant generator
     *
     * @param gen the instant generator
     */
    private JsInstantGen(final Gen<Instant> gen) {
        this.gen = requireNonNull(gen);
    }

    public static Gen<JsInstant> biased() {
        return biased;
    }

    public static Gen<JsInstant> arbitrary() {
        return arbitrary;
    }

    public static Gen<JsInstant> arbitrary(long min,
                                           long max) {
        return new JsInstantGen(InstantGen.arbitrary(min,
                                                     max));
    }

    public static Gen<JsInstant> arbitrary(final ZonedDateTime min,
                                           final ZonedDateTime max) {
        return new JsInstantGen(InstantGen.arbitrary(min,
                                                     max));
    }

    public static Gen<JsInstant> biased(long min,
                                        long max) {
        return new JsInstantGen(InstantGen.biased(min,
                                                  max));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsInstant each time it's called
     *
     * @param seed the generator seed
     * @return a JsInstant supplier
     */
    @Override
    public Supplier<JsInstant> apply(final Random seed) {
        return gen.map(JsInstant::of)
                  .apply(requireNonNull(seed));
    }
}
