package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.List;
import java.util.Set;

public final class JsOneOf extends AbstractNullable implements JsSpec{

    final List<JsSpec> specs;

    private JsOneOf(boolean nullable, List<JsSpec> specs) {
        super(nullable);
        this.specs = specs;
    }

    public static JsOneOf of(List<JsSpec> specs) {
        return new JsOneOf(false, specs);
    }

    @Override
    public JsSpec nullable() {
        return null;
    }

    @Override
    public JsSpecParser parser() {
        return null;
    }

    @Override
    public Set<SpecError> test(JsPath parentPath, JsValue value) {
        return null;
    }

    @Override
    public JsValue toAvro() {
        return null;
    }
}
