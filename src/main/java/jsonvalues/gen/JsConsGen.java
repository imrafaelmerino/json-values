package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsBool;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 * @param <O>
 */
public final class JsConsGen<O extends JsValue> implements Gen<O> {

    /**
     *Constant generator that generates always {@link JsNull#NULL}
     */
    public static final Gen<JsValue> NULL = new JsConsGen<>(JsNull.NULL);
    /**
     *Constant generator that generates always {@link JsBool#TRUE}
     */
    public static final Gen<JsBool> TRUE = new JsConsGen<>(JsBool.TRUE);
    /**
     *Constant generator that generates always {@link JsBool#FALSE}
     */
    public static final Gen<JsBool> FALSE = new JsConsGen<>(JsBool.FALSE);

    private final O value;

    private JsConsGen(O value) {
        this.value = value;
    }

    /**
     * returns a constant generator that generates always the specified value
     * @param value the value returned by the generator
     * @return a constant generator
     * @param <O> the type of the returned value, that is a subtype of {@link JsValue}
     */
    public static <O extends JsValue> Gen<O> cons(O value) {
        return new JsConsGen<>(requireNonNull(value));
    }

    /**
     * Returns a supplier that generates always the same value
     * @param seed the generator seed, which is not used at all
     * @return a supplier that returns always the same constant
     */
    @Override
    public Supplier<O> apply(final Random seed) {
        return () -> value;
    }
}
