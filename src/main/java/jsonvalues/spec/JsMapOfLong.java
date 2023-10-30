package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;


final class JsMapOfLong extends AbstractMap implements JsSpec {
     JsMapOfLong(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfLong(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfLong(nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value) {
       return test(path,value,it -> !it.isLong() && !it.isInt(),ERROR_CODE.LONG_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("long"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;
    }


}
