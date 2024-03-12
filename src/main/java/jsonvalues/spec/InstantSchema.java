package jsonvalues.spec;

import java.time.Instant;
import java.util.Objects;

/**
 * A class representing the schema for instant values in a JSON spec. It allows setting constraints such as minimum and
 * maximum values.
 */
public final class InstantSchema {

  private InstantSchema() {
  }

  private Instant minimum = Instant.MIN;
  private Instant maximum = Instant.MAX;

  /**
   * Creates an InstantSchema with a minimum constraint.
   *
   * @param minimum The minimum value for the instant.
   * @return A InstantSchema instance with the specified minimum constraint.
   */
  public static InstantSchema withMinimum(final Instant minimum) {
    var schema = new InstantSchema();
    schema.minimum = Objects.requireNonNull(minimum);
    return schema;
  }

  /**
   * Creates an InstantSchema with a maximum constraint.
   *
   * @param maximum The maximum value for the instant.
   * @return A InstantSchema instance with the specified maximum constraint.
   */
  public static InstantSchema withMaximum(final Instant maximum) {
    var schema = new InstantSchema();
    schema.maximum = Objects.requireNonNull(maximum);
    return schema;
  }

  /**
   * Creates an InstantSchema with both minimum and maximum constraints.
   *
   * @param minimum The minimum value for the instant.
   * @param maximum The maximum value for the instant.
   * @return A InstantSchema instance with the specified minimum and maximum constraints.
   */
  public static InstantSchema between(final Instant minimum,
                                      final Instant maximum) {
    var schema = new InstantSchema();
    schema.minimum = Objects.requireNonNull(minimum);
    schema.maximum = Objects.requireNonNull(maximum);
    return schema;
  }

  InstantSchemaConstraints build() {
    return new InstantSchemaConstraints(minimum,
                                        maximum);
  }
}
