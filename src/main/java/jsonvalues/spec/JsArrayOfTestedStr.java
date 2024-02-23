package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.STRING_EXPECTED;

import java.util.function.Function;
import jsonvalues.JsValue;

final class JsArrayOfTestedStr extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<String, JsError> predicate;

  JsArrayOfTestedStr(final Function<String, JsError> predicate,
                     final boolean nullable
                    ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedStr(final Function<String, JsError> predicate,
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
    return new JsArrayOfTestedStr(predicate,
                                  true,
                                  min,
                                  max
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfStrEachSuchThat(predicate,
                                                       nullable,
                                                       min,
                                                       max
                                                      );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Functions.testArrayOfTestedValue(v ->
                                                v.isStr() ?
                                                predicate.apply(v.toJsStr().value) :
                                                new JsError(v,
                                                            STRING_EXPECTED

                                                ),
                                            nullable,
                                            min,
                                            max,
                                            value
                                           );
  }

}
