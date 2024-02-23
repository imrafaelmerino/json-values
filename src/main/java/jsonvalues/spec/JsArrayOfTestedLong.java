package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.LONG_EXPECTED;

import java.util.function.LongFunction;
import jsonvalues.JsValue;

final class JsArrayOfTestedLong extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  private final LongFunction<JsError> predicate;

  JsArrayOfTestedLong(final LongFunction<JsError> predicate,
                      final boolean nullable
                     ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedLong(final LongFunction<JsError> predicate,
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
    return new JsArrayOfTestedLong(predicate,
                                   true,
                                   min,
                                   max
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfLongEachSuchThat(predicate,
                                                        nullable,
                                                        min,
                                                        max
                                                       );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v ->
                                                v.isLong() || v.isInt() ?
                                                predicate.apply(v.toJsLong().value) :
                                                new JsError(v,
                                                            LONG_EXPECTED
                                                )
        ,
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }

}
