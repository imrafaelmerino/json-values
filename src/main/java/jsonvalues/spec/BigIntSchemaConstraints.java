package jsonvalues.spec;

import java.math.BigInteger;
import java.util.Objects;

record BigIntSchemaConstraints(BigInteger minimum,
                               BigInteger maximum,
                               boolean exclusiveMinimum,
                               boolean exclusiveMaximum,
                               BigInteger multipleOf) {

  BigIntSchemaConstraints {

    if (Objects.requireNonNull(minimum)
               .compareTo(Objects.requireNonNull(maximum)) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum.equals(maximum) && (exclusiveMinimum || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }

  }
}
