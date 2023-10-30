package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfBool extends AbstractMap implements JsSpec {
     JsMapOfBool(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBool(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBool(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isBool(),ERROR_CODE.BOOLEAN_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("boolean"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }

}
