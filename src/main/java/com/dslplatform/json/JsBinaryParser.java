package com.dslplatform.json;

import jsonvalues.JsBinary;

import java.util.Base64;


final class JsBinaryParser extends AbstractParser {
    @Override
    JsBinary value(final JsonReader<?> reader) {
        try {
            byte[] bytes = Base64.getDecoder().decode(reader.readString());
            return JsBinary.of(bytes);
        } catch (Exception e) {
            throw new JsParserException(e.getMessage());
        }
    }


}
