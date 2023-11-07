package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

final class JsArrayOfTestedObj extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {
    final Function<JsObj, Optional<JsError>> predicate;

    JsArrayOfTestedObj(final Function<JsObj, Optional<JsError>> predicate,
                       final boolean nullable
                      ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedObj(final Function<JsObj, Optional<JsError>> predicate,
                       final boolean nullable,
                       int min,
                       int max
                      ) {
        super(nullable,
              min,
              max);
        this.predicate = predicate;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfTestedObj(predicate,
                                      true,
                                      min,
                                      max
        );
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfObjEachSuchThat(predicate,
                                                           nullable,
                                                           min,
                                                           max
                                                          );
    }


    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testArrayOfTestedValue(v ->
                                                        v.isObj() ?
                                                                predicate.apply(v.toJsObj()) :
                                                                Optional.of(new JsError(v,
                                                                                        OBJ_EXPECTED
                                                                            )
                                                                           ),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }
}
