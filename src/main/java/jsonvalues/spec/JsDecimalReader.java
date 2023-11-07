package jsonvalues.spec;

import jsonvalues.JsBigDec;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;


final class JsDecimalReader extends AbstractReader {

    @Override
    JsBigDec value(final JsReader reader) throws JsParserException {
        return JsBigDec.of(NumberConverter.deserializeDecimal(reader));
    }

    JsBigDec valueSuchThat(final JsReader reader,
                           final Function<BigDecimal, Optional<JsError>> fn
                          ) throws JsParserException {
        final BigDecimal value = NumberConverter.deserializeDecimal(reader);
        final Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return JsBigDec.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }


}
