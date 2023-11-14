package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.List;
import java.util.Optional;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

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
    public Optional<JsError> testValue(JsValue value) {
        if (isNullable() && value.isNull()) return Optional.empty();


        if (!value.isArray()) {
            return Optional.of(new JsError(value,
                                           ARRAY_EXPECTED));

        }
        return apply(value.toJsArray()
                    );
    }


    private Optional<JsError> apply(final JsArray array
                                   ) {

        if (array.size() < min)
            return Optional.of(new JsError(array,
                                           ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN));
        if (array.size() > max)
            return Optional.of(new JsError(array,
                                           ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX));

        for (JsValue value : array) {
            List<SpecError> errors = spec.test(value);
            if (!errors.isEmpty()) return Optional.of(errors.get(0).error);
        }
        return Optional.empty();
    }


}


