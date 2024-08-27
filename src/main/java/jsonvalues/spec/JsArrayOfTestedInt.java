package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.INT_EXPECTED;

import java.util.function.IntFunction;
import jsonvalues.JsValue;

final class JsArrayOfTestedInt extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  private final IntFunction<JsError> predicate;

  JsArrayOfTestedInt(final IntFunction<JsError> predicate,
                     final boolean nullable
                    ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedInt(final IntFunction<JsError> predicate,
                     final boolean nullable,
                     ArraySchemaConstraints arrayConstraints
                    ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedInt(predicate,
                                  true,
                                  arrayConstraints
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfIntEachSuchThat(predicate,
                                                       nullable,
                                                       arrayConstraints
                                                      );
  }

  @Override
  public JsError testValue(final JsValue value) {

    return Fun.testArrayOfTestedValue(v ->
                                          v.isInt() ?
                                          predicate.apply(v.toJsInt().value) :
                                          new JsError(v,
                                                      INT_EXPECTED
                                          ),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }


}
