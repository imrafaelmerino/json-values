package jsonvalues.spec;

import java.math.BigDecimal;
import java.util.Objects;

public final class DecimalSchema {

  private BigDecimal minimum;
  private BigDecimal maximum;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;

  private BigDecimal multipleOf;

  public DecimalSchema setMinimum(final BigDecimal minimum) {
    this.minimum = minimum;
    return this;
  }

  public DecimalSchema setMaximum(final BigDecimal maximum) {
    this.maximum = maximum;
    return this;
  }

  public DecimalSchema setExclusiveMinimum(final boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

  public DecimalSchema setExclusiveMaximum(final boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
    return this;
  }

  public DecimalSchema setMultipleOf(final BigDecimal multipleOf) {
    if (Objects.requireNonNull(multipleOf).signum() <= 0) {
      throw new IllegalArgumentException("multipleOf must be a positive number");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  DecimalSchemaConstraints build() {
    return new DecimalSchemaConstraints(minimum,
                                        maximum,
                                        exclusiveMinimum,
                                        exclusiveMaximum,
                                        multipleOf);
  }
}