package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


final class JsEnum extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

    final JsArray symbols;

    final EnumMetaData metaData;

    private JsEnum(List<String> symbols) {
        this(false, JsArray.ofStrs(Objects.requireNonNull(symbols)), null);
    }

    JsEnum(boolean nullable, JsArray symbols, EnumMetaData metaData) {
        super(nullable);
        this.symbols = symbols;
        this.metaData = metaData;
    }

    static JsEnum of(List<String> symbols) {
        return new JsEnum(symbols);
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
    public Optional<JsError> testValue(JsValue value) {
        if (isNullable() && value.isNull()) return Optional.empty();
        return !symbols.containsValue(value) ?
                Optional.of(new JsError(value, ERROR_CODE.ENUM_SYMBOL_EXPECTED)) :
                Optional.empty();
    }

    JsArray getSymbols() {
        return symbols;
    }

    EnumMetaData getMetaData() {
        return metaData;
    }
}
