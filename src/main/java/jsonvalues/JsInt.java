package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type integer.
 */
public final class JsInt implements JsNumber, Comparable<JsInt>
{


    /**
     The integer value.
     */
    public final int x;

    private JsInt(final int x)
    {
        this.x = x;
    }

    /**
     Compares two {@code JsInt} objects numerically.
     @see Integer#compareTo(Integer)
     */
    @Override
    public int compareTo(final JsInt o)
    {
        return Integer.compare(x,
                               requireNonNull(o).x
                              );
    }


    /**
     Indicates whether some other object is "equal to" this json integer. Numbers of different types
     are equals if the have the same value:
     {@code
     JsInt.of(1).equals(JsDouble.of(1.00))
     JsInt.of(1).equals(JsLong.of(1))
     JsInt.of(1).equals(JsBigInt.of(BigInteger.ONE))
     JsInt.of(1).equals(JsBigDec.of(BigDecimal.valueOf(1.0)))
     }
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

        if (number.isInt()) return x == number.asJsInt().x;
        if (number.isLong()) return (long) x == number.asJsLong().x;

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
     Returns the hashcode of this json integer.
     @return hashcode of this JsInt
     */
    @Override
    public int hashCode()
    {
        return Functions.hashCode(x);
    }


    /**
     * @return a string representation of the integer value.
     * @see Integer#toString() Integer.toString
     */
    @Override
    public String toString()
    {
        return Integer.toString(x);
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
        return JsInt.of(requireNonNull(fn).applyAsInt(x));
    }

    /**
     adds up this integer to the specified one
     @param that the specified integer
     @return the sum of both integers
     */
    public JsInt plus(JsInt that){
        return JsInt.of(x+that.x);
    }

    /**
     subtract this integer from the specified one
     @param that the specified integer
     @return this integer minus the specified one
     */
    public JsInt minus(JsInt that){
        return JsInt.of(x-that.x);
    }
    /**
     multiplies this integer by the specified one
     @param that the specified integer
     @return this integer times the specified one
     */
    public JsInt times(JsInt that){
        return JsInt.of(x*that.x);
    }

    /**
     Tests the value of this json integer on a predicate.
     @param predicate the predicate
     @return true if this integer satisfies the predicate
     */
    public boolean test(IntPredicate predicate)
    {
        return predicate.test(x);
    }

}
