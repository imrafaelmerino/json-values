package jsonvalues.spec;

/**
 * A class representing the schema for integer values in a JSON spec. It allows setting constraints such as minimum and
 * maximum values.
 */
public final class IntegerSchema {

  private IntegerSchema() {
  }
  private int minimum = Integer.MIN_VALUE;
  private int maximum = Integer.MAX_VALUE;

  /**
   * Creates an IntegerSchema with a minimum constraint.
   *
   * @param minimum The minimum value for the integer number.
   * @return A IntegerSchema instance with the specified minimum constraint.
   */
  public static IntegerSchema withMinimum(final int minimum) {
    var schema = new IntegerSchema();
    schema.minimum = minimum;
    return schema;
  }
  /**
   * Creates an IntegerSchema with a maximum constraint.
   *
   * @param maximum The maximum value for the integer number.
   * @return A IntegerSchema instance with the specified maximum constraint.
   */
  public static IntegerSchema withMaximum(final int maximum) {
    var schema = new IntegerSchema();
    schema.maximum = maximum;
    return schema;
  }
  /**
   * Creates an IntegerSchema with both minimum and maximum constraints.
   *
   * @param minimum The minimum value for the integer number.
   * @param maximum The maximum value for the integer number.
   * @return A IntegerSchema instance with the specified minimum and maximum constraints.
   */
  public static IntegerSchema between(final int minimum,
                                      final int maximum) {
    var schema = new IntegerSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }

  IntegerSchemaConstraints build() {
    return new IntegerSchemaConstraints(minimum,
                                        maximum);
  }
}
