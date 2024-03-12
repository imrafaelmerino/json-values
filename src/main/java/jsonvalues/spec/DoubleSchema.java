package jsonvalues.spec;

/**
 * A class representing the schema for double values in a JSON spec. It allows setting constraints such as minimum and
 * maximum values.
 */
public final class DoubleSchema {

  private DoubleSchema() {
  }

  private double minimum = Double.MIN_VALUE;
  private double maximum = Double.MAX_VALUE;
  /**
   * Creates a DoubleSchema with a minimum constraint.
   *
   * @param minimum The minimum value for the decimal.
   * @return A DoubleSchema instance with the specified minimum constraint.
   */
  public static DoubleSchema withMinimum(final double minimum) {
    var schema = new DoubleSchema();
    schema.minimum = minimum;
    return schema;
  }
  /**
   * Creates a DoubleSchema with a maximum constraint.
   *
   * @param maximum The maximum value for the decimal.
   * @return A DoubleSchema instance with the specified maximum constraint.
   */
  public static DoubleSchema withMaximum(final double maximum) {
    var schema = new DoubleSchema();
    schema.maximum = maximum;
    return schema;
  }
  /**
   * Creates a DoubleSchema with both minimum and maximum constraints.
   *
   * @param minimum The minimum value for the double.
   * @param maximum The maximum value for the double.
   * @return A DoubleSchema instance with the specified minimum and maximum constraints.
   */
  public static DoubleSchema between(final double minimum,
                                     final double maximum) {
    var schema = new DoubleSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }


  DoubleSchemaConstraints build() {
    return new DoubleSchemaConstraints(minimum,
                                       maximum);
  }
}
