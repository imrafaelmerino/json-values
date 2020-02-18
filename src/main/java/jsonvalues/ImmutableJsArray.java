package jsonvalues;

import io.vavr.collection.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;


final class ImmutableJsArray extends AbstractJsArray
{

    final static ImmutableJsArray EMPTY = new ImmutableJsArray(Vector.empty());

    private volatile int hascode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recommendation from Effective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;

    ImmutableJsArray(final Vector<JsElem> array
                    )
    {
        super(array);
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
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = super.toString();
        return result;

    }

    @Override
    public final JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapArrElems(this).map(requireNonNull(fn),
                                           p -> true,
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                      .get();

    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn,
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

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapArrElems(this).map_(requireNonNull(fn),
                                            p -> true,
                                            JsPath.empty()
                                                  .index(-1)
                                           )
                                      .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapArrElems(this).map_(requireNonNull(fn),
                                            requireNonNull(predicate),
                                            JsPath.empty()
                                                  .index(-1)
                                           )
                                      .get();
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn)
    {
        return this;
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapArrKeys(this).map_(requireNonNull(fn),
                                           it -> true,
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                     .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return new OpMapArrKeys(this).map_(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                     .get();

    }


    @Override
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


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapArrObjs(this).map(requireNonNull(fn),
                                          (p, o) -> true,
                                          JsPath.empty()
                                                .index(-1)
                                         )
                                     .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return new OpMapArrObjs(this).map_(requireNonNull(fn),
                                           requireNonNull(predicate),
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                     .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapArrObjs(this).map_(requireNonNull(fn),
                                           (p, o) -> true,
                                           JsPath.empty()
                                                 .index(-1)
                                          )
                                     .get();
    }


    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrElems(this).filter(JsPath.empty()
                                                       .index(-1),
                                                 requireNonNull(filter)
                                                )

                                         .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrElems(this).filter_(JsPath.empty()
                                                        .index(-1),
                                                  requireNonNull(filter)
                                                 )

                                         .get();
    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterArrObjs(this).filter(JsPath.empty()
                                                      .index(-1),
                                                requireNonNull(filter)
                                               )

                                        .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterArrObjs(this).filter_(JsPath.empty()
                                                       .index(-1),
                                                 requireNonNull(filter)
                                                )
                                        .get();
    }


    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterArrKeys(this).filter_(JsPath.empty()
                                                       .index(-1),
                                                 filter
                                                )
                                        .get();
    }

