package jsonvalues.spec;

import jsonvalues.*;

import java.util.ArrayList;
import java.util.List;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

final class JsArrayOfObjSpec extends AbstractSizableArr implements JsSpec, JsArraySpec, AvroSpec {

    private final JsObjSpec spec;


    JsArrayOfObjSpec(final boolean nullable,
                     final JsObjSpec spec
                    ) {
        this(nullable,
             spec,
             0,
             Integer.MAX_VALUE);
    }

    JsArrayOfObjSpec(final boolean nullable,
                     final JsObjSpec spec,
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
        return new JsArrayOfObjSpec(true,
                                    spec,
                                    min,
                                    max
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObjSpec(spec.getRequiredFields(),
                                                       spec.parsers,
                                                       spec.predicate,
                                                       spec.strict,
                                                       spec.metaData,
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


