package jsonvalues;


import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.AbstractJsObj.streamOfObj;


abstract class AbstractJsArray<T extends MyVector<T>, O extends JsObj> implements JsArray

{
    public static final long serialVersionUID = 1L;

    @SuppressWarnings("squid:S00116") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private final transient JsPath MINUS_ONE_PATH = JsPath.empty()
                                                          .index(-1);

    protected transient T array;

    AbstractJsArray(T array)
    {
        this.array = array;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsArray appendAll(final JsPath path,
                             final JsArray elems

                            )
    {

        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.appendAll(elems)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().appendAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().appendAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             ),
                                                                                                                                 a -> emptyArray().appendAll(tail,
                                                                                                                                                             elems
                                                                                                                                                            )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
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
    public JsArray append(final JsPath path,
                          final JsElem elem
                         )
    {

        Objects.requireNonNull(elem);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.append(elem)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().append(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().append(tail,
                                                                                                                                                           elem
                                                                                                                                                          ),
                                                                                                                                 a -> emptyArray().append(tail,
                                                                                                                                                          elem
                                                                                                                                                         )
                                                                                                                                )
                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
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

    @Override
    public JsArray appendAll(final JsArray array
                            )
    {
        return appendAllBackTrampoline(this,
                                       requireNonNull(array)
                                      ).get();

    }

    @Override
    public JsArray prependAll(final JsArray array
                             )
    {
        return appendAllFrontTrampoline(this,
                                        requireNonNull(array)
                                       ).get();

    }

    @SuppressWarnings("Duplicates")
    @Override
    public JsArray prependAll(final JsPath path,
                              final JsArray elems
                             )
    {
        Objects.requireNonNull(elems);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.prependAll(elems)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().prependAll(elems)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().prependAll(tail,
                                                                                                                                                               elems
                                                                                                                                                              ),
                                                                                                                                 a -> emptyArray().prependAll(tail,
                                                                                                                                                              elems
                                                                                                                                                             )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
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
    public JsArray prepend(final JsPath path,
                           final JsElem elem
                          )
    {
        Objects.requireNonNull(elem);
        return requireNonNull(path).head()
                                   .match(key -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> Functions.ifArrElse(arr -> of(array.update(index,
                                                                                                                       arr.prepend(elem)
                                                                                                                      )),
                                                                                                e -> of(nullPadding(index,
                                                                                                                    array,
                                                                                                                    emptyArray().prepend(elem)
                                                                                                                   ))
                                                                                               )
                                                                                     .apply(get(Index.of(index))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () -> of(nullPadding(index,
                                                                                                                      array,
                                                                                                                      tail.head()
                                                                                                                          .match(o -> emptyObject().prepend(tail,
                                                                                                                                                            elem
                                                                                                                                                           ),
                                                                                                                                 a -> emptyArray().prepend(tail,
                                                                                                                                                           elem
                                                                                                                                                          )
                                                                                                                                )

                                                                                                                     )),
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
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
    public JsArray append(final JsElem e,
                          final JsElem... others
                         )
    {
        T vector = array.appendBack(requireNonNull(e));
        for (JsElem other : requireNonNull(others)) vector = vector.appendBack(requireNonNull(other));
        return of(vector);
    }

    @Override
    public JsArray prepend(final JsElem e,
                           final JsElem... others
                          )
    {
        T vector = array;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++)
        {
            final JsElem other = others[othersLength - 1 - i];
            vector = vector.appendFront(requireNonNull(other));
        }
        return of(vector.appendFront(requireNonNull(e)));
    }

    @Override
    public boolean containsElem(final JsElem el)
    {
        return array.contains(requireNonNull(el));
    }

    abstract JsArray emptyArray();

    abstract O emptyObject();

    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (!(that instanceof AbstractJsArray)) return false;
        final AbstractJsArray<?, ?> thatArray = (AbstractJsArray) that;
        return this.array.equals(thatArray.array);

    }


    @Override
    public final JsElem get(final Position pos)
    {


        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                         {
                                             if (index == -1 && !array.isEmpty()) return array.last();
                                             return (array.isEmpty() || index < 0 || index > array.size() - 1) ?
                                             JsNothing.NOTHING : array.get(index);
                                         }
                                        );


    }

    @Override
    public int hashCode()
    {
        return array.hashCode();
    }

    @Override
    public final JsElem head()
    {

        return array.head();

    }

    @Override
    public final JsArray init()
    {
        return of(array.init());

    }

    @Override
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    public final JsArray intersection(final JsArray that,
                                      final TYPE ARRAY_AS
                                     )
    {
        return Functions.intersection(this,
                                      requireNonNull(that),
                                      requireNonNull(ARRAY_AS)
                                     )
                        .get();


    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public JsArray intersection_(final JsArray that)
    {
        return Functions.intersection_(this,
                                       requireNonNull(that)
                                      )
                        .get();
    }

    @Override
    public final boolean isEmpty()
    {
        return array.isEmpty();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        return array.iterator();
    }

    @Override
    public final JsElem last()
    {

        return array.last();
    }


    abstract JsArray of(T vector);

    @Override
    public final JsArray put(final JsPath path,
                             final Function<? super JsElem, ? extends JsElem> fn
                            )
    {

        requireNonNull(fn);

        return requireNonNull(path).head()
                                   .match(head -> this,
                                          index ->
                                          {
                                              final JsPath tail = path.tail();

                                              return tail.ifEmptyElse(() -> Functions.ifNothingElse(() -> this,
                                                                                                    elem -> of(nullPadding(index,
                                                                                                                           array,
                                                                                                                           elem
                                                                                                                          ))
                                                                                                   )
                                                                                     .apply(fn.apply(get(path))),
                                                                      () -> tail.ifPredicateElse(t -> putEmptyJson(array).test(index,
                                                                                                                               t
                                                                                                                              ),
                                                                                                 () ->
                                                                                                 {
                                                                                                     final JsElem newElem = tail.head()
                                                                                                                                .match(key -> emptyObject().put(tail,
                                                                                                                                                                fn
                                                                                                                                                               ),
                                                                                                                                       i -> emptyArray().put(tail,
                                                                                                                                                             fn
                                                                                                                                                            )
                                                                                                                                      );
                                                                                                     return of(nullPadding(index,
                                                                                                                           array,
                                                                                                                           newElem
                                                                                                                          ));
                                                                                                 },
                                                                                                 () -> of(array.update(index,
                                                                                                                       array.get(index)
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
    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       )
    {
        return Functions.reduce(this,
                                requireNonNull(op),
                                requireNonNull(map),
                                requireNonNull(predicate),
                                JsPath.empty()
                                      .index(-1),
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
                                 MINUS_ONE_PATH,
                                 Optional.empty()
                                )
                        .get();
    }

    @Override
    public final JsArray remove(final JsPath path)
    {


        return requireNonNull(path).head()
                                   .match(head -> this,
                                          index ->
                                          {
                                              final int maxIndex = array.size() - 1;
                                              if (index < -1 || index > maxIndex) return this;
                                              final JsPath tail = path.tail();
                                              return tail.ifEmptyElse(() -> of(index == -1 ? array.remove(maxIndex) : array.remove(index)),
                                                                      () -> Functions.ifJsonElse(json -> of(array.update(index,
                                                                                                                         json.remove(tail)
                                                                                                                        )),
                                                                                                 e -> this
                                                                                                )
                                                                                     .apply(array.get(index))
                                                                     );
                                          }

                                         );


    }

    @Override
    public final int size()
    {
        return array.size();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public Stream<JsPair> stream_()
    {
        return streamOfArr(this,
                           JsPath.empty()
                          );
    }

    @Override
    public Stream<JsPair> stream()
    {
        return IntStream.range(0,
                               size()
                              )
                        .mapToObj(Integer::toString)
                        .flatMap(i -> Stream.of(JsPair.of(JsPath.of(i),
                                                          get(i)
                                                         )));

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
                                                             .flatMap(pair -> Functions.ifValueElse(e -> Stream.of(pair),
                                                                                                    o -> streamOfObj(o,
                                                                                                                     pair.path
                                                                                                                    ),
                                                                                                    a -> streamOfArr(a,
                                                                                                                     pair.path
                                                                                                                    )
                                                                                                   )
                                                                                       .apply(pair.elem)
                                                                     )
                                                );


    }

    @Override
    public final JsArray tail()
    {

        return of(array.tail());

    }

    @Override
    public String toString()
    {

        return array.toString();


    }

    @Override
    @SuppressWarnings("squid:S00117") //  perfectly fine _
    public final JsArray union(final JsArray that,
                               final TYPE ARRAY_AS
                              )
    {

        return Functions.union(this,
                               requireNonNull(that),
                               requireNonNull(ARRAY_AS)
                              )
                        .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray union_(final JsArray that
                               )
    {

        return Functions.union_(this,
                                requireNonNull(that)
                               )
                        .get();
    }

    private Trampoline<JsArray> appendAllBackTrampoline(final JsArray arr1,
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

    private Trampoline<JsArray> appendAllFrontTrampoline(final JsArray arr1,
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

    private T nullPadding(final int index,
                          final T arr,
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

    private Trampoline<T> nullPaddingTrampoline(final int i,
                                                final T arr,
                                                final JsElem e
                                               )
    {

        if (i == arr.size()) return Trampoline.done(arr.appendBack(e));

        if (i == -1) return Trampoline.done(arr.update(array.size() - 1,
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

    @SuppressWarnings("squid:S1602") // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<Integer, JsPath> putEmptyJson(final MyVector<?> parray)
    {

        return (index, tail) ->
        {
            return index > parray.size() - 1 || parray.isEmpty() || parray.get(index)
                                                                          .isNotJson()
            ||
            (tail.head()
                 .isKey() && parray.get(index)
                                   .isArray()
            )
            ||
            (tail.head()
                 .isIndex() && parray.get(index)
                                     .isObj()
            );


        };


    }
}