package jsonvalues;

import io.vavr.Tuple2;

import java.util.Optional;
import java.util.function.*;


final class OpMapReduce<T> {
    private final Function<JsPrimitive, UnaryOperator<Optional<T>>> accumulator;

    OpMapReduce(final Predicate<? super JsPrimitive> predicate,
                final Function<? super JsPrimitive, T> map,
                final BinaryOperator<T> op
               ) {
        this.accumulator = value -> acc ->
        {
            if (!predicate.test(
                    value
                               )) return acc;
            final T mapped = map.apply(
                    value
                                      );
            final Optional<T> t = acc.map(it -> op.apply(it,
                                                         mapped
                                                        )
                                         );
            if (t.isPresent()) return t;
            return Optional.ofNullable(mapped);
        };
    }

    Optional<T> reduceAll(JsObj obj) {
        return reduceAllObj().apply(obj,
                                    Optional.empty()
                                   );
    }

    Optional<T> reduce(JsObj obj) {
        return reduceObj(
                        ).apply(obj,
                                Optional.empty()
                               );
    }

    Optional<T> reduceAll(JsArray arr) {
        return reduceAllArr(
                           ).apply(arr,
                                   Optional.empty()
                                  );
    }

    Optional<T> reduce(JsArray arr) {
        return reduceArr(
                        ).apply(arr,
                                Optional.empty()
                               );
    }


    private BiFunction<JsObj, Optional<T>, Optional<T>> reduceAllObj() {

        return (obj, acc) ->
        {
            for (final Tuple2<String, JsValue> head : obj) {
                if (head._2.isObj()) {
                    acc = reduceAllObj().apply(head._2.toJsObj(),
                                               acc
                                              );


                }

                else if (head._2.isArray()) {
                    acc = reduceAllArr().apply(head._2.toJsArray(),
                                               acc
                                              );
                }

                else {
                    acc = accumulator.apply(
                            head._2.toJsPrimitive()
                                           )
                                     .apply(acc);
                }

            }

            return acc;

        };


    }

    private BiFunction<JsArray, Optional<T>, Optional<T>> reduceAllArr() {

        return (arr, acc) -> {

            for (final JsValue value : arr) {
                if (value.isObj()) {
                    acc = reduceAllObj().apply(value.toJsObj(),
                                               acc
                                              );


                }

                else if (value.isArray()) {
                    acc = reduceAllArr().apply(value.toJsArray(),
                                               acc
                                              );
                }
                else {
                    acc = accumulator.apply(
                            value.toJsPrimitive()
                                           )
                                     .apply(acc);
                }
            }

            return acc;
        };


    }


    private BiFunction<JsObj, Optional<T>, Optional<T>> reduceObj() {

        return (obj, acc) ->
        {
            for (final Tuple2<String, JsValue> head : obj) {
                if (head._2.isPrimitive()) {
                    acc = accumulator.apply(
                            head._2.toJsPrimitive()
                                           )
                                     .apply(acc);
                }

            }

            return acc;

        };


    }

    private BiFunction<JsArray, Optional<T>, Optional<T>> reduceArr() {

        return (arr, acc) -> {
            for (final JsValue value : arr) {
                if (value.isPrimitive()) {
                    acc = accumulator.apply(
                            value.toJsPrimitive()
                                           )
                                     .apply(acc
                                           );
                }
            }
            return acc;
        };
    }
}
