package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfInstant extends AbstractMap implements JsSpec, AvroSpec {
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
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {
        return test(path,
                    value,
                    it -> !it.isInstant(),
                    ERROR_CODE.INSTANT_EXPECTED);
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
                                   "values", JsStr.of("string"),
                                  "logicalType",JsStr.of("iso-8601")
                                  );
        return nullable ? JsArray.of(JsStr.of("null"), mapSchema) : mapSchema;

    }


}
