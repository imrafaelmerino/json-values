package jsonvalues.gen;

import fun.gen.BigDecGen;
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
 * <code>biased</code> and <code>arbitrary</code> or passing a big integer {@link BigIntGen generator}
 * to the constructor. Arbitrary generators produces uniformed distributions of values.
 * Biased generators produces, with higher probability, potential problematic values that
 * usually cause more bugs.
 *
 */
public final class JsBigIntGen implements Gen<JsBigInt> {
    private final Gen<BigInteger> gen;

    public JsBigIntGen(final Gen<BigInteger> gen) {
        this.gen = requireNonNull(gen);
    }

    public static Gen<JsBigInt> arbitrary(int minBits,
                                          int maxBits) {
        return new JsBigIntGen(BigIntGen.arbitrary(minBits,
                                                   maxBits));
    }


    public static Gen<JsBigInt> biased(int minBits,
                                       int maxBits) {
        return new JsBigIntGen(BigIntGen.biased(minBits,
                                                maxBits));
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
