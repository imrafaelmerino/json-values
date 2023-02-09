package jsonvalues;

import fun.optic.Prism;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * Represents an immutable json string.
 */
public final class JsStr extends JsPrimitive implements Comparable<JsStr> {

    public static final int TYPE_ID = 2;
    /**
     * prism between the sum type JsValue and JsStr
     */
    public static final Prism<JsValue, String> prism =
            new Prism<>(s -> s.isStr() ?
                             Optional.of(s.toJsStr().value) :
                             Optional.empty(),
                        JsStr::of
            );

    public static final Prism<String, byte[]> base64Prism =
            new Prism<>(s -> {
                try {
                    return Optional.of(Base64.getDecoder()
                                             .decode(s));
                } catch (IllegalArgumentException e) {
                    return Optional.empty();
                }
            },
                        bytes -> Base64.getEncoder().encodeToString(bytes)
            );

    public static final Prism<String, Instant> instantPrism =
            new Prism<>(s -> {
                try {
                    return Optional.of(Instant.parse(s));
                } catch (DateTimeParseException e) {
                    return Optional.empty();
                }
            },
                        Instant::toString
            );

    /**
     * The string value.
     */
    public final String value;

    private JsStr(String value) {
        this.value = value;
    }

    /**
     * Static factory method to create a JsStr from a string.
     *
     * @param str the string
     * @return a new JsStr
     */
    public static JsStr of(String str) {
        return new JsStr(requireNonNull(str));
    }

    @Override
    public int id() {
        return TYPE_ID;
    }
    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isStr() {
        return true;
    }

    /**
     * Compares two {@code JsStr} objects lexicographically.
     *
     * @see String#compareTo(String)
     */
    @Override
    public int compareTo(final JsStr o) {
        return value.compareTo(requireNonNull(o).value);
    }

    /**
     * Tests this JsStr on a predicate.
     *
     * @param predicate the predicate
     * @return true if this string satisfies the predicate
     */
    public boolean test(Predicate<String> predicate) {
        return requireNonNull(predicate).test(value);
    }

    /**
     * Returns the hashcode of this json string.
     *
     * @return hashcode of this JsStr
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this json string.
     *
     * @param that the reference object with which to compare.
     * @return true if <code>that</code> is a JsStr with the same value as <code>this</code> JsStr
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (that instanceof JsStr) {
            JsStr thatStr = (JsStr) that;
            return Objects.equals(value,
                                  thatStr.value
            );
        }
        if (that instanceof JsInstant)
            return JsStr.instantPrism.reverseGet.apply(((JsInstant) that).value).equals(value);
        if (that instanceof JsBinary)
            return JsStr.base64Prism.reverseGet.apply(((JsBinary) that).value).equals(value);
        return false;
    }

    /**
     * Returns the string representation of this json string which is its value quoted.
     *
     * @return the value quoted.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Maps this JsStr into another one.
     *
     * @param fn the mapping function
     * @return a new JsStr
     */
    public JsStr map(final UnaryOperator<String> fn) {
        return JsStr.of(requireNonNull(fn).apply(value));
    }

    @Override
    public boolean isBinary() {
        return JsStr.base64Prism.getOptional.apply(value).isPresent();
    }

    @Override
    public boolean isInstant() {
        return JsStr.instantPrism.getOptional.apply(value).isPresent();
    }
}
