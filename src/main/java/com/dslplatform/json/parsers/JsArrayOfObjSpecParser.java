package com.dslplatform.json.parsers;

import java.util.Objects;

final class JsArrayOfObjSpecParser extends JsArrayParser {
    JsArrayOfObjSpecParser(final JsObjSpecParser parser) {
        super(Objects.requireNonNull(parser));
    }
}
