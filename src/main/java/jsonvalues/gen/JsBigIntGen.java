package jsonvalues.gen;

import fun.gen.BigIntGen;
import fun.gen.Gen;
import jsonvalues.JsBigInt;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Supplier;

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

    /**
     * Creates a JsBigInt generator from a specified big integer generator
     *
     * @param gen the big integer generator
     */
    private JsBigIntGen(final Gen<BigInteger> gen) {
        this.gen = requireNonNull(gen);
    }

    public static Gen<JsBigInt> arbitrary(int bits) {
        return new JsBigIntGen(BigIntGen.arbitrary(bits));
    }


    public static Gen<JsBigInt> biased(int bits) {
        return new JsBigIntGen(BigIntGen.biased(bits));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsBigInt each time it's called
     * @param seed the generator seed
     * @return a JsBigInt supplier
     */
    @Override
    public Supplier<JsBigInt> apply(final Random seed) {
        return gen.map(JsBigInt::of)
                  .apply(requireNonNull(seed));
    }

}
