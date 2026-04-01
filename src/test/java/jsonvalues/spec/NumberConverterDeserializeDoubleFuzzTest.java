package jsonvalues.spec;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterDeserializeDoubleFuzzTest {

  private static double parseWithLowPrecision(String number) throws JsParserException {
    JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
    DslJsReader reader = io.newReader(number.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    return NumberConverter.deserializeDouble(reader);
  }

  private static void assertSameAsJdkParser(String number) throws JsParserException {
    double expected = Double.parseDouble(number);
    double actual = parseWithLowPrecision(number);

    if (Double.isNaN(expected)) {
      Assertions.assertTrue(Double.isNaN(actual),
                            number);
      return;
    }
    if (Double.isInfinite(expected)) {
      Assertions.assertEquals(expected,
                              actual,
                              number);
      return;
    }
    if (expected == 0d) {
      Assertions.assertEquals(Double.doubleToRawLongBits(expected),
                              Double.doubleToRawLongBits(actual),
                              number);
      return;
    }
    double tolerance = Math.ulp(expected) * 2d;
    Assertions.assertEquals(expected,
                            actual,
                            tolerance,
                            number);
  }

  @Test
  public void shouldMatchJdkParserForDirectedExtremeExponentCases() throws JsParserException {
    List<String> mantissas = List.of("1",
                                     "1.5",
                                     "1.0000000000000002",
                                     "9.999999999999999",
                                     "2.2250738585072014");
    int[] exponents = new int[]{-300, -299, -250, -200, -150, -120, -101, -100, -99, -50, -20, 0, 20, 50, 99, 100, 101, 120, 150, 200, 250, 299, 300};

    for (String mantissa : mantissas) {
      for (int exponent : exponents) {
        assertSameAsJdkParser(mantissa + "e" + exponent);
        assertSameAsJdkParser("-" + mantissa + "e" + exponent);
      }
    }
  }

  @Test
  public void shouldMatchJdkParserForRoundingEdgeCases() throws JsParserException {
    List<String> values = List.of("2.2250738585072011e-308",
                                  "2.2250738585072012e-308",
                                  "2.2250738585072013e-308",
                                  "2.2250738585072014e-308",
                                  "4.9406564584124653e-324",
                                  "4.9406564584124654e-324",
                                  "1.7976931348623157e308",
                                  "1.7976931348623158e308",
                                  "1.0000000000000001e23",
                                  "1.0000000000000002e23",
                                  "9.999999999999999e22");
    for (String value : values) {
      assertSameAsJdkParser(value);
      assertSameAsJdkParser("-" + value);
    }
  }
}
