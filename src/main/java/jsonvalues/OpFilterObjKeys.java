package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterObjKeys extends OpFilterKeys<JsObj> {

    OpFilterObjKeys(final JsObj json) {
        super(json);
    }

    @Override
    Trampoline<JsObj> filterAll(final JsPath startingPath,
                                final Predicate<? super JsPair> predicate
                               ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjKeys(tail).filterAll(startingPath,
                                                                                                                                 predicate
                                                                                                                                ));
                                    return
                                            predicate.test(JsPair.of(headPath,
                                                                     head._2
                                                                    )) ?
                                            ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjKeys(headObj).filterAll(headPath,
                                                                                                                                                    predicate
                                                                                                                                                   )
                                                                                                                                         .map(headMapped ->
                                                                                                                                                      tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                     headMapped
                                                                                                                                                                    )
                                                                                                                                             )),
                                                       headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrKeys(headArray).filterAll(headPath.index(-1),
                                                                                                                                                        predicate
                                                                                                                                                       )
                                                                                                                                             .map(headMapped ->
                                                                                                                                                          tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                         headMapped
                                                                                                                                                                        )
                                                                                                                                                 )),
                                                       headElem -> more(() -> tailCall).map(it -> it.set(JsPath.fromKey(head._1),
                                                                                                         headElem
                                                                                                        ))
                                                      )
                                                    .apply(head._2) :
                                            tailCall;
                                }
                               );
    }

    @Override
    Trampoline<JsObj> filter(final Predicate<? super JsPair> predicate
                            ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = JsPath.empty()
                                                                  .key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjKeys(tail).filter(predicate));
                                    return predicate.test(JsPair.of(headPath,
                                                                    head._2
                                                                   )) ?
                                           more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                 head._2
                                                                                                )) :


                                           tailCall;
                                }
                               );
    }


}
