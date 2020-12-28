package jsonvalues;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class OpFilterElems<T> {

    T json;

    OpFilterElems(final T json) {
        this.json = json;
    }

    abstract T filterAll(final JsPath startingPath,
                         final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                        );

    abstract T filter(final JsPath startingPath,
                      final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
                     );

    abstract T filterAll(final Predicate<? super JsPrimitive> predicate
                        );

    abstract T filter(final Predicate<? super JsPrimitive> predicate);

}
