package jsonvalues.spec;


import jsonvalues.JsBool;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;


class JsValueReader extends AbstractReader {
    private JsObjReader objDeserializer;
    private JsArrayOfValueReader arrayDeserializer;
    private JsNumberReader numberDeserializer;

    void setNumberDeserializer(JsNumberReader numberDeserializer) {
        this.numberDeserializer = numberDeserializer;
    }

    void setObjDeserializer(JsObjReader objDeserializer) {
        this.objDeserializer = objDeserializer;
    }

    void setArrayDeserializer(JsArrayOfValueReader arrayDeserializer) {
        this.arrayDeserializer = arrayDeserializer;
    }

    JsValue valueSuchThat(JsReader reader,
                          Function<JsValue, Optional<JsError>> fn

                         ) throws JsParserException {
        JsValue value = value(reader);
        Optional<JsError> result = fn.apply(value);
        if (result.isEmpty()) return value;
        throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result.get()),
                                         reader.getPositionInStream()
                                        );
    }

    @Override
    @SuppressWarnings("FallThrough")
    JsValue value(JsReader reader) throws JsParserException {
        return switch (reader.last()) {
            case 't' -> {
                if (reader.wasTrue()) yield JsBool.TRUE;
                throw JsParserException.reasonAt("true was expected", reader.getCurrentIndex());
            }
            case 'f' -> {
                if (reader.wasFalse()) yield JsBool.FALSE;
                throw JsParserException.reasonAt("false was expected", reader.getCurrentIndex());
            }
            case '"' -> JsStr.of(reader.readString());
            case '{' -> objDeserializer.value(reader);
            case '[' -> arrayDeserializer.nullOrValue(reader);
            default -> numberDeserializer.nullOrValue(reader);
        };
    }


}
