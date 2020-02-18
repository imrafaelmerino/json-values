package jsonvalues;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

final class OpMapReduce<T>
{
    private final BiFunction<JsPair, Optional<T>, Optional<T>> accumulator;

    OpMapReduce(final Predicate<? super JsPair> predicate,
                final Function<? super JsPair, T> map,
                final BinaryOperator<T> op
               )
    {
        this.accumulator = (pair, acc) ->
        {
            if (!predicate.test(pair)) return acc;
            final T mapped = map.apply(pair);
            final Optional<T> t = acc.map(it -> op.apply(it,
                                                         mapped
                                                        ));
            if (t.isPresent()) return t;
            return Optional.ofNullable(mapped);
        };
    }

    @SuppressWarnings("squid:S00100")
        //  naming convention, _ -> traverses the whole json recursively
    Optional<T> reduce_(JsObj obj)
    {
        return reduceObj(JsPath.empty(),
                         reduceHeadJsonAndObjTail_()
                        ).apply(obj,
                                Optional.empty()
                               )
                         .get();
    }

    Optional<T> reduce(JsObj obj)
    {
        return reduceObj(JsPath.empty(),
                         reduceHeadJsonAndObjTail()
                        ).apply(obj,
                                Optional.empty()
                               )
                         .get();
    }

    @SuppressWarnings("squid:S00100")
        //  naming convention, _ -> traverses the whole json recursively
    Optional<T> reduce_(JsArray arr)
    {
        return reduceArr(JsPath.empty()
                               .index(-1),
                         reduceHeadJsonAndArrayTail_()
                        ).apply(arr,
                                Optional.empty()
                               )
                         .get();
    }

    Optional<T> reduce(JsArray arr)
    {
        return reduceArr(JsPath.empty()
                               .index(-1),
                         reduceHeadJsonAndArrayTail()
                        ).apply(arr,
                                Optional.empty()
                               )
                         .get();
    }

    private BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndObjTail()
    {

        return (headPath, headJson) -> (tail, acc) -> Trampoline.more(() -> reduceObj(headPath.init(),
                                                                                      reduceHeadJsonAndObjTail()
                                                                                     ).apply(tail,
                                                                                             acc
                                                                                            )
                                                                     );
    }

    private BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndArrayTail()
    {

        return (headPath, headJson) -> (tail, acc) -> Trampoline.more(() -> reduceArr(headPath,
                                                                                      reduceHeadJsonAndArrayTail()
                                                                                     ).apply(tail,
                                                                                             acc
                                                                                            )
                                                                     );
    }

    @SuppressWarnings("squid:S00100")
    //  naming convention, _ -> traverses the whole json recursively
    private BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndObjTail_()
    {
        return (headPath, headJson) -> (tail, acc) -> more(() -> reduceJson(acc).apply(headPath,
                                                                                       headJson
                                                                                      )
                                                          ).flatMap(headAcc ->
                                                                    reduceObj(headPath.init(),
                                                                              reduceHeadJsonAndObjTail_()
                                                                             ).
                                                                              apply(tail,
                                                                                    headAcc
                                                                                   ));
    }


    @SuppressWarnings("squid:S00100")
    //  naming convention: xx_ traverses the whole json
    private BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndArrayTail_()
    {
        return (headPath, headJson) -> (tail, acc) -> more(() -> reduceJson(acc).apply(headPath,
                                                                                       headJson
                                                                                      )
                                                          ).flatMap(headAcc ->
                                                                    reduceArr(headPath,
                                                                              reduceHeadJsonAndArrayTail_()
                                                                             ).
                                                                              apply(tail,
                                                                                    headAcc
                                                                                   ));
    }

    private BiFunction<JsPath, Json<?>, Trampoline<Optional<T>>> reduceJson(final Optional<T> acc)
    {

        return (headPath, headJson) ->
        {
            if (headJson.isObj()) return reduceObj(headPath,
                                                   reduceHeadJsonAndObjTail_()
                                                  ).apply(headJson.asJsObj(),
                                                          acc
                                                         );
            return reduceArr(headPath.index(-1),
                             reduceHeadJsonAndArrayTail_()
                            ).apply(headJson.asJsArray(),
                                    acc
                                   );

        };
    }

    @SuppressWarnings("squid:S00100")
    //  naming convention: xx_ traverses the whole json
    private BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>> reduceObj(final JsPath startingPath,
                                                                              final BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonTail
                                                                             )
    {

        return (obj, acc) -> obj.ifEmptyElse(done(acc),
                                             (head, tail) ->
                                             {
                                                 final JsPath headPath = startingPath.key(head._1);
                                                 return MatchExp.ifJsonElse(headJson -> reduceHeadJsonTail.apply(headPath,
                                                                                                                 headJson
                                                                                                                )
                                                                                                          .apply(tail,
                                                                                                                 acc
                                                                                                                ),
                                                                            headElem -> more(() -> reduceObj(startingPath,
                                                                                                             reduceHeadJsonTail
                                                                                                            ).apply(tail,
                                                                                                                    accumulator.apply(JsPair.of(headPath,
                                                                                                                                                headElem
                                                                                                                                               ),
                                                                                                                                      acc
                                                                                                                                     )
                                                                                                                   ))

                                                                           )
                                                                .apply(head._2);

                                             }
                                            );


    }

    @SuppressWarnings("squid:S00100")
    //  naming convention: xx_ traverses the whole json
    private BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>> reduceArr(final JsPath startingPath,
                                                                                final BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonTail
                                                                               )
    {

        return (arr, acc) -> arr.ifEmptyElse(done(acc),
                                             (head, tail) ->
                                             {
                                                 final JsPath headPath = startingPath.inc();

                                                 return MatchExp.ifJsonElse(json -> reduceHeadJsonTail.apply(headPath,
                                                                                                             json
                                                                                                            )
                                                                                                      .apply(tail,
                                                                                                             acc
                                                                                                            ),
                                                                            elem -> more(() -> reduceArr(headPath,
                                                                                                         reduceHeadJsonTail
                                                                                                        ).apply(tail,
                                                                                                                accumulator.apply(JsPair.of(headPath,
                                                                                                                                            elem
                                                                                                                                           ),
                                                                                                                                  acc
                                                                                                                                 )
                                                                                                               ))

                                                                           )
                                                                .apply(head);
                                             }
                                            );
    }
}
