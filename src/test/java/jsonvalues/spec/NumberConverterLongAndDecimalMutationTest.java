package jsonvalues.spec;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterLongAndDecimalMutationTest {

  private static DslJsReader readerOf(String value) {
    DslJsReader reader = JsIO.INSTANCE.newReader(value.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    return reader;
  }

  @Test
  public void shouldParseLongBoundariesAndRejectOverflow() {
    Assertions.assertEquals(Long.MAX_VALUE,
                            NumberConverter.deserializeLong(readerOf(Long.toString(Long.MAX_VALUE))));
    Assertions.assertEquals(Long.MIN_VALUE,
                            NumberConverter.deserializeLong(readerOf(Long.toString(Long.MIN_VALUE))));

    JsParserException positiveOverflow =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeLong(readerOf("9223372036854775808")));
    Assertions.assertTrue(positiveOverflow.getMessage()
                                          .contains(ParserErrors.LONG_OVERFLOW));

    JsParserException negativeOverflow =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeLong(readerOf("-9223372036854775809")));
    Assertions.assertTrue(negativeOverflow.getMessage()
                                          .contains(ParserErrors.LONG_OVERFLOW));
  }

  @Test
  public void shouldEnforceLeadingZeroRulesAndPlusWhitespaceBehaviorForLong() {
    JsParserException leadingZeroPositive =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeLong(readerOf("01")));
    Assertions.assertTrue(leadingZeroPositive.getMessage()
                                             .contains(ParserErrors.LEADING_ZERO));

    JsParserException leadingZeroNegative =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeLong(readerOf("-01")));
    Assertions.assertTrue(leadingZeroNegative.getMessage()
                                             .contains(ParserErrors.LEADING_ZERO));

    Assertions.assertEquals(123L,
                            NumberConverter.deserializeLong(readerOf("+123   ")));

    JsParserException plusWithoutDigits =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeLong(readerOf("+   ")));
    Assertions.assertTrue(plusWithoutDigits.getMessage()
                                           .contains(ParserErrors.DIGIT_NOT_FOUND));
  }

  @Test
  public void shouldAcceptSignedAndUnsignedZeroWithoutTriggeringLeadingZeroRuleInLong() {
    Assertions.assertEquals(0L,
                            NumberConverter.deserializeLong(readerOf("0")));
    Assertions.assertEquals(0L,
                            NumberConverter.deserializeLong(readerOf("-0")));
  }

  @Test
  public void shouldHandleWhitespaceAfterDigitsForPlusAndNonPlusLong() {
    Assertions.assertEquals(1L,
                            NumberConverter.deserializeLong(readerOf("+1 \t,")));
    Assertions.assertEquals(1L,
                            NumberConverter.deserializeLong(readerOf("1 \t,")));
  }

  @Test
  public void shouldParsePositiveDecimalFastPathWithDotAndExponentAndWhitespace() throws JsParserException {
    Assertions.assertEquals(new BigDecimal("1.25"),
                            NumberConverter.deserializeDecimal(readerOf("1.25 ")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("1e2 "))
                                           .compareTo(new BigDecimal("100")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("1E-2 "))
                                           .compareTo(new BigDecimal("0.01")));
  }

  @Test
  public void shouldRejectInvalidPositiveDecimalFastPathInputs() {
    JsParserException leadingZero =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("01 ")));
    Assertions.assertTrue(leadingZero.getMessage()
                                     .contains(ParserErrors.LEADING_ZERO));

    JsParserException unknownDigit =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("1a ")));
    Assertions.assertTrue(unknownDigit.getMessage()
                                      .contains(ParserErrors.UNKNOWN_DIGIT)
                            || unknownDigit.getMessage()
                                           .contains("Character"),
                          unknownDigit.getMessage());

    Assertions.assertEquals(new BigDecimal("1"),
                            NumberConverter.deserializeDecimal(readerOf("+1 ")));

    JsParserException plusWithoutDigits =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("+ ")));
    Assertions.assertTrue(plusWithoutDigits.getMessage()
                                           .contains(ParserErrors.DIGIT_NOT_FOUND));
  }

  @Test
  public void shouldParsePositiveDecimalContainingZeroAndNineDigits() throws JsParserException {
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("10 "))
                                           .compareTo(new BigDecimal("10")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("19 "))
                                           .compareTo(new BigDecimal("19")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("90.1 "))
                                           .compareTo(new BigDecimal("90.1")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("109e1 "))
                                           .compareTo(new BigDecimal("1090")));
  }

  @Test
  public void shouldRejectPositiveDecimalStartingWithSeparatorOrExponent() {
    JsParserException startsWithDot =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf(".1,")));
    Assertions.assertTrue(startsWithDot.getMessage()
                                       .contains(ParserErrors.DIGIT_NOT_FOUND));

    JsParserException startsWithLowerExponent =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("e1,")));
    Assertions.assertTrue(startsWithLowerExponent.getMessage()
                                                 .contains(ParserErrors.DIGIT_NOT_FOUND));

    JsParserException startsWithUpperExponent =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("E1,")));
    Assertions.assertTrue(startsWithUpperExponent.getMessage()
                                                 .contains(ParserErrors.DIGIT_NOT_FOUND));
  }

  @Test
  public void shouldExerciseFastPathWithNineDigitAndTrailingWhitespaceInDecimal() throws JsParserException {
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("19,"))
                                           .compareTo(new BigDecimal("19")));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("0,"))
                                           .compareTo(BigDecimal.ZERO));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("1 \t,"))
                                           .compareTo(BigDecimal.ONE));
    Assertions.assertEquals(0,
                            NumberConverter.deserializeDecimal(readerOf("0 ,"))
                                           .compareTo(BigDecimal.ZERO));
  }

  @Test
  public void shouldRejectFastPathLeadingZeroAndUnknownDigitInDecimal() {
    JsParserException leadingZero =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("01,")));
    Assertions.assertTrue(leadingZero.getMessage()
                                     .contains(ParserErrors.LEADING_ZERO));

    JsParserException unknownDigit =
        Assertions.assertThrows(JsParserException.class,
                                () -> NumberConverter.deserializeDecimal(readerOf("1/,")));
    Assertions.assertTrue(unknownDigit.getMessage()
                                      .contains(ParserErrors.UNKNOWN_DIGIT));
  }
}
