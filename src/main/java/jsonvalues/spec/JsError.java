package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Objects;

public final class JsError {
    public final ERROR_CODE code;
    public final JsValue value;

    public JsError(final JsValue value,
                   final ERROR_CODE code) {
        this.code = Objects.requireNonNull(code);
        this.value = Objects.requireNonNull(value);
    }


    @Override
    public String toString() {
        return "(" +
                "code=" + code +
                ", value=" + value +
                ')';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JsError)) return false;
        final JsError error = (JsError) o;
        return code == error.code &&
                value.equals(error.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code,
                            value
                           );
    }
}
