package jsonvalues;


import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.*;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;
import static jsonvalues.JsParser.Event.START_ARRAY;
import static jsonvalues.MyScalaImpl.Vector.EMPTY;

/**
 Represents a json array, which is an ordered list of elements. Two implementations are provided, an
 immutable which uses the persistent Scala Vector and a mutable which uses the conventional Java ArrayList.
 */
public interface JsArray extends Json<JsArray>, Iterable<JsElem>

{
     @SuppressWarnings("squid:S1214") //serializable class, explicit declaration of serialVersionUID is fine
     long serialVersionUID = 1L;


    /**
     Returns a mutable empty array.
     @return mutable empty JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _empty_()
    {
        return new JsArrayMutableImpl();
    }

    /**
     Returns a mutable array copying the reference of the collection of elements. Since the reference
     is copied, changes in the array are reflected in the list and vice versa. If the collection is
     immutable (created using List.of or Arrays.asList factory methods for example), every modification
     in the array will throw an UnsupportedOperationException.
     @param list the Collection of JsElem from which reference the JsArray will be created
     @return a mutable JsArray
     @throws UnsupportedOperationException if an elem of the list is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final List<JsElem> list)
    {
        throwErrorIfImmutableElemFound(requireNonNull(list));
        return new JsArrayMutableImpl(new MyJavaImpl.Vector(list));

    }


    /**
     Returns a mutable one-element array.
     @param e the JsElem
     @return a mutable one-element JsArray
     @throws UnsupportedOperationException if the elem is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final JsElem e)
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector().appendFront(throwErrorIfImmutableElem(e)));
    }

    /**
     Returns a mutable two-element array.
     @param e a JsElem
     @param e1 a JsElem
     @return a mutable two-element JsArray
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final JsElem e,
                        final JsElem e1
                       )
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector().appendFront(throwErrorIfImmutableElem(e1))
                                                             .appendFront(throwErrorIfImmutableElem(e)));
    }

    /**
     Returns a mutable three-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @return a mutable three-element JsArray
     @throws UnsupportedOperationException if an elem is an immutable Json

     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final JsElem e,
                        final JsElem e1,
                        final JsElem e2
                       )
    {

        return new JsArrayMutableImpl(new MyJavaImpl.Vector().appendFront(throwErrorIfImmutableElem(e2))
                                                             .appendFront(throwErrorIfImmutableElem(e1))
                                                             .appendFront(throwErrorIfImmutableElem(e)));
    }

    /**
     Returns a mutable four-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @return a mutable four-element JsArray
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final JsElem e,
                        final JsElem e1,
                        final JsElem e2,
                        final JsElem e3
                       )
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector().appendFront(throwErrorIfImmutableElem(e3))
                                                             .appendFront(throwErrorIfImmutableElem(e2))
                                                             .appendFront(throwErrorIfImmutableElem(e1))
                                                             .appendFront(throwErrorIfImmutableElem(e)));
    }

    /**
     Returns a mutable five-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @param e4 a JsElem
     @return a mutable five-element JsArray
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsArray _of_(final JsElem e,
                        final JsElem e1,
                        final JsElem e2,
                        final JsElem e3,
                        final JsElem e4
                       )
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector().appendFront(throwErrorIfImmutableElem(e4))
                                                             .appendFront(throwErrorIfImmutableElem(e3))
                                                             .appendFront(throwErrorIfImmutableElem(e2))
                                                             .appendFront(throwErrorIfImmutableElem(e1))
                                                             .appendFront(throwErrorIfImmutableElem(e)));
    }

    /**
     Returns a mutable array containing an arbitrary number of elements.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @param e4 a JsElem
     @param rest more optional JsElem
     @return a mutable  JsArray
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsArray _of_(final JsElem e,
                        final JsElem e1,
                        final JsElem e2,
                        final JsElem e3,
                        final JsElem e4,
                        final JsElem... rest
                       )
    {
        JsArray empty = _empty_();
        for (JsElem other : requireNonNull(rest)) empty = empty.append(throwErrorIfImmutableElem(other));

        return empty.prepend(throwErrorIfImmutableElem(e4))
                    .prepend(throwErrorIfImmutableElem(e3))
                    .prepend(throwErrorIfImmutableElem(e2))
                    .prepend(throwErrorIfImmutableElem(e1))
                    .prepend(throwErrorIfImmutableElem(e));


    }

    /**
     Tries to parse the string into a mutable json array.
     @param str the string to be parsed
     @return a {@link TryArr} computation
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static TryArr _parse_(final String str)
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))
        )
        )
        {
            JsParser.Event keyEvent = parser.next();
            if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
            MyJavaImpl.Vector array = new MyJavaImpl.Vector();
            Functions.parse(array,
                            parser
                           );
            return new TryArr(new JsArrayMutableImpl(array));
        }

        catch (MalformedJson e)
        {

            return new TryArr(e);
        }

    }

    /**
     Returns true if this array is equal to the given as a parameter. In the case of ARRAY_AS=LIST,
     this method is equivalent to JsArray.equals(Object).
     @param array the given array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return true if both arrays are equals according to ARRAY_AS parameter
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
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
     Tries to parse the string into a mutable array, performing some operations while the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     apply them later.
     @param str     the string that will be parsed.
     @param options a builder with the filters and maps that, if specified, will be applied during the parsing
     @return a {@link TryArr} computation
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static TryArr _parse_(final String str,
                          final ParseOptions options
                         )
    {


        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));
            MyJavaImpl.Vector array = new MyJavaImpl.Vector();
            Functions.parse(array,
                            parser,
                            options.create(),
                            MINUS_ONE_INDEX
                           );
            return new TryArr(new JsArrayMutableImpl(array));
        }

        catch (MalformedJson e)
        {

            return new TryArr(e);
        }


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
    JsArray append(final JsElem elem,
                   final JsElem... others
                  );

    /**
     Adds one or more elements, starting from the last, to the front of this array.
     @param elem   the JsElem to be added to the front.
     @param others more optional JsElem to be added to the front
     @return a new JsArray
     */
    JsArray prepend(final JsElem elem,
                    final JsElem... others
                   );


