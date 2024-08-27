package jsonvalues;

import static java.util.Objects.requireNonNull;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import jsonvalues.JsArray.TYPE;
import jsonvalues.spec.JsIO;

/**
 * Represents an immutable and persistent JSON of type T, where T is either a JsObj or a JsArray. JSON data is modeled
 * as a set of key-value pairs ({@link JsPair}) with keys represented by {@link JsPath} and values as {@link JsValue}.
 * The structure allows for recursion.
 * <p>
 * For instance, the JSON: { "a": 1, "x": { "c": true, "d": null, "e": [false, 1, "hi"] } }
 * <p>
 * Can be seen as a set: Set[(a, 1), (x.c, true), (x.d, null), (x.e.0, false), (x.e.1, 1), (x.e.2, "hi"), (_,
 * JsNothing)]
 * <p>
 * The special element {@link JsNothing#NOTHING} represents any other JsPath, making functions like {@link #get(JsPath)}
 * total (defined for every possible path).
 * <p>
 * JSON modification operations, such as {@link #set(JsPath, JsValue)}, can insert JsNothing without changing the JSON.
 * <p>
 * JSONs can also be treated as streams of pairs, enabling operations like map, filter, and reduce.
 * <p>
 * All methods throw a NullPointerException when passed null parameters, and UserError is thrown for inappropriate
 * method calls. /** The {@code ARRAY_AS} parameter in various methods of the {@link Json} interface allows you to
 * specify how arrays within the JSON data structure should be treated during specific operations. It provides
 * customization options for handling arrays, giving you control over their behavior in various contexts.
 *
 * <p>JSON data structures can contain arrays, and the behavior of operations involving these arrays can vary depending
 * on your requirements. The {@code ARRAY_AS} parameter serves as a way to define this behavior by offering different
 * options or types, each of which affects how arrays are treated.</p>
 *
 * <p>Common use cases for the {@code ARRAY_AS} parameter include:</p>
 *
 * <ul>
 *   <li>Defining how arrays should be merged, concatenated, or otherwise combined when performing operations that involve JSON structures.</li>
 *   <li>Specifying whether arrays should be considered equal only if they have the same elements in the same order or if the order of elements is not important.</li>
 *   <li>Controlling how arrays are affected by operations such as union and intersection between JSON structures.</li>
 * </ul>
 *
 * <p>The specific values or options available for the {@code ARRAY_AS} parameter will depend on the JSON library or framework being used. Developers should refer to the documentation of the library or framework to understand the available options and their behavior.</p>
 *
 * <p>By allowing customization of array handling, the {@code ARRAY_AS} parameter enhances the flexibility of working with JSON data, enabling developers to tailor operations to suit specific structural requirements and use cases.</p>
 *
 * @param <T> Type of the container: either an object (JsObj) or an array (JsArray)
 * @author Rafael Merino Garcia
 * @see JsObj for working with JSON objects
 * @see JsArray for working with JSON arrays
 */
public sealed interface Json<T extends Json<T>> extends JsValue permits JsArray, JsObj {


  /**
   * Converts the JSON to a pretty-printed string with the specified indentation length.
   *
   * @param indentLength The number of spaces to use for indentation.
   * @return A pretty-printed JSON string.
   */
  default String toPrettyString(int indentLength) {
    return JsIO.INSTANCE.toPrettyString(this,
                                        indentLength);

  }

  /**
   * Converts the JSON to a pretty-printed string with a default indentation length of 2 spaces.
   *
   * @return A pretty-printed JSON string.
   * @see #toPrettyString(int)
   */
  default String toPrettyString() {
    return JsIO.INSTANCE.toPrettyString(this,
                                        2);
  }


  /**
   * Checks if this JSON contains the given value in its first level.
   *
   * @param element The value to check for.
   * @return True if this JSON contains the value, otherwise false.
   */
  boolean containsValue(final JsValue element);


  /**
   * Checks if an element exists in this JSON at the given path.
   *
   * @param path The path to check.
   * @return True if a value exists at the path, otherwise false.
   */
  default boolean containsPath(final JsPath path) {
    return get(requireNonNull(path)).isNotNothing();

  }

  /**
   * Gets the value located at the given path or {@link JsNothing} if it doesn't exist.
   *
   * @param path The path to the desired value.
   * @return The value at the given path or JsNothing if it doesn't exist.
   */
  JsValue get(final JsPath path);

