package jsonvalues.spec;

import java.math.BigDecimal;
import java.util.Objects;

record DecimalSchemaConstraints(BigDecimal minimum,
                                BigDecimal maximum,
                                boolean exclusiveMinimum,
                                boolean exclusiveMaximum,
                                BigDecimal multipleOf) {

  DecimalSchemaConstraints {
    if (Objects.requireNonNull(minimum)
               .compareTo(Objects.requireNonNull(maximum)) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum.compareTo(maximum) == 0 && (exclusiveMinimum
                                            || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }
  }


}
