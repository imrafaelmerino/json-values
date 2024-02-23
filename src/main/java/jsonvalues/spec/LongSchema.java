package jsonvalues.spec;

public final class LongSchema {

  private long minimum = Long.MIN_VALUE;
  private long maximum = Long.MAX_VALUE;
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

  public LongSchema setExclusiveMinimum() {
    this.exclusiveMinimum = true;
    return this;
  }

  public LongSchema setExclusiveMaximum() {
    this.exclusiveMaximum = true;
    return this;
  }

  public LongSchema setMultipleOf(final long multipleOf) {
    if (multipleOf <= 0) {
      throw new IllegalArgumentException("multipleOf must be greater than 0");
    }
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