package jsonvalues.spec;


abstract class AbstractJsObjReader extends AbstractReader {

  protected boolean isEmptyObj(final DslJsReader reader) throws JsParserException {

    byte last = reader.last();
    if (last != '{') {
      throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_OBJ_START.formatted(((char) last)),
                                       reader.getPositionInStream()
                                      );
    }
    return reader.readNextToken() == '}';
  }

}
