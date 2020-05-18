package jsonvalues.spec;

import jsonvalues.JsValue;

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
}
