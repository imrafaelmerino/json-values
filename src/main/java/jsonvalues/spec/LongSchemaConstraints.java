package jsonvalues.spec;

record LongSchemaConstraints(long minimum,
                             long maximum,
                             boolean exclusiveMinimum,
                             boolean exclusiveMaximum,
                             long multipleOf) {

  LongSchemaConstraints {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum == maximum && (exclusiveMinimum || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }
  }
}
