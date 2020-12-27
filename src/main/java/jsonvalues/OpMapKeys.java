package jsonvalues;

import java.util.function.BiFunction;

abstract class OpMapKeys<T> {
    T json;

    OpMapKeys(final T json) {
        this.json = json;
    }

    abstract T map(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                   final JsPath startingPath
                  );

    abstract T mapAll(final BiFunction<? super JsPath, ? super JsValue, String> fn,
                      final JsPath startingPath
                     );
}
