package jsonvalues;

import jsonvalues.JsArray.TYPE;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.MINUS_ONE_INDEX;
import static jsonvalues.JsParser.Event.START_ARRAY;

/**
 <pre>
 Represents a json of type T, where T is the type of the container, either a JsObj or a JsArray.

 A json of any type can be modeled as a set of pairs {@link JsPair}=({@link JsPath}, {@link JsElem}), where:

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
 that were introduced in Java 8 (map, filter, reduce, etc). For this purpose the methods {@link #stream_()}
 or {@link #stream()} are provided. To put the stream back into an <b>immutable</b> json the collectors {@link JsObj#collector()}
 and {@link JsArray#collector()} can be used, whereas the collectors {@link JsObj#_collector_()} and {@link JsArray#_collector_()}
 would put the stream back into a <b>mutable</b> json.

 All the methods that accept a {@link JsPath} are overloaded and accept also a path-like string instead.

 There are two types of conventions on method names:

 -Static factory methods that are prefixed and suffixed with underscore return mutable instances.
 -Methods that are suffixed with underscore traverse the whole json recursively.

 All the methods throw a NullPointerException when any of the params passed in is null. The exception
 <code>UnsupportedOperationException</code> is thrown when the user calls a method inappropriately:
 for example calling the method <code>asJsStr</code> in a <code>JsNull</code> instance or calling the
 method head in an empty array, etc. Normally, when that happens, a previous check is missing.
 </pre>

 @param <T> Type of container: either an object or an array
 @see JsObj to work with jsons that are objects
 @see JsArray to work with jsons that are arrays

 @author Rafael Merino Garcia */
public interface Json<T extends Json<T>> extends JsElem, Serializable

{


    long serialVersionUID = 1L;

