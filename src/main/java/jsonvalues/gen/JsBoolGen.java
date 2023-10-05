package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
import jsonvalues.JsBool;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsBool generator
 *
 */
public final class JsBoolGen implements Gen<JsBool> {

    private static final Gen<JsBool> arbitrary = new JsBoolGen(BoolGen.arbitrary());
    private final Gen<Boolean> gen;


    private JsBoolGen(final Gen<Boolean> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns a generator that produces values with a uniform distribution of JsBool.
     *
     * @return A JsBool generator.
     */
    public static Gen<JsBool> arbitrary() {
        return arbitrary;
    }


    @Override
    public Supplier<JsBool> apply(final Random seed) {
        return gen.map(JsBool::of)
                  .apply(requireNonNull(seed));
    }
}

