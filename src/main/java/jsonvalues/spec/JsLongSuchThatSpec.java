package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.LongFunction;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

class JsLongSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final LongFunction<Optional<Pair<JsValue,ERROR_CODE>>> predicate;

    JsLongSuchThatSpec(final LongFunction<Optional<Pair<JsValue,ERROR_CODE>>> predicate,
                       final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsLongSuchThatSpec(
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
    public Optional<Pair<JsValue,ERROR_CODE>> testValue(final JsValue value) {
        final Optional<Pair<JsValue,ERROR_CODE>> error = Functions.testElem(JsValue::isLong,
                                                                LONG_EXPECTED,
                                                                nullable
                                                 )
                                                      .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsLong().value);
    }
}
