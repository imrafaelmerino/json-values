package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;


final class ImmutableJsArray extends AbstractJsArray<ImmutableSeq>
{

    private ImmutableJsons factory;

    private volatile int hascode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recommendation from Effective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private volatile String str;

    ImmutableJsArray(final ImmutableSeq array,
                     final ImmutableJsons factory
                    )
    {
        super(array);
        this.factory = factory;
    }


    @Override
    public JsArray add(final int index,
                       final JsElem elem
                      )
    {

        return new ImmutableJsArray(seq.add(index,
                                            elem
                                           ),
                                    this.factory
        );
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
    JsArray of(final ImmutableSeq vector)
    {
        return new ImmutableJsArray(vector,
                                    this.factory
        );
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public boolean isImmutable()
    {
        return true;
    }


    @Override
    public TryPatch<JsArray> patch(final JsArray arrayOps)
    {
        try
        {
            final List<OpPatch<JsArray>> ops = new Patch<JsArray>(arrayOps).ops;
            if (ops.isEmpty()) return new TryPatch<>(this);
            OpPatch<JsArray> head = ops.get(0);
            List<OpPatch<JsArray>> tail = ops.subList(1,
                                                      ops.size()
                                                     );
            TryPatch<JsArray> accPatch = head.apply(this);
            for (OpPatch<JsArray> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
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
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
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
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                               .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
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
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
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
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
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
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
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

        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                              .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
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
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    (p, o) -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }


    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter(JsPath.empty()
                                                                .index(-1),
                                                          requireNonNull(filter)
                                                         )

                                                  .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter_(JsPath.empty()
                                                                 .index(-1),
                                                           requireNonNull(filter)
                                                          )

                                                  .get();
    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter(JsPath.empty()
                                                               .index(-1),
                                                         requireNonNull(filter)
                                                        )

                                                 .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter_(JsPath.empty()
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
        return new OpFilterImmutableArrKeys(this).filter_(JsPath.empty()
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                                    arr.appendAll(elems)
                                                                                                                   )),
                                                                                               e -> of(nullPadding(index,
                                                                                                                   seq,
                                                                                                                   factory.array.empty()
                                                                                                                                .appendAll(elems)
                                                                                                                  ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      seq,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> factory.object.empty()
                                                                                                                                                    .appendAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              ),
                                                                                                                                 a -> factory.array.empty()
                                                                                                                                                   .appendAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(seq.update(index,
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                    arr.append(elem)
                                                                                                   )),
                                                                               e -> of(nullPadding(index,
                                                                                                   seq,
                                                                                                   factory.array.empty()
                                                                                                                .append(elem)
                                                                                                  ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> of(nullPadding(index,
                                                                                                      seq,
                                                                                                      tail.head()
                                                                                                          .match(o -> factory.object.empty()
                                                                                                                                    .append(tail,
                                                                                                                                            elem
                                                                                                                                           ),
                                                                                                                 a -> factory.array.empty()
                                                                                                                                   .append(tail,
                                                                                                                                           elem
                                                                                                                                          )
                                                                                                                )
                                                                                                     )),
                                                                                 () -> of(seq.update(index,
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                                    arr.prependAll(elems)
                                                                                                                   )),
                                                                                               e -> of(nullPadding(index,
                                                                                                                   seq,
                                                                                                                   factory.array.empty()
                                                                                                                                .prependAll(elems)
                                                                                                                  ))
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      seq,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> factory.object.empty()
                                                                                                                                                    .prependAll(tail,
                                                                                                                                                                elems
                                                                                                                                                               ),
                                                                                                                                 a -> factory.array.empty()
                                                                                                                                                   .prependAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(seq.update(index,
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr -> of(seq.update(index,
                                                                                                    arr.prepend(elem)
                                                                                                   )),
                                                                               e -> of(nullPadding(index,
                                                                                                   seq,
                                                                                                   factory.array.empty()
                                                                                                                .prepend(elem)
                                                                                                  ))
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () -> of(nullPadding(index,
                                                                                                      seq,
                                                                                                      tail.head()
                                                                                                          .match(o -> factory.object.empty()
                                                                                                                                    .prepend(tail,
                                                                                                                                             elem
                                                                                                                                            ),
                                                                                                                 a -> factory.array.empty()
                                                                                                                                   .prepend(tail,
                                                                                                                                            elem
                                                                                                                                           )
                                                                                                                )

                                                                                                     )),
                                                                                 () -> of(seq.update(index,
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
                                                                          elem -> of(nullPadding(index,
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
                                                                                                                .match(key -> factory.object.empty()
                                                                                                                                            .put(tail,
                                                                                                                                                 fn
                                                                                                                                                ),
                                                                                                                       i -> factory.array.empty()
                                                                                                                                         .put(tail,
                                                                                                                                              fn
                                                                                                                                             )
                                                                                                                      );
                                                                                     return of(nullPadding(index,
                                                                                                           seq,
                                                                                                           newElem
                                                                                                          ));
                                                                                 },
                                                                                 () -> of(seq.update(index,
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
        ImmutableSeq acc = this.seq.appendBack(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) acc = acc.appendBack(requireNonNull(other));
        return of(acc);
    }

    @Override
    public final JsArray prepend(final JsElem e,
                                 final JsElem... others
                                )
    {
        ImmutableSeq acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            acc = acc.appendFront(requireNonNull(other));
        }
        return of(acc.appendFront(requireNonNull(e)));
    }

    @Override
    public final JsArray add(JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
                            )
    {
        if (requireNonNull(path).isEmpty()) throw UserError.pathEmpty("add");
        final JsPath tail = path.tail();
        final Position head = path.head();
        return head.match(key ->
                          {
                              throw UserError.addingKeyIntoArray(key,
                                                                 this,
                                                                 path,
                                                                 "add"
                                                                );
                          },
                          index -> tail.ifEmptyElse(() -> of(seq.add(index,
                                                                     fn.apply(get(head))
                                                                    )),
                                                    () ->
                                                    {
                                                        final JsElem headElem = get(head);

                                                        if (headElem.isNothing())
                                                            throw UserError.parentNotFound(JsPath.fromIndex(index),
                                                                                           this,
                                                                                           "add"
                                                                                          );
                                                        if (!headElem.isJson())
                                                            throw UserError.parentIsNotAJson(JsPath.fromIndex(index),
                                                                                             this,
                                                                                             path,
                                                                                             "add"
                                                                                            );
                                                        if (headElem.isObj() && tail.head()
                                                                                    .isIndex())
                                                            throw UserError.addingIndexIntoObject(tail.head()
                                                                                                      .asIndex().n,
                                                                                                  this,
                                                                                                  path,
                                                                                                  "add"
                                                                                                 );
                                                        if (headElem.isArray() && tail.head()
                                                                                      .isKey())
                                                            throw UserError.addingKeyIntoArray(tail.head()
                                                                                                   .asKey().name,
                                                                                               this,
                                                                                               path,
                                                                                               "add"
                                                                                              );


                                                        return of(seq.update(index,
                                                                             headElem.asJson()
                                                                                     .add(tail,
                                                                                          fn
                                                                                         )
                                                                            ));
                                                    }
                                                   )


                         );
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
                              return tail.ifEmptyElse(() -> of(index == -1 ? seq.remove(maxIndex) : seq.remove(index)),
                                                      () -> ifJsonElse(json -> of(seq.update(index,
                                                                                             json.remove(tail)
                                                                                            )),
                                                                       e -> this
                                                                      )
                                                      .apply(seq.get(index))
                                                     );
                          }

                         );


    }

    private Trampoline<ImmutableSeq> nullPaddingTrampoline(final int i,
                                                           final ImmutableSeq arr,
                                                           final JsElem e
                                                          )
    {

        if (i == arr.size()) return Trampoline.done(arr.appendBack(e));

        if (i == -1) return Trampoline.done(arr.update(seq.size() - 1,
                                                       e
                                                      ));

        if (i < arr.size()) return Trampoline.done(arr.update(i,
                                                              e
                                                             ));
        return Trampoline.more(() -> nullPaddingTrampoline(i,
                                                           arr.appendBack(JsNull.NULL),
                                                           e
                                                          ));
    }

    private ImmutableSeq nullPadding(final int index,
                                     final ImmutableSeq arr,
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
