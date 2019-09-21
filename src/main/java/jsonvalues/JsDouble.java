package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type double.
 */
public final class JsDouble implements JsNumber, Comparable<JsDouble>
{

    /**
     The double value.
     */
    public final double x;


    private JsDouble(final double x)
    {
        this.x = x;
    }

    /**
     Compares two {@code JsDouble} objects numerically.
     @see Double#compareTo(Double)
     */
    @Override
    public int compareTo(final JsDouble o)
    {
        return Double.compare(x,
                              requireNonNull(o).x
                             );
    }

    /**
     Indicates whether some other object is "equal to" this json double. Numbers of different types
     are equals if the have the same value.
     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this JsDouble
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;
        if (number.isDouble()) return x == number.asJsDouble().x;
        if (number.isLong()) return equals(number.asJsLong());
        if (number.isInt()) return equals(number.asJsInt());
        if (number.isBigInt()) return equals(number.asJsBigInt());
        if (number.isBigDec()) return equals(number.asJsBigDec());
        return false;
    }

    /**
     Returns the hashcode of this json double.
     @return the hashcode of this JsDouble
     */
    @Override
    public int hashCode()
    {
        final JsBigDec bigDecimal = JsBigDec.of(BigDecimal.valueOf(this.x));

        final OptionalInt optInt = bigDecimal.intValueExact();
        if (optInt.isPresent()) return optInt.getAsInt();

        final OptionalLong optLong = bigDecimal.longValueExact();
        if (optLong.isPresent()) return JsLong.of(optLong.getAsLong())
                                              .hashCode();

        final Optional<BigInteger> optBigInt = bigDecimal.bigIntegerExact();
        return optBigInt.map(BigInteger::hashCode)
                        .orElseGet(bigDecimal::hashCode);
    }

    /**
     Maps this json double into another one.
     @param fn the mapping function
     @return a new JsDouble
     */
    public JsDouble map(DoubleUnaryOperator fn)
    {
        return JsDouble.of(requireNonNull(fn).applyAsDouble(x));
    }

    /**
     subtract this long from the specified one
     @param that the specified long
     @return this long minus the specified one
     */
    public JsDouble minus(JsDouble that)
    {
        return JsDouble.of(x - that.x);
    }

    /**
     * Static factory method to create a JsDouble from a double primitive type.
     * @param n the double primitive type
     * @return a new JsDouble
     */
    public static JsDouble of(double n)
    {
        return new JsDouble(n);
    }

    /**
     adds up this long to the specified one
     @param that the specified long
     @return the sum of both longs
     */
    public JsDouble plus(JsDouble that)
    {
        return JsDouble.of(x + that.x);
    }

    /**
     Tests the value of this json double on a predicate.
     @param predicate the predicate
     @return true if this double satisfies the predicate
     */
    public boolean test(DoublePredicate predicate)
    {
        return predicate.test(x);
    }

    /**
     multiplies this long by the specified one
     @param that the specified long
     @return this long times the specified one
     */
    public JsDouble times(JsDouble that)
    {
        return JsDouble.of(x * that.x);
    }

    /**
     * @return a string representation of this object.
     * @see Double#toString() Double.toString
     */
    @Override
    public String toString()
    {
        return Double.toString(x);
    }

    /**
     * Converts this {@code double} to a {@code BigInteger}, checking for lost information.  An empty
     * optional is returned if this {@code double} has a nonzero fractional part.
     @return this double as an bigint wrapped in an Optional
     */
    public Optional<BigInteger> bigIntegerExact()
    {
        try
        {
            return Optional.ofNullable(BigDecimal.valueOf(x)
                                                 .toBigIntegerExact());
        }
        catch (ArithmeticException e)
        {
            return Optional.empty();
        }
    }

    /**
     returns true if this double and the specified bigdecimal represent the same number
     @param jsBigDec the specified JsBigDec
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigDec jsBigDec)
    {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     returns true if this double and the specified biginteger represent the same number
     @param jsBigInt the specified JsBigInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigInt jsBigInt)
    {

        final Optional<BigInteger> y = bigIntegerExact();
        return y.isPresent() && y.get()
                                 .equals(requireNonNull(jsBigInt).x);
    }

    /**
     returns true if this double and the specified long represent the same number
     @param jsLong the specified JsLong
     @return true if both JsElem are the same value
     */
    public boolean equals(JsLong jsLong)
    {
        return requireNonNull(jsLong).equals(this);
    }

    /**
     returns true if this double and the specified integer represent the same number
     @param jsInt the specified JsInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsInt jsInt)
    {
        return requireNonNull(jsInt).equals(this);
    }

    @Override
    public boolean isDouble()
    {
        return true;
    }

}
