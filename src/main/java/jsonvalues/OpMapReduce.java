package jsonvalues;

import io.vavr.Tuple2;

import java.util.Optional;
import java.util.function.*;


final class OpMapReduce {

    static <T> Optional<T> reduceAllObj(final JsObj obj,
                                        final Predicate<? super JsPrimitive> predicate,
                                        final Function<? super JsPrimitive, T> map,
                                        final BinaryOperator<T> op,
                                        Optional<T> acc) {
        for (final Tuple2<String, JsValue> head : obj) {
            if (head._2.isObj()) {
                acc = reduceAllObj(head._2.toJsObj(),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else if (head._2.isArray()) {
                acc = reduceAllArr(head._2.toJsArray(),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else {
                acc = reducer(head._2.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }

        }
        return acc;
    }

    static <T> Optional<T> reduceAllArr(final JsArray arr,
                                        final Predicate<? super JsPrimitive> predicate,
                                        final Function<? super JsPrimitive, T> map,
                                        final BinaryOperator<T> op,
                                        Optional<T> acc) {
        for (final JsValue value : arr) {
            if (value.isObj()) {
                acc = reduceAllObj(value.toJsObj(),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }

            else if (value.isArray()) {
                acc = reduceAllArr(value.toJsArray(),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else {
                acc = reducer(
                        value.toJsPrimitive(),
                        acc,
                        predicate,
                        map,
                        op
                             );
            }
        }
        return acc;
    }

    static <T> Optional<T> reduceObj(final JsObj obj,
                                     final Predicate<? super JsPrimitive> predicate,
                                     final Function<? super JsPrimitive, T> map,
                                     final BinaryOperator<T> op,
                                     Optional<T> acc) {

        for (final Tuple2<String, JsValue> head : obj) {
            if (head._2.isPrimitive()) {
                acc = reducer(head._2.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }
        return acc;

    }

    private static <T> Optional<T> reducer(final JsPrimitive value,
                                           final Optional<T> acc,
                                           final Predicate<? super JsPrimitive> predicate,
                                           final Function<? super JsPrimitive, T> map,
                                           final BinaryOperator<T> op) {

        if (!predicate.test(value)) return acc;
        final T mapped = map.apply(value);
        final Optional<T> t = acc.map(it -> op.apply(it,
                                                     mapped
                                                    )
                                     );
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }

    static <T> Optional<T> reduceArr(final JsArray arr,
                                     final Predicate<? super JsPrimitive> predicate,
                                     final Function<? super JsPrimitive, T> map,
                                     final BinaryOperator<T> op,
                                     Optional<T> acc) {

        for (final JsValue value : arr) {
            if (value.isPrimitive()) {
                acc = reducer(value.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }
        return acc;
    }

    static <T> Optional<T> reduceObj(final JsObj obj,
                                     final BiPredicate<? super String, ? super JsPrimitive> predicate,
                                     final BiFunction<? super String, ? super JsPrimitive, T> map,
                                     final BinaryOperator<T> op) {
        Optional<T> acc = Optional.empty();
        for (final Tuple2<String, JsValue> head : obj) {
            if (head._2.isPrimitive()) {
                acc = reducer(head._1,
                              head._2.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }
        return acc;

    }

    private static <T> Optional<T> reducer(final String key,
                                           final JsPrimitive value,
                                           final Optional<T> acc,
                                           final BiPredicate<? super String, ? super JsPrimitive> predicate,
                                           final BiFunction<? super String, ? super JsPrimitive, T> map,
                                           final BinaryOperator<T> op) {
        if (!predicate.test(key,
                            value
                           )) return acc;
        final T mapped = map.apply(key,
                                   value
                                  );
        final Optional<T> t = acc.map(it -> op.apply(it,
                                                     mapped
                                                    )
                                     );
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }

    static <T> Optional<T> reduceArr(final JsArray arr,
                                     final BiPredicate<? super Integer, ? super JsPrimitive> predicate,
                                     final BiFunction<? super Integer, ? super JsPrimitive, T> map,
                                     final BinaryOperator<T> op) {
        Optional<T> acc = Optional.empty();

        for (int i = 0; i < arr.size(); i++) {
            JsValue value = arr.get(i);
            if (value.isPrimitive()) {
                acc = reducer(i,
                              value.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }

        return acc;
    }

    private static <T> Optional<T> reducer(final int i,
                                           final JsPrimitive value,
                                           final Optional<T> acc,
                                           final BiPredicate<? super Integer, ? super JsPrimitive> predicate,
                                           final BiFunction<? super Integer, ? super JsPrimitive, T> map,
                                           final BinaryOperator<T> op) {

        if (!predicate.test(i,
                            value
                           )) return acc;
        final T mapped = map.apply(i,
                                   value
                                  );
        final Optional<T> t = acc.map(it -> op.apply(it,
                                                     mapped
                                                    )
                                     );
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }

    private static <T> Optional<T> reducer(final JsPath path,
                                           final JsPrimitive value,
                                           final Optional<T> acc,
                                           final BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                           final BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                           final BinaryOperator<T> op) {

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
    }

    static <T> Optional<T> reduceAllObj(final JsObj obj,
                                        final JsPath startingPath,
                                        final BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                        final BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                        final BinaryOperator<T> op,
                                        Optional<T> acc) {

        for (final Tuple2<String, JsValue> head : obj) {
            final JsPath headPath = startingPath.key(head._1);
            if (head._2.isObj()) {
                acc = reduceAllObj(head._2.toJsObj(),
                                   startingPath.key(head._1),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else if (head._2.isArray()) {
                acc = reduceAllArr(head._2.toJsArray(),
                                   startingPath.index(-1),
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else {
                acc = reducer(headPath,
                              head._2.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }
        return acc;
    }

    static <T> Optional<T> reduceAllArr(final JsArray arr,
                                        final JsPath startingPath,
                                        final BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                        final BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                        final BinaryOperator<T> op,
                                        Optional<T> acc) {

        for (final JsValue value : arr) {
            final JsPath headPath = startingPath.inc();

            if (value.isObj()) {
                acc = reduceAllObj(value.toJsObj(),
                                   headPath,
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }

            else if (value.isArray()) {
                acc = reduceAllArr(value.toJsArray(),
                                   headPath,
                                   predicate,
                                   map,
                                   op,
                                   acc
                                  );
            }
            else {
                acc = reducer(headPath,
                              value.toJsPrimitive(),
                              acc,
                              predicate,
                              map,
                              op
                             );
            }
        }
        return acc;
    }
}

