package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import jsonvalues.JsValue;

import java.util.Optional;


class JsArrayOfValueSpec extends AbstractPredicateSpec implements JsValuePredicate, JsArraySpec {

    JsArrayOfValueSpec(final boolean required,
                       final boolean nullable
                      ) {
        super(required,
              nullable
             );

    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfValueSpec(required,
                                      true
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfValueSpec(false,
                                      nullable
        );
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfValue(nullable);
    }

    @Override
    public Optional<Error> test(final JsValue value) {
        return Functions.testArray(required,
                                   nullable
                                  )
                        .apply(value);
    }
}
