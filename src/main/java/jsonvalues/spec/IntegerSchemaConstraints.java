package jsonvalues.spec;


record IntegerSchemaConstraints(int minimum,
                                int maximum,
                                boolean exclusiveMinimum,
                                boolean exclusiveMaximum,
                                int multipleOf) {

  IntegerSchemaConstraints {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (exclusiveMinimum && minimum == Integer.MAX_VALUE) {
      throw new IllegalArgumentException("minimum must be less than Integer.MAX_VALUE");
    }
  }

  @Override
  public String toString() {
    return "IntegerSchema[" +
           "minimum=" + minimum + ", " +
           "maximum=" + maximum + ", " +
           "exclusiveMinimum=" + exclusiveMinimum + ", " +
           "exclusiveMaximum=" + exclusiveMaximum + ", " +
           "multipleOf=" + multipleOf + ']';
  }

}
