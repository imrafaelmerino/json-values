package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.DOUBLE_EXPECTED;

import java.util.function.DoubleFunction;
import jsonvalues.JsValue;

final class JsArrayOfTestedDouble extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  private final DoubleFunction<JsError> predicate;

  JsArrayOfTestedDouble(final DoubleFunction<JsError> predicate,
                        final boolean nullable
                       ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedDouble(DoubleFunction<JsError> predicate,
                        boolean nullable,
                        ArraySchemaConstraints arrayConstraints
                       ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedDouble(predicate,
                                     true,
                                     arrayConstraints);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfDoubleEachSuchThat(predicate,
                                                          nullable,
                                                          arrayConstraints
                                                         );
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v ->
                                          v.isDouble() ?
                                          predicate.apply(v.toJsDouble().value) :
                                          new JsError(v,
                                                      DOUBLE_EXPECTED

                                          ),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }


}
