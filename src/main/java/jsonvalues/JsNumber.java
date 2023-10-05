package jsonvalues;

/**
 * Represents a sealed abstract class for JSON number values. It serves as a common base class for various
 * numeric JSON types, such as integers and floating-point numbers. Instances of this class are immutable.
 * <p>
 * This class is part of a sealed hierarchy and permits specific subclasses for different numeric JSON types.
 *
 * @see JsBigDec   Represents a JSON number of type big decimal.
 * @see JsBigInt   Represents a JSON number of type big integer.
 * @see JsDouble   Represents a JSON number of type double precision floating-point.
 * @see JsInt      Represents a JSON number of type integer.
 * @see JsLong     Represents a JSON number of type long integer.
 */
public abstract sealed class JsNumber extends JsPrimitive
        permits JsBigDec, JsBigInt, JsDouble, JsInt, JsLong {

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    /**
     * Indicates whether this JSON number is of numeric type.
     *
     * @return true if this JSON number is of numeric type, otherwise false.
     */
    @Override
    public boolean isNumber() {
        return true;
    }

}

