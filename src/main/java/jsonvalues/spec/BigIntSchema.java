package jsonvalues.spec;

import java.math.BigInteger;
import java.util.Objects;

public final class BigIntSchema {

  private BigInteger minimum;
  private BigInteger maximum;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;
  private BigInteger multipleOf;

  public BigIntSchema setMinimum(final BigInteger minimum) {
    this.minimum = minimum;
    return this;
  }

  public BigIntSchema setMaximum(final BigInteger maximum) {
    this.maximum = maximum;
    return this;
  }

  public BigIntSchema setExclusiveMinimum() {
    this.exclusiveMinimum = true;
    return this;
  }

  public BigIntSchema setExclusiveMaximum() {
    this.exclusiveMaximum = true;
    return this;
  }

  public BigIntSchema setMultipleOf(final BigInteger multipleOf) {
    if (Objects.requireNonNull(multipleOf)
               .signum() <= 0) {
      throw new IllegalArgumentException("multipleOf must be a positive number");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  BigIntSchemaConstraints build() {
    return new BigIntSchemaConstraints(minimum,
                                       maximum,
                                       exclusiveMinimum,
                                       exclusiveMaximum,
                                       multipleOf);
  }
}