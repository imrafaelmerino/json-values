package jsonvalues.spec;

import jsonvalues.*;

import java.util.Objects;
import java.util.Set;


final class JsMapOfObjSpec extends AbstractMap implements JsSpec {

    private final JsObjSpec spec;

    JsMapOfObjSpec(JsObjSpec spec) {
        this(false, spec);
    }

    JsMapOfObjSpec(boolean nullable, JsObjSpec spec) {
        super(nullable);
        this.spec = Objects.requireNonNull(spec);
    }


    @Override
    public JsSpec nullable() {
        return new JsMapOfObjSpec(true, spec);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfSpec(spec.parser(),
                                                  nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value
                              ) {
        return test(path, value, it -> !it.isObj(), ERROR_CODE.OBJ_EXPECTED);
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type",JsStr.of("map"),
                                "vales",spec.toAvro());
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;

    }


}
