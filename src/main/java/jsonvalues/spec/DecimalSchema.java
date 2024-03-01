package jsonvalues.spec;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A class representing the schema for BigDecimal values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class DecimalSchema {

  private BigDecimal minimum;
  private BigDecimal maximum;
  private BigDecimal multipleOf;

  /**
   * Sets the minimum value for BigDecimal numbers in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This DecimalSchema instance for method chaining.
   */
  public DecimalSchema setMinimum(final BigDecimal minimum) {
    this.minimum = Objects.requireNonNull(minimum);
    return this;
  }

  /**
   * Sets the maximum value for BigDecimal numbers in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This DecimalSchema instance for method chaining.
   */
  public DecimalSchema setMaximum(final BigDecimal maximum) {
    this.maximum = Objects.requireNonNull(maximum);
    return this;
  }

  /**
   * Sets a constraint for a multiple of BigDecimal numbers in the schema.
   *
   * @param multipleOf The value that the BigDecimal numbers must be a multiple of.
   * @return This DecimalSchema instance for method chaining.
   * @throws IllegalArgumentException If multipleOf is not a positive number.
   */
  public DecimalSchema setMultipleOf(final BigDecimal multipleOf) {
    if (Objects.requireNonNull(multipleOf)
               .signum() <= 0) {
      throw new IllegalArgumentException("multipleOf must be a positive number");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Builds and returns an instance of DecimalSchemaConstraints based on the specified constraints.
   *
   * @return An instance of DecimalSchemaConstraints with the specified constraints.
   */
  DecimalSchemaConstraints build() {
    return new DecimalSchemaConstraints(minimum,
                                        maximum,
                                        multipleOf);
  }
}
