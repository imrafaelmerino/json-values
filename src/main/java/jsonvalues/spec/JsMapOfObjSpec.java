package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;
import java.util.Objects;


final class JsMapOfObjSpec extends AbstractMap implements JsSpec, AvroSpec {

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
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {
       return null;
    }

    @Override
    public JsValue toAvroSchema() {
        JsObj schema = JsObj.of("type",JsStr.of("map"),
                                "vales",spec.toAvroSchema());
        return nullable ? JsArray.of(JsStr.of("null"), schema) : schema;

    }


}
