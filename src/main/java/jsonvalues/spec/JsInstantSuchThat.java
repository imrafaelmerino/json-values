package jsonvalues.spec;

import jsonvalues.*;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.INSTANT_EXPECTED;

final class JsInstantSuchThat extends AbstractNullable implements JsValuePredicate {

    final Function<Instant, Optional<JsError>> predicate;

    JsInstantSuchThat(final Function<Instant, Optional<JsError>> predicate,
                      final boolean nullable
                     ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsInstantSuchThat(predicate,
                                     true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofInstantSuchThat(predicate,
                                                        nullable
                                                       );
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

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isInstant,
                                                           INSTANT_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsInstant().value);
    }
}
