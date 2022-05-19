package com.dslplatform.json;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class JsArrayOfBoolParser extends JsArrayParser {
    JsArrayOfBoolParser(final JsBoolParser parser) {
        super(requireNonNull(parser));
    }
}
