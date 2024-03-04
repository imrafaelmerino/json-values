package jsonvalues.spec;

/**
 * A class representing the schema for double values in a JSON structure. It allows setting constraints such as minimum,
 * maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class DoubleSchema {

  private double minimum = Double.NEGATIVE_INFINITY;
  private double maximum = Double.POSITIVE_INFINITY;
  private double multipleOf;

  /**
   * Sets the minimum value for double numbers in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This DoubleSchema instance for method chaining.
   */
  public DoubleSchema setMinimum(final double minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Sets the maximum value for double numbers in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This DoubleSchema instance for method chaining.
   */
  public DoubleSchema setMaximum(final double maximum) {
    this.maximum = maximum;
    return this;
  }


  /**
   * Sets a constraint for a multiple of double numbers in the schema.
   *
   * @param multipleOf The value that the double numbers must be a multiple of.
   * @return This DoubleSchema instance for method chaining.
   * @throws IllegalArgumentException If multipleOf is not greater than 0.
   */
  public DoubleSchema setMultipleOf(final double multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be > 0");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Builds and returns an instance of DoubleSchemaConstraints based on the specified constraints.
   *
   * @return An instance of DoubleSchemaConstraints with the specified constraints.
   */
  DoubleSchemaConstraints build() {
    return new DoubleSchemaConstraints(minimum,
                                       maximum,
                                       multipleOf);
  }
}
