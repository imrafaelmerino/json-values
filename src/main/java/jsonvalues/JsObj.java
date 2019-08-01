package jsonvalues;

import jsonvalues.JsArray.TYPE;

import java.io.StringReader;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.throwErrorIfImmutableElem;
import static jsonvalues.Functions.throwErrorIfMutableElem;
import static jsonvalues.JsParser.Event.START_OBJECT;
import static jsonvalues.MyScalaImpl.Map.EMPTY;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
public interface JsObj extends Json<JsObj>, Iterable<Map.Entry<String, JsElem>>
{

    @SuppressWarnings("squid:S1214") //serializable class, explicit declaration of serialVersionUID is fine
    long serialVersionUID = 1L;

    /**
     Returns a mutable empty object.
     @return a mutable empty JsObj
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static JsObj _empty_()
    {
        return new JsObjMutableImpl();
    }

    /**
     Returns a mutable one-pair object.
     @param key name of the key
     @param el JsElem to be associated to the key
     @return a mutable one-pair JsObj
     @throws UnsupportedOperationException if the elem is an immutable Json

     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final String key,
                      final JsElem el
                     )
    {
        return _empty_().put(JsPath.empty()
                                   .key(requireNonNull(key)),
                             throwErrorIfImmutableElem(el)
                            );
    }

    /**
     Returns a mutable two-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @return a two-pair mutable JsObj
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2
                     )
    {

        return _of_(requireNonNull(key1),
                    throwErrorIfImmutableElem(el1)
                   ).put(JsPath.empty()
                               .key(requireNonNull(key2)),
                         throwErrorIfImmutableElem(el2)
                        );
    }

    /**
     Returns a mutable three-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @return a three-pair mutable JsObj
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3
                     )
    {
        return _of_(requireNonNull(key1),
                    throwErrorIfImmutableElem(el1),
                    requireNonNull(key2),
                    throwErrorIfImmutableElem(el2)
                   ).put(JsPath.empty()
                               .key(requireNonNull(key3)),
                         throwErrorIfImmutableElem(el3)
                        );
    }

    /**
     Returns a mutable four-pair object.
     @param key1 name of a key
     @param el1 JsElem to be associated to the key1
     @param key2 name of a key
     @param el2 JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @param key4 name of a key
     @param el4  JsElem to be associated to the key4
     @return a mutable four-pair JsObj
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsObj _of_(final String key1,
                      final JsElem el1,
                      final String key2,
                      final JsElem el2,
                      final String key3,
                      final JsElem el3,
                      final String key4,
                      final JsElem el4
                     )
    {
        return _of_(requireNonNull(key1),
                    throwErrorIfImmutableElem(el1),
                    requireNonNull(key2),
                    throwErrorIfImmutableElem(el2),
                    requireNonNull(key3),
                    throwErrorIfImmutableElem(el3)
                   ).put(JsPath.empty()
                               .key(requireNonNull(key4)),
                         throwErrorIfImmutableElem(el4)
                        );
    }

    /**
     Returns a mutable five-pair object.
     @param key1 name parse a key
     @param el1 JsElem to be associated to the key1
     @param key2 name parse a key
     @param el2 JsElem to be associated to the key2
     @param key3 name parse a key
     @param el3  JsElem to be associated to the key3
     @param key4 name parse a key
     @param el4  JsElem to be associated to the key4
     @param key5 name parse a key
     @param el5  JsElem to be associated to the key5
     @return a mutable five-element JsObj
     @throws UnsupportedOperationException if an elem is an immutable Json

     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsObj _of_(final String key1,
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
        return _of_(requireNonNull(key1),
                    throwErrorIfImmutableElem(el1),
                    requireNonNull(key2),
                    throwErrorIfImmutableElem(el2),
                    requireNonNull(key3),
                    throwErrorIfImmutableElem(el3),
                    requireNonNull(key4),
                    throwErrorIfImmutableElem(el4)
                   ).put(JsPath.empty()
                               .key(requireNonNull(key5)),
                         throwErrorIfImmutableElem(el5)
                        );
    }

    /**
     Returns a mutable six-pair object.
     @param key1 name parse a key
     @param el1 JsElem to be associated to the key1
     @param key2 name parse a key
     @param el2 JsElem to be associated to the key2
     @param key3 name parse a key
     @param el3  JsElem to be associated to the key3
     @param key4 name parse a key
     @param el4  JsElem to be associated to the key4
     @param key5 name parse a key
     @param el5  JsElem to be associated to the key5
     @param key6 name parse a key
     @param el6  JsElem to be associated to the key6
     @return a mutable six-element JsObj
     @throws UnsupportedOperationException if an elem is an immutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    // squid:S00100: naming convention: _xx_ returns immutable object
    @SuppressWarnings({"squid:S00100","squid:S00107"})
    static JsObj _of_(final String key1,
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
        return _of_(requireNonNull(key1),
                    throwErrorIfImmutableElem(el1),
                    requireNonNull(key2),
                    throwErrorIfImmutableElem(el2),
                    requireNonNull(key3),
                    throwErrorIfImmutableElem(el3),
                    requireNonNull(key4),
                    throwErrorIfImmutableElem(el4),
                    requireNonNull(key5),
                    throwErrorIfImmutableElem(el5)
                   ).put(JsPath.empty()
                               .key(requireNonNull(key6)),
                         throwErrorIfImmutableElem(el6)
                        );
    }
//

    /**
     Returns a mutable object from a map of elements.
     @param map the map of JsElem
     @return a mutable JsObj
     @throws UnsupportedOperationException if an elem of the map is an immutable Json

     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(final java.util.Map<String,JsElem> map)
    {
        Functions.throwErrorIfImmutableElemFound(Objects.requireNonNull(map)
                                                        .values());
        return new JsObjMutableImpl(new MyJavaImpl.Map(map));
    }

    /**
     Tries to parse the string into a mutable json object.
     @param str the string to be parsed
     @return a {@link TryObj} computation

     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static TryObj _parse_(final String str)
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            MyJavaImpl.Map obj = new MyJavaImpl.Map();
            Functions.parse(obj,
                            parser
                           );
            return new TryObj(new JsObjMutableImpl(obj));
        }

        catch (MalformedJson e)
        {

            return new TryObj(e);
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
    default boolean equals(JsObj that,
                           TYPE ARRAY_AS
                          )
    {
        if (isEmpty()) return that.isEmpty();
        if (that.isEmpty()) return isEmpty();
        return fields().stream()
                       .allMatch(field ->
                                 {
                                     final boolean exists = that.containsPath(field);
                                     if (!exists) return false;
                                     final JsElem elem = get(field);
                                     final JsElem thatElem = that.get(field);
                                     if (elem.isJson() && thatElem.isJson()) return elem.asJson()
                                                                                        .equals(thatElem,
                                                                                                ARRAY_AS
                                                                                               );
                                     return elem.equals(thatElem);
                                 }) && that.fields()
                                           .stream()
                                           .allMatch(this::containsPath);
    }


    /**
     Tries to parse the string into an mutable object, performing some operations while the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str  string to be parsed
     @param options a Options with the filters and maps that will be applied during the parsing
     @return a {@link TryObj} computation
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static TryObj _parse_(final String str,
                          final ParseOptions options
                         )
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            MyJavaImpl.Map obj = new MyJavaImpl.Map();
            Functions.parse(obj,
                            parser,
                            options.create(),
                            JsPath.empty()
                           );
            return new TryObj(new JsObjMutableImpl(obj));
        }

        catch (MalformedJson e)
        {

            return new TryObj(e);
        }


    }

    /**
     Returns a collector that accumulates the pairs from a stream into an immutable object.
     @return a Collector which collects all the pairs of elements into an immutable JsObj, in encounter order
     */
    static Collector<JsPair, JsObj, JsObj> collector()
    {

        return Collector.of(jsonvalues.JsObj::_empty_,
                            (obj, pair) -> obj.put(pair.path,
                                                   pair.elem.isJson() ? pair.elem.asJson()
                                                                                 .toImmutable() : pair.elem
                                                  ),
                            (a, b) -> Functions.combiner_(a,
                                                          b
                                                         )
                                               .get(),
                            jsonvalues.JsObj::toImmutable
                           );

    }

