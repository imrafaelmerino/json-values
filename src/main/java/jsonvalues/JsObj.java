package jsonvalues;

import jsonvalues.JsArray.TYPE;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
public interface JsObj extends Json<JsObj>, Iterable<Map.Entry<String, JsElem>>
{
    /**
     return true if this obj is equal to the given as a parameter. In the case of ARRAY_AS=LIST, this
     method is equivalent to JsObj.equals(Object).
     @param that the given array
     @param ARRAY_AS enum to specify if arrays are considered as lists or sets or multisets
     @return true if both objs are equals
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    default boolean equals(final JsObj that,
                           final TYPE ARRAY_AS
                          )
    {
        if (isEmpty()) return that.isEmpty();
        if (that.isEmpty()) return isEmpty();
        return fields().stream()
                       .allMatch(field ->
                                 {
                                     final boolean exists = that.containsPath(JsPath.fromKey(field));
                                     if (!exists) return false;
                                     final JsElem elem = get(JsPath.fromKey(field));
                                     final JsElem thatElem = that.get(JsPath.fromKey(field));
                                     if (elem.isJson() && thatElem.isJson()) return elem.asJson()
                                                                                        .equals(thatElem,
                                                                                                ARRAY_AS
                                                                                               );
                                     return elem.equals(thatElem);
                                 }) && that.fields()
                                           .stream()
                                           .allMatch(f -> this.containsPath(JsPath.fromKey(f)));
    }

    /**
     Returns a set containing each key fo this object.
     @return a Set containing each key of this JsObj
     */
    Set<String> fields();

    /**
     Returns a pair with an arbitrary key of this object and its associated element. When using head
     and tail to process a JsObj, the key of the pair returned must be passed in to get the tail using
     the method {@link #tail(String)}.
     @return an arbitrary {@code Map.Entry<String,JsElem>} of this JsObj
     @throws UserError if this json object is empty
     */
    Map.Entry<String, JsElem> head();

    /**
     Returns a new object with all the entries of this json object except the one with the given key.
     @param key the given key, which associated pair will be excluded
     @return a new JsObj
     @throws UserError if this json object is empty
     */
    JsObj tail(final String key);

    /**
     {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this} and {@code that}
     {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both {@code this} and {@code that},
     being duplicates allowed.
     {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this} and {@code that},
     and are located at the same position.
     @param that the other obj
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117")
    //  ARRAY_AS  should be a valid name
    JsObj intersection(final JsObj that,
                       final TYPE ARRAY_AS
                      );


    /**
     {@code this.intersection_(that)} behaves as {@code this.intersection(that, LIST)}, but for those elements
     that are containers of the same type and are located at the same position, the result is their
     intersection.  So this operation is kind of a 'recursive' intersection.
     @param that the other object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    // squid:S00100_ naming convention: xx_ traverses the whole json
    // squid:S00117 ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00117", "squid:S00100"})
    JsObj intersection_(final JsObj that,
                        final TYPE ARRAY_AS
                       );

    /**
     returns {@code this} json object plus those pairs from the given json object {@code that} which
     keys don't exist in {@code this}. Taking that into account, it's not a commutative operation unless
     the elements associated with the keys that exist in both json objects are equals.
     @param that the given json object
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    JsObj union(final JsObj that);

    /**
     behaves like the {@link JsObj#union(JsObj)} but, for those keys that exit in both {@code this}
     and {@code that} json objects,
     which associated elements are **containers of the same type**, the result is their union. In this
     case, we can specify if arrays are considered Sets, Lists, or MultiSets. So this operation is kind of a
     'recursive' union.
     @param that the given json object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */

    // squid:S00100:  naming convention: _ traverses recursively
    // squid:squid:S00117: ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100", "squid:S00117"})
    JsObj union_(final JsObj that,
                 final TYPE ARRAY_AS
                );

    default <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                          final BiFunction<Map.Entry<String, JsElem>, JsObj, Trampoline<T>> fn
                                         )
    {


        if (this.isEmpty()) return empty;

        final Map.Entry<String, JsElem> head = this.head();

        final JsObj tail = this.tail(head.getKey());

        return fn.apply(head,
                        tail
                       );

    }

    @Override
    default boolean isObj()
    {
        return true;
    }

    boolean same(JsObj other);
}

