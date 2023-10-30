package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfBigInt extends AbstractMap implements JsSpec {
    JsMapOfBigInt(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBigInt(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBigInt(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value
                              ) {
        return test(path, value, it -> !it.isIntegral(), ERROR_CODE.INTEGRAL_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of("biginteger"));
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", schema);
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }


}
