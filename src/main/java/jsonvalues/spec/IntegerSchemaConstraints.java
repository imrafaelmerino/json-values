package jsonvalues.spec;


record IntegerSchemaConstraints(int minimum,
                                int maximum) {

  IntegerSchemaConstraints {
    if (minimum > maximum) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
  }


}
