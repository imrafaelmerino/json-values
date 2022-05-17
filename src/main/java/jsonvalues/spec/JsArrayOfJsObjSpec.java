package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import io.vavr.Tuple2;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

public class JsArrayOfJsObjSpec implements JsSpec, JsArraySpec {

    private final boolean nullable;
    private final JsObjSpec spec;


    JsArrayOfJsObjSpec(final boolean nullable,
                       final JsObjSpec jsObjSpec
    ) {
        this.nullable = nullable;
        this.spec = jsObjSpec;

    }



    @Override
    public JsSpec nullable() {
        return new JsArrayOfJsObjSpec(true,
                                      spec
        );
    }



    @Override
    public JsSpecParser parser() {

        return JsSpecParsers.INSTANCE.ofArrayOfObjSpec(spec.bindings.filter((k, s) -> !spec.getOptionalFields().contains(k))
                                                                    .map(it -> it._1)
                                                                    .toVector(),
                                                       spec.bindings.map((k, s) -> new Tuple2<>(k,
                                                                                                s.parser())),
                                                       nullable,
                                                       spec.strict
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
        if (array.isEmpty()) return result;
        final JsPath currentPath = path.inc();
        for (JsValue value : array) {
            result.addAll(spec.test(currentPath,
                                    value));
        }
        return result;
    }


}


