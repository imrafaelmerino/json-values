package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Function;

abstract class OpMapKeys<T> {
    T json;

    OpMapKeys(final T json) {
        this.json = json;
    }

    abstract T map(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                   final JsPath startingPath
                  );

    abstract T map(final Function<? super String, String> fn);

    abstract T mapAll(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                      final JsPath startingPath
                     );
    abstract T mapAll(final Function<? super String, String> fn);

}
