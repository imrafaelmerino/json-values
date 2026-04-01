package jsonvalues.api.gen;

import fun.gen.Gen;
import jsonvalues.JsInt;
import jsonvalues.gen.JsObjGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsObjGenValidationTest {

  @Test
  public void shouldRejectOptionalProbabilityLowerThanRange() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> gen.withOptionalProbability(1));
  }

  @Test
  public void shouldRejectOptionalProbabilityGreaterThanRange() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> gen.withOptionalProbability(11));
  }

  @Test
  public void shouldRejectNullableProbabilityLowerThanRange() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> gen.withNullableProbability(1));
  }

  @Test
  public void shouldRejectNullableProbabilityGreaterThanRange() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> gen.withNullableProbability(11));
  }

  @Test
  public void shouldAcceptBoundaryProbabilities() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)));

    Assertions.assertDoesNotThrow(() -> gen.withOptionalProbability(2)
                                           .withNullableProbability(10));
  }
}
