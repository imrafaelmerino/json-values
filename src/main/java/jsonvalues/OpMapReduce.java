package jsonvalues;


import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;


final class OpMapReduce {

  private OpMapReduce() {
  }

  static <T> T reduceObj(JsObj obj,
                         Predicate<? super JsPrimitive> predicate,
                         Function<? super JsPrimitive, T> map,
                         BinaryOperator<T> op,
                         T acc
                        ) {
    for (var head : obj) {
      if (head.value()
              .isObj()) {
        acc = reduceObj(head.value()
                            .toJsObj(),
                        predicate,
                        map,
                        op,
                        acc
                       );
      } else if (head.value()
                     .isArray()) {
        acc = reduceArr(head.value()
                            .toJsArray(),
                        predicate,
                        map,
                        op,
                        acc
                       );
      } else {
        acc = reducer(head.value()
                          .toJsPrimitive(),
                      acc,
                      predicate,
                      map,
                      op
                     );
      }

    }
    return acc;
  }

  static <T> T reduceArr(JsArray arr,
                         Predicate<? super JsPrimitive> predicate,
                         Function<? super JsPrimitive, T> map,
                         BinaryOperator<T> op,
                         T acc
                        ) {
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


  private static <T> T reducer(JsPrimitive value,
                               T acc,
                               Predicate<? super JsPrimitive> predicate,
                               Function<? super JsPrimitive, T> map,
                               BinaryOperator<T> op
                              ) {

    if (!predicate.test(value)) {
      return acc;
    }
    T mapped = map.apply(value);

    if (acc != null) {
      return op.apply(acc,
                      mapped
                     );
    }

    return mapped;

  }


  private static <T> T reducer(JsPath path,
                               JsPrimitive value,
                               T acc,
                               BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                               BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                               BinaryOperator<T> op
                              ) {

    if (!predicate.test(path,
                        value
                       )) {
      return acc;
    }
    T mapped = map.apply(path,
                         value
                        );

    if (acc != null) {
      return op.apply(acc,
                      mapped
                     );
    }
    return mapped;
  }

  static <T> T reduceObj(JsObj obj,
                         JsPath startingPath,
                         BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                         BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                         BinaryOperator<T> op,
                         T acc
                        ) {

    for (var head : obj) {
      JsPath headPath = startingPath.key(head.key());
      if (head.value()
              .isObj()) {
        acc = reduceObj(head.value()
                            .toJsObj(),
                        startingPath.key(head.key()),
                        predicate,
                        map,
                        op,
                        acc
                       );
      } else if (head.value()
                     .isArray()) {
        acc = reduceArr(head.value()
                            .toJsArray(),
                        startingPath.index(-1),
                        predicate,
                        map,
                        op,
                        acc
                       );
      } else {
        acc = reducer(headPath,
                      head.value()
                          .toJsPrimitive(),
                      acc,
                      predicate,
                      map,
                      op
                     );
      }
    }
    return acc;
  }

  static <T> T reduceArr(JsArray arr,
                         JsPath startingPath,
                         BiPredicate<? super JsPath, ? super JsPrimitive> predicate,
                         BiFunction<? super JsPath, ? super JsPrimitive, T> map,
                         BinaryOperator<T> op,
                         T acc
                        ) {

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

