package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import jsonvalues.JsBinary;
import java.io.IOException;


final class JsBinaryParser extends AbstractParser {
    @Override
    JsBinary value(final JsonReader<?> reader){
        try {
            return JsBinary.of(reader.readBase64());
        } catch (IOException e) {
            throw new JsParserException(e.getMessage());
        }
    }




}
