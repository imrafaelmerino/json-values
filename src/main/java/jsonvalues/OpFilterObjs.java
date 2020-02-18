package jsonvalues;

import java.util.function.BiPredicate;

abstract class OpFilterObjs<T>
{

    T json;

    OpFilterObjs(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> filter(final JsPath startingPath,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 );

    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    abstract Trampoline<T> filterAll(final JsPath startingPath,
                                     final BiPredicate<? super JsPath, ? super JsObj> predicate
                                    );
}
