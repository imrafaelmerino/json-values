package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class OpFilterKeys<T> {

    T json;

    OpFilterKeys(final T json
                ) {
        this.json = json;
    }

    abstract T filterAll(final JsPath startingPath,
                         final BiPredicate<? super JsPath, ? super JsValue> predicate
                        );

    abstract T filter(final BiPredicate<? super JsPath, ? super JsValue> predicate);


    abstract T filterAll(final Predicate<? super String> predicate);

    abstract T filter(final Predicate<? super String> predicate);

}
