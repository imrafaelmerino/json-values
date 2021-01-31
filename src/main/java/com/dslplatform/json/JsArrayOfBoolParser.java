package com.dslplatform.json;

import java.util.Objects;

final class JsArrayOfBoolParser extends JsArrayParser {
    JsArrayOfBoolParser(final JsBoolParser parser) {
        super(Objects.requireNonNull(parser));
    }
}
