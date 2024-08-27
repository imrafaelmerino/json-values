package jsonvalues.spec;


record DoubleSchemaConstraints(double minimum,
                               double maximum) {

  DoubleSchemaConstraints {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }

  }

}
