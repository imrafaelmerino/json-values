package jsonvalues;

import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpFilterObjObjs extends OpFilterObjs<JsObj>
{


    OpFilterObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> filter(final JsPath startingPath,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate

                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjObjs(tail).filter(startingPath,
                                                                                                                              predicate
                                                                                                                             ));
                                    return ifObjElse(json -> JsPair.of(headPath,
                                                                       json
                                                                      )
                                                                   .ifElse(p -> predicate.test(p.path,
                                                                                               json
                                                                                              ),
                                                                           p -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                      json
                                                                                                                                     )),
                                                                           p -> tailCall
                                                                          ),
                                                     value -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                    value
                                                                                                                   ))
                                                    )
                                    .apply(head._2);
                                }

                               );
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> filter_(final JsPath startingPath,
                              final BiPredicate<? super JsPath, ? super JsObj> predicate
                             )
    {


        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpFilterObjObjs(tail).filter_(startingPath,
                                                                                                                               predicate
                                                                                                                              ));
                                    return ifJsonElse(headObj -> JsPair.of(headPath,
                                                                           headObj
                                                                          )
                                                                       .ifElse(p -> predicate.test(p.path,
                                                                                                   headObj
                                                                                                  ),
                                                                               p -> more(() -> tailCall).flatMap(tailResult -> new OpFilterObjObjs(headObj).filter_(headPath,
                                                                                                                                                                    predicate
                                                                                                                                                                   )
                                                                                                                                                           .map(headFiltered ->
                                                                                                                                                                         tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                                        headFiltered
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                ),
                                                                               p -> tailCall
                                                                              ),
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpFilterArrObjs(headArray).filter_(headPath.index(-1),
                                                                                                                                                     predicate
                                                                                                                                                    )
                                                                                                                                            .map(headFiltered ->
                                                                                                                                                          tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                         headFiltered
                                                                                                                                                                        )
                                                                                                                                                         )
                                                                                               ),
                                                      headElem -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                                                                                        headElem
                                                                                                       ))
                                                     )
                                    .apply(head._2);
                                }
                               );

    }


}
