package jsonvalues.gen;

import io.vavr.Tuple2;
import jsonvalues.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 Class with different kind of generators
 */
public class JsGens {
    private static final int Z = 122;
    private final static int A = 97;
    private final static int ZERO = 48;
    private static final String LENGTH_EQUAL_ZERO_ERROR = "length must be greater than zero.";

    /**
     Generates character from 0-255
     */
    public static JsGen<JsStr> character = r -> () -> JsStr.of(Character.toString(((char) requireNonNull(r).nextInt(256))));

    //TODO some chars are missing
    /**
     Generates character from 65-122
     */
    public static JsGen<JsStr> characterAlpha = choose(65,
                                                       122
                                                      ).map(i -> JsStr.of(Character.toString((char) i.value)));

    /**
     Generates a digit from 0-9
     */
    public static JsGen<JsStr> digit = choose(0,
                                              9
                                             ).map(i -> JsStr.of(Integer.toString(i.value)));


    /**
     Generates a seq of digits of the given length

     @param length the length of the string
     @return a string generator
     */
    public static JsGen<JsStr> digits(int length) {
        if (length <= 0) throw new IllegalArgumentException("length not greater than zero");
        return JsGens.array(digit,
                            length
                           )
                     .map(arr -> JsStr.of(arr.stream()
                                             .map(it -> it.value.toJsStr().value)
                                             .collect(Collectors.joining()))
                         );
    }


    /**
     Generates a letter from a-z
     */
    public static JsGen<JsStr> letter = choose(0,
                                               25
                                              ).map(i -> JsStr.of(Character.toString(((char) i.value))));

    /**
     Generates a positive integer (zero included)
     */
    public static JsGen<JsInt> natural = r -> () -> JsInt.of(requireNonNull(r).nextInt(Integer.MAX_VALUE));


    /**
     Generates a string where the length is between [0-10]
     */
    public static JsGen<JsStr> str = choose(0,
                                            10
                                           ).flatMap(n -> str(n.value));

    /**
     Generates a alphabetic string where the length is between [0-10]
     */
    public static JsGen<JsStr> alphabetic = choose(0,
                                                   10
                                                  ).flatMap(n -> alphabetic(n.value));

    /**
     Generates a alphanumeric string where the length is between [0-10]
     */
    public static JsGen<JsStr> alphanumeric = choose(0,
                                                     10
                                                    ).flatMap(n -> alphanumeric(n.value));
    /**
     Generates a boolean
     */
    public static JsGen<JsBool> bool = r -> () -> JsBool.of(requireNonNull(r).nextBoolean());
    /**
     Generates a long number
     */
    public static JsGen<JsLong> longInteger = r -> () -> JsLong.of(requireNonNull(r).nextLong());
    /**
     Generates an integer number
     */
    public static JsGen<JsInt> integer = r -> () -> JsInt.of(requireNonNull(r).nextInt());
    /**
     Generates a decimal number
     */
    public static JsGen<JsDouble> decimal = r -> () -> JsDouble.of(requireNonNull(r).nextDouble());

