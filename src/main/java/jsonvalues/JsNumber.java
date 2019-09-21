package jsonvalues;

/**
 Represents an immutable json number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
public interface JsNumber extends JsElem
{

    @Override
    default boolean isNumber()
    {
        return true;
    }

}
