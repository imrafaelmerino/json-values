package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

final class JsInstantSpec extends AbstractNullable implements JsValuePredicate {
    JsObj mapSchema = JsObj.of("type", JsStr.of("long"), "logicalType",
                               JsStr.of("timestamp-millis"));


    JsInstantSpec(final boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsInstantSpec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstant(nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(JsValue::isInstant,
                                  INSTANT_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }

    @Override
    public JsValue toAvro() {
        JsObj mapSchema = JsObj.of("type",
                                   JsStr.of("long"),
                                   "logicalType",
                                   JsStr.of("timestamp-millis")
                                  );
        return nullable ? JsArray.of(JsNull.NULL, mapSchema) : mapSchema;
    }


}
