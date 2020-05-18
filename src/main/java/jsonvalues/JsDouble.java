package jsonvalues;

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
public final class JsDouble extends JsNumber implements Comparable<JsDouble> {
    public static final int ID = 5;


    /**
     prism between the sum type JsValue and JsDouble
     */
    public static Prism<JsValue, Double> prism = new Prism<>(s ->
                                                             {
                                                                 if (s.isDouble())
                                                                     return Optional.of(s.toJsDouble().value);
                                                                 if (s.isDecimal()) return s.toJsBigDec()
                                                                                            .doubleValueExact();
                                                                 return Optional.empty();
                                                             },
                                                             JsDouble::of
    );
    /**
     The double value.
     */
    public final double value;

    private JsDouble(final double value) {
        this.value = value;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    /**
     Compares two {@code JsDouble} objects numerically.

     @see Double#compareTo(Double)
     */
    @Override
    public int compareTo(final JsDouble o) {
        return Double.compare(value,
                              requireNonNull(o).value
                             );
    }

    /**
     Returns the hashcode of this json double.

     @return the hashcode of this JsDouble
     */
    @Override
    public int hashCode() {
        final JsBigDec bigDecimal = JsBigDec.of(BigDecimal.valueOf(this.value));

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
     Indicates whether some other object is "equal to" this json double. Numbers of different types
     are equals if the have the same value.

     @param that the reference object with which to compare.
     @return true if that is a JsNumber with the same value as this JsDouble
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber)) return false;
        final JsNumber number = (JsNumber) that;
        if (number.isDouble()) return value == number.toJsDouble().value;
        if (number.isLong()) return equals(number.toJsLong());
        if (number.isInt()) return equals(number.toJsInt());
        if (number.isBigInt()) return equals(number.toJsBigInt());
        if (number.isBigDec()) return equals(number.toJsBigDec());
        return false;
    }

    /**
     @return a string representation of this object.
     @see Double#toString() Double.toString
     */
    @Override
    public String toString() {
        return Double.toString(value);
    }

    /**
     returns true if this double and the specified long represent the same number

     @param jsLong the specified JsLong
     @return true if both JsElem are the same value
     */
    private boolean equals(JsLong jsLong) {
        return requireNonNull(jsLong).equals(this);
    }

    /**
     returns true if this double and the specified integer represent the same number

     @param jsInt the specified JsInt
     @return true if both JsElem are the same value
     */
    private boolean equals(JsInt jsInt) {
        return requireNonNull(jsInt).equals(this);
    }

    /**
     returns true if this double and the specified biginteger represent the same number

     @param jsBigInt the specified JsBigInt
     @return true if both JsElem are the same value
     */
    boolean equals(JsBigInt jsBigInt) {

        final Optional<BigInteger> y = bigIntegerExact();
        return y.isPresent() && y.get()
                                 .equals(requireNonNull(jsBigInt).value);
    }

    /**
     returns true if this double and the specified bigdecimal represent the same number

     @param jsBigDec the specified JsBigDec
     @return true if both JsElem are the same value
     */
    private boolean equals(JsBigDec jsBigDec) {
        return requireNonNull(jsBigDec).equals(this);
    }

    /**
     Converts this {@code double} to a {@code BigInteger}, checking for lost information.  An empty
     optional is returned if this {@code double} has a nonzero fractional part.

     @return this double as an bigint wrapped in an Optional
     */
    Optional<BigInteger> bigIntegerExact() {
        try {
            return Optional.ofNullable(BigDecimal.valueOf(value)
                                                 .toBigIntegerExact());
        } catch (ArithmeticException e) {
            return Optional.empty();
        }
    }

    /**
     Maps this json double into another one.

     @param fn the mapping function
     @return a new JsDouble
     */
    public JsDouble map(DoubleUnaryOperator fn) {
        return JsDouble.of(requireNonNull(fn).applyAsDouble(value));
    }

    /**
     Static factory method to create a JsDouble from a double primitive type.

     @param n the double primitive type
     @return a new JsDouble
     */
    public static JsDouble of(double n) {
        return new JsDouble(n);
    }

    /**
     Tests the value of this json double on a predicate.

     @param predicate the predicate
     @return true if this double satisfies the predicate
     */
    public boolean test(DoublePredicate predicate) {
        return predicate.test(value);
    }

}
