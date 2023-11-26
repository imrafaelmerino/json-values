package jsonvalues.gen;

import fun.gen.BigIntGen;
import fun.gen.Gen;
import jsonvalues.JsBigInt;

import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

/**
 *
 * Represents a JsBigInt generator. It can be created using the static factory methods
 * <code>biased</code> and <code>arbitrary</code> or, if none of the previous suit your
 * needs, from a big integer generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsBigInt;
 *
 *      Gen<BigInteger> bigIntGen = seed -> () -> {...};
 *      Gen<JsBigInt> jsBigIntGen = gen.map(JsBigInt::of)
 *      }
 *  </pre>
 *  <p>
 * Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsBigIntGen implements Gen<JsBigInt> {
    private final Gen<BigInteger> gen;


    private JsBigIntGen(final Gen<BigInteger> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns an arbitrary JSON big integer generator with a specified number of bits.
     *
     * @param bits The number of bits for the generated big integer.
     * @return An arbitrary JSON big integer generator.
     */
    public static Gen<JsBigInt> arbitrary(int bits) {
        return new JsBigIntGen(BigIntGen.arbitrary(bits));
    }


    /**
     * Returns a biased JSON big integer generator with a specified number of bits.
     *
     * @param bits The number of bits for the generated big integer.
     * @return A biased JSON big integer generator.
     */
    public static Gen<JsBigInt> biased(int bits) {
        return new JsBigIntGen(BigIntGen.biased(bits));
    }


    @Override
    public Supplier<JsBigInt> apply(final RandomGenerator seed) {
        return gen.map(JsBigInt::of)
                  .apply(requireNonNull(seed));
    }

}
