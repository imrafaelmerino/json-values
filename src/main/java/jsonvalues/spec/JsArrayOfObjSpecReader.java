package jsonvalues.spec;

import java.util.Objects;

final class JsArrayOfObjSpecReader extends JsArrayReader {
    JsArrayOfObjSpecReader(final JsObjSpecReader parser) {
        super(Objects.requireNonNull(parser));
    }
}
