package jsonvalues.spec;

import java.math.BigInteger;

record BigIntSchemaConstraints(BigInteger minimum,
                               BigInteger maximum,
                               boolean exclusiveMinimum,
                               boolean exclusiveMaximum,
                               BigInteger multipleOf) {

  BigIntSchemaConstraints {

    if (minimum != null && maximum != null && minimum
                                                  .compareTo(maximum) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum != null && minimum.equals(maximum) && (exclusiveMinimum || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }

  }
}