    /**
     Generates an alphabetic string of the given length

     @param length the length of the generated string
     @return a generator
     */
    public static JsGen<JsStr> str(final int length) {
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

    /**
     Generates an array of 1024 bytes
     */
    public static JsGen<JsBinary> binary =  binary(1024);

    /**
     Generates an array of bytes of the given length
     @param length the number of bytes generated
     @return an array of bytes generator
     */
    public static JsGen<JsBinary> binary(int length) {
        return r -> () -> {
            byte[] bytes = new byte[length];
            r.nextBytes(bytes);
            return JsBinary.of(bytes);
        };
    }


    /**
     Generates an alphabetic string of the given length

     @param length the length of the string
     @return a string generator
     */
    public static JsGen<JsStr> alphabetic(final int length) {
        if (length < 0) throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

        return r -> () -> JsStr.of(requireNonNull(r).ints(A,
                                                          Z + 1
                                                         )
                                                    .limit(length)
                                                    .collect(StringBuilder::new,
                                                             StringBuilder::appendCodePoint,
                                                             StringBuilder::append
                                                            )
                                                    .toString());
    }

    /**
     Generates an alphanumeric string of the given length

     @param length the length of the string
     @return a string generator
     */
    public static JsGen<JsStr> alphanumeric(final int length) {
        if (length < 0) throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

        return r -> () -> JsStr.of(requireNonNull(r).ints(ZERO,
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

    /**
     lift a constant value up into a generator

     @param value the value
     @return a generator that always produce the same value
     */
    public static JsGen<JsValue> single(final JsValue value) {
        return r -> () -> requireNonNull(value);
    }

    /**
     Creates a generator that randomly chooses a value from the given as parameters

     @param a      a value
     @param others the others values
     @return a generator
     */
    public static JsGen<JsValue> oneOf(final JsValue a,
                                       final JsValue... others
                                      ) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(others);
        return r -> () ->
        {
            final int n = requireNonNull(r).nextInt(others.length + 1);
            if (n == 0) return requireNonNull(a);
            else return requireNonNull(others)[others.length - 1];
        };
    }

    /**
     Creates a generator that randomly chooses a value from the provided generators

     @param gen    a generator
     @param others the others generators
     @return a generator
     */
    public static JsGen<?> oneOf(final JsGen<?> gen,
                                 final JsGen<?>... others
                                ) {
        final List<JsGen<?>> gens = new ArrayList<>();
        gens.add(requireNonNull(gen));
        Collections.addAll(gens,
                           requireNonNull(others)
                          );
        return r -> () ->
                gens.get(r.nextInt(1 + requireNonNull(others).length))
                    .apply(r)
                    .get();
    }

    /**
     Creates a generator that randomly chooses a value from the given list

     @param list the list of values
     @param <O>  the type of the generated value
     @return a generator
     */
    public static <O extends JsValue> JsGen<O> oneOf(final List<O> list) {
        if (requireNonNull(list).size() == 0)
            throw new RuntimeException("list empty. No value can be generated");

        return r -> () ->
        {
            final int index = requireNonNull(r).nextInt(list.size());
            return list.get(index);
        };
    }

    /**
     Creates a tuple generator from the given generators. The nth-generator generates
     the nth-element of the tuple

     @param gen    the head generator
     @param others the rest of generators
     @return an array generator
     */
    public static JsGen<JsArray> tuple(final JsGen<?> gen,
                                       final JsGen<?>... others
                                      ) {
        return new JsTupleGen(gen,
                              others
        );
    }

    /**
     Creates an array generator of the given size, whose elements are chosen from the given generator

     @param gen  the generator that elements are chosen from
     @param size the size of the generated array
     @return an array generator
     */
    public static JsGen<JsArray> array(final JsGen<?> gen,
                                       final int size
                                      ) {
        if (size < 0) throw new IllegalArgumentException("size negative");
        return JsArrayGen.of(requireNonNull(gen),
                             size
                            );
    }

    /**
     Generates an array of values from the given generator, with the guarantee that the elements will
     be distinct. If the generator cannot or is unlikely to produce enough distinct elements after 100
     tries, this generator will fail with a RuntimeException

     @param gen  the given generator
     @param size the size of the array
     @return a json generator
     */
    public static JsGen<JsArray> arrayDistinct(final JsGen<?> gen,
                                               final int size
                                              ) {
        if (size < 0) throw new IllegalArgumentException("size negative");

        return arrayDistinct(requireNonNull(gen),
                             size,
                             100
                            );
    }

    /**
     Generates an array of values from the given generator, with the guarantee that the elements will
     be distinct. If the generator cannot or is unlikely to produce enough distinct elements after the
     given number of tries, this generator will fail with a RuntimeException

     @param gen      the given generator
     @param size     the size of the array
     @param maxTries the max number of tries to produce distinct values
     @return a json generator
     */
    public static JsGen<JsArray> arrayDistinct(final JsGen<?> gen,
                                               final int size,
                                               final int maxTries
                                              ) {
        requireNonNull(gen);
        if (size < 0) throw new IllegalArgumentException("size negative");
        if (maxTries < 0) throw new IllegalArgumentException("maxTries negative");

        return r -> () ->
        {
            requireNonNull(r);
            int          tries = 0;
            Set<JsValue> set   = new HashSet<>();
            while (set.size() != size) {
                set.add(gen.apply(r)
                           .get());
                tries += 1;
                if (tries >= maxTries)
                    throw new RuntimeException(String.format("Couldn't generate array of %s distinct elements after %s tries",
                                                             size,
                                                             maxTries
                                                            ));
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

     @param freq   a frequency pair
     @param others the rests of pairs
     @return a json generator
     */
    @SafeVarargs
    public static JsGen<?> frequency(final Tuple2<Integer, JsGen<?>> freq,
                                     final Tuple2<Integer, JsGen<?>>... others
                                    ) {

        @SuppressWarnings("varargs")//method does not throw a ClassCastException
        final List<Tuple2<Integer, JsGen<?>>> filtered = Arrays.stream(requireNonNull(others))
                                                               .filter(it -> it._1 > 0)
                                                               .collect(Collectors.toList());
        if (requireNonNull(freq)._1 > 0) filtered.add(freq);
        if (filtered.size() == 0) {
            throw new IllegalArgumentException("no items with positive weights");
        }
        int                        total   = 0;
        TreeMap<Integer, JsGen<?>> treeMap = new TreeMap<>();
        for (Tuple2<Integer, JsGen<?>> t : filtered) {
            total += t._1;
            treeMap.put(total,
                        t._2
                       );
        }

        return choose(1,
                      total
                     ).flatMap(n -> treeMap.ceilingEntry(n.value)
                                           .getValue());


    }

    /**
     Creates a generator that generates integers in the range`min` to `max`, inclusive.

     @param min the minimum value
     @param max the maximum value
     @return an integer generator
     */
    public static JsGen<JsInt> choose(final int min,
                                      final int max
                                     ) {
        if (min > max) throw new IllegalArgumentException("min must be lower than max");
        return r -> () -> JsInt.of(requireNonNull(r).nextInt((max - min) + 1) + min);
    }


    /**
     generates an instant in UTC formatted with the ISO instant formatter (such as '2011-12-03T10:15:30Z'), between
     an interval given by two date-time with a time-zone.

     @param min the origin of the interval (inclusive)
     @param max the bound of the interval (inclusive)
     @return an instant generator
     */

    public static JsGen<JsInstant> dateBetween(final ZonedDateTime min,
                                               final ZonedDateTime max) {
        if (requireNonNull(max).isBefore(requireNonNull(min)))
            throw new IllegalArgumentException(min.format(DateTimeFormatter.ISO_INSTANT) + " is greater than " + max.format(DateTimeFormatter.ISO_INSTANT));

        return dateBetween(min.toEpochSecond(),
                           max.toEpochSecond()
                          );

    }


    /**
     generates an instant in UTC formatted with the ISO instant formatter (such as '2011-12-03T10:15:30Z'), between
     an interval given by two instants converted to the number of seconds from the epoch of 1970-01-01T00:00:00Z.

     @param min the origin of the interval (inclusive)
     @param max the bound of the interval (inclusive)
     @return an instant generator
     */
    public static JsGen<JsInstant> dateBetween(final long min,
                                               final long max) {
        if (max <= min)
            throw new IllegalArgumentException(max + " is greater than " + min);

        return random -> () -> {

            long seconds = requireNonNull(random).longs(1,
                                                        min,
                                                        max + 1
                                                       )
                                                 .findFirst()
                                                 .getAsLong();

            return JsInstant.of(Instant.ofEpochSecond(seconds));
        };
    }

    /**
     generates a constant
     @param value the constant
     @return a generator
     */
    public static JsGen<JsValue> cons(final JsValue value){
        requireNonNull(value);
        return random -> () -> value;
    }


}
