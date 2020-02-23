package jsonvalues;

import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpFilterArrKeys extends OpFilterKeys<JsArray>
{


    OpFilterArrKeys(final JsArray json)
    {
        super(json);
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
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpFilterArrKeys(tail).filterAll(headPath,
                                                                                                                                   predicate
                                                                                                                                  ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjKeys(headObj).filterAll(headPath,
                                                                                                                                                   predicate
                                                                                                                                                  )
                                                                                                                                        .map(tailResult::prepend)),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrKeys(headArr).filterAll(headPath.index(-1),
                                                                                                                                                   predicate
                                                                                                                                                  )),
                                                      headElem -> more(() -> tailCall).map(it -> it.prepend(headElem))
                                                     )
                                    .apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> filter(final Predicate<? super JsPair> predicate
                              )
    {
        throw InternalError.opNotSupportedForArrays();
    }


}
