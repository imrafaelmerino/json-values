package jsonvalues.spec;


import java.util.Objects;

final class DoubleSchemaConstraints {

  private final double minimum;
  private final double maximum;
  private final boolean exclusiveMinimum;
  private final boolean exclusiveMaximum;
  private final double multipleOf;


  DoubleSchemaConstraints(final double minimum,
                          final double maximum,
                          final boolean exclusiveMinimum,
                          final boolean exclusiveMaximum,
                          final double multipleOf) {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum == maximum && (exclusiveMinimum || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }
    this.minimum = minimum;
    this.maximum = maximum;
    this.exclusiveMinimum = exclusiveMinimum;
    this.exclusiveMaximum = exclusiveMaximum;
    this.multipleOf = multipleOf;
  }

  public double minimum() {
    return minimum;
  }

  public double maximum() {
    return maximum;
  }

  public boolean exclusiveMinimum() {
    return exclusiveMinimum;
  }

  public boolean exclusiveMaximum() {
    return exclusiveMaximum;
  }

  public double multipleOf() {
    return multipleOf;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (DoubleSchemaConstraints) obj;
    return Double.doubleToLongBits(this.minimum) == Double.doubleToLongBits(that.minimum) &&
           Double.doubleToLongBits(this.maximum) == Double.doubleToLongBits(that.maximum) &&
           this.exclusiveMinimum == that.exclusiveMinimum &&
           this.exclusiveMaximum == that.exclusiveMaximum &&
           Double.doubleToLongBits(this.multipleOf) == Double.doubleToLongBits(that.multipleOf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minimum,
                        maximum,
                        exclusiveMinimum,
                        exclusiveMaximum,
                        multipleOf);
  }

  @Override
  public String toString() {
    return "DoubleSchema[" +
           "minimum=" + minimum + ", " +
           "maximum=" + maximum + ", " +
           "exclusiveMinimum=" + exclusiveMinimum + ", " +
           "exclusiveMaximum=" + exclusiveMaximum + ", " +
           "multipleOf=" + multipleOf + ']';
  }

}
