package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

public class JsArrayOfJsObjSpec extends AbstractSizableArrSpec implements JsSpec, JsArraySpec {

    private final JsObjSpec spec;


    JsArrayOfJsObjSpec(final boolean nullable,
                       final JsObjSpec spec
    ) {
        this(nullable,
             spec,
             0,
             Integer.MAX_VALUE);
    }

    JsArrayOfJsObjSpec(final boolean nullable,
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
        return new JsArrayOfJsObjSpec(true,
                                      spec,
                                      min,
                                      max
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofArrayOfObjSpec(spec.getRequiredFields(),
                                                       spec.parsers,
                                                       nullable,
                                                       spec.strict,
                                                       min,
                                                       max
        );

    }

    @Override
    public Set<JsErrorPair> test(final JsPath parentPath,
                                 final JsValue value
    ) {
        Set<JsErrorPair> errors = new HashSet<>();
        if (value.isNull() && nullable) return errors;
        if (!value.isArray()) {
            errors.add(JsErrorPair.of(parentPath,
                                      new JsError(value,
                                                  ARRAY_EXPECTED)));
            return errors;
        }
        return apply(parentPath.index(-1),
                     value.toJsArray()
        );
    }


    private Set<JsErrorPair> apply(final JsPath path,
                                   final JsArray array
    ) {
        Set<JsErrorPair> result = new HashSet<>();
        for (JsValue value : array) {
            result.addAll(spec.test(path.inc(),
                                    value));
        }
        if (array.size() < min)
            result.add(JsErrorPair.of(path,
                                      new JsError(array,
                                                  ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN)));
        if (array.size() > max)
            result.add(JsErrorPair.of(path,
                                      new JsError(array,
                                                  ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX)));
        return result;
    }


}


