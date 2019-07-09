package jsonvalues;


import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 Trampolines allow to define recursive algorithms by iterative loops without blowing the stack when
 methods implementations are tail recursive.

 @param <T> the type of the result
 */
public interface Trampoline<T> extends Supplier<T>
{

    /**
     maps this trampoline which returns a T, into another one that returns a R. When the method get()
     is invoked, this trampoline is executed and then the result is mapped.
     @param fn the map function, from T to R
     @param <R> type of the result
     @return  a Trampoline of type R
     */
    default <R> Trampoline<R> map(Function<? super T, ? extends R> fn)
    {
        if (complete()) return done(fn.apply(get()));
        else return Trampoline.more(() -> bounce().map(fn));
    }

    /**
     map this trampoline which returns a T, into another one that returns a R. When the method get()
     is invoked, this trampoline is executed and then the result of type T is passed to the trampoline specified in
     the flatMap function, which will return a R. So, two trampolines are executed, one after the other.
     @param fn the map function, from T to {@code Trampoline<R>}
     @param <R> type of the result
     @return a Trampoline of type R
     */
    default <R> Trampoline<R> flatMap(Function<? super T, ? extends Trampoline<R>> fn)
    {
        if (complete()) return fn.apply(get());
        else return Trampoline.more(() -> bounce().flatMap(fn));
    }


    /**
     * @return next stage
     */
    default Trampoline<T> bounce()
    {
        return this;
    }


    /**
     Gets a result
     @return a result
     */
    @Override
    T get();


    default boolean complete()
    {
        return true;
    }


    static <T> Trampoline<T> done(final T result)
    {
        return () -> result;
    }


    static <T> Trampoline<T> more(final Trampoline<Trampoline<T>> trampoline)
    {
        return new Trampoline<T>()
        {
            @Override
            public boolean complete()
            {
                return false;
            }

            @Override
            public Trampoline<T> bounce()
            {
                return trampoline.get();
            }

            @Override
            public T get()
            {
                return trampoline(this);
            }

            T trampoline(final Trampoline<T> trampoline)
            {
                return Stream.iterate(trampoline,
                                      Trampoline::bounce
                                     )
                             .filter(Trampoline::complete)
                             .findFirst()
                             .orElseThrow(() -> new UnsupportedOperationException("Trampoline.result but not completed"))
                             .get();
            }
        };
    }

}
