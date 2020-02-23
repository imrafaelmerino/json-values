package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import jsonvalues.JsArray.TYPE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsonLibsFactory.dslJson;

/**
 <pre>
 Represents a json of type T, where T is the type of the container, either a JsObj or a JsArray.

 A json of any type can be modeled as a set of pairs {@link JsPair}=({@link JsPath}, {@link JsValue}), where:

 - a JsElem is a {@link JsBool} or {@link JsStr} or {@link JsNumber} or {@link JsNull}, or another {@link Json} like {@link JsObj} or {@link JsArray},
 what makes the data structure recursive.

 - a JsPath represents the location of the element in the json.

 For example, the json
 {
 "a":1, "x":{ "c": true, "d":null, e: [false, 1, "hi"] }
 }

 can be seen as the following set:

 Set[(a,1), (x.c,true), (x.d,null), (x.e.0,false), (x.e.1,1), (x.e.2,"hi"), (_,NOTHING)]

 where _, which means any other JsPath, and the special element {@link JsNothing#NOTHING}, makes the
 function {@link #get(JsPath)} total (defined for every possible path). Moreover, inserting JsNothing
 in a json doesn't change the json, which is very convenient when passing functions as parameters to
 put data in:

 //all the logic goes into the supplier{@code
Supplier<JsElem> supplier = ()-> (doesnt-put-anything-condition) ? JsNothing.NOTHING : JsInt.of(2);
json.putIfAbsent(path,supplier)
}
 Another way to see a json is like a stream of pairs, which opens the door to doing all the operations
 that were introduced in Java 8 (map, filter, reduce, etc). For this purpose the methods {@link #streamAll()}
 or {@link #stream()} are provided.

 There are one convention on method names:
 -Methods that are suffixed with underscore traverse the whole json recursively.

 All the methods throw a NullPointerException when any of the params passed in is null. The exception
 <code>UserError</code> is thrown when the user calls a method inappropriately:
 for example calling the method <code>asJsStr</code> in a <code>JsNull</code> instance or calling the
 method head in an empty array, etc. Normally, when that happens, a previous check is missing.
 </pre>

 @param <T> Type of container: either an object or an array
 @see JsObj to work with jsons that are objects
 @see JsArray to work with jsons that are arrays

 @author Rafael Merino Garcia */
public interface Json<T extends Json<T>> extends JsValue


{

