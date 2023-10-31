package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.List;
import java.util.Set;

public final class OneOfObjSpec extends AbstractNullable implements JsSpec {

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
    public Set<SpecError> test(JsPath parentPath, JsValue value) {
        return oneOf.test(parentPath, value);
    }


    @Override
    public JsValue toAvro() {
        JsArray schema = JsArray.ofIterable(specs.stream().map(JsSpec::toAvro).toList());
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;

    }
}
