package jsonvalues.spec;

import java.math.BigDecimal;

/**
 * A class representing the schema for BigDecimal values in a JSON structure. It allows setting constraints such as
 * minimum, maximum values, exclusivity of minimum and maximum, and a multiple of constraint.
 */
public final class DecimalSchema {

  private BigDecimal minimum;
  private BigDecimal maximum;

  public static DecimalSchema withMinimum(final BigDecimal minimum) {
    var schema = new DecimalSchema();
    schema.minimum = minimum;
    return schema;
  }

  public static DecimalSchema withMaximum(final BigDecimal maximum) {
    var schema = new DecimalSchema();
    schema.maximum = maximum;
    return schema;
  }

  public static DecimalSchema betweenInterval(final BigDecimal minimum,
                                              final BigDecimal maximum) {
    var schema = new DecimalSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }


  DecimalSchemaConstraints build() {
    return new DecimalSchemaConstraints(minimum,
                                        maximum);
  }
}
