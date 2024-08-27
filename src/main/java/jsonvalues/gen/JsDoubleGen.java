package jsonvalues.gen;


import static java.util.Objects.requireNonNull;

import fun.gen.DoubleGen;
import fun.gen.Gen;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import jsonvalues.JsDouble;

/**
 * Represents a JsDouble generator. It can be created using the static factory methods {@link #biased()} and
 * {@link #arbitrary()} or, if none of the previous suits your needs, from a double generator and the function map:
 *
 * <pre>{@code
 * import fun.gen.Gen;
 * import jsonvalues.JsDouble;
 *
 * Gen<Double> doubleGen = seed -> () -> {...};
 * Gen<JsDouble> jsDoubleGen = gen.map(JsDouble::of);
 * }</pre>
 * <p>
 * Arbitrary generators produce values with a uniform distribution. Biased generators produce potential problematic
 * values with a higher probability, which can help identify and test edge cases.
 */
public final class JsDoubleGen implements Gen<JsDouble> {

  private static final Gen<JsDouble> biased = new JsDoubleGen(DoubleGen.biased());
  private static final Gen<JsDouble> arbitrary = new JsDoubleGen(DoubleGen.arbitrary());
  private final Gen<Double> gen;

  /**
   * Creates a JsDouble generator from a specified double generator
   *
   * @param gen the double generator
   */
  private JsDoubleGen(Gen<Double> gen) {
    this.gen = requireNonNull(gen);
  }

  /**
   * Returns a biased generator that produces potential problematic values with a higher probability. These values
   * include: - The minimum double value - The maximum double value - 0.0
   *
   * @return A biased JsDouble generator.
   */
  public static Gen<JsDouble> biased() {
    return biased;
  }

  /**
   * Returns a generator that produces values with a uniform distribution.
   *
   * @return A JsDouble generator.
   */
  public static Gen<JsDouble> arbitrary() {
    return arbitrary;
  }

  /**
   * Returns a biased generator that produces potential problematic values with a higher probability. These values
   * include: - The lower bound of the interval - The upper bound of the interval - 0.0
   *
   * @param min The lower bound of the interval (inclusive).
   * @param max The upper bound of the interval (inclusive).
   * @return A biased JsDouble generator.
   */
  public static Gen<JsDouble> biased(double min,
                                     double max
                                    ) {
    validateBounds(min,
                   max);
    return new JsDoubleGen(DoubleGen.biased(min,
                                            max));
  }

  private static void validateBounds(final double min,
                                     final double max) {
    if (Double.isNaN(min) || Double.isNaN(max)) {
      throw new IllegalArgumentException("min and max must be valid numbers");
    }
    if (Double.isInfinite(min) || Double.isInfinite(max)) {
      throw new IllegalArgumentException("min and max must be valid numbers");
    }
  }


  /**
   * Returns a generator that produces values uniformly distributed over a specified interval.
   *
   * @param min The lower bound of the interval (inclusive).
   * @param max The upper bound of the interval (inclusive).
   * @return A biased JsDouble generator.
   */
  public static Gen<JsDouble> arbitrary(double min,
                                        double max
                                       ) {
    validateBounds(min,
                   max);
    return new JsDoubleGen(DoubleGen.arbitrary(min,
                                               max));
  }


  @Override
  public Supplier<JsDouble> apply(final RandomGenerator seed) {
    return gen.map(JsDouble::of)
              .apply(requireNonNull(seed));
  }

}