    @SuppressWarnings("Duplicates")
    @Override
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsArray(seq.update(index,
                                                                                                                                      arr.appendAll(elems)
                                                                                                                                     )),
                                                                                               e -> new ImmutableJsArray(nullPadding(index,
                                                                                                                                     seq,
                                                                                                                                     ImmutableJsArray.EMPTY
                                                                                                                                                  .appendAll(elems)
                                                                                                                                    ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> new ImmutableJsArray(nullPadding(index,
                                                                                                                                        seq,
                                                                                                                                        tail.head()
                                                                                                                                            .match(o -> ImmutableJsObj.EMPTY
                                                                                                                                                                      .appendAll(tail,
                                                                                                                                                                                 elems
                                                                                                                                                                                ),
                                                                                                                                                   a -> ImmutableJsArray.EMPTY
                                                                                                                                                                     .appendAll(tail,
                                                                                                                                                                                elems
                                                                                                                                                                               )
                                                                                                                                                  )
                                                                                                                                       )),
                                                                                                 () -> new ImmutableJsArray(seq.update(index,
                                                                                                                                       seq.get(index)
                                                                                                                                          .asJson()
                                                                                                                                          .appendAll(tail,
                                                                                                                                                     elems
                                                                                                                                                    )
                                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @Override
    public final JsArray append(final JsPath path,
                                final JsElem elem
                               )
    {
        if (requireNonNull(path).isEmpty()) return this;
        Objects.requireNonNull(elem);
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsArray(seq.update(index,
                                                                                                                      arr.append(elem)
                                                                                                                     )),
                                                                               e -> new ImmutableJsArray(nullPadding(index,
                                                                                                                     seq,
                                                                                                                     ImmutableJsArray.EMPTY
                                                                                                                                  .append(elem)
                                                                                                                    ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> new ImmutableJsArray(nullPadding(index,
                                                                                                                        seq,
                                                                                                                        tail.head()
                                                                                                                            .match(o -> ImmutableJsObj.EMPTY
                                                                                                                                                      .append(tail,
                                                                                                                                                              elem
                                                                                                                                                             ),
                                                                                                                                   a -> ImmutableJsArray.EMPTY
                                                                                                                                                     .append(tail,
                                                                                                                                                             elem
                                                                                                                                                            )
                                                                                                                                  )
                                                                                                                       )),
                                                                                 () -> new ImmutableJsArray(seq.update(index,
                                                                                                                       seq.get(index)
                                                                                                                          .asJson()
                                                                                                                          .append(tail,
                                                                                                                                  elem
                                                                                                                                 )
                                                                                                                      ))
                                                                                )


                                                     );
                          }

                         );

    }

    @SuppressWarnings("Duplicates")
    @Override
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsArray(seq.update(index,
                                                                                                                                      arr.prependAll(elems)
                                                                                                                                     )),
                                                                                               e -> new ImmutableJsArray(nullPadding(index,
                                                                                                                                     seq,
                                                                                                                                     ImmutableJsArray.EMPTY
                                                                                                                                                  .prependAll(elems)
                                                                                                                                    ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> new ImmutableJsArray(nullPadding(index,
                                                                                                                                        seq,
                                                                                                                                        tail.head()
                                                                                                                                            .match(o -> ImmutableJsObj.EMPTY
                                                                                                                                                                      .prependAll(tail,
                                                                                                                                                                                  elems
                                                                                                                                                                                 ),
                                                                                                                                                   a -> ImmutableJsArray.EMPTY
                                                                                                                                                                     .prependAll(tail,
                                                                                                                                                                                 elems
                                                                                                                                                                                )
                                                                                                                                                  )

                                                                                                                                       )),
                                                                                                 () -> new ImmutableJsArray(seq.update(index,
                                                                                                                                       seq.get(index)
                                                                                                                                          .asJson()
                                                                                                                                          .prependAll(tail,
                                                                                                                                                      elems
                                                                                                                                                     )
                                                                                                                                      ))
                                                                                                )


                                                                     );
                                          }

                                         );

    }

    @SuppressWarnings("Duplicates")
    @Override
    public final JsArray prepend(final JsPath path,
                                 final JsElem elem
                                )
    {
        Objects.requireNonNull(elem);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key -> this,
                          index ->
                          {
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> new ImmutableJsArray(seq.update(index,
                                                                                                                      arr.prepend(elem)
                                                                                                                     )),
                                                                               e -> new ImmutableJsArray(nullPadding(index,
                                                                                                                     seq,
                                                                                                                     ImmutableJsArray.EMPTY
                                                                                                                                  .prepend(elem)
                                                                                                                    ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> new ImmutableJsArray(nullPadding(index,
                                                                                                                        seq,
                                                                                                                        tail.head()
                                                                                                                            .match(o -> ImmutableJsObj.EMPTY
                                                                                                                                                      .prepend(tail,
                                                                                                                                                               elem
                                                                                                                                                              ),
                                                                                                                                   a -> ImmutableJsArray.EMPTY
                                                                                                                                                     .prepend(tail,
                                                                                                                                                              elem
                                                                                                                                                             )
                                                                                                                                  )

                                                                                                                       )),
                                                                                 () -> new ImmutableJsArray(seq.update(index,
                                                                                                                       seq.get(index)
                                                                                                                          .asJson()
                                                                                                                          .prepend(tail,
                                                                                                                                   elem
                                                                                                                                  )
                                                                                                                      ))
                                                                                )


                                                     );
                          }

                         );

    }

    @Override
    public final JsArray put(final JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
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
                                                                          elem -> new ImmutableJsArray(nullPadding(index,
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
                                                                                     final JsElem newElem = tail.head()
                                                                                                                .match(key -> ImmutableJsObj.EMPTY
                                                                                                                                            .put(tail,
                                                                                                                                                 fn
                                                                                                                                                ),
                                                                                                                       i -> ImmutableJsArray.EMPTY
                                                                                                                                         .put(tail,
                                                                                                                                              fn
                                                                                                                                             )
                                                                                                                      );
                                                                                     return new ImmutableJsArray(nullPadding(index,
                                                                                                                             seq,
                                                                                                                             newElem
                                                                                                                            ));
                                                                                 },
                                                                                 () -> new ImmutableJsArray(seq.update(index,
                                                                                                                       seq.get(index)
                                                                                                                          .asJson()
                                                                                                                          .put(tail,
                                                                                                                               fn
                                                                                                                              )
                                                                                                                      ))

                                                                                )
                                                     );

                          }

                         );

    }

    @Override
    public final JsArray append(final JsElem e,
                                final JsElem... others
                               )
    {
        Vector<JsElem> acc = this.seq.append(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) acc = acc.append(requireNonNull(other));
        return new ImmutableJsArray(acc);
    }

    @Override
    public final JsArray prepend(final JsElem e,
                                 final JsElem... others
                                )
    {
        Vector<JsElem> acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            acc = acc.prepend(requireNonNull(other));
        }
        return new ImmutableJsArray(acc.prepend(requireNonNull(e)));
    }



    @Override
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
                              return tail.ifEmptyElse(() -> new ImmutableJsArray(index == -1 ? seq.removeAt(maxIndex) : seq.removeAt(index)),
                                                      () -> ifJsonElse(json -> new ImmutableJsArray(seq.update(index,
                                                                                                               json.remove(tail)
                                                                                                              )),
                                                                       e -> this
                                                                      )
                                                      .apply(seq.get(index))
                                                     );
                          }

                         );


    }

    private Trampoline<Vector<JsElem>> nullPaddingTrampoline(final int i,
                                                             final Vector<JsElem> arr,
                                                             final JsElem e
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

    private Vector<JsElem> nullPadding(final int index,
                                       final Vector<JsElem> arr,
                                       final JsElem e
                                      )
    {
        assert arr != null;
        assert e != null;

        return nullPaddingTrampoline(index,
                                     arr,
                                     e
                                    ).get();
    }

}
