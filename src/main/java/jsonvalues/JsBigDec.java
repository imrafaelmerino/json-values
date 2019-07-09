package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type BigDecimal.
 */
public final class JsBigDec implements JsNumber,Comparable<JsBigDec>
{

    /**
     The big decimal value
     */
    public final BigDecimal x;


    private JsBigDec(final BigDecimal x)
    {
        this.x = x;
    }

    /**
     Compares two {@code JsBigDec} objects numerically
     @see JsBigDec#compareTo(JsBigDec)
     */
    @Override
    public int compareTo(final JsBigDec o)
    {
        return x.compareTo(requireNonNull(o).x);
    }


    /**
     Indicates whether some other object is "equal to" this json big decimal. Numbers of different types are equals if the have the same value:
     {@code
     JsBigDec.of(BigDecimal.valueOf(1.0)).equals(JsDouble.of(1.00))
     JsBigDec.of(BigDecimal.valueOf(1.0)).equals(JsInt.of(1))
     JsBigDec.of(BigDecimal.valueOf(1.0)).equals(JsBigInt.of(BigInteger.ONE))
     JsBigDec.of(BigDecimal.valueOf(1.0)).equals(JsBigDec.of(BigDecimal.valueOf(1.00)))
     JsBigDec.of(BigDecimal.valueOf(1.0)).equals(JsLong.of(1))
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

        if (number.isBigDec()) return x.compareTo(number.asJsBigDec().x) == 0;

        if (number.isBigInt())
            return Functions.equals(number.asJsBigInt().x,
                                    x
                                   );


        if (number.isInt())
            return Functions.equals(number.asJsInt().x,
                                    x
                                   );


        if (number.isLong())
            return Functions.equals(number.asJsLong().x,
                                    x
                                   );


        if (number.isDouble()) return x.equals(BigDecimal.valueOf(number.asJsDouble().x));

        return false;


    }

    /**
     Returns the hashcode of this json big decimal
     @return the hashcode of this JsBigDec
     */
    @Override
    public int hashCode()
    {
        final OptionalInt optionalInt = Functions.bigDecimalToInt(x);
        if (optionalInt.isPresent()) return Functions.hashCode(optionalInt.getAsInt());

        final OptionalLong optionalLong = Functions.bigDecimalToLong(x);
        if (optionalLong.isPresent()) return Functions.hashCode(optionalLong.getAsLong());

        final Optional<BigInteger> optionalBigInteger = Functions.bigDecimalToBigInteger(x);
        return optionalBigInteger.map(Functions::hashCode)
                                 .orElseGet(() -> Functions.hashCode(x));

    }

    /**
     Maps this JsBigDec into another one
     @param fn the mapping function
     @return a new JsBigDec
     */
    public JsBigDec map(UnaryOperator<BigDecimal> fn)
    {
        return JsBigDec.of(requireNonNull(fn).apply(x));
    }

    /**
     Tests the value of this json bigdec on a predicate
     @param predicate the predicate
     @return true if this big decimal satisfies the predicate
     */
    public boolean test(Predicate<BigDecimal> predicate)
    {
        return predicate.test(x);
    }

    /**
     * Static factory method to create a JsBigDec from a BigDecimal object.
     * @param n the big decimal
     * @return a new JsBigDec
     */
    public static JsBigDec of(BigDecimal n)
    {
        return new JsBigDec(requireNonNull(n));
    }


    /**
     * @return a string representation of the bigdec value
     * @see BigDecimal#toString() BigDecimal.toString
     */
    @Override
    public String toString()
    {
        return x.toString();
    }
}
