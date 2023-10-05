
package jsonvalues;


import java.util.Objects;

/**
 * Represents any element in a JSON object which can be modeled with a key and the associated element. Any JSON object
 * can be modeled as a {@link JsObj#streamOfKeys()} stream} of JsObjPair
 *
 */
public final class JsObjPair {
    private final String key;
    private final JsValue value;

    /**
     *
     * @param key   the key
     * @param value the value associated to the key
     */
    JsObjPair(String key, JsValue value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public JsValue value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        JsObjPair that = (JsObjPair) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "JsObjPair[" +
                "key=" + key + ", " +
                "value=" + value + ']';
    }
}
