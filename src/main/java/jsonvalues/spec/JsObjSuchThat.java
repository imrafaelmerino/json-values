package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

final class JsObjSuchThat extends AbstractNullable implements JsValuePredicate {

    final Function<JsObj, Optional<JsError>> predicate;

    JsObjSuchThat(final Function<JsObj, Optional<JsError>> predicate,
                  final boolean nullable
                 ) {
        super(nullable);
        this.predicate = predicate;
    }


    @Override
    public JsSpec nullable() {
        return new JsObjSuchThat(predicate,
                                 true
        );
    }

    @Override
    public JsValue toAvro() {
        throw new AvroNotSupported(JsObjSuchThat.class);
    }



    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObjSuchThat(predicate,
                                                    nullable
                                                   );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        Optional<JsError> error = Functions.testElem(JsValue::isObj,
                                                     OBJ_EXPECTED,
                                                     nullable
                                                    )
                                           .apply(value);

        return error.isPresent() || value.isNull() ?
                error :
                predicate.apply(value.toJsObj());
    }
}
