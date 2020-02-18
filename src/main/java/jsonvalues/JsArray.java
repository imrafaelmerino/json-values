package jsonvalues;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.vavr.collection.Vector;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;

/**
 Represents a json array, which is an ordered list of elements.
 */
public interface JsArray extends Json<JsArray>, Iterable<JsValue>

{
    static JsArray empty(){return ImmutableJsArray.EMPTY; }

    /**
     Returns true if this array is equal to the given as a parameter. In the case of ARRAY_AS=LIST,
     this method is equivalent to JsArray.equals(Object).
     @param array the given array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return true if both arrays are equals according to ARRAY_AS parameter
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    default boolean equals(final JsArray array,
                           final TYPE ARRAY_AS
                          )
    {
        if (ARRAY_AS == LIST) return this.equals(array);
        if (isEmpty()) return array.isEmpty();
        if (array.isEmpty()) return isEmpty();
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(i -> get(Index.of(i)))
                        .allMatch(elem ->
                                  {
                                      if (!array.containsElem(elem)) return false;
                                      if (ARRAY_AS == MULTISET) return times(elem) == array.times(elem);
                                      return true;
                                  }) && IntStream.range(0,
                                                        array.size()
                                                       )
                                                 .mapToObj(i -> array.get(Index.of(i)))
                                                 .allMatch(this::containsElem);
    }


    /**
     Adds all the elements of the given array, starting from the head, to the back of this array.
     @param array the JsArray of elements to be added to the back
     @return a new JsArray
     */
    JsArray appendAll(JsArray array
                     );

    /**
     Adds all the elements of the array, starting from the last, to the front of this array.
     @param array the JsArray of elements to be added to the front
     @return a new JsArray
     */
    JsArray prependAll(JsArray array);


    /**
     Adds one or more elements, starting from the first, to the back of this array.
     @param elem   the JsElem to be added to the back.
     @param others more optional JsElem to be added to the back
     @return a new JsArray
     */
    JsArray append(final JsValue elem,
                   final JsValue... others
                  );

    /**
     Adds one or more elements, starting from the last, to the front of this array.
     @param elem   the JsElem to be added to the front.
     @param others more optional JsElem to be added to the front
     @return a new JsArray
     */
    JsArray prepend(final JsValue elem,
                    final JsValue... others
                   );


    /**
     Type of arrays: SET, MULTISET or LIST.
     */
    enum TYPE
    {
        /**
         The order of data items does not matter (or is undefined) but duplicate data items are not
         permitted.
         */
        SET,
        /**
         The order of data matters and duplicate data items are permitted.
         */
        LIST,
        /**
         The order of data items does not matter, but in this
         case duplicate data items are permitted.
         */
        MULTISET
    }


    /**
     Returns the first element of this array.
     @return the first JsElem of this JsArray
     @throws UserError if this JsArray is empty
     */
    JsValue head();

    /**
     Returns all the elements of this array except the last one.
     @return JsArray with all the JsElem except the last one
     @throws UserError if this JsArray is empty
     */
    JsArray init();


    /**
     Returns the last element of this array.
     @return the last JsElem of this JsArray
     @throws UserError if this JsArray is empty
     */
    JsValue last();

    /**
     Returns a json array consisting of all elements of this array except the first one.
     @return a JsArray consisting of all the elements of this JsArray except the head
     @throws UserError if this JsArray is empty.
     */
    JsArray tail();

    /**
     {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this}
     and {@code that}.
     {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both
     {@code this} and {@code that}, being duplicates allowed.
     {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this}
     and {@code that} and are located at the same position.
     @param that the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    JsArray intersection(final JsArray that,
                         final TYPE ARRAY_AS
                        );

    /**
     {@code this.intersection_(that)} behaves as {@code this.intersection(that, LIST)}, but for those
     elements that are containers of the same type and are located at the same position, the result
     is their intersection. So this operation is kind of a recursive intersection
     @param that the other array
     @return a JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100")
    //  naming convention: xx_ traverses the whole json
    JsArray intersectionAll(final JsArray that);

    /**
     {@code this.union(that, SET)} returns {@code this} plus those elements from {@code that} that
     don't exist in {@code this}.
     {@code this.union(that, MULTISET)} returns {@code this} plus those elements from {@code that}
     appended to the back.
     {@code this.union(that, LIST)} returns {@code this} plus those elements from {@code that} which
     position is {@code >= this.size()}.
     @param that the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new json array of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    JsArray union(final JsArray that,
                  final TYPE ARRAY_AS
                 );


    /**
     returns {@code this} plus those elements from {@code that} which position is  {@code >= this.size()},
     and, at the positions where a container of the same type exists in both {@code this} and {@code that},
     the result is their union. This operations doesn't make any sense if arrays are not considered lists,
     because there is no notion of order.
     @param that the other array
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100")
    //  naming convention: xx_ traverses the whole json
    JsArray unionAll(final JsArray that);

    default <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                          final BiFunction<JsValue, JsArray, Trampoline<T>> fn
                                         )
    {
        if (this.isEmpty()) return empty;
        final JsValue head = this.head();
        final JsArray tail = this.tail();
        return fn.apply(head,
                        tail
                       );
    }

    @Override
    default boolean isArray()
    {
        return true;
    }


    boolean same(JsArray other);

    static JsArray of(JsValue e)
    {

        return ImmutableJsArray.EMPTY.append(e);
    }

    /**
     Returns an immutable array from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return an immutable JsArray
     @throws UserError if an elem of a pair is mutable

     */
    static JsArray of(final JsPair pair,
                      final JsPair... others
                     )
    {
        JsArray arr = ImmutableJsArray.EMPTY.put(pair.path,
                                  pair.elem
                                 );
        for (JsPair p : others)
        {

            arr = arr.put(p.path,
                          p.elem
                         );
        }
        return arr;

    }


