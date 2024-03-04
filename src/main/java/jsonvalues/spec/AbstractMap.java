package jsonvalues.spec;


import java.util.function.Function;
import jsonvalues.JsValue;

abstract class AbstractMap extends AbstractNullable {

  AbstractMap(boolean nullable) {
    super(nullable);
  }


  protected JsError test(JsValue value,
                         Function<JsValue, ERROR_CODE> getError
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
      ERROR_CODE errorCode = getError.apply(pair.value());
      if (errorCode != null) {
        return new JsError(pair.value(),
                           errorCode);
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
