package jsonvalues.spec;

//TODO uniqueItems no implementado
record ArraySchemaConstraints(int minItems,
                              int maxItems,
                              boolean uniqueItems) {

  ArraySchemaConstraints {
    if (maxItems < minItems) {
      throw new IllegalArgumentException("maxItems must be greater than or equal to minItems");
    }
  }

}