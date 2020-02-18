package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.vavr.Tuple2;
import jsonvalues.JsArray.TYPE;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
public interface JsObj extends Json<JsObj>, Iterable<Tuple2<String, JsElem>>


{

    static JsObj empty(){return ImmutableJsObj.EMPTY; }
    /**
     Tries to parse the string into an immutable object.
     @param str the string to be parsed
     @return a Try computation
     */
    static JsObj parse(final String str) throws MalformedJson
    {

        try (JsonParser parser = JacksonFactory.instance.createParser(requireNonNull(str)))
        {
            JsonToken keyEvent = parser.nextToken();
            if (START_OBJECT != keyEvent) throw  MalformedJson.expectedObj(str);
            return new ImmutableJsObj(AbstractJsObj.parse(parser
                                                                     )
            );
        }

        catch (IOException e)
        {
            throw new MalformedJson(e.getMessage());
        }
    }

    /**
     Tries to parse the string into an immutable object,  performing the specified transformations during the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str  string to be parsed
     @param builder builder with the transformations that will be applied during the parsing
     @return a Try computation
     */
    static JsObj parse(final String str,
                            final ParseBuilder builder
                           ) throws MalformedJson
    {

        try (JsonParser parser = JacksonFactory.instance.createParser(requireNonNull(str.getBytes())))
        {
            final JsonToken keyEvent = parser.nextToken();
            if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
            return new ImmutableJsObj(ImmutableJsObj.parse(parser,
                                                                       requireNonNull(builder).create(),
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
     the method {@link #tail()}.
     @return an arbitrary {@code Map.Entry<String,JsElem>} of this JsObj
     @throws UserError if this json object is empty
     */
    Tuple2<String, JsElem> head();

    /**
     Returns a new object with all the entries of this json object except the one with the given key.
     @return a new JsObj
     @throws UserError if this json object is empty
     */
    JsObj tail();

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
                                          final BiFunction<Tuple2<String, JsElem>, JsObj, Trampoline<T>> fn
                                         )
    {


        if (this.isEmpty()) return empty;

        final Tuple2<String, JsElem> head = this.head();

        final JsObj tail = this.tail();

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

    /**
     Returns a one-element immutable object.
     @param key name of a key
     @param el  JsElem to be associated to the key
     @return an immutable one-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    static JsObj of(final String key,
                    final JsElem el
                   )
    {

        return ImmutableJsObj.EMPTY.put(JsPath.empty()
                                 .key(requireNonNull(key)),
                           el
                          );
    }

    /**
     Returns a two-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @return an immutable two-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2
                   )
    {

        return of(key1,
                  el1
                 ).put(JsPath.empty()
                             .key(requireNonNull(key2)),
                       el2
                      );
    }

    /**
     Returns a three-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @return an immutable three-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3
                   )
    {
        return of(key1,
                  el1,
                  key2,
                  el2
                 ).put(JsPath.empty()
                             .key(requireNonNull(key3)),
                       el3
                      );
    }

    /**
     Returns a four-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @return an immutable four-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4
                   )
    {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3
                 ).put(JsPath.empty()
                             .key(requireNonNull(key4)),
                       el4
                      );
    }

    /**
     Returns a five-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @param key5 name of a key
     @param el5 JsElem to be associated to the key5
     @return an immutable five-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4,
                    final String key5,
                    final JsElem el5
                   )
    {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4
                 ).put(JsPath.empty()
                             .key(requireNonNull(key5)),
                       el5
                      );
    }

    /**
     Returns a six-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4 JsElem to be associated to the key4
     @param key5 name of a key
     @param el5 JsElem to be associated to the key5
     @param key6 name of a key
     @param el6 JsElem to be associated to the key6
     @return an immutable six-element JsObj
     @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2,
                    final String key3,
                    final JsElem el3,
                    final String key4,
                    final JsElem el4,
                    final String key5,
                    final JsElem el5,
                    final String key6,
                    final JsElem el6
                   )
    {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5
                 ).put(JsPath.empty()
                             .key(requireNonNull(key6)),
                       el6
                      );
    }

    /**
     Returns an immutable object from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return an immutable JsObject
     @throws UserError if an elem of a pair is mutable

     */
    static JsObj of(final JsPair pair,
                    final JsPair... others
                   )
    {
        JsObj obj = ImmutableJsObj.EMPTY.put(pair.path,
                                pair.elem
                               );
        for (JsPair p : others)
        {

            obj = obj.put(p.path,
                          p.elem
                         );
        }
        return obj;

    }

    static JsObj ofIterable(Iterable<Map.Entry<String, JsElem>> xs)
    {
        JsObj acc = ImmutableJsObj.EMPTY;
        for (Map.Entry<String, JsElem> x : requireNonNull(xs))
        {

            acc = acc.put(JsPath.fromKey(x.getKey()),
                          x.getValue()
                         );
        }
        return acc;
    }


}

