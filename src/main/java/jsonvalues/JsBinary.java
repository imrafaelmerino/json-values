package jsonvalues;

import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class JsBinary implements JsValue {
    public static final int ID = 10;
    public final byte[] value;

    /**
     Creates a JsBinary from an array of bytes
     @param bytes the array of bytes
     @return an immutable JsBinary
     */
    public static JsBinary of(final byte[] bytes){
        return new JsBinary(requireNonNull(bytes));
    }

    /**
     Creates a JsBinary from an array of bytes encoded as a string in base64
     @param base64 the string
     @throws IllegalArgumentException if {@code base64} is not in valid Base64 scheme
     @return an immutable JsBinary
     */
    public static JsBinary of(final String base64){
        return new JsBinary(requireNonNull(Base64.getDecoder().decode(base64)));
    }

    private JsBinary(final byte[] value) {
        this.value = value;
    }

    /**
     prism between the sum type JsValue and JsBinary
     */
    public static Prism<JsValue, byte[]> prism =
            new Prism<>(s -> s.isBinary() ? Optional.of(s.toJsBinary().value) : Optional.empty(),
                        JsBinary::of
            );
    @Override
    public int id() {
        return ID;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsBinary jsBinary = (JsBinary) o;
        return Arrays.equals(value,
                             jsBinary.value
                            );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