  /**
   * Checks if this JSON is equal to another JSON element.
   *
   * @param elem     The JSON element to compare.
   * @param ARRAY_AS The type to consider arrays during comparison.
   * @return True if the JSON elements are equal, otherwise false.
   */
  default boolean equals(final JsValue elem,
                         final TYPE ARRAY_AS
                        ) {
    if (elem == null || getClass() != elem.getClass()) {
      return false;
    }
    if (isObj()) {
      return toJsObj().equals(elem.toJsObj(),
                              ARRAY_AS);
    }
    if (isArray()) {
      return toJsArray().equals(elem.toJsArray(),
                                ARRAY_AS);
    }
    return false;

  }


  /**
   * Filters all the pairs of elements of this json, removing those that don't satisfy the given predicate.
   *
   * @param filter the predicate which takes as input every JsPair of this json
   * @return this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
   */
  T filterValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter);

  /**
   * Filters all the pairs of elements of this json, removing those that don't ifPredicateElse the predicate.
   *
   * @param filter the predicate which takes as the input every JsPair of this json
   * @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
   */
  T filterValues(final Predicate<? super JsPrimitive> filter);


  /**
   * Filters all the keys of this json, removing those that don't satisfy the given predicate.
   *
   * @param filter the predicate which takes as input every JsPair of this json
   * @return this instance if all the keys satisfy the predicate or a new filtered json of the same type T
   */
  T filterKeys(final BiPredicate<? super JsPath, ? super JsValue> filter);


  /**
   * Filters all the keys of this json, removing those that don't ifPredicateElse the predicate.
   *
   * @param filter the predicate which takes as the input every JsPair of this json
   * @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T
   */
  T filterKeys(final Predicate<? super String> filter);


  /**
   * Filters all the pair of jsons of this json, removing those that don't satisfy the given predicate.
   *
   * @param filter the predicate which takes as input every JsPair of this json
   * @return this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
   */
  T filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter);

  /**
   * Filters all the pair of jsons of this json, removing those that don't ifPredicateElse the predicate.
   *
   * @param filter the predicate which takes as the input every JsPair of this json
   * @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
   */
  T filterObjs(final Predicate<? super JsObj> filter);

  /**
   * Returns the array located at the given path or null if it doesn't exist, or it's not an array.
   *
   * @param path the path
   * @return the JsArray located at the given JsPath or null
   */
  default JsArray getArray(final JsPath path) {
    var value = get(requireNonNull(path));
    return value.isArray() ? value.toJsArray() : null;
  }

  /**
   * Returns the array located at the given path or the default value provided if it doesn't exist, or it's not an
   * array.
   *
   * @param path   the path
   * @param orElse the default value provided
   * @return the JsArray located at the given JsPath or null
   */
  default JsArray getArray(final JsPath path,
                           final Supplier<JsArray> orElse
                          ) {
    requireNonNull(orElse);
    var value = get(requireNonNull(path));
    return value.isArray() ? value.toJsArray() : orElse.get();
  }

  /**
   * Returns the number located at the given path as a big decimal or null if it doesn't exist, or it's not a decimal
   * number.
   *
   * @param path the path
   * @return the number located at the given JsPath or null
   */
  default BigDecimal getBigDec(final JsPath path) {
    return Fun.getBigDec(get(requireNonNull(path)));

  }

  /**
   * Returns the number located at the given path as a big decimal or the default value provided if it doesn't exist, or
   * it's not a decimal number.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the number located at the given JsPath or null
   */
  default BigDecimal getBigDec(final JsPath path,
                               final Supplier<BigDecimal> orElse
                              ) {
    requireNonNull(orElse);
    var value = Fun.getBigDec(get(requireNonNull(path)));
    return value != null ? value : orElse.get();
  }

  @Override
  default JsPrimitive toJsPrimitive() {
    throw UserError.isNotAJsPrimitive(this);
  }

  /**
   * Returns the number located at the given path as a big integer or null if it doesn't exist, or it's not an integral
   * number.
   *
   * @param path the path
   * @return the BigInteger located at the given JsPath or null
   */
  default BigInteger getBigInt(final JsPath path) {
    return Fun.getBigInt(get(requireNonNull(path)));

  }

  /**
   * Returns the number located at the given path as a big integer or the default value provided if it doesn't exist, or
   * it's not an integral number.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the BigInteger located at the given JsPath or null
   */
  default BigInteger getBigInt(final JsPath path,
                               final Supplier<BigInteger> orElse
                              ) {
    requireNonNull(orElse);
    var value = Fun.getBigInt(get(requireNonNull(path)));
    return value != null ? value : orElse.get();

  }

  /**
   * Returns the boolean located at the given path or null if it doesn't exist.
   *
   * @param path the path
   * @return the Boolean located at the given JsPath or null
   */
  default Boolean getBool(final JsPath path) {
    var value = get(requireNonNull(path));
    return value.isBool() ? value.toJsBool().value : null;

  }

  /**
   * Returns the boolean located at the given path or the default value provided if it doesn't exist.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the Boolean located at the given JsPath or null
   */
  default boolean getBool(final JsPath path,
                          final Supplier<Boolean> orElse
                         ) {
    requireNonNull(orElse);
    var value = get(requireNonNull(path));
    return value.isBool() ? value.toJsBool().value : orElse.get();

  }

  /**
   * Returns the bytes located at the given path or null if it doesn't exist.
   *
   * @param path the path
   * @return the bytes located at the given JsPath or null
   */
  default byte[] getBinary(final JsPath path) {
    return Fun.getBytes(get(requireNonNull(path)));
  }

  /**
   * Returns the bytes located at the given path or the default value provided if it doesn't exist.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the bytes located at the given JsPath or null
   */
  default byte[] getBinary(final JsPath path,
                           final Supplier<byte[]> orElse
                          ) {
    var value = Fun.getBytes(get(requireNonNull(path)));
    return value != null ? value : orElse.get();
  }

  /**
   * Returns the instant located at the given path or null if it doesn't exist.
   *
   * @param path the path
   * @return the instant located at the given JsPath or null
   */
  default Instant getInstant(final JsPath path) {
    var value = get(requireNonNull(path));
    return value.isInstant() ? value.toJsInstant().value : null;
  }

  /**
   * Returns the instant located at the given path or the default value provided if it doesn't exist.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the instant located at the given JsPath or null
   */
  default Instant getInstant(final JsPath path,
                             final Supplier<Instant> orElse
                            ) {
    requireNonNull(orElse);
    var value = get(requireNonNull(path));
    return value.isInstant() ? value.toJsInstant().value : orElse.get();

  }

  /**
   * Returns the decimal number located at the given path as a double or null if it doesn't exist, or it's not a decimal
   * number. If the number is a BigDecimal, the conversion is identical to that specified in
   * {@link BigDecimal#doubleValue()}.
   *
   * @param path the path
   * @return the decimal number located at the given JsPath or null
   */
  default Double getDouble(final JsPath path) {
    return Fun.getDouble(get(requireNonNull(path)));

  }

  /**
   * Returns the decimal number located at the given path as a double or the default value provided if it doesn't exist,
   * or it's not a decimal number. If the number is a BigDecimal, the conversion is identical to that specified in
   * {@link BigDecimal#doubleValue()}.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the decimal number located at the given JsPath or null
   */
  default double getDouble(final JsPath path,
                           final Supplier<Double> orElse
                          ) {
    requireNonNull(orElse);
    var value = Fun.getDouble(get(requireNonNull(path)));
    return value != null ? value : orElse.get();

  }

  /**
   * Returns the integral number located at the given path as an integer or null if it doesn't exist, or it's not an
   * integral number.
   *
   * @param path the path
   * @return the integral number located at the given JsPath or null
   */
  default Integer getInt(final JsPath path) {
    return Fun.getInt(get(requireNonNull(path)));
  }

  /**
   * Returns the integral number located at the given path as an integer or the default value provided if it doesn't
   * exist, or it's not an integral number.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the integral number located at the given JsPath or null
   */
  default int getInt(final JsPath path,
                     final Supplier<Integer> orElse
                    ) {
    requireNonNull(orElse);
    var value = Fun.getInt(get(requireNonNull(path)));
    return value != null ? value : orElse.get();
  }

  /**
   * Returns the integral number located at the given path as a long or null if it doesn't exist, or it's not an
   * integral number.
   *
   * @param path the path
   * @return the integral number located at the given JsPath or null
   */
  default Long getLong(final JsPath path) {
    return Fun.getLong(get(requireNonNull(path)));

  }

  /**
   * Returns the integral number located at the given path as a long or the default value provided if it doesn't exist,
   * or it's not an integral number.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the integral number located at the given JsPath or null
   */
  default long getLong(final JsPath path,
                       final Supplier<Long> orElse
                      ) {
    requireNonNull(orElse);
    var value = Fun.getLong(get(requireNonNull(path)));
    return value != null ? value : orElse.get();

  }

  /**
   * Returns the object located at the given path or the default value provided if it doesn't exist, or it's not an
   * object.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the JsObj located at the given JsPath or null
   */
  default JsObj getObj(final JsPath path,
                       final Supplier<JsObj> orElse
                      ) {
    requireNonNull(orElse);
    var value = get(requireNonNull(path));
    return value.isObj() ? value.toJsObj() : orElse.get();
  }

  /**
   * Returns the object located at the given path or null if it doesn't exist, or it's not an object.
   *
   * @param path the path
   * @return the JsObj located at the given JsPath or null
   */
  default JsObj getObj(final JsPath path) {
    var value = get(requireNonNull(path));
    return value.isObj() ? value.toJsObj() : null;
  }

  /**
   * Returns the string located at the given path or null if it doesn't exist, or it's not a string.
   *
   * @param path the path
   * @return the string located at the given path or null
   */
  default String getStr(final JsPath path) {
    var value = get(requireNonNull(path));
    return value.isStr() ? value.toJsStr().value : null;
  }

  /**
   * Returns the string located at the given path or the default value provided if it doesn't exist, or it's not a
   * string.
   *
   * @param path   the path
   * @param orElse the default value
   * @return the string located at the given path or null
   */
  default String getStr(final JsPath path,
                        final Supplier<String> orElse
                       ) {
    var value = get(requireNonNull(path));
    return value.isStr() ? value.toJsStr().value : orElse.get();
  }

  /**
   * Declarative way of implementing if(this.isEmpty()) return emptySupplier.get() else return nonEmptySupplier.get().
   *
   * @param emptySupplier    Supplier that will produce the result if this json is empty
   * @param nonemptySupplier Supplier that will produce the result if this json is not empty
   * @param <A>              the type of the result
   * @return an object of type A
   */
  default <A> A ifEmptyElse(final Supplier<A> emptySupplier,
                            final Supplier<A> nonemptySupplier
                           ) {

    return this.isEmpty() ?
           requireNonNull(emptySupplier).get() :
           requireNonNull(nonemptySupplier).get();
  }

  /**
   * Determines whether this Json instance is empty, i.e., it contains no elements.
   *
   * @return true if empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Determines whether this Json instance is not empty, i.e., it contains one or more elements.
   *
   * @return false if empty, true otherwise
   */
  default boolean isNotEmpty() {
    return !isEmpty();
  }


  /**
   * Recursively maps all the values of this JSON, replacing each value with the result of applying the given mapping
   * function. This operation traverses the entire JSON structure.
   *
   * @param fn the mapping function that transforms each value
   * @return a new JSON object of the same type T with the mapped values
   */
  T mapValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn);

  /**
   * Recursively maps all the values of this JSON, replacing each value with the result of applying the given mapping
   * function. This operation traverses the entire JSON structure.
   *
   * @param fn the mapping function that transforms each value
   * @return a new JSON object of the same type T with the mapped values
   */
  T mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn);


  /**
   * Maps all the keys of this JSON object, recursively traversing the entire JSON structure.
   *
   * @param fn the mapping function that transforms each key
   * @return a new JSON object of the same type T with the mapped keys
   */
  T mapKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn);


  /**
   * Maps all the keys of this JSON object, recursively traversing the entire JSON structure.
   *
   * @param fn the mapping function that transforms each key
   * @return a new JSON object of the same type T with the mapped keys
   */
  T mapKeys(final Function<? super String, String> fn);


  /**
   * Maps all the JSON objects of this JSON, traversing the entire JSON if necessary.
   *
   * @param fn the mapping function that transforms each JSON object
   * @return a new JSON object of the same type T with the mapped objects, or the same instance if no objects are found
   */
  T mapObjs(final BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn);

  /**
   * Maps all the JSON objects of this JSON, traversing the entire JSON if necessary.
   *
   * @param fn the mapping function that transforms each JSON object
   * @return a new JSON object of the same type T with the mapped objects, or the same instance if no objects are found
   */
  T mapObjs(final Function<? super JsObj, ? extends JsValue> fn);

  /**
   * Inserts an element at the specified path in this JSON, replacing any existing element.
   *
   * @param path       The path to insert the element.
   * @param element    The element to insert.
   * @param padElement The element to use for padding in arrays when necessary.
   * @return A new JSON with the inserted element, or this instance if the head of the path is incompatible.
   */
  T set(final JsPath path,
        final JsValue element,
        final JsValue padElement
       );

  /**
   * Inserts the given element at the specified path in this JSON, replacing any existing element. If necessary, it
   * fills empty indexes in arrays with {@link jsonvalues.JsNull}. You have the option to use
   * {@link #set(JsPath, JsValue, JsValue)} to specify a custom padding element.
   *
   * @param path    the path where the element will be inserted
   * @param element the element to be inserted
   * @return a JSON object of the same type with the new element, or the same instance if the path is invalid
   */
  default T set(final JsPath path,
                final JsValue element
               ) {
    return set(path,
               element,
               JsNull.NULL
              );
  }


  /**
   * Reduces the values of this JSON object that satisfy the given predicate, allowing access to the element's path.
   * This reduction traverses the entire JSON recursively if necessary.
   *
   * @param op        the operator to apply to values of type R
   * @param map       the mapping function to convert JsPath and JsPrimitive to type R
   * @param predicate the predicate that determines which values are included in the reduction
   * @param <R>       the type of the reduction result
   * @return an {@link Optional} describing the result of the reduction, or empty if no values satisfy the predicate
   */
  <R> R reduce(final BinaryOperator<R> op,
               final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
               final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
              );


  /**
   * Reduces the values of this JSON object that satisfy the given predicate. This reduction traverses the entire JSON
   * recursively if necessary.
   *
   * @param op        the operator to apply to values of type R
   * @param map       the mapping function to convert JsValue to type R
   * @param predicate the predicate that determines which values are included in the reduction
   * @param <R>       the type of the reduction result
   * @return an {@link Optional} describing the result of the reduction, or empty if no values satisfy the predicate
   */
  <R> R reduce(final BinaryOperator<R> op,
               final Function<? super JsPrimitive, R> map,
               final Predicate<? super JsPrimitive> predicate
              );

  /**
   * Removes the element at the specified path within this immutable JSON object, if it exists. Returns a new JSON
   * object with the element removed, or the original JSON object if the element does not exist.
   *
   * @param path the path specifying the element to be removed
   * @return a new JSON object with the specified element removed, or the original JSON object if the element does not
   * exist
   */
  T delete(final JsPath path);

  /**
   * Returns the number of elements in the first level of this JSON object or array. This method provides the count of
   * elements directly contained in the JSON object or array.
   *
   * @return the number of elements in the first level of this JSON object or array
   */
  int size();

  /**
   * Returns a stream over all the key-value pairs {@link JsPair} of elements in this JSON object. This method provides
   * a way to traverse and operate on the key-value pairs within the JSON object.
   *
   * @return a {@code Stream} over all the key-value pairs (JsPairs) in this JSON object
   */
  @SuppressWarnings("squid:S00100")
  Stream<JsPair> stream();


  /**
   * Serializes this JSON object into the given output stream, effectively converting it into its serialized form.
   * Serialization is the process of converting a JSON object into a byte stream representation. The serialized JSON can
   * be written to an output stream, such as a file or network socket.
   *
   * @param outputstream the output stream to which the JSON object will be serialized
   */
  default void serialize(final OutputStream outputstream) {
    JsIO.INSTANCE.serialize(this,
                            requireNonNull(outputstream)
                           );
  }

  /**
   * Serializes this JSON object into a byte array, effectively converting it into its serialized form as a byte
   * sequence. Serialization is the process of converting a JSON object into a byte stream representation. This method
   * returns the serialized JSON as a byte array.
   *
   * @return a byte array containing the serialized representation of this JSON object
   */
  default byte[] serialize() {
    return JsIO.INSTANCE.serialize(this);
  }


  /**
   * Computes the union of this JSON and another JSON object 'that' with respect to the given array merging strategy.
   * The union of two JSON objects is another JSON object that contains all the key-value pairs present in either 'this'
   * or 'that'. If a key is present in both JSON objects, the value from 'this' will be used. Array merging strategy
   * 'ARRAY_AS' determines how arrays are merged during the union operation. If 'ARRAY_AS' is 'MERGE', arrays are merged
   * by concatenating elements. If 'ARRAY_AS' is 'REPLACE', arrays in 'this' will be replaced with arrays from 'that'.
   *
   * @param that     the other JSON object to compute the union with
   * @param ARRAY_AS the array merging strategy, either 'MERGE' or 'REPLACE'
   * @return a new JSON object representing the union of 'this' and 'that'
   */
  T union(final T that,
          final TYPE ARRAY_AS);


  /**
   * Computes the intersection of this JSON and another JSON object 'that' with respect to the given array merging
   * strategy. The intersection of two JSON objects is another JSON object that contains only the key-value pairs
   * present in both 'this' and 'that'. Array merging strategy 'ARRAY_AS' determines how arrays are merged during the
   * intersection operation. If 'ARRAY_AS' is 'MERGE', arrays are merged by concatenating elements. If 'ARRAY_AS' is
   * 'REPLACE', arrays in 'this' will be replaced with arrays from 'that'.
   *
   * @param that     the other JSON object to compute the intersection with
   * @param ARRAY_AS the array merging strategy, either 'MERGE' or 'REPLACE'
   * @return a new JSON object representing the intersection of 'this' and 'that'
   */
  T intersection(final T that,
                 final TYPE ARRAY_AS);


}
