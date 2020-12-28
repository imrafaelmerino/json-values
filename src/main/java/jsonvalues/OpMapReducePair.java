package jsonvalues;

import io.vavr.Tuple2;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;


final class OpMapReducePair<T> {
    private final BiFunction<JsPath, JsPrimitive, UnaryOperator<Optional<T>>> accumulator;

    OpMapReducePair(final BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                    final BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                    final BinaryOperator<T> op
                   ) {
        this.accumulator = (path, value) -> acc ->
        {
            if (!predicate.test(path,
                                value
                               )) return acc;
            final T mapped = map.apply(path,
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
        return reduceAllObj(JsPath.empty()).apply(obj,
                                                  Optional.empty()
                                                 );
    }

    Optional<T> reduce(JsObj obj) {
        return reduceObj(JsPath.empty()
                        ).apply(obj,
                                Optional.empty()
                               );
    }

    Optional<T> reduceAll(JsArray arr) {
        return reduceAllArr(JsPath.empty()
                                  .index(-1)
                           ).apply(arr,
                                   Optional.empty()
                                  );
    }

    Optional<T> reduce(JsArray arr) {
        return reduceArr(JsPath.empty()
                               .index(-1)
                        ).apply(arr,
                                Optional.empty()
                               );
    }


    private BiFunction<JsObj, Optional<T>, Optional<T>> reduceAllObj(final JsPath startingPath) {

        return (obj, acc) ->
        {
            for (final Tuple2<String, JsValue> head : obj) {
                final JsPath headPath = startingPath.key(head._1);
                if (head._2.isObj()) {
                    acc = reduceAllObj(startingPath.key(head._1)).apply(head._2.toJsObj(),
                                                                        acc
                                                                       );


                }

                else if (head._2.isArray()) {
                    acc = reduceAllArr(startingPath.index(-1)).apply(head._2.toJsArray(),
                                                                     acc
                                                                    );
                }

                else {
                    acc = accumulator.apply(headPath,
                                            head._2.toJsPrimitive()
                                           )
                                     .apply(acc);
                }

            }

            return acc;

        };


    }

    private BiFunction<JsArray, Optional<T>, Optional<T>> reduceAllArr(final JsPath startingPath) {

        return (arr, acc) -> {
            final JsPath headPath = startingPath.inc();

            for (final JsValue value : arr) {
                if (value.isObj()) {
                    acc = reduceAllObj(headPath).apply(value.toJsObj(),
                                                       acc
                                                      );


                }

                else if (value.isArray()) {
                    acc = reduceAllArr(headPath).apply(value.toJsArray(),
                                                       acc
                                                      );
                }
                else {
                    acc = accumulator.apply(headPath,
                                            value.toJsPrimitive()
                                           )
                                     .apply(acc);
                }
            }

            return acc;
        };


    }


    private BiFunction<JsObj, Optional<T>, Optional<T>> reduceObj(final JsPath startingPath) {

        return (obj, acc) ->
        {
            for (final Tuple2<String, JsValue> head : obj) {
                if (head._2.isPrimitive()) {
                    final JsPath headPath = startingPath.key(head._1);

                    acc = accumulator.apply(headPath,
                                            head._2.toJsPrimitive()
                                           )
                                     .apply(acc);
                }

            }

            return acc;

        };


    }

    private BiFunction<JsArray, Optional<T>, Optional<T>> reduceArr(final JsPath startingPath) {

        return (arr, acc) -> {

            for (final JsValue value : arr) {

                if (value.isPrimitive()) {
                    final JsPath headPath = startingPath.inc();

                    acc = accumulator.apply(headPath,
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
