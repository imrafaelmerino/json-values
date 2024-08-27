package jsonvalues.spec;

/**
 * A class representing the schema for long values in a JSON spec. It allows setting constraints such as minimum and
 * maximum values.
 */
public final class LongSchema {

  private LongSchema() {
  }

  private long minimum = Long.MIN_VALUE;
  private long maximum = Long.MAX_VALUE;

  /**
   * Creates a LongSchema with a minimum constraint.
   *
   * @param minimum The minimum value for the long number.
   * @return A LongSchema instance with the specified minimum constraint.
   */
  public static LongSchema withMinimum(final long minimum) {
    var schema = new LongSchema();
    schema.minimum = minimum;
    return schema;
  }

  /**
   * Creates a LongSchema with a maximum constraint.
   *
   * @param maximum The maximum value for the long number.
   * @return A LongSchema instance with the specified maximum constraint.
   */
  public static LongSchema withMaximum(final long maximum) {
    var schema = new LongSchema();
    schema.maximum = maximum;
    return schema;
  }

  /**
   * Creates a LongSchema with both minimum and maximum constraints.
   *
   * @param minimum The minimum value for the long number.
   * @param maximum The maximum value for the long number.
   * @return A LongSchema instance with the specified minimum and maximum constraints.
   */
  public static LongSchema between(final long minimum,
                                   final long maximum) {
    var schema = new LongSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }


  LongSchemaConstraints build() {
    return new LongSchemaConstraints(minimum,
                                     maximum);
  }
}
