package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

import java.util.function.Function;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

final class JsArrayOfTestedObj extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec {

  final Function<JsObj, JsError> predicate;

  JsArrayOfTestedObj(final Function<JsObj, JsError> predicate,
                     final boolean nullable
                    ) {
    super(nullable);
    this.predicate = predicate;
  }

  JsArrayOfTestedObj(final Function<JsObj, JsError> predicate,
                     final boolean nullable,
                     ArraySchemaConstraints arrayConstraints
                    ) {
    super(nullable,
          arrayConstraints);
    this.predicate = predicate;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfTestedObj(predicate,
                                  true,
                                  arrayConstraints

    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfObjEachSuchThat(predicate,
                                                       nullable,
                                                       arrayConstraints
                                                      );
  }


  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testArrayOfTestedValue(v ->
                                          v.isObj() ?
                                          predicate.apply(v.toJsObj()) :
                                          new JsError(v,
                                                      OBJ_EXPECTED
                                          ),
                                      nullable,
                                      arrayConstraints,
                                      value
                                     );
  }
}
