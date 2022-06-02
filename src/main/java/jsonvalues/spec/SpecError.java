package jsonvalues.spec;

import fun.tuple.Pair;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class SpecError {

    public final JsPath path;
    public final ERROR_CODE errorCode;
    public final JsValue value;

    private SpecError(final JsPath path,
                      final Pair<JsValue, ERROR_CODE> pair
    ) {
        this.path = path;
        this.errorCode = pair.second();
        this.value = pair.first();
    }

    static SpecError of(final JsPath path,
                        final Pair<JsValue, ERROR_CODE> error) {
        return new SpecError(requireNonNull(path),
                             requireNonNull(error)
        );
    }

    @Override
    public String toString() {
        return "SpecError{" +
                "path=" + (path.isEmpty() ?
                           "root" :
                           path) +
                ", errorCode=" + errorCode +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecError specError = (SpecError) o;
        return Objects.equals(path,
                              specError.path) &&
                errorCode == specError.errorCode &&
                Objects.equals(value,
                               specError.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path,
                            errorCode,
                            value);
    }
}
