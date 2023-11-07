package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.IntFunction;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

final class JsArrayOfTestedInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {
    final IntFunction<Optional<JsError>> predicate;

    JsArrayOfTestedInt(final IntFunction<Optional<JsError>> predicate,
                       final boolean nullable
                      ) {
        super(nullable);
        this.predicate = predicate;
    }

    JsArrayOfTestedInt(final IntFunction<Optional<JsError>> predicate,
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
        return new JsArrayOfTestedInt(predicate,
                                      true,
                                      min,
                                      max
        );
    }


    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                           nullable,
                                                           min,
                                                           max
                                                          );
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {

        return Functions.testArrayOfTestedValue(v ->
                                                        v.isInt() ?
                                                                predicate.apply(v.toJsInt().value) :
                                                                Optional.of(new JsError(v,
                                                                                        INT_EXPECTED
                                                                            )
                                                                           ),
                                                nullable,
                                                min,
                                                max
                                               )
                        .apply(value);
    }


}
