package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

final class JsStrSuchThat extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

    final Function<String, Optional<JsError>> predicate;

    JsStrSuchThat(final Function<String, Optional<JsError>> predicate,
                  final boolean nullable
                 ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsStrSuchThat(predicate,
                                 true
        );
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofStrSuchThat(predicate,
                                                nullable
                                               );
    }



    @Override
    public Optional<JsError> testValue(final JsValue value) {
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
