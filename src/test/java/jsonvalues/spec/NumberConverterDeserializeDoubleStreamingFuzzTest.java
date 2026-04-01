package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterDeserializeDoubleStreamingFuzzTest {

  @Test
  public void shouldMatchByteModeAcrossFragmentedStreamingBoundaries() {
    List<String> cases = List.of("0",
                                 "-0",
                                 "1",
                                 "-1",
                                 "1.0",
                                 "-1.0",
                                 "1e-100",
                                 "-1e-100",
                                 "1e100",
                                 "-1e100",
                                 "1.5e100",
                                 "-1.5e100",
                                 "9.999999999999999e0",
                                 "-9.999999999999999e0",
                                 "1.0000000000000002e23",
                                 "2.2250738585072014e-308",
                                 "4.9406564584124654e-324",
                                 "1.7976931348623157e308",
                                 "1.7976931348623158e308",
                                 "1e",
                                 "1e-",
                                 "1.",
                                 ".1",
                                 "+1");

    for (String value : cases) {
      Outcome bytesOutcome = parseFromBytes(value);
      for (int bufferSize = 1; bufferSize <= 8; bufferSize++) {
        final int currentBuffer = bufferSize;
        Outcome streamOutcome = parseFromStream(value,
                                                bufferSize);
        Assertions.assertEquals(bytesOutcome.ok,
                                streamOutcome.ok,
                                () -> "Mismatch success/failure for value `" + value + "` with buffer " + currentBuffer + ". bytes=" + bytesOutcome + ", stream=" + streamOutcome);
        if (bytesOutcome.ok) {
          Assertions.assertEquals(bytesOutcome.value,
                                  streamOutcome.value,
                                  () -> "Mismatch value for `" + value + "` with buffer " + currentBuffer);
        } else {
          Assertions.assertEquals(bytesOutcome.error,
                                  streamOutcome.error,
                                  () -> "Mismatch error for `" + value + "` with buffer " + currentBuffer);
        }
      }
    }
  }

  private static Outcome parseFromBytes(String value) {
    try {
      JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(payload);
      reader.readNextToken();
      return new Outcome(true,
                         NumberConverter.deserializeDouble(reader),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome parseFromStream(String value,
                                         int bufferSize) {
    try {
      JsIO io = new JsIO(new Settings().doublePrecision(DslJsReader.DoublePrecision.LOW));
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(new ByteArrayInputStream(payload),
                                        new byte[bufferSize]);
      reader.readNextToken();
      return new Outcome(true,
                         NumberConverter.deserializeDouble(reader),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static String withoutPosition(String message) {
    return message.replaceAll("\\. Current parser position is \\d+$",
                              "");
  }

  private record Outcome(boolean ok,
                         Double value,
                         String error) {
  }
}
