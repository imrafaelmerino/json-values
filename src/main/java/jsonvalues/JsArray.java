package jsonvalues;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import io.vavr.collection.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.JsObj.streamOfObj;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Represents a json array, which is an ordered list of elements.
 */
public class JsArray implements Json<JsArray>, Iterable<JsValue>


{
    public static JsArray EMPTY = new JsArray(Vector.empty());
    public static final int ID = 4;
    private volatile int hashcode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recommendation from Effective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;
    private final Vector<JsValue> seq;

    JsArray(Vector<JsValue> seq)
    {
        this.seq = seq;
    }

    /**
     Adds one or more elements, starting from the first, to the back of this array.
     @param e   the JsElem to be added to the back.
     @param others more optional JsElem to be added to the back
     @return a new JsArray
     */
    public final JsArray append(final JsValue e,
                                final JsValue... others
                               )
    {
        Vector<JsValue> acc = this.seq.append(requireNonNull(e));
        for (JsValue other : requireNonNull(others)) acc = acc.append(requireNonNull(other));
        return new JsArray(acc);
    }

    public final JsArray append(final JsPath path,
                                final JsValue elem
                               )
    {
        if (requireNonNull(path).isEmpty()) return this;
        Objects.requireNonNull(elem);
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsArray(seq.update(index,
                                                                                                             arr.append(elem)
                                                                                                            )),
                                                                               e -> new JsArray(nullPadding(index,
                                                                                                            seq,
                                                                                                            JsArray.EMPTY
                                                                                                            .append(elem)
                                                                                                           ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> new JsArray(nullPadding(index,
                                                                                                               seq,
                                                                                                               tail.head()
                                                                                                                   .match(o -> JsObj.EMPTY
                                                                                                                          .append(tail,
                                                                                                                                  elem
                                                                                                                                 ),
                                                                                                                          a -> JsArray.EMPTY
                                                                                                                          .append(tail,
                                                                                                                                  elem
                                                                                                                                 )
                                                                                                                         )
                                                                                                              )),
                                                                                 () -> new JsArray(seq.update(index,
                                                                                                              seq.get(index)
                                                                                                                 .toJson()
                                                                                                                 .append(tail,
                                                                                                                         elem
                                                                                                                        )
                                                                                                             ))
                                                                                )


                                                     );
                          }

                         );

    }

    /**
     Adds all the elements of the given array, starting from the head, to the back of this array.
     @param array the JsArray of elements to be added to the back
     @return a new JsArray
     */
    public final JsArray appendAll(final JsArray array
                                  )
    {
        return appendAllBackTrampoline(this,
                                       requireNonNull(array)
                                      ).get();


    }

    @SuppressWarnings("Duplicates")

    public final JsArray appendAll(final JsPath path,
                                   final JsArray elems

                                  )
    {

        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsArray(seq.update(index,
                                                                                                                             arr.appendAll(elems)
                                                                                                                            )),
                                                                                               e -> new JsArray(nullPadding(index,
                                                                                                                            seq,
                                                                                                                            JsArray.EMPTY
                                                                                                                            .appendAll(elems)
                                                                                                                           ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> new JsArray(nullPadding(index,
                                                                                                                               seq,
                                                                                                                               tail.head()
                                                                                                                                   .match(o -> JsObj.EMPTY
                                                                                                                                          .appendAll(tail,
                                                                                                                                                     elems
                                                                                                                                                    ),
                                                                                                                                          a -> JsArray.EMPTY
                                                                                                                                          .appendAll(tail,
                                                                                                                                                     elems
                                                                                                                                                    )
                                                                                                                                         )
                                                                                                                              )),
                                                                                                 () -> new JsArray(seq.update(index,
                                                                                                                              seq.get(index)
                                                                                                                                 .toJson()
                                                                                                                                 .appendAll(tail,
                                                                                                                                            elems
                                                                                                                                           )
                                                                                                                             ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    protected Trampoline<JsArray> appendAllBackTrampoline(final JsArray arr1,
                                                          final JsArray arr2
                                                         )
    {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return Trampoline.done(arr1);
        if (arr1.isEmpty()) return Trampoline.done(arr2);
        return Trampoline.more(() -> appendAllBackTrampoline(arr1.append(arr2.head()),
                                                             arr2.tail()
                                                            ));
    }

    protected Trampoline<JsArray> appendAllFrontTrampoline(final JsArray arr1,
                                                           final JsArray arr2
                                                          )
    {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return Trampoline.done(arr1);
        if (arr1.isEmpty()) return Trampoline.done(arr2);
        return Trampoline.more(() -> appendAllFrontTrampoline(arr1.prepend(arr2.last()),
                                                              arr2.init()
                                                             ));
    }

    public final boolean containsValue(final JsValue el)
    {
        return seq.contains(requireNonNull(el));
    }

    public static JsArray empty()
    {
        return EMPTY;
    }

    /**
     Returns true if this array is equal to the given as a parameter. In the case of ARRAY_AS=LIST,
     this method is equivalent to JsArray.equals(Object).
     @param array the given array
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return true if both arrays are equals according to ARRAY_AS parameter
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public boolean equals(final JsArray array,
                          final TYPE ARRAY_AS
                         )
    {
        if (ARRAY_AS == LIST) return this.equals(array);
        if (isEmpty()) return array.isEmpty();
        if (array.isEmpty()) return false;
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(i -> get(Index.of(i)))
                        .allMatch(elem ->
                                  {
                                      if (!array.containsValue(elem)) return false;
                                      if (ARRAY_AS == MULTISET) return times(elem) == array.times(elem);
                                      return true;
                                  }) && IntStream.range(0,
                                                        array.size()
                                                       )
                                                 .mapToObj(i -> array.get(Index.of(i)))
                                                 .allMatch(this::containsValue);
    }

    public final boolean equals(final @Nullable Object that)
    {
        if (!(that instanceof JsArray)) return false;
        if (this == that) return true;
        final Vector<JsValue> thatSeq = ((JsArray) that).seq;
        final boolean thatEmpty = thatSeq.isEmpty();
        final boolean thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (this.size() != thatSeq.size()) return false;
        return yContainsX(seq,
                          thatSeq
                         ) && yContainsX(thatSeq,
                                         seq
                                        );

    }

    public final JsArray filterAllValues(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrElems(this).filterAll(JsPath.empty()
                                                          .index(-1),
                                                    requireNonNull(filter)
                                                   )

                                         .get();
    }

    public final JsArray filterAllKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrKeys(this).filterAll(JsPath.empty()
                                                         .index(-1),
                                                   filter
                                                  )
                                        .get();
    }

    public final JsArray filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterArrObjs(this).filterAll(JsPath.empty()
                                                         .index(-1),
                                                   requireNonNull(filter)
                                                  )
                                        .get();
    }

    public final JsArray filterValues(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrElems(this).filter(JsPath.empty()
                                                       .index(-1),
                                                 requireNonNull(filter)
                                                )

                                         .get();
    }

    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterArrObjs(this).filter(JsPath.empty()
                                                      .index(-1),
                                                requireNonNull(filter)
                                               )

                                        .get();
    }

    public final JsValue get(final Position pos)
    {


        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                         {
                                             if (index == -1 && !seq.isEmpty()) return seq.last();
                                             return (seq.isEmpty() || index < 0 || index > seq.size() - 1) ?
                                             JsNothing.NOTHING : seq.get(index);
                                         }
                                        );


    }


  public JsValue get(final int i)
    {
        return seq.get(i);
    }

    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @SuppressWarnings("squid:S1206")

    public final int hashCode()
    {
        int result = hashcode;
        if (result == 0)
            hashcode = result = seq.hashCode();
        return result;
    }

    /**
     Returns the first element of this array.
     @return the first JsElem of this JsArray
     @throws UserError if this JsArray is empty
     */
    public final JsValue head()
    {
        return seq.head();
    }

    public <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
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

    /**
     Returns all the elements of this array except the last one.
     @return JsArray with all the JsElem except the last one
     @throws UserError if this JsArray is empty
     */
    public final JsArray init()
    {
        return new JsArray(seq.init());
    }

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
    public final JsArray intersection(final JsArray that,
                                      final TYPE ARRAY_AS
                                     )
    {
        return intersection(this,
                            requireNonNull(that),
                            requireNonNull(ARRAY_AS)
                           ).get();
    }

    /**
     {@code this.intersectionAll(that)} behaves as {@code this.intersection(that, LIST)}, but for those
     elements that are containers of the same type and are located at the same position, the result
     is their intersection. So this operation is kind of a recursive intersection
     @param that the other array
     @return a JsArray of the same type as the inputs (mutable or immutable)
     */
    public JsArray intersectionAll(final JsArray that)
    {
        return intersectionAll(this,
                               requireNonNull(that)
                              ).get();
    }

    @Override
    public int id()
    {
        return ID;
    }

    public boolean isArray()
    {
        return true;
    }

    public final boolean isEmpty()
    {
        return seq.isEmpty();
    }

    public final Iterator<JsValue> iterator()
    {
        return seq.iterator();
    }

    /**
     Returns the last element of this array.
     @return the last JsElem of this JsArray
     @throws UserError if this JsArray is empty
     */
    public final JsValue last()
    {

        return seq.last();
    }

    public JsArray mapAllValues(final Function<? super JsPair, ? extends JsValue> fn)
    {
        return new OpMapArrElems(this).mapAll(requireNonNull(fn),
                                              p -> true,
                                              JsPath.empty()
                                                    .index(-1)
                                             )
                                      .get();
    }

    public JsArray mapAllValues(final Function<? super JsPair, ? extends JsValue> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapArrElems(this).mapAll(requireNonNull(fn),
                                              requireNonNull(predicate),
                                              JsPath.empty()
                                                    .index(-1)
                                             )
                                      .get();
    }

    public final JsArray mapAllKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapArrKeys(this).mapAll(requireNonNull(fn),
                                             it -> true,
                                             JsPath.empty()
                                                   .index(-1)
                                            )
                                     .get();
    }

    public final JsArray mapAllKeys(final Function<? super JsPair, String> fn,
                                    final Predicate<? super JsPair> predicate
                                   )
    {
        return new OpMapArrKeys(this).mapAll(requireNonNull(fn),
                                             requireNonNull(predicate),
                                             JsPath.empty()
                                                   .index(-1)
                                            )
                                     .get();

    }

    public final JsArray mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                    final BiPredicate<? super JsPath, ? super JsObj> predicate
                                   )
    {
        return new OpMapArrObjs(this).mapAll(requireNonNull(fn),
                                             requireNonNull(predicate),
                                             JsPath.empty()
                                                   .index(-1)
                                            )
                                     .get();
    }

    public final JsArray mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapArrObjs(this).mapAll(requireNonNull(fn),
                                             (p, o) -> true,
                                             JsPath.empty()
                                                   .index(-1)
                                            )
                                     .get();
    }

    public final JsArray mapValues(final Function<? super JsPair, ? extends JsValue> fn)
    {
        return new OpMapArrElems(this).map(requireNonNull(fn),
                                           p -> true,
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                      .get();

    }

    public JsArray mapValues(final Function<? super JsPair, ? extends JsValue> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapArrElems(this).map(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                      .get();
    }

    public final JsArray mapKeys(final Function<? super JsPair, String> fn)
    {
        return this;
    }

    public final JsArray mapKeys(final Function<? super JsPair, String> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return this;
    }

    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return new OpMapArrObjs(this).map(requireNonNull(fn),
                                          requireNonNull(predicate),
                                          JsPath.empty()
                                                .index(-1)
                                         )
                                     .get();

    }

    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapArrObjs(this).map(requireNonNull(fn),
                                          (p, o) -> true,
                                          JsPath.empty()
                                                .index(-1)
                                         )
                                     .get();
    }

    public static JsArray of(JsValue e)
    {

        return JsArray.EMPTY.append(e);
    }

    /**
     Returns an immutable array from one or more pairs.
     @param pair a pair
     @param others more optional pairs
     @return an immutable JsArray
     @throws UserError if an elem of a pair is mutable

     */
    public static JsArray of(final JsPair pair,
                             final JsPair... others
                            )
    {
        JsArray arr = JsArray.EMPTY.put(pair.path,
                                        pair.value
                                       );
        for (JsPair p : others)
        {

            arr = arr.put(p.path,
                          p.value
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
    public static JsArray of(final JsValue e,
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
    public static JsArray of(final JsValue e,
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
    public static JsArray of(final JsValue e,
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
    public static JsArray of(final JsValue e,
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
    public static JsArray of(final JsValue e,
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
     Returns an immutable array from one or more strings.
     @param str a string
     @param others more optional strings
     @return an immutable JsArray
     */
    public static JsArray of(String str,
                             String... others
                            )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsStr.of(str));
        for (String a : others)
        {
            vector = vector.append(JsStr.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more integers.
     @param number an integer
     @param others more optional integers
     @return an immutable JsArray
     */
    public static JsArray of(int number,
                             int... others
                            )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsInt.of(number));
        for (int a : others)
        {
            vector = vector.append(JsInt.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more booleans.
     @param bool an boolean
     @param others more optional booleans
     @return an immutable JsArray
     */
    public static JsArray of(final boolean bool,
                             final boolean... others
                            )
    {
        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBool.of(bool));
        for (boolean a : others)
        {
            vector = vector.append(JsBool.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     Returns an immutable array from one or more longs.
     @param number a long
     @param others more optional longs
     @return an immutable JsArray
     */
    public static JsArray of(final long number,
                             final long... others
                            )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsLong.of(number));
        for (long a : others)
        {
            vector = vector.append(JsLong.of(a));
        }
        return new JsArray(vector

        );
    }

    /**
     Returns an immutable array from one or more doubles.
     @param number a double
     @param others more optional doubles
     @return an immutable JsArray
     */
    public static JsArray of(final double number,
                             final double... others
                            )
    {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsDouble.of(number));
        for (double a : others)
        {
            vector = vector.append(JsDouble.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     returns an immutable json array from an iterable of json elements
     @param iterable the iterable of json elements
     @return an immutable json array
     */
    public static JsArray ofIterable(Iterable<? extends JsValue> iterable)
    {
        Vector<JsValue> vector = Vector.empty();
        for (JsValue e : requireNonNull(iterable))
        {

            vector = vector.append(e);
        }
        return new JsArray(vector

        );
    }

    /**
     Tries to parse the string into an immutable json array.
     @param str the string to be parsed
     @return a JsArray
     @throws MalformedJson if the string doesnt represent a json array
     */
    public static JsArray parse(final String str) throws MalformedJson
    {

        try (JsonParser parser = JsonLibsFactory.jackson.createParser(requireNonNull(str)))
        {
            final JsonToken keyEvent = parser.nextToken();
            if (START_ARRAY != keyEvent) throw MalformedJson.expectedArray(str);
            return new JsArray(parse(parser
                                    ));
        }
        catch (IOException e)
        {

            throw new MalformedJson(e.getMessage());
        }

    }

    public static JsArray parse(final String str,
                                final ParseBuilder builder
                               ) throws MalformedJson
    {

        try (JsonParser parser = JsonLibsFactory.jackson.createParser(requireNonNull(str)))
        {
            final JsonToken keyEvent = parser.nextToken();
            if (START_ARRAY != keyEvent) throw MalformedJson.expectedArray(str);
            return new JsArray(parse(parser,
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

    static Vector<JsValue> parse(final JsonParser parser
                                ) throws IOException
    {
        Vector<JsValue> root = Vector.empty();
        while (true)
        {
            JsonToken token = parser.nextToken();
            JsValue elem;
            switch (token.id())
            {
                case JsonTokenId.ID_END_ARRAY:
                    return root;
                case JsonTokenId.ID_START_OBJECT:
                    elem = new JsObj(JsObj.parse(parser)
                    );
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new JsArray(parse(parser
                                            )
                    );
                    break;
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                default:
                    throw InternalError.tokenNotExpected(token.name());
            }
            root = root.append(elem);
        }
    }

    static Vector<JsValue> parse(final JsonParser parser,
                                 final ParseBuilder.Options options,
                                 final JsPath path
                                ) throws IOException
    {
        JsonToken elem;
        JsPair pair;
        Vector<JsValue> root = Vector.empty();
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.nextToken()) != JsonToken.END_ARRAY)
        {
            final JsPath currentPath = path.inc();
            switch (elem.id())
            {
                case JsonTokenId.ID_STRING:

                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_NUMBER_INT:

                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:

                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;

                    break;
                case JsonTokenId.ID_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;
                    break;
                case JsonTokenId.ID_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    root = condition.test(pair) ? root.append(options.elemMap.apply(pair)) : root;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        root = root.append(new JsObj(JsObj.parse(parser,
                                                                                  options,
                                                                                  currentPath
                                                                                 )
                                           )
                                          );
                    }
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        root = root.append(new JsArray(parse(parser,
                                                             options,
                                                             currentPath.index(-1)
                                                            )
                                           )
                                          );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }
        }
        return root;
    }

    /**
     Adds one or more elements, starting from the last, to the front of this array.
     @param e   the JsElem to be added to the front.
     @param others more optional JsElem to be added to the front
     @return a new JsArray
     */
    public final JsArray prepend(final JsValue e,
                                 final JsValue... others
                                )
    {
        Vector<JsValue> acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsValue other = others[othersLength - 1 - i];
            acc = acc.prepend(requireNonNull(other));
        }
        return new JsArray(acc.prepend(requireNonNull(e)));
    }

    @SuppressWarnings("Duplicates")

    public final JsArray prepend(final JsPath path,
                                 final JsValue elem
                                )
    {
        Objects.requireNonNull(elem);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsArray(seq.update(index,
                                                                                                             arr.prepend(elem)
                                                                                                            )),
                                                                               e -> new JsArray(nullPadding(index,
                                                                                                            seq,
                                                                                                            JsArray.EMPTY
                                                                                                            .prepend(elem)
                                                                                                           ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> new JsArray(nullPadding(index,
                                                                                                               seq,
                                                                                                               tail.head()
                                                                                                                   .match(o -> JsObj.EMPTY
                                                                                                                          .prepend(tail,
                                                                                                                                   elem
                                                                                                                                  ),
                                                                                                                          a -> JsArray.EMPTY
                                                                                                                          .prepend(tail,
                                                                                                                                   elem
                                                                                                                                  )
                                                                                                                         )

                                                                                                              )),
                                                                                 () -> new JsArray(seq.update(index,
                                                                                                              seq.get(index)
                                                                                                                 .toJson()
                                                                                                                 .prepend(tail,
                                                                                                                          elem
                                                                                                                         )
                                                                                                             ))
                                                                                )


                                                     );
                          }

                         );

    }

    /**
     Adds all the elements of the array, starting from the last, to the front of this array.
     @param array the JsArray of elements to be added to the front
     @return a new JsArray
     */
    public final JsArray prependAll(final JsArray array
                                   )
    {
        return appendAllFrontTrampoline(this,
                                        requireNonNull(array)
                                       ).get();

    }

    @SuppressWarnings("Duplicates")

    public final JsArray prependAll(final JsPath path,
                                    final JsArray elems
                                   )
    {
        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsArray(seq.update(index,
                                                                                                                             arr.prependAll(elems)
                                                                                                                            )),
                                                                                               e -> new JsArray(nullPadding(index,
                                                                                                                            seq,
                                                                                                                            JsArray.EMPTY
                                                                                                                            .prependAll(elems)
                                                                                                                           ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> new JsArray(nullPadding(index,
                                                                                                                               seq,
                                                                                                                               tail.head()
                                                                                                                                   .match(o -> JsObj.EMPTY
                                                                                                                                          .prependAll(tail,
                                                                                                                                                      elems
                                                                                                                                                     ),
                                                                                                                                          a -> JsArray.EMPTY
                                                                                                                                          .prependAll(tail,
                                                                                                                                                      elems
                                                                                                                                                     )
                                                                                                                                         )

                                                                                                                              )),
                                                                                                 () -> new JsArray(seq.update(index,
                                                                                                                              seq.get(index)
                                                                                                                                 .toJson()
                                                                                                                                 .prependAll(tail,
                                                                                                                                             elems
                                                                                                                                            )
                                                                                                                             ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    public final JsArray put(final JsPath path,
                             final Function<? super JsValue, ? extends JsValue> fn
                            )
    {

        requireNonNull(fn);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();

                              return tail.ifEmptyElse(() -> ifNothingElse(() -> this,
                                                                          elem -> new JsArray(nullPadding(index,
                                                                                                          seq,
                                                                                                          elem
                                                                                                         ))
                                                                         )
                                                      .apply(fn.apply(get(path))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () ->
                                                                                 {
                                                                                     final JsValue newElem = tail.head()
                                                                                                                 .match(key -> JsObj.EMPTY
                                                                                                                        .put(tail,
                                                                                                                             fn
                                                                                                                            ),
                                                                                                                        i -> JsArray.EMPTY
                                                                                                                        .put(tail,
                                                                                                                             fn
                                                                                                                            )
                                                                                                                       );
                                                                                     return new JsArray(nullPadding(index,
                                                                                                                    seq,
                                                                                                                    newElem
                                                                                                                   ));
                                                                                 },
                                                                                 () -> new JsArray(seq.update(index,
                                                                                                              seq.get(index)
                                                                                                                 .toJson()
                                                                                                                 .put(tail,
                                                                                                                      fn
                                                                                                                     )
                                                                                                             ))

                                                                                )
                                                     );

                          }

                         );

    }

    @SuppressWarnings("squid:S1602")
        // curly braces makes IntelliJ to format the code in a more legible way
    BiPredicate<Integer, JsPath> putEmptyJson(final Vector<JsValue> pseq)
    {
        return (index, tail) ->
        {
            return index > pseq.size() - 1 || pseq.isEmpty() || pseq.get(index)
                                                                    .isNotJson()
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
        };
    }

    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduce(this);


    }

    public final <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                           final Function<? super JsPair, R> map,
                                           final Predicate<? super JsPair> predicate
                                          )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduceAll(this);

    }

    public final JsArray remove(final JsPath path)
    {

        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              final int maxIndex = seq.size() - 1;
                              if (index < -1 || index > maxIndex) return this;
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> new JsArray(index == -1 ? seq.removeAt(maxIndex) : seq.removeAt(index)),
                                                      () -> ifJsonElse(json -> new JsArray(seq.update(index,
                                                                                                      json.remove(tail)
                                                                                                     )),
                                                                       e -> this
                                                                      )
                                                      .apply(seq.get(index))
                                                     );
                          }

                         );


    }

    public final int size()
    {
        return seq.size();
    }

    public Stream<JsPair> stream()
    {
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(i ->
                                  {
                                      final JsPath path = JsPath.fromIndex(i);
                                      return JsPair.of(path,
                                                       get(path)
                                                      );
                                  });

    }

    public Stream<JsPair> streamAll()
    {
        return streamOfArr(this,
                           JsPath.empty()
                          );
    }

    static Stream<JsPair> streamOfArr(final JsArray array,
                                      final JsPath path
                                     )
    {


        requireNonNull(path);
        return requireNonNull(array).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                           array
                                                                          )),
                                                 () -> range(0,
                                                             array.size()
                                                            ).mapToObj(pair -> JsPair.of(path.index(pair),
                                                                                         array.get(Index.of(pair))

                                                                                        ))
                                                             .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  a -> streamOfArr(a,
                                                                                                                   pair.path
                                                                                                                  ),
                                                                                                  e -> Stream.of(pair)
                                                                                                 )
                                                                                      .apply(pair.value)
                                                                     )
                                                );


    }

    /**
     Returns a json array consisting of all elements of this array except the first one.
     @return a JsArray consisting of all the elements of this JsArray except the head
     @throws UserError if this JsArray is empty.
     */
    public final JsArray tail()
    {
        return new JsArray(seq.tail());
    }

    /**
     // Single-check idiom  Item 83 from effective java
     */
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = JsonLibsFactory.toString(this);

        return result;
    }

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
    public final JsArray union(final JsArray that,
                               final JsArray.TYPE ARRAY_AS
                              )
    {
        return union(this,
                     requireNonNull(that),
                     requireNonNull(ARRAY_AS)
                    ).get();
    }

    /**
     returns {@code this} plus those elements from {@code that} which position is  {@code >= this.size()},
     and, at the positions where a container of the same type exists in both {@code this} and {@code that},
     the result is their union. This operations doesn't make any sense if arrays are not considered lists,
     because there is no notion of order.
     @param that the other array
     @return a new JsArray of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00100")
    public final JsArray unionAll(final JsArray that
                                 )
    {
        return unionAll(this,
                        requireNonNull(that)
                       ).get();
    }

    /**
     Type of arrays: SET, MULTISET or LIST.
     */
    public enum TYPE
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

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private static Trampoline<JsArray> intersection(JsArray a,
                                                    JsArray b,
                                                    JsArray.TYPE ARRAY_AS
                                                   )
    {
        switch (ARRAY_AS)
        {
            case SET:
                return intersectionAsSet(a,
                                         b
                                        );
            case LIST:
                return intersectionAsList(a,
                                          b
                                         );
            case MULTISET:
                return intersectionAsMultiSet(a,
                                              b
                                             );
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }

    }

    private Trampoline<JsArray> intersectionAll(final JsArray a,
                                                final JsArray b
                                               )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsValue head = a.head();
        final JsValue otherHead = b.head();

        final Trampoline<JsArray> tailCall = intersectionAsList(a.tail(),
                                                                b.tail()
                                                               );

        if (head.isJson() && head.isSameType(otherHead))
        {
            final Json<?> obj = head.toJson();
            final Json<?> obj1 = otherHead.toJson();

            Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersectionAll(obj,
                                                                                                                obj1,
                                                                                                                JsArray.TYPE.LIST
                                                                                                               ));

            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        } else if (head.equals(otherHead))
            return more(() -> tailCall).map(it -> it.prepend(head));
        else return more(() -> tailCall);
    }

    private static Trampoline<JsArray> intersectionAsList(JsArray a,
                                                          JsArray b
                                                         )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsValue head = a.head();
        final JsArray tail = a.tail();

        final JsValue otherHead = b.head();
        final JsArray otherTail = b.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsList(tail,
                                                                                  otherTail
                                                                                 );

        if (head.equals(otherHead)) return more(tailCall).map(it -> it.prepend(head));

        return more(tailCall);


    }

    private static Trampoline<JsArray> intersectionAsMultiSet(JsArray a,
                                                              JsArray b
                                                             )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsValue head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsMultiSet(tail,
                                                                                      b
                                                                                     );

        if (b.containsValue(head)) return more(tailCall).map(it -> it.prepend(head));

        return more(tailCall);
    }

    private static Trampoline<JsArray> intersectionAsSet(JsArray a,
                                                         JsArray b
                                                        )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsValue head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsSet(tail,
                                                                                 b
                                                                                );

        if (b.containsValue(head) && !tail.containsValue(head))
            return more(tailCall).map(it -> it.prepend(head));

        return more(tailCall);
    }

    private Vector<JsValue> nullPadding(final int index,
                                        final Vector<JsValue> arr,
                                        final JsValue e
                                       )
    {
        assert arr != null;
        assert e != null;

        return nullPaddingTrampoline(index,
                                     arr,
                                     e
                                    ).get();
    }

    private Trampoline<Vector<JsValue>> nullPaddingTrampoline(final int i,
                                                              final Vector<JsValue> arr,
                                                              final JsValue e
                                                             )
    {

        if (i == arr.size()) return Trampoline.done(arr.append(e));

        if (i == -1) return Trampoline.done(arr.update(seq.size() - 1,
                                                       e
                                                      ));

        if (i < arr.size()) return Trampoline.done(arr.update(i,
                                                              e
                                                             ));
        return Trampoline.more(() -> nullPaddingTrampoline(i,
                                                           arr.append(JsNull.NULL),
                                                           e
                                                          ));
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private Trampoline<JsArray> union(JsArray a,
                                      JsArray b,
                                      JsArray.TYPE ARRAY_AS
                                     )
    {
        switch (ARRAY_AS)
        {
            case SET:
                return unionAsSet(a,
                                  b
                                 );
            case LIST:
                return unionAsList(a,
                                   b
                                  );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                                      );
            default:
                throw InternalError.arrayOptionNotImplemented(ARRAY_AS.name());
        }
    }

    private Trampoline<JsArray> unionAll(final JsArray a,
                                         final JsArray b
                                        )
    {
        if (b.isEmpty()) return done(a);
        if (a.isEmpty()) return done(b);
        final JsValue head = a.head();
        final JsValue otherHead = b.head();
        final Trampoline<JsArray> tailCall = unionAll(a.tail(),
                                                      b.tail()
                                                     );
        if (head.isJson() && head.isSameType(otherHead))
        {
            final Json<?> obj = head.toJson();
            final Json<?> obj1 = otherHead.toJson();
            Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().unionAll(obj,
                                                                                                  obj1,
                                                                                                  JsArray.TYPE.LIST
                                                                                                 ));
            return more(() -> tailCall).flatMap(tailResult -> headCall.map(tailResult::prepend));

        }
        return more(() -> tailCall).map(it -> it.prepend(head));
    }

    private static Trampoline<JsArray> unionAsList(final JsArray a,
                                                   final JsArray b
                                                  )
    {
        if (b.isEmpty()) return done(a);
        if (a.isEmpty()) return done(b);
        final Trampoline<JsArray> tailCall = unionAsList(a.tail(),
                                                         b.tail()
                                                        );
        return more(() -> tailCall).map(it -> it.prepend(a.head()));
    }

    private static Trampoline<JsArray> unionAsMultiSet(final JsArray a,
                                                       final JsArray b
                                                      )
    {
        if (b.isEmpty()) return done(a);
        if (a.isEmpty()) return done(b);
        return more(() -> () -> a.appendAll(b));
    }

    private static Trampoline<JsArray> unionAsSet(final JsArray a,
                                                  final JsArray b
                                                 )
    {
        if (b.isEmpty()) return done(a);
        if (a.isEmpty()) return done(b);
        JsValue last = b.last();
        final Trampoline<JsArray> initCall = unionAsSet(a,
                                                        b.init()
                                                       );
        if (!a.containsValue(last)) return more(() -> initCall).map(it -> it.append(last));
        return more(() -> initCall);
    }


    private boolean yContainsX(final Vector<JsValue> x,
                               final Vector<JsValue> y
                              )
    {
        for (int i = 0; i < x.size(); i++)
        {
            if (!Objects.equals(x.get(i),
                                y.get(i)
                               ))
                return false;

        }
        return true;

    }

}



