package jsonvalues.spec;

import java.math.BigDecimal;

record DecimalSchemaConstraints(BigDecimal minimum,
                                BigDecimal maximum,
                                boolean exclusiveMinimum,
                                boolean exclusiveMaximum,
                                BigDecimal multipleOf) {

  DecimalSchemaConstraints {
    if (minimum != null && maximum != null && minimum
                                                  .compareTo(maximum) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }
    if (minimum != null && maximum != null && minimum.compareTo(maximum) == 0 && (exclusiveMinimum
                                                                                  || exclusiveMaximum)) {
      throw new IllegalArgumentException("minimum must be less than maximum if exclusiveMinimum or exclusiveMaximum are true");
    }
  }


}
