package jsonvalues;


import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsObj.streamOfObj;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;

import fun.optic.Prism;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import jsonvalues.spec.JsIO;
import jsonvalues.spec.JsParserException;


/**
 * Represents a JSON array in a persistent data structure.
 *
 * <p>A {@code JsArray} is a collection of JSON values arranged in an ordered sequence. It allows for easy manipulation
 * of JSON arrays, such as adding, updating, or removing elements, while preserving immutability. This class is designed
 * to be used for creating and working with JSON arrays in a functional and safe manner.
 *
 * <p>{@code JsArray} implements the {@link Json} interface, which allows it to seamlessly integrate with other
 * JSON-related classes and operations.
 *
 * <p>As a persistent data structure, modifications to a {@code JsArray} result in a new instance being created,
 * leaving the original array unchanged. This immutability ensures safe concurrent access and simplifies handling of
 * JSON data.
 */
public final class JsArray implements Json<JsArray>, Iterable<JsValue> {

  /**
   * Provides a set of optics (lenses) for working with JSON arrays.
   * <p>The {@code JsArrayLenses} class offers a collection of optics that allow you to focus on specific elements
   * within a JSON array, making it easier to perform targeted modifications and transformations. These optics are
   * designed to work seamlessly with the {@link JsArray} class, enabling precise handling of JSON array data.
   */
  public static final JsOptics.JsArrayLenses lens = JsOptics.array.lens;
  /**
   * Provides a set of optional optics for working with JSON arrays.
   * <p>The {@code JsArrayOptionals} class offers a collection of optional optics that allow you to work with JSON
   * arrays more flexibly. These optics provide ways to safely access and manipulate elements within a JSON array,
   * taking into account the possibility of absent or missing elements.
   */
  public static final JsOptics.JsArrayOptionals optional = JsOptics.array.optional;
  /**
   * A prism for safely converting a {@link JsValue} to a {@link JsArray}.
   *
   * <p>The {@code prism} field provides a way to safely attempt the conversion of a {@link JsValue} into a
   * {@link JsArray}. It checks whether the given {@link JsValue} is actually an array and performs the conversion if it
   * is. Otherwise, it returns an empty {@link Optional}.
   */
  public static final Prism<JsValue, JsArray> prism =
      new Prism<>(
          s -> s.isArray() ?
               Optional.of(s.toJsArray()) :
               Optional.empty(),
          a -> a
      );
  static final JsArray EMPTY = new JsArray(Vector.empty());
  private final Vector<JsValue> seq;
  private volatile int hashcode;
  private volatile String str;

  JsArray(Vector<JsValue> seq) {
    this.seq = requireNonNull(seq);
  }

  /**
   * Returns the singleton empty JSON array
   *
   * @return the singleton empty JSON array
   */
  public static JsArray empty() {
    return EMPTY;
  }


  /**
   * Creates a new {@link JsArray} containing the specified elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified elements in the order they
   * appear in the method's argument list. You can provide one or more {@link JsValue} elements to include in the
   * resulting array.
   *
   * @param values Additional {@link JsValue} elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified elements.
   * @throws NullPointerException if any of the provided elements (including {@code e} and {@code rest}) are null.
   */
  public static JsArray of(final JsValue... values) {
    JsArray result = JsArray.empty();
    for (JsValue other : requireNonNull(values)) {
      result = result.append(other);
    }
    return result;


  }


