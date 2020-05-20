package jsonvalues;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 A Prism is an optic that can be seen as a pair of functions:
 {@code
 - getOptional: S -> Optional<T>
 - reverseGet : T -> S
 }
 Typically a Prism encodes the relation between a Sum or CoProduct type and one of its element.

 @param <S> the source of a prism
 @param <T> the target of a prism */
public class Prism<S, T> {
    /**
     get the target of a Prism or nothing if there is no target
     */
    public final Function<S, Optional<T>> getOptional;

    /** get the modified source of a Prism */
    public final Function<T, S> reverseGet;

    Prism(final Function<S, Optional<T>> getOptional,
          final Function<T, S> reverseGet
         ) {
        this.getOptional = getOptional;
        this.reverseGet = reverseGet;
    }


    /**
     modify the target of a Prism with a function, returning the same source if the prism is not matching. Basically
     it means we dont care about the success of the operation

     @param f the function from T to T
     @return a function from S to S
     */
    public final Function<S, S> modify(Function<T, T> f) {
        Objects.requireNonNull(f);
        return v ->
        {
            final Optional<T> opt = getOptional.apply(v);
            if (opt.isPresent()) return reverseGet.apply(f.apply(opt.get()));
            else return v;
        };
    }

    /**
     modify the target of a Prism with a function, returning empty if the prism is not matching. Unless modify, we
     need to know the success of the operation

     @param f the function from T to T
     @return a function from S to S
     */
    public final Function<S, Optional<S>> modifyOptional(Function<T, T> f) {
        return v ->
        {
            final Optional<T> opt = getOptional.apply(v);
            return opt.map(t -> reverseGet.apply(f.apply(t)));
        };
    }

    /**
     check if there is no target

     @param target the target
     @return true if there is no target
     */
    public final boolean isEmpty(S target) {
        return !getOptional.apply(target)
                           .isPresent();
    }

    /**
     check if there is a target

     @param target the target
     @return true if there is a target
     */
    public final boolean nonEmpty(S target) {
        return getOptional.apply(target)
                          .isPresent();
    }

    /**
     find if the target satisfies the predicate

     @param predicate the given predicate
     @return a function from the source to an optional target
     */
    public final Function<S, Optional<T>> find(Predicate<T> predicate) {
        return v -> getOptional.apply(v)
                               .filter(predicate);
    }

    /**
     check if there is a target and it satisfies the predicate

     @param predicate the given predicate
     @return a predicate on the source
     */
    public final Predicate<S> exists(Predicate<T> predicate) {
        return v -> getOptional.apply(v)
                               .filter(predicate)
                               .isPresent();
    }

    /**
     check if there is no target or the target satisfies the predicate

     @param predicate the given predicate
     @return a predicate on the source
     */
    public final Predicate<S> all(Predicate<T> predicate) {
        return v ->
        {
            final Optional<T> value = getOptional.apply(v);
            return value.map(predicate::test)
                        .orElse(true);
        };
    }


}
