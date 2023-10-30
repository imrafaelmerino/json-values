package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfInstant extends AbstractMap implements JsSpec {
    JsMapOfInstant(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfInstant(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfInstant(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value
                              ) {
        return test(path,
                    value,
                    it -> !it.isInstant(),
                    ERROR_CODE.INSTANT_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("string"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }


}
