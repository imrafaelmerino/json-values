package jsonvalues;

import java.util.function.Function;
import java.util.function.Predicate;

abstract class OpMapKeys<T>
{
    T json;

    OpMapKeys(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> map(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath startingPath
                              );
    abstract Trampoline<T> mapAll(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate,
                                  final JsPath startingPath
                                 );
}
