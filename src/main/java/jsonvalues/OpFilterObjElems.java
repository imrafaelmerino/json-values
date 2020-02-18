package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterObjElems extends OpFilterElems<JsObj>
{
    OpFilterObjElems(final JsObj a)
    {
        super(a
             );
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

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjElems(tail).filterAll(startingPath,
                                                                                                                                  predicate
                                                                                                                                 )
                                                                                      );
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjElems(headObj).filterAll(headPath,
                                                                                                                                                    predicate
                                                                                                                                                   )
                                                                                                                                         .map(headFiltered ->
                                                                                                                                                       tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                      headFiltered
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                             ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrElems(headArr).filterAll(headPath.index(-1),
                                                                                                                                                    predicate
                                                                                                                                                   )
                                                                                                                                         .map(headFiltered ->
                                                                                                                                                       tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                      headFiltered
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                             ),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                           headElem
                                                                                                                                          )
                                                                                                             ),
                                                                                p -> tailCall
                                                                               )

                                                     )
                                    .apply(head._2);

                                }
                               );
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjElems(tail).filter(startingPath,
                                                                                                                               predicate
                                                                                                                              ));
                                    return ifJsonElse(headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                        headElem
                                                                                                                       )),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                           headElem
                                                                                                                                          )),
                                                                                p -> tailCall
                                                                               )
                                                     )
                                    .apply(head._2);

                                }
                               );
    }
}