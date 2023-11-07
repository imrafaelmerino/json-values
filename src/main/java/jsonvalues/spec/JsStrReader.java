package jsonvalues.spec;

import jsonvalues.JsStr;

import java.util.Optional;
import java.util.function.Function;

final class JsStrReader extends AbstractReader {
    @Override
    JsStr value(final JsReader reader) throws JsParserException {
        return JsStr.of(reader.readString());
    }


    JsStr valueSuchThat(final JsReader reader,
                        final Function<String, Optional<JsError>> fn
                       ) throws JsParserException {
        String value = reader.readString();
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return JsStr.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

}
