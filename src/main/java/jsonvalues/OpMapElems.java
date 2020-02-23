package jsonvalues;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class OpMapElems<T>
{
    T json;

    OpMapElems(final T json)
    {
        this.json = json;
    }

    abstract Trampoline<T> map(final Function<? super JsPair, ? extends JsValue> fn,
                               final Predicate<? super JsPair> predicate,
                               final JsPath path
                              );

    abstract Trampoline<T> mapAll(final Function<? super JsPair, ? extends JsValue> fn,
                                  final Predicate<? super JsPair> predicate,
                                  final JsPath path
                                 );


}
