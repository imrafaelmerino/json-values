package jsonvalues.spec;

import java.time.Instant;

/**
 * A class representing the schema for integer values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class InstantSchema {

  private Instant minimum;
  private Instant maximum;

  /**
   * Sets the minimum value for the instant in the schema.
   *
   * @param minimum The minimum value (inclusive).
   * @return This InstantSchema instance for method chaining.
   */
  public InstantSchema setMinimum(final Instant minimum) {
    this.minimum = minimum;
    return this;
  }

  /**
   * Sets the maximum value for the instant in the schema.
   *
   * @param maximum The maximum value (inclusive).
   * @return This InstantSchema instance for method chaining.
   */
  public InstantSchema setMaximum(final Instant maximum) {
    this.maximum = maximum;
    return this;
  }




  /**
   * Builds and returns an instance of IntegerSchemaConstraints based on the specified constraints.
   *
   * @return An instance of IntegerSchemaConstraints with the specified constraints.
   */
  InstantSchemaConstraints build() {
    return new InstantSchemaConstraints(minimum,
                                        maximum);
  }
}
