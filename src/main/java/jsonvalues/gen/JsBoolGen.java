package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
import jsonvalues.JsBool;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsBoolGen generator. It can be created using the static factory method
 * arbitrary or from a boolean generator using the constructor.
 */
public final class JsBoolGen implements Gen<JsBool> {

    private static final Gen<JsBool> arbitrary = new JsBoolGen(BoolGen.arbitrary);
    private final Gen<Boolean> gen;

    /**
     * @param gen the boolean generator
     */
    public JsBoolGen(final Gen<Boolean> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * @return
     */
    public static Gen<JsBool> arbitrary() {
        return arbitrary;
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsBool each time it's called
     * @param seed the generator seed
     * @return a JsBool supplier
     */
    @Override
    public Supplier<JsBool> apply(final Random seed) {
        return gen.map(JsBool::of)
                  .apply(requireNonNull(seed));
    }
}

