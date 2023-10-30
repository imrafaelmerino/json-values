package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfInt extends AbstractMap implements JsSpec {
     JsMapOfInt(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfInt(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfInt(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isInt(),ERROR_CODE.INT_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("int"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }


}
