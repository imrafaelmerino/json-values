package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.List;

final class JsArraySpecReader {
    private final List<JsSpecParser> parsers;

    JsArraySpecReader(final List<JsSpecParser> parsers) {
        this.parsers = parsers;
    }

    JsValue nullOrArray(final JsReader reader) throws IOException {

        return reader.wasNull() ?
                JsNull.NULL :
                array(reader);

    }


    JsArray array(final JsReader reader) throws IOException {
        if (reader.last() != '[')
            throw JsParserException.reasonAt(ParserErrors.EXPECTING_FOR_LIST_START,
                                             reader.getPositionInStream()
                                            );
        reader.getNextToken();
        if (reader.last() == ']') return JsArray.empty();
        JsArray buffer = JsArray.empty();
        int i = 0;
        buffer = buffer.append(parsers.get(i)
                                      .parse(reader));
        while (reader.getNextToken() == ',') {
            i = i + 1;
            reader.getNextToken();
            buffer = buffer.append(parsers.get(i)
                                          .parse(reader));

        }
        reader.checkArrayEnd();
        return buffer;

    }


}
