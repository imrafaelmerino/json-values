package jsonvalues.spec;

import fun.tuple.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;

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

  public SpecGenConfBuilder withOptionalObjFieldProbability(int probability) {
    this.optionalProbability = probability;
    return this;
  }

  public SpecGenConfBuilder withNullableObjProbability(int probability) {
    this.nullableProbability = probability;
    return this;
  }

  public SpecGenConfBuilder withArraySize(int minimumSize,
                                          int maximumSize) {
    this.arraySize = Pair.of(minimumSize,
                             maximumSize);
    return this;
  }

  public SpecGenConfBuilder withMapSize(int minimumSize,
                                        int maximumSize) {
    this.mapSize = Pair.of(minimumSize,
                           maximumSize);
    return this;
  }

  public SpecGenConfBuilder withKeyMapLength(int minimumLength,
                                             int maximumLength) {
    this.keyMapLength = Pair.of(minimumLength,
                                maximumLength);
    return this;
  }

  public SpecGenConfBuilder withStringLength(int minimumLength,
                                             int maximumLength) {
    this.stringLength = Pair.of(minimumLength,
                                maximumLength);
    return this;
  }

  public SpecGenConfBuilder withIntSize(int minimumSize,
                                        int maximumSize) {
    this.intSize = Pair.of(minimumSize,
                           maximumSize);
    return this;
  }

  public SpecGenConfBuilder withLongSize(long minimumSize,
                                         long maximumSize) {
    this.longSize = Pair.of(minimumSize,
                            maximumSize);
    return this;
  }

  public SpecGenConfBuilder withDoubleSize(double minimumSize,
                                           double maximumSize) {
    this.doubleSize = Pair.of(minimumSize,
                              maximumSize);
    return this;
  }

  public SpecGenConfBuilder withBigDecSize(BigDecimal minimumSize,
                                           BigDecimal maximumSize) {
    this.bigDecSize = Pair.of(Objects.requireNonNull(minimumSize),
                              Objects.requireNonNull(maximumSize));
    return this;
  }

  public SpecGenConfBuilder withBigIntSize(BigInteger minimumSize,
                                           BigInteger maximumSize) {
    this.bigIntSize = Pair.of(Objects.requireNonNull(minimumSize),
                              Objects.requireNonNull(maximumSize));
    return this;
  }

  public SpecGenConfBuilder withBinaryLength(int minimumLength,
                                             int maximumLength) {
    this.binarySize = Pair.of(minimumLength,
                              maximumLength);
    return this;
  }

  public SpecGenConfBuilder withInstantRange(Instant minimumDate,
                                             Instant maximumDate) {
    this.instantSize = Pair.of(Objects.requireNonNull(minimumDate),
                               Objects.requireNonNull(maximumDate));
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