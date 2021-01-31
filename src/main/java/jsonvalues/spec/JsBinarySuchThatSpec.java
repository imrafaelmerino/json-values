package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.BINARY_EXPECTED;

class JsBinarySuchThatSpec extends AbstractPredicateSpec implements JsValuePredicate {

    final Function<byte[], Optional<Error>> predicate;

    JsBinarySuchThatSpec(final boolean required,
                         final boolean nullable,
                         final Function<byte[], Optional<Error>> predicate
                        ) {
        super(required,
              nullable
             );
        this.predicate = predicate;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsBinarySuchThatSpec(required,
                                        true,
                                        predicate
        );
    }

    @Override
    public JsSpec optional() {
        return new JsBinarySuchThatSpec(false,
                                        nullable,
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
    public Optional<Error> test(final JsValue value) {
        final Optional<Error> error = Functions.testElem(JsValue::isBinary,
                                                         BINARY_EXPECTED,
                                                         required,
                                                         nullable
                                                        )
                                               .apply(value);

        if (error.isPresent() || value.isNull()) return error;
        return predicate.apply(value.toJsBinary().value);
    }
}
