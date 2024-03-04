package jsonvalues.spec;


public final class LongSchema {

  private long minimum = Long.MIN_VALUE;
  private long maximum = Long.MAX_VALUE;


  public static LongSchema withMinimum(final long minimum) {
    var schema =  new LongSchema();
    schema.minimum = minimum;
    return schema;
  }

  public static LongSchema withMaximum(final long maximum) {
    var schema =  new LongSchema();
    schema.maximum = maximum;
    return schema;
  }

  public static LongSchema betweenInterval(final long minimum,
                                           final long maximum) {
    var schema =  new LongSchema();
    schema.minimum = minimum;
    schema.maximum = maximum;
    return schema;
  }


  LongSchemaConstraints build() {
    return new LongSchemaConstraints(minimum,
                                     maximum);
  }
}