    /**
     Returns a collector that accumulates the pairs from a stream into an immutable array.
     @return a Collector which collects all the pairs of elements into an immutable JsArray, in encounter order
     */
    static Collector<JsPair, JsArray, JsArray> collector()
    {

        return Collector.of(jsonvalues.JsArray::_empty_,
                            (arr, pair) -> arr.put(pair.path,
                                                   pair.elem.isJson() ? pair.elem.asJson()
                                                                                 .toImmutable() : pair.elem
                                                  ),
                            (a, b) -> Functions.combiner_(a,
                                                          b
                                                         )
                                               .get(),
                            jsonvalues.JsArray::toImmutable
                           );

    }

    /**
     Returns a collector that accumulates the pairs from a stream into a mutable array.
     @return a Collector which collects all the pairs of elements into a mutable JsArray, in encounter order
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Collector<JsPair, JsArray, JsArray> _collector_()
    {

        return Collector.of(jsonvalues.JsArray::_empty_,
                            (arr, pair) -> arr.put(pair.path,
                                                   //could be immutable empty vector or array
                                                   pair.elem.isJson() ? pair.elem.asJson()
                                                                                 .toMutable() : pair.elem
                                                  ),
                            (a, b) -> Functions.combiner_(a,
                                                          b
                                                         )
                                               .get()
                           );

    }


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
     Returns the immutable empty array. The same instance is always returned.
     @return the singleton immutable empty JsArray.
     */
    static JsArray empty()
    {
        return JsArrayImmutableImpl.EMPTY;
    }

    /**
     Returns the first element of this array.
     @return the first JsElem of this JsArray
     @throws UnsupportedOperationException if this JsArray is empty
     */
    JsElem head();

