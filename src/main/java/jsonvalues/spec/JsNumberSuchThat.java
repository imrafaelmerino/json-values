package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNumber;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.NUMBER_EXPECTED;

final class JsNumberSuchThat extends AbstractNullable implements JsValuePredicate {
    final Function<JsNumber, Optional<JsError>> predicate;

    JsNumberSuchThat(final Function<JsNumber, Optional<JsError>> predicate,
                     final boolean nullable
                    ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsNumberSuchThat(predicate,
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
    public JsValue toAvro() {
        return
                nullable ? JsArray.of("null", "double") : JsStr.of("double");
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        final Optional<JsError> error = Functions.testElem(JsValue::isNumber,
                                                           NUMBER_EXPECTED,
                                                           nullable
                                                          )
                                                 .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsNumber());
    }
}
