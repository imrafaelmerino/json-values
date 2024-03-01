package jsonvalues.spec;

/**
 * A class representing the schema for long values in a JSON structure. It allows setting constraints such as minimum,
 * maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class LongSchema {

  private long minimum = Long.MIN_VALUE;
  private long maximum = Long.MAX_VALUE;
  private long multipleOf;

  /**
   * Sets the minimum value for long numbers in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This LongSchema instance for method chaining.
   */
  public LongSchema setMinimum(final long minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Sets the maximum value for long numbers in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This LongSchema instance for method chaining.
   */
  public LongSchema setMaximum(final long maximum) {
    this.maximum = maximum;
    return this;
  }

  /**
   * Sets a constraint for a multiple of long numbers in the schema.
   *
   * @param multipleOf The value that the long numbers must be a multiple of.
   * @return This LongSchema instance for method chaining.
   * @throws IllegalArgumentException If multipleOf is not greater than 0.
   */
  public LongSchema setMultipleOf(final long multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be greater than 0");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  /**
   * Builds and returns an instance of LongSchemaConstraints based on the specified constraints.
   *
   * @return An instance of LongSchemaConstraints with the specified constraints.
   */
  LongSchemaConstraints build() {
    return new LongSchemaConstraints(minimum,
                                     maximum,
                                     multipleOf);
  }
}
