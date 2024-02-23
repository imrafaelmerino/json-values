package jsonvalues.spec;

public final class LongSchema {

  private long minimum;
  private long maximum;
  private boolean exclusiveMinimum;
  private boolean exclusiveMaximum;
  private long multipleOf;

  public LongSchema setMinimum(final long minimum) {
    this.minimum = minimum;
    return this;
  }

  public LongSchema setMaximum(final long maximum) {
    this.maximum = maximum;
    return this;
  }

  public LongSchema setExclusiveMinimum(final boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
    return this;
  }

  public LongSchema setExclusiveMaximum(final boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
    return this;
  }

  public LongSchema setMultipleOf(final long multipleOf) {
    if (multipleOf <= 0) throw new IllegalArgumentException("multipleOf must be greater than 0");
    this.multipleOf = multipleOf;
    return this;
  }

  LongSchemaConstraints build() {
    return new LongSchemaConstraints(minimum,
                                     maximum,
                                     exclusiveMinimum,
                                     exclusiveMaximum,
                                     multipleOf);
  }
}