package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type BigDecimal.
 */
public final class JsBigDec implements JsNumber, Comparable<JsBigDec>
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
     Indicates whether some other object is "equal to" this json big decimal. Numbers of different
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
        if (number.isBigDec()) return x.compareTo(number.asJsBigDec().x) == 0;
        if (number.isBigInt()) return equals(number.asJsBigInt());
        if (number.isInt()) return equals(number.asJsInt());
        if (number.isLong()) return equals(number.asJsLong());
        if (number.isDouble()) return equals(number.asJsDouble());
        return false;
    }

    /**
     Returns the hashcode of this json big decimal
     @return the hashcode of this JsBigDec
     */
    @Override
    public int hashCode()
    {
        final OptionalInt optInt = intValueExact();
        if (optInt.isPresent()) return optInt.getAsInt();

        final OptionalLong optLong = longValueExact();
        if (optLong.isPresent()) return JsLong.of(optLong.getAsLong())
                                              .hashCode();

        final Optional<BigInteger> optBigInt = bigIntegerExact();
        return optBigInt.map(BigInteger::hashCode)
                        .orElseGet(x::hashCode);

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

    /**
     * Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code biginteger}.
     @return this bigdecimal as an biginteger wrapped in an OptionalInt
     */
    public Optional<BigInteger> bigIntegerExact()
    {
        try
        {
            return Optional.of(x.toBigIntegerExact());
        }
        catch (ArithmeticException e)
        {
            return Optional.empty();
        }

    }

    /**
     * Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code int}.
     @return this bigdecimal as an int wrapped in an OptionalInt
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
     * Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code long}.
     @return this bigdecimal as an long wrapped in an OptionalLong
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
     Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code double}.
     @return this bigdecimal as an double wrapped in an OptionalDouble
     @see BigDecimal#doubleValue()
     */
    public OptionalDouble doubleValueExact()
    {
        final double value = x.doubleValue();
        if (value == Double.NEGATIVE_INFINITY) return OptionalDouble.empty();
        if (value == Double.POSITIVE_INFINITY) return OptionalDouble.empty();
        return OptionalDouble.of(value);

    }

    /**
     returns true if this bigdecimal and the specified biginteger represent the same number
     @param jsBigInt the specified JsBigInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsBigInt jsBigInt)
    {
        final Optional<BigInteger> optional = bigIntegerExact();
        return optional.isPresent() && optional.get()
                                               .equals(requireNonNull(jsBigInt).x);
    }

    /**
     returns true if this bigdecimal and the specified integer represent the same number
     @param jsInt the specified JsInt
     @return true if both JsElem are the same value
     */
    public boolean equals(JsInt jsInt)
    {
        final OptionalInt optional = intValueExact();
        return optional.isPresent() && optional.getAsInt() == requireNonNull(jsInt).x;
    }

    /**
     returns true if this bigdecimal and the specified long represent the same number
     @param jsLong the specified JsLong
     @return true if both JsElem are the same value
     */
    public boolean equals(JsLong jsLong)
    {
        final OptionalLong optional = longValueExact();
        return optional.isPresent() && optional.getAsLong() == requireNonNull(jsLong).x;
    }

    /**
     returns true if this bigdecimal and the specified double represent the same number
     @param jsDouble the specified JsDouble
     @return true if both JsElem are the same value
     */
    public boolean equals(JsDouble jsDouble)
    {

        //errorProne warning BigDecimalEquals -> compareTo instead of equals so 2.0 = 2.000
        return BigDecimal.valueOf(requireNonNull(jsDouble).x)
                         .compareTo(x) == 0;
    }

    @Override
    public boolean isBigDec()
    {
        return true;
    }

}
