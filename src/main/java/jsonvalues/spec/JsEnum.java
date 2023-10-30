package jsonvalues.spec;

import jsonvalues.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class JsEnum extends AbstractNullable implements JsSpec {

    final JsArray symbols;
    private String avroName;
    private String avroDoc;
    private JsArray aliases;
    private AvroAttBuilder avroAttBuilder;

    JsEnum(List<String> symbols) {
        this(false, JsArray.ofIterable(symbols.stream().map(JsStr::of).toList()));
    }

    private JsEnum(boolean nullable, JsArray symbols) {
        super(nullable);
        this.symbols = symbols;
    }


    @Override
    public JsSpec nullable() {
        return new JsEnum(true, symbols);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofStrSuchThat(value -> {
            JsStr str = JsStr.of(value);
            boolean valid = symbols.containsValue(str);
            return valid ?
                    Optional.empty() :
                    Optional.of(new JsError(str, ERROR_CODE.ENUM_SYMBOL_EXPECTED));
        }, nullable);
    }

    @Override
    public Set<SpecError> test(JsPath parentPath, JsValue value) {
        Set<SpecError> errors = new HashSet<>();
        if (nullable && value.isNull()) return errors;
        if (!symbols.containsValue(value))
            errors.add(SpecError.of(parentPath, new JsError(value, ERROR_CODE.ENUM_SYMBOL_EXPECTED)));
        return errors;

    }

    public JsEnum withAvroAtt(final AvroAttBuilder builder) {
        this.avroAttBuilder = requireNonNull(builder);
        return this;
    }

    @Override
    public JsValue toAvro() {
        if (avroAttBuilder == null)
            throw new IllegalArgumentException("avroAttBuilder is null. Set one with `withAvroAtt(builder)`");
        AvroAtt avroAtt = avroAttBuilder.build();
        JsObj schema = JsObj.of("name", JsStr.of(avroAtt.name));
        if (avroAtt.doc != null) schema = schema.set("doc", JsStr.of(avroAtt.doc));
        if (avroAtt.aliases != null) schema = schema.set("aliases", avroAtt.aliases);
        schema = schema.set("symbols",symbols);
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;
    }
}
