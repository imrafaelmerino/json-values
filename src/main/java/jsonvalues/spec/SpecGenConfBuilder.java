package jsonvalues.spec;

import fun.tuple.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;

/**
 * The `SpecGenConfBuilder` class is responsible for configuring the generation parameters used by `SpecToGen`. It
 * allows customization of the characteristics of the generated data, such as the size of arrays, maps, and numbers, as
 * well as the length of strings, keys in maps, and other data types. Additionally, it provides options for specifying
 * the probability of generating optional and nullable object fields.
 *
 * @see SpecToGen
 */
public final class SpecGenConfBuilder {

  private static final int MAX_ARRAY_SIZE = 100;
  private static final int MIN_ARRAY_SIZE = 0;
  private static final int MAX_MAP_SIZE = 20;
  private static final int MIN_MAP_SIZE = 1;
  private static final int MIN_KEY_MAP_SIZE = 1;
  private static final int MAX_KEY_MAP_SIZE = 10;
  private static final int MIN_STRING_LENGTH = 0;
  private static final int MAX_STRING_LENGTH = 100;
  private static final int MIN_INT_SIZE = Integer.MIN_VALUE;
  private static final int MAX_INT_SIZE = Integer.MAX_VALUE;
  private static final long MIN_LONG_SIZE = Long.MIN_VALUE;
  private static final long MAX_LONG_SIZE = Long.MAX_VALUE;
  private static final double MIN_DOUBLE_SIZE = Double.MIN_VALUE;
  private static final double MAX_DOUBLE_SIZE = Double.MAX_VALUE;
  private static final BigDecimal MIN_BIG_DEC = BigDecimal.valueOf(Double.MIN_VALUE);
  private static final BigDecimal MAX_BIG_DEC = BigDecimal.valueOf(Double.MAX_VALUE);
  private static final BigInteger MIN_BIG_INT = new BigInteger("-100000000000000000000000000000000");
  private static final BigInteger MAX_BIG_INT = new BigInteger("100000000000000000000000000000000");
  private static final int MIN_BINARY_LENGTH = 0;
  private static final int MAX_BINARY_LENGTH = 100;
  private Pair<Integer, Integer> arraySize = Pair.of(MIN_ARRAY_SIZE,
                                                     MAX_ARRAY_SIZE);
  private Pair<Integer, Integer> mapSize = Pair.of(MIN_MAP_SIZE,
                                                   MAX_MAP_SIZE);
  private Pair<Integer, Integer> keyMapLength = Pair.of(MIN_KEY_MAP_SIZE,
                                                        MAX_KEY_MAP_SIZE);
  private Pair<Integer, Integer> stringLength = Pair.of(MIN_STRING_LENGTH,
                                                        MAX_STRING_LENGTH);
  private Pair<Integer, Integer> intSize = Pair.of(MIN_INT_SIZE,
                                                   MAX_INT_SIZE);
  private Pair<Long, Long> longSize = Pair.of(MIN_LONG_SIZE,
                                              MAX_LONG_SIZE);
  private Pair<Double, Double> doubleSize = Pair.of(MIN_DOUBLE_SIZE,
                                                    MAX_DOUBLE_SIZE);
  private Pair<BigDecimal, BigDecimal> bigDecSize = Pair.of(MIN_BIG_DEC,
                                                            MAX_BIG_DEC);
  private Pair<BigInteger, BigInteger> bigIntSize = Pair.of(MIN_BIG_INT,
                                                            MAX_BIG_INT);
  private Pair<Integer, Integer> binarySize = Pair.of(MIN_BINARY_LENGTH,
                                                      MAX_BINARY_LENGTH);
  private Pair<Instant, Instant> instantSize = Pair.of(Instant.MIN,
                                                       Instant.MAX);

  int optionalProbability = 4;
  int nullableProbability = 4;

