package jsonvalues;

import java.util.Objects;

public final class JsObjPair {
    private final String key;
    private final JsValue value;

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
        return "JsPair[" +
                "key=" + key + ", " +
                "value=" + value + ']';
    }


}
