package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;

import java.util.List;

final class JsArraySpecReader {
    private final List<JsSpecParser> parsers;

    JsArraySpecReader(final List<JsSpecParser> parsers) {
        this.parsers = parsers;
    }

    JsValue nullOrArray(final JsReader reader) throws JsParserException {

        return reader.wasNull() ?
                JsNull.NULL :
                array(reader);

    }


    JsArray array(final JsReader reader) throws JsParserException {
        if (reader.last() != '[')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_LIST_START,
                                             reader.getPositionInStream()
                                            );
        reader.readNextToken();
        if (reader.last() == ']') return JsArray.empty();
        JsArray buffer = JsArray.empty();
        int i = 0;
        buffer = buffer.append(parsers.get(i)
                                      .parse(reader));
        while (reader.readNextToken() == ',') {
            i = i + 1;
            reader.readNextToken();
            buffer = buffer.append(parsers.get(i)
                                          .parse(reader));

        }
        reader.checkArrayEnd();
        return buffer;

    }


}
