package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsParserException;
import jsonvalues.JsValue;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

abstract class JsArrayReader extends AbstractReader {

    static final JsArray EMPTY = JsArray.empty();
    private final AbstractReader parser;

    JsArrayReader(final AbstractReader parser) {
        this.parser = parser;
    }

    JsValue nullOrArray(final JsReader reader,
                        int min,
                        int max
                       ) throws IOException {

        return reader.wasNull() ?
                JsNull.NULL :
                array(reader,
                      min,
                      max
                     );
    }

    JsArray array(final JsReader reader,
                  int min,
                  int max
                 ) throws IOException {
        if (checkIfEmpty(isEmptyArray(reader),
                         min,
                         reader.getPositionInStream()
                        )) return EMPTY;
        JsArray buffer = EMPTY.append(parser.value(reader));
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(parser.value(reader));
            checkSize(buffer.size() > max,
                      ParserErrors.TOO_LONG_ARRAY.apply(max),
                      reader.getPositionInStream()
                     );

        }
        checkSize(buffer.size() < min,
                  ParserErrors.TOO_SHORT_ARRAY.apply(min),
                  reader.getPositionInStream()
                 );

        reader.checkArrayEnd();
        return buffer;
    }

    private boolean checkIfEmpty(boolean error,
                                 int min,
                                 long reader
                                ) {
        if (error) {
            checkSize(min > 0,
                      ParserErrors.EMPTY_ARRAY.apply(min),
                      reader
                     );
            return true;
        }
        return false;
    }

    @Override
    public JsArray value(final JsReader reader) throws IOException {
        if (isEmptyArray(reader)) return EMPTY;
        JsArray buffer = EMPTY.append(parser.value(reader));
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(parser.value(reader));
        }
        reader.checkArrayEnd();
        return buffer;

    }




    private boolean isEmptyArray(final JsReader reader) throws IOException {
        checkSize(reader.last() != '[',
                  ParserErrors.EXPECTING_FOR_LIST_START,
                  reader.getPositionInStream()
                 );
        reader.getNextToken();
        return reader.last() == ']';
    }

    public JsValue nullOrArraySuchThat(final JsReader reader,
                                       final Function<JsArray, Optional<JsError>> fn
                                      ) throws IOException {

        return reader.wasNull() ?
                JsNull.NULL :
                arraySuchThat(reader,
                              fn
                             );

    }


    JsArray arraySuchThat(final JsReader reader,
                          final Function<JsArray, Optional<JsError>> fn
                         ) throws IOException {

        final JsArray array = value(reader);
        final Optional<JsError> result = fn.apply(array);
        if (result.isEmpty()) return array;
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

    JsArray arrayEachSuchThat(final JsReader reader,
                              final IOCallable<JsValue> f,
                              final int min,
                              final int max
                             ) throws IOException {

        if (checkIfEmpty(isEmptyArray(reader),
                         min,
                         reader.getPositionInStream()
                        )) return EMPTY;
        JsArray buffer = EMPTY.append(f.call());
        while (reader.getNextToken() == ',') {
            reader.getNextToken();
            buffer = buffer.append(f.call());
            checkSize(buffer.size() > max,
                      ParserErrors.TOO_LONG_ARRAY.apply(max),
                      reader.getPositionInStream()
                     );
        }
        checkSize(buffer.size() < min,
                  ParserErrors.TOO_SHORT_ARRAY.apply(min),
                  reader.getPositionInStream()
                 );

        reader.checkArrayEnd();
        return buffer;

    }

    private void checkSize(boolean error,
                           String message,
                           long reader
                          ) {
        if (error)
            throw JsParserException.reasonAt(message,
                                             reader
                                            );
    }

    JsValue nullOrArrayEachSuchThat(final JsReader reader,
                                    final IOCallable<JsValue> fn,
                                    final int min,
                                    final int max
                                   ) throws IOException {

        return reader.wasNull() ?
                JsNull.NULL :
                arrayEachSuchThat(reader,
                                  fn,
                                  min,
                                  max
                                 );
    }


}
