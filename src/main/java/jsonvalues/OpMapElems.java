package jsonvalues;

import java.util.function.BiFunction;

abstract class OpMapElems<T> {
    T json;

    OpMapElems(final T json) {
        this.json = json;
    }

    abstract T map(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn,
                   final JsPath path
                  );

    abstract T mapAll(final BiFunction<? super JsPath,? super  JsPrimitive, ? extends JsValue> fn,
                      final JsPath path
                     );


}
