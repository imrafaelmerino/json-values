package jsonvalues;

import jsonvalues.JsArray.TYPE;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static jsonvalues.AbstractJsArray.streamOfArr;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Explicit instantiation of JsObj interface to reduce class file size in subclasses.
 @param <M> type of the map implementation to hold json objects
 */
abstract class AbstractJsObj<M extends MyMap<M>> implements JsObj
{

    protected M map;

    AbstractJsObj(final M myMap)
    {
        assert myMap != null;
        this.map = myMap;
    }


    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (!(that instanceof AbstractJsObj<?>)) return false;
        if (this == that) return true;
        if (getClass() != that.getClass()) return false;
        final AbstractJsObj<?> thatMap = (AbstractJsObj) that;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = thatMap.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

        return fields().stream()
                       .allMatch(f ->
                                 thatMap.map.getOptional(f)
                                            .map(it -> it.equals(map.get(f)))
                                            .orElse(false) && thatMap.fields()
                                                                     .stream()
                                                                     .allMatch(it -> map.contains(it)));
    }

    @Override
    public final Set<String> fields()
    {
        return map.keys();
    }


    @Override
    public final JsElem get(final Position position)
    {
        return requireNonNull(position).match(key -> map.contains(key) ? map.get(key) : NOTHING,
                                              index -> NOTHING
                                             );
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }

    @Override
    public final Map.Entry<String, JsElem> head()
    {
        return map.head();
    }

    @Override
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public final JsObj intersection(final JsObj that,
                                    final TYPE ARRAY_AS
                                   )
    {
        requireNonNull(that);
        return intersection(this,
                            that,
                            ARRAY_AS
                           )
        .get();
    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private Trampoline<JsObj> intersection(final JsObj a,
                                           final JsObj b,
                                           final JsArray.TYPE ARRAY_AS
                                          )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);
        Map.Entry<String, JsElem> head = a.head();
        JsObj tail = a.tail(head.getKey());
        final Trampoline<Trampoline<JsObj>> tailCall = () -> intersection(tail,
                                                                          b,
                                                                          ARRAY_AS
                                                                         );
        final JsElem bElem = b.get(JsPath.fromKey(head.getKey()));

        return ((bElem.isJson() && bElem.asJson()
                                        .equals(head.getValue(),
                                                ARRAY_AS
                                               )) || bElem.equals(head.getValue())) ?
        more(tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                        head.getValue()
                                       )) :
        more(tailCall);
    }

    @Override
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) //  ARRAY_AS should be a valid name, naming convention: _ traverses recursively
    public final JsObj intersection_(final JsObj that,
                                     final TYPE ARRAY_AS
                                    )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return intersection_(this,
                             that,
                             ARRAY_AS
                            ).get();

    }

    @SuppressWarnings({"squid:S00117", "squid:S00100"}) // ARRAY_AS should be a valid name for an enum constant, naming convention _
    private Trampoline<JsObj> intersection_(final JsObj a,
                                            final JsObj b,
                                            final JsArray.TYPE ARRAY_AS
                                           )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);
        Map.Entry<String, JsElem> head = a.head();

        JsObj tail = a.tail(head.getKey());

        final Trampoline<JsObj> tailCall = more(() -> intersection_(tail,
                                                                    b,
                                                                    ARRAY_AS
                                                                   ));
        if (b.containsPath(JsPath.fromKey(head.getKey())))
        {

            final JsElem headOtherElement = b.get(JsPath.fromKey(head.getKey()));
            if (headOtherElement.equals(head.getValue()))
            {
                return more(() -> intersection_(tail,
                                                b.tail(head.getKey()),
                                                ARRAY_AS
                                               )).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                   head.getValue()
                                                                  ));

            } else if (head.getValue()
                           .isJson() && head.getValue()
                                            .isSameType(headOtherElement))
            {//different but same container
                Json<?> obj = head.getValue()
                                  .asJson();
                Json<?> obj1 = headOtherElement.asJson();

                Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersection_(obj,
                                                                                                                  obj1,
                                                                                                                  ARRAY_AS
                                                                                                                 )
                                                             );
                return more(() -> tailCall).flatMap(json -> headCall
                                                    .map(it -> json.put(JsPath.fromKey(head.getKey()),
                                                                        it
                                                                       )
                                                        )
                                                   );
            }

        }
        return tailCall;
    }


    @Override
    public final boolean isEmpty()
    {
        return map.isEmpty();
    }


    abstract JsObj of(M map);


    @Override
    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduce(this);
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final <R> Optional<R> reduce_(final BinaryOperator<R> op,
                                         final Function<? super JsPair, R> map,
                                         final Predicate<? super JsPair> predicate
                                        )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduce_(this);

    }


    @Override
    public final int size()
    {
        return map.size();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final Stream<JsPair> stream_()
    {
        return streamOfObj(this,
                           JsPath.empty()
                          );
    }

    @Override
    public final Stream<JsPair> stream()
    {
        return this.fields()
                   .stream()
                   .map(f ->
                        {
                            final JsPath key = JsPath.fromKey(f);
                            return JsPair.of(key,
                                             this.get(key)
                                            );
                        }

                       );
    }

    static Stream<JsPair> streamOfObj(final JsObj obj,
                                      final JsPath path
                                     )
    {

        requireNonNull(path);
        return requireNonNull(obj).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                         obj
                                                                        )),
                                               () -> obj.fields()
                                                        .stream()
                                                        .map(key -> JsPair.of(path.key(key),
                                                                              obj.get(Key.of(key))
                                                                             ))
                                                        .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                              pair.path
                                                                                                             ),
                                                                                             a -> streamOfArr(a,
                                                                                                              pair.path
                                                                                                             ),
                                                                                             e -> Stream.of(pair)
                                                                                            )
                                                                                 .apply(pair.elem))
                                              );

    }


    @Override
    public final JsObj tail(final String head)
    {
        return of(map.tail(head));
    }

    @Override
    public String toString()
    {
        return map.toString();
    }

    @Override
    public final JsObj union(final JsObj that
                            )
    {
        return union(this,
                     requireNonNull(that)
                    ).get();

    }

    private Trampoline<JsObj> union(JsObj a,
                                    JsObj b
                                   )
    {
        if (b.isEmpty()) return done(a);
        Map.Entry<String, JsElem> head = b.head();
        JsObj tail = b.tail(head.getKey());
        return union(a,
                     tail
                    ).map(it ->
                          it.putIfAbsent(JsPath.fromKey(head.getKey()),
                                         head::getValue
                                        ));
    }

    @Override
    public final boolean containsElem(final JsElem el)
    {
        return stream().anyMatch(p -> p.elem.equals(Objects.requireNonNull(el)));
    }

    @Override
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) // ARRAY_AS  should be a valid name, naming convention: xx_ traverses the whole json
    public final JsObj union_(final JsObj that,
                              final TYPE ARRAY_AS
                             )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return ifEmptyElse(() -> that,
                           () -> that.ifEmptyElse(() -> this,
                                                  () -> union_(this,
                                                               that,
                                                               ARRAY_AS
                                                              )
                                                  .get()
                                                 )
                          );

    }

    //squid:S00117 ARRAY_AS should be a valid name
    //squid:S00100 naming convention: xx_ traverses the whole json
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) //  ARRAY_AS  should be a valid name
    private Trampoline<JsObj> union_(final JsObj a,
                                     final JsObj b,
                                     final JsArray.TYPE ARRAY_AS
                                    )
    {

        if (b.isEmpty()) return done(a);
        Map.Entry<String, JsElem> head = b.head();
        JsObj tail = b.tail(head.getKey());
        Trampoline<JsObj> tailCall = more(() -> union_(a,
                                                       tail,
                                                       ARRAY_AS
                                                      ));
        return ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head.getKey()),
                                                                         head.getValue()
                                                                        )),
                             MatchExp.ifPredicateElse(e -> e.isJson() && e.isSameType(head.getValue()),
                                                      it ->
                                                      {
                                                          Json<?> obj = a.get(JsPath.empty()
                                                                                    .key(head.getKey()))
                                                                         .asJson();
                                                          Json<?> obj1 = head.getValue()
                                                                             .asJson();

                                                          Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().union_(obj,
                                                                                                                                              obj1,
                                                                                                                                              ARRAY_AS
                                                                                                                                             )
                                                                                                       );
                                                          return more(() -> tailCall).flatMap(tailResult -> headCall.map(headUnion_ ->
                                                                                                                         tailResult.put(JsPath.fromKey(head.getKey()),
                                                                                                                                        headUnion_
                                                                                                                                       )
                                                                                                                        )
                                                                                             );
                                                      },
                                                      it -> tailCall
                                                     )
                            )
        .apply(a.get(JsPath.empty()
                           .key(head.getKey())));


    }

    @Override
    public final boolean same(final JsObj obj)
    {
        final MyMap<?> other = ((AbstractJsObj) obj).map;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = other.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

        return fields().stream()
                       .allMatch(f ->
                                 other.getOptional(f)
                                      .map(it ->
                                           {
                                               final JsElem a = map.get(f);
                                               if (a.isObj() && it.isObj()) return a.asJsObj()
                                                                                    .same(it.asJsObj());
                                               else if (a.isArray() && it.isArray()) return a.asJsArray()
                                                                                             .same(it.asJsArray());
                                               else return it.equals(a);
                                           })
                                      .orElse(false) && other.keys()
                                                             .stream()
                                                             .allMatch(it -> map.contains(it)));
    }


    @SuppressWarnings("squid:S1602")
    // curly braces makes IntelliJ to format the code in a more legible way
    BiPredicate<String, JsPath> isReplaceWithEmptyJson(final M pmap)
    {
        return (head, tail) ->
        {
            return (!pmap.contains(head) || pmap.get(head)
                                                .isNotJson())
            ||
            (
            (tail.head()
                 .isKey() && pmap.get(head)
                                 .isArray()
            )
            ||
            (tail.head()
                 .isIndex() && pmap.get(head)
                                   .isObj()
            )
            );
        };
    }

}
