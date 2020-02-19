package jsonvalues;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MatchExp.ifNothingElse;


final class ImmutableJsObj extends AbstractJsObj
{


     static final ImmutableJsObj EMPTY = new ImmutableJsObj(HashMap.empty());


    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private volatile int hascode;
    //squid:S3077: doesn't make any sese, volatile is perfectly valid here an as a matter of fact
    //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;


    ImmutableJsObj(final HashMap<String, JsValue> myMap
                  )
    {
        super(myMap);
    }


    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        int result = hascode;
        if (result == 0)
            hascode = result = super.hashCode();
        return result;
    }

    @Override
    public Iterator<Tuple2<String, JsValue>> iterator()
    {
        return map.iterator();
    }


    /**
     // Single-check idiom  Item 83 from effective java
     */
    @Override
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = super.toString();
        return result;
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsValue> fn)
    {

        return new OpMapObjElems(this).map(requireNonNull(fn),
                                           p -> true,
                                           EMPTY_PATH
                                          )
                                      .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsValue> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapObjElems(this).map(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           EMPTY_PATH
                                          )
                                      .get();
    }

    @Override
    public final JsObj mapAllElems(final Function<? super JsPair, ? extends JsValue> fn)
    {
        return new OpMapObjElems(this).mapAll(requireNonNull(fn),
                                            p -> true,
                                              EMPTY_PATH
                                             )
                                      .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapAllElems(final Function<? super JsPair, ? extends JsValue> fn,
                                   final Predicate<? super JsPair> predicate
                                  )
    {
        return new OpMapObjElems(this).mapAll(requireNonNull(fn),
                                              requireNonNull(predicate),
                                              EMPTY_PATH
                                             )
                                      .get();
    }


    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapObjKeys(this).map(requireNonNull(fn),
                                          it -> true,
                                          EMPTY_PATH
                                         )
                                     .get();
    }

    @Override
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

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj mapAllKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapObjKeys(this).mapAll(requireNonNull(fn),
                                           it -> true,
                                             EMPTY_PATH
                                            )
                                     .get();

    }

    @Override
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


    @Override
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

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapObjObjs(this).map(requireNonNull(fn),
                                          (p, o) -> true,
                                          JsPath.empty()
                                         )
                                     .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
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

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapObjObjs(this).mapAll(requireNonNull(fn),
                                             (p, o) -> true,
                                             JsPath.empty()
                                            )
                                     .get();
    }


    @Override
    public final JsObj filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjElems(this).filter(JsPath.empty(),
                                                 requireNonNull(filter)
                                                )

                                         .get();
    }


    @Override
    public final JsObj filterAllElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjElems(this).filterAll(JsPath.empty(),
                                                    requireNonNull(filter)
                                                   )

                                         .get();

    }

    @Override
    public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterObjObjs(this).filter(JsPath.empty(),
                                                requireNonNull(filter)
                                               )

                                        .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterObjObjs(this).filterAll(JsPath.empty(),
                                                   requireNonNull(filter)
                                                  )

                                        .get();

    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjKeys(this).filter(filter)
                                        .get();

    }

    @Override
    public JsObj filterAllKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjKeys(this).filterAll(JsPath.empty(),
                                                   filter
                                                  )
                                        .get();
    }



    @SuppressWarnings("Duplicates")
    @Override
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsObj(map.put(head,
                                                                                                                 arr.appendAll(elems)
                                                                                                                )),
                                                                               el -> new ImmutableJsObj(map.put(head,
                                                                                                                JsArray.EMPTY
                                                                                                                             .appendAll(elems)
                                                                                                               ))
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  tail.head()
                                                                                                                      .match(key -> ImmutableJsObj.EMPTY
                                                                                                                                                  .appendAll(tail,
                                                                                                                                                             elems
                                                                                                                                                            ),
                                                                                                                             index -> JsArray.EMPTY
                                                                                                                                                   .appendAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             )
                                                                                                                            )
                                                                                                                 )),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  map.get(head)
                                                                                                                     .get()
                                                                                                                     .asJson()
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

    @Override
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsObj(map.put(head,
                                                                                                                 arr.append(elem)
                                                                                                                )),
                                                                               el -> new ImmutableJsObj(map.put(head,
                                                                                                                JsArray.EMPTY
                                                                                                                             .append(elem)
                                                                                                               ))
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  tail.head()
                                                                                                                      .match(key -> ImmutableJsObj.EMPTY
                                                                                                                                                  .append(tail,
                                                                                                                                                          elem
                                                                                                                                                         ),
                                                                                                                             index -> JsArray.EMPTY
                                                                                                                                                   .append(tail,
                                                                                                                                                           elem
                                                                                                                                                          )
                                                                                                                            )
                                                                                                                 )),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  map.get(head)
                                                                                                                     .get()
                                                                                                                     .asJson()
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
    @Override
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsObj(map.put(head,
                                                                                                                 arr.prependAll(elems)
                                                                                                                )),
                                                                               el -> new ImmutableJsObj(map.put(head,
                                                                                                                JsArray.EMPTY
                                                                                                                             .prependAll(elems)
                                                                                                               ))
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  tail.head()
                                                                                                                      .match(key -> ImmutableJsObj.EMPTY
                                                                                                                                                  .prependAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             ),
                                                                                                                             index -> JsArray.EMPTY
                                                                                                                                                   .prependAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              )
                                                                                                                            )
                                                                                                                 )),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  map.get(head)
                                                                                                                     .get()
                                                                                                                     .asJson()
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

    @SuppressWarnings("Duplicates")
    @Override
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsObj(map.put(head,
                                                                                                                 arr.prepend(elem)
                                                                                                                )),
                                                                               el -> new ImmutableJsObj(map.put(head,
                                                                                                                JsArray.EMPTY
                                                                                                                             .prepend(elem)
                                                                                                               ))
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  tail.head()
                                                                                                                      .match(key -> ImmutableJsObj.EMPTY
                                                                                                                                                  .prepend(tail,
                                                                                                                                                           elem
                                                                                                                                                          ),
                                                                                                                             index -> JsArray.EMPTY
                                                                                                                                                   .prepend(tail,
                                                                                                                                                            elem
                                                                                                                                                           )
                                                                                                                            )
                                                                                                                 )),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  map.get(head)
                                                                                                                     .get()
                                                                                                                     .asJson()
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

    @Override
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
                                                                          elem -> new ImmutableJsObj(map.put(head,
                                                                                                             elem
                                                                                                            ))
                                                                         )
                                                      .apply(fn.apply(get(path))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  tail.head().
                                                                                                                      match(key -> ImmutableJsObj.EMPTY
                                                                                                                                                  .put(tail,
                                                                                                                                                       fn
                                                                                                                                                      ),
                                                                                                                             index -> JsArray.EMPTY
                                                                                                                                                   .put(tail,
                                                                                                                                                        fn
                                                                                                                                                       )
                                                                                                                            )
                                                                                                                 )),
                                                                                 () -> new ImmutableJsObj(map.put(head,
                                                                                                                  map.get(head)
                                                                                                                     .get()
                                                                                                                     .asJson()
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



    @Override
    public final JsObj remove(final JsPath path)
    {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key ->
                          {
                              if (!map.containsKey(key)) return this;
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> new ImmutableJsObj(map.remove(key)),
                                                      () -> MatchExp.ifJsonElse(json -> new ImmutableJsObj(map.put(key,
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


}
