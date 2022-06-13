package com.dslplatform.json;

import jsonvalues.JsBinary;

import java.util.Base64;


final class JsBinaryParser extends AbstractParser {
    @Override
    JsBinary value(final JsonReader<?> reader) {
        try {
            byte[] bytes = Base64.getDecoder().decode(reader.readString());
            return JsBinary.of(bytes);
        }
        catch (ParsingException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        }
        catch (JsParserException e){
            throw e;
        }
        catch (Exception e) {
            throw new JsParserException(e,reader.getCurrentIndex());
        }
    }


}
