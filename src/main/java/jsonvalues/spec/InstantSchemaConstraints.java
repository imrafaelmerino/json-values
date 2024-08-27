package jsonvalues.spec;


import java.time.Instant;

record InstantSchemaConstraints(Instant minimum,
                                Instant maximum) {

  InstantSchemaConstraints {
    if (minimum != null && maximum != null) {
      if (minimum.isAfter(maximum)) {
        throw new IllegalArgumentException("minimum must be before maximum");
      }
    }
  }


}
