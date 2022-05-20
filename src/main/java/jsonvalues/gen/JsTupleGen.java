package jsonvalues.gen;


import fun.gen.Gen;
import fun.gen.SplitGen;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsTupleGen implements Gen<JsArray> {
    private final List<Gen<? extends JsValue>> gens = new ArrayList<>();

    @SafeVarargs
    @SuppressWarnings("varargs")
    private JsTupleGen(final Gen<? extends JsValue> gen,
                       final Gen<? extends JsValue>... others
    ) {
        gens.add(requireNonNull(gen));
        gens.addAll(Arrays.asList(requireNonNull(others)));
    }

    /**
     *
     * @param gen
     * @param others
     * @return
     */

    @SafeVarargs
    public static Gen<JsArray> of(final Gen<? extends JsValue> gen,
                                  final Gen<? extends JsValue>... others
    ) {
        return new JsTupleGen(gen,
                              others);
    }

    /**
     *
     * @param random the function argument
     * @return
     */
    @Override
    public Supplier<JsArray> apply(final Random random) {
        requireNonNull(random);
        List<Supplier<? extends JsValue>> suppliers =
                gens.stream()
                    .map(it -> it.apply(SplitGen.DEFAULT.apply(random)))
                    .collect(Collectors.toList());
        return () ->
        {
            JsArray array = JsArray.empty();
            for (Supplier<? extends JsValue> supplier : suppliers) array = array.append(supplier.get());
            return array;
        };
    }


}
