package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

class JsBinarySuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {

    final Function<byte[], Optional<JsError>> predicate;

    JsBinarySuchThatSpec(final boolean nullable,
                         final Function<byte[], Optional<JsError>> predicate
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsBinarySuchThatSpec(true,
                                        predicate
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBinarySuchThat(predicate,
                                                       nullable
        );
    }

    @Override
    public Optional<JsError> test(final JsValue value) {
        Optional<JsError> error = Functions.testElem(JsValue::isBinary,
                                                     BINARY_EXPECTED,
                                                     nullable
                                           )
                                           .apply(value);

        return error.isPresent() || value.isNull() ?
               error :
               predicate.apply(value.toJsBinary().value);
    }
}
