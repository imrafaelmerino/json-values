package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfSpec extends AbstractSizableArr implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  private final JsSpec spec;

  JsArrayOfSpec(final boolean nullable,
                final JsSpec spec
               ) {
    this(nullable,
         spec,
         0,
         Integer.MAX_VALUE);
  }

  JsArrayOfSpec(final boolean nullable,
                final JsSpec spec,
                int min,
                int max
               ) {
    super(nullable,
          min,
          max);
    this.spec = spec;
  }

  JsSpec getElemSpec() {
    return spec;
  }

  @Override
  public JsSpec nullable() {
    return new JsArrayOfSpec(true,
                             spec,
                             min,
                             max
    );
  }


  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfSpec(spec.parser(),
                                            nullable,
                                            min,
                                            max
                                           );

  }


  @Override
  public JsError testValue(JsValue value) {
    if (isNullable() && value.isNull()) {
      return null;
    }

    if (!value.isArray()) {
      return new JsError(value,
                         ARRAY_EXPECTED);

    }
    return apply(value.toJsArray()
                );
  }


  private JsError apply(final JsArray array
                       ) {

    if (array.size() < min) {
      return new JsError(array,
                         ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN);
    }
    if (array.size() > max) {
      return new JsError(array,
                         ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX);
    }

    for (JsValue value : array) {
      List<SpecError> errors = spec.test(value);
      if (!errors.isEmpty()) {
        return errors.get(0).error;
      }
    }
    return null;
  }


}


