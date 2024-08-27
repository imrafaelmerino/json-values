package jsonvalues.spec;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

abstract class AbstractMap extends AbstractNullable {

  AbstractMap(boolean nullable) {
    super(nullable);
  }


  protected List<SpecError> test(JsPath parent,
                                 JsValue value,
                                 Function<JsValue, ERROR_CODE> getError
                                ) {
    if (value.isNull() && nullable) {
      return List.of();
    }
    List<SpecError> errors = new ArrayList<>();

    if (!value.isObj()) {
      errors.add(SpecError.of(parent,
                              new JsError(value,
                                          ERROR_CODE.OBJ_EXPECTED)));
      return errors;

    }

    var obj = value.toJsObj();

    for (var pair : obj) {
      ERROR_CODE errorCode = getError.apply(pair.value());
      if (errorCode != null) {
        errors.add(SpecError.of(parent.key(pair.key()),
                                new JsError(pair.value(),
                                            errorCode)));

      }
    }

    return errors;
  }

  protected List<SpecError> test(JsPath parent,
                                 JsValue value,
                                 JsSpec spec
                                ) {
    if (value.isNull() && nullable) {
      return List.of();
    }

    List<SpecError> errors = new ArrayList<>();

    if (!value.isObj()) {
      errors.add(SpecError.of(parent,
                              new JsError(value,
                                          ERROR_CODE.OBJ_EXPECTED)));
      return errors;
    }

    var obj = value.toJsObj();

    for (var pair : obj) {
      var xs = spec.test(parent.key(pair.key()),
                         pair.value());
      errors.addAll(xs);
    }

    return errors;
  }

}
