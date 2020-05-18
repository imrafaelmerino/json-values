package jsonvalues;

import java.util.function.Function;

abstract class OpMapElems<T> {
    T json;

    OpMapElems(final T json) {
        this.json = json;
    }

    abstract Trampoline<T> map(final Function<? super JsPair, ? extends JsValue> fn,
                               final JsPath path
                              );

    abstract Trampoline<T> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                                  final JsPath path
                                 );


}
