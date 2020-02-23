package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpMapObjObjs extends OpMapObjs<JsObj>
{
    OpMapObjObjs(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                          final BiPredicate<? super JsPath, ? super JsObj> predicate,
                          final JsPath startingPath
                         )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjObjs(tail).map(fn,
                                                                                                                        predicate,
                                                                                                                        startingPath
                                                                                                                       ));
                                    return ifObjElse(headObj -> more(() -> tailCall).map(tailResult ->
                                                                                         {
                                                                                             final JsValue headMapped = JsPair.of(headPath,
                                                                                                                                  headObj
                                                                                                                                 )
                                                                                                                              .ifElse(p -> predicate.test(p.path,
                                                                                                                                                         headObj
                                                                                                                                                        ),
                                                                                                                                     p -> fn.apply(p.path,
                                                                                                                                                   headObj
                                                                                                                                                  ),
                                                                                                                                     p -> p.value
                                                                                                                                    );
                                                                                             return tailResult.put(JsPath.fromKey(head._1),
                                                                                                                   headMapped
                                                                                                                  );
                                                                                         }),
                                                     headElem -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                       headElem
                                                                                                                      ))
                                                    )
                                    .apply(head._2);
                                }
                               );
    }

    @Override
    Trampoline<JsObj> mapAll(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                             final BiPredicate<? super JsPath, ? super JsObj> predicate,
                             final JsPath startingPath
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjObjs(tail).mapAll(fn,
                                                                                                                           predicate,
                                                                                                                           startingPath
                                                                                                                          )
                                                                                      );
                                    return ifJsonElse(headObj ->
                                                      {
                                                          JsObj headMapped = JsPair.of(headPath,
                                                                                       headObj
                                                                                      )
                                                                                   .ifElse(p -> predicate.test(headPath,
                                                                                                               headObj
                                                                                                              ),
                                                                                           p -> fn.apply(headPath,
                                                                                                         headObj
                                                                                                        ),
                                                                                           p -> headObj
                                                                                          );
                                                          return more(() -> tailCall).flatMap(tailResult -> new OpMapObjObjs(headMapped).mapAll(fn,
                                                                                                                                                predicate,
                                                                                                                                                headPath
                                                                                                                                               )
                                                                                                                                        .map(headMappedResult ->
                                                                                                                                                      tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                     headMappedResult
                                                                                                                                                                    )
                                                                                                                                                     )
                                                                                             );
                                                      },
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrObjs(headArray).mapAll(fn,
                                                                                                                                                 predicate,
                                                                                                                                                 headPath.index(-1)
                                                                                                                                                )
                                                                                                                                         .map(headResult ->
                                                                                                                                                       tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                      headResult
                                                                                                                                                                     )
                                                                                                                                                      )
                                                                                               ),
                                                      headElement -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                           headElement
                                                                                                                          ))
                                                     )
                                    .apply(head._2);
                                }
                               );
    }
}
