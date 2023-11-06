package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class JsEnum extends AbstractNullable implements JsSpec, AvroSpec {

    final JsArray symbols;

    final EnumMetaData metaData;

    JsEnum(List<String> symbols) {
        this(false, JsArray.ofStrs(symbols), null);
    }

    JsEnum(boolean nullable, JsArray symbols, EnumMetaData metaData) {
        super(nullable);
        this.symbols = symbols;
        this.metaData = metaData;
    }


    @Override
    public JsSpec nullable() {
        return new JsEnum(true, symbols, metaData);
    }

    @Override
    public JsParser parser() {
        return JsParsers.INSTANCE.ofStrSuchThat(value -> {
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

    public JsArray getSymbols() {
        return symbols;
    }

    public EnumMetaData getMetaData() {
        return metaData;
    }
}