    /**
     Returns a collector that accumulates the pairs from a stream into an mutable object.
     @return a Collector which collects all the pairs of elements into an mutable JsObj, in encounter order
     */
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Collector<JsPair, JsObj, JsObj> _collector_()
    {

        return Collector.of(jsonvalues.JsObj::_empty_,
                            (obj, pair) -> obj.put(pair.path,
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
     Returns the immutable empty object. The same instance is always returned.
     @return the singleton immutable empty JsObj
     */
    static JsObj empty()
    {
        return JsObjImmutableImpl.EMPTY;
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
     @throws UnsupportedOperationException if this json object is empty
     */
    Map.Entry<String, JsElem> head();


    /**
     Returns an immutable one-element object.
     @param key  name of the key
     @param el  JsElem to be associated to the key
     @return an immutable one-element JsObj
     @throws UnsupportedOperationException if the elem is a mutable Json
     */
    static JsObj of(final String key,
                    final JsElem el
                   )
    {
        return empty().put(JsPath.empty()
                                 .key(requireNonNull(key)),
                           throwErrorIfMutableElem(el)
                          );
    }

    /**
     Returns a two-element immutable object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @return an immutable two-element JsObj
     @throws UnsupportedOperationException if an elem is a mutable Json

     */
    static JsObj of(final String key1,
                    final JsElem el1,
                    final String key2,
                    final JsElem el2
                   )
    {

        return of(requireNonNull(key1),
                  throwErrorIfMutableElem(el1)
                 ).put(JsPath.empty()
                             .key(requireNonNull(key2)),
                       throwErrorIfMutableElem(el2)
                      );
    }

    /**
     Returns a three-element immutable json object.
     @param key1 name of a key
     @param el1  JsElem to be associated to the key1
     @param key2 name of a key
     @param el2  JsElem to be associated to the key2
     @param key3 name of a key
     @param el3  JsElem to be associated to the key3
     @return an immutable three-element JsObj
     @throws UnsupportedOperationException if an elem is a mutable Json
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
        return of(requireNonNull(key1),
                  throwErrorIfMutableElem(el1),
                  requireNonNull(key2),
                  throwErrorIfMutableElem(el2)
                 ).put(JsPath.empty()
                             .key(requireNonNull(key3)),
                       throwErrorIfMutableElem(el3)
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
     @throws UnsupportedOperationException if an elem is a mutable Json

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
        return of(requireNonNull(key1),
                  throwErrorIfMutableElem(el1),
                  requireNonNull(key2),
                  throwErrorIfMutableElem(el2),
                  requireNonNull(key3),
                  throwErrorIfMutableElem(el3)
                 ).put(JsPath.empty()
                             .key(requireNonNull(key4)),
                       throwErrorIfMutableElem(el4)
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
     @throws UnsupportedOperationException if an elem is a mutable Json

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
        return of(requireNonNull(key1),
                  throwErrorIfMutableElem(el1),
                  requireNonNull(key2),
                  throwErrorIfMutableElem(el2),
                  requireNonNull(key3),
                  throwErrorIfMutableElem(el3),
                  requireNonNull(key4),
                  throwErrorIfMutableElem(el4)
                 ).put(JsPath.empty()
                             .key(requireNonNull(key5)),
                       throwErrorIfMutableElem(el5)
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
     @throws UnsupportedOperationException if an elem is a mutable Json

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
        return of(requireNonNull(key1),
                  throwErrorIfMutableElem(el1),
                  requireNonNull(key2),
                  throwErrorIfMutableElem(el2),
                  requireNonNull(key3),
                  throwErrorIfMutableElem(el3),
                  requireNonNull(key4),
                  throwErrorIfMutableElem(el4),
                  requireNonNull(key5),
                  throwErrorIfMutableElem(el5)
                 ).put(JsPath.empty()
                             .key(requireNonNull(key6)),
                       throwErrorIfMutableElem(el6)
                      );
    }

    /**
     Returns a immutable object from a map of elements.
     @param map the map of JsElem
     @return an immutable JsObj
     @throws UnsupportedOperationException if an elem of the map is a mutable Json
     */
    static JsObj of(final java.util.Map<String, JsElem> map)
    {
        if (requireNonNull(map).isEmpty()) return empty();
        Functions.throwErrorIfMutableElemFound(map.values());
        return new JsObjImmutableImpl(EMPTY.updateAll(map));
    }

    /**
     Tries to parse the string into an immutable object.
     @param str the string to be parsed
     @return a {@link TryObj} computation
     */
    static TryObj parse(final String str)
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            return new TryObj(new JsObjImmutableImpl(Functions.parse(EMPTY,
                                                                     parser

                                                                    )));
        }

        catch (MalformedJson e)
        {
            return new TryObj(e);
        }


    }


    /**
     Tries to parse the string into an immutable object, performing some operations during the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str  string to be parsed
     @param options a Options with the filters and maps that will be applied during the parsing
     @return a {@link TryObj} computation
     */
    static TryObj parse(final String str,
                        final ParseOptions options
                       )
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {
            JsParser.Event keyEvent = parser.next();
            if (START_OBJECT != keyEvent) return new TryObj(MalformedJson.expectedObj(str));
            return new TryObj(new JsObjImmutableImpl(Functions.parse(EMPTY,
                                                                     parser,
                                                                     options.create(),
                                                                     JsPath.empty()

                                                                    )));
        }

        catch (MalformedJson e)
        {

            return new TryObj(e);
        }


    }


    /**
     Removes the key from this object if it's present.
     @param key the key to be removed
     @return a new JsObj without the key or the same this JsObj if the key is not present
     */
    @Override
    default JsObj remove(final String key)
    {
        return remove(JsPath.of(requireNonNull(key)));
    }


    /**
     Returns a new object with all the entries of this json object except the one with the given key.
     @param key the given key, which associated pair will be excluded
     @return a new JsObj
     @throws UnsupportedOperationException if this json object is empty
     */
    JsObj tail(final String key);

    /**
     Returns the intersection of this object and another, defining characteristics like order and duplicates
     occurrence in arrays with the given ARRAY_AS parameter.
     @param that the other obj
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    JsObj intersection(final JsObj that,
                       final TYPE ARRAY_AS
                      );



     /**
     Returns the intersection of this object and another given as parameter applying recursively
     the intersection to those elements which are Json of the same type and are located at the same key
     and defining characteristics like order and duplicates occurrence in arrays with the given ARRAY_AS
     parameter.
     @param that the other object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
     @SuppressWarnings("squid:S00117") //  perfectly fine _
     JsObj intersection_(final JsObj that,
                        final TYPE ARRAY_AS
                       );


    /**
     Returns the union of this object and another given as a parameter.
     @param that the other object
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    JsObj union(final JsObj that);


    /**
     Returns the union of this object and another given as parameter applying recursively the union
     to those elements which are Json of the same type and are located at the same key
     and defining characteristics like order and duplicates occurrence in arrays with the given ARRAY_AS
     parameter.
     @param that the other object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    JsObj union_(final JsObj that,
                 final TYPE ARRAY_AS
                );



    /**
     Returns an immutable object from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return an immutable JsObject
     @throws UnsupportedOperationException if an elem of a pair is mutable

     */
    static JsObj of(final JsPair pair,
                    final JsPair... others
                   )
    {
        JsObj obj = empty().put(requireNonNull(pair.path),
                                throwErrorIfMutableElem(pair.elem)
                               );
        for (JsPair p : others)
        {
            obj = obj.put(requireNonNull(p.path),
                          throwErrorIfMutableElem(p.elem)
                         );
        }
        return obj;

    }

    /**
     Returns a mutable object from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return a mutable JsObject
     @throws UnsupportedOperationException if an elem of a pair is immutable
     */
    @SuppressWarnings("squid:S00100")//  naming convention: _xx_ returns immutable object
    static JsObj _of_(JsPair pair,
                      JsPair... others
                     )
    {
        JsObj obj = _empty_().put(requireNonNull(pair.path),
                                  throwErrorIfImmutableElem(pair.elem)
                                 );
        for (JsPair p : others)
        {
            obj.put(requireNonNull(p.path),
                    throwErrorIfImmutableElem(p.elem)
                   );
        }
        return obj;
    }


}

