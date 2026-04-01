package jsonvalues.spec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsParsersLeadingZeroWithPlusTest {

  @Test
  public void shouldRejectLeadingZeroAfterPlusSignInIntegralNumber() {
    JsObjSpecParser parser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                             JsSpecs.longInteger()));

    JsParserException exception =
        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("{\"x\":+01}"));

    Assertions.assertTrue(exception.getMessage()
                                   .contains(ParserErrors.LEADING_ZERO));
  }

  @Test
  public void shouldRejectPlusSignInDoubleNumber() {
    JsObjSpecParser parser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                             JsSpecs.doubleNumber()));

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("{\"x\":+1}"));
  }
}
