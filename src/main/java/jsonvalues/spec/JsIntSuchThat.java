package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

final class JsIntSuchThat extends AbstractNullable implements JsValuePredicate, AvroSpec {
    final IntFunction<Optional<JsError>> predicate;

    JsIntSuchThat(final IntFunction<Optional<JsError>> predicate,
                  final boolean nullable
                 ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsIntSuchThat(predicate,
                                 true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofIntSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public JsValue toAvroSchema() {
        return nullable ? JsArray.of("null", "int") : JsStr.of("int");
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isInt,
                                                           INT_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsInt().value);
    }
}
