package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

final class JsArrayOfSpec extends AbstractSizableArr implements JsSpec, JsArraySpec, AvroSpec {

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
    public List<SpecError> test(final JsPath parentPath,
                                final JsValue value
                               ) {
        List<SpecError> errors = new ArrayList<>();
        if (value.isNull() && nullable) return errors;
        if (!value.isArray()) {
            errors.add(SpecError.of(parentPath,
                                    new JsError(value,
                                                ARRAY_EXPECTED)));
            return errors;
        }
        return apply(parentPath.index(-1),
                     value.toJsArray()
                    );
    }


    private List<SpecError> apply(final JsPath path,
                                  final JsArray array
                                 ) {
        List<SpecError> result = new ArrayList<>();
        for (JsValue value : array) {
            result.addAll(spec.test(path.inc(),
                                    value));
        }
        if (array.size() < min)
            result.add(SpecError.of(path,
                                    new JsError(array,
                                                ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN)));
        if (array.size() > max)
            result.add(SpecError.of(path,
                                    new JsError(array,
                                                ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX)));
        return result;
    }


}


