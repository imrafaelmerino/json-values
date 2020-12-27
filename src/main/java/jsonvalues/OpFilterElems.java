package jsonvalues;

import java.util.function.BiPredicate;

abstract class OpFilterElems<T> {

     T json;

    OpFilterElems(final T json) {
        this.json = json;
    }

    abstract T filterAll(final JsPath startingPath,
                         final BiPredicate<? super JsPath,? super JsValue> predicate
                        );

    abstract T filter(final JsPath startingPath,
                      final BiPredicate<? super JsPath,? super JsValue> predicate
                     );


}
