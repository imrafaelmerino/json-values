package jsonvalues;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class JsInstant implements JsValue{

    public static final int ID = 11;
    public final Instant value;

    public static JsInstant of(final Instant instant){
        return new JsInstant(Objects.requireNonNull(instant));
    }

    private JsInstant(final Instant value) {
        this.value = value;
    }

    /**
     prism between the sum type JsValue and JsInstant
     */
    public static Prism<JsValue, Instant> prism =
            new Prism<>(s -> s.isInstant() ? Optional.of(s.toJsInstant().value) : Optional.empty(),
                        JsInstant::of
            );
    @Override
    public int id() {
        return ID;
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
        if (o == null || getClass() != o.getClass()) return false;
        final JsInstant jsInstant = (JsInstant) o;
        return value.equals(jsInstant.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
