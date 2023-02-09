package jsonvalues.spec;

import jsonvalues.JsLong;
import jsonvalues.JsParserException;

import java.io.IOException;
import java.util.Optional;
import java.util.function.LongFunction;

final class JsLongReader extends AbstractReader {

    @Override
    JsLong value(final JsReader reader) throws IOException {
        return JsLong.of(NumberConverter.deserializeLong(reader));
    }

    JsLong valueSuchThat(final JsReader reader,
                         final LongFunction<Optional<JsError>> fn
                        ) throws IOException {
        long value = NumberConverter.deserializeLong(reader);
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return JsLong.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

}
