package jsonvalues.spec;

import java.math.BigInteger;
import java.util.Objects;

/**
 * A class representing the schema for BigInteger values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class BigIntSchema {

  private BigInteger minimum;
  private BigInteger maximum;
  private BigInteger multipleOf;

  /**
   * Sets the minimum value for BigInteger numbers in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This BigIntSchema instance for method chaining.
   */
  public BigIntSchema setMinimum(final BigInteger minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Sets the maximum value for BigInteger numbers in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This BigIntSchema instance for method chaining.
   */
  public BigIntSchema setMaximum(final BigInteger maximum) {
    this.maximum = maximum;
    return this;
  }


  /**
   * Sets a constraint for a multiple of BigInteger numbers in the schema.
   *
   * @param multipleOf The value that the BigInteger numbers must be a multiple of.
   * @return This BigIntSchema instance for method chaining.
   * @throws IllegalArgumentException If multipleOf is not a positive number.
   */
  public BigIntSchema setMultipleOf(final BigInteger multipleOf) {
    if (Objects.requireNonNull(multipleOf)
               .signum() <= 0) {
      throw new IllegalArgumentException("multipleOf must be a positive number");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Builds and returns an instance of BigIntSchemaConstraints based on the specified constraints.
   *
   * @return An instance of BigIntSchemaConstraints with the specified constraints.
   */
  BigIntSchemaConstraints build() {
    return new BigIntSchemaConstraints(minimum,
                                       maximum,
                                       multipleOf);
  }
}
