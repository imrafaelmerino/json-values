package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

class JsBinarySuchThatSpec extends AbstractNullableSpec implements JsValuePredicate {

    final Function<byte[], Optional<JsError>> predicate;

    JsBinarySuchThatSpec(final Function<byte[], Optional<JsError>> predicate,
                         final boolean nullable
    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsBinarySuchThatSpec(predicate,
                                        true
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofBinarySuchThat(predicate,
                                                       nullable
        );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
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
