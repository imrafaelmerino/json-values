package jsonvalues;

import java.util.Objects;

public final class JsPair {
    private final JsPath path;
    private final JsValue value;

    JsPair(JsPath path, JsValue value) {
        this.path = path;
        this.value = value;
    }

    public JsPath path() {
        return path;
    }

    public JsValue value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        JsPair that = (JsPair) obj;
        return Objects.equals(this.path, that.path) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, value);
    }

    @Override
    public String toString() {
        return "JsPair[" +
                "path=" + path + ", " +
                "value=" + value + ']';
    }


}
