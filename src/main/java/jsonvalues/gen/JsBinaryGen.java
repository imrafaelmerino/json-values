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
     * returns an arbitrary Json bytes generator
     * @param minLength minimum number of bytes (inclusive)
     * @param maxLength maximum number of bytes (inclusive)
     * @return an arbitrary Json bytes generator
     */
    public static Gen<JsBinary> arbitrary(int minLength,
                                          int maxLength) {
        return new JsBinaryGen(BytesGen.arbitrary(minLength,
                                                  maxLength));
    }

    /**
     * returns a biased Json bytes generator
     * @param minLength minimum number of bytes (inclusive)
     * @param maxLength maximum number of bytes (inclusive)
     * @return a biased Json bytes generator
     */
    public static Gen<JsBinary> biased(int minLength,
                                       int maxLength) {
        return new JsBinaryGen(BytesGen.biased(minLength,
                                               maxLength));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsBinary each time it's called
     * @param seed the generator seed
     * @return a JsBinary supplier
     */
    @Override
    public Supplier<JsBinary> apply(Random seed) {
        return gen.map(JsBinary::of)
                  .apply(requireNonNull(seed));
    }
}
