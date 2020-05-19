package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpFilterObjObjs extends OpFilterObjs<JsObj> {


    OpFilterObjObjs(final JsObj json) {
        super(json);
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate

                            ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall =
                                            Trampoline.more(() -> new OpFilterObjObjs(tail).filter(startingPath,
                                                                                                   predicate
                                                                                                  ));
                                    return ifObjElse(json -> predicate.test(headPath,
                                                                            json
                                                                           ) ?
                                                             more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                   json
                                                                                                                  )) :
                                                             tailCall
                                            ,
                                                     value -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                    value
                                                                                                                   ))
                                                    )
                                            .apply(head._2);
                                }

                               );
    }

    @Override
    Trampoline<JsObj> filterAll(final JsPath startingPath,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               ) {


        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjObjs(tail).filterAll(startingPath,
                                                                                                                                 predicate
                                                                                                                                ));
                                    return ifJsonElse(headObj -> predicate.test(headPath,
                                                                                headObj
                                                                               ) ?
                                                                 more(() -> tailCall).flatMap(tailResult -> new OpFilterObjObjs(headObj).filterAll(headPath,
                                                                                                                                                   predicate
                                                                                                                                                  )
                                                                                                                                        .map(headFiltered ->
                                                                                                                                                     tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                    headFiltered
                                                                                                                                                                   )
                                                                                                                                            )
                                                                                             ) :
                                                                 tailCall,
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrObjs(headArray).filterAll(headPath.index(-1),
                                                                                                                                                       predicate
                                                                                                                                                      )
                                                                                                                                            .map(headFiltered ->
                                                                                                                                                         tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                        headFiltered
                                                                                                                                                                       )
                                                                                                                                                )
                                                                                               ),
                                                      headElem -> more(() -> tailCall).map(it -> it.set(JsPath.fromKey(head._1),
                                                                                                        headElem
                                                                                                       ))
                                                     )
                                            .apply(head._2);
                                }
                               );

    }


}
