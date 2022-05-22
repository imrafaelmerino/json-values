package jsonvalues.gen;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SplitGen;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * represent a generator of Json arrays.
 */
public final class JsArrayGen implements Gen<JsArray> {

    private final int size;
    private final Gen<? extends JsValue> gen;

    /**
     * @param gen
     * @param size
     */
    public JsArrayGen(final Gen<? extends JsValue> gen,
                      final int size
    ) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        this.size = size;
        this.gen = requireNonNull(gen);
    }

    /**
     * @param size
     * @return
     */

    public static Gen<JsArray> arbitrary(final Gen<? extends JsValue> gen,
                                         final int size) {
        return new JsArrayGen(gen,
                              size
        );
    }

    /**
     * @param minLength
     * @param maxLength
     * @return
     */
    public static Gen<JsArray> arbitrary(final Gen<? extends JsValue> gen,
                                         final int minLength,
                                         final int maxLength) {
        if (minLength < 0) throw new IllegalArgumentException("min < 0");
        if (maxLength < minLength) throw new IllegalArgumentException("max < min");
        requireNonNull(gen);
        return seed -> {
            Supplier<Integer> sizeSupplier =
                    IntGen.arbitrary(minLength,
                                     maxLength)
                          .apply(SplitGen.DEFAULT.apply(seed));

            Supplier<? extends JsValue> elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
            return arraySupplier(elemSupplier,
                                 sizeSupplier);
        };

    }

    /**
     * @param min
     * @param max
     * @return
     */
    public static Gen<JsArray> biased(final Gen<? extends JsValue> gen,
                                      final int min,
                                      final int max) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max < min) throw new IllegalArgumentException("max < min");
        requireNonNull(gen);
        return Combinators.freq(new Pair<>(1,
                                           new JsArrayGen(gen,
                                                          min
                                           )),
                                new Pair<>(1,
                                           new JsArrayGen(gen,
                                                          max
                                           )),
                                new Pair<>(2,
                                           JsArrayGen.arbitrary(gen,
                                                                min,
                                                                max)));


    }

    /**
     * @param elemSupplier
     * @param sizeSupplier
     * @return
     */
    private static Supplier<JsArray> arraySupplier(Supplier<? extends JsValue> elemSupplier,
                                                   Supplier<Integer> sizeSupplier) {


        return () -> JsArray.ofIterable(IntStream.range(0,
                                                        sizeSupplier.get())
                                                 .mapToObj(i -> elemSupplier.get())
                                                 .collect(Collectors.toList()));
    }

    /**
     * @param random the function argument
     * @return
     */
    @Override
    public Supplier<JsArray> apply(final Random random) {
        Objects.requireNonNull(random);
        return arraySupplier(gen.apply(requireNonNull(random)),
                             () -> size);
    }


}
