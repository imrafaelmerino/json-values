package jsonvalues;

import io.vavr.Tuple2;

import java.util.Optional;
import java.util.function.*;


final class OpMapReduce {

    private OpMapReduce() {
    }

    static <T> Optional<T> reduceObj(JsObj obj,
                                     Predicate<? super JsPrimitive> predicate,
                                     Function<? super JsPrimitive, T> map,
                                     BinaryOperator<T> op,
                                     Optional<T> acc) {
        for (Tuple2<String, JsValue> head : obj) {
            if (head._2.isObj()) {
                acc = reduceObj(head._2.toJsObj(),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else if (head._2.isArray()) {
                acc = reduceArr(head._2.toJsArray(),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else {
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

    static <T> Optional<T> reduceArr(JsArray arr,
                                     Predicate<? super JsPrimitive> predicate,
                                     Function<? super JsPrimitive, T> map,
                                     BinaryOperator<T> op,
                                     Optional<T> acc) {
        for (JsValue value : arr) {
            if (value.isObj()) {
                acc = reduceObj(value.toJsObj(),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else if (value.isArray()) {
                acc = reduceArr(value.toJsArray(),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else {
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


    private static <T> Optional<T> reducer(JsPrimitive value,
                                           Optional<T> acc,
                                           Predicate<? super JsPrimitive> predicate,
                                           Function<? super JsPrimitive, T> map,
                                           BinaryOperator<T> op) {

        if (!predicate.test(value)) return acc;
        T mapped = map.apply(value);
        Optional<T> t = acc.map(it -> op.apply(it,
                                               mapped
                                )
        );
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);

    }


    private static <T> Optional<T> reducer(JsPath path,
                                           JsPrimitive value,
                                           Optional<T> acc,
                                           BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                           BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                           BinaryOperator<T> op) {

        if (!predicate.test(path,
                            value
        )) return acc;
        T mapped = map.apply(path,
                             value
        );
        Optional<T> t = acc.map(it -> op.apply(it,
                                               mapped
                                )
        );
        if (t.isPresent()) return t;
        return Optional.ofNullable(mapped);
    }

    static <T> Optional<T> reduceObj(JsObj obj,
                                     JsPath startingPath,
                                     BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                     BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                     BinaryOperator<T> op,
                                     Optional<T> acc) {

        for (Tuple2<String, JsValue> head : obj) {
            JsPath headPath = startingPath.key(head._1);
            if (head._2.isObj()) {
                acc = reduceObj(head._2.toJsObj(),
                                startingPath.key(head._1),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else if (head._2.isArray()) {
                acc = reduceArr(head._2.toJsArray(),
                                startingPath.index(-1),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else {
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

    static <T> Optional<T> reduceArr(JsArray arr,
                                     JsPath startingPath,
                                     BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                                     BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                                     BinaryOperator<T> op,
                                     Optional<T> acc) {

        for (JsValue value : arr) {
            JsPath headPath = startingPath.inc();

            if (value.isObj()) {
                acc = reduceObj(value.toJsObj(),
                                headPath,
                                predicate,
                                map,
                                op,
                                acc
                );
            } else if (value.isArray()) {
                acc = reduceArr(value.toJsArray(),
                                headPath.index(-1),
                                predicate,
                                map,
                                op,
                                acc
                );
            } else {
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

