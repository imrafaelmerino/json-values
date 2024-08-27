package jsonvalues.spec;

import java.math.BigDecimal;

/**
 * A class representing the schema for BigDecimal values in a JSON spec. It allows setting constraints such as minimum and
 * maximum values.
 */
public final class DecimalSchema {

  private DecimalSchema() {
  }

  private BigDecimal minimum = BigDecimal.valueOf(Long.MIN_VALUE);
  private BigDecimal maximum = BigDecimal.valueOf(Long.MAX_VALUE);

  /**
   * Creates a DecimalSchema with a minimum constraint.
   *
   * @param minimum The minimum value for the decimal.
   * @return A DecimalSchema instance with the specified minimum constraint.
   */
  public static DecimalSchema withMinimum(final BigDecimal minimum) {
    var schema = new DecimalSchema();
    schema.minimum = minimum;
    return schema;
  }

  /**
   * Creates a DecimalSchema with a maximum constraint.
   *
   * @param maximum The maximum value for the decimal.
   * @return A DecimalSchema instance with the specified maximum constraint.
   */
  public static DecimalSchema withMaximum(final BigDecimal maximum) {
    var schema = new DecimalSchema();
    schema.maximum = maximum;
    return schema;
  }

  /**
   * Creates a DecimalSchema with both minimum and maximum constraints.
   *
   * @param minimum The minimum value for the decimal.
   * @param maximum The maximum value for the decimal.
   * @return A DecimalSchema instance with the specified minimum and maximum constraints.
   */
  public static DecimalSchema between(final BigDecimal minimum,
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
