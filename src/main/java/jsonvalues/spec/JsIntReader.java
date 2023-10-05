package jsonvalues.spec;

import jsonvalues.JsInt;
import jsonvalues.JsParserException;

import java.util.Optional;
import java.util.function.IntFunction;

final class JsIntReader extends AbstractReader {
    @Override
    JsInt value(final JsReader reader) throws JsParserException {
        return JsInt.of(NumberConverter.deserializeInt(reader));
    }

    JsInt valueSuchThat(final JsReader reader,
                        final IntFunction<Optional<JsError>> fn
                       ) throws JsParserException {
        int value = NumberConverter.deserializeInt(reader);
        Optional<JsError> result = fn.apply(value);
        if (!result.isPresent()) return JsInt.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }


}
