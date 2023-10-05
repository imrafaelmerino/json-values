package jsonvalues;

import fun.optic.Prism;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Represents an array of bytes in a JSON-like data structure. The purpose of the {@code JsBinary} class is
 * to allow the inclusion of binary data within JSON-like objects, even though binary data is not part of
 * the JSON specification. This class provides a means to serialize binary data into a string using the
 * Base64 encoding scheme, making it compatible with JSON-like structures.
 *
 * <p>Binary data is often encountered in various data formats, and while JSON itself does not support binary
 * data, the {@code JsBinary} class serves as a custom solution for embedding binary content within JSON-like
 * objects.
 *
 * <p>When serialized to a JSON string, a {@code JsBinary} object is represented as a string containing the
 * Base64-encoded binary data. Consequently, a {@code JsBinary} object and a {@code JsStr} object created from
 * the Base64-encoded string are considered equal if the encoded content matches.
 *
 * <p>Here is an example of how to use {@code JsBinary}:
 * <pre>{@code
 * // Create a byte array containing binary data (e.g., image bytes)
 * byte[] bytes = "foo".getBytes();
 *
 * // Encode the binary data as a Base64 string
 * String base64 = Base64.getEncoder().encodeToString(bytes);
 *
 * // Create a JsBinary object from the byte array
 * JsBinary binary = JsBinary.of(bytes);
 *
 * // Create a JsStr object from the Base64-encoded string
 * JsStr strFromBase64 = JsStr.of(base64);
 *
 * // Check if the JsBinary and JsStr objects are considered equal
 * boolean areEqual = binary.equals(strFromBase64); // true
 * }</pre>
 *
 * <p>It's important to note that while {@code JsBinary} is a useful tool for representing binary data in JSON-like
 * structures, it is not part of the JSON standard. Its purpose is to provide a means of encoding binary data within
 * a format that can coexist with JSON-like data structures.
 *
 * @see JsPrimitive
 */
public final class JsBinary extends JsPrimitive {
    /**
     * prism between the sum type JsValue and JsBinary
     */
    public static final Prism<JsValue, byte[]> prism =
            new Prism<>(s -> {
                if (s.isBinary())
                    return Optional.of(s.toJsBinary().value);
                if (s.isStr()) {
                    return JsStr.base64Prism.getOptional.apply(s.toJsStr().value);
                }
                return Optional.empty();
            },
                        JsBinary::of
            );
    /**
     * the array of bytes
     */
    public final byte[] value;

    private JsBinary(final byte[] value) {
        this.value = value;
    }

    /**
     * Creates a JsBinary from an array of bytes
     *
     * @param bytes the array of bytes
     * @return an immutable JsBinary
     */
    public static JsBinary of(final byte[] bytes) {
        return new JsBinary(requireNonNull(bytes));
    }

    /**
     * Creates a JsBinary from an array of bytes encoded as a string in base64
     *
     * @param base64 the string
     * @return an immutable JsBinary
     * @throws IllegalArgumentException if {@code base64} is not in valid Base64 scheme
     */
    public static JsBinary of(final String base64) {
        return new JsBinary(requireNonNull(Base64.getDecoder().decode(base64)));
    }


    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public String toString() {
        return  Base64.getEncoder()
                            .encodeToString(value)  ;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if (o instanceof JsValue) {
            return JsBinary.prism.getOptional.apply(((JsValue) o))
                                             .map(bytes -> Arrays.equals(bytes,
                                                                         value))
                                             .orElse(false);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Base64.getEncoder().encodeToString(value));
    }
}
