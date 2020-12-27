package jsonvalues;

import java.util.function.BiPredicate;

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
}
