package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.OBJ_EXPECTED;

import java.util.function.Function;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

final class JsObjSuchThat extends AbstractNullable implements JsOneErrorSpec {

  final Function<JsObj, JsError> predicate;

  JsObjSuchThat(final Function<JsObj, JsError> predicate,
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
  public JsParser parser() {
    return JsParsers.INSTANCE.ofObjSuchThat(predicate,
                                            nullable
                                           );
  }

  @Override
  public JsError testValue(final JsValue value) {
    JsError error = Fun.testValue(JsValue::isObj,
                                  OBJ_EXPECTED,
                                  nullable,
                                  value
                                 );

    return error != null || value.isNull() ?
           error :
           predicate.apply(value.toJsObj());
  }
}
