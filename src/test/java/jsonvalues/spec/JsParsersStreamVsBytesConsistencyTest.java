package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsParsersStreamVsBytesConsistencyTest {

  @Test
  public void shouldBehaveConsistentlyBetweenBytesAndStreamForNumericInputs() {
    JsObjSpecParser intParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                JsSpecs.integer()));
    JsObjSpecParser longParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                 JsSpecs.longInteger()));
    JsObjSpecParser doubleParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                   JsSpecs.doubleNumber()));
    JsObjSpecParser decimalParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                    JsSpecs.decimal()));

    List<String> numbers = new ArrayList<>();
    numbers.add("0");
    numbers.add("-0");
    numbers.add("1");
    numbers.add("-1");
    numbers.add("+1");
    numbers.add("01");
    numbers.add("-01");
    numbers.add("2147483647");
    numbers.add("2147483648");
    numbers.add("-2147483648");
    numbers.add("-2147483649");
    numbers.add("9223372036854775807");
    numbers.add("9223372036854775808");
    numbers.add("-9223372036854775808");
    numbers.add("-9223372036854775809");
    numbers.add("1.0");
    numbers.add("-1.0");
    numbers.add(".1");
    numbers.add("1.");
    numbers.add("1e10");
    numbers.add("1E-10");
    numbers.add("-1e+10");
    numbers.add("1e");
    numbers.add("1e-");
    numbers.add("-");
    numbers.add("+");
    numbers.add("99999999999999999999999999999999999999999999");

    for (String number : numbers) {
      assertSameOutcome(intParser,
                        number);
      assertSameOutcome(longParser,
                        number);
      assertSameOutcome(doubleParser,
                        number);
      assertSameOutcome(decimalParser,
                        number);
    }
  }

  private static void assertSameOutcome(JsObjSpecParser parser,
                                        String number
                                       ) {
    String json = "{\"x\":" + number + "}";
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

    Outcome fromBytes = outcome(() -> parser.parse(bytes));
    Outcome fromStream = outcome(() -> parser.parse(new ByteArrayInputStream(bytes)));

    Assertions.assertEquals(fromBytes.ok,
                            fromStream.ok,
                            () -> "Different success/failure for input `" + number + "`");
    if (!fromBytes.ok) {
      Assertions.assertEquals(fromBytes.reason,
                              fromStream.reason,
                              () -> "Different parser reason for input `" + number + "`");
    }
  }

  private static Outcome outcome(Runnable parse) {
    try {
      parse.run();
      return new Outcome(true,
                         "");
    } catch (JsParserException e) {
      return new Outcome(false,
                         withoutPosition(e.getMessage()));
    }
  }

  private static String withoutPosition(String message) {
    return message.replaceAll("\\. Current parser position is \\d+$",
                              "");
  }

  private record Outcome(boolean ok, String reason) {
  }
}
