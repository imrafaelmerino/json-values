package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

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
     equals if the have the same value:
     {@code
     JsLong.parse(1).equals(JsLong.parse(1))
     JsLong.parse(1).equals(JsInt.parse(1))
     JsLong.parse(1).equals(JsBigInt.parse(BigInteger.ONE))
     JsLong.parse(1).equals(JsBigDec.parse(BigDecimal.valueOf(1.0)))
     JsLong.parse(1).equals(JsDouble.parse(1.0))
     }
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

        if (number.isInt()) return x == (long) number.asJsInt().x;


        if (number.isBigInt())
            return Functions.equals(number.asJsBigInt().x,
                                    x
                                   );

        if (number.isBigDec())
            return Functions.equals(number.asJsBigDec().x,
                                    x
                                   );

        if (number.isDouble()) return (double) x == number.asJsDouble().x;

        return false;

    }

    /**
     Returns the hashcode of this json long.
     @return hashcode of this JsLong
     */
    @Override
    public int hashCode()
    {
        return Functions.longToInt(x)
                        .isPresent() ? Functions.hashCode((int) x) : Functions.hashCode(x);
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
    public JsLong plus(JsLong that){
        return JsLong.of(x+that.x);
    }

    /**
     subtract this long from the specified one
     @param that the specified long
     @return this long minus the specified one
     */
    public JsLong minus(JsLong that){
        return JsLong.of(x-that.x);
    }
    /**
     multiplies this long by the specified one
     @param that the specified long
     @return this long times the specified one
     */
    public JsLong times(JsLong that){
        return JsLong.of(x*that.x);
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


}
