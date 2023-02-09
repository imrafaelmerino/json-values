package jsonvalues;

/**
 * Represents an immutable json number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
public abstract  class JsNumber extends JsPrimitive  {

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

}
