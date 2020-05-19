package jsonvalues;

import java.util.function.Function;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrKeys extends OpMapKeys<JsArray> {
    OpMapArrKeys(final JsArray json) {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, String> fn,
                            final JsPath startingPath
                           ) {
        throw InternalError.opNotSupportedForArrays();
    }

    @Override
    Trampoline<JsArray> mapAll(final Function<? super JsPair, String> fn,
                               final JsPath startingPath
                              ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall =
                                            Trampoline.more(() -> new OpMapArrKeys(tail).mapAll(fn,
                                                                                                headPath
                                                                                               ));
                                    return ifJsonElse(headObj ->
                                                              more(() -> tailCall).flatMap(tailResult ->
                                                                                                   new OpMapObjKeys(headObj).mapAll(fn,
                                                                                                                                    headPath
                                                                                                                                   )
                                                                                                                            .map(tailResult::prepend)),
                                                      headArr ->
                                                              more(() -> tailCall).flatMap(tailResult ->
                                                                                                   new OpMapArrKeys(headArr).mapAll(fn,
                                                                                                                                    headPath.index(-1)
                                                                                                                                   )
                                                                                                                            .map(tailResult::prepend)),
                                                      headElem ->
                                                              more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                     )
                                            .apply(head);
                                }
                               );
    }
}
