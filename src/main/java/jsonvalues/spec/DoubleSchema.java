package jsonvalues.spec;

public final class DoubleSchema {

  private double minimum;
  private double maximum;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;
  private double multipleOf;

  public DoubleSchema setMinimum(final double minimum) {
    this.minimum = minimum;
    return this;
  }

  public DoubleSchema setMaximum(final double maximum) {
    this.maximum = maximum;
    return this;
  }

  public DoubleSchema setExclusiveMinimum(final boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

  public DoubleSchema setExclusiveMaximum(final boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
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