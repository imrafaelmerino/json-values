package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import jsonvalues.JsNumber;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

class JsNumberSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {
    final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate;

    JsNumberSuchThatSpec(final Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                         final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsNumberSuchThatSpec(predicate,
                                        true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofNumberSuchThat(predicate,
                                                       nullable
        );
    }

    @Override
    public Optional<Pair<JsValue, ERROR_CODE>> test(final JsValue value) {
        final Optional<Pair<JsValue, ERROR_CODE>> error = Functions.testElem(JsValue::isNumber,
                                                                             NUMBER_EXPECTED,
                                                                             nullable
                                                                   )
                                                                   .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsNumber());
    }
}
