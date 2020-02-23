package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapObjElems extends OpMapElems<JsObj>
{
    OpMapObjElems(final JsObj json)
    {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, ? extends JsValue> fn,
                          final Predicate<? super JsPair> predicate,
                          final JsPath startingPath
                         )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjElems(tail).map(fn,
                                                                                                                         predicate,
                                                                                                                         startingPath
                                                                                                                        ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                        headJson
                                                                                                                       )),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = JsPair.of(headPath,
                                                                                         headElem
                                                                                        )
                                                                                     .ifElse(predicate,
                                                                                            fn::apply,
                                                                                            p -> headElem
                                                                                           );
                                                          return more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                       headMapped
                                                                                                                      ));
                                                      }
                                                     ).apply(head._2);
                                }
                               );

    }

    @Override
    Trampoline<JsObj> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                             final Predicate<? super JsPair> predicate,
                             final JsPath startingPath
                            )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjElems(tail).mapAll(fn,
                                                                                                                            predicate,
                                                                                                                            startingPath
                                                                                                                           ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjElems(headJson).mapAll(fn,
                                                                                                                                                predicate,
                                                                                                                                                headPath
                                                                                                                                               )
                                                                                                                                        .map(headMapped ->
                                                                                                                                                      tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                     headMapped
                                                                                                                                                                    )
                                                                                                                                                     )
                                                                                              ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrElems(headArr).mapAll(fn,
                                                                                                                                              predicate,
                                                                                                                                              headPath.index(-1)
                                                                                                                                             )
                                                                                                                                      .map(headMapped ->
                                                                                                                                                    tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                   headMapped
                                                                                                                                                                  )
                                                                                                                                                   )
                                                                                             ),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = JsPair.of(headPath,
                                                                                         headElem
                                                                                        )
                                                                                     .ifElse(predicate,
                                                                                            fn::apply,
                                                                                            p -> headElem
                                                                                           );
                                                          return more(() -> tailCall).map(tailResult -> tailResult.put(JsPath.fromKey(head._1),
                                                                                                                       headMapped
                                                                                                                      ));
                                                      }
                                                     ).apply(head._2);
                                }
                               );
    }
}
