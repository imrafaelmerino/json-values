package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterNumberAndDecimalStreamingFuzzTest {

  @Test
  public void shouldMatchByteModeForDeserializeNumberAcrossFragmentedStreamingBoundaries() {
    List<String> cases = List.of("0",
                                 "-0",
                                 "1",
                                 "-1",
                                 "01",
                                 "-01",
                                 "+1",
                                 ".1",
                                 "1.",
                                 "1.0",
                                 "-1.0",
                                 "1e100",
                                 "1e-100",
                                 "-1e-100",
                                 "1e",
                                 "1e-",
                                 "1e+",
                                 "1e+1",
                                 "99999999999999999999999999999999999999999999");

    for (String value : cases) {
      Outcome bytesOutcome = parseNumberFromBytes(value);
      for (int bufferSize = 1; bufferSize <= 8; bufferSize++) {
        final int currentBuffer = bufferSize;
        Outcome streamOutcome = parseNumberFromStream(value,
                                                      currentBuffer);
        Assertions.assertEquals(bytesOutcome.ok,
                                streamOutcome.ok,
                                () -> "Mismatch success/failure for number `" + value + "` with buffer " + currentBuffer + ". bytes=" + bytesOutcome + ", stream=" + streamOutcome);
        if (bytesOutcome.ok) {
          Assertions.assertEquals(bytesOutcome.value,
                                  streamOutcome.value,
                                  () -> "Mismatch parsed number for `" + value + "` with buffer " + currentBuffer);
        } else {
          Assertions.assertEquals(bytesOutcome.error,
                                  streamOutcome.error,
                                  () -> "Mismatch error for number `" + value + "` with buffer " + currentBuffer);
        }
      }
    }
  }

  @Test
  public void shouldMatchByteModeForDeserializeDecimalAcrossFragmentedStreamingBoundaries() {
    List<String> cases = List.of("0",
                                 "-0",
                                 "1",
                                 "-1",
                                 "01",
                                 "-01",
                                 "+1",
                                 ".1",
                                 "1.",
                                 "1.0",
                                 "-1.0",
                                 "1e100",
                                 "1e-100",
                                 "-1e-100",
                                 "1e",
                                 "1e-",
                                 "1e+",
                                 "1e+1",
                                 "0.0000000000000000000000000000000001");

    for (String value : cases) {
      Outcome bytesOutcome = parseDecimalFromBytes(value);
      for (int bufferSize = 1; bufferSize <= 8; bufferSize++) {
        final int currentBuffer = bufferSize;
        Outcome streamOutcome = parseDecimalFromStream(value,
                                                       currentBuffer);
        Assertions.assertEquals(bytesOutcome.ok,
                                streamOutcome.ok,
                                () -> "Mismatch success/failure for decimal `" + value + "` with buffer " + currentBuffer + ". bytes=" + bytesOutcome + ", stream=" + streamOutcome);
        if (bytesOutcome.ok) {
          Assertions.assertEquals(bytesOutcome.value,
                                  streamOutcome.value,
                                  () -> "Mismatch parsed decimal for `" + value + "` with buffer " + currentBuffer);
        } else {
          Assertions.assertEquals(bytesOutcome.error,
                                  streamOutcome.error,
                                  () -> "Mismatch error for decimal `" + value + "` with buffer " + currentBuffer);
        }
      }
    }
  }

  private static Outcome parseNumberFromBytes(String value) {
    try {
      JsIO io = JsIO.INSTANCE;
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(payload);
      reader.readNextToken();
      Number parsed = NumberConverter.deserializeNumber(reader);
      return new Outcome(true,
                         canonicalNumber(parsed),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome parseNumberFromStream(String value,
                                               int bufferSize) {
    try {
      JsIO io = JsIO.INSTANCE;
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(new ByteArrayInputStream(payload),
                                        new byte[bufferSize]);
      reader.readNextToken();
      Number parsed = NumberConverter.deserializeNumber(reader);
      return new Outcome(true,
                         canonicalNumber(parsed),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome parseDecimalFromBytes(String value) {
    try {
      JsIO io = JsIO.INSTANCE;
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(payload);
      reader.readNextToken();
      BigDecimal parsed = NumberConverter.deserializeDecimal(reader);
      return new Outcome(true,
                         parsed.toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome parseDecimalFromStream(String value,
                                                int bufferSize) {
    try {
      JsIO io = JsIO.INSTANCE;
      byte[] payload = value.getBytes(StandardCharsets.UTF_8);
      DslJsReader reader = io.newReader(new ByteArrayInputStream(payload),
                                        new byte[bufferSize]);
      reader.readNextToken();
      BigDecimal parsed = NumberConverter.deserializeDecimal(reader);
      return new Outcome(true,
                         parsed.toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static String canonicalNumber(Number number) {
    if (number instanceof BigDecimal bd) {
      return "BigDecimal:" + bd.toString();
    }
    return number.getClass()
                 .getSimpleName() + ":" + number.toString();
  }

  private static String withoutPosition(String message) {
    return message.replaceAll("\\. Current parser position is \\d+$",
                              "");
  }

  private record Outcome(boolean ok,
                         String value,
                         String error) {
  }
}
