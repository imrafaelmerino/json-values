package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterObjKeys extends OpFilterKeys<JsObj>
{

    OpFilterObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> filterAll(final JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjKeys(tail).filterAll(startingPath,
                                                                                                                                 predicate
                                                                                                                                ));
                                    return JsPair.of(headPath,
                                                     head._2
                                                    )
                                                 .ifElse(predicate,
                                                         p -> ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjKeys(headObj).filterAll(headPath,
                                                                                                                                                                      predicate
                                                                                                                                                                     )
                                                                                                                                                           .map(headMapped ->
                                                                                                                                                                         tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                                        headMapped
                                                                                                                                                                                       )
                                                                                                                                                                        )),
                                                                         headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrKeys(headArray).filterAll(headPath.index(-1),
                                                                                                                                                                          predicate
                                                                                                                                                                         )
                                                                                                                                                               .map(headMapped ->
                                                                                                                                                                             tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                                            headMapped
                                                                                                                                                                                           )
                                                                                                                                                                            )),
                                                                         headElem -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                                                                                                           headElem
                                                                                                                          ))
                                                                        )
                                                         .apply(head._2),
                                                         p -> tailCall
                                                        );
                                }
                               );
    }

    @Override
    Trampoline<JsObj> filter(final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = JsPath.empty()
                                                                  .key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjKeys(tail).filter(predicate));
                                    return JsPair.of(headPath,
                                                     head._2
                                                    )
                                                 .ifElse(predicate,
                                                         p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                    head._2
                                                                                                                   )),


                                                         p -> tailCall
                                                        );
                                }
                               );
    }


}
