package jsonvalues;

import jsonvalues.JsArray.TYPE;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static jsonvalues.AbstractJsArray.streamOfArr;
import static jsonvalues.JsNothing.NOTHING;


abstract class AbstractJsObj<T extends MyMap<T>, A extends JsArray> implements JsObj
{

    public static final long serialVersionUID = 1L;

    protected transient T map;

    AbstractJsObj(final T myMap)
    {
        assert myMap != null;
        this.map = myMap;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsObj appendAll(final JsPath path,
                           final JsArray elems
                          )
    {
        requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(head ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(map.update(head,
                                                                                                                     arr.appendAll(elems)
                                                                                                                    )),
                                                                                                el -> of(map.update(head,
                                                                                                                    emptyArray().appendAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Key.of(head))),
                                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                                       t
                                                                                                                                      ),
                                                                                                 () -> of(map.update(head,
                                                                                                                     tail.head()
                                                                                                                         .match(key -> emptyObject().appendAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              ),
                                                                                                                                index -> emptyArray().appendAll(tail,
                                                                                                                                                                elems
                                                                                                                                                               )
                                                                                                                               )
                                                                                                                    )),
                                                                                                 () -> of(map.update(head,
                                                                                                                     map.get(head)
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
    public JsObj append(final JsPath path,
                        final JsElem elem
                       )
    {
        requireNonNull(elem);
        return requireNonNull(path).head()
                                   .match(head ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(map.update(head,
                                                                                                                     arr.append(elem)
                                                                                                                    )),
                                                                                                el -> of(map.update(head,
                                                                                                                    emptyArray().append(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Key.of(head))),
                                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                                       t
                                                                                                                                      ),
                                                                                                 () -> of(map.update(head,
                                                                                                                     tail.head()
                                                                                                                         .match(key -> emptyObject().append(tail,
                                                                                                                                                            elem
                                                                                                                                                           ),
                                                                                                                                index -> emptyArray().append(tail,
                                                                                                                                                             elem
                                                                                                                                                            )
                                                                                                                               )
                                                                                                                    )),
                                                                                                 () -> of(map.update(head,
                                                                                                                     map.get(head)
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
    public JsObj prependAll(final JsPath path,
                            final JsArray elems
                           )
    {
        requireNonNull(elems);

        return requireNonNull(path).head()
                                   .match(head ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(map.update(head,
                                                                                                                     arr.prependAll(elems)
                                                                                                                    )),
                                                                                                el -> of(map.update(head,
                                                                                                                    emptyArray().prependAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Key.of(head))),
                                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                                       t
                                                                                                                                      ),
                                                                                                 () -> of(map.update(head,
                                                                                                                     tail.head()
                                                                                                                         .match(key -> emptyObject().prependAll(tail,
                                                                                                                                                                elems
                                                                                                                                                               ),
                                                                                                                                index -> emptyArray().prependAll(tail,
                                                                                                                                                                 elems
                                                                                                                                                                )
                                                                                                                               )
                                                                                                                    )),
                                                                                                 () -> of(map.update(head,
                                                                                                                     map.get(head)
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
    public JsObj prepend(final JsPath path,
                         final JsElem elem
                        )
    {
        requireNonNull(elem);

        return requireNonNull(path).head()
                                   .match(head ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(map.update(head,
                                                                                                                     arr.prepend(elem)
                                                                                                                    )),
                                                                                                el -> of(map.update(head,
                                                                                                                    emptyArray().prepend(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Key.of(head))),
                                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                                       t
                                                                                                                                      ),
                                                                                                 () -> of(map.update(head,
                                                                                                                     tail.head()
                                                                                                                         .match(key -> emptyObject().prepend(tail,
                                                                                                                                                             elem
                                                                                                                                                            ),
                                                                                                                                index -> emptyArray().prepend(tail,
                                                                                                                                                              elem
                                                                                                                                                             )
                                                                                                                               )
                                                                                                                    )),
                                                                                                 () -> of(map.update(head,
                                                                                                                     map.get(head)
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

    abstract A emptyArray();

    abstract JsObj emptyObject();

    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (!(that instanceof AbstractJsObj)) return false;
        final AbstractJsObj<?, ?> thatObj = (AbstractJsObj) that;
        return this.map.equals(thatObj.map);
    }

    @Override
    public final Set<String> fields()
    {
        return map.fields();
    }


    @Override
    public final JsElem get(final Position position)
    {

        return requireNonNull(position).match(key -> map.contains(key) ? map.get(key) : NOTHING,
                                              index -> NOTHING
                                             );

    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }

    @Override
    public final Map.Entry<String, JsElem> head()
    {
        return map.head();
    }

    @Override
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    public final JsObj intersection(final JsObj that,
                                    final TYPE ARRAY_AS
                                   )
    {
        requireNonNull(that);
        return Functions.intersection(this,
                                      that,
                                      ARRAY_AS
                                     )
                        .get();


    }

    @Override
    @SuppressWarnings({"squid:S00117","squid:S00100"}) //  ARRAY_AS should be a valid name, naming convention: _xx_ returns immutable object
    public final JsObj intersection_(final JsObj that,
                                     final TYPE ARRAY_AS
                                    )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return Functions.intersection_(this,
                                       that,
                                       ARRAY_AS
                                      )
                        .get();

    }

    @Override
    public final boolean isEmpty()
    {
        return map.isEmpty();
    }


    abstract JsObj of(T map);

    @Override
    public final JsObj put(final JsPath path,
                           final Function<? super JsElem, ? extends JsElem> fn
                          )
    {
        requireNonNull(fn);

        return requireNonNull(path).head()
                                   .match(head ->
                                          {
                                              final JsPath tail = path.tail();

                                              return tail.ifEmptyElse(() -> Functions.ifNothingElse(() -> this,
                                                                                                    elem -> of(map.update(head,
                                                                                                                          elem
                                                                                                                         ))
                                                                                                   )
                                                                                     .apply(fn.apply(get(path))),
                                                                      () -> tail.ifPredicateElse(t -> isReplaceWithEmptyJson(map).test(head,
                                                                                                                                       t
                                                                                                                                      ),
                                                                                                 () -> of(map.update(head,
                                                                                                                     tail.head()
                                                                                                                         .match(key -> emptyObject().put(tail,
                                                                                                                                                         fn
                                                                                                                                                        ),
                                                                                                                                index -> emptyArray().put(tail,
                                                                                                                                                          fn
                                                                                                                                                         )
                                                                                                                               )
                                                                                                                    )),
                                                                                                 () -> of(map.update(head,
                                                                                                                     map.get(head)
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
    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       )
    {
        return Functions.reduce(this,
                                requireNonNull(op),
                                requireNonNull(map),
                                requireNonNull(predicate),
                                JsPath.empty(),
                                Optional.empty()
                               )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final <R> Optional<R> reduce_(final BinaryOperator<R> op,
                                         final Function<? super JsPair, R> map,
                                         final Predicate<? super JsPair> predicate
                                        )
    {
        return Functions.reduce_(this,
                                 requireNonNull(op),
                                 requireNonNull(map),
                                 requireNonNull(predicate),
                                 JsPath.empty(),
                                 Optional.empty()
                                )
                        .get();
    }

    @Override
    public final JsObj remove(final JsPath path)
    {

        return requireNonNull(path).head()
                                   .match(key ->
                                          {
                                              if (!map.contains(key)) return this;
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> of(map.remove(key)),
                                                                      () -> Functions.ifJsonElse(json -> of(map.update(key,
                                                                                                                       json.remove(tail)
                                                                                                                      )),
                                                                                                 e -> this
                                                                                                )
                                                                                     .apply(map.get(key))
                                                                     );
                                          },
                                          index -> this
                                         );


    }

    @Override
    public final int size()
    {
        return map.size();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public Stream<JsPair> stream_()
    {
        return streamOfObj(this,
                           JsPath.empty()
                          );
    }

    @Override
    public Stream<JsPair> stream()
    {
        return this.fields()
                   .stream()
                   .flatMap(f -> Stream.of(JsPair.of(JsPath.of(f),
                                                     this.get(f)
                                                    )
                                          )
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
                                               () -> obj.fields()
                                                        .stream()
                                                        .map(key -> JsPair.of(path.key(key),
                                                                              obj.get(Key.of(key))
                                                                             ))
                                                        .flatMap(pair -> Functions.ifValueElse(e -> Stream.of(pair),
                                                                                               o -> streamOfObj(o,
                                                                                                                pair.path
                                                                                                               ),
                                                                                               a -> streamOfArr(a,
                                                                                                                pair.path
                                                                                                               )
                                                                                              )
                                                                                  .apply(pair.elem))
                                              );

    }


    @Override
    public final JsObj tail(final String head)
    {
        return of(map.tail(head));
    }

    @Override
    public String toString()
    {
        return map.toString();

    }

    @Override
    public final JsObj union(final JsObj that
                            )
    {
        return Functions.union(this,
                               requireNonNull(that)
                              )
                        .get();

    }

    @Override
    public boolean containsElem(final JsElem el)
    {
        return stream().anyMatch(p -> p.elem.equals(Objects.requireNonNull(el)));
    }

    @Override
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) // ARRAY_AS  should be a valid name, naming convention: xx_ traverses the whole json
    public final JsObj union_(final JsObj that,
                              final TYPE ARRAY_AS
                             )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return ifEmptyElse(() -> that,
                           () -> that.ifEmptyElse(() -> this,
                                                  () -> Functions.union_(this,
                                                                         that,
                                                                         ARRAY_AS
                                                                        )
                                                                 .get()
                                                 )
                          );

    }

    @SuppressWarnings("squid:S1602") // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<String, JsPath> isReplaceWithEmptyJson(final MyMap<?> pmap)
    {

        return (head, tail) ->
        {
            return (!pmap.contains(head) || pmap.get(head)
                                                .isNotJson())
            ||
            (
            (tail.head()
                 .isKey() && pmap.get(head)
                                 .isArray()
            )
            ||
            (tail.head()
                 .isIndex() && pmap.get(head)
                                   .isObj()
            )
            );


        };
    }


}
