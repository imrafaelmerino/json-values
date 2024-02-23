package jsonvalues.spec;


import java.util.function.Predicate;
import jsonvalues.JsValue;

abstract class AbstractMap extends AbstractNullable {

  AbstractMap(boolean nullable) {
    super(nullable);
  }


  protected JsError test(JsValue value,
                         Predicate<JsValue> isError,
                         ERROR_CODE code
                        ) {
    if (value.isNull() && nullable) {
      return null;
    }
    if (!value.isObj()) {
      return new JsError(value,
                         ERROR_CODE.OBJ_EXPECTED);
    }

    var obj = value.toJsObj();

    for (var pair : obj) {
      if (isError.test(pair.value())) {
        return new JsError(pair.value(),
                           code);
      }
    }

    return null;
  }

  protected JsError test(JsValue value,
                         JsSpec spec
                        ) {
    if (value.isNull() && nullable) {
      return null;
    }

    if (!value.isObj()) {
      return new JsError(value,
                         ERROR_CODE.OBJ_EXPECTED);
    }

    var obj = value.toJsObj();

    for (var pair : obj) {
      var xs = spec.test(pair.value());
      if (!xs.isEmpty()) {
        return xs.get(0).error;
      }
    }

    return null;
  }

}
