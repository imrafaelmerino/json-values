package jsonvalues.gen;

import io.vavr.Tuple2;
import jsonvalues.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class JsGens
{
  private static final int Z = 122;
  private final static int A = 97;
  private final static int ZERO = 48;
  private static final String LENGTH_EQUAL_ZERO_ERROR = "string length must be greater than zero.";

  public static JsGen<JsStr> character = r -> () -> JsStr.of(Character.toString(((char) r.nextInt(256))));

  public static JsGen<JsStr> characterAlpha = r -> () -> JsStr.of(Character.toString(((char) choose(65,
                                                                                                    122
                                                                                                   ).apply(r)
                                                                                                    .get().value))
                                                                 );
  public static JsGen<JsStr> digit = r -> () -> JsStr.of(Integer.toString(r.nextInt(10)));


  public static JsGen<JsStr> letter = r -> () ->
  {
    char c = (char) (r.nextInt(26) + 'a');
    return JsStr.of(Character.toString(c));
  };

  public static JsGen<JsInt> natural = r -> () -> JsInt.of(r.nextInt(Integer.MAX_VALUE));


  public static JsGen<JsStr> str = choose(0,
                                          10
                                         ).flatMap(n -> str(n.value));

  public static JsGen<JsStr> alphabetic = choose(0,
                                                 10
                                                ).flatMap(n -> alphabetic(n.value));

  public static JsGen<JsStr> alphanumeric = choose(0,
                                                   10
                                                  ).flatMap(n -> alphanumeric(n.value));

  public static JsGen<JsStr> str(final int length)
  {
    if (length < 0) throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () ->
    {
      if (length == 0) return JsStr.of("");
      byte[] array = new byte[r.nextInt(length)];
      new Random().nextBytes(array);
      return JsStr.of(new String(array,
                                 StandardCharsets.UTF_8
                      )
                     );
    };
  }

  public static JsGen<JsBool> bool = r -> () -> JsBool.of(r.nextBoolean());

  public static JsGen<JsLong> longInteger = r -> () -> JsLong.of(r.nextLong());

  public static JsGen<JsInt> integer = r -> () -> JsInt.of(r.nextInt());

  public static JsGen<JsDouble> decimal = r -> () -> JsDouble.of(r.nextDouble());

  public static JsGen<JsStr> alphabetic(final int length)
  {
    if (length < 0) throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () -> JsStr.of(r.ints(A,
                                      Z + 1
                                     )
                                .limit(length)
                                .collect(StringBuilder::new,
                                         StringBuilder::appendCodePoint,
                                         StringBuilder::append
                                        )
                                .toString());
  }

  public static JsGen<JsStr> alphanumeric(final int length)
  {
    if (length < 0) throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () -> JsStr.of(r.ints(ZERO,
                                      Z + 1
                                     )
                                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                .limit(length)
                                .collect(StringBuilder::new,
                                         StringBuilder::appendCodePoint,
                                         StringBuilder::append
                                        )
                                .toString());
  }

  public static JsGen<JsInt> choose(final int min,
                                    final int max
                                   )
  {
    if (min > max) throw new IllegalArgumentException("min must be lower than max");
    return r -> () -> JsInt.of(r.nextInt((max - min) + 1) + min);
  }

  public static JsGen<JsValue> single(final JsValue value)
  {
    return r -> () -> requireNonNull(value);
  }


  public static JsGen<JsValue> oneOf(final JsValue a,
                                     final JsValue... others
                                    )
  {
    return r -> () ->
    {
      final int n = r.nextInt(others.length + 1);
      if (n == 0) return requireNonNull(a);
      else return requireNonNull(others)[others.length - 1];
    };
  }

  public static JsGen<?> oneOf(final JsGen<?> a,
                               final JsGen<?>... others
                              )
  {
    int n = new Random().nextInt(1 + others.length);
    final List<JsGen<?>> gens = new ArrayList<>();
    gens.add(requireNonNull(a));
    Collections.addAll(gens,
                       requireNonNull(others)
                      );
    return gens.get(n);
  }

  public static <O extends JsValue> JsGen<O> oneOf(final List<O> list)
  {
    return r -> () ->
    {
      if (requireNonNull(list).size() == 0) return list.get(0);
      final int index = r.nextInt(list.size());
      return list.get(index);
    };
  }


  public static JsGen<JsArray> tuple(final JsGen<?> gen,
                                     final JsGen<?>... others
                                    )
  {
    return new JsTupleGen(gen,
                          others
    );
  }

  public static JsGen<JsArray> array(final JsGen<?> gen,
                                     final int size
                                    )
  {
    return JsArrayGen.of(gen,
                         size
                        );
  }

  public static JsGen<JsArray> arrayDistinct(final JsGen<?> gen,
                                             final int size
                                            )
  {
    return arrayDistinct(gen,
                         size,
                         100);
  }

  public static JsGen<JsArray> arrayDistinct(final JsGen<?> gen,
                                             final int size,
                                             final int maxTries
                                            )
  {

    return r -> () ->
    {
      int tries = 0;
      Set<JsValue> set = new HashSet<>();
      while (set.size() != size)
      {
        set.add(gen.apply(r)
                   .get());
        tries += 1;
        if (tries >= maxTries)
          throw new RuntimeException(String.format("Couldn't generate array of %s distinct elements  after %s tries",
                                                   size,
                                                   maxTries));
      }
      return JsArray.ofIterable(set);

    };
  }

  /**
   Creates a generator that chooses a generator from `pairs` based on the
   provided likelihoods. The likelihood of a given generator being chosen is
   its likelihood divided by the sum of all likelihoods. Shrinks toward
   choosing an earlier generator, as well as shrinking the value generated
   by the chosen generator.
   */
  public static JsGen<?> frequency(final Tuple2<Integer, JsGen<?>> freq,
                                   final Tuple2<Integer, JsGen<?>>... others
                                  )
  {

    final List<Tuple2<Integer, JsGen<?>>> filtered = Arrays.stream(others)
                                                           .filter(it -> it._1 > 0)
                                                           .collect(Collectors.toList());
    if (freq._1 > 0) filtered.add(freq);
    if (filtered.size() == 0)
      throw new IllegalArgumentException("no items with positive weights");
    int total = 0;
    TreeMap<Integer, JsGen<?>> treeMap = new TreeMap<>();
    for (Tuple2<Integer, JsGen<?>> t : filtered)
    {
      total += t._1;
      treeMap.put(total,
                  t._2
                 );
    }

    return choose(1,
                  total).flatMap(n -> treeMap.ceilingEntry(n.value)
                                             .getValue());


  }

  public static void main(String[] args)
  {

    Random r = new Random();
    for (int i = 0; i < 10; i++)
    {
      System.out.println(arrayDistinct(letter,
                    5).apply(r)
                      .get());
    }


  }


}
