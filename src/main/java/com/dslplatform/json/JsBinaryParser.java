package com.dslplatform.json;

import jsonvalues.JsBinary;

import java.io.IOException;
import java.util.Base64;


final class JsBinaryParser extends AbstractParser {
    @Override
    JsBinary value(final JsonReader<?> reader) throws IOException {
        try {
            byte[] bytes = Base64.getDecoder().decode(reader.readString());
            return JsBinary.of(bytes);
        } catch (IllegalArgumentException  e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
    }


}
