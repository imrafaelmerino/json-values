package jsonvalues.spec;

public final class DoubleSchema {

  private double minimum = Double.NEGATIVE_INFINITY;
  private double maximum = Double.NEGATIVE_INFINITY;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;
  private double multipleOf = Double.NEGATIVE_INFINITY;

  public DoubleSchema setMinimum(final double minimum) {
    this.minimum = minimum;
    return this;
  }

  public DoubleSchema setMaximum(final double maximum) {
    this.maximum = maximum;
    return this;
  }

  public DoubleSchema setExclusiveMinimum() {
    this.exclusiveMinimum = true;
    return this;
  }

  public DoubleSchema setExclusiveMaximum() {
    this.exclusiveMaximum = true;
    return this;
  }

  public DoubleSchema setMultipleOf(final double multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be > 0");
    }
    this.multipleOf = multipleOf;
    return this;
  }

  DoubleSchemaConstraints build() {
    return new DoubleSchemaConstraints(minimum,
                                       maximum,
                                       exclusiveMinimum,
                                       exclusiveMaximum,
                                       multipleOf);
  }
}