package jsonvalues;

import java.util.function.BiFunction;

abstract class OpMapObjs<T> {
    T json;

    OpMapObjs(final T json) {
        this.json = json;
    }

    abstract T map(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                   final JsPath startingPath
                  );

    abstract T mapAll(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn,
                      final JsPath startingPath
                     );
}
