package jsonvalues.gen;


import fun.gen.BoolGen;
import fun.gen.Gen;
import jsonvalues.JsBool;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsBoolGen implements Gen<JsBool> {

    private static final Gen<JsBool> arbitrary = new JsBoolGen(BoolGen.arbitrary);
    private final Gen<Boolean> gen;

    /**
     *
     * @param gen
     */
    public JsBoolGen(Gen<Boolean> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     *
     * @return
     */
    public static Gen<JsBool> arbitrary() {
        return arbitrary;
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsBool> apply(Random seed) {
        return gen.map(JsBool::of).apply(seed);
    }
}

