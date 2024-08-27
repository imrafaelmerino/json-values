package jsonvalues.spec;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import jsonvalues.JsInstant;

final class JsInstantReader extends AbstractReader {

  @Override
  JsInstant value(final DslJsReader reader) throws JsParserException {
    try {
      return JsInstant.of(Instant.from(ISO_INSTANT.parse(reader.readString())));
    } catch (DateTimeParseException e) {
      throw JsParserException.reasonAt(e.getMessage(),
                                       reader.getPositionInStream()
                                      );
    }

  }

}
