package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Objects;

public class Error {
    public final ERROR_CODE code;
    public final JsValue value;

    public Error(final JsValue value,
                 final ERROR_CODE code) {
        this.code = code;
        this.value = value;
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
        if (o == null || getClass() != o.getClass()) return false;
        final Error error = (Error) o;
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
