package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import jsonvalues.JsArray.TYPE;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.dslplatform.json.MyDslJson.INSTANCE;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsArray.streamOfArr;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
public class JsObj implements Json<JsObj>, Iterable<Tuple2<String, JsValue>>
{
  public static final JsObj EMPTY = new JsObj(HashMap.empty());
  @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
  private static final JsPath EMPTY_PATH = JsPath.empty();
  private static final int ID = 3;
  private final HashMap<String, JsValue> map;
  private volatile int hascode;
  //squid:S3077: doesn't make any sese, volatile is perfectly valid here an as a matter of fact
  //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
  @SuppressWarnings("squid:S3077")
  @Nullable
  private volatile String str;

  public JsObj(final HashMap<String, JsValue> myMap)
  {
    this.map = myMap;
  }

  public final JsObj append(final JsPath path,
                            final JsValue elem
                           )
  {
    requireNonNull(elem);
    if (requireNonNull(path).isEmpty()) return this;
    return path.head()
               .match(head ->
                      {
                        final JsPath tail = path.tail();
                        return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsObj(map.put(head,
                                                                                                  arr.append(elem)
                                                                                                 )),
                                                                         el -> new JsObj(map.put(head,
                                                                                                 JsArray.EMPTY
                                                                                                   .append(elem)
                                                                                                ))
                                                                        )
                                                              .apply(get(this,
                                                                         Key.of(head)
                                                                        )),
                                                () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                 t
                                                                                                                ),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   tail.head()
                                                                                                       .match(key -> JsObj.EMPTY
                                                                                                                .append(tail,
                                                                                                                        elem
                                                                                                                       ),
                                                                                                              index -> JsArray.EMPTY
                                                                                                                .append(tail,
                                                                                                                        elem
                                                                                                                       )
                                                                                                             )
                                                                                                  )),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   map.get(head)
                                                                                                      .get()
                                                                                                      .toJson()
                                                                                                      .append(tail,
                                                                                                              elem
                                                                                                             )
                                                                                                  )
                                                                           )

                                                                          )

                                               );
                      },
                      index -> this
                     );

  }

  @SuppressWarnings("Duplicates")

  public final JsObj appendAll(final JsPath path,
                               final JsArray elems
                              )
  {
    requireNonNull(elems);
    if (requireNonNull(path).isEmpty()) return this;

    return path.head()
               .match(head ->
                      {
                        final JsPath tail = path.tail();
                        return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsObj(map.put(head,
                                                                                                  arr.appendAll(elems)
                                                                                                 )),
                                                                         el -> new JsObj(map.put(head,
                                                                                                 JsArray.EMPTY
                                                                                                   .appendAll(elems)
                                                                                                ))
                                                                        )
                                                              .apply(get(this,
                                                                         Key.of(head)
                                                                        )),
                                                () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                 t
                                                                                                                ),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   tail.head()
                                                                                                       .match(key -> JsObj.EMPTY
                                                                                                                .appendAll(tail,
                                                                                                                           elems
                                                                                                                          ),
                                                                                                              index -> JsArray.EMPTY
                                                                                                                .appendAll(tail,
                                                                                                                           elems
                                                                                                                          )
                                                                                                             )
                                                                                                  )),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   map.get(head)
                                                                                                      .get()
                                                                                                      .toJson()
                                                                                                      .appendAll(tail,
                                                                                                                 elems
                                                                                                                )
                                                                                                  )
                                                                           )
                                                                          )
                                               );
                      },
                      index -> this
                     );
  }

  /**
   return true if the key is present
   @param key the key
   @return true if the specified key exists
   */
  public boolean containsKey(String key)
  {
    return map.containsKey(key);
  }


  public final boolean containsValue(final JsValue el)
  {
    return stream().anyMatch(p -> p.value.equals(Objects.requireNonNull(el)));
  }

  public static JsObj empty()
  {
    return EMPTY;
  }

  /**
   return true if this obj is equal to the given as a parameter. In the case of ARRAY_AS=LIST, this
   method is equivalent to JsObj.equals(Object).
   @param that the given array
   @param ARRAY_AS enum to specify if arrays are considered as lists or sets or multisets
   @return true if both objs are equals
   */
  @SuppressWarnings("squid:S00117") //  perfectly fine _
  public boolean equals(final JsObj that,
                        final TYPE ARRAY_AS
                       )
  {
    if (isEmpty()) return that.isEmpty();
    if (that.isEmpty()) return isEmpty();
    return keySet().stream()
                   .allMatch(field ->
                             {
                               final boolean exists = that.containsPath(JsPath.fromKey(field));
                               if (!exists) return false;
                               final JsValue elem = get(JsPath.fromKey(field));
                               final JsValue thatElem = that.get(JsPath.fromKey(field));
                               if (elem.isJson() && thatElem.isJson())
                                 return elem.toJson()
                                            .equals(thatElem,
                                                    ARRAY_AS
                                                   );
                               return elem.equals(thatElem);
                             }) && that.keySet()
                                       .stream()
                                       .allMatch(f -> this.containsPath(JsPath.fromKey(f)));
  }

  public final boolean equals(final @Nullable Object that)
  {
    if (!(that instanceof JsObj)) return false;
    if (this == that) return true;
    if (getClass() != that.getClass()) return false;
    final JsObj thatMap = (JsObj) that;
    final boolean thisEmpty = isEmpty();
    final boolean thatEmpty = thatMap.isEmpty();
    if (thisEmpty && thatEmpty) return true;
    if (thisEmpty != thatEmpty) return false;

    return keySet().stream()
                   .allMatch(f ->
                               thatMap.map.get(f)
                                          .map(it -> it.equals(map.get(f)
                                                                  .get())
                                              )
                                          .getOrElse(false) && thatMap.keySet()
                                                                      .stream()
                                                                      .allMatch(map::containsKey));
  }

  /**
   Returns a set containing each key fo this object.
   @return a Set containing each key of this JsObj
   */
  public final Set<String> keySet()
  {
    return map.keySet()
              .toJavaSet();
  }

  public JsObj filterAllKeys(final Predicate<? super JsPair> filter)
  {
    return new OpFilterObjKeys(this).filterAll(JsPath.empty(),
                                               filter
                                              )
                                    .get();
  }

  public final JsObj filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
  {
    return new OpFilterObjObjs(this).filterAll(JsPath.empty(),
                                               requireNonNull(filter)
                                              )

                                    .get();

  }

  public final JsObj filterAllValues(final Predicate<? super JsPair> filter)
  {
    return new OpFilterObjElems(this).filterAll(JsPath.empty(),
                                                requireNonNull(filter)
                                               )

                                     .get();

  }

  public final JsObj filterKeys(final Predicate<? super JsPair> filter)
  {
    return new OpFilterObjKeys(this).filter(filter)
                                    .get();

  }

  public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
  {
    return new OpFilterObjObjs(this).filter(JsPath.empty(),
                                            requireNonNull(filter)
                                           )

                                    .get();
  }

  public final JsObj filterValues(final Predicate<? super JsPair> filter)
  {
    return new OpFilterObjElems(this).filter(JsPath.empty(),
                                             requireNonNull(filter)
                                            )

                                     .get();
  }

  static JsValue get(final JsObj obj,
                     final Position position
                    )
  {
    return requireNonNull(position).match(key -> obj.map.getOrElse(key,
                                                                   NOTHING
                                                                  ),
                                          index -> NOTHING
                                         );
  }

  @Override
  public JsValue get(final JsPath path)
  {
    if (path.isEmpty()) return this;
    final JsValue e = get(this,
                          path.head()
                         );
    final JsPath tail = path.tail();
    if (tail.isEmpty()) return e;
    if (e.isNotJson()) return NOTHING;
    return e.toJson()
            .get(tail);
  }

  public JsValue get(final String key)
  {
    return get(JsPath.fromKey(Objects.requireNonNull(key)));
  }

  /**
   Returns the array located at the given key or {@link Optional#empty()} if it doesn't exist or
   it's not an array.
   @param key the key
   @return the JsArray located at the given key wrapped in an Optional

   */
  public Optional<JsArray> getOptArray(final String key)
  {
    return getOptArray(JsPath.fromKey(key));
  }

  /**
   Returns the array located at the given key or null if it doesn't exist or it's not an array.
   @param key the key
   @return the JsArray located at the given key or null
   */
  public JsArray getArray(final String key)
  {
    return getOptArray(key).orElse(null);
  }

  /**
   Returns the number located at the given key as a big decimal or {@link Optional#empty()} if it doesn't
   exist or it's not a decimal number.
   @param key the key
   @return the BigDecimal located at the given key wrapped in an Optional
   */
  public Optional<BigDecimal> getOptBigDec(final String key)
  {
    return getOptBigDec(JsPath.fromKey(key));
  }

  /**
   Returns the number located at the given key as a big decimal or null if it doesn't exist or it's
   not a decimal number.
   @param key the key
   @return the BigDecimal located at the given key or null
   */
  public BigDecimal getBigDec(final String key)
  {
    return getOptBigDec(key).orElse(null);

  }

  /**
   Returns the big integer located at the given key as a big integer or {@link Optional#empty()} if it doesn't
   exist or it's not an integral number.
   @param key the key
   @return the BigInteger located at the given key wrapped in an Optional
   */
  public Optional<BigInteger> getOptBigInt(final String key)
  {
    return getOptBigInt(JsPath.fromKey(key));
  }

  /**
   Returns the big integer located at the given key as a big integer or null if it doesn't
   exist or it's not an integral number.
   @param key the key
   @return the BigInteger located at the given key or null
   */
  public BigInteger getBigInt(final String key)
  {
    return getOptBigInt(key).orElse(null);
  }

  /**
   Returns the boolean located at the given key or {@link Optional#empty()} if it doesn't exist.
   @param key the key
   @return the Boolean located at the given key wrapped in an Optional
   */
  public Optional<Boolean> getOptBool(final String key)
  {
    return getOptBool(JsPath.fromKey(key));
  }

  /**
   Returns the boolean located at the given key or null if it doesn't exist.
   @param key the key
   @return the Boolean located at the given key or null
   */
  public Boolean getBool(final String key)
  {
    return getOptBool(key).orElse(null);
  }

  /**
   Returns the number located at the given key as a double or {@link OptionalDouble#empty()} if it
   doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
   to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
   the precision of the BigDecimal
   @param key the key
   @return the decimal number located at the given key wrapped in an OptionalDouble
   */
  public OptionalDouble getOptDouble(final String key)
  {
    return getOptDouble(JsPath.fromKey(key));
  }

  /**
   Returns the number located at the given key as a double or null if it
   doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
   to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
   the precision of the BigDecimal
   @param key the key
   @return the decimal number located at the given key or null
   */
  public Double getDouble(final String key)
  {
    final OptionalDouble optDouble = getOptDouble(key);
    return optDouble.isPresent() ? optDouble.getAsDouble() : null;
  }

  /**
   Returns the integral number located at the given key as an integer or {@link OptionalInt#empty()} if it
   doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
   @param key the key
   @return the integral number located at the given key wrapped in an OptionalInt
   */
  public OptionalInt getOptInt(final String key)
  {
    return getOptInt(JsPath.fromKey(key));
  }

  /**
   Returns the integral number located at the given key as an integer or null if it
   doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
   @param key the key
   @return the integral number located at the given key or null
   */
  public Integer getInt(final String key)
  {
    final OptionalInt optInt = getOptInt(key);
    return optInt.isPresent() ? optInt.getAsInt() : null;
  }

  /**
   Returns the integral number located at the given key as a long or {@link OptionalLong#empty()} if it
   doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.
   @param key the key
   @return the integral number located at the given key wrapped in an OptionalLong
   */
  public OptionalLong getOptLong(final String key)
  {
    return getOptLong(JsPath.fromKey(key));
  }

  /**
   Returns the integral number located at the given key as a long or null if it
   doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.
   @param key the key
   @return the integral number located at the given key or null
   */
  public Long getLong(final String key)
  {
    final OptionalLong optLong = getOptLong(key);
    return optLong.isPresent() ? optLong.getAsLong() : null;
  }

  /**
   Returns the json object located at the given key or {@link Optional#empty()} if it doesn't exist or it's
   not an object.
   @param key the key
   @return the json object located at the given key wrapped in an Optional
   */
  public Optional<JsObj> getOptObj(final String key)
  {
    return getOptObj(JsPath.fromKey(key));
  }

  /**
   Returns the json object located at the given key or null if it doesn't exist or it's not an object.
   @param key the key
   @return the json object located at the given key or null
   */
  public JsObj getObj(final String key)
  {
    return getOptObj(key).orElse(null);
  }

  /**
   Returns the string located at the given key or {@link Optional#empty()} if it doesn't exist or it's
   not an string.
   @param key the key
   @return the string located at the given key wrapped in an Optional
   */
  public Optional<String> getOptStr(final String key)
  {
    return getOptStr(JsPath.fromKey(key));
  }

  /**
   Returns the string located at the given key or null if it doesn't exist or it's not an string.
   @param key the key
   @return the string located at the given key or null
   */
  public String getStr(final String key)
  {
    return getOptStr(key).orElse(null);
  }

  /**
   equals method is inherited, so it's implemented. The purpose of this method is to cache
   the hashcode once calculated. the object is immutable and it won't change
   Single-check idiom  Item 83 from Effective Java
   */
  @SuppressWarnings("squid:S1206")

  public final int hashCode()
  {
    int result = hascode;
    if (result == 0)
      hascode = result = map.hashCode();
    return result;
  }

  /**
   Returns a pair with an arbitrary key of this object and its associated element. When using head
   and tail to process a JsObj, the key of the pair returned must be passed in to get the tail using
   the method {@link #tail()}.
   @return an arbitrary {@code Map.Entry<String,JsElem>} of this JsObj
   @throws UserError if this json object is empty
   */
  public final Tuple2<String, JsValue> head()
  {
    return map.head();
  }

  @Override
  public int id()
  {
    return ID;
  }

  <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                final BiFunction<Tuple2<String, JsValue>, JsObj, Trampoline<T>> fn
                               )
  {


    if (this.isEmpty()) return empty;

    final Tuple2<String, JsValue> head = this.head();

    final JsObj tail = this.tail();

    return fn.apply(head,
                    tail
                   );

  }

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
  public JsObj intersection(final JsObj that,
                            final TYPE ARRAY_AS
                           )
  {
    requireNonNull(that);
    return intersection(this,
                        that,
                        ARRAY_AS
                       )
      .get();
  }

  /**
   {@code this.intersectionAll(that)} behaves as {@code this.intersection(that, LIST)}, but for those elements
   that are containers of the same type and are located at the same position, the result is their
   intersection.  So this operation is kind of a 'recursive' intersection.
   @param that the other object
   @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
   @return a new JsObj of the same type as the inputs (mutable or immutable)
   */
  // squid:S00117 ARRAY_AS should be a valid name
  @SuppressWarnings({"squid:S00117"})
  public JsObj intersectionAll(final JsObj that,
                               final TYPE ARRAY_AS
                              )
  {
    requireNonNull(that);
    requireNonNull(ARRAY_AS);
    return intersectionAll(this,
                           that,
                           ARRAY_AS
                          ).get();
  }

  public final boolean isEmpty()
  {
    return map.isEmpty();
  }

  public boolean isObj()
  {
    return true;
  }

  @SuppressWarnings("squid:S1602")
    // curly braces makes IntelliJ to format the code in a more legible way
  BiPredicate<String, JsPath> isReplaceWithEmptyJson(final HashMap<String, JsValue> pmap)
  {
    return (head, tail) ->
    {
      return (!pmap.containsKey(head) || !pmap.get(head)
                                              .filter(JsValue::isNotJson)
                                              .isEmpty())
        ||
        (
          (tail.head()
               .isKey() && !pmap.get(head)
                                .filter(JsValue::isArray)
                                .isEmpty())
        )
        ||
        (tail.head()
             .isIndex() && !pmap.get(head)
                                .filter(JsValue::isObj)
                                .isEmpty());
    };
  }

  @Override
  public Iterator<Tuple2<String, JsValue>> iterator()
  {
    return map.iterator();
  }

  public final JsObj mapAllKeys(final Function<? super JsPair, String> fn)
  {
    return new OpMapObjKeys(this).mapAll(requireNonNull(fn),
                                         it -> true,
                                         EMPTY_PATH
                                        )
                                 .get();

  }

  @SuppressWarnings("squid:S00100") // xx_ traverses the whole json
  public final JsObj mapAllKeys(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
  {
    return new OpMapObjKeys(this).mapAll(requireNonNull(fn),
                                         requireNonNull(predicate),
                                         EMPTY_PATH
                                        )
                                 .get();
  }

  public final JsObj mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
  {
    return new OpMapObjObjs(this).mapAll(requireNonNull(fn),
                                         requireNonNull(predicate),
                                         JsPath.empty()
                                        )
                                 .get();
  }

  public final JsObj mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
  {
    return new OpMapObjObjs(this).mapAll(requireNonNull(fn),
                                         (p, o) -> true,
                                         JsPath.empty()
                                        )
                                 .get();
  }

  public final JsObj mapAllValues(final Function<? super JsPair, ? extends JsValue> fn)
  {
    return new OpMapObjElems(this).mapAll(requireNonNull(fn),
                                          p -> true,
                                          EMPTY_PATH
                                         )
                                  .get();
  }

  public final JsObj mapAllValues(final Function<? super JsPair, ? extends JsValue> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
  {
    return new OpMapObjElems(this).mapAll(requireNonNull(fn),
                                          requireNonNull(predicate),
                                          EMPTY_PATH
                                         )
                                  .get();
  }

  public final JsObj mapKeys(final Function<? super JsPair, String> fn)
  {
    return new OpMapObjKeys(this).map(requireNonNull(fn),
                                      it -> true,
                                      EMPTY_PATH
                                     )
                                 .get();
  }

  public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                             final Predicate<? super JsPair> predicate
                            )
  {
    return new OpMapObjKeys(this).map(requireNonNull(fn),
                                      requireNonNull(predicate),
                                      EMPTY_PATH
                                     )
                                 .get();
  }

  public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate
                            )
  {

    return new OpMapObjObjs(this).map(requireNonNull(fn),
                                      requireNonNull(predicate),
                                      JsPath.empty()
                                     )
                                 .get();
  }

  public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
  {
    return new OpMapObjObjs(this).map(requireNonNull(fn),
                                      (p, o) -> true,
                                      JsPath.empty()
                                     )
                                 .get();
  }

  public final JsObj mapValues(final Function<? super JsPair, ? extends JsValue> fn)
  {

    return new OpMapObjElems(this).map(requireNonNull(fn),
                                       p -> true,
                                       EMPTY_PATH
                                      )
                                  .get();
  }

  public final JsObj mapValues(final Function<? super JsPair, ? extends JsValue> fn,
                               final Predicate<? super JsPair> predicate
                              )
  {
    return new OpMapObjElems(this).map(requireNonNull(fn),
                                       requireNonNull(predicate),
                                       EMPTY_PATH
                                      )
                                  .get();
  }

  /**
   Returns a one-element immutable object.
   @param key name of a key
   @param el  JsElem to be associated to the key
   @return an immutable one-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  public static JsObj of(final String key,
                         final JsValue el
                        )
  {

    return JsObj.EMPTY.put(JsPath.empty()
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
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2
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
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3
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
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4
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
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5
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
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @return an immutable seven-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7
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
              el5,
              key6,
              el6
             ).put(JsPath.empty()
                         .key(requireNonNull(key7)),
                   el7
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @return an immutable eight-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8
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
              el5,
              key6,
              el6,
              key7,
              el7
             ).put(JsPath.empty()
                         .key(requireNonNull(key8)),
                   el8
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @return an immutable nine-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8
             ).put(JsPath.empty()
                         .key(requireNonNull(key9)),
                   el9
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @return an immutable ten-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9
             ).put(JsPath.empty()
                         .key(requireNonNull(key10)),
                   el10
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @param key11 name of a key
   @param el11 JsElem to be associated to the key11
   @return an immutable eleven-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10,
                         final String key11,
                         final JsValue el11
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9,
              key10,
              el10
             ).put(JsPath.empty()
                         .key(requireNonNull(key11)),
                   el11
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @param key11 name of a key
   @param el11 JsElem to be associated to the key11
   @param key12 name of a key
   @param el12 JsElem to be associated to the key12
   @return an immutable twelve-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10,
                         final String key11,
                         final JsValue el11,
                         final String key12,
                         final JsValue el12
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9,
              key10,
              el10,
              key11,
              el11
             ).put(JsPath.empty()
                         .key(requireNonNull(key12)),
                   el12
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @param key11 name of a key
   @param el11 JsElem to be associated to the key11
   @param key12 name of a key
   @param el12 JsElem to be associated to the key12
   @param key13 name of a key
   @param el13 JsElem to be associated to the key13
   @return an immutable twelve-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10,
                         final String key11,
                         final JsValue el11,
                         final String key12,
                         final JsValue el12,
                         final String key13,
                         final JsValue el13
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9,
              key10,
              el10,
              key11,
              el11,
              key12,
              el12
             ).put(JsPath.empty()
                         .key(requireNonNull(key13)),
                   el13
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @param key11 name of a key
   @param el11 JsElem to be associated to the key11
   @param key12 name of a key
   @param el12 JsElem to be associated to the key12
   @param key13 name of a key
   @param el13 JsElem to be associated to the key13
   @param key14 name of a key
   @param el14 JsElem to be associated to the key14
   @return an immutable twelve-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10,
                         final String key11,
                         final JsValue el11,
                         final String key12,
                         final JsValue el12,
                         final String key13,
                         final JsValue el13,
                         final String key14,
                         final JsValue el14
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9,
              key10,
              el10,
              key11,
              el11,
              key12,
              el12,
              key13,
              el13
             ).put(JsPath.empty()
                         .key(requireNonNull(key14)),
                   el14
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
   @param key7 name of a key
   @param el7 JsElem to be associated to the key7
   @param key8 name of a key
   @param el8 JsElem to be associated to the key8
   @param key9 name of a key
   @param el9 JsElem to be associated to the key9
   @param key10 name of a key
   @param el10 JsElem to be associated to the key10
   @param key11 name of a key
   @param el11 JsElem to be associated to the key11
   @param key12 name of a key
   @param el12 JsElem to be associated to the key12
   @param key13 name of a key
   @param el13 JsElem to be associated to the key13
   @param key14 name of a key
   @param el14 JsElem to be associated to the key14
   @param key15 name of a key
   @param el15 JsElem to be associated to the key15
   @return an immutable twelve-element JsObj
   @throws UserError if an elem is a mutable Json
   */
  // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
  @SuppressWarnings("squid:S00107")
  public static JsObj of(final String key1,
                         final JsValue el1,
                         final String key2,
                         final JsValue el2,
                         final String key3,
                         final JsValue el3,
                         final String key4,
                         final JsValue el4,
                         final String key5,
                         final JsValue el5,
                         final String key6,
                         final JsValue el6,
                         final String key7,
                         final JsValue el7,
                         final String key8,
                         final JsValue el8,
                         final String key9,
                         final JsValue el9,
                         final String key10,
                         final JsValue el10,
                         final String key11,
                         final JsValue el11,
                         final String key12,
                         final JsValue el12,
                         final String key13,
                         final JsValue el13,
                         final String key14,
                         final JsValue el14,
                         final String key15,
                         final JsValue el15
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
              el5,
              key6,
              el6,
              key7,
              el7,
              key8,
              el8,
              key9,
              el9,
              key10,
              el10,
              key11,
              el11,
              key12,
              el12,
              key13,
              el13,
              key14,
              el14
             ).put(JsPath.empty()
                         .key(requireNonNull(key15)),
                   el15
                  );
  }
  /**
   Returns an immutable object from one or more pairs.
   @param pair a pair
   @param others more optional pairs
   @return an immutable JsObject
   @throws UserError if an elem of a pair is mutable

   */
  public static JsObj of(final JsPair pair,
                         final JsPair... others
                        )
  {
    JsObj obj = JsObj.EMPTY.put(pair.path,
                                pair.value
                               );
    for (JsPair p : others)
    {

      obj = obj.put(p.path,
                    p.value
                   );
    }
    return obj;

  }

  public static JsObj ofIterable(final Iterable<Map.Entry<String, JsValue>> xs)
  {
    JsObj acc = JsObj.EMPTY;
    for (Map.Entry<String, JsValue> x : requireNonNull(xs))
    {

      acc = acc.put(JsPath.fromKey(x.getKey()),
                    x.getValue()
                   );
    }
    return acc;
  }

  /**
   Tries to parse the string into an immutable object.
   @param str the string to be parsed
   @return a JsOb object
   @throws MalformedJson if the string doesnt represent a json object
   */
  public static JsObj parse(final String str) throws MalformedJson
  {

    try (JsonParser parser = JacksonFactory.INSTANCE.createParser(requireNonNull(str)))
    {
      JsonToken keyEvent = parser.nextToken();
      if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
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
   Tries to parse the string into an immutable object,  performing the specified transformations during the parsing.
   It's faster to do certain operations right while the parsing instead of doing the parsing and
   applying them later.
   @param str  string to be parsed
   @param builder builder with the transformations that will be applied during the parsing
   @return a JsObj object
   @throws MalformedJson if the string doesnt represent a json object
   */
  public static JsObj parse(final String str,
                            final ParseBuilder builder
                           ) throws MalformedJson
  {

    try (JsonParser parser = JacksonFactory.INSTANCE.createParser(requireNonNull(str.getBytes())))
    {
      final JsonToken keyEvent = parser.nextToken();
      if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
      return new JsObj(JsObj.parse(parser,
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

  static HashMap<String, JsValue> parse(final JsonParser parser) throws IOException
  {
    HashMap<String, JsValue> map = HashMap.empty();
    String key = parser.nextFieldName();
    for (; key != null; key = parser.nextFieldName())
    {
      JsValue elem;
      switch (parser.nextToken()
                    .id())
      {
        case JsonTokenId.ID_STRING:
          elem = JsStr.of(parser.getValueAsString());
          break;
        case JsonTokenId.ID_NUMBER_INT:
          elem = JsNumber.of(parser);
          break;
        case JsonTokenId.ID_NUMBER_FLOAT:
          elem = JsBigDec.of(parser.getDecimalValue());
          break;
        case JsonTokenId.ID_FALSE:
          elem = FALSE;
          break;
        case JsonTokenId.ID_TRUE:
          elem = TRUE;
          break;
        case JsonTokenId.ID_NULL:
          elem = NULL;
          break;
        case JsonTokenId.ID_START_OBJECT:
          elem = new JsObj(parse(parser));
          break;
        case JsonTokenId.ID_START_ARRAY:
          elem = new JsArray(JsArray.parse(parser));
          break;
        default:
          throw InternalError.tokenNotExpected(parser.currentToken()
                                                     .name());
      }
      map = map.put(key,
                    elem
                   );
    }

    return map;

  }

  static HashMap<String, JsValue> parse(final JsonParser parser,
                                        final ParseBuilder.Options options,
                                        final JsPath path
                                       ) throws IOException
  {

    HashMap<String, JsValue> map = HashMap.empty();
    final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
    while (parser.nextToken() != JsonToken.END_OBJECT)
    {
      final String key = options.keyMap.apply(parser.getCurrentName());
      final JsPath currentPath = path.key(key);
      final JsPair pair;
      switch (parser.nextToken()
                    .id())
      {
        case JsonTokenId.ID_STRING:
          pair = JsPair.of(currentPath,
                           JsStr.of(parser.getValueAsString())
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;
        case JsonTokenId.ID_NUMBER_INT:
          pair = JsPair.of(currentPath,
                           JsNumber.of(parser)
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;
        case JsonTokenId.ID_NUMBER_FLOAT:
          pair = JsPair.of(currentPath,
                           JsBigDec.of(parser.getDecimalValue())
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;
        case JsonTokenId.ID_TRUE:
          pair = JsPair.of(currentPath,
                           TRUE
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;
        case JsonTokenId.ID_FALSE:
          pair = JsPair.of(currentPath,
                           FALSE
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;
        case JsonTokenId.ID_NULL:
          pair = JsPair.of(currentPath,
                           NULL
                          );
          map = (condition.test(pair)) ? map.put(key,
                                                 options.elemMap.apply(pair)
                                                ) : map;
          break;

        case JsonTokenId.ID_START_OBJECT:
          if (options.keyFilter.test(currentPath))
          {
            map = map.put(key,
                          new JsObj(parse(parser,
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
            map = map.put(key,
                          new JsArray(JsArray.parse(parser,
                                                    options,
                                                    currentPath.index(-1)
                                                   )
                          )
                         );
          }
          break;
        default:
          throw InternalError.tokenNotExpected(parser.currentToken()
                                                     .name());
      }
    }
    return map;

  }

  @SuppressWarnings("Duplicates")

  public final JsObj prepend(final JsPath path,
                             final JsValue elem
                            )
  {
    requireNonNull(elem);
    if (requireNonNull(path).isEmpty()) return this;
    return path.head()
               .match(head ->
                      {
                        final JsPath tail = path.tail();
                        return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsObj(map.put(head,
                                                                                                  arr.prepend(elem)
                                                                                                 )),
                                                                         el -> new JsObj(map.put(head,
                                                                                                 JsArray.EMPTY
                                                                                                   .prepend(elem)
                                                                                                ))
                                                                        )
                                                              .apply(get(this,
                                                                         Key.of(head)
                                                                        )),
                                                () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                 t
                                                                                                                ),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   tail.head()
                                                                                                       .match(key -> JsObj.EMPTY.prepend(tail,
                                                                                                                                         elem
                                                                                                                                        ),
                                                                                                              index -> JsArray.EMPTY.prepend(tail,
                                                                                                                                             elem
                                                                                                                                            )
                                                                                                             )
                                                                                                  )),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   map.get(head)
                                                                                                      .get()
                                                                                                      .toJson()
                                                                                                      .prepend(tail,
                                                                                                               elem
                                                                                                              )
                                                                                                  ))

                                                                          )

                                               );
                      },
                      index -> this
                     );

  }

  @SuppressWarnings("Duplicates")

  public final JsObj prependAll(final JsPath path,
                                final JsArray elems
                               )
  {
    requireNonNull(elems);
    if (requireNonNull(path).isEmpty()) return this;
    return path.head()
               .match(head ->
                      {
                        final JsPath tail = path.tail();
                        return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new JsObj(map.put(head,
                                                                                                  arr.prependAll(elems)
                                                                                                 )),
                                                                         el -> new JsObj(map.put(head,
                                                                                                 JsArray.EMPTY
                                                                                                   .prependAll(elems)
                                                                                                ))
                                                                        )
                                                              .apply(get(this,
                                                                         Key.of(head)
                                                                        )),
                                                () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                 t
                                                                                                                ),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   tail.head()
                                                                                                       .match(key -> JsObj.EMPTY
                                                                                                                .prependAll(tail,
                                                                                                                            elems
                                                                                                                           ),
                                                                                                              index -> JsArray.EMPTY
                                                                                                                .prependAll(tail,
                                                                                                                            elems
                                                                                                                           )
                                                                                                             )
                                                                                                  )),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   map.get(head)
                                                                                                      .get()
                                                                                                      .toJson()
                                                                                                      .prependAll(tail,
                                                                                                                  elems
                                                                                                                 )
                                                                                                  ))

                                                                          )

                                               );
                      },
                      index -> this
                     );

  }

  /**
   Inserts the element at the key in this json, replacing any existing element.
   @param key the key
   @param value the element
   @return a new json object
   */
  public JsObj put(final String key,
                   final JsValue value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the string at the key in this json, replacing any existing element.
   @param key the key
   @param value the string
   @return a new json object
   */
  public JsObj put(final String key,
                   final String value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the integer number at the key in this json, replacing any existing element.
   @param key the key
   @param value the number
   @return a new json object
   */
  public JsObj put(final String key,
                   final int value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the long number at the key in this json, replacing any existing element.
   @param key the key
   @param value the number
   @return a new json object
   */
  public JsObj put(final String key,
                   final long value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the boolean at the key in this json, replacing any existing element.
   @param key the key
   @param value the boolean
   @return a new json object
   */
  public JsObj put(final String key,
                   final boolean value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the object at the key in this json, replacing any existing element.
   @param key the key
   @param value the object
   @return a new json object
   */
  public JsObj put(final String key,
                   final JsObj value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the array at the key in this json, replacing any existing element.
   @param key the key
   @param value the array
   @return a new json object
   */
  public JsObj put(final String key,
                   final JsArray value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the big decimal number at the key in this json, replacing any existing element.
   @param key the key
   @param value the number
   @return a new json object
   */
  public JsObj put(final String key,
                   final BigDecimal value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the big integer number at the key in this json, replacing any existing element.
   @param key the key
   @param value the number
   @return a new json object
   */
  public JsObj put(final String key,
                   final BigInteger value
                  )
  {
    return put(JsPath.fromKey(key),
               value
              );
  }

  /**
   Inserts the element returned by the function at the given key in this json, replacing any existing element
   @param key  the key
   @param fn the function that takes as an input the JsElem at the key and produces the JsElem to
   be inserted at the key
   @return the same instance or a new json of the same type T
   */
  public final JsObj put(final String key,
                         final Function<? super JsValue, ? extends JsValue> fn
                        )
  {
    return put(JsPath.fromKey(key),
               fn
              );
  }

  public final JsObj put(final JsPath path,
                         final Function<? super JsValue, ? extends JsValue> fn
                        )
  {
    requireNonNull(fn);
    if (requireNonNull(path).isEmpty()) return this;
    return path.head()
               .match(head ->
                      {
                        final JsPath tail = path.tail();

                        return tail.ifEmptyElse(() -> ifNothingElse(() -> this,
                                                                    elem -> new JsObj(map.put(head,
                                                                                              elem
                                                                                             ))
                                                                   )
                                                  .apply(fn.apply(get(path))),
                                                () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                 t
                                                                                                                ),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   tail.head()
                                                                                                       .
                                                                                                         match(key -> JsObj.EMPTY
                                                                                                                 .put(tail,
                                                                                                                      fn
                                                                                                                     ),
                                                                                                               index -> JsArray.EMPTY
                                                                                                                 .put(tail,
                                                                                                                      fn
                                                                                                                     )
                                                                                                              )
                                                                                                  )),
                                                                           () -> new JsObj(map.put(head,
                                                                                                   map.get(head)
                                                                                                      .get()
                                                                                                      .toJson()
                                                                                                      .put(tail,
                                                                                                           fn
                                                                                                          )
                                                                                                  ))

                                                                          )
                                               );
                      },
                      index -> this

                     );

  }

  /**
   Inserts the element returned by the supplier at the given key in this json, if no element is present.
   The supplier is not invoked if the element is present.
   @param key the key
   @param supplier the supplier which computes the new JsElem if absent
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final Supplier<? extends JsValue> supplier
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       supplier
                      );
  }

  /**
   Inserts at the given key in this json, if no element is present, the specified integer.
   @param key the key
   @param number the integer
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final int number
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       number
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified string.
   @param key the key
   @param str the string
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final String str
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       str
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified boolean.
   @param key the key
   @param bool the boolean
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final boolean bool
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       bool
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified obj.
   @param key the key
   @param obj the json object
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final JsObj obj
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       obj
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified array.
   @param key the key
   @param array the json array
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final JsArray array
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       array
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified long number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final long number
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       number
                      );

  }

  /**
   Inserts at the given key in this json, if no element is present, the specified double number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfAbsent(final String key,
                           final double number
                          )
  {
    return putIfAbsent(JsPath.fromKey(key),
                       number
                      );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified integer number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final int number
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        number
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified string.
   @param key the key
   @param str the string
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final String str
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        str
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified obj.
   @param key the key
   @param obj the object
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final JsObj obj
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        obj
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified array.
   @param key the key
   @param array the array
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final JsArray array
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        array
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified long number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final long number
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        number
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified double number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final double number
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        number
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified boolean.
   @param key the key
   @param bool the boolean
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final boolean bool
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        bool
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified big integer number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final BigInteger number
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        number
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the specified big decimal number.
   @param key the key
   @param number the number
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final BigDecimal number
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        number
                       );

  }

  /**
   Inserts at the given key in this json, if some element is present, the element returned by the
   function.
   @param key the key
   @param fn the function which computes the new JsElem from the existing one
   @return the same instance or a new obj
   */
  public JsObj putIfPresent(final String key,
                            final Function<? super JsValue, ? extends JsValue> fn
                           )
  {
    return putIfPresent(JsPath.fromKey(key),
                        fn
                       );

  }

  public JsObj putNull(final String key)
  {
    return put(JsPath.fromKey(key),
               NULL
              );
  }

  public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                      final Function<? super JsPair, R> map,
                                      final Predicate<? super JsPair> predicate
                                     )
  {
    return new OpMapReduce<>(requireNonNull(predicate),
                             map,
                             op
    ).reduce(this);
  }

  public final <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                         final Function<? super JsPair, R> map,
                                         final Predicate<? super JsPair> predicate
                                        )
  {
    return new OpMapReduce<>(requireNonNull(predicate),
                             map,
                             op
    ).reduceAll(this);

  }

  public final JsObj remove(final JsPath path)
  {
    if (requireNonNull(path).isEmpty()) return this;
    return path.head()
               .match(key ->
                      {
                        if (!map.containsKey(key)) return this;
                        final JsPath tail = path.tail();
                        return tail.ifEmptyElse(() -> new JsObj(map.remove(key)),
                                                () -> MatchExp.ifJsonElse(json -> new JsObj(map.put(key,
                                                                                                    json.remove(tail)
                                                                                                   )),
                                                                          e -> this
                                                                         )
                                                              .apply(map.get(key)
                                                                        .get())
                                               );
                      },
                      index -> this
                     );


  }

  public final boolean same(final JsObj obj)
  {
    final HashMap<String, JsValue> other = obj.map;
    final boolean thisEmpty = isEmpty();
    final boolean thatEmpty = other.isEmpty();
    if (thisEmpty && thatEmpty) return true;
    if (thisEmpty != thatEmpty) return false;

    return keySet().stream()
                   .allMatch(f ->
                               other.get(f)
                                    .map(it ->
                                         {
                                           final JsValue a = map.get(f)
                                                                .get();
                                           if (a.isObj() && it.isObj()) return a.toJsObj()
                                                                                .equals(it.toJsObj());
                                           else if (a.isArray() && it.isArray()) return a.toJsArray()
                                                                                         .equals(it.toJsArray());
                                           else return it.equals(a);
                                         })
                                    .getOrElse(false) && other.keySet()
                                                              .toJavaStream()
                                                              .allMatch(map::containsKey));
  }

  public final int size()
  {
    return map.size();
  }

  public final Stream<JsPair> stream()
  {
    return this.keySet()
               .stream()
               .map(f ->
                    {
                      final JsPath key = JsPath.fromKey(f);
                      return JsPair.of(key,
                                       this.get(key)
                                      );
                    }

                   );
  }

  public final Stream<JsPair> streamAll()
  {
    return streamOfObj(this,
                       JsPath.empty()
                      );
  }

  static Stream<JsPair> streamOfObj(final JsObj obj,
                                    final JsPath path
                                   )
  {

    requireNonNull(path);
    return requireNonNull(obj).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                     obj
                                                                    )),
                                           () -> obj.keySet()
                                                    .stream()
                                                    .map(key -> JsPair.of(path.key(key),
                                                                          get(obj,
                                                                              Key.of(key)
                                                                             )
                                                                         ))
                                                    .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                          pair.path
                                                                                                         ),
                                                                                         a -> streamOfArr(a,
                                                                                                          pair.path
                                                                                                         ),
                                                                                         e -> Stream.of(pair)
                                                                                        )
                                                                             .apply(pair.value))
                                          );

  }

  /**
   Returns a new object with all the entries of this json object except the one with the given key.
   @return a new JsObj
   @throws UserError if this json object is empty
   */
  public final JsObj tail()
  {
    return new JsObj(map.tail());
  }

  /**
   // Single-check idiom  Item 83 from effective java
   */
  public final String toString()
  {
    String result = str;
    if (result == null)
      str = result = new String(INSTANCE.serialize(this));
    return result;
  }


  /**
   returns {@code this} json object plus those pairs from the given json object {@code that} which
   keys don't exist in {@code this}. Taking that into account, it's not a commutative operation unless
   the elements associated with the keys that exist in both json objects are equals.
   @param that the given json object
   @return a new JsObj of the same type as the inputs (mutable or immutable)
   */
  public final JsObj union(final JsObj that
                          )
  {
    return union(this,
                 requireNonNull(that)
                ).get();

  }

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
  public final JsObj unionAll(final JsObj that,
                              final TYPE ARRAY_AS
                             )
  {
    requireNonNull(that);
    requireNonNull(ARRAY_AS);
    return ifEmptyElse(() -> that,
                       () -> that.ifEmptyElse(() -> this,
                                              () -> unionAll(this,
                                                             that,
                                                             ARRAY_AS
                                                            )
                                                .get()
                                             )
                      );

  }

  @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
  private Trampoline<JsObj> intersection(final JsObj a,
                                         final JsObj b,
                                         final JsArray.TYPE ARRAY_AS
                                        )
  {
    if (a.isEmpty()) return done(a);
    if (b.isEmpty()) return done(b);
    Tuple2<String, JsValue> head = a.head();
    JsObj tail = a.tail();
    final Trampoline<Trampoline<JsObj>> tailCall = () -> intersection(tail,
                                                                      b,
                                                                      ARRAY_AS
                                                                     );
    final JsValue bElem = b.get(JsPath.fromKey(head._1));

    return ((bElem.isJson() && bElem.toJson()
                                    .equals(head._2,
                                            ARRAY_AS
                                           )) || bElem.equals(head._2)) ?
      more(tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                      head._2
                                     )) :
      more(tailCall);
  }

  @SuppressWarnings({"squid:S00117",}) // ARRAY_AS should be a valid name for an enum constant
  private Trampoline<JsObj> intersectionAll(final JsObj a,
                                            final JsObj b,
                                            final JsArray.TYPE ARRAY_AS
                                           )
  {
    if (a.isEmpty()) return done(a);
    if (b.isEmpty()) return done(b);
    Tuple2<String, JsValue> head = a.head();

    JsObj tail = a.tail();

    final Trampoline<JsObj> tailCall = more(() -> intersectionAll(tail,
                                                                  b,
                                                                  ARRAY_AS
                                                                 ));
    if (b.containsPath(JsPath.fromKey(head._1)))
    {

      final JsValue headOtherElement = b.get(JsPath.fromKey(head._1));
      if (headOtherElement.equals(head._2))
      {
        return more(() -> intersectionAll(tail,
                                          b.tail(),
                                          ARRAY_AS
                                         )).map(it -> it.put(JsPath.fromKey(head._1),
                                                             head._2
                                                            ));

      } else if (head._2
        .isJson() && head._2
        .isSameType(headOtherElement))
      {//different but same container
        Json<?> obj = head._2
          .toJson();
        Json<?> obj1 = headOtherElement.toJson();

        Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersectionAll(obj,
                                                                                                            obj1,
                                                                                                            ARRAY_AS
                                                                                                           )
                                                     );
        return more(() -> tailCall).flatMap(json -> headCall
                                              .map(it -> json.put(JsPath.fromKey(head._1),
                                                                  it
                                                                 )
                                                  )
                                           );
      }

    }
    return tailCall;
  }

  private Trampoline<JsObj> union(JsObj a,
                                  JsObj b
                                 )
  {
    if (b.isEmpty()) return done(a);
    Tuple2<String, JsValue> head = b.head();
    JsObj tail = b.tail();
    return union(a,
                 tail
                ).map(it ->
                        it.putIfAbsent(JsPath.fromKey(head._1),
                                       () -> head._2
                                      ));
  }

  //squid:S00117 ARRAY_AS should be a valid name
  private Trampoline<JsObj> unionAll(final JsObj a,
                                     final JsObj b,
                                     final JsArray.TYPE ARRAY_AS
                                    )
  {

    if (b.isEmpty()) return done(a);
    Tuple2<String, JsValue> head = b.head();
    JsObj tail = b.tail();
    Trampoline<JsObj> tailCall = more(() -> unionAll(a,
                                                     tail,
                                                     ARRAY_AS
                                                    ));
    return ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                                                     head._2
                                                                    )),
                         MatchExp.ifPredicateElse(e -> e.isJson() && e.isSameType(head._2),
                                                  it ->
                                                  {
                                                    Json<?> obj = a.get(JsPath.empty()
                                                                              .key(head._1))
                                                                   .toJson();
                                                    Json<?> obj1 = head._2
                                                      .toJson();

                                                    Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().unionAll(obj,
                                                                                                                                          obj1,
                                                                                                                                          ARRAY_AS
                                                                                                                                         )
                                                                                                 );
                                                    return more(() -> tailCall).flatMap(tailResult -> headCall.map(headUnion_ ->
                                                                                                                     tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                    headUnion_
                                                                                                                                   )
                                                                                                                  )
                                                                                       );
                                                  },
                                                  it -> tailCall
                                                 )
                        )
      .apply(a.get(JsPath.empty()
                         .key(head._1)));


  }


}

