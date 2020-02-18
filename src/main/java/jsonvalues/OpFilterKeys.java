package jsonvalues;

import java.util.function.Predicate;

abstract class OpFilterKeys<T>
{

    final T json;

    OpFilterKeys(final T json
                )
    {
        this.json = json;
    }


    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json recursively
    abstract Trampoline<T> filterAll(final JsPath startingPath,
                                     final Predicate<? super JsPair> predicate
                                    );

    abstract Trampoline<T> filter(final Predicate<? super JsPair> predicate);


}
