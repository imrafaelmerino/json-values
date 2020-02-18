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
    Trampoline<JsObj> map(final Function<? super JsPair, ? extends JsElem> fn,
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
                                                          JsElem headMapped = JsPair.of(headPath,
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
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    Trampoline<JsObj> map_(final Function<? super JsPair, ? extends JsElem> fn,
                           final Predicate<? super JsPair> predicate,
                           final JsPath startingPath
                          )
    {
        return json.ifEmptyElse(Trampoline.done(json),
                                (head, tail) ->
                                {
                                    final JsPath headPath = startingPath.key(head._1);

                                    final Trampoline<JsObj> tailCall = Trampoline.more(() -> new OpMapObjElems(tail).map_(fn,
                                                                                                                          predicate,
                                                                                                                          startingPath
                                                                                                                         ));
                                    return ifJsonElse(headJson -> more(() -> tailCall).flatMap(tailResult -> new OpMapObjElems(headJson).map_(fn,
                                                                                                                                              predicate,
                                                                                                                                              headPath
                                                                                                                                             )
                                                                                                                                        .map(headMapped ->
                                                                                                                                                      tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                                     headMapped
                                                                                                                                                                    )
                                                                                                                                                     )
                                                                                              ),
                                                      headArr -> more(() -> tailCall).flatMap(tailResult -> new OpMapArrElems(headArr).map_(fn,
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
                                                          JsElem headMapped = JsPair.of(headPath,
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
