package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterParseDoubleMutationTest {

  private static double parseLow(String number) throws JsParserException {
    JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
    DslJsReader reader = io.newReader(number.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    return NumberConverter.deserializeDouble(reader);
  }

  private static double parseLowStream(String number,
                                       int bufferSize) throws JsParserException {
    JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
    DslJsReader reader = io.newReader(new ByteArrayInputStream(number.getBytes(StandardCharsets.UTF_8)),
                                      new byte[bufferSize]);
    reader.readNextToken();
    return NumberConverter.deserializeDouble(reader);
  }

  @Test
  public void shouldMatchJdkBitPatternForRepresentativeApproximationCases() throws JsParserException {
    List<String> values = List.of("0.1234567890123456",
                                  "7.1234567890123456",
                                  "8.1234567890123456",
                                  "1.2345678901234567e2",
                                  "1.2345678901234567e-2",
                                  "9.999999999999999e0");

    for (String value : values) {
      double expected = Double.parseDouble(value);
      double actual = parseLow(value);
      double tolerance = Math.ulp(expected);
      Assertions.assertEquals(expected,
                              actual,
                              tolerance,
                              value);
    }
  }

  @Test
  public void shouldRejectIncompleteExponentConsistentlyInBytesAndStream() {
    List<String> invalid = List.of("1e",
                                   "1e+",
                                   "1e-",
                                   "-1e",
                                   "-1e+",
                                   "-1e-");

    for (String value : invalid) {
      JsParserException bytes =
          Assertions.assertThrows(JsParserException.class,
                                  () -> parseLow(value));
      JsParserException stream =
          Assertions.assertThrows(JsParserException.class,
                                  () -> parseLowStream(value,
                                                       1));
      String bytesReason = bytes.getMessage()
                                .replaceAll("\\. Current parser position is \\d+$",
                                            "");
      String streamReason = stream.getMessage()
                                  .replaceAll("\\. Current parser position is \\d+$",
                                              "");
      Assertions.assertEquals(bytesReason,
                              streamReason,
                              value);
      Assertions.assertTrue(bytesReason.contains(ParserErrors.NO_EXPONENT_DIGITS)
                            || bytesReason.contains("For input string"),
                            value + " -> " + bytesReason);
    }
  }

  @Test
  public void shouldRejectLeadingDotAndTrailingDotConsistentlyInBytesAndStream() {
    List<String> invalid = List.of(".1",
                                   "1.");

    for (String value : invalid) {
      JsParserException bytes =
          Assertions.assertThrows(JsParserException.class,
                                  () -> parseLow(value));
      JsParserException stream =
          Assertions.assertThrows(JsParserException.class,
                                  () -> parseLowStream(value,
                                                       1));
      String bytesReason = bytes.getMessage()
                                .replaceAll("\\. Current parser position is \\d+$",
                                            "");
      String streamReason = stream.getMessage()
                                  .replaceAll("\\. Current parser position is \\d+$",
                                              "");
      Assertions.assertEquals(bytesReason,
                              streamReason,
                              value);
    }
  }
}
