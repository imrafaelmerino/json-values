package jsonvalues;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JsInstant implements JsValue{

    public static final int ID = 11;
    public final Instant value;

    public static JsInstant of(final Instant instant){
        return new JsInstant(Objects.requireNonNull(instant));
    }

    private JsInstant(final Instant value) {
        this.value = value;
    }

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
}
