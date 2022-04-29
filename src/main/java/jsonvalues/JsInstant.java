package jsonvalues;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable instant. An instant is not part of the Json specification. It it serialized into
 its a string representation using ISO-8601 representation. A JsInstant and a JsStr are equals
 if both represent the same date.
 {@code
 Instant a = Instant.now();
 JsStr.of(a.toString()).equals(JsIntant.of(a)) // true
 }
 */
public final class JsInstant  extends JsPrimitive implements Comparable<JsInstant>{

    public static final int TYPE_ID = 11;
    public final Instant value;

    public static JsInstant of(final Instant instant) {
        return new JsInstant(Objects.requireNonNull(instant));
    }

    private JsInstant(final Instant value) {
        this.value = value;
    }

    public JsInstant map(Function<Instant,Instant> fn){
        return JsInstant.of(Objects.requireNonNull(fn).apply(value));
    }

    /**
     prism between the sum type JsValue and JsInstant
     */
    public static final Prism<JsValue, Instant> prism =
            new Prism<>(s -> {
                if (s.isInstant()) return Optional.of(s.toJsInstant().value);
                if (s.isStr()) {
                    return JsStr.instantPrism.getOptional.apply(s.toJsStr().value);
                }
                return Optional.empty();
            },
                        JsInstant::of
            );

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof JsValue) {
            return JsInstant.prism.getOptional.apply(((JsValue) o))
                                              .map(instant -> instant.equals(value))
                                              .orElse(false);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(final JsInstant o) {
        return value.compareTo(requireNonNull(o).value);

    }
}
