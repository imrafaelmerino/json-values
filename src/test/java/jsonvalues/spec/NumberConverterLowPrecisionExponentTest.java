package jsonvalues.spec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterLowPrecisionExponentTest {

  @Test
  public void shouldHandleNegativeExponentWithLowPrecision() throws JsParserException {
    JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
    DslJsReader reader = io.newReader("1e-100".getBytes());
    reader.readNextToken();

    double value = NumberConverter.deserializeDouble(reader);

    Assertions.assertEquals(1e-100d,
                            value,
                            0d);
  }

  @Test
  public void shouldKeepFractionPartWithLowPrecisionAndLargeExponent() throws JsParserException {
    JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
    DslJsReader reader = io.newReader("1.5e100".getBytes());
    reader.readNextToken();

    double value = NumberConverter.deserializeDouble(reader);

    Assertions.assertEquals(1.5e100d,
                            value,
                            1e85d);
  }
}
