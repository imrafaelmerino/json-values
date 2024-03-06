package jsonvalues.spec;

import java.math.BigInteger;


public final class BigIntSchema {

  private BigIntSchema() {
  }

  private BigInteger minimum;
  private BigInteger maximum;

  public static BigIntSchema withMinimum(final BigInteger minimum) {
    var schema = new BigIntSchema();
    schema.minimum = minimum;
    return schema;
  }

  public static BigIntSchema withMaximum(final BigInteger maximum) {
    var schema = new BigIntSchema();
    schema.maximum = maximum;
    return schema;
  }

  public static BigIntSchema between(final BigInteger minimum,
                                     final BigInteger maximum) {
    var schema = new BigIntSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }

  BigIntSchemaConstraints build() {
    return new BigIntSchemaConstraints(minimum,
                                       maximum);
  }
}
