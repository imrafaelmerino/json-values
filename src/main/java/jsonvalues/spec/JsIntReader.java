package jsonvalues.spec;

import jsonvalues.JsInt;

import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntReader extends AbstractReader {
    @Override
    JsInt value(final JsonReader reader) throws JsParserException {
        return JsInt.of(NumberConverter.deserializeInt(reader));
    }

    JsInt valueSuchThat(final JsonReader reader,
                        final IntFunction<Optional<JsError>> fn
                       ) throws JsParserException {
        int value = NumberConverter.deserializeInt(reader);
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return JsInt.of(value);
        throw JsParserException.create(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                       reader.getCurrentIndex(),
                                       false
                                      );
    }


}
