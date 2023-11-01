package jsonvalues.spec;

import jsonvalues.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class JsEnum extends AbstractNullable implements JsSpec, AvroSpec {

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
    public List<SpecError> test(JsPath parentPath, JsValue value) {
        List<SpecError> errors = new ArrayList<>();
        if (nullable && value.isNull()) return errors;
        if (!symbols.containsValue(value))
            errors.add(SpecError.of(parentPath, new JsError(value, ERROR_CODE.ENUM_SYMBOL_EXPECTED)));
        return errors;

    }

    public JsEnum withAvroAtt(final AvroAttBuilder builder) {
        this.avroAttBuilder = requireNonNull(builder);
        return this;
    }


    public AvroAttBuilder getAvroAttBuilder() {
        return avroAttBuilder;
    }

    public JsArray getSymbols() {
        return symbols;
    }
}
