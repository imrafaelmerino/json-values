package jsonvalues;


import fun.optic.Prism;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an immutable json number of type BigInteger.
 */
public final class JsBigInt extends JsNumber implements Comparable<JsBigInt> {
    public static final Prism<JsValue, BigInteger> prism =
            new Prism<>(s ->
                        {
                            if (s.isLong())
                                return Optional.of(BigInteger.valueOf(s.toJsLong().value));
                            if (s.isInt())
                                return Optional.of(BigInteger.valueOf(s.toJsInt().value));
                            if (s.isBigInt())
                                return Optional.of(s.toJsBigInt().value);
                            return Optional.empty();
                        },
                        JsBigInt::of
            );
    /**
     * the big integer value.
     */
    public final BigInteger value;

    private JsBigInt(final BigInteger value) {
        this.value = value;
    }

    /**
     * Static factory method to create a JsBigInt from BigInteger objects.
     *
     * @param n the big integer
     * @return a new JsBigInt
     */
    public static JsBigInt of(BigInteger n) {
        return new JsBigInt(requireNonNull(n));
    }


    @Override
    public boolean isBigInt() {
        return true;
    }

    /**
     * Compares two {@code JsBigInt} objects numerically.
     *
     * @see JsBigInt#compareTo(JsBigInt)
     */
    @Override
    public int compareTo(final JsBigInt o) {
        return value.compareTo(requireNonNull(o).value);
    }

    /**
     * Returns the hashcode of this json big integer.
     *
     * @return the hashcode of this JsBigInt
     */
    @Override
    public int hashCode() {
        Optional<Integer> optInt = intValueExact();
        if (optInt.isPresent()) return optInt.get();

        Optional<Long> optLong = longValueExact();
        return optLong.map(aLong -> JsLong.of(aLong)
                                          .hashCode()).orElseGet(value::hashCode);

    }

    /**
     * Indicates whether some other object is "equal to" this json big integer. Numbers of different
     * types are equals if they have the same value.
     *
     * @param that the reference object with which to compare.
     * @return true if that is a JsNumber with the same value as this
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof JsNumber number)) return false;
        if (number.isBigInt()) return value.equals(number.toJsBigInt().value);
        if (number.isInt()) return intEquals(number.toJsInt());
        if (number.isLong()) return longEquals(number.toJsLong());
        if (number.isBigDec()) return bigDecEquals(number.toJsBigDec());
        if (number.isDouble()) return doubleEquals(number.toJsDouble());
        return false;
    }

    /**
     * @return a string representation of the bigint value.
     * @see BigInteger#toString() BigInteger.toString
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * returns true if this biginteger and the specified integer represent the same number
     *
     * @param jsInt the specified JsInt
     * @return true if both JsValue are the same value
     */
    boolean intEquals(JsInt jsInt) {
        Optional<Integer> optional = intValueExact();
        return optional.isPresent() && optional.get() == requireNonNull(jsInt).value;
    }

    /**
     * returns true if this bigint and the specified long represent the same number
     *
     * @param jsLong the specified JsLong
     * @return true if both JsValue are the same value
     */
    boolean longEquals(JsLong jsLong) {
        Optional<Long> optional = longValueExact();
        return optional.isPresent() && optional.get() == requireNonNull(jsLong).value;
    }

    /**
     * returns true if this bigint and the specified big-decimal represent the same number
     *
     * @param jsBigDec the specified JsBigDec
     * @return true if both JsValue are the same value
     */
    private boolean bigDecEquals(JsBigDec jsBigDec) {
        return requireNonNull(jsBigDec).bigIntEquals(this);
    }

    /**
     * returns true if this bigint and the specified double represent the same number
     *
     * @param jsDouble the specified JsDouble
     * @return true if both JsValue are the same value
     */
    private boolean doubleEquals(JsDouble jsDouble) {
        return requireNonNull(jsDouble).bigIntEquals(this);
    }

    /**
     * Returns the value of this biginteger; or an empty optional if the value overflows an {@code int}.
     *
     * @return this biginteger as an int wrapped in an OptionalInt
     */
    Optional<Integer> intValueExact() {
        try {
            return Optional.of(value.intValueExact());
        } catch (ArithmeticException e) {
            return Optional.empty();
        }

    }

    /**
     * Returns the value of this biginteger; or an empty optional if the value overflows an {@code long}.
     *
     * @return this biginteger as a long wrapped in an OptionalLong
     */
    Optional<Long> longValueExact() {
        try {
            return Optional.of(value.longValueExact());
        } catch (ArithmeticException e) {
            return Optional.empty();
        }

    }

    /**
     * Maps this json bigint into another one.
     *
     * @param fn the mapping function
     * @return a new JsBigInt
     */
    public JsBigInt map(UnaryOperator<BigInteger> fn) {
        return JsBigInt.of(requireNonNull(fn).apply(value));
    }

    /**
     * Tests the value of this json bigint on a predicate.
     *
     * @param predicate the predicate
     * @return true if this big integer satisfies the predicate
     */
    public boolean test(Predicate<BigInteger> predicate) {
        return requireNonNull(predicate).test(value);
    }

}
