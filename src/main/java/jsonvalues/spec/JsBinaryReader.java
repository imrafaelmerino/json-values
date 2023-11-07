package jsonvalues.spec;

import jsonvalues.JsBinary;

import java.util.Base64;


final class JsBinaryReader extends AbstractReader {
    @Override
    JsBinary value(final JsReader reader) throws JsParserException {
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
