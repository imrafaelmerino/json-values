package jsonvalues.gen;

import static java.util.Objects.requireNonNull;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.SplitGen;
import fun.tuple.Pair;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

/**
 * Represents a JsArray generator. It can be created from the static factory methods biased and arbitrary, specifying an
 * element generator that produces JsValue and the size of the array (either a bound interval or a fixed size).
 */
public final class JsArrayGen implements Gen<JsArray> {

  private final int size;
  private final Gen<? extends JsValue> gen;

  private JsArrayGen(final Gen<? extends JsValue> gen,
                     final int size
                    ) {
    if (size < 0) {
      throw new IllegalArgumentException("size < 0");
    }
    this.size = size;
    this.gen = requireNonNull(gen);
  }


  /**
   * Returns a fixed-size array generator.
   *
   * @param gen  The element generator.
   * @param size The size of the generated array.
   * @return A JsArray generator.
   */

  public static Gen<JsArray> ofN(final Gen<? extends JsValue> gen,
                                 final int size
                                ) {
    return new JsArrayGen(gen,
                          size
    );
  }

  /**
   * Returns an array generator.
   *
   * @param gen     The element generator.
   * @param minSize The minimum size of the arrays.
   * @param maxSize The maximum size of the arrays.
   * @return A JsArray generator.
   */
  public static Gen<JsArray> arbitrary(final Gen<? extends JsValue> gen,
                                       final int minSize,
                                       final int maxSize
                                      ) {
    if (minSize < 0) {
      throw new IllegalArgumentException("minSize < 0");
    }
    if (maxSize < minSize) {
      throw new IllegalArgumentException("maxSize < minSize");
    }
    requireNonNull(gen);
    return seed -> {
      var sizeSupplier = IntGen.arbitrary(minSize,
                                          maxSize)
                               .apply(SplitGen.DEFAULT.apply(seed));

      var elemSupplier = gen.apply(SplitGen.DEFAULT.apply(seed));
      return arraySupplier(elemSupplier,
                           sizeSupplier);
    };

  }

  /**
   * Returns a biased array generator.
   *
   * @param gen     The element generator.
   * @param minSize The minimum size of the arrays.
   * @param maxSize The maximum size of the arrays.
   * @return A JsArray generator.
   */
  public static Gen<JsArray> biased(final Gen<? extends JsValue> gen,
                                    final int minSize,
                                    final int maxSize
                                   ) {
    if (minSize < 0) {
      throw new IllegalArgumentException("minSize < 0");
    }
    if (maxSize < minSize) {
      throw new IllegalArgumentException("maxSize < minSize");
    }
    requireNonNull(gen);
    return Combinators.freq(Pair.of(1,
                                    new JsArrayGen(gen,
                                                   minSize)),
                            Pair.of(1,
                                    new JsArrayGen(gen,
                                                   maxSize)),
                            Pair.of(2,
                                    JsArrayGen.arbitrary(gen,
                                                         minSize,
                                                         maxSize))
                           );
  }


  private static Supplier<JsArray> arraySupplier(Supplier<? extends JsValue> elemSupplier,
                                                 Supplier<Integer> sizeSupplier
                                                ) {
    return () -> JsArray.ofIterable(IntStream.range(0,
                                                    sizeSupplier.get())
                                             .mapToObj(i -> elemSupplier.get())
                                             .collect(Collectors.toList())
                                   );
  }


  @Override
  public Supplier<JsArray> apply(final RandomGenerator seed) {
    return arraySupplier(gen.apply(requireNonNull(seed)),
                         () -> size);
  }


}
