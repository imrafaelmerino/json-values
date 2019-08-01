package jsonvalues;

import jsonvalues.JsArray.TYPE;
import jsonvalues.JsParser.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsParser.Event.END_ARRAY;
import static jsonvalues.JsParser.Event.END_OBJECT;
import static jsonvalues.MyScalaImpl.Map.EMPTY;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

class Functions
{

    @SuppressWarnings("squid:S00117") //  perfectly fine using _ to name constant
    static final JsPath MINUS_ONE_INDEX = JsPath.empty()
                                                .index(-1);

    private Functions()
    {
    }


    /**
     Declarative way of consuming an element based on its type
     @param ifValue the consumer to be invoked if this JsElem is a JsValue
     @param ifObj the consumer to be invoked if this JsElem is a JsObj
     @param ifArray the consumer to be invoked if this JsElem is a JsArray
     */
    static Consumer<JsElem> accept(final Consumer<JsElem> ifValue,
                                   final Consumer<JsObj> ifObj,
                                   final Consumer<JsArray> ifArray
                                  )
    {
        requireNonNull(ifValue);
        requireNonNull(ifObj);
        requireNonNull(ifArray);
        return e ->
        {
            if (e.isNotJson()) ifValue.accept(e);
            if (e.isObj()) ifObj.accept(e.asJsObj());
            if (e.isArray()) ifArray.accept(e.asJsArray());
        };

    }

    static UnsupportedOperationException castingError(String method,
                                                      Class<?> pclass
                                                     )
    {
        return new UnsupportedOperationException(String.format("%s of %s",
                                                               method,
                                                               pclass.getName()
                                                              ));
    }

    static Consumer<JsPair> consumeIf(Predicate<JsPair> predicate,
                                      Consumer<JsPair> consumer
                                     )
    {
        return pair ->
        {
            if (predicate.test(pair)) consumer.accept(pair);


        };
    }

    static JsElem get(final JsElem elem,
                      final JsPath path
                     )
    {
        assert elem != null;
        assert path != null;
        if (path.isEmpty()) return elem;
        if (elem.isNotJson() || elem.isNothing()) return JsNothing.NOTHING;
        return get(elem.asJson()
                       .get(path.head()),
                   path.tail()
                  );

    }

    /**
     Declarative way of implementing if(this.isArray()) return ifArr.apply(this.asJsArray()) else return ifNotArr.apply(this)
     @param ifArr the function to be applied if this JsElem is a JsArray
     @param ifNotArr the function to be applied if this JsElem is not a JsArray
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifArrElse(final Function<? super JsArray, T> ifArr,
                                             final Function<? super JsElem, T> ifNotArr

                                            )
    {

        return elem -> elem.isArray() ? requireNonNull(ifArr).apply(elem.asJsArray()) : requireNonNull(ifNotArr).apply(elem);
    }

    /**
     Declarative way of implementing if(this.isBool()) return ifBoolean.get() else ifNotBoolean.get()
     @param ifBoolean the function to be applied if this JsElem is a JsBool
     @param ifNotBoolean the function to be applied if this JsElem is not a JsBool
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifBoolElse(final Function<? super Boolean, T> ifBoolean,
                                              final Function<? super JsElem, T> ifNotBoolean
                                             )
    {
        return e -> e.isBool() ? requireNonNull(ifBoolean).apply(e.asJsBool().x) : requireNonNull(ifNotBoolean).apply(e);
    }

    /**
     Declarative way of returning an object based on the type of decimal number this element is
     @param ifDouble the function to be applied if this JsElem is a JsDouble
     @param ifBigDecimal the function to be applied if this JsElem is a JsBigDec
     @param ifOther the function to be applied if this JsElem is a not a decimal JsNumber
     @param <T> the type of the object returned
     @return an object of type T
     */
    static <T> Function<JsElem, T> ifDecimalElse(final DoubleFunction<T> ifDouble,
                                                 final Function<BigDecimal, T> ifBigDecimal,
                                                 final Function<? super JsElem, T> ifOther
                                                )
    {
        return elem ->
        {
            if (elem.isBigDec()) return requireNonNull(ifBigDecimal).apply(elem.asJsBigDec().x);
            if (elem.isDouble()) return requireNonNull(ifDouble).apply(elem.asJsDouble().x);
            return requireNonNull(ifOther).apply(elem);
        };

    }

    static <R> Function<JsPair, R> ifElse(Predicate<? super JsPair> predicate,
                                          Function<? super JsPair, R> ifTrue,
                                          Function<? super JsPair, R> ifFalse
                                         )
    {
        return pair -> predicate.test(pair) ? ifTrue.apply(pair) : ifFalse.apply(pair);
    }

    static <R> Function<JsPair, R> ifElse(Predicate<? super JsPair> predicate,
                                          Supplier<R> ifTrue,
                                          Supplier<R> ifFalse
                                         )
    {
        return pair -> predicate.test(pair) ? ifTrue.get() : ifFalse.get();
    }


    static <T> Function<JsElem, T> ifIntegralElse(final IntFunction<T> ifInt,
                                                  final LongFunction<T> ifLong,
                                                  final Function<BigInteger, T> ifBigInt,
                                                  final Function<? super JsElem, T> ifOther
                                                 )
    {
        return elem ->
        {
            if (elem.isLong()) return requireNonNull(ifLong).apply(elem.asJsLong().x);
            if (elem.isInt()) return requireNonNull(ifInt).apply(elem.asJsInt().x);
            if (elem.isBigInt()) return requireNonNull(ifBigInt).apply(elem.asJsBigInt().x);
            return requireNonNull(ifOther).apply(elem);
        };

    }

