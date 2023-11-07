package jsonvalues.spec;

import jsonvalues.JsBinary;

import java.util.Base64;


final class JsFixedBinaryReader extends AbstractReader {

    final int size;

    JsFixedBinaryReader(int size) {
        this.size = size;
    }

    @Override
    JsBinary value(final JsReader reader) throws JsParserException {
        try {
            byte[] bytes = Base64.getDecoder().decode(reader.readString());
            if (bytes.length != size) {
                throw JsParserException.reasonAt(ParserErrors.INVALID_FIXED_BINARY_SIZE.formatted(size, bytes.length),
                                                 reader.getPositionInStream());
            }
            return JsBinary.of(bytes);
        } catch (IllegalArgumentException e) {
            throw JsParserException.reasonAt(e.getMessage(),
                                             reader.getPositionInStream()
                                            );
        }
    }


}
