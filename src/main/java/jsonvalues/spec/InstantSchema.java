package jsonvalues.spec;

import java.time.Instant;
import java.util.Objects;

/**
 * A class representing the schema for integer values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class InstantSchema {

  private InstantSchema() {
  }
  private Instant minimum;
  private Instant maximum;


  public static InstantSchema withMinimum(final Instant minimum) {
    var schema = new InstantSchema();
    schema.minimum = Objects.requireNonNull(minimum);
    return schema;
  }

  public static InstantSchema withMaximum(final Instant maximum) {
    var schema = new InstantSchema();
    schema.maximum = Objects.requireNonNull(maximum);
    return schema;
  }

  public static InstantSchema between(final Instant minimum,
                                      final Instant maximum) {
    var schema = new InstantSchema();
    schema.minimum = Objects.requireNonNull(minimum);
    schema.maximum = Objects.requireNonNull(maximum);
    return schema;
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
