package jsonvalues.spec;

import jsonvalues.JsPath;

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
}
