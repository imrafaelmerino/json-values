package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterArrElems extends OpFilterElems<JsArray>
{


    OpFilterArrElems(final JsArray a)
    {
        super(a);
    }

    @Override
    Trampoline<JsArray> filter(final JsPath startingPath,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {

                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterArrElems(tail).filter(headPath,
                                                                                                                                 predicate
                                                                                                                                ));
                                    return ifJsonElse(elem -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                      elem -> JsPair.of(headPath,
                                                                        elem
                                                                       )
                                                                    .ifElse(predicate,
                                                                            p -> more(() -> tailCall).map(it -> it.prepend(elem)),
                                                                            p -> tailCall
                                                                           )
                                                     )
                                    .apply(head);
                                }
                               );
    }


    @Override
    Trampoline<JsArray> filterAll(final JsPath startingPath,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {

                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterArrElems(tail).filterAll(headPath,
                                                                                                                                    predicate
                                                                                                                                   ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjElems(headObj).filterAll(headPath,
                                                                                                                                                    predicate
                                                                                                                                                   )
                                                                                                                                         .map(tailResult::prepend)
                                                                                             )
                                    ,
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrElems(headArray).filterAll(headPath.index(-1),
                                                                                                                                                        predicate
                                                                                                                                                       )
                                                                                                                                             .map(tailResult::prepend)
                                                                                               ),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem)),
                                                                                p -> tailCall
                                                                               )
                                                     )
                                    .apply(head);
                                }
                               );
    }
}
