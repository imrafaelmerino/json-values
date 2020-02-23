package jsonvalues;

import java.util.function.Predicate;

abstract class OpFilterElems<T>
{

    final T json;

    OpFilterElems(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> filterAll(final JsPath startingPath,
                                     final Predicate<? super JsPair> predicate
                                    );

    abstract Trampoline<T> filter(final JsPath startingPath,
                                  final Predicate<? super JsPair> predicate
                                 );


}
