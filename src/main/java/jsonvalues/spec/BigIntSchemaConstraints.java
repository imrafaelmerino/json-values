package jsonvalues.spec;

import java.math.BigInteger;

record BigIntSchemaConstraints(BigInteger minimum,
                               BigInteger maximum) {

  BigIntSchemaConstraints {

    if (minimum != null && maximum != null && minimum.compareTo(maximum) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
  }
}
