package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfStr extends AbstractMap implements JsSpec {
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
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isStr(),ERROR_CODE.STRING_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("string"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;
    }


}
