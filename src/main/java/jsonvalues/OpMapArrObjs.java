package jsonvalues;

import java.util.function.BiFunction;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrObjs extends OpMapObjs<JsArray> {
    OpMapArrObjs(final JsArray json) {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                            final JsPath startingPath
                           ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall =
                                            Trampoline.more(() -> new OpMapArrObjs(tail).map(fn,
                                                                                             headPath
                                                                                            ));
                                    return ifObjElse(headJson -> {
                                                         JsPair pair = JsPair.of(headPath,
                                                                                 headJson
                                                                                );
                                                         return more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(pair.path,
                                                                                                                                   headJson
                                                                                                                                  )));
                                                     },
                                                     headElem ->
                                                             more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                    )
                                            .apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> mapAll(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                               final JsPath startingPath) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall =
                                            Trampoline.more(() -> new OpMapArrObjs(tail).mapAll(fn,
                                                                                                headPath
                                                                                               ));
                                    return ifJsonElse(
                                            headObj -> more(() -> tailCall).flatMap(tailResult -> {
                                                JsValue headMapped = fn.apply(headPath,
                                                                              headObj
                                                                             );
                                                if (headMapped.isObj())
                                                    return new OpMapObjObjs(headMapped.toJsObj()).mapAll(fn,
                                                                                                         headPath
                                                                                                        )
                                                                                                 .map(tailResult::prepend);
                                                return more(() -> tailCall).map(t -> t.prepend(headMapped));
                                            }),
                                            headArr ->
                                                    more(() -> tailCall).flatMap(tailResult ->
                                                                                         new OpMapArrObjs(headArr)
                                                                                                 .mapAll(fn,
                                                                                                         headPath.index(-1)
                                                                                                        )
                                                                                                 .map(tailResult::prepend)),
                                            headElem ->
                                                    more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))).apply(head);
                                }

                               );
    }
}
