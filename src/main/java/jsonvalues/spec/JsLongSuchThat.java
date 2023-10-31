package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

final class JsLongSuchThat extends AbstractNullable implements JsValuePredicate, AvroSpec {
    final LongFunction<Optional<JsError>> predicate;

    JsLongSuchThat(final LongFunction<Optional<JsError>> predicate,
                   final boolean nullable
                  ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsLongSuchThat(
                predicate,
                true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofLongSuchThat(predicate,
                                                     nullable
                                                    );
    }

    @Override
    public JsValue toAvroSchema() {
        return nullable ? JsArray.of("null", "long") : JsStr.of("long");
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isLong,
                                                           LONG_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsLong().value);
    }
}
