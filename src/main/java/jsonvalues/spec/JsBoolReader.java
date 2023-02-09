package jsonvalues.spec;

import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;


final class JsBoolReader extends AbstractReader {

    @Override
    JsBool value(final JsReader reader) throws JsParserException {

        if (reader.wasTrue()) return JsBool.TRUE;
        if (reader.wasFalse()) return JsBool.FALSE;
        throw JsParserException.reasonAt(ParserErrors.BOOL_EXPECTED,
                                         reader.getPositionInStream()
                                        );
    }

    JsValue nullOrTrue(final JsReader reader) throws JsParserException {
        return reader.wasNull() ?
                JsNull.NULL :
                True(reader);
    }

    JsBool True(final JsReader reader) throws JsParserException {
        if (reader.wasTrue()) return JsBool.TRUE;
        throw JsParserException.reasonAt(ParserErrors.TRUE_EXPECTED,
                                         reader.getPositionInStream()
                                        );

    }

    JsValue nullOrFalse(final JsReader reader) throws JsParserException {
        return reader.wasNull() ?
                JsNull.NULL :
                False(reader);

    }

    JsBool False(final JsReader reader) throws JsParserException {

        if (reader.wasFalse()) return JsBool.FALSE;
        throw JsParserException.reasonAt(ParserErrors.FALSE_EXPECTED,
                                         reader.getPositionInStream()
                                        );

    }


}
