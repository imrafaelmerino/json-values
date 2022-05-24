package jsonvalues.gen;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SplitGen;
import fun.tuple.Pair;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * represent a JsArray generator. It can be created from the static factory methods biased and
 * arbitrary, specifying an element generator that produces JsValue and the size of the array
 * (either a bound interval or a fixed size).
 */
public final class JsArrayGen implements Gen<JsArray> {

    private final int size;
    private final Gen<? extends JsValue> gen;

    private JsArrayGen(final Gen<? extends JsValue> gen,
                       final int size
    ) {
        if (size < 0) throw new IllegalArgumentException("size < 0");
        this.size = size;
        this.gen = requireNonNull(gen);
    }

    /**
     * @param gen  the element generator
     * @param size the size of the generated array
     * @return a JsArray generator
     */

    public static Gen<JsArray> arbitrary(final Gen<? extends JsValue> gen,
                                         final int size) {
        return new JsArrayGen(gen,
                              size
        );
    }

    /**
     * @param gen     the element generator
     * @param minSize the minimum size of the arrays
     * @param maxSize the maximum size of the arrays
     * @return a JsArray generator
     */
    public static Gen<JsArray> arbitrary(final Gen<? extends JsValue> gen,
                                         final int minSize,
                                         final int maxSize) {
        if (minSize < 0) throw new IllegalArgumentException("minSize < 0");
        if (maxSize < minSize) throw new IllegalArgumentException("maxSize < minSize");
        requireNonNull(gen);
        return seed -> {
            Supplier<Integer> sizeSupplier =
                    IntGen.arbitrary(minSize,
                                     maxSize)
                          .apply(SplitGen.DEFAULT.apply(seed));

            Supplier<? extends JsValue> elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
            return arraySupplier(elemSupplier,
                                 sizeSupplier);
        };

    }

    /**
     * @param gen     the element generator
     * @param minSize the minimum size of the arrays
     * @param maxSize the maximum size of the arrays
     * @return a JsArray generator
     */
    public static Gen<JsArray> biased(final Gen<? extends JsValue> gen,
                                      final int minSize,
                                      final int maxSize) {
        if (minSize < 0) throw new IllegalArgumentException("minSize < 0");
        if (maxSize < minSize) throw new IllegalArgumentException("maxSize < minSize");
        requireNonNull(gen);
        return Combinators.freq(new Pair<>(1,
                                           new JsArrayGen(gen,
                                                          minSize
                                           )),
                                new Pair<>(1,
                                           new JsArrayGen(gen,
                                                          maxSize
                                           )),
                                new Pair<>(2,
                                           JsArrayGen.arbitrary(gen,
                                                                minSize,
                                                                maxSize)));


    }


    private static Supplier<JsArray> arraySupplier(Supplier<? extends JsValue> elemSupplier,
                                                   Supplier<Integer> sizeSupplier) {


        return () -> JsArray.ofIterable(IntStream.range(0,
                                                        sizeSupplier.get())
                                                 .mapToObj(i -> elemSupplier.get())
                                                 .collect(Collectors.toList()));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsArray each time it's called
     *
     * @param seed the generator seed
     * @return a JsArray supplier
     */
    @Override
    public Supplier<JsArray> apply(final Random seed) {
        return arraySupplier(gen.apply(requireNonNull(seed)),
                             () -> size);
    }


}
