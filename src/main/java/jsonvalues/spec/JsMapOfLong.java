package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfLong extends AbstractMap implements JsSpec, AvroSpec {
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
    public List<SpecError> test(JsPath path,
                                JsValue value) {
       return test(path,value,it -> !it.isLong() && !it.isInt(),ERROR_CODE.LONG_EXPECTED);
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"), "values", JsStr.of("long"));
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;
    }


}
