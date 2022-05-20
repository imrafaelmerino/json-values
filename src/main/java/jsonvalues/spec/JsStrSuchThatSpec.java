package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

class JsStrSuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {

    final Function<String, Optional<JsError>> predicate;

    JsStrSuchThatSpec(final Function<String, Optional<JsError>> predicate,
                      final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsStrSuchThatSpec(predicate,
                                     true
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStrSuchThat(predicate,
                                                    nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isStr,
                                                           STRING_EXPECTED,
                                                           nullable
                                                 )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsStr().value);
    }
}
