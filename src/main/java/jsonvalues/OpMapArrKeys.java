package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrKeys extends OpMapKeys<JsArray>
{
    OpMapArrKeys(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, String> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        throw InternalError.opNotSupportedForArrays();
    }

    @Override
    Trampoline<JsArray> mapAll(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath startingPath
                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrKeys(tail).mapAll(fn,
                                                                                                                             predicate,
                                                                                                                             headPath
                                                                                                                            ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjKeys(headObj).mapAll(fn,
                                                                                                                                             predicate,
                                                                                                                                             headPath
                                                                                                                                            )
                                                                                                                                     .map(tailResult::prepend)),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrKeys(headArr).mapAll(fn,
                                                                                                                                             predicate,
                                                                                                                                             headPath.index(-1)
                                                                                                                                            )
                                                                                                                                     .map(tailResult::prepend)),
                                                      headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                     )
                                    .apply(head);
                                }
                               );
    }
}
