package jsonvalues.spec;

import java.util.Base64;
import jsonvalues.JsBinary;


final class JsBinaryReader extends AbstractReader {

  @Override
  JsBinary value(final DslJsReader reader) throws JsParserException {
    try {
      byte[] bytes = Base64.getDecoder()
                           .decode(reader.readString());
      return JsBinary.of(bytes);
    } catch (IllegalArgumentException e) {
      throw JsParserException.reasonAt(e.getMessage(),
                                       reader.getPositionInStream()
                                      );
    }
  }


}
