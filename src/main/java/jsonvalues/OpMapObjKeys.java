package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.Trampoline.more;

final class OpMapObjKeys extends OpMapKeys<JsObj>
{
    OpMapObjKeys(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, String> fn,
                          final Predicate<? super JsPair> predicate,
                          final JsPath startingPath
                         )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);
                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjKeys(tail).map(fn,
                                                                                                                        predicate,
                                                                                                                        startingPath
                                                                                                                       )
                                                                                      );
                                    return Trampoline.more(() -> tailCall)
                                                     .map(tailResult ->
                                                          {
                                                              final String keyMapped = JsPair.of(headPath,
                                                                                                 head._2
                                                                                                )
                                                                                             .ifElse(predicate,
                                                                                                     fn,
                                                                                                     p -> head._1
                                                                                                    );
                                                              return tailResult.put(JsPath.fromKey(keyMapped),
                                                                                    head._2
                                                                                   );
                                                          });
                                }
                               );
    }

    @Override
    Trampoline<JsObj> mapAll(final Function<? super JsPair, String> fn,
                             final Predicate<? super JsPair> predicate,
                             final JsPath startingPath
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjKeys(tail).mapAll(fn,
                                                                                                                           predicate,
                                                                                                                           startingPath
                                                                                                                          )
                                                                                      );
                                    JsPair pair = JsPair.of(headPath,
                                                            head._2
                                                           );

                                    return pair.ifElse(predicate,
                                                       headTrue -> headTrue.ifJsonElse((path, headObj) ->
                                                                                       more(() -> tailCall).flatMap(tailResult -> new OpMapObjKeys(headObj).mapAll(fn,
                                                                                                                                                                   predicate,
                                                                                                                                                                   headPath
                                                                                                                                                                  )
                                                                                                                                                           .map(headObjResult ->
                                                                                                                                                                         tailResult.put(JsPath.fromKey(fn.apply(pair)),
                                                                                                                                                                                        headObjResult
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                   ),
                                                                                       (path, headArr) ->
                                                                                       more(() -> tailCall).flatMap(tailResult -> new OpMapArrKeys(headArr).mapAll(fn,
                                                                                                                                                                   predicate,
                                                                                                                                                                   headPath.index(-1)
                                                                                                                                                                  )
                                                                                                                                                           .map(headArrResult ->
                                                                                                                                                                         tailResult.put(JsPath.fromKey(fn.apply(pair)),
                                                                                                                                                                                        headArrResult
                                                                                                                                                                                       )
                                                                                                                                                                        )
                                                                                                                   ),
                                                                                       (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(fn.apply(pair)),
                                                                                                                                                                 headElem
                                                                                                                                                                ))
                                                                                      ),
                                                       headFalse -> headFalse.ifJsonElse((path, headObj) ->
                                                                                         more(() -> tailCall).flatMap(tailResult -> new OpMapObjKeys(headObj).mapAll(fn,
                                                                                                                                                                     predicate,
                                                                                                                                                                     headPath
                                                                                                                                                                    )
                                                                                                                                                             .map(headObjResult ->
                                                                                                                                                                           tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                                          headObjResult
                                                                                                                                                                                         )
                                                                                                                                                                          )
                                                                                                                     ),
                                                                                         (path, headArr) ->
                                                                                         more(() -> tailCall).flatMap(tailResult -> new OpMapArrKeys(headArr).mapAll(fn,
                                                                                                                                                                     predicate,
                                                                                                                                                                     headPath.index(-1)
                                                                                                                                                                    )
                                                                                                                                                             .map(headArrResult ->
                                                                                                                                                                           tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                                          headArrResult
                                                                                                                                                                                         )
                                                                                                                                                                          )
                                                                                                                     ),
                                                                                         (path, headElem) -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                   headElem
                                                                                                                                                                  ))
                                                                                        )
                                                      );
                                }
                               );
    }
}
