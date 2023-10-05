package jsonvalues.gen;


import fun.gen.BytesGen;
import fun.gen.Gen;
import jsonvalues.JsBinary;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsBinary generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a bytes generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsBinary;
 *
 *      Gen<byte[]> byteGen = seed -> () -> {...};
 *      Gen<JsBinary> jsByteGen = gen.map(JsBinary::of)
 *      }
 *  </pre>
 *  <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsBinaryGen implements Gen<JsBinary> {
    private final Gen<byte[]> gen;

    /**
     * Creates a JsBinary generator from a specified array of bytes generator
     *
     * @param gen the array of bytes generator
     */
    private JsBinaryGen(final Gen<byte[]> gen) {
        this.gen = Objects.requireNonNull(gen);
    }

    /**
     * Returns an arbitrary JSON binary generator with a specified range of byte lengths.
     *
     * @param minLength The minimum number of bytes (inclusive).
     * @param maxLength The maximum number of bytes (inclusive).
     * @return An arbitrary JSON binary generator.
     */
    public static Gen<JsBinary> arbitrary(int minLength,
                                          int maxLength) {
        return new JsBinaryGen(BytesGen.arbitrary(minLength,
                                                  maxLength));
    }

    /**
     * Returns a biased JSON binary generator with a specified range of byte lengths.
     *
     * @param minLength The minimum number of bytes (inclusive).
     * @param maxLength The maximum number of bytes (inclusive).
     * @return A biased JSON binary generator.
     */
    public static Gen<JsBinary> biased(int minLength,
                                       int maxLength) {
        return new JsBinaryGen(BytesGen.biased(minLength,
                                               maxLength));
    }


    @Override
    public Supplier<JsBinary> apply(Random seed) {
        return gen.map(JsBinary::of)
                  .apply(requireNonNull(seed));
    }
}
