package jsonvalues.spec;

import jsonvalues.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OneOf extends AbstractNullable implements JsSpec {

    final List<? extends JsSpec> specs;

    OneOf(boolean nullable, List<? extends JsSpec> specs) {
        super(nullable);
        this.specs = specs;
    }

    public static OneOf of(List<JsSpec> specs) {
        return new OneOf(false, specs);
    }

    @Override
    public JsSpec nullable() {
        return new OneOf(true, specs);
    }

    @Override
    public JsSpecParser parser() {
        return reader -> parse(reader, 0);
    }

    private JsValue parse(JsReader reader, int i) {
        if (i >= specs.size())
            throw JsParserException.reasonAt(ParserErrors.ONEOF_EXHAUSTED,
                                             reader.getPositionInStream()
                                            );
        JsSpec spec = specs.get(i);
        if(i < specs.size() -1)reader.setMark();
        try {
            JsValue parsed = spec.parser()
                                 .parse(reader);
            return parsed;
        } catch (JsParserException e) {
            if(i < specs.size() -1)reader.rollbackToMark();
            return parse(reader, i + 1);
        }
    }

    @Override
    public Set<SpecError> test(JsPath parentPath, JsValue value) {
        return test(parentPath, value, 0, new HashSet<>());
    }

    private Set<SpecError> test(JsPath parentPath,
                                JsValue value,
                                int i,
                                Set<SpecError> accumulated
                               ) {

        if (i >= specs.size()) return accumulated;

        JsSpec spec = specs.get(i);
        Set<SpecError> iErrors = spec.test(parentPath,
                                           value);
        if (iErrors.isEmpty()) return iErrors;

        iErrors.forEach(e -> e.setSpec(i + ""));
        accumulated.addAll(iErrors);

        return test(parentPath, value, i + 1, accumulated);
    }

    @Override
    public JsValue toAvro() {
        JsArray schema = JsArray.ofIterable(specs.stream().map(JsSpec::toAvro).toList());
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;

    }
}
