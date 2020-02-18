package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrObjs extends OpMapObjs<JsArray>
{
    OpMapArrObjs(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                            final BiPredicate<? super JsPath, ? super JsObj> predicate,
                            final JsPath startingPath
                           )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrObjs(tail).map(fn,
                                                                                                                          predicate,
                                                                                                                          headPath
                                                                                                                         ));
                                    return ifObjElse(headJson -> JsPair.of(headPath,
                                                                           headJson
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   headJson
                                                                                                  ),
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p.path,
                                                                                                                                                       headJson
                                                                                                                                                      ))),
                                                                               p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headJson))

                                                                              ),
                                                     headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                    )
                                    .apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> mapAll(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
                               final JsPath startingPath
                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();
                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrObjs(tail).mapAll(fn,
                                                                                                                             predicate,
                                                                                                                             headPath
                                                                                                                            ));
                                    return ifJsonElse(headObj -> JsPair.of(headPath,
                                                                           headObj
                                                                          )
                                                                       .ifElse(p -> predicate.test(headPath,
                                                                                                   headObj
                                                                                                  ),
                                                                               p -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjObjs(fn.apply(headPath,
                                                                                                                                                         headObj
                                                                                                                                                        )).mapAll(fn,
                                                                                                                                                                  predicate,
                                                                                                                                                                  headPath
                                                                                                                                                                 )
                                                                                                                                                                   .map(tailResult::prepend))
                                                                       ,
                                                                               p -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjObjs(headObj).mapAll(fn,
                                                                                                                                                                predicate,
                                                                                                                                                                headPath
                                                                                                                                                               )
                                                                                                                                                        .map(tailResult::prepend))
                                                                              )
                                    ,
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrObjs(headArr).mapAll(fn,
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
