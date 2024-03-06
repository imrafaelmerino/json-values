package jsonvalues.spec;

/**
 * A class representing the schema for integer values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class IntegerSchema {

  private IntegerSchema() {
  }
  private int minimum = Integer.MIN_VALUE;
  private int maximum = Integer.MAX_VALUE;

  public static IntegerSchema withMinimum(final int minimum) {
    var schema = new IntegerSchema();
    schema.minimum = minimum;
    return schema;
  }

  public static IntegerSchema withMaximum(final int maximum) {
    var schema = new IntegerSchema();
    schema.maximum = maximum;
    return schema;
  }

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
