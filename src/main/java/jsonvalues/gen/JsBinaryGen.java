package jsonvalues.gen;


import fun.gen.BytesGen;
import fun.gen.Gen;
import jsonvalues.JsBinary;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 *
 */
public final class JsBinaryGen implements Gen<JsBinary> {
    private final Gen<byte[]> gen;

    /**
     *
     * @param gen
     */
    public JsBinaryGen(Gen<byte[]> gen) {
        this.gen = Objects.requireNonNull(gen);
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsBinary> arbitrary(int min,
                                          int max) {
        return new JsBinaryGen(BytesGen.arbitrary(min,
                                                  max));
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsBinary> biased(int min,
                                       int max) {
        return new JsBinaryGen(BytesGen.biased(min,
                                               max));
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsBinary> apply(Random seed) {
        return gen.map(JsBinary::of).apply(seed);
    }
}
