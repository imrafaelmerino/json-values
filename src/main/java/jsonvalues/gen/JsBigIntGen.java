package jsonvalues.gen;

import static java.util.Objects.requireNonNull;

import fun.gen.BigIntGen;
import fun.gen.Gen;
import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import jsonvalues.JsBigInt;

/**
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
 * <p>
 * Arbitrary generators produce uniformed distributions of values. Biased generators produce, with higher probability,
 * potential problematic values that usually cause more bugs.
 */
public final class JsBigIntGen implements Gen<JsBigInt> {

  private static final Gen<JsBigInt> biased = new JsBigIntGen(BigIntGen.biased());
  private static final Gen<JsBigInt> arbitrary = new JsBigIntGen(BigIntGen.arbitrary());
  private final Gen<BigInteger> gen;

  private JsBigIntGen(final Gen<BigInteger> gen) {
    this.gen = requireNonNull(gen);
  }


  public static Gen<JsBigInt> arbitrary(BigInteger min,
                                        BigInteger max
                                       ) {
    return new JsBigIntGen(BigIntGen.arbitrary(requireNonNull(min),
                                               requireNonNull(max)));
  }


  public static Gen<JsBigInt> arbitrary() {
    return arbitrary;
  }

  public static Gen<JsBigInt> biased() {
    return biased;
  }


  public static Gen<JsBigInt> biased(final BigInteger min,
                                     final BigInteger max) {
    return new JsBigIntGen(BigIntGen.biased(requireNonNull(min),
                                            requireNonNull(max)));
  }


  @Override
  public Supplier<JsBigInt> apply(final RandomGenerator seed) {
    return gen.map(JsBigInt::of)
              .apply(requireNonNull(seed));
  }

}
