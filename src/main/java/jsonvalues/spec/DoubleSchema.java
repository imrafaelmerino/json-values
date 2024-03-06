package jsonvalues.spec;

/**
 * A class representing the schema for double values in a JSON structure. It allows setting constraints such as minimum,
 * maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class DoubleSchema {

  private DoubleSchema() {
  }

  private double minimum = Double.NEGATIVE_INFINITY;
  private double maximum = Double.POSITIVE_INFINITY;

  public static DoubleSchema withMinimum(final double minimum) {
    var schema = new DoubleSchema();
    schema.minimum = minimum;
    return schema;
  }

  public static DoubleSchema withMaximum(final double maximum) {
    var schema = new DoubleSchema();
    schema.maximum = maximum;
    return schema;
  }

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
