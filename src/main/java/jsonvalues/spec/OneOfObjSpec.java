package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;

public final class OneOfObjSpec extends AbstractNullable implements JsSpec, AvroSpec {

    final List<JsObjSpec> specs;
    final OneOf oneOf;

    private OneOfObjSpec(boolean nullable, List<JsObjSpec> specs) {
        super(nullable);
        this.specs = specs;
        oneOf = new OneOf(nullable, specs);

    }

    public static OneOfObjSpec of(List<JsObjSpec> specs) {
        return new OneOfObjSpec(false, specs);
    }

    @Override
    public JsSpec nullable() {
        return new OneOfObjSpec(true, specs);
    }

    @Override
    public JsSpecParser parser() {
        return oneOf.parser();
    }


    @Override
    public List<SpecError> test(JsPath parentPath, JsValue value) {
        return oneOf.test(parentPath, value);
    }


    @Override
    public JsValue toAvroSchema() {
        JsArray schema = JsArray.ofIterable(specs.stream().map(JsObjSpec::toAvroSchema).toList());
        return nullable ? JsArray.of(JsStr.of("null"), schema) : schema;

    }
}
