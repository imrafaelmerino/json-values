package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
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


    ImmutableJsObj(final HashMap<String, JsElem> myMap
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
    public Iterator<Tuple2<String, JsElem>> iterator()
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
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {

        return new OpMapObjElems(this).map(requireNonNull(fn),
                                           p -> true,
                                           EMPTY_PATH
                                          )
                                      .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
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
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapObjElems(this).map_(requireNonNull(fn),
                                            p -> true,
                                            EMPTY_PATH
                                           )
                                      .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return new OpMapObjElems(this).map_(requireNonNull(fn),
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
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapObjKeys(this).map_(requireNonNull(fn),
                                           it -> true,
                                           EMPTY_PATH
                                          )
                                     .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") // xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapObjKeys(this).map_(requireNonNull(fn),
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
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return new OpMapObjObjs(this).map_(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           JsPath.empty()
                                          )
                                     .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapObjObjs(this).map_(requireNonNull(fn),
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
    public final JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjElems(this).filter_(JsPath.empty(),
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
    public final JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterObjObjs(this).filter_(JsPath.empty(),
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
    public JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterObjKeys(this).filter_(JsPath.empty(),
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
                                                                                                                ImmutableJsArray.EMPTY
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
                                                                                                                             index -> ImmutableJsArray.EMPTY
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
                              final JsElem elem
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
                                                                                                                ImmutableJsArray.EMPTY
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
                                                                                                                             index -> ImmutableJsArray.EMPTY
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
                                                                                                                ImmutableJsArray.EMPTY
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
                                                                                                                             index -> ImmutableJsArray.EMPTY
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
                               final JsElem elem
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
                                                                                                                ImmutableJsArray.EMPTY
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
                                                                                                                             index -> ImmutableJsArray.EMPTY
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
                           final Function<? super JsElem, ? extends JsElem> fn
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
                                                                                                                             index -> ImmutableJsArray.EMPTY
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
