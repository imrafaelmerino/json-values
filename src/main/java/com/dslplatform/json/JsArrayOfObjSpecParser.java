package com.dslplatform.json;

import java.util.Objects;

final class JsArrayOfObjSpecParser extends JsArrayParser {
    JsArrayOfObjSpecParser(final JsObjSpecParser parser) {
        super(Objects.requireNonNull(parser));
    }
}
