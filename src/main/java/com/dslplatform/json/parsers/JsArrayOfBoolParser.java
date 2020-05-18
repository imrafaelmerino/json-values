package com.dslplatform.json.parsers;

import java.util.Objects;

final class JsArrayOfBoolParser extends JsArrayParser {
    JsArrayOfBoolParser(final JsBoolParser parser) {
        super(Objects.requireNonNull(parser));
    }
}
