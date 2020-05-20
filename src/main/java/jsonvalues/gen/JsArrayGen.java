package jsonvalues.gen;

import jsonvalues.JsArray;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 represent a generator of Json arrays.
 */
final class JsArrayGen implements JsGen<JsArray> {

    private final int size;
    private final JsGen<?> gen;

    private JsArrayGen(final int size,
                       final JsGen<?> gen
                      ) {
        this.size = size;
        this.gen = gen;
    }

    public static JsArrayGen of(final JsGen<?> gen,
                                final int size
                               ) {
        return new JsArrayGen(size,
                              gen
        );
    }

    @Override
    public Supplier<JsArray> apply(final Random random) {
        Objects.requireNonNull(random);
        return () ->
        {
            JsArray array = JsArray.empty();
            for (int i = 0; i < size; i++)
                array = array.append(gen.apply(requireNonNull(random))
                                        .get());
            return array;
        };
    }


}
