package jsonvalues;

import java.util.function.Function;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrElems extends OpMapElems<JsArray> {
    OpMapArrElems(final JsArray json) {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, ? extends JsValue> fn,
                            final JsPath startingPath
                           ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrElems(tail).map(fn,
                                                                                                                           headPath
                                                                                                                          ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).map(it -> it.prepend(headJson)),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = fn.apply(JsPair.of(headPath,
                                                                                                  headElem
                                                                                                 ));

                                                          return more(() -> tailCall).map(tailResult -> tailResult.prepend(headMapped));
                                                      }
                                                     ).apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                               final JsPath startingPath
                              ) {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrElems(tail).mapAll(fn,
                                                                                                                              headPath
                                                                                                                             ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjElems(headObj).mapAll(fn,
                                                                                                                                              headPath
                                                                                                                                             )
                                                                                                                                      .map(tailResult::prepend)
                                                                                             ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrElems(headArr).mapAll(fn,
                                                                                                                                              headPath.index(-1)
                                                                                                                                             )
                                                                                                                                      .map(tailResult::prepend)
                                                                                             ),
                                                      headElem -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(JsPair.of(headPath,
                                                                                                                                               headElem
                                                                                                                                              ))))
                                                     )
                                            .apply(head);


                                }
                               );
    }
}

