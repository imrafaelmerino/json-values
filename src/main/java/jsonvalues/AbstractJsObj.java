package jsonvalues;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import jsonvalues.JsArray.TYPE;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
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
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Explicit instantiation of JsObj interface to reduce class file size in subclasses.
 */
abstract class AbstractJsObj implements JsObj
{

    protected HashMap<String, JsValue> map;

    AbstractJsObj(final HashMap<String, JsValue> myMap)
    {
        assert myMap != null;
        this.map = myMap;
    }


    @Override
    public final boolean equals(final @Nullable Object that)
    {
        if (!(that instanceof AbstractJsObj)) return false;
        if (this == that) return true;
        if (getClass() != that.getClass()) return false;
        final AbstractJsObj thatMap = (AbstractJsObj) that;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = thatMap.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

        return fields().stream()
                       .allMatch(f ->
                                 thatMap.map.get(f)
                                            .map(it -> it.equals(map.get(f)
                                                                    .get()))
                                            .getOrElse(false) && thatMap.fields()
                                                                        .stream()
                                                                        .allMatch(it -> map.containsKey(it)));
    }

    @Override
    public final Set<String> fields()
    {
        return map.keySet().toJavaSet();
    }


    @Override
    public final JsValue get(final Position position)
    {
        return requireNonNull(position).match(key -> map.getOrElse(key,
                                                                   NOTHING),
                                              index -> NOTHING
                                             );
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }

    @Override
    public final Tuple2<String, JsValue> head()
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
        Tuple2<String, JsValue> head = a.head();
        JsObj tail = a.tail();
        final Trampoline<Trampoline<JsObj>> tailCall = () -> intersection(tail,
                                                                          b,
                                                                          ARRAY_AS
                                                                         );
        final JsValue bElem = b.get(JsPath.fromKey(head._1));