  /**
   * Sets the probability of generating optional object fields. A number `n` between [2,10], being the ratio of the
   * probability of generating an object with optional fields `n/1`. It can be useful to avoid stackoverflow errors when
   * generating recursive objects, in which case we may need to specify a greater probability of optional fields.
   *
   * @param probability The probability value to set.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withOptionalKeyProbability(int probability) {
    this.optionalProbability = probability;
    return this;
  }

  /**
   * Sets the probability of generating nullable objects. A number `n` between [2,10], being the ratio of the
   * probability of generating an object with null fields `n/1`.It can be useful to avoid stackoverflow errors when *
   * generating recursive objects, in which case we may need to specify a greater probability of optional fields.
   *
   * @param probability The probability value to set.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withNullableKeyProbability(int probability) {
    this.nullableProbability = probability;
    return this;
  }

  /**
   * Sets the size range for generated arrays.
   *
   * @param minimumSize The minimum size for arrays.
   * @param maximumSize The maximum size for arrays.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withArraySize(int minimumSize,
                                          int maximumSize) {
    this.arraySize = Pair.of(minimumSize,
                             maximumSize);
    return this;
  }

  /**
   * Sets the size range for generated json objects.
   *
   * @param minimumSize The minimum size for maps.
   * @param maximumSize The maximum size for maps.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withObjSize(int minimumSize,
                                        int maximumSize) {
    this.mapSize = Pair.of(minimumSize,
                           maximumSize);
    return this;
  }

  /**
   * Sets the length range for keys in generated json objects.
   *
   * @param minimumLength The minimum length for keys.
   * @param maximumLength The maximum length for keys.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withKeyLength(int minimumLength,
                                          int maximumLength) {
    this.keyMapLength = Pair.of(minimumLength,
                                maximumLength
                               );
    return this;
  }

  /**
   * Sets the length range for generated strings.
   *
   * @param minimumLength The minimum length for strings.
   * @param maximumLength The maximum length for strings.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withStringLength(int minimumLength,
                                             int maximumLength) {
    this.stringLength = Pair.of(minimumLength,
                                maximumLength
                               );
    return this;
  }

  /**
   * Sets the size range for generated integers.
   *
   * @param minimumSize The minimum size for integers.
   * @param maximumSize The maximum size for integers.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withIntSize(int minimumSize,
                                        int maximumSize) {
    this.intSize = Pair.of(minimumSize,
                           maximumSize
                          );
    return this;
  }

  /**
   * Sets the size range for generated long integers.
   *
   * @param minimumSize The minimum size for long integers.
   * @param maximumSize The maximum size for long integers.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withLongSize(long minimumSize,
                                         long maximumSize
                                        ) {
    this.longSize = Pair.of(minimumSize,
                            maximumSize);
    return this;
  }

  /**
   * Sets the size range for generated double values.
   *
   * @param minimumSize The minimum size for double values.
   * @param maximumSize The maximum size for double values.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withDoubleSize(double minimumSize,
                                           double maximumSize
                                          ) {
    this.doubleSize = Pair.of(minimumSize,
                              maximumSize);
    return this;
  }

  /**
   * Sets the size range for generated BigDecimal values.
   *
   * @param minimumSize The minimum size for BigDecimal values.
   * @param maximumSize The maximum size for BigDecimal values.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withBigDecSize(BigDecimal minimumSize,
                                           BigDecimal maximumSize
                                          ) {
    this.bigDecSize = Pair.of(Objects.requireNonNull(minimumSize),
                              Objects.requireNonNull(maximumSize));
    return this;
  }

  /**
   * Sets the size range for generated BigInteger values.
   *
   * @param minimumSize The minimum size for BigInteger values.
   * @param maximumSize The maximum size for BigInteger values.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withBigIntSize(BigInteger minimumSize,
                                           BigInteger maximumSize
                                          ) {
    this.bigIntSize = Pair.of(Objects.requireNonNull(minimumSize),
                              Objects.requireNonNull(maximumSize));
    return this;
  }

  /**
   * Sets the length range for generated binary data.
   *
   * @param minimumLength The minimum length for binary data.
   * @param maximumLength The maximum length for binary data.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withBinaryLength(int minimumLength,
                                             int maximumLength
                                            ) {
    this.binarySize = Pair.of(minimumLength,
                              maximumLength);
    return this;
  }

  /**
   * Sets the range for generated Instant values.
   *
   * @param minimumDate The minimum Instant value.
   * @param maximumDate The maximum Instant value.
   * @return The updated instance of SpecGenConfBuilder.
   */
  public SpecGenConfBuilder withInstantRange(Instant minimumDate,
                                             Instant maximumDate
                                            ) {
    this.instantSize = Pair.of(Objects.requireNonNull(minimumDate),
                               Objects.requireNonNull(maximumDate)
                              );
    return this;
  }

  GenConf build() {
    return new GenConf(arraySize,
                       mapSize,
                       keyMapLength,
                       stringLength,
                       intSize,
                       longSize,
                       doubleSize,
                       bigDecSize,
                       bigIntSize,
                       binarySize,
                       instantSize,
                       optionalProbability,
                       nullableProbability);
  }
}