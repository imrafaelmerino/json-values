package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.InstantGen;
import jsonvalues.JsInstant;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsInstantGen implements Gen<JsInstant> {
    private static final Gen<JsInstant> biased = new JsInstantGen(InstantGen.biased);
    private static final Gen<JsInstant> arbitrary = new JsInstantGen(InstantGen.arbitrary);
    private final Gen<Instant> gen;

    public JsInstantGen(Gen<Instant> gen) {
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


    @Override
    public Supplier<JsInstant> apply(Random seed) {
        return gen.map(JsInstant::of)
                  .apply(seed);
    }
}
