package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type integer.
 */
public final class JsInt extends JsNumber implements Comparable<JsInt>
{
    private static final int ID = 9;

    @Override
    public int id()
    {
        return ID;
    }

    /**
     The integer value.
     */
    public final int value;

    private JsInt(final int value)
    {
        this.value = value;
    }

    /**
     Compares two {@code JsInt} objects numerically.
     @see Integer#compareTo(Integer)
     */
    @Override
    public int compareTo(final JsInt o)
    {
        return Integer.compare(value,
                               requireNonNull(o).value
                              );
    }


    /**
     Indicates whether some other object is "equal to" this json integer. Numbers of different types
     are equals if the have the same value.
     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this JsInt.
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {

        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;

        if (number.isInt()) return value == number.toJsInt().value;
        if (number.isLong()) return (long) value == number.toJsLong().value;
        if (number.isBigInt()) return equals(number.toJsBigInt());
        if (number.isBigDec()) return equals(number.toJsBigDec());
        if (number.isDouble()) return equals(number.toJsDouble());

        return false;
    }

    /**
     Returns the hashcode of this json integer.
     @return hashcode of this JsInt
     */
    @Override
    public int hashCode()
    {
        return value;
    }


    /**
     * @return a string representation of the integer value.
     * @see Integer#toString() Integer.toString
     */
    @Override
    public String toString()
    {
        return Integer.toString(value);
    }

    /**
     * Static factory method to create a JsInt from an integer primitive type.
     * @param n the integer primitive type
     * @return a new JsInt
     */
    public static JsInt of(int n)
    {
        return new JsInt(n);
    }

    /**
     Maps this json integer into another one.
     @param fn the mapping function
     @return a new JsInt
     */
    public JsInt map(IntUnaryOperator fn)
    {
        return JsInt.of(requireNonNull(fn).applyAsInt(value));
    }

    /**
     adds up this integer to the specified one
     @param that the specified integer
     @return the sum of both integers
     */
    public JsInt plus(JsInt that)
    {
        return JsInt.of(value + that.value);
    }

    /**
     subtract this integer from the specified one
     @param that the specified integer
     @return this integer minus the specified one
     */
    public JsInt minus(JsInt that)
    {
        return JsInt.of(value - that.value);
    }

    /**
     multiplies this integer by the specified one
     @param that the specified integer
     @return this integer times the specified one
     */
    public JsInt times(JsInt that)
    {
        return JsInt.of(value * that.value);
    }

    /**
     Tests the value of this json integer on a predicate.
     @param predicate the predicate
     @return true if this integer satisfies the predicate
     */
    public boolean test(IntPredicate predicate)
    {
        return requireNonNull(predicate).test(value);
    }

    /**
     returns true if this integer and the specified biginteger represent the same number
     @param jsBigInt the specified JsBigInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigInt jsBigInt)
    {
        return requireNonNull(jsBigInt).equals(this);
    }

    /**
     returns true if this integer and the specified bigdecimal represent the same number
     @param jsBigDec the specified JsBigDec
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigDec jsBigDec)
    {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     returns true if this integer and the specified long represent the same number
     @param jsLong the specified JsLong
     @return true if both JsElem are the same value
     */
    public boolean equals(JsLong jsLong)
    {
        return requireNonNull(jsLong).equals(this);
    }

    /**
     returns true if this integer and the specified double represent the same number
     @param jsDouble the specified JsDouble
     @return true if both JsElem are the same value
     */
    public boolean equals(JsDouble jsDouble)
    {
        return (double) value == requireNonNull(jsDouble).value;
    }

    @Override
    public boolean isInt()
    {
        return true;
    }

}