        return ((bElem.isJson() && bElem.asJson()
                                        .equals(head._2,
                                                ARRAY_AS
                                               )) || bElem.equals(head._2)) ?
        more(tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                        head._2
                                       )) :
        more(tailCall);
    }

    @Override
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) //  ARRAY_AS should be a valid name, naming convention: _ traverses recursively
    public final JsObj intersectionAll(final JsObj that,
                                       final TYPE ARRAY_AS
                                      )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return intersectionAll(this,
                               that,
                               ARRAY_AS
                              ).get();

    }

    @SuppressWarnings({"squid:S00117", "squid:S00100"}) // ARRAY_AS should be a valid name for an enum constant, naming convention _
    private Trampoline<JsObj> intersectionAll(final JsObj a,
                                              final JsObj b,
                                              final JsArray.TYPE ARRAY_AS
                                             )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);
        Tuple2<String, JsValue> head = a.head();

        JsObj tail = a.tail();

        final Trampoline<JsObj> tailCall = more(() -> intersectionAll(tail,
                                                                      b,
                                                                      ARRAY_AS
                                                                     ));
        if (b.containsPath(JsPath.fromKey(head._1)))
        {

            final JsValue headOtherElement = b.get(JsPath.fromKey(head._1));
            if (headOtherElement.equals(head._2))
            {
                return more(() -> intersectionAll(tail,
                                                  b.tail(),
                                                  ARRAY_AS
                                                 )).map(it -> it.put(JsPath.fromKey(head._1),
                                                                   head._2
                                                                  ));

            } else if (head._2
            .isJson() && head._2
            .isSameType(headOtherElement))
            {//different but same container
                Json<?> obj = head._2
                .asJson();
                Json<?> obj1 = headOtherElement.asJson();

                Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersectionAll(obj,
                                                                                                                    obj1,
                                                                                                                    ARRAY_AS
                                                                                                                   )
                                                             );
                return more(() -> tailCall).flatMap(json -> headCall
                                                    .map(it -> json.put(JsPath.fromKey(head._1),
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
    public final <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                           final Function<? super JsPair, R> map,
                                           final Predicate<? super JsPair> predicate
                                          )
    {
        return new OpMapReduce<>(predicate,
                                 map,
                                 op
        ).reduceAll(this);

    }


    @Override
    public final int size()
    {
        return map.size();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final Stream<JsPair> streamAll()
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
    public final JsObj tail()
    {
        return new ImmutableJsObj(map.tail());
    }

    @Override
    public String toString()
    {
        if (map.isEmpty()) return "{}";


        return map.keysIterator()
                            .map(key -> String.format("\"%s\":%s",
                                                          key,
                                                          map.apply(key)
                                                         ))
                            .mkString("{",
                                      ",",
                                      "}"
                                     );
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
        Tuple2<String, JsValue> head = b.head();
        JsObj tail = b.tail();
        return union(a,
                     tail
                    ).map(it ->
                          it.putIfAbsent(JsPath.fromKey(head._1),
                                         () -> head._2
                                        ));
    }

    @Override
    public final boolean containsElem(final JsValue el)
    {
        return stream().anyMatch(p -> p.elem.equals(Objects.requireNonNull(el)));
    }

    @Override
    @SuppressWarnings({"squid:S00117", "squid:S00100"}) // ARRAY_AS  should be a valid name, naming convention: xx_ traverses the whole json
    public final JsObj unionAll(final JsObj that,
                                final TYPE ARRAY_AS
                               )
    {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return ifEmptyElse(() -> that,
                           () -> that.ifEmptyElse(() -> this,
                                                  () -> unionAll(this,
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
    private Trampoline<JsObj> unionAll(final JsObj a,
                                       final JsObj b,
                                       final JsArray.TYPE ARRAY_AS
                                      )
    {

        if (b.isEmpty()) return done(a);
        Tuple2<String, JsValue> head = b.head();
        JsObj tail = b.tail();
        Trampoline<JsObj> tailCall = more(() -> unionAll(a,
                                                         tail,
                                                         ARRAY_AS
                                                        ));
        return ifNothingElse(() -> more(() -> tailCall).map(it -> it.put(JsPath.fromKey(head._1),
                                                            head._2
                                                           )),
        MatchExp.ifPredicateElse(e -> e.isJson() && e.isSameType(head._2),
                                 it ->
                                 {
                                     Json<?> obj = a.get(JsPath.empty()
                                                               .key(head._1))
                                                    .asJson();
                                     Json<?> obj1 = head._2
                                                        .asJson();

                                     Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().unionAll(obj,
                                                                                                                           obj1,
                                                                                                                           ARRAY_AS
                                                                                                                          )
                                                                                  );
                                     return more(() -> tailCall).flatMap(tailResult -> headCall.map(headUnion_ ->
                                                                                                    tailResult.put(JsPath.fromKey(head._1),
                                                                                                                   headUnion_
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

    @Override
    public final boolean same(final JsObj obj)
    {
        final HashMap<String, JsValue> other = ((AbstractJsObj) obj).map;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = other.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

        return fields().stream()
                       .allMatch(f ->
                                 other.get(f)
                                      .map(it ->
                                           {
                                               final JsValue a = map.get(f).get();
                                               if (a.isObj() && it.isObj()) return a.asJsObj()
                                                                                    .same(it.asJsObj());
                                               else if (a.isArray() && it.isArray()) return a.asJsArray()
                                                                                             .same(it.asJsArray());
                                               else return it.equals(a);
                                           })
                                      .getOrElse(false) && other.keySet()
                                                             .toJavaStream()
                                                             .allMatch(it -> map.containsKey(it)));
    }


    @SuppressWarnings("squid:S1602")
        // curly braces makes IntelliJ to format the code in a more legible way
    BiPredicate<String, JsPath> isReplaceWithEmptyJson(final HashMap<String, JsValue> pmap)
    {
        return (head, tail) ->
        {
            return (!pmap.containsKey(head) || !pmap.get(head)
                                                    .filter(JsValue::isNotJson)
                                                    .isEmpty())
            ||
            (
            (tail.head()
                 .isKey() && !pmap.get(head)
                                  .filter(JsValue::isArray)
                                  .isEmpty())
            )
            ||
            (tail.head()
                 .isIndex() && !pmap.get(head)
                                    .filter(JsValue::isObj)
                                    .isEmpty());
        };
    }

    static HashMap<String, JsValue> parse(final JsonParser parser
                                         ) throws IOException
    {
        HashMap<String, JsValue> map = HashMap.empty();
        String key = parser.nextFieldName();
        for (; key != null; key = parser.nextFieldName())
        {
            JsValue elem;
            switch (parser.nextToken()
                          .id())
            {
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    elem = new ImmutableJsObj(parse(parser)

                    );
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new ImmutableJsArray(AbstractJsArray.parse(parser
                                                                               )

                    );
                    break;
                default:
                    throw InternalError.tokenNotExpected(parser.currentToken()
                                                               .name());


            }
            map = map.put(key,
                          elem
                         );
        }

        return map;

    }

    static HashMap<String, JsValue> parse(final JsonParser parser,
                                          final ParseBuilder.Options options,
                                          final JsPath path
                                         ) throws IOException
    {

        HashMap<String, JsValue> map = HashMap.empty();
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getCurrentName());
            final JsPath currentPath = path.key(key);
            final JsPair pair;
            switch (parser.nextToken()
                          .id())
            {
                case JsonTokenId.ID_STRING:
                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;
                case JsonTokenId.ID_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;
                case JsonTokenId.ID_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;
                case JsonTokenId.ID_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    map = (condition.test(pair)) ? map.put(key,
                                                           options.elemMap.apply(pair)
                                                          ) : map;
                    break;

                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        map = map.put(key,
                                      new ImmutableJsObj(parse(parser,
                                                               options,
                                                               currentPath
                                                              )
                                      )
                                     );
                    }
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        map = map.put(key,
                                      new ImmutableJsArray(AbstractJsArray.parse(parser,
                                                                                 options,
                                                                                           currentPath.index(-1)
                                                                                          )
                                      )
                                     );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(parser.currentToken()
                                                               .name());
            }
        }
        return map;
    }

}
