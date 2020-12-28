package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class OpFilterObjs<T> {

    T json;

    OpFilterObjs(final T json) {
        this.json = json;
    }

    abstract T filter(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsObj> predicate
                     );

    abstract T filterAll(final JsPath startingPath,
                         final BiPredicate<? super JsPath, ? super JsObj> predicate
                        );


    abstract T filter(final Predicate<? super JsObj> predicate);

    abstract T filterAll(final Predicate<? super JsObj> predicate);
}
