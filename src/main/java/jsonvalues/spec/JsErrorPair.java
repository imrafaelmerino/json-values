package jsonvalues.spec;

import jsonvalues.JsPath;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class JsErrorPair {

    public final JsPath path;
    public final Error error;

    private JsErrorPair(final JsPath path,
                        final Error error
                       ) {
        this.path = path;
        this.error = error;
    }

    public static JsErrorPair of(final JsPath path,
                                 final Error error) {
        return new JsErrorPair(requireNonNull(path),
                               requireNonNull(error)
        );
    }

    @Override
    public String toString() {
        return "(" +
                "path=" + path +
                ", error=" + error +
                ')';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final JsErrorPair that = (JsErrorPair) o;
        return path.equals(that.path) &&
                error.equals(that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path,
                            error
                           );
    }
}
