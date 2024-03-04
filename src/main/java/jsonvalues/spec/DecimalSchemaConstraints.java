package jsonvalues.spec;

import java.math.BigDecimal;

record DecimalSchemaConstraints(BigDecimal minimum,
                                BigDecimal maximum) {

  DecimalSchemaConstraints {
    if (minimum != null && maximum != null && minimum
                                                  .compareTo(maximum) > 0) {
      throw new IllegalArgumentException("minimum must be less than or equal to maximum");
    }

  }


}
