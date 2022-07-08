package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

class JsIntSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final IntFunction<Optional<Pair<JsValue,ERROR_CODE>>> predicate;

    JsIntSuchThatSpec(final IntFunction<Optional<Pair<JsValue,ERROR_CODE>>> predicate,
                      final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsIntSuchThatSpec(predicate,
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
    public Optional<Pair<JsValue,ERROR_CODE>> testValue(final JsValue value) {
        final Optional<Pair<JsValue,ERROR_CODE>> error = Functions.testElem(JsValue::isInt,
                                                                INT_EXPECTED,
                                                                nullable
                                                 )
                                                      .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsInt().value);
    }
}