    /**
     Returns all the elements of this array except the last one.
     @return JsArray with all the JsElem except the last one
     @throws UnsupportedOperationException if this JsArray is empty
     */
    JsArray init();


    /**
     Returns the last element of this array.
     @return the last JsElem of this JsArray
     @throws UnsupportedOperationException if this JsArray is empty
     */
    JsElem last();

    /**
     Returns an immutable array from the collection of elements.
     @param list the Collection of JsElem from which the JsArray will be created
     @return an immutable JsArray
     @throws UnsupportedOperationException if an elem of the list is a mutable Json

     */
    static JsArray of(final Collection<? extends JsElem> list)
    {
        if (requireNonNull(list).isEmpty()) return empty();
        Functions.throwErrorIfMutableElemFound(list);
        return new JsArrayImmutableImpl(EMPTY.add(list));

    }

    /**
     Returns an immutable one-element array.
     @param e the JsElem
     @return a mutable JsArray
     @throws UnsupportedOperationException if the elem is a mutable Json

     */
    static JsArray of(JsElem e)
    {
        return empty().prepend(throwErrorIfMutableElem(e));
    }

    /**
     Returns an immutable two-element array.
     @param e a JsElem
     @param e1 a JsElem
     @return an immutable two-element JsArray
     @throws UnsupportedOperationException if an elem is a mutable Json
     */
    static JsArray of(final JsElem e,
                      final JsElem e1
                     )
    {
        return empty().prepend(throwErrorIfMutableElem(e1))
                      .prepend(throwErrorIfMutableElem(e));

    }

    /**
     Returns an immutable three-element array.
     @param e  a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @return an immutable three-element JsArray
     @throws UnsupportedOperationException if an elem is a mutable Json
     */
    static JsArray of(final JsElem e,
                      final JsElem e1,
                      final JsElem e2
                     )
    {

        return empty().prepend(throwErrorIfMutableElem(e2))
                      .prepend(throwErrorIfMutableElem(e1))
                      .prepend(throwErrorIfMutableElem(e));
    }

    /**
     Returns an immutable four-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @return an immutable four-element JsArray
     @throws UnsupportedOperationException if an elem is a mutable Json
     */
    static JsArray of(final JsElem e,
                      final JsElem e1,
                      final JsElem e2,
                      final JsElem e3
                     )
    {
        return empty().prepend(throwErrorIfMutableElem(e3))
                      .prepend(throwErrorIfMutableElem(e2))
                      .prepend(throwErrorIfMutableElem(e1))
                      .prepend(throwErrorIfMutableElem(e));
    }

