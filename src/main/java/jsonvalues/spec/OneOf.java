package jsonvalues.spec;

import jsonvalues.JsNull;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;

final class OneOf extends AbstractNullable implements JsSpec, AvroSpec {

    final List<? extends JsSpec> specs;

    OneOf(boolean nullable, List<? extends JsSpec> specs) {
        super(nullable);
        this.specs = specs;
    }


    @Override
    public JsSpec nullable() {
        return new OneOf(true, specs);
    }

    @Override
    public JsParser parser() {

        return reader -> {
            if (reader.wasNull()) {
                if (nullable) return JsNull.NULL;
                else throw reader.newParseError(ParserErrors.INVALID_NULL);
            }
            return parse(reader, 0);
        };
    }

    private JsValue parse(JsReader reader, int i) {
        if (i >= specs.size())
            throw JsParserException.reasonAt(ParserErrors.ONEOF_EXHAUSTED,
                                             reader.getPositionInStream()
                                            );
        JsSpec spec = specs.get(i);
        if (i < specs.size() - 1) reader.setMark();
        try {
            return spec.parser()
                       .parse(reader);
        } catch (JsParserException e) {
            if (i < specs.size() - 1) reader.rollbackToMark();
            return parse(reader, i + 1);
        }
    }

    @Override
    public List<SpecError> test(JsPath parentPath, JsValue value) {
        if (nullable && value.isNull()) return List.of();
        return test(parentPath, value, 0, new ArrayList<>());
    }

    private List<SpecError> test(JsPath parentPath,
                                 JsValue value,
                                 int i,
                                 List<SpecError> accumulated
                                ) {

        if (i >= specs.size()) return accumulated;

        JsSpec spec = specs.get(i);
        List<SpecError> iErrors = spec.test(parentPath,
                                            value);
        if (iErrors.isEmpty()) return iErrors;

        iErrors.forEach(e -> e.setSpec(i + ""));
        accumulated.addAll(iErrors);

        return test(parentPath, value, i + 1, accumulated);
    }


    public List<? extends JsSpec> getSpecs() {
        return specs;
    }
}
