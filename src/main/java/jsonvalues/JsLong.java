package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type long.
 */
public final class JsLong implements JsNumber, Comparable<JsLong>
{


    /**
     The long primitive.
     */
    public final long x;

    private JsLong(final long x)
    {
        this.x = x;
    }


    /**
     Compares two {@code JsLong} objects numerically.
     @see Long#compareTo(Long)
     */
    @Override
    public int compareTo(final JsLong o)
    {
        return Long.compare(x,
                            requireNonNull(o).x
                           );
    }


    /**
     Indicates whether some other object is "equal to" this json long. Numbers of different types are
     equals if the have the same value.
     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this JsLong
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;

        if (number.isLong()) return x == number.asJsLong().x;
        if (number.isInt()) return equals(number.asJsInt());
        if (number.isBigInt()) return equals(number.asJsBigInt());
        if (number.isBigDec()) return equals(number.asJsBigDec());
        if (number.isDouble()) return equals(number.asJsDouble());

        return false;

    }

    /**
     Returns the hashcode of this json long.
     @return hashcode of this JsLong
     */
    @Override
    public int hashCode()
    {
        final OptionalInt intExact = intValueExact();
        return intExact.isPresent() ? intExact.getAsInt() : (int) (x ^ (x >>> 32));
    }

    /**
     Maps this json long into another one.
     @param fn the mapping function
     @return a new JsLong
     */
    public JsLong map(LongUnaryOperator fn)
    {
        return JsLong.of(requireNonNull(fn).applyAsLong(x));
    }

    /**
     adds up this long to the specified one
     @param that the specified long
     @return the sum of both longs
     */
    public JsLong plus(JsLong that)
    {
        return JsLong.of(x + that.x);
    }

    /**
     subtract this long from the specified one
     @param that the specified long
     @return this long minus the specified one
     */
    public JsLong minus(JsLong that)
    {
        return JsLong.of(x - that.x);
    }

    /**
     multiplies this long by the specified one
     @param that the specified long
     @return this long times the specified one
     */
    public JsLong times(JsLong that)
    {
        return JsLong.of(x * that.x);
    }

    /**
     Tests the value of this json long on a predicate.
     @param predicate the predicate
     @return true if this long satisfies the predicate
     */
    public boolean test(LongPredicate predicate)
    {
        return predicate.test(x);
    }


    /**
     * @return a string representation of the long value.
     * @see Long#toString() Long.toString
     */
    @Override
    public String toString()
    {
        return Long.toString(x);
    }

    /**
     * Static factory method to create a JsLong from a long primitive type.
     * @param n the long primitive type
     * @return a new JsLong
     */
    public static JsLong of(long n)
    {
        return new JsLong(n);
    }

    /**
     * Returns the value of this long; or an empty optional if the value overflows an {@code int}.
     @return this long as an int wrapped in an OptionalInt
     */
    public OptionalInt intValueExact()
    {
        try
        {
            return OptionalInt.of(Math.toIntExact(x));
        }
        catch (ArithmeticException e)
        {
            return OptionalInt.empty();
        }
    }
    /**
     returns true if this long and the specified bigdecimal represent the same number
     @param jsBigDec the specified JsBigDec
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigDec jsBigDec)
    {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     returns true if this long and the specified biginteger represent the same number
     @param jsBigInt the specified JsBigInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigInt jsBigInt)
    {
        return requireNonNull(jsBigInt).equals(this);
    }
    /**
     returns true if this long and the specified integer represent the same number
     @param jsInt the specified JsInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsInt jsInt)
    {
        return x == (long) requireNonNull(jsInt).x;
    }

    /**
     returns true if this long and the specified double represent the same number
     @param jsDouble the specified JsDouble
     @return true if both JsElem are the same value
     */
    public boolean equals(JsDouble jsDouble)
    {
        return (double) x == requireNonNull(jsDouble).x;
    }

    @Override
    public boolean isLong()
    {
        return true;
    }

}
