package jsonvalues;

import java.util.Objects;

/**
 * Represents any element in a JSON which can be modeled with a path location the associated element. Any JSON can be
 * modeled as a {@link Json#stream() stream} of JsPair
 *
 */
public final class JsPair {
    private final JsPath path;
    private final JsValue value;

    /**
     *
     * @param path  the location of the value
     * @param value the value itself
     */
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
