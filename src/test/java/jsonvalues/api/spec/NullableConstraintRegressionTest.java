package jsonvalues.api.spec;

import java.time.Instant;
import jsonvalues.JsInstant;
import jsonvalues.JsNull;
import jsonvalues.spec.InstantSchema;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NullableConstraintRegressionTest {

  @Test
  void shouldKeepInstantConstraintsAfterNullable() {
    JsSpec spec = JsSpecs.instant(InstantSchema.withMaximum(Instant.EPOCH))
                         .nullable();

    Assertions.assertTrue(spec.test(JsNull.NULL)
                              .isEmpty());
    Assertions.assertTrue(spec.test(JsInstant.of(Instant.EPOCH))
                              .isEmpty());
    Assertions.assertFalse(spec.test(JsInstant.of(Instant.EPOCH.plusSeconds(1)))
                               .isEmpty());
  }
}