    /** Converts the string representation of this Json to a pretty print version
     *
     * @return pretty print version of the string representation of this Json
     */
    default String toPrettyString(){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            dslJson.serialize(this,
                              new MyPrettifyOutputStream(baos)
                             );
            return baos.toString(StandardCharsets.UTF_8.name());

        }
        catch (IOException e)
        {
            throw  InternalError.unexpectedErrorSerializingAJsonIntoString(e);
        }
    }


    /**
     Appends one or more elements, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given JsPath pointing to the array in which all the elements will be appended
     @param elem   the first JsElem to be appended to the existing or created array
     @param others more optional JsElem to be appended

     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final JsValue elem,
                     final JsValue... others
                    )
    {


        T result = append(path,
                          elem
                         );
        for (final JsValue other : Objects.requireNonNull(others))
        {
            result = result.append(path,
                                   Objects.requireNonNull(other)
                                  );
        }
        return result;

    }

    /**
     Appends one element to the array located at the given path in this json. If the array doesn't exist,
     a new one is created, replacing any existing element in the path and filling empty indexes in arrays
     with {@link jsonvalues.JsNull} when necessary. The same this instance is returned when it's an array
     and the head of the path is a key or when it's an object and the head of the path is an index.
     @param path   the path pointing to the array in which the element will be appended
     @param elem   the JsElem to be appended to the existing or created array
     @return same this instance or a new json of the same type T
     */
    T append(final JsPath path,
             final JsValue elem
            );

    /**
     Appends one or more strings, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path pointing to the array in which all the strings will be appended
     @param elem   the first string to be appended to the existing or created array
     @param others more optional strings to be appended
     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final String elem,
                     final String... others
                    )
    {
        T result = append(path,
                          JsStr.of(elem)
                         );
        for (final String other : others)
        {
            result = result.append(path,
                                   JsStr.of(other)
                                  );
        }
        return result;

    }

    /**
     Appends one or more integers, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path pointing to the array in which all the integers will be appended
     @param elem   the first integer to be appended to the existing or created array
     @param others more optional integers to be appended
     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final int elem,
                     final int... others
                    )
    {
        T result = append(path,
                          JsInt.of(elem)
                         );
        for (final int other : others)
        {
            result = result.append(path,
                                   JsInt.of(other)
                                  );
        }
        return result;
    }

    /**
     Appends one or more longs, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path pointing to the array in which all the longs will be appended
     @param elem   the first long to be appended to the existing or created array
     @param others more optional longs to be appended
     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final long elem,
                     final long... others
                    )
    {
        T result = append(path,
                          JsLong.of(elem)
                         );
        for (final long other : others)
        {
            result = result.append(path,
                                   JsLong.of(other)
                                  );
        }
        return result;
    }

    /**
     Appends one or more booleans, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path pointing to the array in which all the booleans will be appended
     @param elem   the first boolean to be appended to the existing or created array
     @param others more optional booleans to be appended
     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final boolean elem,
                     final boolean... others
                    )
    {
        T result = append(path,
                          JsBool.of(elem)
                         );
        for (final boolean other : others)
        {
            result = result.append(path,
                                   JsBool.of(other)
                                  );
        }
        return result;
    }

    /**
     Appends one or more doubles, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path pointing to the array in which all the doubles will be appended
     @param elem   the first double to be appended to the existing or created array
     @param others more optional doubles to be appended
     @return same this instance or a new json of the same type T
     */
    default T append(final JsPath path,
                     final double elem,
                     final double... others
                    )
    {
        T result = append(path,
                          JsDouble.of(elem)
                         );
        for (final double other : others)
        {
            result = result.append(path,
                                   JsDouble.of(other)
                                  );
        }
        return result;
    }

    /**
     Appends all the elements of the array, starting from the head, to the array located at the given
     path in this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path  the given JsPath pointing to the array in which all the elements will be appended
     @param elems the JsArray of elements to be appended
     @return same this instance or a new json of the same type T
     */
    T appendAll(final JsPath path,
                final JsArray elems
               );

    /**
     Appends all the elements of the array computed by the supplier, starting from the head, to an
     array located at the given path in this json, returning the same this instance if the array is
     not present, in which case, the supplier is not invoked.
     @param path the given JsPath object pointing to the existing array in which all the elements will be appended
     @param supplier   the supplier of the array of elements that will be appended
     @return same this instance or a new json of the same type T
     */
    default T appendAllIfPresent(final JsPath path,
                                 final Supplier<JsArray> supplier


                                )
    {
        return MatchExp.ifArrElse(it -> appendAll(path,
                                                  requireNonNull(supplier).get()
                                                 ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(requireNonNull(path)));

    }

    /**
     Appends the element given by the supplier, to the array located at the given path in this json,
     returning the same this instance if the array is not present. The supplier is not applied if
     there's no array at the specified path.
     @param path   the JsPath pointing to the existing array in which the element will be appended
     @param supplier   the given supplier
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final Supplier<? extends JsValue> supplier
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               Objects.requireNonNull(supplier)
                                                      .get()
                                              ),
                                  it ->
                                  {
                                      @SuppressWarnings("unchecked") final T t = (T) this; //this is an instance of T (recursive type)
                                      return t;
                                  }
                                 )
                       .apply(get(requireNonNull(path)));
    }

    /**
     Appends one or more integers to the array located at the given path in this json, returning the
     same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the integers will be appended
     @param number   the integer to be appended
     @param  others more optional integers to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final int number,
                              final int... others
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               number,
                                               others
                                              ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(Objects.requireNonNull(path)));
    }

    /**
     Appends one or more longs to the array located at the given path in this json, returning the
     same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the longs will be appended
     @param number   the long to be appended
     @param  others more optional longs to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final long number,
                              final long... others
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               number,
                                               others
                                              ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(Objects.requireNonNull(path)));

    }

    /**
     Appends one or more strings to the array located at the given path in this json, returning the
     same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the strings will be appended
     @param str   the string to be appended
     @param  others more optional strings to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final String str,
                              final String... others
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               str,
                                               others
                                              ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(Objects.requireNonNull(path)));

    }

    /**
     Appends one or more booleans to the array located at the given path in this json, returning the
     same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the booleans will be appended
     @param number   the boolean to be appended
     @param  others more optional booleans to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final boolean number,
                              final boolean... others
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               number,
                                               others
                                              ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(Objects.requireNonNull(path)));

    }

    /**
     Appends one or more doubles to the array located at the given path in this json, returning the
     same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the doubles will be appended
     @param number   the double to be appended
     @param  others more optional doubles to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final double number,
                              final double... others
                             )
    {
        return MatchExp.ifArrElse(it -> append(path,
                                               number,
                                               others
                                              ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(Objects.requireNonNull(path)));

    }

    /**
     Returns true if this json contains the given element in the first level.
     @param element the give element JsElem whose presence in this JsArray is to be tested
     @return true if this JsArray contains the  JsElem
     */
    boolean containsValue(JsValue element);


    /**
     Returns true if an element exists in this json at the given path.
     @param path the JsPath
     @return true if a JsElem exists at the JsPath
     */
    default boolean containsPath(final JsPath path)
    {
        return get(requireNonNull(path)).isNotNothing();

    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    default boolean equals(final JsValue elem,
                           final TYPE ARRAY_AS
                          )
    {
        if (elem == null || getClass() != elem.getClass()) return false;
        if (isObj()) return toJsObj().equals(elem.toJsObj(),
                                             ARRAY_AS
                                            );
        if (isArray()) return toJsArray().equals(elem.toJsArray(),
                                                 ARRAY_AS
                                                );
        return false;

    }

    /**
     Filters the pairs of elements in the first level of this json, removing those that don't ifPredicateElse
     the predicate.
     @param filter the predicate which takes as the input every JsPair in the first level of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterAllValues(Predicate) how to filter the pair of elements of the whole json and not only the first level
     */
    T filterValues(final Predicate<? super JsPair> filter);

    /**
     Filters all the pairs of elements of this json, removing those that don't ifPredicateElse the predicate.
     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterValues(Predicate) how to filter the pairs of values of only the first level
     */
    T filterAllValues(final Predicate<? super JsPair> filter);

    /**
     Filters the keys in the first level of this json, removing those that don't ifPredicateElse the predicate.
     @param filter the predicate which takes as the input every JsPair in the first level of this json
     @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T
     @see #filterAllKeys(Predicate) how to filter the keys of the whole json and not only the first level
     */
    T filterKeys(final Predicate<? super JsPair> filter);

    /**
     Filters all the keys of this json, removing those that don't ifPredicateElse the predicate.
     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T
     @see #filterKeys(Predicate) how to filter the keys of only the first level
     */
    T filterAllKeys(final Predicate<? super JsPair> filter);

    /**
     Filters the pair of jsons in the first level of this json, removing those that don't ifPredicateElse
     the predicate.
     @param filter the predicate which takes as the input every JsPair in the first level of this json

     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T

     @see #filterAllObjs(BiPredicate) how to filter the pair of jsons of the whole json and not only the first level
     */
    T filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter
                );

    /**
     Filters all the pair of jsons of this json, removing those that don't ifPredicateElse the predicate.
     @param filter the predicate which takes as the input every JsPair of this json
     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T
     @see #filterObjs(BiPredicate) how to filter the pair of jsons of only the first level
     */
    T filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter
                   );

    /**
     Returns the element located at the key or index specified by the given position or {@link JsNothing} if it
     doesn't exist.
     @param position key or index of the element
     @return the JsElem located at the given Position or JsNothing if it doesn't exist
     */
    JsValue get(final Position position);

    /**
     Returns the element located at the given path or {@link JsNothing} if it doesn't exist.
     @param path the JsPath object of the element that will be returned
     @return the JsElem located at the given JsPath or JsNothing if it doesn't exist
     */
    default JsValue get(final JsPath path)
    {
        if (path.isEmpty()) return this;
        final JsValue e = get(path.head());
        final JsPath tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isNotJson()) return NOTHING;
        return e.toJson()
                .get(tail);
    }

    /**
     Returns the array located at the given path or {@link Optional#empty()} if it doesn't exist or
     it's not an array.
     @param path the JsPath object of the JsArray that will be returned
     @return the JsArray located at the given JsPath wrapped in an Optional

     */
    default Optional<JsArray> getArray(final JsPath path)
    {
        final Function<JsValue, Optional<JsArray>> ifElse = MatchExp.ifArrElse(Optional::of,
                                                                              it -> Optional.empty()
                                                                              );
        return ifElse.apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the big decimal located at the given path as a big decimal or {@link Optional#empty()} if
     it doesn't exist or it's not a decimal number.
     @param path the JsPath object of the BigDecimal that will be returned
     @return the BigDecimal located at the given JsPath wrapped in an Optional
     */
    default Optional<BigDecimal> getBigDecimal(final JsPath path)
    {
        final Function<JsValue, Optional<BigDecimal>> ifElse = MatchExp.ifDecimalElse(it -> Optional.of(BigDecimal.valueOf(it)),
                                                                                      Optional::of,
                                                                                     it -> Optional.empty()
                                                                                     );
        return ifElse.apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the big integer located at the given path as a big integer or {@link Optional#empty()} if it doesn't
     exist or it's not an integral number.
     @param path the JsPath object of the BigInteger that will be returned
     @return the BigInteger located at the given JsPath wrapped in an Optional
     */
    default Optional<BigInteger> getBigInt(final JsPath path)
    {
        final Function<JsValue, Optional<BigInteger>> ifElse = MatchExp.ifIntegralElse(it -> Optional.of(BigInteger.valueOf(it)),
                                                                                      it -> Optional.of(BigInteger.valueOf(it)),
                                                                                       Optional::of,
                                                                                      e -> Optional.empty()
                                                                                      );
        return ifElse.apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the boolean located at the given path or {@link Optional#empty()} if it doesn't exist.
     @param path the JsPath object of the Boolean that will be returned
     @return the Boolean located at the given JsPath wrapped in an Optional
     */
    default Optional<Boolean> getBool(final JsPath path)
    {
        final Function<JsValue, Optional<Boolean>> fn = MatchExp.ifBoolElse(Optional::of,
                                                                           it -> Optional.empty()
                                                                           );
        return fn.apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the decimal number located at the given path as a double or {@link OptionalDouble#empty()} if it
     doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     the precision of the BigDecimal
     @param path the JsPath object of the double that will be returned
     @return the decimal number located at the given JsPath wrapped in an OptionalDouble
     */
    default OptionalDouble getDouble(final JsPath path)
    {
        return MatchExp.ifDecimalElse(OptionalDouble::of,
                                      bd -> JsBigDec.of(bd)
                                                    .doubleValueExact(),
                                      elem -> OptionalDouble.empty()
                                     )
                       .apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the integral number located at the given path as an integer or {@link OptionalInt#empty()} if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
     @param path the JsPath object of the integral number that will be returned
     @return the integral number located at the given JsPath wrapped in an OptionalInt
     */
    default OptionalInt getInt(final JsPath path)
    {
        return MatchExp.ifIntegralElse(OptionalInt::of,
                                       l -> JsLong.of(l)
                                                  .intValueExact(),
                                       bi -> JsBigInt.of(bi)
                                                     .intValueExact(),
                                       other -> OptionalInt.empty()
                                      )
                       .apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the integral number located at the given path as a long or {@link OptionalLong#empty()} if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.
     @param path the JsPath object of the integral number that will be returned
     @return the integral number located at the given JsPath wrapped in an OptionalLong
     */
    default OptionalLong getLong(final JsPath path)
    {
        return MatchExp.ifIntegralElse(OptionalLong::of,
                                       OptionalLong::of,
                                       bi -> JsBigInt.of(bi)
                                                     .longValueExact(),
                                       elem -> OptionalLong.empty()
                                      )
                       .apply(this.get(requireNonNull(path)));

    }

    /**
     Returns the object located at the given path or {@link Optional#empty()} if it doesn't exist or it's
     not an object.
     @param path the JsPath object of the JsObj that will be returned
     @return the JsObj located at the given JsPath wrapped in an Optional
     */
    default Optional<JsObj> getObj(final JsPath path)
    {
        final Function<JsValue, Optional<JsObj>> ifElse = MatchExp.ifObjElse(Optional::of,
                                                                            it -> Optional.empty()
                                                                            );
        return ifElse.apply(this.get(requireNonNull(path)));
    }

    /**
     Returns the string located at the given path or {@link Optional#empty()} if it doesn't exist or it's
     not an string.
     @param path the JsPath object of the JsStr that will be returned
     @return the JsStr located at the given path wrapped in an Optional
     */
    default Optional<String> getStr(final JsPath path)
    {
        final Function<JsValue, Optional<String>> ifStrElseFn = MatchExp.ifStrElse(Optional::of,
                                                                                  it -> Optional.empty()
                                                                                  );
        return ifStrElseFn.apply(this.get(requireNonNull(path)));

    }

    /**
     Declarative way of implementing if(this.isEmpty()) return emptySupplier.get() else return
     nonEmptySupplier.get()
     @param emptySupplier    Supplier that will produce the result if this json is empty
     @param nonemptySupplier Supplier that will produce the result if this json is not empty
     @param <A> the type of the result
     @return an object of type A

     */
    default <A> A ifEmptyElse(Supplier<A> emptySupplier,
                              Supplier<A> nonemptySupplier
                             )
    {

        return this.isEmpty() ? requireNonNull(emptySupplier).get() : requireNonNull(nonemptySupplier).get();
    }

    /**
     return true if there's no element in this json
     @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     return true if this json it not empty
     @return false if empty, true otherwise
     */
    default boolean isNotEmpty()
    {
        return !isEmpty();
    }

    default T map(UnaryOperator<T> fn)
    {
        //this is an instance of T (recursive type)
        @SuppressWarnings("unchecked") T o = fn.apply((T) this);
        return o;
    }

    /**
     Maps the values in the first level of this json.
     @param fn the mapping function

     @return a new mapped json of the same type T
     @see #mapObjs(BiFunction) to map jsons
     @see #mapKeys(Function) to map keys of json objects
     @see #mapAllValues(Function) to map all the values and not only the first level
     */
    T mapValues(final Function<? super JsPair, ? extends JsValue> fn);

    /**
     Maps the values in the first level of this json that satisfies a given predicate.
     @param fn the mapping function
     @param predicate the given predicate that determines what JsValues will be mapped
     @return same this instance or a new mapped json of the same type T


     @see #mapObjs(BiFunction, BiPredicate) to map jsons
     @see #mapKeys(Function, Predicate) to map keys of json objects
     @see #mapAllValues(Function, Predicate) to map all the values and not only the first level
     */
    T mapValues(final Function<? super JsPair, ? extends JsValue> fn,
                final Predicate<? super JsPair> predicate
               );

    /**
     Maps all the values of this json.
     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllObjs(BiFunction) to map jsons
     @see #mapAllKeys(Function) to map keys of json objects
     @see #mapValues(Function) to map only the first level
     */
    T mapAllValues(final Function<? super JsPair, ? extends JsValue> fn);

    /**
     Maps all the values of this json that satisfies a given predicate.
     @param fn the  mapping function
     @param predicate the given predicate that determines what JsValues will be mapped
     @return same this instance or a new mapped json of the same type TT
     @see #mapAllObjs(BiFunction, BiPredicate) to map jsons
     @see #mapAllKeys(Function, Predicate) to map keys of json objects
     @see #mapValues(Function, Predicate) to map only the first level
     */
    T mapAllValues(final Function<? super JsPair, ? extends JsValue> fn,
                   final Predicate<? super JsPair> predicate
                  );

    /**
     Maps the keys in the first level of this json.
     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapValues(Function) to map values
     @see #mapObjs(BiFunction) to map jsons
     @see #mapAllKeys(Function) to map all the keys and not only the first level
     */
    T mapKeys(final Function<? super JsPair, String> fn);

    /**
     Maps the keys in the first level of this json that satisfies a given predicate.
     @param fn the mapping function
     @param predicate the given predicate that determines what keys will be mapped
     @return same this instance or a new mapped json of the same type T
     @see #mapObjs(BiFunction, BiPredicate) to map jsons
     @see #mapValues(Function, Predicate) to map values
     @see #mapAllKeys(Function, Predicate) to map all the keys and not only the first level
     */

    T mapKeys(final Function<? super JsPair, String> fn,
              final Predicate<? super JsPair> predicate
             );

    /**
     Maps all the keys of this json.
     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllValues(Function) to map values
     @see #mapAllObjs(BiFunction) to map jsons
     @see #mapKeys(Function) to map only the first level
     */
    T mapAllKeys(final Function<? super JsPair, String> fn);

    /**
     Maps all the keys of this json that satisfies a given predicate.
     @param fn the mapping function
     @param predicate the given predicate that determines what keys will be mapped
     @return same this instance or a new mapped json of the same type T
     @see #mapAllValues(Function, Predicate) to map values
     @see #mapAllObjs(BiFunction, BiPredicate) to map jsons
     @see #mapKeys(Function, Predicate) to map only the first level
     */
    T mapAllKeys(final Function<? super JsPair, String> fn,
                 final Predicate<? super JsPair> predicate
                );

    /**
     Maps the jsons in the first level of this json that satisfies a given predicate.
     @param fn the mapping function
     @param predicate the given predicate that determines what Jsons will be mapped
     @return same this instance or a new mapped json of the same type T
     @see #mapValues(Function, Predicate) to map values
     @see #mapKeys(Function, Predicate) to map keys of json objects
     @see #mapAllObjs(BiFunction, BiPredicate) to map all the jsons and not only the first level
     */
    T mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
              final BiPredicate<? super JsPath, ? super JsObj> predicate
             );

    /**
     Maps the jsons in the first level of this json.
     @param fn the  mapping function
     @return a new mapped json of the same type T
     @see #mapValues(Function) to map values
     @see #mapKeys(Function) to map keys of json objects
     @see #mapAllObjs(BiFunction) to map all the jsons and not only the first level
     */
    T mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn
             );

    /**
     Maps all the jsons of this json that satisfies a given predicate.
     @param fn the  mapping function
     @param predicate the given predicate that determines what Jsons will be mapped
     @return same this instance or a new mapped json of the same type T
     @see #mapAllValues(Function, Predicate) to map values
     @see #mapAllKeys(Function, Predicate) to map keys of json objects
     @see #mapObjs(BiFunction, BiPredicate) to map only the first level
     */
    T mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                );

    /**
     Maps all the jsons of this json.
     @param fn the mapping function
     @return a new mapped json of the same type T
     @see #mapAllValues(Function) to map values
     @see #mapAllKeys(Function) to map keys of json objects
     @see #mapObjs(BiFunction) to map only the first level
     */
    T mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn
                );

    /**
     Tries to parse the string into an immutable json.
     @return an immutable json
     @param str the string that will be parsed
     @throws MalformedJson if the string doesnt represent a json
     */
    static Json<?> parse(String str) throws MalformedJson
    {

        try (JsonParser parser = JsonLibsFactory.jackson.createParser(requireNonNull(str)))
        {
            final JsonToken event = parser.nextToken();
            if (event == START_ARRAY)
            {
                return new JsArray(JsArray.parse(parser
                                                                 )
                );
            }
            return new JsObj(JsObj.parse(parser
                                                         )

            );
        }


        catch (IOException e)
        {

            throw new MalformedJson(e.getMessage());

        }


    }

    /**
     Tries to parse the string into an immutable json, performing the specified transformations while the parsing.
     @return an immutable json
     @param str     the string that will be parsed
     @param builder a builder with the transformations that will be applied during the parsing
     @throws MalformedJson if it's not a valid Json
     */
    static Json<?> parse(String str,
                          ParseBuilder builder
                         ) throws MalformedJson
    {

        try (JsonParser parser = JsonLibsFactory.jackson.createParser(requireNonNull(str)))
        {
            final JsonToken event = parser.nextToken();
            if (event == START_ARRAY) return new JsArray(JsArray.parse(parser,
                                                                                        builder.create(),
                                                                                        JsPath.empty()
                                                                                              .index(-1)

                                                                                       )

            );
            return new JsObj(JsObj.parse(parser,
                                                          builder.create(),
                                                          JsPath.empty()
                                                         )

            );

        }
        catch (IOException e)
        {

            throw new MalformedJson(e.getMessage());

        }


    }

    /**
     prepends one or more elements, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which all the elements will be prepended
     @param elem   the first JsElem to be prepended to the existing or created array
     @param others more optional JsElem to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final JsValue elem,
                      final JsValue... others
                     )
    {
        // T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    Objects.requireNonNull(others[i - 1])
                                   );
        }
        return result.prepend(path,
                              elem
                             );
    }

    /**
     prepends one element to the array located at the path in this json. If the array at the path doesn't
     exist, a new one is created, replacing any existing element in the path and filling empty indexes in
     arrays with {@link jsonvalues.JsNull} when necessary. The same this instance is returned when it's
     an array and the head of the path is a key or when it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which the element will be prepended
     @param elem   the JsElem to be prepended to the existing or created array
     @return same this instance or a new json of the same type T
     */
    T prepend(final JsPath path,
              final JsValue elem
             );

    /**
     Prepends one or more strings, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the path-like string pointing to the array in which all the string will be prepended
     @param elem   the first string to be prepended to the existing or created array
     @param others more optional strings to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final String elem,
                      final String... others
                     )
    {
        // T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    JsStr.of(Objects.requireNonNull(others[i - 1]))
                                   );
        }
        return result.prepend(path,
                              JsStr.of(elem)
                             );
    }

    /**
     prepends one or more integers, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which all the integers will be prepended
     @param elem   the first integer to be prepended to the existing or created array
     @param others more optional integers to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final int elem,
                      final int... others
                     )
    {
// T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    JsInt.of(others[i - 1])
                                   );
        }
        return result.prepend(path,
                              JsInt.of(elem)
                             );
    }

    /**
     prepends one or more longs, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which all the longs will be prepended
     @param elem   the first long to be prepended to the existing or created array
     @param others more optional longs to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final long elem,
                      final long... others
                     )
    {
// T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    JsLong.of(others[i - 1])
                                   );
        }
        return result.prepend(path,
                              JsLong.of(elem)
                             );
    }

    /**
     prepends one or more booleans, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which all the booleans will be prepended
     @param elem   the first boolean to be prepended to the existing or created array
     @param others more optional booleans to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final boolean elem,
                      final boolean... others
                     )
    {
        // T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    JsBool.of(others[i - 1])
                                   );
        }
        return result.prepend(path,
                              JsBool.of(elem)
                             );
    }

    /**
     prepends one or more doubles, starting from the first, to the array located at the path in this
     json. If the array at the path doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the JsPath pointing to the array in which all the doubles will be prepended
     @param elem   the first double to be prepended to the existing or created array
     @param others more optional doubles to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prepend(final JsPath path,
                      final double elem,
                      final double... others
                     )
    {
        // T recursive type, this is an instance of T
        @SuppressWarnings("unchecked")
        T result = (T) this;
        for (int i = Objects.requireNonNull(others).length; i > 0; i--)
        {
            result = result.prepend(path,
                                    JsDouble.of(others[i - 1])
                                   );
        }
        return result.prepend(path,
                              JsDouble.of(elem)
                             );
    }

    /**
     prepends all the elements of the array, starting from the head, to the array located at the path
     in this json. If the array at the path doesn't exist, a new one is created, replacing any existing
     element in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when
     necessary. The same this instance is returned when it's an array and the head of the path is
     a key or when it's an object and the head of the path is an index.
     @param path  the JsPath pointing to the array in which all the elements will be prepended
     @param elems the JsArray of elements to be prepended to the existing or created array

     @return same this instance or a new json of the same type T
     */
    T prependAll(final JsPath path,
                 final JsArray elems
                );

    /**
     Prepends all the elements of the array computed by the supplier, starting from the head, to the
     array located at the path in this json, returning the same this instance if the array is not present,
     in which case, the supplier is not invoked.
     @param path the JsPath object pointing to the existing array in which all the elements will be prepended
     @param supplier   the supplier of the array of elements that will be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependAllIfPresent(final JsPath path,
                                  final Supplier<JsArray> supplier
                                 )
    {

        requireNonNull(supplier);
        return MatchExp.ifArrElse(it -> prependAll(path,
                                                   supplier.get()
                                                  ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(requireNonNull(path)));
    }

    /**
     Prepends one element given by a supplier, to the array located at the given path in this json,
     returning the same this instance if the array is not present. The supplier is not applied if
     there's no array at the specified path.
     @param path   the JsPath pointing to the existing array in which all the elements will be appended
     @param supplier   the given supplier
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final Supplier<JsValue> supplier
                              )
    {

        requireNonNull(path);
        requireNonNull(supplier);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                supplier.get()
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Prepends one or more integers to the array located at the given path in this json in the following
     order [number, others, existing elements], returning the same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the integers will be prepended
     @param number   the integer to be prepended
     @param  others more optional integers to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final int number,
                               final int... others
                              )
    {

        requireNonNull(path);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                number,
                                                others
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Prepends one or more longs to the array located at the given path in this json in the following
     order [number, others, existing elements], returning the same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the longs will be prepended
     @param number   the long to be prepended
     @param  others more optional longs to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final long number,
                               final long... others
                              )
    {

        requireNonNull(path);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                number,
                                                others
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Prepends one or more doubles to the array located at the given path in this json in the following
     order [number, others, existing elements], returning the same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the doubles will be prepended
     @param number   the double to be prepended
     @param  others more optional doubles to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final double number,
                               final double... others
                              )
    {

        requireNonNull(path);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                number,
                                                others
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Prepends one or more strings to the array located at the given path in this json in the following
     order [number, others, existing elements], returning the same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the strings will be prepended
     @param str   the string to be prepended
     @param  others more optional strings to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final String str,
                               final String... others
                              )
    {

        requireNonNull(path);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                str,
                                                others
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Prepends one or more booleans to the array located at the given path in this json in the following
     order [number, others, existing elements], returning the same this instance if the array is not present.
     @param path   the path pointing to the existing array in which the booleans will be prepended
     @param bool   the boolean to be prepended
     @param  others more optional booleans to be prepended
     @return same this instance or a new json of the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final boolean bool,
                               final boolean... others
                              )
    {

        requireNonNull(path);
        return MatchExp.ifArrElse(it -> prepend(path,
                                                bool,
                                                others
                                               ),
                                  it ->
                                  {
                                      //this is an instance of T (recursive type)
                                      @SuppressWarnings("unchecked") final T t = (T) this;
                                      return t;
                                  }
                                 )
                       .apply(get(path));

    }

    /**
     Inserts the element returned by the function at the given path in this json, replacing any existing element
     and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary. The same instance
     is returned when the path is empty, or the head of the path is a key and this is an array or the head of the path is an index
     and this is an object. In both cases the function is not invoked.
     The same instance is returned as well when the element returned by the function is {@link JsNothing}
     @param path    the JsPath object where the JsElem will be inserted at
     @param fn the function that takes as an input the JsElem at the path and produces the JsElem to
     be inserted at the path
     @return the same instance or a new json of the same type T
     */
    T put(final JsPath path,
          final Function<? super JsValue, ? extends JsValue> fn
         );

    /**
     Inserts the element at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty
     indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head
     of the path is an index and this is an object or the element is {@link JsNothing}
     @param path    the JsPath object where the element will be inserted at
     @param element the JsElem that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final JsValue element
                 )
    {
        requireNonNull(path);
        requireNonNull(element);
        if (element.isNothing())
        {
            //this is an instance of T (recursive type)
            @SuppressWarnings("unchecked") final T t = (T) this;
            return t;
        }

        return put(path,
                   e -> element
                  );
    }

    /**
     Inserts the integer number at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull}
     empty indexes in arrays when necessary. The same instance is returned when the head of the path
     is a key and this is an array or the head of the path is an index and this is an object or the
     element is {@link JsNothing}
     @param path  the path where the integer number will be inserted at
     @param n the integer that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final int n
                 )
    {
        return put(requireNonNull(path),
                   JsInt.of(n)
                  );
    }

    /**
     Inserts the long number at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull}
     empty indexes in arrays when necessary. The same instance is returned when the head of the path
     is a key and this is an array or the head of the path is an index and this is an object or the
     element is {@link JsNothing}
     @param path the path where the long number will be inserted at
     @param n the long number that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final long n
                 )
    {
        return put(requireNonNull(path),
                   JsLong.of(n)
                  );
    }

    /**
     Inserts the string at the given path in this json, replacing any existing element in the path
     and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path the path where the string will be inserted at
     @param str the string that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final String str
                 )
    {
        return put(requireNonNull(path),
                   JsStr.of(str)
                  );
    }

    /**
     Inserts the big integer number at the given path in this json, replacing any existing element
     in teh path and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path    the given path where the big integer number will be inserted at
     @param bigint the big integer number that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final BigInteger bigint
                 )
    {
        return put(requireNonNull(path),
                   JsBigInt.of(requireNonNull(bigint))
                  );
    }

    /**
     Inserts the big decimal number at the given path in this json, replacing any existing element in
     the path and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path    the given path where the big decimal number will be inserted at
     @param bigdecimal the big decimal number that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final BigDecimal bigdecimal
                 )
    {
        return put(requireNonNull(path),
                   e -> JsBigDec.of(requireNonNull(bigdecimal))
                  );
    }

    /**
     Inserts the boolean at the given path in this json, replacing any existing element in the path
     and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path  the given path where the boolean will be inserted at
     @param bool the boolean that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final boolean bool
                 )
    {
        return put(requireNonNull(path),
                   JsBool.of(bool)
                  );
    }

    /**
     Inserts at the given path in this json, if the existing element satisfies the predicate, a new
     element returned by the function.
     If the predicate evaluates to false, the function is not computed. If the function returns {@link JsNothing},
     the same this instance is returned.
     @param predicate the predicate on which the existing element is tested on
     @param path the JsPath object
     @param fn the function witch computes the new element if the existing one satisfies the given predicate
     @return the same instance or a new json of the same type T
     */
    default T putIf(final Predicate<? super JsValue> predicate,
                    final JsPath path,
                    final Function<? super JsValue, ? extends JsValue> fn
                   )
    {
        final JsValue elem = get(requireNonNull(path));
        if (requireNonNull(predicate).test(elem)) return put(path,
                                                             requireNonNull(fn).apply(elem)
                                                            );
        //this is an instance of T (recursive type))
        @SuppressWarnings("unchecked") final T t = (T) this;

        return t;

    }

    /**
     Inserts at the given path in this json, if no element is present, the element returned by the
     supplier, replacing any existing element in the path and filling with {@link jsonvalues.JsNull}
     empty positions in arrays when necessary. The supplier is not invoked if the element is present.
     @param path the given JsPath object
     @param supplier the supplier which computes the new JsElem if absent
     @return the same instance or a new json of the same type T
     */
    default T putIfAbsent(final JsPath path,
                          final Supplier<? extends JsValue> supplier
                         )
    {
        requireNonNull(supplier);
        return putIf(JsValue::isNothing,
                     requireNonNull(path),
                     elem -> supplier.get()
                    );
    }

    /**
     Inserts at the given path in this json, if no element is present, the specified integer, replacing
     any existing element in the path and filling with {@link jsonvalues.JsNull} empty positions in
     arrays when necessary.
     @param path the given JsPath object
     @param number the specified integer
     @return the same instance or a new json of the same type T
     */
    default T putIfAbsent(final JsPath path,
                          final int number
                         )
    {
        return putIf(JsValue::isNothing,
                     requireNonNull(path),
                     elem -> JsInt.of(number)
                    );
    }

    /**
     Inserts at the given path in this json, if no element is present, the specified long, replacing
     any existing element in the path and filling with {@link jsonvalues.JsNull} empty positions in
     arrays when necessary.
     @param path the given JsPath object
     @param number the specified long
     @return the same instance or a new json of the same type T
     */
    default T putIfAbsent(final JsPath path,
                          final long number
                         )
    {
        return putIf(JsValue::isNothing,
                     requireNonNull(path),
                     elem -> JsLong.of(number)
                    );
    }

    /**
     Inserts at the given path in this json, if no element is present, the specified double, replacing
     any existing element in the path and filling with {@link jsonvalues.JsNull} empty positions in
     arrays when necessary.
     @param path the given JsPath object
     @param number the specified double
     @return the same instance or a new json of the same type T
     */
    default T putIfAbsent(final JsPath path,
                          final double number
                         )
    {
        return putIf(JsValue::isNothing,
                     requireNonNull(path),
                     elem -> JsDouble.of(number)
                    );
    }

    /**
     Inserts at the given path in this json, if some element is present, the specified integer.
     @param path the given path
     @param number the specified integer
     @return the same instance or a new json of the same type T
     */
    default T putIfPresent(final JsPath path,
                           final int number
                          )
    {
        return putIfPresent(path,
                            e -> JsInt.of(number)
                           );
    }

    /**
     Inserts at the given path in this json, if some element is present, the specified long.
     @param path the given path
     @param number the specified long
     @return the same instance or a new json of the same type T
     */
    default T putIfPresent(final JsPath path,
                           final long number
                          )
    {
        return putIfPresent(path,
                            e -> JsLong.of(number)
                           );
    }

    /**
     Inserts at the given path in this json, if some element is present, the specified double.
     @param path the given path
     @param number the specified double
     @return the same instance or a new json of the same type T
     */
    default T putIfPresent(final JsPath path,
                           final double number
                          )
    {
        return putIfPresent(path,
                            e -> JsDouble.of(number)
                           );
    }

    /**
     Inserts at the given path in this json, if some element is present, the element returned by the
     function.
     @param path the given JsPath object
     @param fn the function which computes the new JsElem from the existing one
     @return the same instance or a new json of the same type T
     */
    default T putIfPresent(final JsPath path,
                           final Function<? super JsValue, ? extends JsValue> fn
                          )
    {
        return putIf(JsValue::isNotNothing,
                     requireNonNull(path),
                     requireNonNull(fn)

                    );
    }

    /**
     Performs a reduction on the values that satisfy the predicate in the first level of this json. The reduction is performed mapping
     each value with the mapping function and then applying the operator
     @param op the operator upon two objects of type R
     @param map the mapping function which produces an object of type R from a JsValue
     @param predicate the predicate that determines what JsValue will be mapped and reduced
     @param <R> the type of the operands of the operator
     @return an {@link Optional} describing the of of the reduction
     @see #reduceAll(BinaryOperator, Function, Predicate) to apply the reduction in all the Json and not only in the first level
     */
    <R> Optional<R> reduce(BinaryOperator<R> op,
                           Function<? super JsPair, R> map,
                           Predicate<? super JsPair> predicate
                          );

    /**
     Performs a reduction on the values of this json that satisfy the predicate. The reduction is performed mapping
     each value with the mapping function and then applying the operator
     @param op the operator upon two objects of type R
     @param map the mapping function which produces an object of type R from a JsValue
     @param predicate the predicate that determines what JsValue will be mapped and reduced
     @param <R> the type of the operands of the operator
     @return an {@link Optional} describing the result of the reduction

     @see #reduce(BinaryOperator, Function, Predicate) to apply the reduction only in the first level
     */
    <R> Optional<R> reduceAll(BinaryOperator<R> op,
                              Function<? super JsPair, R> map,
                              Predicate<? super JsPair> predicate
                             );

    /**
     Removes the element in this json located at the given path, if it exists, returning the same this
     instance otherwise
     @param path the given JsPath object
     @return a json of the same type T
     */
    T remove(final JsPath path);

    /**
     Returns the number of elements in the first level of this json
     @return the number of elements in the first level of this json
     */
    int size();

    /**
     Returns the size of the json located at the given path in this json or OptionalInt.empty() if it
     doesn't exist or it's not a Json
     @param path the given JsPath object
     @return an OptionalInt
     */
    default OptionalInt size(final JsPath path)
    {

        return MatchExp.ifJsonElse(it -> OptionalInt.of(it.size()),
                                   it -> OptionalInt.empty()
                                  )
                       .apply(get(requireNonNull(path)));


    }

    /**
     Returns the number of all the elements in this json
     @return the number of all the elements in this json
     */
    default int sizeAll()
    {
        return streamAll().mapToInt(p -> 1)
                          .reduce(0,
                                Integer::sum
                               );
    }

    /**
     Returns the size of the json located at the given path in this json or OptionalInt.empty() if it
     doesn't exist or it's not a Json
     @param path the given JsPath object
     @return an OptionalInt
     */
    default OptionalInt sizeAll(final JsPath path)
    {

        return MatchExp.ifJsonElse(it -> OptionalInt.of(it.sizeAll()),
                                   it -> OptionalInt.empty()
                                  )
                       .apply(get(requireNonNull(path)));


    }

    /**
     Returns a stream over the pairs of elements in the first level of this json object.
     @return a {@code Stream} over all the JsPairs in the first level of this json
     */
    Stream<JsPair> stream();

    /**
     Returns a stream over all the pairs of elements in this json object.
     @return a {@code Stream} over all the JsPairs in this json
     */
    @SuppressWarnings("squid:S00100")
    Stream<JsPair> streamAll();

    default long times(JsValue e)
    {
        return stream().filter(p -> p.value.equals(Objects.requireNonNull(e)))
                       .count();
    }

    default long timesAll(JsValue e)
    {
        return streamAll().filter(p -> p.value.equals(Objects.requireNonNull(e)))
                          .count();
    }

    default byte[] serialize(){
        return JsonLibsFactory.serialize(this);
    }

    default void serialize(final OutputStream os) throws IOException
    {
        JsonLibsFactory.serialize(this,os);
    }
}