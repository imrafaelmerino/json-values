package jsonvalues;

import java.util.function.BiFunction;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifObjElse;
import static jsonvalues.Trampoline.more;

final class OpMapObjObjs extends OpMapObjs<JsObj> {
    OpMapObjObjs(final JsObj json) {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                          final JsPath startingPath
                         ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjObjs(tail).map(fn,
                                                                                                                        startingPath
                                                                                                                       ));
                                    return ifObjElse(headObj -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                      fn.apply(JsPair.of(headPath,
                                                                                                                                         headObj
                                                                                                                                        ).path,
                                                                                                                               headObj
                                                                                                                              )
                                                                                                                     )),
                                                     headElem -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                       headElem
                                                                                                                      ))
                                                    ).apply(head._2);
                                }
                               );
    }

    @Override
    Trampoline<JsObj> mapAll(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                             final JsPath startingPath
                            ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjObjs(tail).mapAll(fn,
                                                                                                                           startingPath
                                                                                                                          )
                                                                                      );
                                    return ifJsonElse(headObj ->
                                                      {
                                                          JsValue headMapped = fn.apply(headPath,
                                                                                        headObj
                                                                                       );
                                                          if (headMapped.isObj())
                                                              return more(() -> tailCall).flatMap(tailResult -> new OpMapObjObjs(headMapped.toJsObj()).mapAll(fn,
                                                                                                                                                              headPath
                                                                                                                                                             )
                                                                                                                                                      .map(headMappedResult ->
                                                                                                                                                                   tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                                  headMappedResult
                                                                                                                                                                                 )
                                                                                                                                                          )
                                                                                                 );
                                                          return more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                       headMapped
                                                                                                                      ));

                                                      },
                                                      headArray -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrObjs(headArray).mapAll(fn,
                                                                                                                                                 headPath.index(-1)
                                                                                                                                                )
                                                                                                                                         .map(headResult ->
                                                                                                                                                      tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                     headResult
                                                                                                                                                                    )
                                                                                                                                             )
                                                                                               ),
                                                      headElement -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                           headElement
                                                                                                                          ))
                                                     )
                                            .apply(head._2);
                                }
                               );
    }
}
