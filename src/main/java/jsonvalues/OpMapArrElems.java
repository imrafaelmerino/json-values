package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.Trampoline.more;

final class OpMapArrElems extends OpMapElems<JsArray>
{
    OpMapArrElems(final JsArray json)
    {
        super(json);
    }

    @Override
    Trampoline<JsArray> map(final Function<? super JsPair, ? extends JsValue> fn,
                            final Predicate<? super JsPair> predicate,
                            final JsPath startingPath
                           )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrElems(tail).map(fn,
                                                                                                                           predicate,
                                                                                                                           headPath
                                                                                                                          ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).map(it -> it.prepend(headJson)),
                                                      headElem ->
                                                      {
                                                          JsValue headMapped = JsPair.of(headPath,
                                                                                         headElem
                                                                                        )
                                                                                     .ifElse(predicate,
                                                                                            fn::apply,
                                                                                            p -> p.value
                                                                                           );
                                                          return more(() -> tailCall).map(tailResult -> tailResult.prepend(headMapped));
                                                      }
                                                     ).apply(head);
                                }
                               );
    }

    @Override
    Trampoline<JsArray> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath startingPath
                              )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.inc();

                                    final Trampoline<JsArray> tailCall = Trampoline.more(() -> new OpMapArrElems(tail).mapAll(fn,
                                                                                                                              predicate,
                                                                                                                              headPath
                                                                                                                             ));
                                    return ifJsonElse(headObj -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjElems(headObj).mapAll(fn,
                                                                                                                                              predicate,
                                                                                                                                              headPath
                                                                                                                                             )
                                                                                                                                      .map(tailResult::prepend)
                                                                                             ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrElems(headArr).mapAll(fn,
                                                                                                                                              predicate,
                                                                                                                                              headPath.index(-1)
                                                                                                                                             )
                                                                                                                                      .map(tailResult::prepend)
                                                                                             ),
                                                      headElem -> JsPair.of(headPath,
                                                                            headElem
                                                                           )
                                                                        .ifElse(predicate,
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(fn.apply(p))),
                                                                                p -> more(() -> tailCall).map(tailResult -> tailResult.prepend(headElem))
                                                                               )
                                                     )
                                    .apply(head);


                                }
                               );
    }
}

