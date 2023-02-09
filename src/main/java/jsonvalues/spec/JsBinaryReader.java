package jsonvalues.spec;

import jsonvalues.JsBinary;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.util.Base64;


final class JsBinaryReader extends AbstractReader {
    @Override
    JsBinary value(final JsReader reader) throws IOException {
        try {
            byte[] bytes = Base64.getDecoder().decode(reader.readString());
            return JsBinary.of(bytes);
        } catch (IllegalArgumentException e) {
            throw JsParserException.reasonAt(e.getMessage(),
                                             reader.getPositionInStream()
                                            );
        }
    }


}
