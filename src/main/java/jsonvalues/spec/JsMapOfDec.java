package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfDec extends AbstractMap implements JsSpec {
     JsMapOfDec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfDec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfDecimal(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isDecimal(),ERROR_CODE.DECIMAL_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("double"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }

}
