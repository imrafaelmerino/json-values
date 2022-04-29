package jsonvalues;


import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 Java doesn't support Pattern Matching, but we can implement some matching expressions using high
 order functions.
 */
final class MatchExp {

    private MatchExp() {
    }


    /**
     return a matching expression to extract objs and arrays out of json elements.

     @param ifObj   function to be applied if the JsValue is a JsObj
     @param ifArr   function to be applied if the JsValue is not a JsArr
     @param ifValue function to be applied if the JsValue is not a Json
     @param <T>     the type of the result
     @return a function that takes a JsValue and returns an object of type T
     */
    public static <T> Function<JsValue, T> ifJsonElse(final Function<? super JsObj, T> ifObj,
                                                      final Function<? super JsArray, T> ifArr,
                                                      final Function<? super JsValue, T> ifValue
                                                     ) {

        return elem ->
        {
            if (elem.isObj()) return requireNonNull(ifObj).apply(elem.toJsObj());
            if (elem.isArray()) return requireNonNull(ifArr).apply(elem.toJsArray());
            return ifValue.apply(elem);
        };
    }

    /**
     return a matching expression to extract jsons out of json elements.

     @param ifJson    function to be applied if the JsValue is a Json
     @param ifNotJson function to be applied if the JsValue is not a Json
     @param <T>       the type of the result
     @return a function that takes a JsValue and returns an object of type T
     */
    public static <T> Function<JsValue, T> ifJsonElse(final Function<Json<?>, T> ifJson,
                                                      final Function<JsValue, T> ifNotJson
                                                     ) {
        return elem -> requireNonNull(elem).isJson() ? requireNonNull(ifJson).apply(elem.toJson()) : requireNonNull(ifNotJson).apply(elem);
    }

    /**
     return a matching expression to extract JsNothing out of json elements.

     @param nothingSupplier supplier to be invoked if the JsValue is JsNothing
     @param elseFn          function to be applied if the JsValue is not JsNothing
     @param <T>             the type of the result
     @return a function that takes a JsValue and returns an object of type T
     */
    public static <T> Function<JsValue, T> ifNothingElse(final Supplier<T> nothingSupplier,
                                                         final Function<JsValue, T> elseFn
                                                        ) {
        return elem -> elem.isNothing() ? requireNonNull(nothingSupplier).get() : requireNonNull(elseFn).apply(elem);
    }

}
