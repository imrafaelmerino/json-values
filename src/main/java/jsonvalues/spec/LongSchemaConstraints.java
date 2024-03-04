package jsonvalues.spec;

record LongSchemaConstraints(long minimum,
                             long maximum) {

  LongSchemaConstraints {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }

  }
}