  /**
   * Creates a new {@link JsArray} containing the specified string elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified string elements in the order
   * they appear in the method's argument list. You can provide one or more strings to include in the resulting array.
   *
   * <p>Example usage:
   * <pre>{@code
   * // Creating a JsArray with specified string elements
   * JsArray array = JsArray.of("Hello", "World");
   * }</pre>
   *
   * @param str    The first string element to include in the array.
   * @param others Additional string elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified string elements.
   * @throws NullPointerException if any of the provided string elements (including {@code str} and {@code others}) are
   *                              null.
   */
  public static JsArray of(String str,
                           String... others
                          ) {

    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsStr.of(str));
    for (String a : others) {
      vector = vector.append(JsStr.of(a));
    }
    return new JsArray(vector
    );
  }

  /**
   * Creates a new {@link JsArray} containing the specified integer elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified integer elements in the order
   * they appear in the method's argument list. You can provide one or more integers to include in the resulting array.
   *
   * @param number The first integer element to include in the array.
   * @param others Additional integer elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified integer elements.
   */
  public static JsArray of(int number,
                           int... others
                          ) {

    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsInt.of(number));
    for (int a : others) {
      vector = vector.append(JsInt.of(a));
    }
    return new JsArray(vector
    );
  }

  /**
   * Creates a new {@link JsArray} containing the specified boolean elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified boolean elements in the order
   * they appear in the method's argument list. You can provide one or more boolean values to include in the resulting
   * array.
   *
   * @param bool   The first boolean element to include in the array.
   * @param others Additional boolean elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified boolean elements.
   */
  public static JsArray of(final boolean bool,
                           final boolean... others
                          ) {
    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsBool.of(bool));
    for (boolean a : others) {
      vector = vector.append(JsBool.of(a));
    }
    return new JsArray(vector
    );
  }

  /**
   * Creates a new {@link JsArray} containing the specified long elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified long elements in the order
   * they appear in the method's argument list. You can provide one or more long values to include in the resulting
   * array.
   *
   * @param number The first long element to include in the array.
   * @param others Additional long elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified long elements.
   */
  public static JsArray of(final long number,
                           final long... others
                          ) {

    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsLong.of(number));
    for (long a : others) {
      vector = vector.append(JsLong.of(a));
    }
    return new JsArray(vector

    );
  }

  /**
   * Creates a new {@link JsArray} containing the specified {@link BigInteger} elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified {@link BigInteger} elements
   * in the order they appear in the method's argument list. You can provide one or more {@link BigInteger} values to
   * include in the resulting array.
   *
   * @param number The first {@link BigInteger} element to include in the array.
   * @param others Additional {@link BigInteger} elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified {@link BigInteger} elements.
   */
  public static JsArray of(final BigInteger number,
                           final BigInteger... others
                          ) {

    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsBigInt.of(number));
    for (BigInteger a : others) {
      vector = vector.append(JsBigInt.of(a));
    }
    return new JsArray(vector);
  }

  /**
   * Creates a new {@link JsArray} containing the specified {@code double} elements.
   *
   * <p>The {@code of} method returns a new {@link JsArray} instance with the specified {@code double} elements in
   * the order they appear in the method's argument list. You can provide one or more {@code double} values to include
   * in the resulting array.
   *
   * @param number The first {@code double} element to include in the array.
   * @param others Additional {@code double} elements to include in the array (optional).
   * @return A new {@link JsArray} containing the specified {@code double} elements.
   */
  public static JsArray of(final double number,
                           final double... others
                          ) {

    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsDouble.of(number));
    for (double a : others) {
      vector = vector.append(JsDouble.of(a));
    }
    return new JsArray(vector
    );
  }

  /**
   * Creates a new {@link JsArray} from an {@link Iterable} of {@link JsValue} elements.
   *
   * <p>The {@code ofIterable} method returns a new {@link JsArray} instance containing the elements provided by the
   * specified {@link Iterable}. This allows you to convert an existing collection of {@link JsValue} objects into a
   * {@link JsArray}.
   *
   * @param iterable The {@link Iterable} containing {@link JsValue} elements to include in the array.
   * @return A new {@link JsArray} containing the elements from the specified {@link Iterable}.
   */
  public static JsArray ofIterable(final Iterable<? extends JsValue> iterable) {
    Vector<JsValue> vector = Vector.empty();
    for (JsValue e : requireNonNull(iterable)) {
      vector = vector.append(e);
    }
    return new JsArray(vector);
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link String} elements. *
   *
   * @param list The {@link Collection} of strings to include in the array.
   * @return A new {@link JsArray}
   */
  public static JsArray ofStrs(final Collection<String> list) {
    return ofIterable(list.stream()
                          .map(JsStr::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link Integer} elements.
   *
   * @param list The {@link Collection} of integers to include in the array.
   * @return A new {@link JsArray} {@link Collection}.
   */
  public static JsArray ofInts(final Collection<Integer> list) {
    return ofIterable(list.stream()
                          .map(JsInt::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link Long} elements. *
   *
   * @param list The {@link Collection} of longs to include in the array.
   * @return A new {@link JsArray} {@link Collection}.
   */
  public static JsArray ofLongs(final Collection<Long> list) {
    return ofIterable(list.stream()
                          .map(JsLong::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link BigDecimal} elements.
   *
   * @param list The {@link Collection} of {@link BigDecimal} values to include in the array.
   * @return A new {@link JsArray}
   */
  public static JsArray ofDecs(final Collection<BigDecimal> list) {
    return ofIterable(list.stream()
                          .map(JsBigDec::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link Double} elements.
   *
   * @param list The {@link Collection} of {@link Double} values to include in the array.
   * @return A new {@link JsArray} {@link Collection}.
   */
  public static JsArray ofDoubles(final Collection<Double> list) {
    return ofIterable(list.stream()
                          .map(JsDouble::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link Collection} of {@link Boolean} elements.
   *
   * @param list The {@link Collection} of boolean values to include in the array.
   * @return A new {@link JsArray}
   */
  public static JsArray ofBools(final Collection<Boolean> list) {
    return ofIterable(list.stream()
                          .map(JsBool::of)
                          .toList());
  }

  /**
   * Creates a new {@link JsArray} from a {@link List} of {@link String} elements representing instant values.
   *
   * @param list The {@link List} of strings representing instant values to include in the array.
   * @return A new {@link JsArray}
   */
  public static JsArray ofInstants(final List<Instant> list) {
    return ofIterable(list.stream()
                          .map(JsInstant::of)
                          .toList());
  }


  /**
   * Creates a new {@link JsArray} from an array of {@link BigDecimal} elements.
   *
   * @param elem   the first element
   * @param others The rest {@link BigDecimal} values to include in the array.
   * @return A new {@link JsArray}
   */

  public static JsArray of(final BigDecimal elem,
                           final BigDecimal... others
                          ) {
    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsBigDec.of(elem));
    for (BigDecimal other : others) {
      vector = vector.append(JsBigDec.of(other));
    }
    return new JsArray(vector
    );
  }


  /**
   * Creates a new {@link JsArray} from an array of {@link Instant} elements representing instant values.
   *
   * @param elem   the first elements of the array
   * @param others The others elements representing instant values to include in the array.
   * @return A new {@link JsArray} containing the converted {@link JsInstant} elements.
   */
  public static JsArray of(final Instant elem,
                           final Instant... others
                          ) {
    Vector<JsValue> vector = Vector.<JsValue>empty()
                                   .append(JsInstant.of(elem));
    for (Instant a : others) {
      vector = vector.append(JsInstant.of(a));
    }
    return new JsArray(vector);
  }


  /**
   * Parses a JSON array represented as a string and returns a new {@link JsArray} instance.
   *
   * <p>The {@code parse} method takes a JSON array string as input, parses it, and returns a new {@link JsArray}
   * containing the parsed JSON values. If the input string is not a valid JSON array representation, a
   * {@link JsParserException} is thrown.
   *
   * @param str The JSON array string to parse.
   * @return A new {@link JsArray} containing the parsed JSON values.
   * @throws JsParserException If the input string is not a valid JSON array representation.
   * @see JsParserException
   */
  public static JsArray parse(final String str) throws JsParserException {

    return JsIO.INSTANCE.parseToJsArray(str.getBytes(StandardCharsets.UTF_8));

  }

  /**
   * Parses a JSON array represented as a byte array and returns a new {@link JsArray} instance.
   *
   * <p>The {@code parse} method takes a JSON array represented as a byte array, parses it, and returns a new
   * {@link JsArray} containing the parsed JSON values. If the input byte array does not represent a valid JSON array, a
   * {@link JsParserException} is thrown.
   *
   * @param bytes The byte array representing the JSON array to parse.
   * @return A new {@link JsArray} containing the parsed JSON values.
   * @throws JsParserException If the input byte array does not represent a valid JSON array.
   * @see JsParserException
   */
  public static JsArray parse(final byte[] bytes) throws JsParserException {

    return JsIO.INSTANCE.parseToJsArray(bytes);

  }


  static Stream<JsPair> streamOfArr(final JsArray array,
                                    final JsPath path
                                   ) {

    requireNonNull(path);
    return requireNonNull(array).ifEmptyElse(() -> Stream.of(new JsPair(path,
                                                                        array
                                             )),
                                             () -> range(0,
                                                         array.size()
                                                        ).mapToObj(pair -> new JsPair(path.index(pair),
                                                                                      array.get(Index.of(pair))
                                                         ))
                                                         .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                               pair.path()
                                                                                                              ),
                                                                                              a -> streamOfArr(a,
                                                                                                               pair.path()
                                                                                                              ),
                                                                                              e -> Stream.of(pair)
                                                                                             )
                                                                                  .apply(pair.value())
                                                                 )
                                            );


  }


  /**
   * Appends one or more {@link JsValue} elements to the end of this JSON array.
   *
   * <p>The {@code append} method allows you to add one or more {@link JsValue} elements to the end of this JSON
   * array. The elements are added in the order they appear in the argument list.
   *
   * @param e      The first {@link JsValue} element to append.
   * @param others Additional {@link JsValue} elements to append (optional).
   * @return A new {@link JsArray} containing the elements of this array followed by the appended elements.
   */
  public JsArray append(final JsValue e,
                        final JsValue... others
                       ) {
    Vector<JsValue> acc = this.seq.append(requireNonNull(e));
    for (JsValue other : requireNonNull(others)) {
      acc = acc.append(requireNonNull(other));
    }
    return new JsArray(acc);
  }

  /**
   * Appends all elements from another {@link JsArray} to the end of this JSON array.
   *
   * <p>The {@code appendAll} method allows you to add all elements from another {@link JsArray} to the end of this
   * JSON array. The elements are added in the order they appear in the source array.
   *
   * @param array The {@link JsArray} containing elements to append to this array.
   * @return A new {@link JsArray} containing the elements of this array followed by the elements from the source array.
   * @see JsArray
   */
  public JsArray appendAll(final JsArray array) {
    return appendAllBack(this,
                         requireNonNull(array)
                        );


  }

  private JsArray appendAllBack(JsArray arr1,
                                final JsArray arr2
                               ) {
    assert arr1 != null;
    assert arr2 != null;
    if (arr2.isEmpty()) {
      return arr1;
    }
    if (arr1.isEmpty()) {
      return arr2;
    }
    for (final JsValue value : arr2) {
      arr1 = arr1.append(value);
    }
    return arr1;
  }

  private JsArray appendAllFront(JsArray arr1,
                                 JsArray arr2
                                ) {
    assert arr1 != null;
    assert arr2 != null;
    if (arr2.isEmpty()) {
      return arr1;
    }
    if (arr1.isEmpty()) {
      return arr2;
    }
    for (final JsValue value : arr1) {
      arr2 = arr2.append(value);
    }
    return arr2;
  }

  /**
   * Checks if this JSON array is equal to another JSON array.
   *
   * <p>The {@code equals} method compares this JSON array to another JSON array to determine if they are equal.
   * Equality is defined based on the specified {@link TYPE} of array comparison. If {@code ARRAY_AS} is
   * {@link TYPE#SET}, both arrays must contain the same elements (order doesn't matter, duplicates are ignored). If
   * {@code ARRAY_AS} is {@link TYPE#LIST}, both arrays must have the same elements in the same order. If
   * {@code ARRAY_AS} is {@link TYPE#MULTISET}, both arrays must contain the same elements (order doesn't matter,
   * duplicates are counted).
   *
   * @param array    The {@link JsArray} to compare to this array.
   * @param ARRAY_AS The {@link TYPE} specifying the type of array comparison (SET, LIST, or MULTISET).
   * @return {@code true} if the arrays are equal according to the specified type, {@code false} otherwise.
   * @see TYPE
   */
  public boolean equals(final JsArray array,
                        final TYPE ARRAY_AS
                       ) {
    if (ARRAY_AS == LIST) {
      return this.equals(array);
    }
    if (isEmpty()) {
      return array.isEmpty();
    }
    if (array.isEmpty()) {
      return false;
    }
    return IntStream.range(0,
                           size()
                          )
                    .mapToObj(i -> get(Index.of(i)))
                    .allMatch(elem ->
                              {
                                if (!array.containsValue(elem)) {
                                  return false;
                                }
                                if (ARRAY_AS == MULTISET) {
                                  return seq.count(it -> it.equals(elem)) == array.seq.count(it -> it.equals(elem));
                                }
                                return true;
                              })
           && IntStream.range(0,
                              array.size()
                             )
                       .mapToObj(i -> array.get(Index.of(i)))
                       .allMatch(this::containsValue);
  }

  /**
   * Returns the integral number located at the given index as an integer or null if it doesn't exist, or it's not an
   * integral number, or it's an integral number but doesn't fit in an integer.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to parse it as an
   * integral number. If the value at the index is a valid integral number that can be represented as an integer, it is
   * returned as an {@code Integer}. If the value is not an integral number or cannot fit in an integer, this method
   * returns {@code null}.
   *
   * <p>Example usage:
   * <pre>{@code
   * // Create a JSON array and retrieve an integral number as an integer
   * JsArray array = JsArray.of(1, 2, 3);
   * Integer value = array.getInt(1); // Returns 2
   * }</pre>
   *
   * @param index The index of the JSON value to retrieve as an integer.
   * @return The integral number located at the given index as an {@code Integer}, or {@code null} if it doesn't exist
   * or is not a valid integral number.
   */
  public Integer getInt(final int index) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return null;
    }
    return Fun.getInt(seq.get(index));

  }

  /**
   * Returns the integral number located at the given index as an integer or a default value if it doesn't exist, or
   * it's not an integral number, or it's an integral number but doesn't fit in an integer.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to parse it as an
   * integral number. If the value at the index is a valid integral number that can be represented as an integer, it is
   * returned as an {@code Integer}. If the value is not an integral number or cannot fit in an integer, the default
   * value provided by the {@code orElse} supplier is returned.
   *
   * @param index  The index of the JSON value to retrieve as an integer.
   * @param orElse A supplier that provides a default integer value to return if the JSON value at the index is not a
   *               valid integral number or cannot fit in an integer.
   * @return The integral number located at the given index as an {@code Integer}, or the default value provided by
   * {@code orElse} if it doesn't exist or is not a valid integral number.
   * @see JsArray
   */
  public int getInt(final int index,
                    final Supplier<Integer> orElse
                   ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getInt(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Returns the integral number located at the given index as a long or null if it doesn't exist, or it's not an
   * integral number, or it's an integral number but doesn't fit in a long.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to parse it as an
   * integral number. If the value at the index is a valid integral number that can be represented as a long, it is
   * returned as a {@code Long}. If the value is not an integral number or cannot fit in a long, {@code null} is
   * returned.
   *
   * @param index The index of the JSON value to retrieve as a long.
   * @return The integral number located at the given index as a {@code Long}, or {@code null} if it doesn't exist or is
   * not a valid integral number.
   */
  public Long getLong(final int index) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return null;
    }
    return Fun.getLong(seq.get(index));

  }

  /**
   * Returns the integral number located at the given index as a long or a default value provided by the {@code orElse}
   * supplier if it doesn't exist, or it's not an integral number, or it's an integral number but doesn't fit in a
   * long.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to parse it as an
   * integral number. If the value at the index is a valid integral number that can be represented as a long, it is
   * returned as a {@code Long}. If the value is not an integral number or cannot fit in a long, the {@code orElse}
   * supplier is used to provide a default value.
   *
   * @param index  The index of the JSON value to retrieve as a long.
   * @param orElse A {@code Supplier} that provides the default value to return if the value at the given index is not a
   *               valid integral number or cannot fit in a long.
   * @return The integral number located at the given index as a {@code Long}, or the default value provided by
   * {@code orElse} if it doesn't exist or is not a valid integral number.
   */
  public long getLong(final int index,
                      final Supplier<Long> orElse
                     ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getLong(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Returns the string located at the given index or {@code null} if it doesn't exist, or it's not a string.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to interpret it as a
   * string. If the value at the index is a string, it is returned as a {@code String}. If the value is not a string, or
   * if it doesn't exist at the specified index, {@code null} is returned.
   *
   * @param index The index of the JSON value to retrieve as a string.
   * @return The string located at the given index, or {@code null} if it doesn't exist or is not a string.
   */
  public String getStr(final int index) {
    return getStr(index,
                  () -> null);

  }


  /**
   * Returns the string located at the given index or a default value supplied by the provided {@code orElse} function
   * if it doesn't exist or is not a string.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to interpret it as a
   * string. If the value at the index is a string, it is returned as a {@code String}. If the value is not a string, or
   * if it doesn't exist at the specified index, the {@code orElse} function is invoked to provide a default value.
   *
   * @param index  The index of the JSON value to retrieve as a string.
   * @param orElse A {@code Supplier<String>} providing a default value if the value is not a string or doesn't exist.
   * @return The string located at the given index, or the result of invoking {@code orElse} if it doesn't exist or is
   * not a string.
   */
  public String getStr(final int index,
                       final Supplier<String> orElse
                      ) {
    requireNonNull(orElse);
    if (seq.isEmpty() || index < 0 || index > seq.length() - 1) {
      return orElse.get();
    }
    JsValue value = seq.get(index);
    return value.isStr()
           ? value.toJsStr()
               .value : orElse.get();

  }

  /**
   * Returns the instant located at the given index as an {@link Instant} or {@code null} if it doesn't exist or is not
   * a valid instant representation.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to interpret it as
   * an instant. If the value at the index is a valid instant representation, it is returned as an {@link Instant}. If
   * the value is not a valid instant representation, or if it doesn't exist at the specified index, {@code null} is
   * returned.
   *
   * @param index The index of the JSON value to retrieve as an {@link Instant}.
   * @return The instant located at the given index as an {@link Instant}, or {@code null} if it doesn't exist or is not
   * a valid instant representation.
   */
  public Instant getInstant(final int index) {
    return getInstant(index,
                      () -> null);

  }

  /**
   * Returns the instant located at the given index as an {@link Instant} or a default {@link Instant} provided by the
   * {@code orElse} supplier if it doesn't exist or is not a valid instant representation.
   *
   * <p>This method retrieves the JSON value at the specified index in the array and attempts to interpret it as
   * an instant. If the value at the index is a valid instant representation, it is returned as an {@link Instant}. If
   * the value is not a valid instant representation, or if it doesn't exist at the specified index, the default
   * {@link Instant} provided by the {@code orElse} supplier is returned.
   *
   * @param index  The index of the JSON value to retrieve as an {@link Instant}.
   * @param orElse A {@link Supplier} providing the default {@link Instant} to return if the value at the specified
   *               index is not a valid instant representation or doesn't exist.
   * @return The instant located at the given index as an {@link Instant}, or the default {@link Instant} provided by
   * the {@code orElse} supplier if it doesn't exist or is not a valid instant representation.
   */
  public Instant getInstant(final int index,
                            final Supplier<Instant> orElse
                           ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getInstant(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Retrieves the binary data located at the specified index within the JSON-like array and returns it as an array of
   * bytes. If the element at the given index is either a binary array or a string encoded in Base64, it is converted
   * into an array of bytes and returned. If the element does not exist at the specified index or is not a valid binary
   * representation, this method returns null.
   *
   * @param index The index at which to retrieve the binary data.
   * @return An array of bytes representing the binary data at the specified index, or null if the data does not exist
   * or is not a valid binary representation.
   */
  public byte[] getBinary(final int index) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return null;
    }
    return Fun.getBytes(seq.get(index));

  }

  /**
   * Retrieves the binary data located at the specified index within the JSON-like array and returns it as an array of
   * bytes. If the element at the given index is either a binary array or a string encoded in Base64, it is converted
   * into an array of bytes and returned. If the element does not exist at the specified index or is not a valid binary
   * representation, this method returns the result obtained from the provided default value supplier.
   *
   * @param index  The index at which to retrieve the binary data.
   * @param orElse A supplier function that provides a default value (an array of bytes) to be returned when the data
   *               does not exist at the specified index or is not a valid binary representation.
   * @return An array of bytes representing the binary data at the specified index, or the default value provided by the
   * supplier if the data does not exist or is not a valid binary representation.
   */
  public byte[] getBinary(final int index,
                          final Supplier<byte[]> orElse
                         ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getBytes(seq.get(index));
    return value == null ? orElse.get() : value;

  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * boolean. If the value at the given index is a boolean, it returns the boolean value; otherwise, it returns null.
   *
   * @param index The index at which to retrieve the value.
   * @return The boolean value located at the given index, or null if it doesn't exist or the value at the index is not
   * a boolean.
   */
  public Boolean getBool(final int index) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return null;
    }
    var value = seq.get(index);
    return value.isBool() ? value.toJsBool().value : null;

  }

  /**
   * Retrieves the boolean value located at the specified index within the JSON-like array and returns it. If the
   * element at the given index is a valid boolean value, it is returned. If the element does not exist at the specified
   * index or is not a valid boolean representation, this method returns the result obtained from the provided default
   * value supplier.
   *
   * @param index  The index at which to retrieve the boolean value.
   * @param orElse A supplier function that provides a default boolean value to be returned when the value does not
   *               exist at the specified index or is not a valid boolean representation.
   * @return The boolean value located at the given index, or the default value provided by the supplier if the value
   * does not exist or is not a valid boolean representation.
   */
  public boolean getBool(final int index,
                         final Supplier<Boolean> orElse
                        ) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return requireNonNull(orElse).get();
    }
    var value = seq.get(index);
    return value.isBool() ? value.toJsBool().value : orElse.get();

  }


  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * decimal number, converting it to a double. If the value at the given index is a decimal number (either a double or
   * a BigDecimal), it returns the corresponding double value. If the value is not a decimal number or the index doesn't
   * exist, it returns null.
   *
   * <p>The conversion process is designed to handle decimal numbers, but it's important to note that
   * if the value is a BigDecimal, the conversion to a double may result in a potential loss of precision. BigDecimal
   * numbers can represent decimal values with high precision, whereas double has limited precision. Therefore, when
   * converting a BigDecimal to a double, there may be a loss of information beyond the double's precision, potentially
   * leading to rounding or truncation.
   *
   * @param index The index at which to retrieve the value.
   * @return The double value located at the given index, or null if it doesn't exist, or the value at the index is not
   * a decimal number.
   */
  public Double getDouble(final int index) {
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return null;
    }
    return Fun.getDouble(seq.get(index));

  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * decimal number, converting it to a double. If the value at the given index is a decimal number (either a double or
   * a BigDecimal), it returns the corresponding double value. If the value is not a decimal number or the index doesn't
   * exist, it returns the default value provided by the {@code orElse} supplier.
   *
   * <p>The conversion process is designed to handle decimal numbers, but it's important to note that
   * if the value is a BigDecimal, the conversion to a double may result in a potential loss of precision. BigDecimal
   * numbers can represent decimal values with high precision, whereas double has limited precision. Therefore, when
   * converting a BigDecimal to a double, there may be a loss of information beyond the double's precision, potentially
   * leading to rounding or truncation.
   *
   * @param index  The index at which to retrieve the value.
   * @param orElse A supplier providing the default double value to return if the value is not a decimal number or the
   *               index doesn't exist.
   * @return The double value located at the given index, or the default value provided by {@code orElse} if it doesn't
   * exist or the value at the index is not a decimal number.
   */
  public double getDouble(final int index,
                          final Supplier<Double> orElse
                         ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getDouble(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * decimal number, returning it as a BigDecimal. If the value at the given index is a decimal number (either a
   * BigDecimal or a double), it returns the corresponding BigDecimal value. If the value is not a decimal number or the
   * index doesn't exist, it returns null.
   *
   * @param index The index at which to retrieve the value.
   * @return The BigDecimal value located at the given index, or null if it doesn't exist or the value at the index is
   * not a decimal number.
   */
  public BigDecimal getBigDec(final int index) {
    return getBigDec(index,
                     () -> null);
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * decimal number, returning it as a BigDecimal. If the value at the given index is a decimal number (either a
   * BigDecimal or a double), it returns the corresponding BigDecimal value. If the value is not a decimal number or the
   * index doesn't exist, it returns the default BigDecimal value provided by the specified supplier.
   *
   * @param index  The index at which to retrieve the value.
   * @param orElse A supplier function that provides a default BigDecimal value if the value at the index is not a
   *               decimal number or the index doesn't exist.
   * @return The BigDecimal value located at the given index, or the default value provided by the supplier if it
   * doesn't exist or the value at the index is not a decimal number.
   */
  public BigDecimal getBigDec(final int index,
                              final Supplier<BigDecimal> orElse
                             ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getBigDec(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as an
   * integral number, returning it as a BigInteger. If the value at the given index is an integral number (either a
   * BigInteger, long, or int), it returns the corresponding BigInteger value. If the value is not an integral number or
   * the index doesn't exist, it returns null.
   *
   * @param index The index at which to retrieve the value.
   * @return The BigInteger value located at the given index, or null if it doesn't exist or the value at the index is
   * not an integral number.
   */
  public BigInteger getBigInt(final int index) {
    return getBigInt(index,
                     () -> null);
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as an
   * integral number, returning it as a BigInteger. If the value at the given index is an integral number (either a
   * BigInteger, long, or int), it returns the corresponding BigInteger value. If the value is not an integral number or
   * the index doesn't exist, it returns the default value provided.
   *
   * @param index  The index at which to retrieve the value.
   * @param orElse The default value to return if the value at the index is not an integral number or if the index
   *               doesn't exist.
   * @return The integral number located at the given index, or the default value if it doesn't exist or the value at
   * the index is not an integral number.
   */
  public BigInteger getBigInt(final int index,
                              final Supplier<BigInteger> orElse
                             ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = Fun.getBigInt(seq.get(index));
    return value == null ? orElse.get() : value;
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * JSON object (JsObj). If the value at the given index is a JSON object, it returns the corresponding JsObj. If the
   * value is not a JSON object or the index doesn't exist, it returns null.
   *
   * @param index The index at which to retrieve the value.
   * @return The JsObj located at the given index, or null if it doesn't exist or the value at the index is not a JSON
   * object.
   */
  public JsObj getObj(final int index) {
    return getObj(index,
                  () -> null);
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * JSON object (JsObj). If the value at the given index is a JSON object, it returns the corresponding JsObj. If the
   * value is not a JSON object or the index doesn't exist, it returns the default JsObj provided by the `orElse`
   * supplier.
   *
   * @param index  The index at which to retrieve the value.
   * @param orElse A supplier that provides the default JsObj if the index is out of bounds or the value at the index is
   *               not a JSON object.
   * @return The JsObj located at the given index or the default JsObj provided by `orElse`.
   */
  public JsObj getObj(final int index,
                      final Supplier<JsObj> orElse
                     ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = seq.get(index);
    return value.isObj() ? value.toJsObj() : orElse.get();
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * JSON array (JsArray). If the value at the given index is a JSON array, it returns the corresponding JsArray. If the
   * value is not a JSON array or the index doesn't exist, it returns null.
   *
   * @param index The index at which to retrieve the value.
   * @return The JsArray located at the given index or null if it doesn't exist or is not a JSON array.
   */
  public JsArray getArray(final int index) {
    return getArray(index,
                    () -> null);
  }

  /**
   * Retrieves the value located at the specified index within the JSON-like array and attempts to interpret it as a
   * JSON array (JsArray). If the value at the given index is a JSON array, it returns the corresponding JsArray. If the
   * value is not a JSON array or the index doesn't exist, it returns the default value provided by the specified
   * Supplier.
   *
   * @param index  The index at which to retrieve the value.
   * @param orElse A Supplier that provides the default JsArray value if the index doesn't exist or the value is not a
   *               JSON array.
   * @return The JsArray located at the given index or the default value provided by orElse.
   */
  public JsArray getArray(final int index,
                          final Supplier<JsArray> orElse
                         ) {
    requireNonNull(orElse);
    if (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) {
      return orElse.get();
    }
    var value = seq.get(index);
    return value.isArray() ? value.toJsArray() : orElse.get();
  }

  private JsValue get(final Position pos) {
    return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                     index ->
                                         (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
                                         JsNothing.NOTHING :
                                         this.seq.get(index)
                                    );
  }

  @Override
  public boolean containsValue(final JsValue el) {
    return seq.contains(requireNonNull(el));
  }

  @Override
  public JsValue get(final JsPath path) {
    if (path.isEmpty()) {
      return this;
    }
    JsValue head = get(path.head());
    JsPath tail = path.tail();
    if (tail.isEmpty()) {
      return head;
    }
    if (head.isPrimitive()) {
      return NOTHING;
    }
    return head.toJson()
               .get(tail);
  }


  @Override
  public JsArray filterValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter) {
    return OpFilterArrElems.filter(this,
                                   JsPath.empty(),
                                   requireNonNull(filter)
                                  );
  }

  @Override
  public JsArray filterValues(final Predicate<? super JsPrimitive> filter) {
    return OpFilterArrElems.filter(this,
                                   requireNonNull(filter)
                                  );
  }


  @Override
  public JsArray filterKeys(final BiPredicate<? super JsPath, ? super JsValue> filter) {
    return OpFilterArrKeys.filter(this,
                                  JsPath.empty(),
                                  filter
                                 );
  }

  @Override
  public JsArray filterKeys(final Predicate<? super String> filter) {
    return OpFilterArrKeys.filter(this,
                                  filter
                                 );
  }


  @Override
  public JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
    return OpFilterArrObjs.filter(this,
                                  JsPath.empty(),
                                  requireNonNull(filter)
                                 );
  }

  @Override
  public JsArray filterObjs(final Predicate<? super JsObj> filter) {
    return OpFilterArrObjs.filter(this,
                                  requireNonNull(filter)
                                 );
  }

  @Override
  public boolean isEmpty() {
    return seq.isEmpty();
  }


  @Override
  public JsArray mapValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn) {
    return OpMapArrElems.map(this,
                             requireNonNull(fn),
                             JsPath.empty()
                                   .index(-1)
                            );
  }

  @Override
  public JsArray mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
    return OpMapArrElems.map(this,
                             requireNonNull(fn)
                            );
  }


  @Override
  public JsArray mapKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn) {
    return OpMapArrKeys.map(this,
                            requireNonNull(fn),
                            JsPath.empty()
                                  .index(-1)
                           );
  }

  @Override
  public JsArray mapKeys(final Function<? super String, String> fn) {
    return OpMapArrKeys.map(this,
                            requireNonNull(fn)
                           );
  }

  @Override
  public JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn) {
    return OpMapArrObjs.map(this,
                            requireNonNull(fn),
                            JsPath.empty()
                                  .index(-1)

                           );
  }

  @Override
  public JsArray mapObjs(final Function<? super JsObj, ? extends JsValue> fn) {
    return OpMapArrObjs.map(this,
                            requireNonNull(fn)
                           );
  }

  @Override
  public JsArray set(final JsPath path,
                     final JsValue element
                    ) {
    return set(path,
               element,
               NULL
              );
  }

  /**
   * Sets the value at the specified index within the JSON-like array to the provided JsValue using a default null value
   * for padding.
   *
   * @param index   The index at which to set the value.
   * @param element The JsValue to set at the specified index.
   * @return The modified JsArray with the updated value at the specified index.
   */
  public JsArray set(final int index,
                     final JsValue element
                    ) {
    return set(index,
               element,
               NULL
              );
  }

  /**
   * Sets the value at the specified index within the JSON-like array to the provided JsValue. If the array size is less
   * than the specified index, it will be padded with the given padElement to accommodate the new value.
   *
   * @param index      The index at which to set the value.
   * @param value      The JsValue to set at the specified index.
   * @param padElement The JsValue to use for padding the array if necessary.
   * @return The modified JsArray with the updated value at the specified index.
   */
  public JsArray set(final int index,
                     final JsValue value,
                     final JsValue padElement
                    ) {

    requireNonNull(value);

    return ifNothingElse(() -> this.delete(index),
                         elem -> new JsArray(nullPadding(index,
                                                         seq,
                                                         elem,
                                                         padElement
                                                        ))
                        )
        .apply(value);

  }


  @Override
  public JsArray set(final JsPath path,
                     final JsValue value,
                     final JsValue padElement
                    ) {

    requireNonNull(value);
    if (requireNonNull(path).isEmpty()) {
      return this;
    }
    return path.head()
               .match(head -> this,
                      index ->
                      {
                        JsPath tail = path.tail();

                        return tail.isEmpty() ?
                               ifNothingElse(() -> this.delete(index),
                                             elem -> new JsArray(nullPadding(index,
                                                                             seq,
                                                                             elem,
                                                                             padElement
                                                                            ))
                                            )
                                   .apply(value) :
                               putEmptyJson(seq).test(index,
                                                      tail
                                                     ) ?
                               new JsArray(nullPadding(index,
                                                       seq,
                                                       tail.head()
                                                           .match(key -> JsObj.EMPTY
                                                                      .set(tail,
                                                                           value,
                                                                           padElement

                                                                          ),
                                                                  i -> JsArray.EMPTY
                                                                      .set(tail,
                                                                           value,
                                                                           padElement
                                                                          )
                                                                 ),
                                                       padElement
                                                      )) :

                               new JsArray(seq.update(index,
                                                      seq.get(index)
                                                         .toJson()
                                                         .set(tail,
                                                              value,
                                                              padElement
                                                             )
                                                     ));

                      }

                     );

  }


  @Override
  public <R> R reduce(final BinaryOperator<R> op,
                      final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
                      final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                     ) {
    return OpMapReduce.reduceArr(this,
                                 JsPath.fromIndex(-1),
                                 requireNonNull(predicate),
                                 map,
                                 op,
                                 null
                                );

  }

  @Override
  public <R> R reduce(final BinaryOperator<R> op,
                      final Function<? super JsPrimitive, R> map,
                      final Predicate<? super JsPrimitive> predicate
                     ) {
    return OpMapReduce.reduceArr(this,
                                 requireNonNull(predicate),
                                 map,
                                 op,
                                 null
                                );
  }

  @Override
  public JsArray delete(final JsPath path) {
    if (requireNonNull(path).isEmpty()) {
      return this;
    }
    return path.head()
               .match(head -> this,
                      index ->
                      {
                        int maxIndex = seq.length() - 1;
                        if (index < 0 || index > maxIndex) {
                          return this;
                        }
                        JsPath tail = path.tail();
                        return tail.isEmpty() ?
                               new JsArray(seq.removeAt(index)) :
                               ifJsonElse(json -> new JsArray(seq.update(index,
                                                                         json.delete(tail)
                                                                        )),
                                          e -> this
                                         )
                                   .apply(seq.get(index));
                      }

                     );


  }

  @Override
  public int size() {
    return seq.length();
  }

  @Override
  public Stream<JsPair> stream() {
    return streamOfArr(this,
                       JsPath.empty()
                      );
  }

  /**
   * Returns a non-recursive stream of values contained within the JSON-like array. This stream allows for sequential
   * processing of the immediate values in the array without recursively traversing nested objects or arrays.
   *
   * @return A stream of JsValue objects representing the immediate values in the array.
   * @see #stream() for traversing recursively
   */
  public Stream<JsValue> streamOfValues() {
    return seq.toJavaStream();
  }


  private boolean yContainsX(final Vector<JsValue> x,
                             final Vector<JsValue> y
                            ) {
    for (int i = 0; i < x.length(); i++) {
      if (!Objects.equals(x.get(i),
                          y.get(i)
                         )) {
        return false;
      }

    }
    return true;

  }

  /**
   * Returns the element located at the specified index or {@link JsNothing} if it doesn't exist. This method is a total
   * function and never throws exceptions.
   *
   * @param i The index of the element to retrieve.
   * @return A {@link JsValue} representing the element at the specified index, or {@link JsNothing} if it doesn't
   * exist.
   */
  public JsValue get(final int i) {
    try {
      return seq.get(i);
    } catch (IndexOutOfBoundsException e) {
      return NOTHING;
    }
  }

  /**
   * equals method is inherited, so it's implemented. The purpose of this method is to cache the hashcode once
   * calculated. the object is immutable, and it won't change Single-check idiom  Item 83 from Effective Java
   */
  @Override
  @SuppressWarnings("squid:S1206")
  public int hashCode() {
    int result = hashcode;
    if (result == 0) {
      hashcode = result = seq.hashCode();
    }
    return result;
  }

  @Override
  public boolean equals(final Object that) {
    if (!(that instanceof JsArray)) {
      return false;
    }
    if (this == that) {
      return true;
    }
    Vector<JsValue> thatSeq = ((JsArray) that).seq;
    boolean thatEmpty = thatSeq.isEmpty();
    boolean thisEmpty = isEmpty();
    if (thatEmpty && thisEmpty) {
      return true;
    }
    if (this.size() != thatSeq.length()) {
      return false;
    }
    return yContainsX(seq,
                      thatSeq
                     ) && yContainsX(thatSeq,
                                     seq
                                    );

  }

  /**
   * // Single-check idiom  Item 83 from effective java
   */
  @Override
  public String toString() {
    String result = str;
    if (result == null) {
      str = result = new String(JsIO.INSTANCE.serialize(this),
                                StandardCharsets.UTF_8);
    }

    return result;
  }

  /**
   * Returns the first element of this array.
   *
   * @return The first {@link JsValue} of this {@link JsArray}.
   * @throws UserError If this {@link JsArray} is empty.
   */
  public JsValue head() {
    return seq.head();
  }

  /**
   * Returns a JSON array consisting of all elements of this array except the first one.
   *
   * @return A {@link JsArray} consisting of all the elements of this {@link JsArray} except the head.
   * @throws UserError If this {@link JsArray} is empty.
   */
  public JsArray tail() {
    return new JsArray(seq.tail());
  }

  /**
   * Returns all the elements of this array except the last one.
   *
   * @return A {@link JsArray} containing all the {@link JsValue} elements except the last one.
   * @throws UserError If this {@link JsArray} is empty.
   */
  public JsArray init() {
    return new JsArray(seq.init());
  }


  /**
   * {@code this.union(that, SET)} returns {@code this} plus those elements from {@code that} that don't exist in
   * {@code this}. {@code this.union(that, MULTISET)} returns {@code this} plus those elements from {@code that}
   * appended to the back. {@code this.union(that, LIST)} returns {@code this} plus those elements from {@code that}
   * which position is {@code >= this.size()}. For those elements that are containers of the same type and are located
   * at the same position, the result is their intersection. So this operation is kind of a recursive intersection
   *
   * @param that the other array
   * @return a JsArray of the same type as the inputs
   */
  @Override
  public JsArray intersection(final JsArray that,
                              final JsArray.TYPE ARRAY_AS
                             ) {
    return intersection(this,
                        requireNonNull(that),
                        ARRAY_AS
                       );
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public Iterator<JsValue> iterator() {
    return seq.iterator();
  }

  /**
   * Returns the last element of this array.
   *
   * @return The last {@link JsValue} of this {@link JsArray}.
   * @throws UserError If this {@link JsArray} is empty.
   */
  public JsValue last() {
    return seq.last();
  }

  /**
   * Adds one or more elements, starting from the last, to the front of this array.
   *
   * @param e      The {@link JsValue} to be added to the front.
   * @param others More optional {@link JsValue} elements to be added to the front.
   * @return A new {@link JsArray} containing the elements added to the front.
   */
  public JsArray prepend(final JsValue e,
                         final JsValue... others
                        ) {
    Vector<JsValue> acc = seq;
    for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++) {
      JsValue other = others[othersLength - 1 - i];
      acc = acc.prepend(requireNonNull(other));
    }
    return new JsArray(acc.prepend(requireNonNull(e)));
  }

  /**
   * Adds all the elements of the {@link JsArray}, starting from the last, to the front of this array.
   *
   * @param array The {@link JsArray} of elements to be added to the front.
   * @return A new {@link JsArray} containing all the elements added to the front.
   */
  public JsArray prependAll(final JsArray array) {
    return appendAllFront(this,
                          requireNonNull(array)
                         );

  }


  private BiPredicate<Integer, JsPath> putEmptyJson(final Vector<JsValue> pseq) {
    return (index, tail) ->
        index > pseq.length() - 1 || pseq.isEmpty() || pseq.get(index)
                                                           .isPrimitive()
        ||
        (tail.head()
             .isKey() && pseq.get(index)
                             .isArray()
        )
        ||
        (tail.head()
             .isIndex() && pseq.get(index)
                               .isObj()
        );
  }

  /**
   * Deletes the element at the specified index from this {@link JsArray}.
   *
   * @param index The index of the element to be deleted.
   * @return A new {@link JsArray} with the element at the specified index removed.
   * @throws IllegalArgumentException if the index is negative.
   */
  public JsArray delete(final int index) {
    if (index < 0) {
      throw new IllegalArgumentException("index must be >= 0");
    }
    int maxIndex = seq.length() - 1;
    if (index > maxIndex) {
      return this;
    }
    return new JsArray(seq.removeAt(index));
  }


  @Override
  public JsArray union(final JsArray that,
                       final TYPE ARRAY_AS
                      ) {
    return union(this,
                 requireNonNull(that),
                 ARRAY_AS
                );
  }

  private JsArray intersection(final JsArray a,
                               final JsArray b,
                               final JsArray.TYPE ARRAY_AS
                              ) {
    if (a.isEmpty()) {
      return a;
    }
    if (b.isEmpty()) {
      return b;
    }

    JsArray result = JsArray.empty();

    for (int i = 0; i < a.size(); i++) {
      JsValue head = a.get(i);
      JsValue otherHead = b.get(i);
      if (head.isJson() && head.isSameType(otherHead)) {
        Json<?> obj = head.toJson();
        Json<?> obj1 = otherHead.toJson();
        result = result.set(i,
                            OpIntersectionJsons.intersectionAll(obj,
                                                                obj1,
                                                                ARRAY_AS
                                                               )
                           );


      } else if (head.equals(otherHead)) {
        result = result.set(i,
                            head
                           );
      }

    }

    return result;
  }

  private Vector<JsValue> nullPadding(final int index,
                                      Vector<JsValue> arr,
                                      final JsValue e,
                                      final JsValue pad
                                     ) {
    assert arr != null;
    assert e != null;

    if (index == arr.length()) {
      return arr.append(e);
    }

    if (index < arr.length()) {
      return arr.update(index,
                        e);
    }
    for (int j = arr.length(); j < index; j++) {
      arr = arr.append(pad);
    }
    return arr.append(e);
  }


  private JsArray union(final JsArray a,
                        final JsArray b,
                        final TYPE ARRAY_AS
                       ) {
    if (b.isEmpty()) {
      return a;
    }
    if (a.isEmpty()) {
      return b;
    }
    JsArray result = a;
    for (int i = 0; i < b.size(); i++) {
      JsValue head = a.get(i);
      JsValue otherHead = b.get(i);
      if (head.isJson() && head.isSameType(otherHead)) {
        Json<?> obj = head.toJson();
        Json<?> obj1 = otherHead.toJson();
        result = result.set(i,
                            OpUnionJsons.unionAll(obj,
                                                  obj1,
                                                  ARRAY_AS
                                                 )
                           );

      } else if (!otherHead.isNothing() && head.isNothing()) {
        result = result.append(otherHead);
      }
    }

    return result;


  }


  /**
   * Enumeration representing different types of arrays: SET, LIST, or MULTISET.
   * <p>
   * Arrays can be categorized into these types based on their behavior and characteristics. Understanding the type of
   * array is important when dealing with data structures and their usage.
   * </p>
   * <ul>
   *   <li>
   *     <b>SET:</b> This type of array does not consider the order of data items (or the order is undefined),
   *     and it does not allow duplicate data items. Each element in a SET is unique.
   *   </li>
   *   <li>
   *     <b>LIST:</b> In a LIST, the order of data items matters, and it allows duplicate data items.
   *     Elements are stored in the order they are added, and duplicates are allowed.
   *   </li>
   *   <li>
   *     <b>MULTISET:</b> Similar to a SET, the order of data items does not matter, but in this case, duplicate
   *     data items are permitted. This means that elements can be repeated in a MULTISET.
   *   </li>
   * </ul>
   * <p>
   * Choosing the appropriate type of array for your use case can help ensure that your data is organized and
   * behaves as expected in your application.
   * </p>
   */
  public enum TYPE {

    SET,

    LIST,

    MULTISET
  }


}



