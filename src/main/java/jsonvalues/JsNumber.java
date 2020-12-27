package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 Represents an immutable json number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
public abstract class JsNumber extends JsPrimitive {
    static JsNumber of(JsonParser parser) throws IOException {
        try {
            return JsInt.of(parser.getIntValue());
        } catch (Exception ex) {
            try {
                return JsLong.of(parser.getLongValue());
            } catch (Exception ex1) {
                return JsBigInt.of(parser.getBigIntegerValue());
            }
        }

    }

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

}
