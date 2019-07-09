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
     types are equals if the have the same value:
     {@code
     JsBigInt.parse(BigInteger.ONE).equals(JsBigInt.parse(BigInteger.ONE))
     JsBigInt.parse(BigInteger.ONE).equals(JsInt.parse(1))
     JsBigInt.parse(BigInteger.ONE).equals(JsDouble.parse(1.0))
     JsBigInt.parse(BigInteger.ONE).equals(JsBigDec.parse(BigDecimal.valueOf(1.0)))
     JsBigInt.parse(BigInteger.ONE).equals(JsLong.parse(1))
     }
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

        if (number.isInt())
            return Functions.equals(x,
                                    number.asJsInt().x
                                   );


        if (number.isLong())
            return Functions.equals(x,
                                    number.asJsLong().x
                                   );


        if (number.isBigDec())
            return Functions.equals(x,
                                    number.asJsBigDec().x
                                   );


        if (number.isDouble())
            return Functions.equals(number.asJsDouble().x,
                                    x
                                   );

        return false;

    }

    /**
     Returns the hashcode of this json big integer.
     @return the hashcode of this JsBigInt
     */
    @Override
    public int hashCode()
    {
        final OptionalInt optInt = Functions.bigIntToInt(x);
        if (optInt.isPresent()) return Functions.hashCode(optInt.getAsInt());

        final OptionalLong optLong = Functions.bigIntToLong(x);
        if (optLong.isPresent())
            return Functions.hashCode(optLong.getAsLong());

        return Functions.hashCode(x);

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


}
