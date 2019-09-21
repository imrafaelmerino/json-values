package jsonvalues;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.MatchExp.ifNothingElse;

final class MutableJsObj extends AbstractJsObj<MutableMap>
{
    private MutableJsons factory;

    MutableJsObj(final MutableMap map,
                 final MutableJsons factory
                )
    {
        super(map);
        this.factory = factory;
    }


    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final MutableMap map)
    {
        return new MutableJsObj(map,
                                factory
        );
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableObjElems(this).map(requireNonNull(fn),
                                                  p -> true,
                                                  JsPath.empty()
                                                 )
                                             .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapMutableObjElems(this).map(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                             .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapMutableObjElems(this).map_(requireNonNull(fn),
                                                   p -> true,
                                                   JsPath.empty()
                                                  )
                                             .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return new OpMapMutableObjElems(this).map_(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                  )
                                             .get();
    }


    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return new OpMapMutableObjKeys(this).map(requireNonNull(fn),
                                                 it -> true,
                                                 JsPath.empty()
                                                )
                                            .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return new OpMapMutableObjKeys(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                )
                                            .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapMutableObjKeys(this).map_(requireNonNull(fn),
                                                  it -> true,
                                                  JsPath.empty()
                                                 )
                                            .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return new OpMapMutableObjKeys(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                            .get();
    }


    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return new OpMapMutableObjObjs(this).map(requireNonNull(fn),
                                                 requireNonNull(predicate),
                                                 JsPath.empty()
                                                )
                                            .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableObjObjs(this).map(requireNonNull(fn),
                                                 (p, o) -> true,
                                                 JsPath.empty()
                                                )
                                            .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {
        return new OpMapMutableObjObjs(this).map_(requireNonNull(fn),
                                                  requireNonNull(predicate),
                                                  JsPath.empty()
                                                 )
                                            .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapMutableObjObjs(this).map_(requireNonNull(fn),
                                                  (p, o) -> true,
                                                  JsPath.empty()
                                                 )
                                            .get();
    }


    public JsObj copy()
    {
        return new MutableJsObj(map.copy(),
                                factory
        );
    }

    @Override
    public JsObj filterElems(final Predicate<? super JsPair> predicate)
    {
        return new OpFilterMutableObjElems(this).filter(JsPath.empty(),
                                                        predicate
                                                       )
                                                .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjElems(this).filter_(JsPath.empty(),
                                                         filter
                                                        )
                                                .get();
    }


    @Override
    public JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableObjObjs(this).filter(JsPath.empty(),
                                                       filter
                                                      )
                                               .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterMutableObjObjs(this).filter_(JsPath.empty(),
                                                        filter
                                                       )
                                               .get();
    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjKeys(this).filter(requireNonNull(filter))
                                               .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterMutableObjKeys(this).filter_(JsPath.empty(),
                                                        requireNonNull(filter)
                                                       )
                                               .get();

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
    public TryPatch<JsObj> patch(final JsArray arrayOps)
    {
        try
        {
            final List<OpPatch<JsObj>> ops = new Patch<JsObj>(arrayOps).ops;
            if (ops.isEmpty()) return new TryPatch<>(this);
            OpPatch<JsObj> head = ops.get(0);
            List<OpPatch<JsObj>> tail = ops.subList(1,
                                                    ops.size()
                                                   );
            JsObj copy = this.copy();
            TryPatch<JsObj> accPatch = head.apply(copy);
            for (OpPatch<JsObj> op : tail) accPatch = accPatch.flatMap(op::apply);
            return accPatch;
        }

        catch (PatchMalformed patchMalformed)
        {
            return new TryPatch<>(patchMalformed);

        }
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   map.update(head,
                                                                                              arr.appendAll(elems)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               el ->
                                                                               {
                                                                                   map.update(head,
                                                                                              factory.array.empty()
                                                                                                           .appendAll(elems)
                                                                                             );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                tail.head()
                                                                                                    .match(key -> factory.object.empty()
                                                                                                                                .appendAll(tail,
                                                                                                                                           elems
                                                                                                                                          ),
                                                                                                           index -> factory.array.empty()
                                                                                                                                 .appendAll(tail,
                                                                                                                                            elems
                                                                                                                                           )
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                map.get(head)
                                                                                                   .asJson()
                                                                                                   .appendAll(tail,
                                                                                                              elems
                                                                                                             )
                                                                                               );
                                                                                     return this;
                                                                                 }
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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   map.update(head,
                                                                                              arr.append(elem)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               el ->
                                                                               {
                                                                                   map.update(head,
                                                                                              factory.array.empty()
                                                                                                           .append(elem)
                                                                                             );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                tail.head()
                                                                                                    .match(key -> factory.object.empty()
                                                                                                                                .append(tail,
                                                                                                                                        elem
                                                                                                                                       ),
                                                                                                           index -> factory.array.empty()
                                                                                                                                 .append(tail,
                                                                                                                                         elem
                                                                                                                                        )
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                map.get(head)
                                                                                                   .asJson()
                                                                                                   .append(tail,
                                                                                                           elem
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 }

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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   map.update(head,
                                                                                              arr.prependAll(elems)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               el ->
                                                                               {
                                                                                   map.update(head,
                                                                                              factory.array.empty()
                                                                                                           .prependAll(elems)
                                                                                             );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                tail.head()
                                                                                                    .match(key -> factory.object.empty()
                                                                                                                                .prependAll(tail,
                                                                                                                                            elems
                                                                                                                                           ),
                                                                                                           index -> factory.array.empty()
                                                                                                                                 .prependAll(tail,
                                                                                                                                             elems
                                                                                                                                            )
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                map.get(head)
                                                                                                   .asJson()
                                                                                                   .prependAll(tail,
                                                                                                               elems
                                                                                                              )
                                                                                               );
                                                                                     return this;
                                                                                 }

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
                              return tail.ifEmptyElse(() -> MatchExp.ifArrElse(arr ->
                                                                               {
                                                                                   map.update(head,
                                                                                              arr.prepend(elem)
                                                                                             );
                                                                                   return this;
                                                                               },
                                                                               el ->
                                                                               {
                                                                                   map.update(head,
                                                                                              factory.array.empty()
                                                                                                           .prepend(elem)
                                                                                             );
                                                                                   return this;
                                                                               }
                                                                              )
                                                                    .apply(get(Key.of(head))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                tail.head()
                                                                                                    .match(key -> factory.object.empty()
                                                                                                                                .prepend(tail,
                                                                                                                                         elem
                                                                                                                                        ),
                                                                                                           index -> factory.array.empty()
                                                                                                                                 .prepend(tail,
                                                                                                                                          elem
                                                                                                                                         )
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                map.get(head)
                                                                                                   .asJson()
                                                                                                   .prepend(tail,
                                                                                                            elem
                                                                                                           )
                                                                                               );
                                                                                     return this;
                                                                                 }

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
                                                                          elem ->
                                                                          {
                                                                              map.update(head,
                                                                                         elem
                                                                                        );
                                                                              return this;
                                                                          }
                                                                         ).apply(fn.apply(get(path))),
                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                       t
                                                                                                                      ),
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                tail.head()
                                                                                                    .match(key -> factory.object.empty()
                                                                                                                                .put(tail,
                                                                                                                                     fn
                                                                                                                                    ),
                                                                                                           index -> factory.array.empty()
                                                                                                                                 .put(tail,
                                                                                                                                      fn
                                                                                                                                     )
                                                                                                          )
                                                                                               );
                                                                                     return this;
                                                                                 },
                                                                                 () ->
                                                                                 {
                                                                                     map.update(head,
                                                                                                map.get(head)
                                                                                                   .asJson()
                                                                                                   .put(tail,
                                                                                                        fn
                                                                                                       )
                                                                                               );
                                                                                     return this;
                                                                                 }

                                                                                )
                                                     );
                          },
                          index -> this

                         );

    }

    @Override
    public final JsObj add(final JsPath path,
                           final Function<? super JsElem, ? extends JsElem> fn
                          )
    {
        if (requireNonNull(path).isEmpty()) throw UserError.pathEmpty("add");
        final JsPath tail = path.tail();
        final Position head = path.head();
        return head.match(key -> tail.ifEmptyElse(() ->
                                                  {
                                                      map.update(key,
                                                                 fn.apply(get(head))
                                                                );
                                                      return this;
                                                  },
                                                  () ->
                                                  {
                                                      final JsElem headElem = get(head);

                                                      if (headElem.isNothing())
                                                          throw UserError.parentNotFound(JsPath.fromKey(key),
                                                                                         this,
                                                                                         "add"
                                                                                        );
                                                      if (!headElem.isJson())
                                                          throw UserError.parentIsNotAJson(JsPath.fromKey(key),
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

                                                      map.update(key,
                                                                 headElem.asJson()
                                                                         .add(tail,
                                                                              fn
                                                                             )
                                                                );
                                                      return this;
                                                  }
                                                 ),
                          index ->
                          {
                              throw UserError.addingIndexIntoObject(index,
                                                                    this,
                                                                    path,
                                                                    "add"
                                                                   );
                          }
                         );
    }

    @Override
    public final JsObj remove(final JsPath path)
    {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key ->
                          {
                              if (!map.contains(key)) return this;
                              final JsPath tail = path.tail();
                              return tail.ifEmptyElse(() ->
                                                      {
                                                          map.remove(key);
                                                          return this;
                                                      },
                                                      () -> MatchExp.ifJsonElse(json ->
                                                                                {
                                                                                    map.update(key,
                                                                                               json.remove(tail)
                                                                                              );
                                                                                    return this;
                                                                                },
                                                                                e -> this
                                                                               )
                                                                    .apply(map.get(key))
                                                     );
                          },
                          index -> this
                         );


    }


}