    /**
     Tries to parse the string into a mutable json.
     @param str the string to be parsed
     @return a {@link Try} computation
     */
    static Try _parse_(String str)
    {

        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();

            if (event == START_ARRAY)
            {
                final MyJavaImpl.Vector array = new MyJavaImpl.Vector();
                Functions.parse(array,
                                parser
                               );
                return new Try(new JsArrayMutableImpl(array));
            }

            final MyJavaImpl.Map obj = new MyJavaImpl.Map();
            Functions.parse(obj,
                            parser
                           );
            return new Try(new JsObjMutableImpl(obj));


        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }

    }


    /**
     Tries to parse the string into a mutable json, performing some operations while the parsing.
     It's faster to do certain operations right while the parsing instead of doing the parsing and
     applying them later.
     @param str     the string that will be parsed.
     @param options a builder with the filters and maps that, if specified, will be applied during the parsing
     @return a {@link Try} computation
     */
    static Try _parse_(String str,
                       ParseOptions options
                      )
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();

            if (event == START_ARRAY)
            {
                final MyJavaImpl.Vector array = new MyJavaImpl.Vector();
                Functions.parse(array,
                                parser,
                                options.create(),
                                MINUS_ONE_INDEX
                               );
                return new Try(new JsArrayMutableImpl(array));
            }

            final MyJavaImpl.Map obj = new MyJavaImpl.Map();
            Functions.parse(obj,
                            parser,
                            options.create(),
                            JsPath.empty()
                           );
            return new Try(new JsObjMutableImpl(obj));


        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }

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
    T appendAll(JsPath path,
                JsArray elems
               );

    /**
     Appends all the elements of the array, starting from the head, to the array located at the given
     path in this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path  the given path-like string pointing to the array in which all the elements will be appended
     @param elems the JsArray of elements to be appended to the existing or created array

     @return same this instance or a new json of the same type T
     */
    default T appendAll(String path,
                        JsArray elems
                       )
    {
        return appendAll(JsPath.of(path),
                         elems
                        );
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
                     final JsElem elem,
                     final JsElem... others
                    )
    {
        final ArrayList<JsElem> collection = new ArrayList<>(Arrays.asList(requireNonNull(others)));
        collection.add(0,
                       requireNonNull(elem)
                      );

        return appendAll(requireNonNull(path),
                         jsonvalues.JsArray.of(collection)
                        );

    }

    /**
     Appends one or more elements, starting from the first, to the array located at the given path in
     this json. If the array doesn't exist, a new one is created, replacing any existing element
     in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when
     it's an object and the head of the path is an index.
     @param path   the given path-like string pointing to the array in which all the elements will be appended
     @param elem   the first JsElem to be appended to the existing or created array
     @param others more optional JsElem to be appended

     @return same this instance or a new json of the same type T
     */
    default T append(final String path,
                     final JsElem elem,
                     final JsElem... others
                    )
    {
        return append(JsPath.of(path),
                      elem,
                      others
                     );
    }


    /**
     Appends one or more elements, starting from the first, to the array located at the given path in
     this json, returning the same this instance if the array is not present.
     @param path   the path-like string pointing to the existing array in which all the elements will be appended
     @param elem   the first JsElem to be appended to the existing array
     @param others more optional JsElem to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final String path,
                              final JsElem elem,
                              final JsElem... others
                             )
    {
        return appendIfPresent(JsPath.of(path),
                               elem,
                               others
                              );
    }

    /**
     Appends one or more elements, starting from the first, to the array located at the given path in
     this json, returning the same this instance if the array is not present.
     @param path   the JsPath pointing to the existing array in which all the elements will be appended
     @param elem   the first JsElem to be appended to the existing array
     @param others more optional JsElem to be appended
     @return same this instance or a new json of the same type T
     */
    default T appendIfPresent(final JsPath path,
                              final JsElem elem,
                              final JsElem... others
                             )
    {
        return Functions.ifArrElse(it -> append(path,
                                                elem,
                                                others
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
     Appends all the elements of the array computed by the function, starting from the head, to an
     array located at the given path in this json, returning the same this instance if the array is 
     not present, in which case, the function is not invoked.
     @param path the given JsPath object pointing to the existing array in which all the elements will be appended
     @param function   the function which input is the existing array and output the array of elements that will be appended
     @return same this instance or a new json of the same type T
     */
    default T appendAllIfPresent(final JsPath path,
                                 final Function<? super JsArray, JsArray> function


                                )
    {
        return Functions.ifArrElse(it -> appendAll(path,
                                                   requireNonNull(function).apply(it)
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
     Appends all the elements of the array computed by the function, starting from the head, to
     an array located at the path in this json, returning the same this instance if the array is not 
     present, in which case, the function is not invoked.
     @param path the path-like string pointing to the existing array in which all the elements will be appended
     @param function the function which input is the existing array and output the array of elements that will be appended
     @return same this instance or a new json of the same type T


     */
    default T appendAllIfPresent(final String path,
                                 final Function<? super JsArray, JsArray> function

                                )
    {
        return appendAllIfPresent(JsPath.of(path),
                                  function
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
    T prependAll(JsPath path,
                 JsArray elems
                );

    /**
     prepends all the elements of the array, starting from the head, to the array located at the path
     in this json. If the array at the path doesn't exist, a new one is created, replacing any existing 
     element in the path and filling empty indexes in arrays with {@link jsonvalues.JsNull} when 
     necessary. The same this instance is returned when it's an array and the head of the path is 
     a key or when it's an object and the head of the path is an index.
     @param path  the path-like string pointing to the array in which all the elements will be prepended
     @param elems the JsArray of elements to be prepended to the existing or created array

     @return same this instance or a new json of the same type T
     */
    default T prependAll(String path,
                         JsArray elems
                        )
    {
        return prependAll(JsPath.of(path),
                          elems
                         );
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

     @return same this instance or a new json parse the same type T
     */
    default T prepend(final JsPath path,
                      final JsElem elem,
                      final JsElem... others
                     )
    {

        final ArrayList<JsElem> collection = new ArrayList<>(Arrays.asList(requireNonNull(others)));
        collection.add(0,
                       requireNonNull(elem)
                      );

        return prependAll(requireNonNull(path),
                          jsonvalues.JsArray.of(collection)
                         );


    }

    /**
     prepends one or more elements, starting from the first, to the array located at the path in this json. If the array at the path doesn't
     exist, a new one is created, replacing any existing element and filling empty indexes in arrays with {@link jsonvalues.JsNull} when necessary.
     The same this instance is returned when it's an array and the head of the path is a key or when it's an object and the head of the path is an index.
     <p>
     Examples:<pre>{@code
    JsObj.empty().prepend("0.a",JsInt.parse(1)) == JsObj.empty()  // object containsElem keys and not indexes
    JsArray.empty().prepend("a.0",JsInt.parse(1)) == JsArray.empty() // array containsElem indexes and not keys

    //a new array is created at a.x
    JsObj.empty().prepend("a.x", JsInt.parse(1),JsStr.parse("a"),JsBool.FALSE)  // =>  {a: { x: [1, "a", false] } }

    //a new array is created at a.x.2, filling with null the positions a.x.0 and a.x.1
    JsObj.empty().prepend("a.x.2",JsBool.TRUE, JsBool.FALSE) // =>  {a: { x: [ null, null,  [true, false] ] } }

    // {a: { x: 2, c: "bye"} }, a.x=2 is replaced by an array
    JsObj a = JsObj.parse("{ 'a': { 'x' : 2, 'c': 'bye' } }".replaceAll("'","\"")).orElseThrow()
    a.prepend("a.x", JsBool.TRUE, JsBool.FALSE)  // =>  {a: { x: [true, false], c: "bye"} }

    // [ { a: { x: [ true ] } } ], two new elements are appended to the back parse the array at 0.a.x
    JsArray x = JsArray.parse("[ { 'a' : { 'x': [ true ] } } ]".replaceAll("'","\"")).orElseThrow()
    x.prepend("0.a.x", JsBool.TRUE, JsBool.FALSE)  // => [ { a: { x: [ true, false, true] } } ]
    }</pre>

     @param path   the path-like string pointing to the array in which all the elements will be prepended
     @param elem   the first JsElem to be prepended to the existing or created array
     @param others more optional JsElem to be prepended

     @return same this instance or a new json parse the same type T
     */
    default T prepend(final String path,
                      final JsElem elem,
                      final JsElem... others
                     )
    {

        return prepend(JsPath.of(requireNonNull(path)),
                       requireNonNull(elem),
                       requireNonNull(others)
                      );
    }


    /**
     prepends all the elements parse the array computed by the function, starting from the head, to the array located at the path in this json, returning the same
     this instance if the array is not present, in which case, the function is not invoked.
     <p>
     Examples:
     <pre>{@code
    // function that returns an array with the first and last element parse the input
    Function<JsArray,JsArray> fn = arr -> JsArray.parse(arr.get("0"), arr.get("-1"), JsBigInt.parse(BigInteger.ONE))

    JsObj.empty().prependAllIfPresent(JsPath.parse("a"),fn) == JsObj.empty()       //there's no array present at a

    JsArray.empty().prependAllIfPresent(JsPath.parse("0"),fn) == JsArray.empty()   //there's no array present at 0

    JsObj a = JsObj.parse("{ 'a': { 'x' : [1, 2, 3] } }".replaceAll("'","\"")).orElseThrow()
    a.prependAllIfPresent(JsPath.parse("a.x"),fn) // => {a: { x: [1, 3, 1, 1, 2, 3] } }
    }</pre>

     @param path the JsPath object pointing to the existing array in which all the elements will be prepended
     @param function   the function which input is the existing array and output the array of elements that will be prepended


     @return same this instance or a new json parse the same type T

     */
    default T prependAllIfPresent(final JsPath path,
                                  final Function<? super JsArray, JsArray> function
                                 )
    {

        requireNonNull(function);
        return Functions.ifArrElse(it -> prependAll(path,
                                                    function.apply(it)
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
     prepends all the elements parse the array computed by the function, starting from the head, to the array located at the path in this json, returning the same
     this instance if the array is not present, in which case, the function is not invoked.
     <p>
     Examples:
     <pre>{@code
    // function that returns an array with the first and last element parse the input
    Function<JsArray,JsArray> fn = arr -> JsArray.parse(arr.get("0"), arr.get("-1"), JsBigInt.parse(BigInteger.ONE))

    JsObj.empty().prependAllIfPresent("a",fn) == JsObj.empty()       //there's no array present at a

    JsArray.empty().prependAllIfPresent("0",fn) == JsArray.empty()   //there's no array present at 0

    JsObj a = JsObj.parse("{ 'a': { 'x' : [1, 2, 3] } }".replaceAll("'","\"")).orElseThrow()
    a.prependAllIfPresent("a.x",fn) // => {a: { x: [1, 3, 1, 1, 2, 3] } }
    }</pre>

     @param path the path-like string pointing  to the existing array in which all the elements will be prepended
     @param function   the function which input is the existing array and output the array of elements that will be prepended

     @return same this instance or a new json parse the same type T
     */
    default T prependAllIfPresent(final String path,
                                  final Function<? super JsArray, JsArray> function
                                 )
    {
        return prependAllIfPresent(JsPath.of(path),
                                   function
                                  );
    }


    /**
     prepends one or more elements, starting from the first, to the array located at the path in this json, returning the same this instance if the array is not present.
     <p>
     Examples:
     <pre>{@code
    //no array is present at a.x so no element is appended
    JsObj.empty().prependIfPresent(JsPath.parse("a.x"), JsInt.parse(1),JsNull.NULL) == JsObj.empty()

    //the element at a.x is not array (is an integer), so no element is appended
    JsObj a = JsObj.parse("{ 'a': { 'x' : 2, 'c': 'bye' } }".replaceAll("'","\"")).orElseThrow()
    a.prependIfPresent(JsPath.parse("a.x"), JsInt.parse(1)) == JsObj.empty()

    //two new elements are appended to the back parse the existing array at 0.a.x
    JsArray x = JsArray.parse("[ { 'a' : { 'x': [ true ] } } ]".replaceAll("'","\"")).orElseThrow()
    x.prependIfPresent(JsPath.parse("0.a.x"), JsInt.parse(1), JsStr.parse("hi"))  // => [ { a: { x: [ 1, "hi", true ] } } ]
    }</pre>


     @param path   the path-like string pointing to the array in which all the elements will be prepended
     @param elem   the first JsElem to be appended to the existing array
     @param others more optional JsElem to be appended

     @return same this instance or a new json parse the same type T
     */
    default T prependIfPresent(final String path,
                               final JsElem elem,
                               final JsElem... others
                              )
    {

        return prependIfPresent(JsPath.of(requireNonNull(path)),
                                requireNonNull(elem),
                                requireNonNull(others)
                               );
    }

    /**
     prepends one or more elements, starting from the first, to the array located at the path in this json, returning the same this instance if the array is not present.
     <p>
     Examples:
     <pre>{@code
    //no array is present at a.x so no element is appended
    JsObj.empty().prependIfPresent(JsPath.parse("a.x"), JsInt.parse(1),JsNull.NULL) == JsObj.empty()

    //the element at a.x is not array (is an integer), so no element is appended
    JsObj a = JsObj.parse("{ 'a': { 'x' : 2, 'c': 'bye' } }".replaceAll("'","\"")).orElseThrow()
    a.prependIfPresent(JsPath.parse("a.x"), JsInt.parse(1)) == JsObj.empty()

    //two new elements are appended to the back parse the existing array at 0.a.x
    JsArray x = JsArray.parse("[ { 'a' : { 'x': [ true ] } } ]".replaceAll("'","\"")).orElseThrow()
    x.prependIfPresent(JsPath.parse("0.a.x"), JsInt.parse(1), JsStr.parse("hi"))  // => [ { a: { x: [ 1, "hi", true] } } ]
    }</pre>

     @param path   the JsPath pointing to the array in which all the elements will be prepended
     @param elem   the first JsElem to be appended to the existing array
     @param others more optional JsElem to be appended

     @return same this instance or a new json parse the same type T
     */
    default T prependIfPresent(final JsPath path,
                               final JsElem elem,
                               final JsElem... others
                              )
    {

        requireNonNull(path);
        requireNonNull(elem);
        requireNonNull(others);
        return Functions.ifArrElse(it -> prepend(path,
                                                 elem,
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
     Filters the pairs of elements in the first level of this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':1,'x':'apple','c':[1,'peach',false],'e':{'f':'orange','g':2},'g':false}".replace("'","\"")
    Json.parse(a).objOrElseThrow().filterElems(p->p.elem.isStr())
    //{"e":{"f":"orange","g":2},"x":"apple","c":[1,"peach",false]}

    Json.parse(a).objOrElseThrow().filterElems(p->p.elem.isIntegral())
    //{"e":{"f":"orange", "g":2},"a":1,"c":[1,"peach",false]}
    }</pre>

     @param filter the predicate which takes as the input every JsPair in the first level parse this json

     @return same this instance if all the pairs satisfy the predicate or a new filtered json parse the same type T

     @see #filterElems_(Predicate) how to filter the pair of elements of the whole json and not only the first level
     */
    T filterElems(final Predicate<JsPair> filter);

    /**
     Filters all the pairs of elements of this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':1,'x':'apple','c':[1,'peach',false],'e':{'f':'},'g':false}".replace("'","\"")
    Json.parse(a).objOrElseThrow().filterElems_(p->p.elem.isStr())
    //{"e":{"f":"orange"},"x":"apple","c":["peach"]}

    Json.parse(a).objOrElseThrow().filterElems_(p->p.elem.isIntegral())
    //{"e":{"g":2},"a":1,"c":[1]}

    String x = "{'a':1,'x':'apple','c':2, 'DD':3, 'EE':4, 'f':{'GG':2,'h':3}}".replace("'","\"")
    Json.parse(x).objOrElseThrow().filterElems_(p->p.elem.isIntegral() && p.path.last().isKey(key->key.length()==1))
    //{"f":{"h":3},"a":1,"c":2}

    Json.parse(x).objOrElseThrow().filterElems_(p->p.elem.isIntegral() && p.path.head().isKey(key->key.length()==1))
    //{"f":{"GG":2,"h":3},"a":1,"c":2}
    }</pre>

     @param filter the predicate which takes as the input every JsPair of this json

     @return same this instance if all the pairs satisfy the predicate or a new filtered json of the same type T

     @see #filterElems(Predicate) how to filter the pairs of values parse only the first level
     */
    T filterElems_(final Predicate<JsPair> filter);

    /**
     Filters the pair of jsons in the first level parse this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':{},'x':[],'c':[1,2,{},[]],'e':{'f':'a','g':2},'g':false}".replace("'","\"")
    Json.parse(a).objOrElseThrow().filterObjs(p->p.elem.isNotEmpty())
    // {"e":{"f":"a","g":2},"g":false,"c":[1,2,{},[]]}

    Json.parse(a).objOrElseThrow().filterObjs(p->p.path.isNotEmpty)
    }</pre>

     @param filter the predicate which takes as the input every JsPair in the first level parse this json

     @return same this instance if all the pairs satisfy the predicate or a new filtered json parse the same type T

     @see #filterObjs_(BiPredicate) how to filter the pair of jsons parse the whole json and not only the first level
     */
    T filterObjs(final BiPredicate<JsPath, JsObj> filter
                );

    /**
     Filters all the pair of jsons parse this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':{},'x':[],'c':[1,2,{},[],[true,false]],'e':{'f':'a','g':2},'g':false}".replace("'","\"")

    Json.parse(a).objOrElseThrow().filterObjs_(p->p.elem.isNotEmpty())
    // {"e":{"f":"a","g":2},"g":false,"c":[1,2,[true,false]]}

    Json.parse(a).objOrElseThrow().filterObjs_(p->p.path.last().isKey())
    //{"e":{"f":"a","g":2},"a":{},"x":[],"g":false,"c":[1,2]}

    }</pre>

     @param filter the predicate which takes as the input every JsPair of this json

     @return same this instance if all the pairs satisfy the predicate or a new filtered json parse the same type T

     @see #filterObjs(BiPredicate) how to filter the pair of jsons parse only the first level
     */
    T filterObjs_(final BiPredicate<JsPath, JsObj> filter
                 );

    /**
     Filters the keys in the first level parse this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':1,'x':'a','cc':[1,2,{'a':1,'bb':'h'}],'ee':{'f':'g','gg':2},'g':1}".replace("'","\"")
    Json.parse(a).objOrElseThrow().filterKeys(p->p.path.last().asKey().name.length()==1)
    //{"a":1,"x":"a","g":1}

    Json.parse(a).objOrElseThrow().filterKeys(p->p.path.last().asKey().name.length()==2)
    // {"cc":[1,2,{"a":1,"bb":"h"}],"ee":{"f":"g","gg":2}}

    Json.parse(a).objOrElseThrow().filterKeys(p->p.elem.isIntegral())
    //{"a":1,"g":1}
    }</pre>

     @param filter the predicate which takes as the input every JsPair in the first level parse this json

     @return same this instance if all the keys satisfy the predicate or a new filtered json parse the same type T

     @see #filterKeys_(Predicate) how to filter the keys parse the whole json and not only the first level
     */
    T filterKeys(final Predicate<JsPair> filter);

    /**
     Filters all the keys parse this json, removing those that don't match the predicate.
     <p>Examples:
     <pre>{@code
    String a = "{'a':1,'x':'a','cc':[1,2,{'a':1,'bb':'h'}],'ee':{'f':'g','gg':2},'g':1}".replace("'","\"")
    Json.parse(a).objOrElseThrow().filterKeys_(p->p.path.last().asKey().name.length()==1)
    //{"a":1,"x":"a","g":1}

    Json.parse(a).objOrElseThrow().filterKeys(p->p.path.last().asKey().name.length()==2)
    {"cc":[1,2,{"bb":"h"}],"ee":{"gg":2}}

    Json.parse(a).objOrElseThrow().filterKeys_(p->p.elem.isStr())
    //{"x":"a"}
    }</pre>

     @param filter the predicate which takes as the input every JsPair of this json

     @return same this instance if all the keys satisfy the predicate or a new filtered json of the same type T

     @see #filterKeys(Predicate) how to filter the keys of only the first level
     */
    T filterKeys_(final Predicate<JsPair> filter);

    /**
     Returns the element located at the key or index specified by the given position or {@link JsNothing} if it doesn't exist.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @param position key or index parse the element
     @return the JsElem located at the given Position or JsNothing if it doesn't exist
     */
    JsElem get(final Position position);

    /**
     Returns the element located at the given path or {@link JsNothing} if it doesn't exist.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the element that will be returned

     @return the JsElem located at the given JsPath or JsNothing if it doesn't exist

     */
    default JsElem get(final JsPath path)
    {
        return Functions.get(this,
                             requireNonNull(path)
                            );
    }

    /**
     Returns the element located at the given path or {@link JsNothing} if it doesn't exist.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the element that will be returned

     @return the JsElem located at the given JsPath or JsNothing if it doesn't exist

     */
    default JsElem get(final String path)
    {
        return get(JsPath.of(path));
    }


    /**
     Returns the array located at the given path or {@link Optional#empty()} if it doesn't exist or it's not an array.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the JsArray that will be returned

     @return the JsArray located at the given JsPath wrapped in an Optional

     */
    default Optional<JsArray> getArray(final JsPath path)
    {
        final Function<JsElem, Optional<JsArray>> ifElse = Functions.ifArrElse(Optional::of,
                                                                               it -> Optional.empty()
                                                                              );
        return ifElse.apply(Functions.get(this,
                                          requireNonNull(path)
                                         ));
    }

    /**
     Returns the array located at the given path as a big decimal or {@link Optional#empty()} if it doesn't exist or it's not an array.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the JsArray that will be returned

     @return the JsArray located at the given path wrapped in an Optional

     */
    default Optional<JsArray> getArray(final String path)
    {
        return getArray(JsPath.of(path));
    }


    /**
     Returns the big decimal located at the given path as a big decimal or {@link Optional#empty()} if it doesn't exist or it's not a decimal number.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the BigDecimal that will be returned

     @return the BigDecimal located at the given JsPath wrapped in an Optional

     */
    default Optional<BigDecimal> getBigDecimal(final JsPath path)
    {
        final Function<JsElem, Optional<BigDecimal>> ifElse = Functions.ifDecimalElse(it -> Optional.of(BigDecimal.valueOf(it)),
                                                                                      Optional::of,
                                                                                      it -> Optional.empty()
                                                                                     );
        return ifElse.apply(Functions.get(this,
                                          requireNonNull(path)
                                         ));
    }

    /**
     Returns the big decimal located at the given path as a big decimal or {@link Optional#empty()} if it doesn't exist or it's not a decimal number.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the BigDecimal that will be returned

     @return the BigDecimal located at the given path wrapped in an Optional

     */
    default Optional<BigDecimal> getBigDecimal(final String path)
    {
        return getBigDecimal(JsPath.of(path));
    }

    /**
     Returns the big integer located at the given path as a big integer or {@link Optional#empty()} if it doesn't exist or it's not an integral number.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the BigInteger that will be returned

     @return the BigInteger located at the given JsPath wrapped in an Optional

     */
    default Optional<BigInteger> getBigInt(final JsPath path)
    {
        final Function<JsElem, Optional<BigInteger>> ifElse = Functions.ifIntegralElse(it -> Optional.of(BigInteger.valueOf(it)),
                                                                                       it -> Optional.of(BigInteger.valueOf(it)),
                                                                                       Optional::of,
                                                                                       $ -> Optional.empty()
                                                                                      );
        return ifElse.apply(Functions.get(this,
                                          requireNonNull(path)
                                         ));
    }

    /**
     Returns the big integer located at the given path as a big integer or {@link Optional#empty()} if it doesn't exist  or it's not an integral number.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the BigInteger that will be returned

     @return the BigInteger located at the given path wrapped in an Optional

     */
    default Optional<BigInteger> getBigInt(final String path)
    {
        return getBigInt(JsPath.of(path));
    }


    /**
     Returns the boolean located at the given path or {@link Optional#empty()} if it doesn't exist.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the Boolean that will be returned

     @return the Boolean located at the given JsPath wrapped in an Optional

     */
    default Optional<Boolean> getBool(final JsPath path)
    {
        final Function<JsElem, Optional<Boolean>> fn = Functions.ifBoolElse(Optional::of,
                                                                            it -> Optional.empty()
                                                                           );
        return fn.apply(Functions.get(this,
                                      requireNonNull(path)
                                     ));
    }

    /**
     Returns the boolean located at the given path or {@link Optional#empty()} if it doesn't exist.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the Boolean that will be returned

     @return the Boolean located at the given JsPath wrapped in an Optional

     */
    default Optional<Boolean> getBool(final String path)
    {
        return getBool(JsPath.of(path));
    }


    /**
     Returns the decimal number located at the given path as a double or {@link OptionalDouble#empty()} if it doesn't exist or it's not a decimal number. If the number is a BigDecimal,
     the conversion is identical to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about the precision parse the BigDecimal
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the double that will be returned

     @return the decimal number located at the given JsPath wrapped in an OptionalDouble

     */
    default OptionalDouble getDouble(final JsPath path)
    {
        return Functions.ifDecimalElse(OptionalDouble::of,
                                       Functions::bigDecimalToDouble,
                                       $ -> OptionalDouble.empty()
                                      )
                        .apply(Functions.get(this,
                                             requireNonNull(path)
                                            ));
    }


    /**
     Returns the decimal number located at the given path as a double or {@link OptionalDouble#empty()} if it doesn't exist or it's not a decimal number. If the number is a BigDecimal,
     the conversion is identical to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about the precision parse the BigDecimal
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the decimal number that will be returned

     @return the decimal number located at the given path wrapped in an OptionalDouble

     */
    default OptionalDouble getDouble(final String path)
    {
        return getDouble(JsPath.of(path));
    }


    /**
     Returns the integral number located at the given path as an integer or {@link OptionalInt#empty()} if it doesn't exist or it's not an integral number
     or it's an integral number but doesn't fit in an integer.
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the integral number that will be returned

     @return the integral number located at the given JsPath wrapped in an OptionalInt

     */
    default OptionalInt getInt(final JsPath path)
    {
        return Functions.ifIntegralElse(OptionalInt::of,
                                        Functions::longToInt,
                                        Functions::bigIntToInt,
                                        $ -> OptionalInt.empty()
                                       )
                        .apply(Functions.get(this,
                                             requireNonNull(path)
                                            ));
    }


    /**
     Returns the integral number located at the given path as an integer or {@link OptionalInt#empty()} if it doesn't exist or it's not an integral number
     or it's an integral number but doesn't fit in an integer.
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the integral number that will be returned

     @return the integral number located at the given path wrapped in an OptionalInt

     */
    default OptionalInt getInt(final String path)
    {
        return getInt(JsPath.of(path));
    }


    /**
     Returns the integral number located at the given path as a long or {@link OptionalLong#empty()} if it doesn't exist or it's not an integral number
     or it's an integral number but doesn't fit in a long.
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the integral number that will be returned

     @return the integral number located at the given JsPath wrapped in an OptionalLong

     */
    default OptionalLong getLong(final JsPath path)
    {
        return Functions.ifIntegralElse(OptionalLong::of,
                                        OptionalLong::of,
                                        Functions::bigIntToLong,
                                        $ -> OptionalLong.empty()
                                       )
                        .apply(Functions.get(this,
                                             requireNonNull(path)
                                            ));

    }

    /**
     Returns the integral number located at the given path as a long or {@link OptionalLong#empty()} if it doesn't exist or it's not an integral number
     or it's an integral number but doesn't fit in a long.
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the integral number that will be returned

     @return the integral number located at the given path wrapped in an OptionalLong

     */
    default OptionalLong getLong(final String path)
    {
        return getLong(JsPath.of(path));
    }


    /**
     Returns the object located at the given path or {@link Optional#empty()} if it doesn't exist or it's not an object.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the JsObj that will be returned

     @return the JsObj located at the given JsPath wrapped in an Optional

     */
    default Optional<JsObj> getObj(final JsPath path)
    {
        final Function<JsElem, Optional<JsObj>> ifElse = Functions.ifObjElse(Optional::of,
                                                                             it -> Optional.empty()
                                                                            );
        return ifElse.apply(Functions.get(this,
                                          requireNonNull(path)
                                         ));
    }

    /**
     Returns the object located at the given path or {@link Optional#empty()} if it doesn't exist or it's not an object.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the JsObj that will be returned

     @return the JsObj located at the given path wrapped in an Optional

     */
    default Optional<JsObj> getObj(final String path)
    {
        return getObj(JsPath.of(path));
    }


    /**
     Returns the string located at the given path or {@link Optional#empty()} if it doesn't exist or it's not an string.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the JsPath object parse the JsStr that will be returned

     @return the JsStr located at the given path wrapped in an Optional

     */
    default Optional<String> getStr(final JsPath path)
    {
        final Function<JsElem, Optional<String>> ifStrElseFn = Functions.ifStrElse(Optional::of,
                                                                                   it -> Optional.empty()
                                                                                  );
        return ifStrElseFn.apply(Functions.get(this,
                                               requireNonNull(path)
                                              ));

    }

    /**
     Returns the string located at the given path or {@link Optional#empty()} if it doesn't exist or it's not an string.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param path the path-like string parse the JsStr that will be returned

     @return the JsStr located at the given path wrapped in an Optional

     */
    default Optional<String> getStr(final String path)
    {
        return getStr(JsPath.of(path));
    }


    /**
     Declarative way parse implementing if(this.isEmpty()) return emptySupplier.get() else return nonEmptySupplier.get()
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param emptySupplier    Supplier that will produce the result if this json is empty
     @param nonemptySupplier Supplier that will produce the result if this json is not empty
     @param <A>      the type parse the result

     @return an object parse type A

     */
    default <A> A ifEmptyElse(Supplier<A> emptySupplier,
                              Supplier<A> nonemptySupplier
                             )
    {

        return this.isEmpty() ? requireNonNull(emptySupplier).get() : requireNonNull(nonemptySupplier).get();
    }


    default long times(JsElem e)
    {
        return stream().filter(p -> p.elem.equals(Objects.requireNonNull(e)))
                       .count();
    }

    default long times_(JsElem e)
    {
        return stream_().filter(p -> p.elem.equals(Objects.requireNonNull(e)))
                        .count();
    }


    default boolean equals(final JsElem elem,
                           final TYPE ARRAY_AS
                          )
    {
        if (elem == null || getClass() != elem.getClass()) return false;
        if (isObj()) return asJsObj().equals(elem.asJsObj(),
                                             ARRAY_AS
                                            );
        if (isArray()) return asJsArray().equals(elem.asJsArray(),
                                                 ARRAY_AS
                                                );
        return false;

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


    /**
     Maps the values in the first level parse this json.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @param fn the mapping function

     @return a new mapped json parse the same type T
     @see #mapObjs(BiFunction) to map jsons
     @see #mapKeys(Function) to map keys parse json objects
     @see #mapElems_(Function) to map all the values and not only the first level
     */
    T mapElems(final Function<JsPair, ? extends JsElem> fn);


    /**
     Maps the values in the first level parse this json that satisfies a given predicate.
     @param fn the mapping function
     @param predicate the given predicate that determines what JsValues will be mapped
     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @return same this instance or a new mapped json parse the same type T


     @see #mapObjs(BiFunction, BiPredicate) to map jsons
     @see #mapKeys(Function, Predicate) to map keys parse json objects
     @see #mapElems_(Function, Predicate) to map all the values and not only the first level
     */
    T mapElems(final Function<JsPair, ? extends JsElem> fn,
               final Predicate<JsPair> predicate
              );

    /**
     Maps all the values parse this json.

     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @param fn the mapping function
     @return a new mapped json parse the same type T

     @see #mapObjs_(BiFunction) to map jsons
     @see #mapKeys_(Function) to map keys parse json objects
     @see #mapElems(Function) to map only the first level
     */
    T mapElems_(final Function<JsPair, ? extends JsElem> fn);


    /**
     Maps all the values parse this json that satisfies a given predicate.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the  mapping function
     @param predicate the given predicate that determines what JsValues will be mapped

     @return same this instance or a new mapped json parse the same type TT

     @see #mapObjs_(BiFunction, BiPredicate) to map jsons
     @see #mapKeys_(Function, Predicate) to map keys parse json objects
     @see #mapElems(Function, Predicate) to map only the first level
     */
    T mapElems_(final Function<JsPair, ? extends JsElem> fn,
                final Predicate<JsPair> predicate
               );

    /**
     Maps the jsons in the first level parse this json that satisfies a given predicate.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the mapping function
     @param predicate the given predicate that determines what Jsons will be mapped

     @return same this instance or a new mapped json parse the same type T

     @see #mapElems(Function, Predicate) to map values
     @see #mapKeys(Function, Predicate) to map keys parse json objects
     @see #mapObjs_(BiFunction, BiPredicate) to map all the jsons and not only the first level
     */
    T mapObjs(final BiFunction<JsPath, JsObj, JsObj> fn,
              final BiPredicate<JsPath, JsObj> predicate
             );

    /**
     Maps the jsons in the first level parse this json.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @param fn the  mapping function

     @return a new mapped json parse the same type T

     @see #mapElems(Function) to map values
     @see #mapKeys(Function) to map keys parse json objects
     @see #mapObjs_(BiFunction) to map all the jsons and not only the first level
     */
    T mapObjs(final BiFunction<JsPath, JsObj, JsObj> fn
             );

    /**
     Maps all the jsons parse this json that satisfies a given predicate.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the  mapping function
     @param predicate the given predicate that determines what Jsons will be mapped

     @return same this instance or a new mapped json parse the same type T

     @see #mapElems_(Function, Predicate) to map values
     @see #mapKeys_(Function, Predicate) to map keys parse json objects
     @see #mapObjs(BiFunction, BiPredicate) to map only the first level
     */
    T mapObjs_(final BiFunction<JsPath, JsObj, JsObj> fn,
               final BiPredicate<JsPath, JsObj> predicate
              );

    /**
     Maps all the jsons parse this json.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the mapping function
     @return a new mapped json parse the same type T

     @see #mapElems_(Function) to map values
     @see #mapKeys_(Function) to map keys parse json objects
     @see #mapObjs(BiFunction) to map only the first level
     */
    T mapObjs_(final BiFunction<JsPath, JsObj, JsObj> fn
              );

    /**
     Maps the keys in the first level parse this json.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>
     @param fn the mapping function

     @return a new mapped json parse the same type T

     @see #mapElems(Function) to map values
     @see #mapObjs(BiFunction) to map jsons
     @see #mapKeys_(Function) to map all the keys and not only the first level
     */
    T mapKeys(final Function<JsPair, String> fn);

    /**
     Maps the keys in the first level parse this json that satisfies a given predicate.

     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the mapping function
     @param predicate the given predicate that determines what keys will be mapped

     @return same this instance or a new mapped json parse the same type T

     @see #mapObjs(BiFunction, BiPredicate) to map jsons
     @see #mapElems(Function, Predicate) to map values
     @see #mapKeys_(Function, Predicate) to map all the keys and not only the first level
     */
    T mapKeys(final Function<JsPair, String> fn,
              final Predicate<JsPair> predicate
             );

    /**
     Maps all the keys parse this json.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the mapping function
     @return a new mapped json parse the same type T

     @see #mapElems_(Function) to map values
     @see #mapObjs_(BiFunction) to map jsons
     @see #mapKeys(Function) to map only the first level
     */
    T mapKeys_(final Function<JsPair, String> fn);

    /**
     Maps all the keys parse this json that satisfies a given predicate.
     <p>
     Examples:
     <pre>
     {@code }
     </pre>

     @param fn the mapping function
     @param predicate the given predicate that determines what keys will be mapped

     @return same this instance or a new mapped json parse the same type T

     @see #mapElems_(Function, Predicate) to map values
     @see #mapObjs_(BiFunction, BiPredicate) to map jsons
     @see #mapKeys(Function, Predicate) to map only the first level
     */
    T mapKeys_(final Function<JsPair, String> fn,
               final Predicate<JsPair> predicate
              );

    /**
     If the given path is not already associated with a value or is associated with null, associates it with the given value. Otherwise,
     replaces the associated value with the results parse the given remapping function. This method may be parse use when combining multiple mapped
     values for a key.For example, to either create or append a String msg to a value mapping:
     {@code

     map.merge(key, msg, String::concat)
     }

     @param path the given JsPath object which the resulting value is to be associated
     @param value the given value to be merged with the existing value associated with the path or, if no existing value or a null value is
     associated with the path, to be associated with the path
     @param fn the given function to recompute a value if present
     @return a new json parse the same type T
     */
    default T merge(final JsPath path,
                    final JsElem value,
                    final BiFunction<? super JsElem, ? super JsElem, ? extends JsElem> fn
                   )
    {
        requireNonNull(fn);
        requireNonNull(value);
        return put(requireNonNull(path),
                   elem ->
                   {
                       if (elem.isNothing() || elem.isNull()) return value;
                       return fn.apply(value,
                                       elem
                                      );
                   }
                  );
    }

    /**
     If the given path is not already associated with a value or is associated with null, associates it with the given value. Otherwise,
     replaces the associated value with the results parse the given remapping function. This method may be parse use when combining multiple mapped
     values for a key.For example, to either create or append a String msg to a value mapping:
     {@code

     map.merge(key, msg, String::concat)
     }

     @param path the given path-like string which the resulting value is to be associated
     @param value the given value to be merged with the existing value associated with the path or, if no existing value or a null value is
     associated with the path, to be associated with the path
     @param fn the given function to recompute a value if present
     @return a new json parse the same type T
     */
    default T merge(final String path,
                    final JsElem value,
                    final BiFunction<? super JsElem, ? super JsElem, ? extends JsElem> fn
                   )
    {
        return merge(JsPath.of(path),
                     value,
                     fn
                    );
    }

    /**
     Tries to parse the string into an immutable json.
     <p>
     Examples:
     <pre>{@code
    Json.parse("{1}").isFailure() == true

    Json.parse("{1}").objOrElse(()-> JsObj._empty_()).equals(JsObj._empty_())

    Json.parse("{1}").objOrElseThrow()  //jsonvalues.MalformedJson thrown: Json malformed: Invalid token=NUMBER at
    //(line no=1, column no=2, offset=1). Expected tokens are: [STRING]

    Json.parse("{ 'a': 1 }".replaceAll("'","\"")).objOrElse(()-> JsObj.empty()).equals(JsObj._parse_("a", JsInt.parse(1)))

    // java.lang.IllegalArgumentException thrown: Received an array: []
    Json.parse("[]").objOrElseThrow((arr,ex)->{
    if(ex!=null) return ex;
    else return new IllegalArgumentException("Received an array: "+arr);
    }
    )

    Json.parse("[1,2]").arrOrElseThrow().equals(JsArray._parse_(JsInt.parse(1),JsInt.parse(2)))
    }</pre>

     @param str the string that will be parsed

     @return a {@link Try} computation
     */
    static Try parse(String str)
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();
            if (event == START_ARRAY) return new Try(new JsArrayImmutableImpl(Functions.parse(MyScalaImpl.Vector.EMPTY,
                                                                                              parser
                                                                                             )
            ));
            return new Try(new JsObjImmutableImpl(Functions.parse(MyScalaImpl.Map.EMPTY,
                                                                  parser

                                                                 )));
        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }


    }

    /**
     Tries to parse the string into an immutable json, performing some operations while the parsing.
     <p>
     It's faster to do certain operations right while the parsing, instead parse parsing the string into a json and apply them later.

     Examples:
     <pre>{@code

    //removes null and converts to uppercase keys
    String a = "{'a': {'x':1},'x':null,'c':' hi ','d':[2, ' bye ',{ 'e':' hi ','f': null }] }".replaceAll("'","\"")
    Json.parse(a,Options.builder().filter(pair->pair.elem.isNotNull()).keyMap(key-> key.toUpperCase()))
    .objOrElseThrow()
    //the result is {"A":{"B":1},"C":" hi ","D":[2, " bye ",{"E":" hi "}]}

    //trims and converts to uppercase string elements
    Json.parse(a,Options.builder().map(pair-> pair.elem.mapIfStr(s->s.trim().toUpperCase()))).objOrElseThrow()
    //the result is {"a":{"x":1},"x":null,"c":"HI","d":[2, "BYE",{"e":"HI","f":null}]}

    }</pre>

     @param str     the string that will be parsed
     @param options a Options with the filters and maps that will be applied during the parsing

     @return a {@link Try} computation
     */
    static Try parse(String str,
                     ParseOptions options
                    )
    {
        try (JsParser parser = new JsParser(new StringReader(requireNonNull(str))))
        {

            final JsParser.Event event = parser.next();
            if (event == START_ARRAY) return new Try(new JsArrayImmutableImpl(Functions.parse(MyScalaImpl.Vector.EMPTY,
                                                                                              parser,
                                                                                              options.create(),
                                                                                              MINUS_ONE_INDEX

                                                                                             )));
            return new Try(new JsObjImmutableImpl(Functions.parse(MyScalaImpl.Map.EMPTY,
                                                                  parser,
                                                                  options.create(),
                                                                  JsPath.empty()

                                                                 )));
        }

        catch (MalformedJson e)
        {

            return new Try(e);

        }


    }

    /**
     Inserts the element returned by the function at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head of the path is an index and this is an object. In both cases the function is not invoked.
     The same instance is returned as well when the element returned by the function is {@link JsNothing}
     Examples:
     <pre>
     {@code




     }
     </pre>

     @param path    the JsPath object where the JsElem will be inserted at
     @param fn the function that takes as an input the JsElem at the path and produces the JsElem to be inserted at the path

     @return the same instance or a new json parse the same type T
     */
    T put(final JsPath path,
          final Function<? super JsElem, ? extends JsElem> fn
         );


    /**
     Inserts the element returned by the function at the path in this json, replacing any existing
     element in the path and filling with {@link jsonvalues.JsNull} empty indexes in arrays when
     necessary. The same instance is returned if the  path doesn't exist. In both cases the function
     is not invoked. The same instance is returned when the element returned by the function is
     {@link JsNothing}
     @param path    the path-like string where the JsElem will be inserted at
     @param fn the function that takes as an input the JsElem at the path and produces the JsElem to be inserted at the path

     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final Function<? super JsElem, ? extends JsElem> fn
                 )
    {
        return put(JsPath.of(path),
                   fn
                  );
    }

    /**
     Inserts the element at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head of the path is an index and this is an object or the element is {@link JsNothing}
     Examples:
     <pre>
     {@code




     }
     </pre>

     @param path    the JsPath object where the element will be inserted at
     @param element the JsElem that will be inserted

     @return the same instance or a new json of the same type T
     */
    default T put(final JsPath path,
                  final JsElem element
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
                   $ -> element
                  );
    }

    /**
     Inserts the element at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head of the path is an index and this is an object or the element is {@link JsNothing}
     Examples:
     <pre>
     {@code




     }
     </pre>

     @param path    the path-like string where the element will be inserted at
     @param element the JsElem that will be inserted

     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final JsElem element
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   element
                  );
    }

    /**
     Inserts the integer number at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head of the path is an index and this is an object or the element is {@link JsNothing}
     Examples:
     <pre>
     {@code




     }
     </pre>

     @param path    the path-like string where the integer number will be inserted at
     @param n the integer that will be inserted

     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final int n
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   JsInt.of(n)
                  );
    }


    /**
     Inserts the long number at the path in this json, replacing any existing element and filling with {@link jsonvalues.JsNull} empty indexes in arrays when necessary.
     <p>
     The same instance is returned when the head of the path is a key and this is an array or the head of the path is an index and this is an object or the element is {@link JsNothing}
     Examples:
     <pre>
     {@code




     }
     </pre>

     @param path    the path-like string where the long number will be inserted at
     @param n the long number that will be inserted

     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final long n
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   JsLong.of(n)
                  );
    }


    /**
     Inserts the string at the given path in this json, replacing any existing element in the path
     and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path    the path-like string where the string will be inserted at
     @param str the string that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final String str
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   JsStr.of(str)
                  );
    }


    /**
     Inserts the big integer number at the given path in this json, replacing any existing element
     in teh path and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path    the given path-like string where the big integer number will be inserted at
     @param bigint the big integer number that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final BigInteger bigint
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   JsBigInt.of(bigint)
                  );
    }

    /**
     Inserts the big decimal number at the given path in this json, replacing any existing element in
     the path and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path    the given path-like string where the big decimal number will be inserted at
     @param bigdecimal the big decimal number that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final BigDecimal bigdecimal
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
                   JsBigDec.of(bigdecimal)
                  );
    }

    /**
     Inserts the boolean at the given path in this json, replacing any existing element in the path
     and filling with {@link jsonvalues.JsNull} empty positions in arrays when necessary.
     @param path  the given path-like string where the boolean will be inserted at
     @param bool the boolean that will be inserted
     @return the same instance or a new json of the same type T
     */
    default T put(final String path,
                  final boolean bool
                 )
    {
        return put(JsPath.of(requireNonNull(path)),
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
    default T putIf(final Predicate<? super JsElem> predicate,
                    final JsPath path,
                    final Function<? super JsElem, ? extends JsElem> fn
                   )
    {
        final JsElem elem = get(requireNonNull(path));
        if (requireNonNull(predicate).test(elem)) return put(path,
                                                             requireNonNull(fn).apply(elem)
                                                            );

        //this is an instance of T (recursive type));
        @SuppressWarnings("unchecked") final T t = (T) this;
        return t;

    }

    /**
     Inserts at the given path in this json, if the existing element satisfies the predicate, a new
     element returned by the function.
     If the predicate evaluates to false, the function is not computed. If the function returns {@link JsNothing},
     the same this instance is returned.
     @param predicate the predicate on which the existing element is tested on
     @param path      the path-like string
     @param fn        the function witch computes the new element if the existing satisfies the given predicate

     @return the same instance or a new json of the same type T
     */
    default T putIf(final Predicate<? super JsElem> predicate,
                    final String path,
                    final Function<? super JsElem, ? extends JsElem> fn
                   )
    {
        return putIf(predicate,
                     JsPath.of(path),
                     fn
                    );
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
                          final Supplier<? extends JsElem> supplier
                         )
    {
        requireNonNull(supplier);
        return putIf(JsElem::isNothing,
                     requireNonNull(path),
                     $ -> supplier.get()
                    );
    }

    /**
     Inserts at the given path in this json, if no element is present, the element returned by the
     supplier, replacing any existing element in the path and filling with {@link jsonvalues.JsNull}
     empty positions in arrays when necessary. The supplier is not invoked if the element is present.
     @param path  the path-like string
     @param supplier the supplier which computes the new JsElem if absent
     @return the same instance or a new json of the same type T
     */
    default T putIfAbsent(final String path,
                          final Supplier<? extends JsElem> supplier
                         )
    {
        return putIfAbsent(JsPath.of(path),
                           supplier
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
                           final Function<? super JsElem, ? extends JsElem> fn
                          )
    {
        return putIf(JsElem::isNotNothing,
                     requireNonNull(path),
                     requireNonNull(fn)

                    );
    }

    /**
     Inserts at the given path in this json, if some element is present, the element returned by the
     function.
     @param path the given path-like string
     @param fn the function which computes the new JsElem from the existing one
     @return the same instance or a new json of the same type T
     */
    default T putIfPresent(final String path,
                           final Function<? super JsElem, ? extends JsElem> fn
                          )
    {
        return putIfPresent(JsPath.of(path),
                            fn
                           );
    }


    /**
     Performs a reduction on the values that satisfy the predicate in the first level of this json. The reduction is performed mapping
     each value with the mapping function and then applying the operator
     @param op the operator upon two objects of type R
     @param map the mapping function which produces an object of type R from a JsValue
     @param predicate the predicate that determines what JsValue will be mapped and reduced
     @param <R> the type of the operands of the operator
     @return an {@link Optional} describing the of parse the reduction
     @see #reduce_(BinaryOperator, Function, Predicate) to apply the reduction in all the Json and not only in the first level
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
    <R> Optional<R> reduce_(BinaryOperator<R> op,
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
     Removes the element in this json located at the given path, if it exists, returning the same this
     instance otherwise
     @param path the given path-like string
     @return a json of the same type T
     */
    default T remove(final String path)
    {
        return remove(JsPath.of(path));
    }

    /**
     Returns the number of elements in the first level of this json
     @return the number of elements in the first level of this json
     */
    int size();

    /**
     Returns the number of all the elements in this json
     @return the number of all the elements in this json
     */
    default int size_()
    {
        return stream_().mapToInt(p -> 1)
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
    default OptionalInt size(final JsPath path)
    {

        return Functions.ifJsonElse(it -> OptionalInt.of(it.size()),
                                    it -> OptionalInt.empty()
                                   )
                        .apply(get(requireNonNull(path)));


    }

    /**
     Returns the size of the json located at the given path in this json or OptionalInt.empty() if it
     doesn't exist or it's not a Json
     @param path the given JsPath object
     @return an OptionalInt
     */
    default OptionalInt size_(final JsPath path)
    {

        return Functions.ifJsonElse(it -> OptionalInt.of(it.size_()),
                                    it -> OptionalInt.empty()
                                   )
                        .apply(get(requireNonNull(path)));


    }

    /**
     Returns the size of the json located at the given path in this json or OptionalInt.empty() if it
     doesn't exist or it's not a Json
     @param path the given path-like string
     @return an OptionalInt
     */
    default OptionalInt size(final String path)
    {
        return size(JsPath.of(path));
    }

    /**
     Returns the size of the Json located at the given path in this json or OptionalInt.empty() if it
     doesn't exist or it's not a Json
     @param path the given path-like string
     @return an OptionalInt
     */
    default OptionalInt size_(final String path)
    {

        return size_(JsPath.of(path));


    }

    /**
     Returns a stream over all the pairs of elements in this json object.
     @return a {@code Stream} over all the JsPairs in this json
     */
    Stream<JsPair> stream_();

    /**
     Returns a stream over the pairs of elements in the first level of this json object.
     @return a {@code Stream} over all the JsPairs in the first level of this json
     */
    Stream<JsPair> stream();


    /**
     Converts this json into immutable if it's mutable, returning this same instance otherwise.
     @return an immutable Json
     */
    T toImmutable();

    /**
     Converts this json into mutable if it's immutable, returning this same instance otherwise.
     @return an mutable Json
     */
    T toMutable();

    /**
     Returns true if an element exists in this json at the given path.
     @param path the given path-like string
     @return true if a JsElem exists at the path
     */
    default boolean containsPath(final String path)
    {
        return containsPath(JsPath.of(requireNonNull(path)));
    }

    /**
     Returns true if an element exists in this json at the given path.
     @param path the JsPath
     @return true if a JsElem exists at the JsPath
     */
    default boolean containsPath(final JsPath path)
    {
        return get(requireNonNull(path)).isNotNothing();

    }

    /**
     Returns true if this json contains the given element in the first level.
     @param element the give element JsElem whose presence in this JsArray is to be tested
     @return true if this JsArray contains the  JsElem
     */
    boolean containsElem(JsElem element);

    /**
     Returns true if this json or any of its elements, contains the given element.
     @param element the give JsElem whose presence in this JsArray is to be tested
     @return true if this JsArray contains the JsElem
     */
    default boolean containsElem_(final JsElem element)
    {
        return stream_().anyMatch(p -> p.elem.equals(Objects.requireNonNull(element)));
    }


    default T map(Function<T, T> fn)
    {
        //this is an instance of T (recursive type)
        @SuppressWarnings("unchecked") T o = fn.apply((T) this);
        return o;
    }

    /**
     @return true if the implementation is mutable
     */
    boolean isMutable();

    /**
     @return  true if the implementation is immutable
     */
    boolean isImmutable();


}