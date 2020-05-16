package jsonvalues;

import java.util.function.Function;

import static jsonvalues.Trampoline.more;

final class OpMapObjKeys extends OpMapKeys<JsObj>
{
    OpMapObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, String> fn,
                          final JsPath startingPath
                         )
    {
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
                            )
    {
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

                                    return pair.ifJsonElse((path, headObj) ->
                                        more(() -> tailCall).flatMap(tailResult -> new OpMapObjKeys(headObj).mapAll(fn,
                                          headPath
                                          )
                                            .map(headObjResult ->
                                              tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                                headObjResult
                                              )
                                            )
                                        ),
                                      (path, headArr) ->
                                        more(() -> tailCall).flatMap(tailResult -> new OpMapArrKeys(headArr).mapAll(fn,
                                          headPath.index(-1)
                                          )
                                            .map(headArrResult ->
                                              tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                                headArrResult
                                              )
                                            )
                                        ),
                                      (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(fn.apply(pair)),
                                        headElem
                                      ))
                                    );
                                }
                               );
    }
}
