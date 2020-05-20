package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import io.vavr.Tuple2;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;

public class JsArrayOfJsObjSpec implements JsSpec, JsArraySpec {

    private final boolean nullable;
    private final boolean required;
    private final JsObjSpec spec;


    JsArrayOfJsObjSpec(final boolean nullable,
                       final boolean required,
                       final JsObjSpec jsObjSpec
                      ) {
        this.nullable = nullable;
        this.required = required;
        this.spec = jsObjSpec;

    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsSpec nullable() {
        return new JsArrayOfJsObjSpec(true,
                                      required,
                                      spec
        );
    }

    @Override
    public JsSpec optional() {
        return new JsArrayOfJsObjSpec(nullable,
                                      false,
                                      spec
        );
    }

    @Override
    public JsSpecParser parser() {

        return JsSpecParsers.INSTANCE.ofArrayOfObjSpec(spec.bindings.filter((k, s) -> s.isRequired())
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
                                      new Error(value,
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


