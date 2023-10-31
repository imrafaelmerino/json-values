package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfStr extends AbstractMap implements JsSpec , AvroSpec {
     JsMapOfStr(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfStr(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfString(nullable);
    }

    @Override
    public List<SpecError> test(JsPath path,
                                JsValue value) {
       return test(path,value,it -> !it.isStr(),ERROR_CODE.STRING_EXPECTED);
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("string"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;
    }


}
