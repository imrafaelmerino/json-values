package jsonvalues;


import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;

/**
 Represents a json array, which is an ordered list of elements.
 */
public interface JsArray extends Json<JsArray>, Iterable<JsElem>

{

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
    JsElem head();

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
    JsElem last();

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
    JsArray intersection_(final JsArray that);

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
    JsArray union_(final JsArray that);

    default <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                          final BiFunction<JsElem, JsArray, Trampoline<T>> fn
                                         )
    {
        if (this.isEmpty()) return empty;
        final JsElem head = this.head();
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

    JsArray add(int index,
                JsElem elem
               );

    boolean same(JsArray other);

}
