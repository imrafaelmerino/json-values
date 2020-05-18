package jsonvalues;

import java.util.function.Function;

import static jsonvalues.Trampoline.more;

final class OpMapObjKeys extends OpMapKeys<JsObj> {
    OpMapObjKeys(final JsObj json) {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, String> fn,
                          final JsPath startingPath
                         ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjKeys(tail).map(fn,
                                                                                                                        startingPath
                                                                                                                       )
                                                                                      );
                                    return Trampoline.more(() -> tailCall)
                                                     .map(tailResult ->
                                                          {
                                                              JsPair pair = JsPair.of(headPath,
                                                                                      head._2
                                                                                     );
                                                              final String keyMapped = fn.apply(pair);
                                                              return tailResult.set(JsPath.fromKey(keyMapped),
                                                                                    head._2
                                                                                   );
                                                          });
                                }
                               );
    }

    @Override
    Trampoline<JsObj> mapAll(final Function<? super JsPair, String> fn,
                             final JsPath startingPath
                            ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjKeys(tail).mapAll(fn,
                                                                                                                           startingPath
                                                                                                                          )
                                                                                      );
                                    JsPair pair = JsPair.of(headPath,
                                                            head._2
                                                           );
                                    if (head._2.isObj()) {
                                        return more(() -> tailCall).flatMap(tailResult -> new OpMapObjKeys(head._2.toJsObj()).mapAll(fn,
                                                                                                                                     headPath
                                                                                                                                    )
                                                                                                                             .map(headObjResult ->
                                                                                                                                          tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                                                                                                                                         headObjResult
                                                                                                                                                        )
                                                                                                                                 )
                                                                           );
                                    }
                                    else if (head._2.isArray()) {
                                        return more(() -> tailCall).flatMap(tailResult -> new OpMapArrKeys(head._2.toJsArray()).mapAll(fn,
                                                                                                                                       headPath.index(-1)
                                                                                                                                      )
                                                                                                                               .map(headArrResult ->
                                                                                                                                            tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                                                                                                                                           headArrResult
                                                                                                                                                          )
                                                                                                                                   )
                                                                           );
                                    }
                                    else {
                                        return more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                                                                                     head._2
                                                                                                    ));
                                    }
                                }
                               );
    }
}
