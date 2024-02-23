package jsonvalues.spec;

public final class IntegerSchema {

  private int minimum;
  private int maximum;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;
  private int multipleOf;

  public IntegerSchema setMinimum(final int minimum) {
    this.minimum = minimum;
    return this;
  }

  public IntegerSchema setMaximum(final int maximum) {
    this.maximum = maximum;
    return this;
  }

  public IntegerSchema setExclusiveMinimum(final boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

  public IntegerSchema setExclusiveMaximum(final boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
    return this;
  }

  public IntegerSchema setMultipleOf(final int multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be > 0");
    }
    this.multipleOf = multipleOf;
    return this;
  }

   IntegerSchemaConstraints build() {
    return new IntegerSchemaConstraints(minimum,
                                        maximum,
                                        exclusiveMinimum,
                                        exclusiveMaximum,
                                        multipleOf);
  }
}