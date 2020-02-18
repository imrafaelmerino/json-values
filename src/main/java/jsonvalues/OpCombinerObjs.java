package jsonvalues;

import io.vavr.Tuple2;

import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

final class OpCombinerObjs extends OpCombiner<JsObj>
{
    OpCombinerObjs(final JsObj a,
                   final JsObj b
                  )
    {
        super(a,
              b
             );
    }

    @SuppressWarnings("squid:S00100")
        //  naming convention:  xx_ traverses the whole json
    Trampoline<JsObj> combine()
    {
        if (b.isEmpty()) return done(a);
        Tuple2<String, JsElem> head = b.head();
        JsObj tail = b.tail();
        Trampoline<JsObj> tailCall = more(new OpCombinerObjs(a,
                                                             tail
        )::combine);
        return MatchExp.ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                                                                  head._2
                                                                                 )),
                                      MatchExp.ifPredicateElse(e -> e.isJson() && e.isSameType(head._2),
                                                               it ->
                                                               {
                                                                   Json<?> obj = a.get(JsPath.fromKey(head._1))
                                                                                  .asJson();
                                                                   Json<?> obj1 = head._2
                                                                                      .asJson();
                                                                   Trampoline<? extends Json<?>> headCall = more(() -> combine(obj,
                                                                                                                               obj1
                                                                                                                              )
                                                                                                                );
                                                                   return more(() -> tailCall).flatMap(tailResult -> headCall.map(headResult ->
                                                                                                                                  tailResult.put(JsPath.fromKey(head._1),
                                                                                                                                                 headResult
                                                                                                                                                )
                                                                                                                                 )
                                                                                                      );
                                                               },
                                                               it -> tailCall
                                                              )
                                     )
                       .apply(a.get(JsPath.empty()
                                          .key(head._1)));

    }
}
