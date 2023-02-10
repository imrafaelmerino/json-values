package jsonvalues;

/**
 * Represents an immutable JSON number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
public abstract sealed class JsNumber extends JsPrimitive permits JsBigDec, JsBigInt, JsDouble, JsInt, JsLong {

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

}
