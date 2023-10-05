package jsonvalues;

/**
 * Represents a sealed abstract class for primitive JSON values. It serves as a common base class for various
 * primitive JSON types, such as numbers, strings, booleans, and others. Instances of this class are immutable.
 *
 * This class is part of a sealed hierarchy and permits specific subclasses for different JSON primitive types.
 *
 * @see JsBinary
 * @see JsBool
 * @see JsInstant
 * @see JsNull
 * @see JsNumber
 * @see JsStr
 */
public abstract sealed class JsPrimitive implements JsValue permits JsBinary, JsBool, JsInstant, JsNull, JsNumber, JsStr {

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isJson() {
        return false;
    }
}
