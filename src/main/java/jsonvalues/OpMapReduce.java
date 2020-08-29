package jsonvalues;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

final class OpMapReduce<T> {
    private final BiFunction<JsPair, Optional<T>, Optional<T>> accumulator;

    OpMapReduce(final Predicate<? super JsPair> predicate,
                final Function<? super JsPair, T> map,
                final BinaryOperator<T> op
               ) {
        this.accumulator = (pair, acc) ->
        {
            if (!predicate.test(pair)) return acc;
            final T mapped = map.apply(pair);
            final Optional<T> t = acc.map(it -> op.apply(it,
                                                         mapped
                                                        )
                                         );
            if (t.isPresent()) return t;
            return Optional.ofNullable(mapped);
        };
    }

    Optional<T> reduceAll(JsObj obj) {
        return reduceObj(JsPath.empty(),
                         reduceAllHeadJsonAndObjTail()
                        ).apply(obj,
                                Optional.empty()
                               )
                         .get();
    }

    Optional<T> reduce(JsObj obj) {
        return reduceObj(JsPath.empty(),
                         reduceHeadJsonAndObjTail()
                        ).apply(obj,
                                Optional.empty()
                               )
                         .get();
    }

    Optional<T> reduceAll(JsArray arr) {
        return reduceArr(JsPath.empty()
                               .index(-1),
                         reduceAllHeadJsonAndArrayTail()
                        ).apply(arr,
                                Optional.empty()
                               )
                         .get();
    }

    Optional<T> reduce(JsArray arr) {
        return reduceArr(JsPath.empty()
                               .index(-1),
                         reduceHeadJsonAndArrayTail()
                        ).apply(arr,
                                Optional.empty()
                               )
                         .get();
    }

    private BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndObjTail() {

        return (headPath, headJson) -> (tail, acc) -> Trampoline.more(() -> reduceObj(headPath.init(),
                                                                                      reduceHeadJsonAndObjTail()
                                                                                     ).apply(tail,
                                                                                             acc
                                                                                            )
                                                                     );
    }

    private BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonAndArrayTail() {

        return (headPath, headJson) -> (tail, acc) -> Trampoline.more(() -> reduceArr(headPath,
                                                                                      reduceHeadJsonAndArrayTail()
                                                                                     ).apply(tail,
                                                                                             acc
                                                                                            )
                                                                     );
    }

    private BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceAllHeadJsonAndObjTail() {
        return (headPath, headJson) -> (tail, acc) -> more(() -> reduceJson(acc).apply(headPath,
                                                                                       headJson
                                                                                      )
                                                          ).flatMap(headAcc ->
                                                                            reduceObj(headPath.init(),
                                                                                      reduceAllHeadJsonAndObjTail()
                                                                                     ).
                                                                                              apply(tail,
                                                                                                    headAcc
                                                                                                   ));
    }


    private BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceAllHeadJsonAndArrayTail() {
        return (headPath, headJson) -> (tail, acc) -> more(() -> reduceJson(acc).apply(headPath,
                                                                                       headJson
                                                                                      )
                                                          ).flatMap(headAcc ->
                                                                            reduceArr(headPath,
                                                                                      reduceAllHeadJsonAndArrayTail()
                                                                                     ).
                                                                                              apply(tail,
                                                                                                    headAcc
                                                                                                   ));
    }

    private BiFunction<JsPath, Json<?>, Trampoline<Optional<T>>> reduceJson(final Optional<T> acc) {

        return (headPath, headJson) ->
        {
            if (headJson.isObj()) return reduceObj(headPath,
                                                   reduceAllHeadJsonAndObjTail()
                                                  ).apply(headJson.toJsObj(),
                                                          acc
                                                         );
            return reduceArr(headPath.index(-1),
                             reduceAllHeadJsonAndArrayTail()
                            ).apply(headJson.toJsArray(),
                                    acc
                                   );

        };
    }

    private BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>> reduceObj(final JsPath startingPath,
                                                                              final BiFunction<JsPath, Json<?>, BiFunction<JsObj, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonTail
                                                                             ) {

        return (obj, acc) ->
                obj.ifEmptyElse(done(acc),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    return MatchExp.ifJsonElse(headJson ->
                                                                       reduceHeadJsonTail.apply(headPath,
                                                                                                headJson
                                                                                               )
                                                                                         .apply(tail,
                                                                                                acc
                                                                                               ),
                                                               headElem ->
                                                                       more(() -> reduceObj(startingPath,
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

    private BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>> reduceArr(final JsPath startingPath,
                                                                                final BiFunction<JsPath, Json<?>, BiFunction<JsArray, Optional<T>, Trampoline<Optional<T>>>> reduceHeadJsonTail
                                                                               ) {

        return (arr, acc) -> arr.ifEmptyElse(done(acc),
                                             (head, tail) ->
                                             {
                                                 final JsPath headPath = startingPath.inc();

                                                 return MatchExp.ifJsonElse(json ->
                                                                                    reduceHeadJsonTail.apply(headPath,
                                                                                                             json
                                                                                                            )
                                                                                                      .apply(tail,
                                                                                                             acc
                                                                                                            ),
                                                                            elem ->
                                                                                    more(() -> reduceArr(headPath,
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
