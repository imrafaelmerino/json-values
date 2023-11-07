package jsonvalues.spec;

import jsonvalues.JsDouble;

import java.util.Optional;
import java.util.function.DoubleFunction;

final class JsDoubleReader extends AbstractReader {

    @Override
    JsDouble value(final JsReader reader) throws JsParserException {
        return JsDouble.of(NumberConverter.deserializeDouble(reader));
    }

    JsDouble valueSuchThat(final JsReader reader,
                           final DoubleFunction<Optional<JsError>> fn
                          ) throws JsParserException {
        double value = NumberConverter.deserializeDouble(reader);
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return JsDouble.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

}