    static <T> Function<JsElem, T> ifJsonElse(final Function<? super JsObj, T> ifObj,
                                              final Function<? super JsArray, T> ifArr,
                                              final Function<? super JsElem, T> ifValue
                                             )
    {

        return elem ->
        {


            if (elem.isObj()) return ifObj.apply(elem.asJsObj());
            if (elem.isArray()) return ifArr.apply(elem.asJsArray());
            return ifValue.apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifJsonElse(final Function<Json<?>, T> ifJson,
                                              final Function<JsElem, T> ifNotJson
                                             )
    {

        return elem -> requireNonNull(elem).isJson() ? requireNonNull(ifJson).apply(elem.asJson()) : requireNonNull(ifNotJson).apply(elem);
    }


    static <T> Function<JsPair, T> ifJsonElse(BiFunction<JsPath, Json<?>, T> ifJson,
                                              BiFunction<JsPath, JsElem, T> ifNotJson
                                             )
    {

        return pair -> pair.elem.isJson() ? requireNonNull(ifJson).apply(pair.path,
                                                                         pair.elem.asJson()
                                                                        ) : requireNonNull(ifNotJson).apply(pair.path,
                                                                                                            pair.elem
                                                                                                           );
    }

    static <T> Function<JsElem, T> ifNothingElse(final Supplier<T> nothingSupplier,
                                                 final Function<JsElem, T> elseFn
                                                )
    {

        return elem -> elem.isNothing() ? requireNonNull(nothingSupplier).get() : requireNonNull(elseFn).apply(elem);
    }

    static <T> Function<JsElem, T> ifObjElse(final Function<? super JsObj, T> ifObj,
                                             final Function<? super JsElem, T> ifNotObj
                                            )
    {
        return elem ->
        {
            if (elem.isObj()) return requireNonNull(ifObj).apply(elem.asJsObj());
            else return requireNonNull(ifNotObj).apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifPredicateElse(final Predicate<JsElem> predicate,
                                                   final Function<JsElem, T> ifTrue,
                                                   final Function<JsElem, T> ifFalse
                                                  )
    {

        return elem ->
        {
            if (requireNonNull(predicate).test(elem)) return requireNonNull(ifTrue).apply(elem);
            return requireNonNull(ifFalse).apply(elem);
        };
    }

    static <T> Function<JsElem, T> ifStrElse(final Function<? super String, T> ifStr,
                                             final Function<? super JsElem, T> ifNotStr
                                            )
    {
        return elem -> elem.isStr() ? requireNonNull(ifStr).apply(elem.asJsStr().x) : requireNonNull(ifNotStr).apply(elem);
    }

    static <T> Function<JsElem, T> ifValueElse(final Function<JsElem, T> ifValue,
                                               final Function<JsObj, T> ifObj,
                                               final Function<JsArray, T> ifArray
                                              )
    {

        return e ->
        {
            if (e.isNotJson()) return requireNonNull(ifValue).apply(e);
            if (e.isObj()) return requireNonNull(ifObj).apply(e.asJsObj());
            else return requireNonNull(ifArray).apply(e.asJsArray());
        };

    }

    static Predicate<JsElem> isSameType(final JsElem that)
    {
        return e -> e.getClass() == requireNonNull(that).getClass();

    }


    static <T> Trampoline<Optional<T>> reduce(final JsObj obj,
                                              final BinaryOperator<T> op,
                                              final Function<? super JsPair, T> fn,
                                              final Predicate<? super JsPair> predicate,
                                              final JsPath path,
                                              final Optional<T> result
                                             )
    {

        return ifEmptyObjElse(obj,
                              done(result),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  return ifJsonElse(json -> more(() -> reduce(tail,
                                                                              op,
                                                                              fn,
                                                                              predicate,
                                                                              path,
                                                                              result
                                                                             )),
                                                    elem -> ifElse(predicate,
                                                                   p -> more(() -> reduce(tail,
                                                                                          op,
                                                                                          fn,
                                                                                          predicate,
                                                                                          path,
                                                                                          mapAndReduce(p,
                                                                                                       op,
                                                                                                       fn,
                                                                                                       result
                                                                                                      )
                                                                                         )),
                                                                   p -> more(() -> reduce(tail,
                                                                                          op,
                                                                                          fn,
                                                                                          predicate,
                                                                                          path,
                                                                                          result
                                                                                         ))
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))

                                                   )
                                  .apply(head.getValue());
                              }
                             );


    }

    static <T> Trampoline<Optional<T>> reduce(final JsArray arr,
                                              final BinaryOperator<T> op,
                                              final Function<? super JsPair, T> fn,
                                              final Predicate<? super JsPair> predicate,
                                              final JsPath path,
                                              final Optional<T> result
                                             )
    {

        return ifEmptyArrElse(arr,
                              done(result),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();
                                  return ifJsonElse(json -> more(() -> reduce(tail,
                                                                              op,
                                                                              fn,
                                                                              predicate,
                                                                              headPath,
                                                                              result
                                                                             )),
                                                    elem -> ifElse(predicate,
                                                                   p -> more(() -> reduce(tail,
                                                                                          op,
                                                                                          fn,
                                                                                          predicate,
                                                                                          headPath,
                                                                                          mapAndReduce(p,
                                                                                                       op,
                                                                                                       fn,
                                                                                                       result
                                                                                                      )
                                                                                         )),
                                                                   p -> more(() -> reduce(tail,
                                                                                          op,
                                                                                          fn,
                                                                                          predicate,
                                                                                          headPath,
                                                                                          result
                                                                                         ))
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))

                                                   )
                                  .apply(head);
                              }
                             );

    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static <T> Trampoline<Optional<T>> reduce_(final JsArray arr,
                                               final BinaryOperator<T> op,
                                               final Function<? super JsPair, T> fn,
                                               final Predicate<? super JsPair> predicate,
                                               final JsPath path,
                                               final Optional<T> result
                                              )
    {


        return ifEmptyArrElse(arr,
                              done(result),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  return ifJsonElse(json -> more(() -> reduce_(json,
                                                                               op,
                                                                               fn,
                                                                               predicate,
                                                                               headPath,
                                                                               result
                                                                              )
                                                                ).flatMap(r -> reduce_(tail,
                                                                                       op,
                                                                                       fn,
                                                                                       predicate,
                                                                                       path,
                                                                                       r
                                                                                      )),
                                                    elem -> ifElse(predicate,
                                                                   p -> more(() -> reduce_(tail,
                                                                                           op,
                                                                                           fn,
                                                                                           predicate,
                                                                                           headPath,
                                                                                           mapAndReduce(p,
                                                                                                        op,
                                                                                                        fn,
                                                                                                        result
                                                                                                       )
                                                                                          )),
                                                                   p -> more(() -> reduce_(tail,
                                                                                           op,
                                                                                           fn,
                                                                                           predicate,
                                                                                           headPath,
                                                                                           result
                                                                                          ))
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))

                                                   )
                                  .apply(head);
                              }
                             );


    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static <T> Trampoline<Optional<T>> reduce_(final JsObj obj,
                                               final BinaryOperator<T> op,
                                               final Function<? super JsPair, T> fn,
                                               final Predicate<? super JsPair> predicate,
                                               final JsPath path,
                                               final Optional<T> result
                                              )
    {
        return ifEmptyObjElse(obj,
                              done(result),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());
                                  return ifJsonElse(json -> more(() -> reduce_(json,
                                                                               op,
                                                                               fn,
                                                                               predicate,
                                                                               headPath,
                                                                               result
                                                                              )
                                                                ).flatMap(r -> reduce_(tail,
                                                                                       op,
                                                                                       fn,
                                                                                       predicate,
                                                                                       path,
                                                                                       r
                                                                                      ))
                                  ,
                                                    elem -> ifElse(predicate,
                                                                   p -> more(() -> reduce_(tail,
                                                                                           op,
                                                                                           fn,
                                                                                           predicate,
                                                                                           path,
                                                                                           mapAndReduce(p,
                                                                                                        op,
                                                                                                        fn,
                                                                                                        result
                                                                                                       )
                                                                                          )),
                                                                   p -> more(() -> reduce_(tail,
                                                                                           op,
                                                                                           fn,
                                                                                           predicate,
                                                                                           path,
                                                                                           result
                                                                                          ))
                                                                  ).apply(JsPair.of(headPath,
                                                                                    elem
                                                                                   ))

                                                   )
                                  .apply(head.getValue());

                              }
                             );

    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray _filterJsObj_(final JsArray arr,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                 final JsPath path
                                )
    {

        JsPath currentPath = path;
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsElem next = iterator.next();
            if (next.isObj() && predicate.negate()
                                         .test(currentPath,
                                               next.asJsObj()
                                              )
            ) iterator.remove();

        }

        return arr;

    }
    @SuppressWarnings("squid:S00100") // naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsObj _filterJsObj_(final JsObj obj,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate,
                               final JsPath path
                              )
    {

        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();

        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsElem value = entry.getValue();
            if (value.isObj() && predicate.negate()
                                          .test(path.key(entry.getKey()),
                                                value.asJsObj()
                                               )
            ) iterator.remove();


        }

        return obj;


    }
    @SuppressWarnings({"ReturnValueIgnored","squid:S00100"}) //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private static void _filterJsObj__(final Json<?> json,
                                       final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                       final JsPath path
                                      )
    {
        ifObjElse(it -> _filterJsObj__(it,
                                       predicate,
                                       path
                                      ),
                  it -> _filterJsObj__(it.asJsArray(),
                                       predicate,
                                       path.index(-1)

                                      )
                 )
        .apply(json);
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray _filterJsObj__(final JsArray arr,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                  final JsPath path
                                 )
    {
        JsPath currentPath = path;
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isJson())
            {
                if (pair.elem.isObj() && predicate.negate()
                                                  .test(pair.path,
                                                        pair.elem.asJsObj()
                                                       )
                ) iterator.remove();
                else
                    _filterJsObj__(pair.elem.asJson(),
                                   predicate,
                                   currentPath
                                  );
            }

        }
        return arr;
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsObj _filterJsObj__(final JsObj obj,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                final JsPath path
                               )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isJson())
            {

                if (pair.elem.isObj() && predicate.negate()
                                                  .test(pair.path,
                                                        pair.elem.asJsObj()
                                                       ))
                    iterator.remove();
                else
                    _filterJsObj__(pair.elem.asJson(),
                                   predicate,
                                   pair.path
                                  );
            }
        }

        return obj;

    }


    static JsObj _filterKeys_(final JsObj obj,
                              final Predicate<? super JsPair> predicate,
                              final JsPath path
                             )
    {

        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (predicate.negate()
                         .test(pair))
                iterator.remove();
        }
        return obj;
    }
    @SuppressWarnings({"ReturnValueIgnored","squid:S00100"})//  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private static void _filterKeys__(final Json<?> json,
                                      final Predicate<? super JsPair> predicate,
                                      final JsPath path
                                     )

    {
        ifObjElse(it -> _filterKeys__(it,
                                      predicate,
                                      path
                                     ),
                  it -> _filterKeys__(it.asJsArray(),
                                      predicate,
                                      path.index(-1)
                                     )
                 ).apply(json);
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray _filterKeys__(final JsArray arr,
                                 final Predicate<? super JsPair> predicate,
                                 final JsPath path
                                )
    {
        JsPath currentPath = path;
        for (final JsElem elem : arr)
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          elem
                                         );
            if (pair.elem.isJson())
                _filterKeys__(pair.elem.asJson(),
                              predicate,
                              currentPath
                             );

        }
        return arr;

    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsObj _filterKeys__(final JsObj obj,
                               final Predicate<? super JsPair> predicate,
                               final JsPath path
                              )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );
            if (predicate.negate()
                         .test(pair))
                iterator.remove();
            else if (pair.elem.isJson())
                _filterKeys__(pair.elem.asJson(),
                              predicate,
                              pair.path
                             );
        }

        return obj;


    }

    static JsArray _filterValues_(final JsArray arr,
                                  final Predicate<? super JsPair> predicate,
                                  final JsPath path
                                 )
    {
        JsPath currentPath = path;
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();

        }
        return arr;


    }

    static JsObj _filterValues_(final JsObj obj,
                                final Predicate<? super JsPair> predicate,
                                final JsPath path
                               )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson() && predicate.negate()
                                                  .test(pair))
                iterator.remove();

        }

        return obj;

    }
    @SuppressWarnings({"ReturnValueIgnored","squid:S00100"})//  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private static void _filterValues__(final Json<?> json,
                                        final Predicate<? super JsPair> predicate,
                                        final JsPath path
                                       )
    {
        ifObjElse(it -> _filterValues__(it,
                                        predicate,
                                        path
                                       ),
                  it -> _filterValues__(it.asJsArray(),
                                        predicate,
                                        path.index(-1)

                                       )
                 )
        .apply(json);

    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsArray _filterValues__(final JsArray arr,
                                   final Predicate<? super JsPair> predicate,
                                   final JsPath path
                                  )
    {
        JsPath currentPath = path;
        final Iterator<JsElem> iterator = arr.iterator();
        while (iterator.hasNext())
        {
            currentPath = currentPath.inc();
            final JsPair pair = JsPair.of(currentPath,
                                          iterator.next()
                                         );
            if (pair.elem.isNotJson())
            {
                if (predicate.negate()
                             .test(pair))
                    iterator.remove();
            } else
            {
                _filterValues__(pair.elem.asJson(),
                                predicate,
                                currentPath
                               );
            }
        }
        return arr;


    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static JsObj _filterValues__(final JsObj obj,
                                 final Predicate<? super JsPair> predicate,
                                 final JsPath path
                                )
    {
        final Iterator<Map.Entry<String, JsElem>> iterator = obj.iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, JsElem> entry = iterator.next();
            final JsPair pair = JsPair.of(path.key(entry.getKey()),
                                          entry.getValue()
                                         );

            if (pair.elem.isNotJson())
            {

                if (predicate.negate()
                             .test(pair))
                    iterator.remove();
            } else
                _filterValues__(pair.elem.asJson(),
                                predicate,
                                pair.path
                               );
        }

        return obj;

    }

    static Trampoline<JsObj> _mapJsObj_(final JsObj acc,
                                        final JsObj remaining,
                                        final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                        final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                        final JsPath path
                                       )
    {
        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapJsObj_(acc,
                                                                                           tail,
                                                                                           fn,
                                                                                           predicate,
                                                                                           path
                                                                                          ));
                                  return ifObjElse(obj -> ifElse(p -> predicate.test(p.path,
                                                                                     obj
                                                                                    ),
                                                                 p ->
                                                                 {
                                                                     obj.remove(head.getKey());
                                                                     return put(head.getKey(),
                                                                                fn.apply(p.path,
                                                                                         obj
                                                                                        ),
                                                                                () -> tailCall
                                                                               );
                                                                 }

                                  ,
                                                                 p -> put(head.getKey(),
                                                                          p.elem,
                                                                          () -> tailCall
                                                                         )
                                                                ).apply(JsPair.of(headPath,
                                                                                  obj
                                                                                 )),
                                                   value -> put(head.getKey(),
                                                                value,
                                                                () -> tailCall
                                                               )
                                                  ).apply(head.getValue());
                              }

                             );
    }

    static Trampoline<JsArray> _mapJsObj_(final JsArray acc,
                                          final JsArray remaining,
                                          final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                          final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                          final JsPath path
                                         )
    {
        return ifEmptyArrElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = more(() -> _mapJsObj_(acc,
                                                                                             tail,
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ));
                                  return ifObjElse(obj -> ifElse(p -> predicate.test(p.path,
                                                                                     obj
                                                                                    ),
                                                                 p -> put(new JsPath(headPath.last()),
                                                                          fn.apply(p.path,
                                                                                   obj
                                                                                  ),
                                                                          () -> tailCall
                                                                         ),
                                                                 p -> put(new JsPath(headPath.last()),
                                                                          p.elem,
                                                                          () -> tailCall
                                                                         )
                                                                ).apply(JsPair.of(headPath,
                                                                                  obj
                                                                                 )),
                                                   value -> put(new JsPath(headPath.last()),
                                                                value,
                                                                () -> tailCall
                                                               )
                                                  ).apply(head);
                              }

                             );
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsObj> _mapJsObj__(final JsObj acc,
                                         final JsObj remaining,
                                         final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                         final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                         final JsPath path
                                        )
    {
        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapJsObj__(acc,
                                                                                            tail,
                                                                                            fn,
                                                                                            predicate,
                                                                                            path
                                                                                           ));
                                  return ifJsonElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                       json
                                                                                      ),
                                                                   p -> put_(JsPath.of(head.getKey()),
                                                                             () ->
                                                                             {
                                                                                 final JsObj mapped = fn.apply(headPath,
                                                                                                               json
                                                                                                              );

                                                                                 return _mapJsObj__(mapped,
                                                                                                    mapped,
                                                                                                    fn,
                                                                                                    predicate,
                                                                                                    headPath
                                                                                                   );
                                                                             },
                                                                             () -> tailCall
                                                                            ),
                                                                   p -> put_(JsPath.of(head.getKey()),
                                                                             () -> _mapJsObj__(json,
                                                                                               json,
                                                                                               fn,
                                                                                               predicate,
                                                                                               headPath
                                                                                              ),
                                                                             () -> tailCall
                                                                            )
                                                                  ).apply(JsPair.of(headPath,
                                                                                    json
                                                                                   )
                                                                         ),
                                                    arr -> put_(JsPath.of(head.getKey()),
                                                                () -> _mapJsObj__(arr,
                                                                                  arr,
                                                                                  fn,
                                                                                  predicate,
                                                                                  headPath.index(-1)
                                                                                 ),
                                                                () -> tailCall
                                                               ),
                                                    value -> put(head.getKey(),
                                                                 value,
                                                                 () -> tailCall
                                                                )
                                                   ).apply(head.getValue());
                              }

                             );
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsArray> _mapJsObj__(final JsArray acc,
                                           final JsArray remaining,
                                           final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                           final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                           final JsPath path
                                          )
    {
        return ifEmptyArrElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = more(() -> _mapJsObj__(acc,
                                                                                              tail,
                                                                                              fn,
                                                                                              predicate,
                                                                                              headPath
                                                                                             ));
                                  return ifJsonElse(obj -> ifElse(p -> predicate.test(p.path,
                                                                                      obj
                                                                                     ),
                                                                  p -> putInArray_(new JsPath(headPath.last()),
                                                                                   () ->
                                                                                   {
                                                                                       final JsObj mapped = fn.apply(headPath,
                                                                                                                     obj
                                                                                                                    );
                                                                                       return _mapJsObj__(mapped,
                                                                                                          mapped,
                                                                                                          fn,
                                                                                                          predicate,
                                                                                                          headPath
                                                                                                         );
                                                                                   },
                                                                                   () -> tailCall
                                                                                  ),
                                                                  p -> putInArray_(new JsPath(headPath.last()),
                                                                                   () -> _mapJsObj__(obj,
                                                                                                     obj,
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     headPath
                                                                                                    ),
                                                                                   () -> tailCall
                                                                                  )
                                                                 ).apply(JsPair.of(headPath,
                                                                                   obj
                                                                                  )
                                                                        ),
                                                    arr -> putInArray_(new JsPath(headPath.last()),
                                                                       () -> _mapJsObj__(arr,
                                                                                         arr,
                                                                                         fn,
                                                                                         predicate,
                                                                                         headPath.index(-1)
                                                                                        ),
                                                                       () -> tailCall
                                                                      ),
                                                    value -> put(new JsPath(headPath.last()),
                                                                 value,
                                                                 () -> tailCall
                                                                )
                                                   )
                                  .apply(head);
                              }

                             );
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Trampoline<JsObj> _mapKeys_(final JsObj acc,
                                       final JsObj remaining,
                                       final Function<? super JsPair, String> fn,
                                       final Predicate<? super JsPair> predicate,
                                       final JsPath path
                                      )
    {

        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapKeys_(acc,
                                                                                          remaining.tail(head.getKey()),
                                                                                          fn,
                                                                                          predicate,
                                                                                          path
                                                                                         ));

                                  return ifElse(predicate,
                                                p -> removeOldKeyAndPutNew(head.getKey(),
                                                                           fn.apply(p),
                                                                           p.elem,
                                                                           () -> tailCall

                                                                          ),
                                                p -> put(head.getKey(),
                                                         p.elem,
                                                         () -> tailCall
                                                        )
                                               ).apply(JsPair.of(headPath,
                                                                 head.getValue()
                                                                ));


                              }
                             );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsArray> _mapKeys__(final JsArray acc,
                                          final JsArray remaining,
                                          final Function<? super JsPair, String> fn,
                                          final Predicate<? super JsPair> predicate,
                                          final JsPath path
                                         )
    {


        return ifEmptyArrElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {

                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = more(() -> _mapKeys__(acc,
                                                                                             remaining.tail(),
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ));

                                  return ifJsonElse(json -> putInArray_(new JsPath(headPath.last()),
                                                                        () -> _mapKeys__(json,
                                                                                         fn,
                                                                                         predicate,
                                                                                         headPath
                                                                                        ),
                                                                        () -> tailCall
                                                                       ),
                                                    e -> put(new JsPath(headPath.last()),
                                                             head,
                                                             () -> tailCall
                                                            )
                                                   )
                                  .apply(head);

                              }
                             );


    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsObj> _mapKeys__(final JsObj acc,
                                        final JsObj remaining,
                                        final Function<? super JsPair, String> fn,
                                        final Predicate<? super JsPair> predicate,
                                        final JsPath path
                                       )
    {

        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapKeys__(acc,
                                                                                           remaining.tail(head.getKey()),
                                                                                           fn,
                                                                                           predicate,
                                                                                           path
                                                                                          ));

                                  final JsPair pair = JsPair.of(headPath,
                                                                head.getValue()
                                                               );
                                  return ifElse(predicate,
                                                ifJsonElse((ppath, json) -> removeOldKeyAndPutNew_(head.getKey(),
                                                                                                   fn.apply(pair),
                                                                                                   () -> _mapKeys__(json,
                                                                                                                    fn,
                                                                                                                    predicate,
                                                                                                                    ppath
                                                                                                                   ),
                                                                                                   () -> tailCall
                                                                                                  ),
                                                           (p, elem) -> removeOldKeyAndPutNew(head.getKey(),
                                                                                                  fn.apply(pair),
                                                                                                  elem,
                                                                                                  () -> tailCall
                                                                                                 )
                                                          ),
                                                ifJsonElse((ppath, json) -> put_(JsPath.of(head.getKey()),
                                                                                 () -> _mapKeys__(json,
                                                                                                  fn,
                                                                                                  predicate,
                                                                                                  ppath
                                                                                                 ),
                                                                                 () -> tailCall
                                                                                ),
                                                           (p, elem) -> put(head.getKey(),
                                                                                elem,
                                                                                () -> tailCall
                                                                               )

                                                          )
                                               ).apply(pair);


                              }
                             );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Trampoline<JsArray> _mapValues_(final JsArray acc,
                                           final JsArray remaining,
                                           final Function<? super JsPair, ? extends JsElem> fn,
                                           final Predicate<? super JsPair> predicate,
                                           final JsPath path
                                          )
    {


        return ifEmptyArrElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = more(() -> _mapValues_(acc,
                                                                                              tail,
                                                                                              fn,
                                                                                              predicate,
                                                                                              headPath
                                                                                             ));

                                  return ifJsonElse(elem -> put(new JsPath(headPath.last()),
                                                                elem,
                                                                () -> tailCall
                                                               ),
                                                    elem -> ifElse(predicate,
                                                                   p -> put(new JsPath(headPath.last()),
                                                                            fn.apply(p),
                                                                            () -> tailCall
                                                                           ),
                                                                   p -> put(new JsPath(headPath.last()),
                                                                            elem,
                                                                            () -> tailCall
                                                                           )
                                                                  ).apply(JsPair.of(headPath,
                                                                                    elem
                                                                                   ))
                                                   ).apply(head);


                              }
                             );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object
    static Trampoline<JsObj> _mapValues_(final JsObj acc,
                                         final JsObj remaining,
                                         final Function<? super JsPair, ? extends JsElem> fn,
                                         final Predicate<? super JsPair> predicate,
                                         final JsPath path
                                        )
    {

        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapValues_(acc,
                                                                                            tail,
                                                                                            fn,
                                                                                            predicate,
                                                                                            path
                                                                                           ));

                                  return mapHead(fn,
                                                 predicate,
                                                 head,
                                                 headPath,
                                                 tailCall
                                                );

                              }
                             );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsArray> _mapValues__(final JsArray acc,
                                            final JsArray remaining,
                                            final Function<? super JsPair, ? extends JsElem> fn,
                                            final Predicate<? super JsPair> predicate,
                                            final JsPath path
                                           )
    {


        return ifEmptyArrElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = more(() -> _mapValues__(acc,
                                                                                               tail,
                                                                                               fn,
                                                                                               predicate,
                                                                                               headPath
                                                                                              ));


                                  return ifJsonElse(json -> putInArray_(new JsPath(headPath.last()),
                                                                        () -> _mapValues__(json,
                                                                                           fn,
                                                                                           predicate,
                                                                                           headPath
                                                                                          ),
                                                                        () -> tailCall
                                                                       ),
                                                    elem ->
                                                    ifElse(predicate,
                                                           p -> put(new JsPath(headPath.last()),
                                                                    fn.apply(p),
                                                                    () -> tailCall
                                                                   ),
                                                           p -> put(new JsPath(headPath.last()),
                                                                    elem,
                                                                    () -> tailCall
                                                                   )
                                                          )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))
                                                   )
                                  .apply(head);


                              }
                             );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    static Trampoline<JsObj> _mapValues__(final JsObj acc,
                                          final JsObj remaining,
                                          final Function<? super JsPair, ? extends JsElem> fn,
                                          final Predicate<? super JsPair> predicate,
                                          final JsPath path
                                         )
    {

        return ifEmptyObjElse(remaining,
                              done(acc),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = more(() -> _mapValues__(acc,
                                                                                             tail,
                                                                                             fn,
                                                                                             predicate,
                                                                                             path

                                                                                            ));

                                  return mapHead_(fn,
                                                  predicate,
                                                  head,
                                                  headPath,
                                                  tailCall,
                                                  json -> () -> _mapValues__(json,
                                                                             fn,
                                                                             predicate,
                                                                             headPath
                                                                            )
                                                 );

                              }
                             );


    }

    private static Trampoline<JsArray> appendBack(final JsElem head,
                                                  final Trampoline<Trampoline<JsArray>> tail
                                                 )
    {
        return more(tail).map(it -> it.append(head));
    }


    private static Trampoline<JsArray> appendFront(final JsElem head,
                                                   final Trampoline<Trampoline<JsArray>> tail
                                                  )
    {
        return more(tail).map(it -> it.prepend(head));
    }

    private static Trampoline<JsArray> appendFront_(final Trampoline<Trampoline<? extends Json<?>>> head,
                                                    final Trampoline<Trampoline<JsArray>> tail
                                                   )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(json::prepend));
    }

    static Trampoline<JsArray> filterJsObjs(final JsArray arr,
                                            final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                            final JsPath path
                                           )
    {


        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterJsObjs(tail,
                                                                                                          predicate,
                                                                                                          headPath
                                                                                                         ));
                                  return ifObjElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                      json
                                                                                     ),
                                                                  p -> appendFront(json,
                                                                                   () -> tailCall
                                                                                  ),
                                                                  p -> tailCall
                                                                 )
                                                   .apply(JsPair.of(headPath,
                                                                    json
                                                                   )),
                                                   value -> appendFront(value,
                                                                        () -> tailCall
                                                                       )
                                                  )
                                  .apply(head);
                              }

                             );


    }

    static Trampoline<JsObj> filterJsObjs(final JsObj obj,
                                          final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                          final JsPath path
                                         )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterJsObjs(tail,
                                                                                                        predicate,
                                                                                                        path
                                                                                                       ));
                                  return ifObjElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                      json
                                                                                     ),
                                                                  p -> put(head.getKey(),
                                                                           json,
                                                                           () -> tailCall
                                                                          )

                                  ,
                                                                  p -> tailCall
                                                                 )
                                                   .apply(JsPair.of(headPath,
                                                                    json
                                                                   )),
                                                   value -> put(head.getKey(),
                                                                value,
                                                                () -> tailCall
                                                               )
                                                  )
                                  .apply(head.getValue());
                              }

                             );
    }

    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json

    static Trampoline<JsArray> filterJsObjs_(final JsArray arr,
                                             final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                             final JsPath path
                                            )
    {


        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterJsObjs_(tail,
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          ));
                                  return ifJsonElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                       json
                                                                                      ),
                                                                   p -> appendFront_(() -> filterJsObjs_(json,
                                                                                                         predicate,
                                                                                                         headPath
                                                                                                        ),
                                                                                     () -> tailCall

                                                                                    ),
                                                                   p -> tailCall

                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     json
                                                                    )),
                                                    _arr -> appendFront_(() -> filterJsObjs_(_arr,
                                                                                             predicate,
                                                                                             headPath.index(-1)
                                                                                            ),
                                                                         () -> tailCall
                                                                        ),
                                                    value -> appendFront(value,
                                                                         () -> tailCall
                                                                        )
                                                   )
                                  .apply(head);
                              }

                             );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsObj> filterJsObjs_(final JsObj obj,
                                           final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                           final JsPath path
                                          )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterJsObjs_(tail,
                                                                                                         predicate,
                                                                                                         path
                                                                                                        ));
                                  return ifJsonElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                       json
                                                                                      ),
                                                                   p -> put_(JsPath.of(head.getKey()),
                                                                             () -> filterJsObjs_(json,
                                                                                                 predicate,
                                                                                                 headPath
                                                                                                ),
                                                                             () -> tailCall
                                                                            ),
                                                                   p -> tailCall
                                                                  ).apply(JsPair.of(headPath,
                                                                                    json
                                                                                   )),
                                                    arr -> put_(JsPath.of(head.getKey()),
                                                                () -> filterJsObjs_(arr,
                                                                                    predicate,
                                                                                    headPath.index(-1)
                                                                                   ),
                                                                () -> tailCall
                                                               ),
                                                    value -> put(head.getKey(),
                                                                 value,
                                                                 () -> tailCall
                                                                )
                                                   )
                                  .apply(head.getValue());
                              }

                             );

    }

    static Trampoline<JsObj> filterKeys(final JsObj obj,
                                        final Predicate<? super JsPair> predicate,
                                        final JsPath path
                                       )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterKeys(tail,
                                                                                                      predicate,
                                                                                                      path
                                                                                                     ));

                                  return ifElse(predicate,
                                                () -> put(head.getKey(),
                                                          head.getValue(),
                                                          () -> tailCall
                                                         ),


                                                () -> tailCall
                                               )
                                  .apply(JsPair.of(headPath,
                                                   head.getValue()
                                                  ));
                              }
                             );


    }
    //private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    @SuppressWarnings("squid:S1452")
    private static Trampoline<? extends Json<?>> filterKeys_(final Json<?> json,
                                                             final Predicate<? super JsPair> predicate,
                                                             final JsPath path
                                                            )

    {
        return ifObjElse(it -> filterKeys_(it,
                                           predicate,
                                           path
                                          ),
                         it -> filterKeys_(it.asJsArray(),
                                           predicate,
                                           path.index(-1)
                                          )
                        )
        .apply(json);
    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsArray> filterKeys_(final JsArray arr,
                                           final Predicate<? super JsPair> predicate,
                                           final JsPath path
                                          )
    {


        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {

                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterKeys_(tail,
                                                                                                         predicate,
                                                                                                         headPath
                                                                                                        ));


                                  return ifJsonElse(elem -> appendFront_(() -> filterKeys_(elem,
                                                                                           predicate,
                                                                                           headPath
                                                                                          ),
                                                                         () -> tailCall
                                                                        ),
                                                    elem -> appendFront(elem,
                                                                        () -> tailCall

                                                                       )
                                                   )
                                  .apply(head);
                              }
                             );


    }

    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsObj> filterKeys_(final JsObj obj,
                                         final Predicate<? super JsPair> predicate,
                                         final JsPath path
                                        )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterKeys_(tail,
                                                                                                       predicate,
                                                                                                       path
                                                                                                      ));
                                  return ifElse(predicate,
                                                () -> ifJsonElse(json -> put_(JsPath.of(head.getKey()),
                                                                              () -> filterKeys_(json,
                                                                                                predicate,
                                                                                                headPath

                                                                                               ),
                                                                              () -> tailCall
                                                                             ),
                                                                 value -> put(head.getKey(),
                                                                              value,
                                                                              () -> tailCall
                                                                             )

                                                                )
                                                .apply(head.getValue()),
                                                () -> tailCall
                                               ).apply(JsPair.of(headPath,
                                                                 head.getValue()
                                                                ));
                              }
                             );


    }

    static Trampoline<JsArray> filterValues(final JsArray arr,
                                            final Predicate<? super JsPair> predicate,
                                            final JsPath path
                                           )
    {


        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {

                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterValues(tail,
                                                                                                          predicate,
                                                                                                          headPath
                                                                                                         ));
                                  return ifJsonElse(elem -> appendFront(elem,
                                                                        () -> tailCall
                                                                       ),
                                                    elem -> ifElse(predicate,
                                                                   () -> appendFront(elem,
                                                                                     () -> tailCall
                                                                                    ),
                                                                   () -> tailCall
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))
                                                   )
                                  .apply(head);
                              }
                             );


    }

    static Trampoline<JsObj> filterValues(final JsObj obj,
                                          final Predicate<? super JsPair> predicate,
                                          final JsPath path
                                         )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterValues(tail,
                                                                                                        predicate,
                                                                                                        path
                                                                                                       ));
                                  return ifJsonElse(headElem -> put(head.getKey(),
                                                                    headElem,
                                                                    () -> tailCall
                                                                   ),
                                                    headElem -> ifElse(predicate,
                                                                       () -> put(head.getKey(),
                                                                                 headElem,
                                                                                 () -> tailCall
                                                                                ),
                                                                       () -> tailCall
                                                                      )
                                                    .apply(JsPair.of(headPath,
                                                                     headElem
                                                                    ))
                                                   )
                                  .apply(head.getValue());

                              }
                             );

    }
    //private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    @SuppressWarnings("squid:S1452")
    private static Trampoline<? extends Json<?>> filterValues_(final Json<?> json,
                                                               final Predicate<? super JsPair> predicate,
                                                               final JsPath path
                                                              )
    {

        return ifObjElse(it -> filterValues_(it,
                                             predicate,
                                             path
                                            ),
                         it -> filterValues_(it.asJsArray(),
                                             predicate,
                                             path.index(-1)
                                            )
                        )
        .apply(json);

    }

    static Trampoline<JsArray> filterValues_(final JsArray arr,
                                             final Predicate<? super JsPair> predicate,
                                             final JsPath path
                                            )
    {


        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {

                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> filterValues_(tail,
                                                                                                           predicate,
                                                                                                           headPath
                                                                                                          ));
                                  return ifJsonElse(elem -> appendFront_(() -> filterValues_(elem,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ),
                                                                         () -> tailCall
                                                                        ),
                                                    elem -> ifElse(predicate,
                                                                   () -> appendFront(elem,
                                                                                     () -> tailCall
                                                                                    ),
                                                                   () -> tailCall
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     elem
                                                                    ))
                                                   )
                                  .apply(head);
                              }
                             );


    }

    static Trampoline<JsObj> filterValues_(final JsObj obj,
                                           final Predicate<? super JsPair> predicate,
                                           final JsPath path
                                          )
    {


        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> filterValues_(tail,
                                                                                                         predicate,
                                                                                                         path
                                                                                                        ));
                                  return ifJsonElse(headElem -> put_(JsPath.of(head.getKey()),
                                                                     () -> filterValues_(headElem,
                                                                                         predicate,
                                                                                         headPath
                                                                                        ),
                                                                     () -> tailCall
                                                                    ),
                                                    headElem -> ifElse(predicate,
                                                                       () -> put(head.getKey(),
                                                                                 headElem,
                                                                                 () -> tailCall
                                                                                ),
                                                                       () -> tailCall
                                                                      )
                                                    .apply(JsPair.of(headPath,
                                                                     headElem
                                                                    ))
                                                   )
                                  .apply(head.getValue());

                              }
                             );

    }

    static <T> Trampoline<T> ifEmptyArrElse(final JsArray arr,
                                            final Trampoline<T> empty,
                                            final BiFunction<JsElem, JsArray, Trampoline<T>> fn
                                           )
    {


        if (arr.isEmpty()) return empty;

        final JsElem head = arr.head(); // when filtering mutable arrays, to remove indexes and not lose the track you have to iterate starting from the last

        final JsArray tail = arr.tail();

        return fn.apply(head,
                        tail
                       );

    }


    static <T> Trampoline<T> ifEmptyObjElse(final JsObj obj,
                                            final Trampoline<T> empty,
                                            final BiFunction<Map.Entry<String, JsElem>, JsObj, Trampoline<T>> fn
                                           )
    {


        if (obj.isEmpty()) return empty;

        final Map.Entry<String, JsElem> head = obj.head();

        final JsObj tail = obj.tail(head.getKey());

        return fn.apply(head,
                        tail
                       );

    }
    @SuppressWarnings("squid:S00100") //  perfectly fine _
    static Trampoline<JsArray> intersection(JsArray a,
                                            JsArray b,
                                            JsArray.TYPE ARRAY_AS
                                           )
    {


        switch (ARRAY_AS)
        {
            case SET:
                return intersectionAsSet(a,
                                         b
                                        );
            case LIST:
                return intersectionAsList(a,
                                          b
                                         );
            case MULTISET:
                return intersectionAsMultiSet(a,
                                              b
                                             );
        }

        throw new IllegalArgumentException(ARRAY_AS.name() + " option not supported");
    }
    @SuppressWarnings("squid:S00100") //  perfectly fine _
    static Trampoline<JsObj> intersection(final JsObj a,
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

        final JsElem bElem = b.get(head.getKey());


        if ((bElem.isJson() && bElem.asJson()
                                    .equals(head.getValue(),
                                            ARRAY_AS
                                           )) || bElem.equals(head.getValue())) return put(head.getKey(),
                                                                                           head.getValue(),
                                                                                           tailCall
                                                                                          );

        return more(tailCall);


    }

    private static Trampoline<JsArray> intersectionAsList(JsArray a,
                                                          JsArray b
                                                         )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final JsElem otherHead = b.head();
        final JsArray otherTail = b.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsList(tail,
                                                                                  otherTail
                                                                                 );

        if (head.equals(otherHead)) return appendFront(head,
                                                       tailCall
                                                      );
        return more(tailCall);


    }


    private static Trampoline<JsArray> intersectionAsMultiSet(JsArray a,
                                                              JsArray b
                                                             )
    {

        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsMultiSet(tail,
                                                                                      b
                                                                                     );

        if (b.containsElem(head)) return appendFront(head,
                                                     tailCall
                                                    );
        return more(tailCall);
    }

    private static Trampoline<JsArray> intersectionAsSet(JsArray a,
                                                         JsArray b
                                                        )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsArray tail = a.tail();

        final Trampoline<Trampoline<JsArray>> tailCall = () -> intersectionAsSet(tail,
                                                                                 b
                                                                                );

        if (b.containsElem(head) && !tail.containsElem(head)) return appendFront(head,
                                                                                 tailCall
                                                                                );
        return more(tailCall);


    }
    @SuppressWarnings("squid:S00100") //  perfectly fine _
    static Trampoline<JsObj> intersection_(final JsObj a,
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
        if (b.containsPath(head.getKey()))
        {

            final JsElem headOtherElement = b.get(JsPath.of(head.getKey()));
            if (headOtherElement.equals(head.getValue()))
            {
                return put(head.getKey(),
                           head.getValue(),
                           () -> intersection_(tail,
                                               b.tail(head.getKey()),
                                               ARRAY_AS
                                              )
                          );
            } else if (head.getValue()
                           .isJson() && isSameType(headOtherElement).test(head.getValue()))
            {//different but same container
                Json<?> obj = head.getValue()
                                  .asJson();
                Json<?> obj1 = headOtherElement.asJson();

                Trampoline<? extends Json<?>> headCall = more(() -> intersection_(obj,
                                                                                  obj1,
                                                                                  ARRAY_AS
                                                                                 ));

                return put_(JsPath.of(head.getKey()),
                            () -> headCall,
                            () -> tailCall
                           );
            }

        }

        return tailCall;


    }
    //squid:S1452 -> private method not exposed to the user. the wildcard allows to refactor some code, and Json<?> has only two possible types: JsObj or JsArr
    //squid:S00100 ->  naming convention: xx_ traverses the whole json
    //squid:S00117 -> ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00100","squid:S1452","squid:S00117"})
    private static Trampoline<? extends Json<?>> intersection_(final Json<?> a,
                                                               final Json<?> b,
                                                               final JsArray.TYPE ARRAY_AS
                                                              )
    {

        if (a.isObj() && b.isObj()) return intersection_(a.asJsObj(),
                                                         b.asJsObj(),
                                                         ARRAY_AS
                                                        );
        if (ARRAY_AS == TYPE.LIST) return intersection_(a.asJsArray(),
                                                        b.asJsArray()
                                                       );
        return intersection(a.asJsArray(),
                            b.asJsArray(),
                            ARRAY_AS
                           );


    }

    static Trampoline<JsArray> intersection_(final JsArray a,
                                             final JsArray b
                                            )
    {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);

        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = intersectionAsList(a.tail(),
                                                                b.tail()
                                                               );

        if (head.isJson() && isSameType(otherHead).test(head))
        {
            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> intersection_(obj,
                                                                              obj1,
                                                                              JsArray.TYPE.LIST
                                                                             ));
            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        } else if (head.equals(otherHead)) return appendFront(head,
                                                              () -> tailCall
                                                             );
        else return more(() -> tailCall);


    }

    static Trampoline<JsObj> mapJsObj(final JsObj obj,
                                      final BiFunction<? super JsPath, ? super jsonvalues.JsObj, jsonvalues.JsObj> fn,
                                      final BiPredicate<? super JsPath, ? super jsonvalues.JsObj> predicate,
                                      final JsPath path
                                     )
    {
        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapJsObj(tail,
                                                                                                    fn,
                                                                                                    predicate,
                                                                                                    path
                                                                                                   ));
                                  return ifObjElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                      json
                                                                                     ),
                                                                  p -> put(head.getKey(),
                                                                           fn.apply(p.path,
                                                                                    json
                                                                                   ),
                                                                           () -> tailCall
                                                                          )

                                  ,
                                                                  p -> put(head.getKey(),
                                                                           p.elem,
                                                                           () -> tailCall
                                                                          )
                                                                 )
                                                   .apply(JsPair.of(headPath,
                                                                    json
                                                                   )),
                                                   value -> put(head.getKey(),
                                                                value,
                                                                () -> tailCall
                                                               )
                                                  )
                                  .apply(head.getValue());
                              }

                             );
    }

    static Trampoline<JsArray> mapJsObj(final JsArray arr,
                                        final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                        final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                        final JsPath path
                                       )
    {
        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapJsObj(tail,
                                                                                                      fn,
                                                                                                      predicate,
                                                                                                      headPath
                                                                                                     ));
                                  return ifObjElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                      json
                                                                                     ),
                                                                  p -> appendFront(fn.apply(p.path,
                                                                                            json
                                                                                           ),
                                                                                   () -> tailCall
                                                                                  ),
                                                                  p -> appendFront(p.elem,
                                                                                   () -> tailCall
                                                                                  )
                                                                 )
                                                   .apply(JsPair.of(headPath,
                                                                    json
                                                                   )),
                                                   value -> appendFront(value,
                                                                        () -> tailCall
                                                                       )
                                                  )
                                  .apply(head);
                              }

                             );
    }

    static Trampoline<JsObj> mapJsObj_(final JsObj obj,
                                       final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                       final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                       final JsPath path
                                      )
    {
        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapJsObj_(tail,
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     path
                                                                                                    ));
                                  return ifJsonElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                       json
                                                                                      ),
                                                                   p -> put_(JsPath.of(head.getKey()),
                                                                             () -> mapJsObj_(fn.apply(p.path,
                                                                                                      json
                                                                                                     ),
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ),
                                                                             () -> tailCall
                                                                            ),
                                                                   p -> put_(JsPath.of(head.getKey()),
                                                                             () -> mapJsObj_(json,
                                                                                             fn,
                                                                                             predicate,
                                                                                             headPath
                                                                                            ),
                                                                             () -> tailCall
                                                                            )
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     json
                                                                    )),
                                                    arr -> put_(JsPath.of(head.getKey()),
                                                                () -> mapJsObj_(arr,
                                                                                fn,
                                                                                predicate,
                                                                                headPath.index(-1)
                                                                               ),
                                                                () -> tailCall
                                                               ),
                                                    value -> put(head.getKey(),
                                                                 value,
                                                                 () -> tailCall
                                                                )
                                                   )
                                  .apply(head.getValue());
                              }

                             );
    }

    static Trampoline<JsArray> mapJsObj_(final JsArray arr,
                                         final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                         final BiPredicate<? super JsPath, ? super JsObj> predicate,
                                         final JsPath path
                                        )
    {
        return ifEmptyArrElse(arr,
                              Trampoline.done(arr),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapJsObj_(tail,
                                                                                                       fn,
                                                                                                       predicate,
                                                                                                       headPath
                                                                                                      ));
                                  return ifJsonElse(json -> ifElse(p -> predicate.test(p.path,
                                                                                       json
                                                                                      ),
                                                                   p -> appendFront_(() -> mapJsObj_(fn.apply(p.path,
                                                                                                              json
                                                                                                             ),
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     headPath
                                                                                                    ),
                                                                                     () -> tailCall

                                                                                    ),
                                                                   p -> appendFront_(() -> mapJsObj_(json,
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     headPath
                                                                                                    ),
                                                                                     () -> tailCall
                                                                                    )
                                                                  )
                                                    .apply(JsPair.of(headPath,
                                                                     json
                                                                    )),
                                                    _arr -> appendFront_(() -> mapJsObj_(_arr,
                                                                                         fn,
                                                                                         predicate,
                                                                                         headPath.index(-1)
                                                                                        ),
                                                                         () -> tailCall
                                                                        ),
                                                    value -> appendFront(value,
                                                                         () -> tailCall
                                                                        )
                                                   )
                                  .apply(head);
                              }

                             );
    }

    static Trampoline<JsObj> mapKeys(final JsObj obj,
                                     final Function<? super JsPair, String> fn,
                                     final Predicate<? super JsPair> predicate,
                                     final JsPath path
                                    )
    {

        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapKeys(obj.tail(head.getKey()),
                                                                                                   fn,
                                                                                                   predicate,
                                                                                                   path
                                                                                                  ));

                                  return ifElse(predicate,
                                                p -> put(fn.apply(p),
                                                         p.elem,
                                                         () -> tailCall

                                                        ),
                                                p -> put(head.getKey(),
                                                         p.elem,
                                                         () -> tailCall
                                                        )


                                               ).apply(JsPair.of(headPath,
                                                                 head.getValue()
                                                                ));


                              }
                             );


    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsArray> mapKeys_(final JsArray array,
                                        final Function<? super JsPair, String> fn,
                                        final Predicate<? super JsPair> predicate,
                                        final JsPath path
                                       )
    {


        return ifEmptyArrElse(array,
                              Trampoline.done(array),
                              (head, tail) ->
                              {

                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapKeys_(array.tail(),
                                                                                                      fn,
                                                                                                      predicate,
                                                                                                      headPath
                                                                                                     ));

                                  return ifJsonElse(json -> appendFront_(() -> mapKeys_(json,
                                                                                        fn,
                                                                                        predicate,
                                                                                        headPath
                                                                                       ),
                                                                         () -> tailCall
                                                                        ),
                                                    e -> appendFront(head,
                                                                     () -> tailCall
                                                                    )
                                                   )
                                  .apply(head);

                              }
                             );


    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    static Trampoline<JsObj> mapKeys_(final JsObj obj,
                                      final Function<? super JsPair, String> fn,
                                      final Predicate<? super JsPair> predicate,
                                      final JsPath path
                                     )
    {

        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapKeys_(obj.tail(head.getKey()),
                                                                                                    fn,
                                                                                                    predicate,
                                                                                                    path
                                                                                                   ));
                                  JsPair pair = JsPair.of(headPath,
                                                          head.getValue()
                                                         );
                                  return ifElse(predicate,
                                                ifJsonElse((ppath, json) -> put_(JsPath.of(fn.apply(pair)),
                                                                                 () -> mapKeys_(json,
                                                                                                fn,
                                                                                                predicate,
                                                                                                ppath
                                                                                               ),
                                                                                 () -> tailCall
                                                                                ),
                                                           (p, elem) -> put(fn.apply(pair),
                                                                                elem,
                                                                                () -> tailCall
                                                                               )
                                                          ),
                                                ifJsonElse((ppath, json) -> put_(JsPath.of(head.getKey()),
                                                                                 () -> mapKeys_(json,
                                                                                                fn,
                                                                                                predicate,
                                                                                                ppath
                                                                                               ),
                                                                                 () -> tailCall
                                                                                ),
                                                           (p, elem) -> put(head.getKey(),
                                                                                elem,
                                                                                () -> tailCall
                                                                               )

                                                          )
                                               )
                                  .apply(pair);


                              }
                             );


    }

    static Trampoline<JsArray> mapValues(final JsArray array,
                                         final Function<? super JsPair, ? extends JsElem> fn,
                                         final Predicate<? super JsPair> predicate,
                                         final JsPath path
                                        )
    {


        return ifEmptyArrElse(array,
                              Trampoline.done(array),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapValues(tail,
                                                                                                       fn,
                                                                                                       predicate,
                                                                                                       headPath
                                                                                                      ));

                                  return ifJsonElse(elem -> appendFront(elem,
                                                                        () -> tailCall
                                                                       ),
                                                    elem -> ifElse(predicate,
                                                                   p -> appendFront(fn.apply(p),
                                                                                    () -> tailCall
                                                                                   ),
                                                                   p -> appendFront(elem,
                                                                                    () -> tailCall
                                                                                   )
                                                                  ).apply(JsPair.of(headPath,
                                                                                    elem
                                                                                   ))
                                                   ).apply(head);


                              }
                             );
    }

    static Trampoline<JsObj> mapValues(final JsObj obj,
                                       final Function<? super JsPair, ? extends JsElem> fn,
                                       final Predicate<? super JsPair> predicate,
                                       final JsPath path
                                      )
    {

        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapValues(tail,
                                                                                                     fn,
                                                                                                     predicate,
                                                                                                     path
                                                                                                    ));

                                  return mapHead(fn,
                                                 predicate,
                                                 head,
                                                 headPath,
                                                 tailCall
                                                );

                              }
                             );


    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    static Trampoline<JsArray> mapValues_(final JsArray array,
                                          final Function<? super JsPair, ? extends JsElem> fn,
                                          final Predicate<? super JsPair> predicate,
                                          final JsPath path
                                         )
    {


        return ifEmptyArrElse(array,
                              Trampoline.done(array),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.inc();

                                  final Trampoline<JsArray> tailCall = Trampoline.more(() -> mapValues_(tail,
                                                                                                        fn,
                                                                                                        predicate,
                                                                                                        headPath
                                                                                                       ));


                                  return ifJsonElse(json -> appendFront_(() -> mapValues_(json,
                                                                                          fn,
                                                                                          predicate,
                                                                                          headPath
                                                                                         ),
                                                                         () -> tailCall
                                                                        ),
                                                    elem -> ifElse(predicate,
                                                                   p -> appendFront(fn.apply(p),
                                                                                    () -> tailCall
                                                                                   ),
                                                                   p -> appendFront(elem,
                                                                                    () -> tailCall
                                                                                   )
                                                                  ).apply(JsPair.of(headPath,
                                                                                    elem
                                                                                   ))
                                                   )
                                  .apply(head);


                              }
                             );
    }

    static Trampoline<JsObj> mapValues_(final JsObj obj,
                                        final Function<? super JsPair, ? extends JsElem> fn,
                                        final Predicate<? super JsPair> predicate,
                                        final JsPath path
                                       )
    {

        return ifEmptyObjElse(obj,
                              Trampoline.done(obj),
                              (head, tail) ->
                              {
                                  final JsPath headPath = path.key(head.getKey());

                                  final Trampoline<JsObj> tailCall = Trampoline.more(() -> mapValues_(tail,
                                                                                                      fn,
                                                                                                      predicate,
                                                                                                      path
                                                                                                     ));

                                  return mapHead_(fn,
                                                  predicate,
                                                  head,
                                                  headPath,
                                                  tailCall,
                                                  json -> () -> mapValues_(json,
                                                                           fn,
                                                                           predicate,
                                                                           headPath
                                                                          )
                                                 );

                              }
                             );


    }

    static MyScalaImpl.Vector parse(final MyScalaImpl.Vector root,
                                    final JsParser parser
                                   ) throws MalformedJson
    {

        Event elem;
        MyScalaImpl.Vector newRoot = root;
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = newRoot.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    newRoot = newRoot.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    newRoot = newRoot.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    newRoot = newRoot.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    newRoot = newRoot.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final MyScalaImpl.Map newObj = parse(EMPTY,
                                                         parser
                                                        );
                    newRoot = newRoot.appendBack(new JsObjImmutableImpl(newObj));
                    break;

                case START_ARRAY:
                    final MyScalaImpl.Vector newVector = parse(MyScalaImpl.Vector.EMPTY,
                                                               parser
                                                              );

                    newRoot = newRoot.appendBack(new JsArrayImmutableImpl(newVector));
                    break;
                default:
                    throw unexpectedEventError(parser.currentEvent);
            }
        }

        return newRoot;


    }

    static MyScalaImpl.Vector parse(final MyScalaImpl.Vector root,
                                    final JsParser parser,
                                    final ParseOptions.Options options,
                                    final JsPath path
                                   ) throws MalformedJson
    {
        Event elem;
        MyScalaImpl.Vector newRoot = root;
        JsPair pair;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:

                    pair = JsPair.of(currentPath,
                                     parser.getJsString()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_NUMBER:
                    pair = JsPair.of(currentPath,
                                     parser.getJsNumber()
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;


                    break;

                case VALUE_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;

                    break;
                case VALUE_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case VALUE_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = condition.test(pair) ? newRoot.appendBack(options.elemMap.apply(pair)) : newRoot;
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new JsObjImmutableImpl(parse(EMPTY,
                                                                                  parser,
                                                                                  options,
                                                                                  currentPath
                                                                                 )));
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.appendBack(new JsArrayImmutableImpl(parse(MyScalaImpl.Vector.EMPTY,
                                                                                    parser,
                                                                                    options,
                                                                                    currentPath.index(-1)
                                                                                   )));
                    }

                    break;

                default:
                    throw unexpectedEventError(elem);


            }


        }

        return newRoot;
    }

    static void parse(MyJavaImpl.Vector root,
                      final JsParser parser
                     ) throws MalformedJson
    {
        Event elem;
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    root.appendBack(parser.getJsString());
                    break;
                case VALUE_NUMBER:
                    root.appendBack(parser.getJsNumber());
                    break;
                case VALUE_FALSE:
                    root.appendBack(FALSE);
                    break;
                case VALUE_TRUE:
                    root.appendBack(TRUE);
                    break;
                case VALUE_NULL:
                    root.appendBack(NULL);
                    break;
                case START_OBJECT:
                    final MyJavaImpl.Map obj = new MyJavaImpl.Map();

                    parse(obj,
                          parser
                         );
                    root.appendBack(new JsObjMutableImpl(obj));
                    break;

                case START_ARRAY:
                    final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                    parse(arr,
                          parser
                         );

                    root.appendBack(new JsArrayMutableImpl(arr));
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
    }

    static void parse(final MyJavaImpl.Vector root,
                      final JsParser parser,
                      final ParseOptions.Options options,
                      final JsPath path
                     ) throws MalformedJson
    {
        Event elem;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while ((elem = parser.next()) != END_ARRAY)
        {
            if (elem == null) throw unexpectedEventError(null);
            final JsPath currentPath = path.inc();
            switch (elem)
            {
                case VALUE_STRING:
                    consumeIf(condition,
                              p -> root.appendBack(options.elemMap.apply(p))
                             )
                    .accept(JsPair.of(currentPath,
                                      parser.getJsString()
                                     ));
                    break;
                case VALUE_NUMBER:
                    consumeIf(condition,
                              p -> root.appendBack(options.elemMap.apply(p))
                             )
                    .accept(JsPair.of(currentPath,
                                      parser.getJsNumber()
                                     ));
                    break;
                case VALUE_FALSE:
                    consumeIf(condition,
                              p -> root.appendBack(options.elemMap.apply(p))
                             )
                    .accept(JsPair.of(currentPath,
                                      FALSE
                                     ));
                    break;
                case VALUE_TRUE:
                    consumeIf(condition,
                              p -> root.appendBack(options.elemMap.apply(p))
                             )
                    .accept(JsPair.of(currentPath,
                                      TRUE
                                     ));
                    break;
                case VALUE_NULL:
                    consumeIf(condition,
                              p -> root.appendBack(options.elemMap.apply(p))
                             )
                    .accept(JsPair.of(currentPath,
                                      NULL
                                     ));
                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.appendBack(new JsObjMutableImpl(obj));
                    }
                    break;

                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );

                        root.appendBack(new JsArrayMutableImpl(arr));
                    }
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
    }

    static MyScalaImpl.Map parse(MyScalaImpl.Map root,
                                 final JsParser parser
                                ) throws MalformedJson
    {
        MyScalaImpl.Map newRoot = root;
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = newRoot.update(key,
                                             parser.getJsString()
                                            );
                    break;
                case VALUE_NUMBER:
                    newRoot = newRoot.update(key,
                                             parser.getJsNumber()
                                            );
                    break;
                case VALUE_FALSE:
                    newRoot = newRoot.update(key,
                                             FALSE
                                            );
                    break;
                case VALUE_TRUE:
                    newRoot = newRoot.update(key,
                                             TRUE
                                            );
                    break;
                case VALUE_NULL:
                    newRoot = newRoot.update(key,
                                             NULL
                                            );
                    break;
                case START_OBJECT:
                    final MyScalaImpl.Map newObj = parse(EMPTY,
                                                         parser
                                                        );
                    newRoot = newRoot.update(key,
                                             new JsObjImmutableImpl(newObj)
                                            );
                    break;
                case START_ARRAY:
                    final MyScalaImpl.Vector newArr = parse(MyScalaImpl.Vector.EMPTY,
                                                            parser
                                                           );
                    newRoot = newRoot.update(key,
                                             new JsArrayImmutableImpl(newArr)
                                            );
                    break;
                default:
                    throw unexpectedEventError(elem);
            }
        }
        return newRoot;


    }

    static MyScalaImpl.Map parse(final MyScalaImpl.Map root,
                                 final JsParser parser,
                                 final ParseOptions.Options options,
                                 final JsPath path
                                ) throws MalformedJson
    {

        MyScalaImpl.Map newRoot = root;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          parser.getJsString()
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_NUMBER:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          parser.getJsNumber()
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_TRUE:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          TRUE
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_FALSE:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          FALSE
                                                         ),
                                                key
                                               );
                    break;
                case VALUE_NULL:
                    newRoot = updateIfCondition(condition,
                                                options.elemMap,
                                                newRoot,
                                                JsPair.of(currentPath,
                                                          NULL
                                                         ),
                                                key
                                               );
                    break;

                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new JsObjImmutableImpl(parse(EMPTY,
                                                                              parser,
                                                                              options,
                                                                              currentPath
                                                                             ))
                                                );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
                                                 new JsArrayImmutableImpl(parse(MyScalaImpl.Vector.EMPTY,
                                                                                parser,
                                                                                options,
                                                                                currentPath.index(-1)
                                                                               ))
                                                );
                    }
                    break;
                default:
                    throw unexpectedEventError(parser.currentEvent);


            }


        }

        return newRoot;
    }

    static void parse(final MyJavaImpl.Map root,
                      final JsParser parser
                     ) throws MalformedJson
    {
        while (parser.next() != END_OBJECT)
        {
            final String key = parser.getString();
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);
            switch (elem)
            {
                case VALUE_STRING:
                    root.update(key,
                                parser.getJsString()
                               );
                    break;
                case VALUE_NUMBER:
                    root.update(key,
                                parser.getJsNumber()
                               );
                    break;
                case VALUE_FALSE:
                    root.update(key,
                                FALSE
                               );
                    break;
                case VALUE_TRUE:
                    root.update(key,
                                TRUE
                               );
                    break;
                case VALUE_NULL:
                    root.update(key,
                                NULL
                               );
                    break;
                case START_OBJECT:
                    final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                    parse(obj,
                          parser
                         );
                    root.update(key,
                                new JsObjMutableImpl(obj)
                               );
                    break;
                case START_ARRAY:
                    final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                    parse(arr,
                          parser
                         );
                    root.update(key,
                                new JsArrayMutableImpl(arr)
                               );
                    break;
                default:
                    throw unexpectedEventError(elem);
            }


        }
    }

    static void parse(final MyJavaImpl.Map root,
                      final JsParser parser,
                      final ParseOptions.Options options,
                      final JsPath path
                     ) throws MalformedJson
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.next() != END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getString());
            final JsPath currentPath = path.key(key);
            Event elem = parser.next();
            if (elem == null) throw unexpectedEventError(null);

            switch (elem)
            {
                case VALUE_STRING:
                    consumeIf(condition,
                              p -> root.update(key,
                                               options.elemMap.apply(p)
                                              )
                             )
                    .accept(JsPair.of(currentPath,
                                      parser.getJsString()
                                     ));

                    break;
                case VALUE_NUMBER:
                    consumeIf(condition,
                              p -> root.update(key,
                                               options.elemMap.apply(p)
                                              )
                             )
                    .accept(JsPair.of(currentPath,
                                      parser.getJsNumber()
                                     ));

                    break;
                case VALUE_FALSE:
                    consumeIf(condition,
                              p -> root.update(key,
                                               options.elemMap
                                               .apply(p)
                                              )
                             )
                    .accept(JsPair.of(currentPath,
                                      FALSE
                                     ));

                    break;
                case VALUE_TRUE:
                    consumeIf(condition,
                              p -> root.update(key,
                                               options.elemMap
                                               .apply(p)
                                              )
                             )
                    .accept(JsPair.of(currentPath,
                                      TRUE
                                     ));

                    break;
                case VALUE_NULL:

                    consumeIf(condition,
                              p -> root.update(key,
                                               options.elemMap
                                               .apply(p)
                                              )
                             )
                    .accept(JsPair.of(currentPath,
                                      NULL
                                     ));

                    break;
                case START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Map obj = new MyJavaImpl.Map();
                        parse(obj,
                              parser,
                              options,
                              currentPath
                             );
                        root.update(key,
                                    new JsObjMutableImpl(obj)
                                   );
                    }
                    break;
                case START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        final MyJavaImpl.Vector arr = new MyJavaImpl.Vector();
                        parse(arr,
                              parser,
                              options,
                              currentPath.index(-1)
                             );
                        root.update(key,
                                    new JsArrayMutableImpl(arr)
                                   );
                    }
                    break;
                default:
                    throw unexpectedEventError(elem);
            }


        }
    }

    static Trampoline<JsObj> put(final String key,
                                 final JsElem elem,
                                 final Trampoline<Trampoline<JsObj>> tail

                                )
    {
        return more(tail).map(it -> it.put(JsPath.of(key),
                                           elem
                                          ));
    }

    static Trampoline<JsArray> put(final JsPath path,
                                   final JsElem head,
                                   final Trampoline<Trampoline<JsArray>> tail
                                  )
    {
        return more(tail).map(it -> it.put(path,
                                           head
                                          ));
    }

    static Trampoline<JsArray> putInArray_(final JsPath path,
                                           final Trampoline<Trampoline<? extends Json<?>>> head,
                                           final Trampoline<Trampoline<JsArray>> tail
                                          )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(it ->
                                                   json.put(path,
                                                            it
                                                           )));
    }

    static Trampoline<JsObj> put_(final JsPath path,
                                  final Trampoline<Trampoline<? extends Json<?>>> head,
                                  final Trampoline<Trampoline<JsObj>> tail

                                 )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(it ->
                                                   json.put(path,
                                                            it
                                                           )
                                                  )
                                 );
    }

    private static Trampoline<JsObj> removeOldKeyAndPutNew(final String oldKey,
                                                           final String newKey,
                                                           final JsElem elem,
                                                           final Trampoline<Trampoline<JsObj>> tail

                                                          )
    {
        return more(tail).map(it ->
                              {
                                  it.remove(oldKey);
                                  return it.put(newKey,
                                                elem
                                               );
                              });
    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    private static Trampoline<JsObj> removeOldKeyAndPutNew_(final String oldKey,
                                                            final String newKey,
                                                            final Trampoline<Trampoline<? extends Json<?>>> head,
                                                            final Trampoline<Trampoline<JsObj>> tail

                                                           )

    {
        return more(tail).flatMap(json -> head.get()
                                              .map(it ->
                                                   {
                                                       json.remove(oldKey);
                                                       return json.put(JsPath.of(newKey),
                                                                       it
                                                                      );
                                                   }
                                                  )
                                 );
    }


    private static IllegalStateException unexpectedEventError(final @Nullable Event elem)
    {

        return new IllegalStateException(String.format("Unexpected event during parsing: %s",
                                                       elem
                                                      ));
    }


    static OptionalDouble bigDecimalToDouble(BigDecimal bigDecimal)
    {

        final double value = bigDecimal.doubleValue();
        if (value == Double.NEGATIVE_INFINITY) return OptionalDouble.empty();
        if (value == Double.POSITIVE_INFINITY) return OptionalDouble.empty();
        return OptionalDouble.of(value);

    }

    static OptionalInt bigIntToInt(BigInteger bigInteger)
    {
        try
        {
            return OptionalInt.of(bigInteger.intValueExact());
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }

    }

    static OptionalInt bigDecimalToInt(BigDecimal bigDecimal)
    {

        try
        {
            return OptionalInt.of(bigDecimal.intValueExact());
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }

    }

    static OptionalLong bigDecimalToLong(BigDecimal bigDecimal)
    {
        try
        {
            return OptionalLong.of(bigDecimal.longValueExact());
        }
        catch (Exception e)
        {
            return OptionalLong.empty();
        }

    }

    static Optional<BigInteger> bigDecimalToBigInteger(BigDecimal bigDecimal)
    {
        try
        {
            return Optional.of(bigDecimal.toBigIntegerExact());
        }
        catch (Exception e)
        {
            return Optional.empty();
        }

    }

    static Optional<BigInteger> doubleToBigInteger(double x)
    {
        try
        {
            return Optional.ofNullable(BigDecimal.valueOf(x)
                                                 .toBigIntegerExact());
        }
        catch (Exception e)
        {
            return Optional.empty();
        }
    }

    static boolean equals(BigInteger bigInteger,
                          BigDecimal bigDecimal
                         )
    {
        final Optional<BigInteger> optional = bigDecimalToBigInteger(bigDecimal);
        return optional.isPresent() && optional.get()
                                               .equals(bigInteger);
    }

    static boolean equals(double d,
                          BigInteger bigInteger
                         )
    {


        final Optional<BigInteger> x = doubleToBigInteger(d);
        return x.isPresent() && x.get()
                                 .equals(bigInteger);
    }

    static boolean equals(double d,
                          BigDecimal bigDecimal
                         )
    {

        //errorProne warning BigDecimalEquals -> compareTo instead of equals so 2.0 = 2.000
        return BigDecimal.valueOf(d)
                         .compareTo(bigDecimal) == 0;
    }

    static boolean equals(int x,
                          BigDecimal bigDecimal
                         )
    {
        final OptionalInt optional = bigDecimalToInt(bigDecimal);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    static boolean equals(long x,
                          BigDecimal bigDecimal
                         )
    {
        final OptionalLong optional = bigDecimalToLong(bigDecimal);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    static boolean equals(BigInteger bigInteger,
                          long x
                         )
    {
        final OptionalLong optional = bigIntToLong(bigInteger);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    static boolean equals(BigInteger bigInteger,
                          int x
                         )
    {
        final OptionalInt optional = bigIntToInt(bigInteger);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    static boolean equals(BigDecimal bigDecimal,
                          long x
                         )
    {
        final OptionalLong optional = bigDecimalToLong(bigDecimal);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    static boolean equals(BigDecimal bigDecimal,
                          int x
                         )
    {
        final OptionalInt optional = bigDecimalToInt(bigDecimal);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    static OptionalLong bigIntToLong(BigInteger bigInteger)
    {
        try
        {
            return OptionalLong.of(bigInteger.longValueExact());
        }
        catch (Exception e)
        {
            return OptionalLong.empty();
        }

    }

    static OptionalInt longToInt(Long _long)
    {
        try
        {
            return OptionalInt.of(Math.toIntExact(_long));
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }
    }

    static int hashCode(int n)
    {
        return n;
    }

    static int hashCode(long n)
    {
        return (int) (n ^ (n >>> 32));

    }


    static int hashCode(BigInteger n)
    {

        return n.hashCode();


    }

    static int hashCode(BigDecimal n)
    {


        return n.hashCode();

    }


    private static Trampoline<JsObj> mapHead(final Function<? super JsPair, ? extends JsElem> fn,
                                             final Predicate<? super JsPair> predicate,
                                             final Map.Entry<String, JsElem> head,
                                             final JsPath headPath,
                                             final Trampoline<JsObj> tailCall
                                            )
    {
        return ifJsonElse(elem -> put(head.getKey(),
                                      elem,
                                      () -> tailCall
                                     ),
                          elem -> ifElse(predicate,
                                         p -> put(head.getKey(),
                                                  fn.apply(p),
                                                  () -> tailCall
                                                 ),
                                         p -> put(head.getKey(),
                                                  elem,
                                                  () -> tailCall
                                                 )
                                        )
                          .apply(JsPair.of(headPath,
                                           elem
                                          ))
                         ).apply(head.getValue());
    }

    private static Trampoline<JsObj> mapHead_(final Function<? super JsPair, ? extends JsElem> fn,
                                              final Predicate<? super JsPair> predicate,
                                              final Map.Entry<String, JsElem> head,
                                              final JsPath headPath,
                                              final Trampoline<JsObj> tailCall,
                                              final Function<Json<?>, Trampoline<Trampoline<? extends Json<?>>>> headTrampoline
                                             )
    {
        return ifJsonElse(elem -> put_(JsPath.of(head.getKey()),
                                       headTrampoline.apply(elem),
                                       () -> tailCall
                                      ),
                          elem -> ifElse(predicate,
                                         p -> put(head.getKey(),
                                                  fn.apply(p),
                                                  () -> tailCall
                                                 ),
                                         p -> put(head.getKey(),
                                                  elem,
                                                  () -> tailCall
                                                 )
                                        ).apply(JsPair.of(headPath,
                                                          elem
                                                         ))
                         ).apply(head.getValue());
    }


    static Trampoline<JsArray> union(JsArray a,
                                     JsArray b,
                                     JsArray.TYPE ARRAY_AS
                                    )
    {


        switch (ARRAY_AS)
        {
            case SET:
                return unionAsSet(a,
                                  b
                                 );
            case LIST:
                return unionAsList(a,
                                   b
                                  );
            case MULTISET:
                return unionAsMultiSet(a,
                                       b
                                      );
        }

        throw new IllegalArgumentException(ARRAY_AS.name() + " option not supported");

    }

    static Trampoline<JsObj> union(JsObj a,
                                   JsObj b
                                  )
    {


        if (b.isEmpty()) return done(a);

        Map.Entry<String, JsElem> head = b.head();

        JsObj tail = b.tail(head.getKey());


        return union(a,
                     tail
                    ).map(it ->
                          it.putIfAbsent(JsPath.of(head.getKey()),
                                         head::getValue
                                        ));
    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public static Trampoline<JsObj> combiner_(final JsObj a,
                                              final JsObj b
                                             )
    {

        if (b.isEmpty()) return done(a);

        Map.Entry<String, JsElem> head = b.head();

        JsObj tail = b.tail(head.getKey());

        Trampoline<JsObj> tailCall = more(() -> combiner_(a,
                                                          tail
                                                         ));

        return ifNothingElse(() -> put(head.getKey(),
                                       head.getValue(),
                                       () -> tailCall
                                      ),
                             ifPredicateElse(e -> e.isJson() && isSameType(head.getValue()).test(e),
                                             it ->
                                             {
                                                 Json<?> obj = a.get(JsPath.empty()
                                                                           .key(head.getKey()))
                                                                .asJson();
                                                 Json<?> obj1 = head.getValue()
                                                                    .asJson();

                                                 Trampoline<? extends Json<?>> headCall = more(() -> combiner_(obj,
                                                                                                               obj1
                                                                                                              )
                                                                                              );

                                                 return put_(JsPath.of(head.getKey()),
                                                             () -> headCall,
                                                             () -> tailCall
                                                            );
                                             },
                                             it -> tailCall
                                            )

                            ).apply(a.get(JsPath.empty()
                                                .key(head.getKey())));


    }
    @SuppressWarnings("squid:S00100") //  perfectly fine _
    static Trampoline<JsObj> union_(final JsObj a,
                                    final JsObj b,
                                    final TYPE ARRAY_AS
                                   )
    {

        if (b.isEmpty()) return done(a);

        Map.Entry<String, JsElem> head = b.head();

        JsObj tail = b.tail(head.getKey());

        Trampoline<JsObj> tailCall = more(() -> union_(a,
                                                       tail,
                                                       ARRAY_AS
                                                      ));

        return ifNothingElse(() -> put(head.getKey(),
                                       head.getValue(),
                                       () -> tailCall
                                      ),
                             ifPredicateElse(e -> e.isJson() && isSameType(head.getValue()).test(e),
                                             it ->
                                             {
                                                 Json<?> obj = a.get(JsPath.empty()
                                                                           .key(head.getKey()))
                                                                .asJson();
                                                 Json<?> obj1 = head.getValue()
                                                                    .asJson();

                                                 Trampoline<? extends Json<?>> headCall = more(() -> union_(obj,
                                                                                                            obj1,
                                                                                                            ARRAY_AS
                                                                                                           )
                                                                                              );

                                                 return put_(JsPath.of(head.getKey()),
                                                             () -> headCall,
                                                             () -> tailCall
                                                            );
                                             },
                                             it -> tailCall
                                            )

                            ).apply(a.get(JsPath.empty()
                                                .key(head.getKey())));


    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    private static Trampoline<? extends Json<?>> combiner_(final Json<?> a,
                                                           final Json<?> b
                                                          )
    {

        if (a.isObj() && b.isObj()) return combiner_(a.asJsObj(),
                                                     b.asJsObj()
                                                    );
        return combiner_(a.asJsArray(),
                         b.asJsArray()
                        );


    }
    @SuppressWarnings("squid:S00100") //  perfectly fine _
    private static Trampoline<? extends Json<?>> union_(final Json<?> a,
                                                        final Json<?> b,
                                                        final JsArray.TYPE ARRAY_AS
                                                       )
    {

        if (a.isObj() && b.isObj()) return union_(a.asJsObj(),
                                                  b.asJsObj(),
                                                  ARRAY_AS
                                                 );
        if (ARRAY_AS == TYPE.LIST) return union_(a.asJsArray(),
                                                 b.asJsArray()
                                                );

        return union(a.asJsArray(),
                     b.asJsArray(),
                     ARRAY_AS
                    );


    }

    static Trampoline<JsArray> union_(final JsArray a,
                                      final JsArray b
                                     )
    {

        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = union_(a.tail(),
                                                    b.tail()
                                                   );


        if (head.isJson() && isSameType(otherHead).test(head))
        {


            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> union_(obj,
                                                                       obj1,
                                                                       JsArray.TYPE.LIST
                                                                      ));


            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        }

        return appendFront(head,
                           () -> tailCall
                          );


    }


    private static <T> Trampoline<Optional<T>> reduce_(final Json<?> json,
                                                       final BinaryOperator<T> op,
                                                       final Function<? super JsPair, T> fn,
                                                       final Predicate<? super JsPair> predicate,
                                                       final JsPath headPath,
                                                       final Optional<T> result

                                                      )
    {
        if (json.isObj()) return reduce_(json.asJsObj(),
                                         op,
                                         fn,
                                         predicate,
                                         headPath,
                                         result
                                        );
        else return reduce_(json.asJsArray(),
                            op,
                            fn,
                            predicate,
                            headPath.index(-1),
                            result
                           );
    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private static Trampoline<? extends Json<?>> _mapKeys__(final Json<?> json,
                                                            final Function<? super JsPair, String> fn,
                                                            final Predicate<? super JsPair> predicate,
                                                            final JsPath path
                                                           )

    {
        return ifObjElse(it -> _mapKeys__(it,
                                          it,
                                          fn,
                                          predicate,
                                          path
                                         ),
                         it -> _mapKeys__(it.asJsArray(),
                                          it.asJsArray(),
                                          fn,
                                          predicate,
                                          path.index(-1)
                                         )
                        )
        .apply(json);

    }
    @SuppressWarnings("squid:S00100") //  naming convention: _xx_ returns immutable object, xx_ traverses the whole json
    private static Trampoline<? extends Json<?>> _mapValues__(Json<?> json,
                                                              Function<? super JsPair, ? extends JsElem> fn,
                                                              Predicate<? super JsPair> predicate,
                                                              JsPath path
                                                             )

    {
        return ifObjElse(it -> _mapValues__(it,
                                            it,
                                            fn,
                                            predicate,
                                            path
                                           ),
                         it -> _mapValues__(it.asJsArray(),
                                            it.asJsArray(),
                                            fn,
                                            predicate,
                                            path.index(-1)
                                           )
                        ).apply(json);

    }


    private static <T> Optional<T> mapAndReduce(final JsPair p,
                                                final BinaryOperator<T> op,
                                                final Function<? super JsPair, T> fn,
                                                final Optional<T> result
                                               )
    {
        final T mapped = fn.apply(p);

        final Optional<T> t = result.map(it -> op.apply(it,
                                                        mapped
                                                       ));
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    private static Trampoline<? extends Json<?>> mapKeys_(final Json<?> json,
                                                          final Function<? super JsPair, String> fn,
                                                          final Predicate<? super JsPair> predicate,
                                                          final JsPath path
                                                         )

    {
        return ifObjElse(it -> mapKeys_(it,
                                        fn,
                                        predicate,
                                        path
                                       ),
                         it -> mapKeys_(it.asJsArray(),
                                        fn,
                                        predicate,
                                        path.index(-1)
                                       )
                        )
        .apply(json);

    }
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    private static Trampoline<? extends Json<?>> mapValues_(Json<?> json,
                                                            Function<? super JsPair, ? extends JsElem> fn,
                                                            Predicate<? super JsPair> predicate,
                                                            JsPath path
                                                           )

    {
        return ifObjElse(it -> mapValues_(it,
                                          fn,
                                          predicate,
                                          path
                                         ),
                         it -> mapValues_(it.asJsArray(),
                                          fn,
                                          predicate,
                                          path.index(-1)
                                         )
                        ).apply(json);

    }


    private static Trampoline<JsArray> unionAsList(final JsArray a,
                                                   final JsArray b
                                                  )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        final Trampoline<JsArray> tailCall = unionAsList(a.tail(),
                                                         b.tail()
                                                        );
        return appendFront(a.head(),
                           () -> tailCall
                          );


    }
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public static Trampoline<JsArray> combiner_(final JsArray a,
                                                final JsArray b
                                               )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);


        final JsElem head = a.head();
        final JsElem otherHead = b.head();

        final Trampoline<JsArray> tailCall = combiner_(a.tail(),
                                                       b.tail()
                                                      );


        if (head.isJson() && isSameType(otherHead).test(head))
        {


            final Json<?> obj = head.asJson();
            final Json<?> obj1 = otherHead.asJson();

            Trampoline<? extends Json<?>> headCall = more(() -> combiner_(obj,
                                                                          obj1
                                                                         ));


            return appendFront_(() -> headCall,
                                () -> tailCall
                               );
        }

        return appendFront(head.isNull() ? otherHead : head,
                           () -> tailCall
                          );


    }


    private static Trampoline<JsArray> unionAsMultiSet(final JsArray a,
                                                       final JsArray b
                                                      )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        return more(() -> () -> a.appendAll(b));

    }

    private static Trampoline<JsArray> unionAsSet(final JsArray a,
                                                  final JsArray b
                                                 )
    {
        if (b.isEmpty()) return done(a);

        if (a.isEmpty()) return done(b);

        JsElem last = b.last();

        final Trampoline<JsArray> initCall = unionAsSet(a,
                                                        b.init()
                                                       );

        if (!a.containsElem(last)) return appendBack(last,
                                                     () -> initCall
                                                    );


        return more(() -> initCall);
    }

    private static MyScalaImpl.Map updateIfCondition(Predicate<? super JsPair> condition,
                                                     Function<? super JsPair, ? extends JsElem> elemMap,
                                                     MyScalaImpl.Map map,
                                                     final JsPair pair,
                                                     final String key
                                                    )
    {


        if (condition.test(pair)) map = map.update(key,
                                                   elemMap.apply(pair)
                                                  );
        return map;
    }


    static void throwErrorIfMutableElemFound(Collection<? extends JsElem> elems)
    {

        for (JsElem elem : elems)
        {
            if (isMutable(elem))
                throw new UnsupportedOperationException("all the elements have to be immutable when calling the 'of' factory methods. Use JsObj.of(...) and JsArray.of(...) methods instead.");
        }

    }

    static JsElem throwErrorIfMutableElem(JsElem elem)
    {
        if (isMutable(elem))
            throw new UnsupportedOperationException("all the elements have to be immutable when calling the 'of' factory methods. Use JsObj.of(...) and JsArray.of(...) methods instead.");
        return elem;
    }

    static void throwErrorIfImmutableElemFound(Collection<? extends JsElem> elems)
    {
        for (JsElem elem : elems)
        {
            if (isImmutable(elem))
                throw new UnsupportedOperationException("all the elements have to be mutable when calling the '_of_' factory methods. Use JsObj._of_(...) and JsArray._of_(...) methods instead.");
        }
    }


    static JsElem throwErrorIfImmutableElem(JsElem elem)
    {
        if (isImmutable(elem))
            throw new UnsupportedOperationException("all the elements have to be mutable when calling the '_of_' factory methods. Use JsObj._of_(...) and JsArray._of_(...) methods instead.");
        return elem;
    }

    private static boolean isImmutable(final JsElem elem)
    {
        return Objects.requireNonNull(elem)
                      .isJson(Json::isImmutable);
    }

    private static boolean isMutable(final JsElem elem)
    {
        return Objects.requireNonNull(elem)
                      .isJson(Json::isMutable);
    }
}
