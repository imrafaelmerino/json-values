package jsonvalues;

import java.util.Base64;

import static java.util.Objects.requireNonNull;

public class JsBinary implements JsValue {
    public static final int ID = 10;
    public final byte[] value;

    public static JsBinary of(final byte[] bytes){
        return new JsBinary(requireNonNull(bytes));
    }

    /**

     @param base64
     @return
     @throws IllegalArgumentException if {@code base64} is not in valid Base64 scheme
     */
    public static JsBinary of(final String base64){
        return new JsBinary(requireNonNull(Base64.getDecoder().decode(base64)));
    }

    private JsBinary(final byte[] value) {
        this.value = value;
    }

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
}