    /**
     Returns an immutable five-element array.
     @param e a JsElem
     @param e1 a JsElem
     @param e2 a JsElem
     @param e3 a JsElem
     @param e4 a JsElem
     @return an immutable five-element JsArray
     @throws UnsupportedOperationException if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsArray of(final JsElem e,
                      final JsElem e1,
                      final JsElem e2,
                      final JsElem e3,
                      final JsElem e4
                     )
    {
        return empty().prepend(throwErrorIfMutableElem(e4))
                      .prepend(throwErrorIfMutableElem(e3))
                      .prepend(throwErrorIfMutableElem(e2))
                      .prepend(throwErrorIfMutableElem(e1))
                      .prepend(throwErrorIfMutableElem(e));
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
     @throws UnsupportedOperationException if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsArray of(final JsElem e,
                      final JsElem e1,
                      final JsElem e2,
                      final JsElem e3,
                      final JsElem e4,
                      final JsElem... rest
                     )
    {
        JsArray empty = empty();
        for (JsElem other : requireNonNull(rest)) empty = empty.append(throwErrorIfMutableElem(other));

        return empty.prepend(throwErrorIfMutableElem(e4))
                    .prepend(throwErrorIfMutableElem(e3))
                    .prepend(throwErrorIfMutableElem(e2))
                    .prepend(throwErrorIfMutableElem(e1))
                    .prepend(throwErrorIfMutableElem(e));


    }

    /**
     Tries to parse the string into an immutable array.
     @param str the string to be parsed
     @return a {@link TryArr} computation
     */
    static TryArr parse(final String str)
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))
        )
        )
        {
            JsParser.Event keyEvent = parser.next();
            if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));

            return new TryArr(new JsArrayImmutableImpl(Functions.parse(EMPTY,
                                                                       parser

                                                                      )));
        }

        catch (MalformedJson e)
        {

            return new TryArr(e);
        }

    }

    /**
     Tries to parse the string into an immutable array, performing some operations during the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and applying them later.
     @param str string to be parsed
     @param options a Options with the filters and maps that will be applied during the parsing
     @return a {@link TryArr} computation
     */
    static TryArr parse(final String str,
                        final ParseOptions options
                       )
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_ARRAY != keyEvent) return new TryArr(MalformedJson.expectedArray(str));

            return new TryArr(new JsArrayImmutableImpl(Functions.parse(MyScalaImpl.Vector.EMPTY,
                                                                       parser,
                                                                       options.create(),
                                                                       MINUS_ONE_INDEX
                                                                      )));
        }

        catch (MalformedJson e)
        {

            return new TryArr(e);
        }

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

        MyScalaImpl.Vector vector = EMPTY.appendBack(JsStr.of(str));
        for (String a : others)
        {
            vector = vector.appendBack(JsStr.of(a));
        }
        return new JsArrayImmutableImpl(vector);
    }

    /**
     Returns a mutable array from one or more strings.
     @param str a string
     @param others more optional strings
     @return a mutable JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(String str,
                        String... others
                       )
    {

        MyJavaImpl.Vector vector = new MyJavaImpl.Vector().appendBack(JsStr.of(str));
        for (String a : others)
        {
            vector = vector.appendBack(JsStr.of(a));
        }
        return new JsArrayMutableImpl(vector);
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

        MyScalaImpl.Vector vector = EMPTY.appendBack(JsInt.of(number));
        for (int a : others)
        {
            vector = vector.appendBack(JsInt.of(a));
        }
        return new JsArrayImmutableImpl(vector);
    }

    /**
     Returns a mutable array from one or more integers
     @param number an integer
     @param others more optional integers
     @return an mutable JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final int number,
                        final int... others
                       )
    {

        MyJavaImpl.Vector vector = new MyJavaImpl.Vector().appendBack(JsInt.of(number));
        for (int a : others)
        {
            vector = vector.appendBack(JsInt.of(a));
        }
        return new JsArrayMutableImpl(vector);
    }

    /**
     Returns an immutable array from one or more longs.
     @param number a long
     @param others more optional longs
     @return an immutable JsArray
     */
    static JsArray of(long number,
                      long... others
                     )
    {

        MyScalaImpl.Vector vector = EMPTY.appendBack(JsLong.of(number));
        for (long a : others)
        {
            vector = vector.appendBack(JsLong.of(a));
        }
        return new JsArrayImmutableImpl(vector);
    }

    /**
     Returns an immutable array from one or more booleans.
     @param bool a boolean
     @param others more optional booleans
     @return an immutable JsArray
     */
    static JsArray of(boolean bool,
                      boolean... others
                     )
    {

        MyScalaImpl.Vector vector = EMPTY.appendBack(JsBool.of(bool));
        for (boolean a : others)
        {
            vector = vector.appendBack(JsBool.of(a));
        }
        return new JsArrayImmutableImpl(vector);
    }

    /**
     Returns an immutable array from one or more doubles.
     @param number a double
     @param others more optional doubles
     @return an immutable JsArray
     */
    static JsArray of(double number,
                      double... others
                     )
    {

        MyScalaImpl.Vector vector = EMPTY.appendBack(JsDouble.of(number));
        for (double a : others)
        {
            vector = vector.appendBack(JsDouble.of(a));
        }
        return new JsArrayImmutableImpl(vector);
    }

    /**
     Returns a mutable array from one or more longs.
     @param number a long
     @param others more optional longs
     @return a mutable JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final long number,
                        final long... others
                       )
    {

        MyJavaImpl.Vector vector = new MyJavaImpl.Vector().appendBack(JsLong.of(number));
        for (long a : others)
        {
            vector = vector.appendBack(JsLong.of(a));
        }
        return new JsArrayMutableImpl(vector);
    }

    /**
     Returns a mutable array from one or more booleans.
     @param bool a boolean
     @param others more optional booleans
     @return a mutable JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final boolean bool,
                        final boolean... others
                       )
    {

        MyJavaImpl.Vector vector = new MyJavaImpl.Vector().appendBack(JsBool.of(bool));
        for (boolean a : others)
        {
            vector = vector.appendBack(JsBool.of(a));
        }
        return new JsArrayMutableImpl(vector);
    }

    /**
     Returns a mutable array from one or more doubles.
     @param number a double
     @param others more optional doubles
     @return a mutable JsArray
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final double number,
                        final double... others
                       )
    {

        MyJavaImpl.Vector vector = new MyJavaImpl.Vector().appendBack(JsDouble.of(number));
        for (double a : others)
        {
            vector = vector.appendBack(JsDouble.of(a));
        }
        return new JsArrayMutableImpl(vector);
    }


    /**
     Returns a json array consisting of all elements of this array except the first one.
     @return a JsArray consisting of all the elements of this JsArray except the head
     @throws UnsupportedOperationException if this JsArray is empty.
     */
    JsArray tail();

    /**
     Returns the intersection of this array and another given as a parameter, defining characteristics
     like order and duplicates occurrence with the given ARRAY_AS parameter.
     @param that the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    JsArray intersection(final JsArray that,
                         final TYPE ARRAY_AS
                        );

    /**
     Returns the intersection of this array and another given as parameter considering both {@link TYPE#LIST}lists
     and applying recursively the intersection to those elements which are Json of the same type and
     are located at the same position.
     @param that the other array
     @return a JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    JsArray intersection_(final JsArray that);

    /**
     Returns the union of this array and another, defining characteristics like order and duplicates
     occurrence with the given ARRAY_AS parameter.
     @param that the other array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new json array of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    JsArray union(final JsArray that,
                  final TYPE ARRAY_AS
                 );


    /**
     Returns the union of this array and another given as parameter considering both {@link TYPE#LIST}lists
     and applying recursively the union to those elements which are Json of the same type and are located
     at the same position.
     @param that the other array
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    JsArray union_(final JsArray that);


    /**
     Returns an immutable array from one or more pairs.
     @param pair a pair
     @param pairs more optional pairs
     @return an immutable JsArray
     @throws UnsupportedOperationException if an elem of a pair is mutable

     */
    static JsArray of(JsPair pair,
                      JsPair... pairs
                     )
    {

        JsArray arr = empty().put(pair.path,
                                  throwErrorIfMutableElem(pair.elem)
                                 );
        for (final JsPair p : pairs)
        {
            arr = arr.put(p.path,
                          throwErrorIfMutableElem(p.elem)
                         );
        }

        return arr;
    }

    /**
     Returns a mutable array from one or more pairs.
     @param pair a pair
     @param pairs more optional pairs
     @return a mutable JsArray
     @throws UnsupportedOperationException if an elem of a pair is immutable
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsArray _of_(final JsPair pair,
                        final JsPair... pairs
                       )
    {

        JsArray arr = _empty_().put(pair.path,
                                    throwErrorIfImmutableElem(pair.elem)
                                   );
        for (final JsPair p : pairs)
        {
            arr.put(p.path,
                    throwErrorIfImmutableElem(p.elem)
                   );
        }

        return arr;
    }
}
