package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Objects;

public final class JsError {
    private final JsValue value;
    private final ERROR_CODE error;

    JsError(JsValue value, ERROR_CODE error) {
        this.value = value;
        this.error = error;
    }

    public JsValue value() {
        return value;
    }

    public ERROR_CODE error() {
        return error;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        JsError that = (JsError) obj;
        return Objects.equals(this.value, that.value) &&
                Objects.equals(this.error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return "JsError[" +
                "value=" + value + ", " +
                "error=" + error + ']';
    }

}
