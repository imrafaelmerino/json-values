package jsonvalues;

import fun.optic.Prism;

import java.util.Optional;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an immutable json number of type long.
 */
public final class JsLong extends JsNumber implements Comparable<JsLong> {

    /**
     * prism between the sum type JsValue and JsLong
     */
    public static final Prism<JsValue, Long> prism =
            new Prism<>(s ->
                        {
                            if (s.isLong())
                                return Optional.of(s.toJsLong().value);
                            if (s.isInt())
                                return Optional.of((long) s.toJsInt().value);
                            if (s.isBigInt())
                                return s.toJsBigInt()
                                        .longValueExact();
                            return Optional.empty();
                        },
                        JsLong::of
            );
    /**
     * The long primitive.
     */
    public final long value;

    private JsLong(final long value) {
        this.value = value;
    }

    /**
     * Static factory method to create a JsLong from a long primitive type.
     *
     * @param n the long primitive type
     * @return a new JsLong
     */
    public static JsLong of(long n) {
        return new JsLong(n);
    }


    @Override
    public boolean isLong() {
        return true;
    }

    /**
     * Compares two {@code JsLong} objects numerically.
     *
     * @see Long#compareTo(Long)
     */
    @Override
    public int compareTo(final JsLong o) {
        return Long.compare(value,
                            requireNonNull(o).value
        );
    }

    /**
     * Returns the hashcode of this json long.
     *
     * @return hashcode of this JsLong
     */
    @Override
    public int hashCode() {
        Optional<Integer> intExact = intValueExact();
        return intExact.orElseGet(() -> (int) (value ^ (value >>> 32)));
    }

    /**
     * Indicates whether some other object is "equal to" this json long. Numbers of different types are
     * equals if they have the same value.
     *
     * @param that the reference object with which to compare.
     * @return true if that is a JsNumber with the same value as this JsLong
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (!(that instanceof JsNumber number)) return false;
        if (number.isLong()) return value == number.toJsLong().value;
        if (number.isInt()) return intEquals(number.toJsInt());
        if (number.isBigInt()) return bigIntEquals(number.toJsBigInt());
        if (number.isBigDec()) return bigDecEquals(number.toJsBigDec());
        if (number.isDouble()) return doubleEquals(number.toJsDouble());

        return false;

    }

    /**
     * @return a string representation of the long value.
     * @see Long#toString() Long.toString
     */
    @Override
    public String toString() {
        return Long.toString(value);
    }

    /**
     * returns true if this long and the specified integer represent the same number
     *
     * @param jsInt the specified JsInt
     * @return true if both JsValue are the same value
     */
    boolean intEquals(JsInt jsInt) {
        return value == (long) requireNonNull(jsInt).value;
    }

    /**
     * returns true if this long and the specified biginteger represent the same number
     *
     * @param jsBigInt the specified JsBigInt
     * @return true if both JsValue are the same value
     */
    private boolean bigIntEquals(JsBigInt jsBigInt) {
        return requireNonNull(jsBigInt).longEquals(this);
    }

    /**
     * returns true if this long and the specified big-decimal represent the same number
     *
     * @param jsBigDec the specified JsBigDec
     * @return true if both JsValue are the same value
     */
    private boolean bigDecEquals(JsBigDec jsBigDec) {
        return requireNonNull(jsBigDec).longEquals(this);
    }

    /**
     * returns true if this long and the specified double represent the same number
     *
     * @param jsDouble the specified JsDouble
     * @return true if both JsValue are the same value
     */
    boolean doubleEquals(JsDouble jsDouble) {
        return (double) value == requireNonNull(jsDouble).value;
    }

    /**
     * Returns the value of this long; or an empty optional if the value overflows an {@code int}.
     *
     * @return this long as an int wrapped in an OptionalInt
     */
    Optional<Integer> intValueExact() {
        try {
            return Optional.of(Math.toIntExact(value));
        } catch (ArithmeticException e) {
            return Optional.empty();
        }
    }

    /**
     * Maps this json long into another one.
     *
     * @param fn the mapping function
     * @return a new JsLong
     */
    public JsLong map(LongUnaryOperator fn) {
        return JsLong.of(requireNonNull(fn).applyAsLong(value));
    }

    /**
     * Tests the value of this json long on a predicate.
     *
     * @param predicate the predicate
     * @return true if this long satisfies the predicate
     */
    public boolean test(LongPredicate predicate) {
        return predicate.test(value);
    }

}
