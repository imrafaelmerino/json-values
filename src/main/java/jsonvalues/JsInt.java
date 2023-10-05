package jsonvalues;

import fun.optic.Prism;

import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an immutable JSON number of type integer.
 */
public final class JsInt extends JsNumber implements Comparable<JsInt> {
    /**
     * prism between the sum type JsValue and JsInt
     */
    public static final Prism<JsValue, Integer> prism =
            new Prism<>(s ->
                        {
                            if (s.isInt())
                                return Optional.of(s.toJsInt().value);
                            if (s.isLong())
                                return s.toJsLong()
                                        .intValueExact();
                            if (s.isBigInt())
                                return s.toJsBigInt()
                                        .intValueExact();
                            return Optional.empty();
                        },
                        JsInt::of
            );
    /**
     * The integer value.
     */
    public final int value;

    private JsInt(final int value) {
        this.value = value;
    }

    /**
     * Static factory method to create a JsInt from an integer primitive type.
     *
     * @param n the integer primitive type
     * @return a new JsInt
     */
    public static JsInt of(int n) {
        return new JsInt(n);
    }


    @Override
    public boolean isInt() {
        return true;
    }

    /**
     * Compares two {@code JsInt} objects numerically.
     *
     * @see Integer#compareTo(Integer)
     */
    @Override
    public int compareTo(final JsInt o) {
        return Integer.compare(value,
                               requireNonNull(o).value
        );
    }

    /**
     * Returns the hashcode of this JSON integer.
     *
     * @return hashcode of this JsInt
     */
    @Override
    public int hashCode() {
        return value;
    }

    /**
     * Indicates whether some other object is "equal to" this JSON integer. Numbers of different types
     * are equals if they have the same value.
     *
     * @param that the reference object with which to compare.
     * @return true if that is a JsNumber with the same value as this JsInt.
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (!(that instanceof JsNumber number)) return false;
        if (number.isInt()) return value == number.toJsInt().value;
        if (number.isLong()) return longEquals(number.toJsLong());
        if (number.isBigInt()) return bigIntEquals(number.toJsBigInt());
        if (number.isBigDec()) return bigDecEquals(number.toJsBigDec());
        if (number.isDouble()) return doubleEquals(number.toJsDouble());

        return false;
    }

    /**
     * @return a string representation of the integer value.
     * @see Integer#toString() Integer.toString
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     * returns true if this integer and the specified long represent the same number
     *
     * @param jsLong the specified JsLong
     * @return true if both JsValue are the same value
     */
    private boolean longEquals(JsLong jsLong) {
        return requireNonNull(jsLong).intEquals(this);
    }

    /**
     * returns true if this integer and the specified biginteger represent the same number
     *
     * @param jsBigInt the specified JsBigInt
     * @return true if both JsValue are the same value
     */
    private boolean bigIntEquals(JsBigInt jsBigInt) {
        return requireNonNull(jsBigInt).intEquals(this);
    }

    /**
     * returns true if this integer and the specified big-decimal represent the same number
     *
     * @param jsBigDec the specified JsBigDec
     * @return true if both JsValue are the same value
     */
    private boolean bigDecEquals(JsBigDec jsBigDec) {
        return requireNonNull(jsBigDec).intEquals(this);
    }

    /**
     * returns true if this integer and the specified double represent the same number
     *
     * @param jsDouble the specified JsDouble
     * @return true if both JsValue are the same value
     */
    boolean doubleEquals(JsDouble jsDouble) {
        return (double) value == requireNonNull(jsDouble).value;
    }

    /**
     * Maps this JSON integer into another one.
     *
     * @param fn the mapping function
     * @return a new JsInt
     */
    public JsInt map(IntUnaryOperator fn) {
        return JsInt.of(requireNonNull(fn).applyAsInt(value));
    }

    /**
     * /**
     * Tests the value of this JSON integer on a predicate.
     *
     * @param predicate the predicate
     * @return true if this integer satisfies the predicate
     */
    public boolean test(IntPredicate predicate) {
        return requireNonNull(predicate).test(value);
    }

}
