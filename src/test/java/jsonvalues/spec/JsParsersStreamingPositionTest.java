package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsParsersStreamingPositionTest {

  private static final Pattern POSITION_PATTERN = Pattern.compile("Current parser position is (\\d+)");

  @Test
  public void shouldReportAbsolutePositionForUnexpectedEndOfJsonInStream() {
    JsObjSpecParser parser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                             JsSpecs.str()));

    String malformed = "{\"x\":\"" + "a".repeat(6000);
    byte[] bytes = malformed.getBytes(StandardCharsets.UTF_8);

    JsParserException exception =
        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse(new ByteArrayInputStream(bytes)));

    Matcher matcher = POSITION_PATTERN.matcher(exception.getMessage());
    Assertions.assertTrue(matcher.find(),
                          "Expected parser position in error message: " + exception.getMessage());

    long reportedPosition = Long.parseLong(matcher.group(1));
    Assertions.assertTrue(reportedPosition >= bytes.length - 2,
                          "Expected absolute stream position close to input length. Reported: " + reportedPosition);
  }
}
