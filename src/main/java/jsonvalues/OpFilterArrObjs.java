package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpFilterArrObjs extends OpFilterObjs<JsArray> {


    OpFilterArrObjs(final JsArray json) {
        super(json);
    }

    @Override
    Trampoline<JsArray> filter(final JsPath startingPath,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate

                              ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterArrObjs(tail).filter(headPath,
                                                                                                                                predicate
                                                                                                                               ));
                                    return ifObjElse(headJson -> predicate.test(headPath,
                                                                                headJson
                                                                               ) ?
                                                                 more(() -> tailCall).map(tailResult -> tailResult.prepend(headJson)) :
                                                                 tailCall,
                                                     headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                    )
                                            .apply(head);
                                }

                               );
    }

    @Override
    Trampoline<JsArray> filterAll(final JsPath startingPath,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate

                                 ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall =
                                            Trampoline.more(() -> new OpFilterArrObjs(tail).filterAll(headPath,
                                                                                                      predicate
                                                                                                     ));
                                    return ifJsonElse(headObj -> predicate.test(headPath,
                                                                                headObj
                                                                               ) ?
                                                                 more(() -> tailCall).flatMap(tailResult -> new OpFilterObjObjs(headObj).filterAll(headPath,
                                                                                                                                                   predicate
                                                                                                                                                  )
                                                                                                                                        .map(tailResult::prepend)) :
                                                                 tailCall,
                                                      headArr -> more(() -> tailCall).flatMap(json -> new OpFilterArrObjs(headArr).filterAll(headPath.index(-1),
                                                                                                                                             predicate
                                                                                                                                            )
                                                                                                                                  .map(json::prepend)),
                                                      headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                     )
                                            .apply(head);
                                }

                               );
    }


}