    /**
     Returns an immutable two-element array.
     @param e a JsElem
     @param e1 a JsElem
     @return an immutable two-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    static JsArray of(final JsValue e,
                      final JsValue e1
                     )
    {
        return of(e).append(e1);
    }

    /**
     Returns an immutable three-element array.
     @param e  a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @return an immutable three-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    static JsArray of(final JsValue e,
                      final JsValue e1,
                      final JsValue e2
                     )
    {

        return of(e,
                  e1
                 ).append(e2);
    }

    /**
     Returns an immutable four-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @return an immutable four-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    static JsArray of(final JsValue e,
                      final JsValue e1,
                      final JsValue e2,
                      final JsValue e3
                     )
    {
        return of(e,
                  e1,
                  e2
                 ).append(e3);
    }

    /**
     Returns an immutable five-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @param e4 a JsElem
     @return an immutable five-element JsArray
     @throws UserError if an elem is a mutable Json
     */
    //squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsArray of(final JsValue e,
                      final JsValue e1,
                      final JsValue e2,
                      final JsValue e3,
                      final JsValue e4
                     )
    {

        return of(e,
                  e1,
                  e2,
                  e3
                 ).append(e4);
    }

    /**
     Returns an immutable array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @param e4 a JsElem
     @param rest more optional JsElem
     @return an immutable JsArray
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsArray of(final JsValue e,
                      final JsValue e1,
                      final JsValue e2,
                      final JsValue e3,
                      final JsValue e4,
                      final JsValue... rest
                     )
    {
        JsArray result = of(e,
                            e1,
                            e2,
                            e3,
                            e4
                           );
        for (JsValue other : requireNonNull(rest))
        {
            result = result.append(other);
        }
        return result;


    }

    /**
     returns an immutable json array from an iterable of json elements
     @param iterable the iterable of json elements
     @return an immutable json array
     */
    static JsArray ofIterable(Iterable<JsValue> iterable)
    {
        Vector<JsValue> vector = Vector.empty();
        for (JsValue e : requireNonNull(iterable))
        {

            vector = vector.append(e);
        }
        return new ImmutableJsArray(vector

        );
    }

    /**
     Returns an immutable array from one or more strings.
     @param str a string
     @param others more optional strings
     @return an immutable JsArray
     */
    static JsArray of(String str,
                      String... others
                     )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsStr.of(str));
        for (String a : others)
        {
            vector = vector.append(JsStr.of(a));
        }
        return new ImmutableJsArray(vector
        );
    }


    /**
     Returns an immutable array from one or more integers.
     @param number an integer
     @param others more optional integers
     @return an immutable JsArray
     */
    static JsArray of(int number,
                      int... others
                     )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsInt.of(number));
        for (int a : others)
        {
            vector = vector.append(JsInt.of(a));
        }
        return new ImmutableJsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more booleans.
     @param bool an boolean
     @param others more optional booleans
     @return an immutable JsArray
     */
    static JsArray of(final boolean bool,
                      final boolean... others
                     )
    {
        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBool.of(bool));
        for (boolean a : others)
        {
            vector = vector.append(JsBool.of(a));
        }
        return new ImmutableJsArray(vector
        );
    }


    /**
     Returns an immutable array from one or more longs.
     @param number a long
     @param others more optional longs
     @return an immutable JsArray
     */
    static JsArray of(final long number,
                      final long... others
                     )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsLong.of(number));
        for (long a : others)
        {
            vector = vector.append(JsLong.of(a));
        }
        return new ImmutableJsArray(vector

        );
    }

    /**
     Returns an immutable array from one or more doubles.
     @param number a double
     @param others more optional doubles
     @return an immutable JsArray
     */
    static JsArray of(final double number,
                      final double... others
                     )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsDouble.of(number));
        for (double a : others)
        {
            vector = vector.append(JsDouble.of(a));
        }
        return new ImmutableJsArray(vector
        );
    }
    /**
     Tries to parse the string into an immutable json array.
     @param str the string to be parsed
     @return a Try computation
     */
    static JsArray parse(final String str) throws MalformedJson
    {

        try (JsonParser parser = JacksonFactory.instance.createParser(requireNonNull(str)))
        {
            final JsonToken keyEvent = parser.nextToken();
            if (START_ARRAY != keyEvent) throw MalformedJson.expectedArray(str);
            return  new ImmutableJsArray(AbstractJsArray.parse(parser
            ));
        }
        catch (IOException e)
        {

            throw new MalformedJson(e.getMessage());
        }

    }

    static JsArray parse(final String str,
                              final ParseBuilder builder
                             ) throws MalformedJson
    {

        try (JsonParser parser = JacksonFactory.instance.createParser(requireNonNull(str)))
        {
            final JsonToken keyEvent = parser.nextToken();
            if (START_ARRAY != keyEvent) throw MalformedJson.expectedArray(str);
            return new ImmutableJsArray(AbstractJsArray.parse(parser,
                                                          requireNonNull(builder).create(),
                                                          JsPath.fromIndex(-1)
                                                         )
            );
        }
        catch (IOException e)
        {
            throw new MalformedJson(e.getMessage());
        }

    }

}



