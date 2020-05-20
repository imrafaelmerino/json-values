package jsonvalues;

import java.util.function.Function;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapObjElems extends OpMapElems<JsObj> {
    OpMapObjElems(final JsObj json) {
        super(json);
    }

    @Override
    Trampoline<JsObj> map(final Function<? super JsPair, ? extends JsValue> fn,
                          final JsPath startingPath
                         ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjElems(tail).map(fn,
                                                                                                                         startingPath
                                                                                                                        ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                        headJson
                                                                                                                       )),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = fn.apply(JsPair.of(headPath,
                                                                                                  headElem
                                                                                                 ));

                                                          return more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                       headMapped
                                                                                                                      ));
                                                      }
                                                     ).apply(head._2);
                                }
                               );

    }

    @Override
    Trampoline<JsObj> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                             final JsPath startingPath
                            ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjElems(tail).mapAll(fn,
                                                                                                                            startingPath
                                                                                                                           ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjElems(headJson).mapAll(fn,
                                                                                                                                                headPath
                                                                                                                                               )
                                                                                                                                        .map(headMapped ->
                                                                                                                                                     tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                    headMapped
                                                                                                                                                                   )
                                                                                                                                            )
                                                                                              ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrElems(headArr).mapAll(fn,
                                                                                                                                              headPath.index(-1)
                                                                                                                                             )
                                                                                                                                      .map(headMapped ->
                                                                                                                                                   tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                                  headMapped
                                                                                                                                                                 )
                                                                                                                                          )
                                                                                             ),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = fn.apply(JsPair.of(headPath,
                                                                                                  headElem
                                                                                                 ));

                                                          return more(() -> tailCall).map(tailResult -> tailResult.set(JsPath.fromKey(head._1),
                                                                                                                       headMapped
                                                                                                                      ));
                                                      }
                                                     ).apply(head._2);
                                }
                               );
    }
}
