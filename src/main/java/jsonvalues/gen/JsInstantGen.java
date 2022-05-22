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
 * biased  and arbitrary  or from an isntant generator using the constructor.
 */
public final class JsInstantGen implements Gen<JsInstant> {
    private static final Gen<JsInstant> biased = new JsInstantGen(InstantGen.biased());
    private static final Gen<JsInstant> arbitrary = new JsInstantGen(InstantGen.arbitrary());
    private final Gen<Instant> gen;

    public JsInstantGen(final Gen<Instant> gen) {
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

    public static Gen<JsInstant> arbitrary(ZonedDateTime min,
                                           ZonedDateTime max) {
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
     * @param seed the generator seed
     * @return a JsInstant supplier
     */
    @Override
    public Supplier<JsInstant> apply(final Random seed) {
        return gen.map(JsInstant::of)
                  .apply(requireNonNull(seed));
    }
}
