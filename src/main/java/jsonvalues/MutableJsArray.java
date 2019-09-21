package jsonvalues;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;

final class MutableJsArray extends AbstractJsArray<MutableSeq>
{

    private MutableJsons factory;

    MutableJsArray(final MutableSeq array,
                   final MutableJsons factory
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
        seq.add(index,
                elem
               );
        return of(seq);
    }


    @Override
    JsArray of(final MutableSeq vector)
    {
        return new MutableJsArray(vector,
                                  factory
        );
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)

    {
        return new OpMapMutableArrElems(this).map(requireNonNull(fn),
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
        return new OpMapMutableArrElems(this).map(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                             .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableArrElems(this).map_(requireNonNull(fn),
                                                   p -> true,
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                             .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapMutableArrElems(this).map_(requireNonNull(fn),
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
        return new OpMapMutableArrKeys(this).map_(requireNonNull(fn),
                                                  it -> true,
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return new OpMapMutableArrKeys(this).map_(requireNonNull(fn),
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

        return new OpMapMutableArrObjs(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                       .index(-1)
                                                )
                                            .get();

    }

    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableArrObjs(this).map(requireNonNull(fn),
                                                 (p, o) -> true,
                                                 JsPath.empty()
                                                       .index(-1)
                                                )
                                            .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return new OpMapMutableArrObjs(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableArrObjs(this).map_(requireNonNull(fn),
                                                  (p, o) -> true,
                                                  JsPath.empty()
                                                        .index(-1)
                                                 )
                                            .get();

    }


    @Override
    public JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableArrElems(this).filter(JsPath.empty()
                                                              .index(-1),
                                                        filter
                                                       )
                                                .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableArrElems(this).filter_(JsPath.empty()
                                                               .index(-1),
                                                         filter
                                                        )
                                                .get();
    }


    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableArrObjs(this).filter(JsPath.empty()
                                                             .index(-1),
                                                       filter
                                                      )
                                               .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableArrObjs(this).filter_(JsPath.empty()
                                                              .index(-1),
                                                        filter
                                                       )
                                               .get();
    }

    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableArrKeys(this).filter_(JsPath.empty()
                                                              .index(-1),
                                                        requireNonNull(filter)
                                                       )
                                               .get();

    }


    public MutableJsArray copy()
    {
        return new MutableJsArray(seq.copy(),
                                  factory
        );
    }


    @Override
    public boolean isMutable()
    {
        return true;
    }

    @Override
    public boolean isImmutable()
    {
        return false;
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
            JsArray copy = this.copy();
            TryPatch<JsArray> accPatch = head.apply(copy);
            for (OpPatch<JsArray> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                                               {
                                                                                                   seq.update(index,
                                                                                                              arr.appendAll(elems)
                                                                                                             );
                                                                                                   return this;
                                                                                               },
                                                                                               e ->
                                                                                               {
                                                                                                   nullPadding(index,
                                                                                                               factory.array.empty()
                                                                                                                            .appendAll(elems)
                                                                                                              );
                                                                                                   return this;
                                                                                               }
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () ->
                                                                                                 {
                                                                                                     nullPadding(index,
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
                                                                                                                );
                                                                                                     return this;
                                                                                                 },
                                                                                                 () ->
                                                                                                 {
                                                                                                     seq.update(index,
                                                                                                                seq.get(index)
                                                                                                                   .asJson()
                                                                                                                   .appendAll(tail,
                                                                                                                              elems
                                                                                                                             )
                                                                                                               );
                                                                                                     return this;
                                                                                                 }
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   seq.update(index,
                                                                                              arr.append(elem)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               e ->
                                                                               {
                                                                                   nullPadding(index,
                                                                                               factory.array.empty()
                                                                                                            .append(elem)
                                                                                              );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () ->
                                                                                 {
                                                                                     nullPadding(index,
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
                                                                                                );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     seq.update(index,
                                                                                                seq.get(index)
                                                                                                   .asJson()
                                                                                                   .append(tail,
                                                                                                           elem
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 }
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
                                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                                               {
                                                                                                   seq.update(index,
                                                                                                              arr.prependAll(elems)
                                                                                                             );
                                                                                                   return this;
                                                                                               },
                                                                                               e ->
                                                                                               {
                                                                                                   nullPadding(index,
                                                                                                               factory.array.empty()
                                                                                                                            .prependAll(elems)
                                                                                                              );
                                                                                                   return this;
                                                                                               }
                                                                                              )
                                                                                    .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                                             t
                                                                                                                            ),
                                                                                                 () ->
                                                                                                 {
                                                                                                     nullPadding(index,
                                                                                                                 tail.head()
                                                                                                                     .match(o -> factory.array.empty()
                                                                                                                                              .prependAll(tail,
                                                                                                                                                          elems
                                                                                                                                                         ),
                                                                                                                            a -> factory.array.empty()
                                                                                                                                              .prependAll(tail,
                                                                                                                                                          elems
                                                                                                                                                         )
                                                                                                                           )

                                                                                                                );
                                                                                                     return this;
                                                                                                 },
                                                                                                 () ->
                                                                                                 {
                                                                                                     seq.update(index,
                                                                                                                seq.get(index)
                                                                                                                   .asJson()
                                                                                                                   .prependAll(tail,
                                                                                                                               elems
                                                                                                                              )
                                                                                                               );
                                                                                                     return this;
                                                                                                 }
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   seq.update(index,
                                                                                              arr.prepend(elem)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               e ->
                                                                               {
                                                                                   nullPadding(index,
                                                                                               factory.array.empty()
                                                                                                            .prepend(elem)
                                                                                              );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Index.of(index))),
                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(seq).test(index,
                                                                                                             t
                                                                                                            ),
                                                                                 () ->
                                                                                 {
                                                                                     nullPadding(index,
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

                                                                                                );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     seq.update(index,
                                                                                                seq.get(index)
                                                                                                   .asJson()
                                                                                                   .prepend(tail,
                                                                                                            elem
                                                                                                           )
                                                                                               );
                                                                                     return this;
                                                                                 }
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
                                                                          elem ->
                                                                          {
                                                                              nullPadding(index,
                                                                                          elem
                                                                                         );
                                                                              return this;
                                                                          }
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
                                                                                     nullPadding(index,
                                                                                                 newElem
                                                                                                );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     seq.update(index,
                                                                                                seq.get(index)
                                                                                                   .asJson()
                                                                                                   .put(tail,
                                                                                                        fn
                                                                                                       )
                                                                                               );
                                                                                     return this;
                                                                                 }

                                                                                )
                                                     );

                          }

                         );

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
                          index -> tail.ifEmptyElse(() ->
                                                    {
                                                        seq.add(index,
                                                                fn.apply(get(head))
                                                               );
                                                        return this;
                                                    },
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

                                                        seq.update(index,
                                                                   headElem.asJson()
                                                                           .add(tail,
                                                                                fn
                                                                               )
                                                                  );
                                                        return this;
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
                              return tail.ifEmptyElse(() ->
                                                      {
                                                          if (index == -1) seq.remove(maxIndex);
                                                          else seq.remove(index);
                                                          return this;
                                                      },
                                                      () -> ifJsonElse(json ->
                                                                       {
                                                                           seq.update(index,
                                                                                      json.remove(tail)
                                                                                     );
                                                                           return this;
                                                                       },
                                                                       e -> this
                                                                      )
                                                      .apply(seq.get(index))
                                                     );
                          }

                         );


    }

    private void nullPadding(final int i,
                             final JsElem e
                            )
    {
        if (i == seq.size()) seq.appendBack(e);
        else if (i == -1) seq.update(seq.size() - 1,
                                     e
                                    );
        else if (i < seq.size()) seq.update(i,
                                            e
                                           );
        else
        {
            for (int j = seq.size(); j < i; j++) seq.appendBack(JsNull.NULL);
            seq.appendBack(e);
        }
    }


    @Override
    public final JsArray append(final JsElem e,
                                final JsElem... others
                               )
    {
        seq.appendBack(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) seq.appendBack(requireNonNull(other));
        return this;
    }

    @Override
    public final JsArray prepend(final JsElem e,
                                 final JsElem... others
                                )
    {
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            seq.appendFront(requireNonNull(other));
        }
        seq.appendFront(requireNonNull(e));
        return this;
    }


}
