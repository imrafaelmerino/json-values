package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

final class JsArrayOfBoolReader extends JsArrayReader {

  JsArrayOfBoolReader(final JsBoolReader parser) {
    super(requireNonNull(parser));
  }
}
