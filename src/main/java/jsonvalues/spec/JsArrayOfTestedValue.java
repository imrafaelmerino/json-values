package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsValue;

final class JsArrayOfTestedValue extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {

  private final Function<JsValue, JsError> predicate;

  JsArrayOfTestedValue(final Function<JsValue, JsError> predicate,
                       final boolean nullable
                      ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedValue(Function<JsValue, JsError> predicate,
                       boolean nullable,
                       ArraySchemaConstraints arrayConstraints
                      ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedValue(predicate,
                                    true,
                                    arrayConstraints
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfValueEachSuchThat(predicate,
                                                         nullable,
                                                         arrayConstraints);
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(predicate,
                                      nullable,
                                      arrayConstraints,
                                      value);
  }
}
