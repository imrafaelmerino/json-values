package jsonvalues.spec;

import jsonvalues.JsParserException;
import jsonvalues.JsStr;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

final class JsStrReader extends AbstractReader {
    @Override
    JsStr value(final JsReader reader) throws IOException {
        return JsStr.of(reader.readString());
    }


    JsStr valueSuchThat(final JsReader reader,
                        final Function<String, Optional<JsError>> fn
                       ) throws IOException {
        String value = reader.readString();
        Optional<JsError> result = fn.apply(value);
        if (!result.isPresent()) return JsStr.of(value);
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

}
