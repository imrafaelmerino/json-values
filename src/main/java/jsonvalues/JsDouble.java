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
                              requireNonNull(o).x);
    }

    /**
     Indicates whether some other object is "equal to" this json double. Numbers parse different of
     are equals if the have the same value:
     {@code
     JsDouble.of(1.0).equals(JsDouble.of(1))
     JsDouble.of(1).equals(JsInt.of(1))
     JsDouble.of(1).equals(JsBigInt.of(BigInteger.ONE))
     JsDouble.of(1).equals(JsBigDec.of(BigDecimal.valueOf(1.0)))
     JsDouble.of(1).equals(JsLong.of(1))
     }
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
        if (number.isLong()) return x == (double) number.asJsLong().x;
        if (number.isInt()) return x == (double) number.asJsInt().x;
        if (number.isBigInt())
            return Functions.equals(x,
                                    number.asJsBigInt().x
                                   );

        if (number.isBigDec())
            return Functions.equals(x,
                                    number.asJsBigDec().x
                                   );


        return false;
    }

    /**
     Returns the hashcode of this json double.
     @return the hashcode of this JsDouble
     */
    @Override
    public int hashCode()
    {
        final BigDecimal bigDecimal = BigDecimal.valueOf(this.x);
        final OptionalInt optionalInt = Functions.bigDecimalToInt(bigDecimal);
        if (optionalInt.isPresent()) return Functions.hashCode(optionalInt.getAsInt());
        final OptionalLong optionalLong = Functions.bigDecimalToLong(bigDecimal);
        if (optionalLong.isPresent()) return Functions.hashCode(optionalLong.getAsLong());
        final Optional<BigInteger> optionalBigInteger = Functions.bigDecimalToBigInteger(bigDecimal);
        return optionalBigInteger.map(Functions::hashCode)
                                 .orElseGet(() -> Functions.hashCode(bigDecimal));
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
     adds up this long to the specified one
     @param that the specified long
     @return the sum of both longs
     */
    public JsDouble plus(JsDouble that){
        return JsDouble.of(x+that.x);
    }

    /**
     subtract this long from the specified one
     @param that the specified long
     @return this long minus the specified one
     */
    public JsDouble minus(JsDouble that){
        return JsDouble.of(x-that.x);
    }
    /**
     multiplies this long by the specified one
     @param that the specified long
     @return this long times the specified one
     */
    public JsDouble times(JsDouble that){
        return JsDouble.of(x*that.x);
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
     * Static factory method to create a JsDouble from a double primitive type.
     * @param n the double primitive type
     * @return a new JsDouble
     */
    public static JsDouble of(double n)
    {
        return new JsDouble(n);
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

}
