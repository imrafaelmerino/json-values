package jsonvalues;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json number of type BigDecimal.
 */
public final class JsBigDec extends JsNumber implements Comparable<JsBigDec> {

    public static final int TYPE_ID = 8;

    /**
     prism between the sum type JsValue and JsBigDec
     */
    public static final Prism<JsValue, BigDecimal> prism = new Prism<>(s ->
                                                                 {
                                                                     if (s.isDouble())
                                                                         return Optional.of(BigDecimal.valueOf(s.toJsDouble().value));
                                                                     if (s.isBigDec())
                                                                         return Optional.of(s.toJsBigDec().value);
                                                                     return Optional.empty();
                                                                 },
                                                                 JsBigDec::of
    );
    /**
     The big decimal value
     */
    public final BigDecimal value;

    private JsBigDec(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public boolean isBigDec() {
        return true;
    }

    /**
     Compares two {@code JsBigDec} objects numerically

     @see JsBigDec#compareTo(JsBigDec)
     */
    @Override
    public int compareTo(final JsBigDec o) {
        return value.compareTo(requireNonNull(o).value);
    }

    /**
     Returns the hashcode of this json big decimal

     @return the hashcode of this JsBigDec
     */
    @Override
    public int hashCode() {
        final OptionalInt optInt = intValueExact();
        if (optInt.isPresent()) return optInt.getAsInt();

        final OptionalLong optLong = longValueExact();
        if (optLong.isPresent()) return JsLong.of(optLong.getAsLong())
                                              .hashCode();

        final Optional<BigInteger> optBigInt = bigIntegerExact();
        return optBigInt.map(BigInteger::hashCode)
                        .orElseGet(value::hashCode);

    }

    /**
     Indicates whether some other object is "equal to" this json big decimal. Numbers of different
     types are equals if the have the same value.

     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;
        if (number.isBigDec()) return value.compareTo(number.toJsBigDec().value) == 0;
        if (number.isBigInt()) return bigIntEquals(number.toJsBigInt());
        if (number.isInt()) return intEquals(number.toJsInt());
        if (number.isLong()) return longEquals(number.toJsLong());
        if (number.isDouble()) return doubleEquals(number.toJsDouble());
        return false;
    }

    /**
     @return a string representation of the bigdec value
     @see BigDecimal#toString() BigDecimal.toString
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     returns true if this bigdecimal and the specified biginteger represent the same number

     @param jsBigInt the specified JsBigInt
     @return true if both JsValue are the same value
     */
    boolean bigIntEquals(JsBigInt jsBigInt) {
        final Optional<BigInteger> optional = bigIntegerExact();
        return optional.isPresent() && optional.get()
                                               .equals(requireNonNull(jsBigInt).value);
    }

    /**
     returns true if this bigdecimal and the specified integer represent the same number

     @param jsInt the specified JsInt
     @return true if both JsValue are the same value
     */
    boolean intEquals(JsInt jsInt) {
        final OptionalInt optional = intValueExact();
        return optional.isPresent() && optional.getAsInt() == requireNonNull(jsInt).value;
    }

    /**
     returns true if this bigdecimal and the specified long represent the same number

     @param jsLong the specified JsLong
     @return true if both JsValue are the same value
     */
    boolean longEquals(JsLong jsLong) {
        final OptionalLong optional = longValueExact();
        return optional.isPresent() && optional.getAsLong() == requireNonNull(jsLong).value;
    }

    /**
     returns true if this bigdecimal and the specified double represent the same number

     @param jsDouble the specified JsDouble
     @return true if both JsValue are the same value
     */
    boolean doubleEquals(JsDouble jsDouble) {

        //errorProne warning BigDecimalEquals -> compareTo instead of equals so 2.0 = 2.000
        return BigDecimal.valueOf(requireNonNull(jsDouble).value)
                         .compareTo(value) == 0;
    }

    /**
     Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code biginteger}.

     @return this bigdecimal as an biginteger wrapped in an OptionalInt
     */
    Optional<BigInteger> bigIntegerExact() {
        try {
            return Optional.of(value.toBigIntegerExact());
        } catch (ArithmeticException e) {
            return Optional.empty();
        }

    }

    /**
     Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code int}.

     @return this bigdecimal as an int wrapped in an OptionalInt
     */
    OptionalInt intValueExact() {
        try {
            return OptionalInt.of(value.intValueExact());
        } catch (ArithmeticException e) {
            return OptionalInt.empty();
        }
    }

    /**
     Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code long}.

     @return this bigdecimal as an long wrapped in an OptionalLong
     */
    OptionalLong longValueExact() {
        try {
            return OptionalLong.of(value.longValueExact());
        } catch (ArithmeticException e) {
            return OptionalLong.empty();
        }

    }

    /**
     Maps this JsBigDec into another one

     @param fn the mapping function
     @return a new JsBigDec
     */
    public JsBigDec map(UnaryOperator<BigDecimal> fn) {
        return JsBigDec.of(requireNonNull(fn).apply(value));
    }

    /**
     Static factory method to create a JsBigDec from a BigDecimal object.

     @param n the big decimal
     @return a new JsBigDec
     */
    public static JsBigDec of(BigDecimal n) {
        return new JsBigDec(requireNonNull(n));
    }

    /**
     Tests the value of this json bigdec on a predicate

     @param predicate the predicate
     @return true if this big decimal satisfies the predicate
     */
    public boolean test(Predicate<BigDecimal> predicate) {
        return predicate.test(value);
    }

    /**
     Returns the value of this bigdecimal; or an empty optional if the value overflows an {@code double}.

     @return this bigdecimal as an double wrapped in an OptionalDouble
     @see BigDecimal#doubleValue()
     */
    Optional<Double> doubleValueExact() {
        final double number = this.value.doubleValue();
        if (number == Double.NEGATIVE_INFINITY) return Optional.empty();
        if (number == Double.POSITIVE_INFINITY) return Optional.empty();
        return Optional.of(number);

    }

}
