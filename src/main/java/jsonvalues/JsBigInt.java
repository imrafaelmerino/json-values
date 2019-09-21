package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigInteger;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type BigInteger.
 */
public final class JsBigInt implements JsNumber, Comparable<JsBigInt>
{
    /**
     the big integer value.
     */
    public final BigInteger x;

    private JsBigInt(final BigInteger x)
    {
        this.x = x;
    }

    /**
     Compares two {@code JsBigInt} objects numerically.
     @see JsBigInt#compareTo(JsBigInt)
     */
    @Override
    public int compareTo(final JsBigInt o)
    {
        return x.compareTo(requireNonNull(o).x);
    }


    /**
     Indicates whether some other object is "equal to" this json big integer. Numbers of different
     types are equals if the have the same value.
     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this
     */
    @Override
    public boolean equals(@Nullable Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;
        if (number.isBigInt()) return x.equals(number.asJsBigInt().x);
        if (number.isInt()) return equals(number.asJsInt());
        if (number.isLong()) return equals(number.asJsLong());
        if (number.isBigDec()) return equals(number.asJsBigDec());
        if (number.isDouble()) return equals(number.asJsDouble());
        return false;
    }

    /**
     Returns the hashcode of this json big integer.
     @return the hashcode of this JsBigInt
     */
    @Override
    public int hashCode()
    {
        final OptionalInt optInt = intValueExact();
        if (optInt.isPresent()) return optInt.getAsInt();

        final OptionalLong optLong = longValueExact();
        if (optLong.isPresent())
            return JsLong.of(optLong.getAsLong())
                         .hashCode();

        return x.hashCode();

    }

    /**
     Maps this json bigint into another one.
     @param fn the mapping function
     @return a new JsBigInt
     */
    public JsBigInt map(UnaryOperator<BigInteger> fn)
    {
        return JsBigInt.of(requireNonNull(fn).apply(x));
    }

    /**
     Tests the value of this json bigint on a predicate.
     @param predicate the predicate
     @return true if this big integer satisfies the predicate
     */
    public boolean test(Predicate<BigInteger> predicate)
    {
        return predicate.test(x);
    }

    /**
     * Static factory method to create a JsBigInt from BigInteger objects.
     * @param n the big integer
     * @return a new JsBigInt
     */
    public static JsBigInt of(BigInteger n)
    {
        return new JsBigInt(requireNonNull(n));
    }

    /**
     * @return a string representation of the bigint value.
     * @see BigInteger#toString() BigInteger.toString
     */
    @Override
    public String toString()
    {
        return x.toString();
    }

    /**
     * Returns the value of this biginteger; or an empty optional if the value overflows an {@code int}.
     @return this biginteger as an int wrapped in an OptionalInt
     */
    public OptionalInt intValueExact()
    {
        try
        {
            return OptionalInt.of(x.intValueExact());
        }
        catch (ArithmeticException e)
        {
            return OptionalInt.empty();
        }

    }

    /**
     * Returns the value of this biginteger; or an empty optional if the value overflows an {@code long}.
     @return this biginteger as an long wrapped in an OptionalLong
     */
    public OptionalLong longValueExact()
    {
        try
        {
            return OptionalLong.of(x.longValueExact());
        }
        catch (ArithmeticException e)
        {
            return OptionalLong.empty();
        }

    }

    /**
     returns true if this bigint and the specified bigdecimal represent the same number
     @param jsBigDec the specified JsBigDec
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigDec jsBigDec)
    {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     returns true if this biginteger and the specified integer represent the same number
     @param jsInt the specified JsInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsInt jsInt)
    {
        final OptionalInt optional = intValueExact();
        return optional.isPresent() && optional.getAsInt() == requireNonNull(jsInt).x;
    }

    /**
     returns true if this bigint and the specified long represent the same number
     @param jsLong the specified JsLong
     @return true if both JsElem are the same value
     */
    public boolean equals(JsLong jsLong)
    {
        final OptionalLong optional = longValueExact();
        return optional.isPresent() && optional.getAsLong() == requireNonNull(jsLong).x;
    }

    /**
     returns true if this bigint and the specified double represent the same number
     @param jsDouble the specified JsDouble
     @return true if both JsElem are the same value
     */
    public boolean equals(JsDouble jsDouble)
    {
        return requireNonNull(jsDouble).equals(this);
    }

    @Override
    public boolean isBigInt()
    {
        return true;
    }

}
