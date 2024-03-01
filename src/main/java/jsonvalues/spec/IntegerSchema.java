package jsonvalues.spec;

/**
 * A class representing the schema for integer values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class IntegerSchema {

  private int minimum = Integer.MIN_VALUE;
  private int maximum = Integer.MAX_VALUE;
  private int multipleOf;

  /**
   * Sets the minimum value for integer numbers in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This IntegerSchema instance for method chaining.
   */
  public IntegerSchema setMinimum(final int minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Sets the maximum value for integer numbers in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This IntegerSchema instance for method chaining.
   */
  public IntegerSchema setMaximum(final int maximum) {
    this.maximum = maximum;
    return this;
  }


  /**
   * Sets a constraint for a multiple of integer numbers in the schema.
   *
   * @param multipleOf The value that the integer numbers must be a multiple of.
   * @return This IntegerSchema instance for method chaining.
   * @throws IllegalArgumentException If multipleOf is not greater than 0.
   */
  public IntegerSchema setMultipleOf(final int multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be > 0");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Builds and returns an instance of IntegerSchemaConstraints based on the specified constraints.
   *
   * @return An instance of IntegerSchemaConstraints with the specified constraints.
   */
  IntegerSchemaConstraints build() {
    return new IntegerSchemaConstraints(minimum,
                                        maximum,
                                        multipleOf);
  }
}
