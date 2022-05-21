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
 */
public final class JsBigIntGen implements Gen<JsBigInt> {
    private final Gen<BigInteger> gen;

    public JsBigIntGen(Gen<BigInteger> gen) {
        this.gen = requireNonNull(gen);
    }

    public static Gen<JsBigInt> arbitrary(int min,
                                          int max) {
        return new JsBigIntGen(BigIntGen.arbitrary(min,
                                                   max));
    }

    /**
     * @param maxBits
     * @return
     */
    public static Gen<JsBigInt> biased(int minBits,
                                       int maxBits) {
        return new JsBigIntGen(BigIntGen.biased(minBits,
                                                maxBits));
    }

    /**
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsBigInt> apply(Random seed) {
        return gen.map(JsBigInt::of)
                  .apply(seed);
    }

}
