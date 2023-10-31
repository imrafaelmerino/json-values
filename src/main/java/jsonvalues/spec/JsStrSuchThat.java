package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

final class JsStrSuchThat extends AbstractNullable implements JsValuePredicate, AvroSpec {

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
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStrSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public JsValue toAvroSchema() {
        return nullable ?
                JsArray.of("null", "string") :
                JsStr.of("string");
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
