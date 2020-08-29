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

    /**
     check if there is no target
     */
    public final Predicate<S> isEmpty;

    /**
     check if there is a target
     */
    public final Predicate<S> nonEmpty;

    /**
     modify the target of a Prism with a function, returning the same source if the prism is not matching. Basically
     it means we dont care about the success of the operation
     */
    public final Function<Function<T, T>, Function<S, S>> modify;

    /**
     modify the target of a Prism with a function, returning empty if the prism is not matching. Unless modify, we
     need to know the success of the operation
     */
    public final Function<Function<T, T>, Function<S, Optional<S>>> modifyOpt;

    /**
     find if the target satisfies the predicate
     */
    public final Function<Predicate<T>, Function<S, Optional<T>>> find;

    /**
     check if there is a target and it satisfies the predicate
     */
    public final Function<Predicate<T>, Predicate<S>> exists;

    /**
     check if there is no target or the target satisfies the predicate
     */
    public final Function<Predicate<T>, Predicate<S>> all;

    public Prism(final Function<S, Optional<T>> getOptional,
                 final Function<T, S> reverseGet
                ) {
        this.getOptional = getOptional;
        this.reverseGet = reverseGet;
        this.modify = f -> {
            Objects.requireNonNull(f);
            return v ->
            {
                final Optional<T> opt = getOptional.apply(v);
                if (opt.isPresent()) return reverseGet.apply(f.apply(opt.get()));
                else return v;
            };
        };
        this.modifyOpt = f -> v ->
        {
            final Optional<T> opt = getOptional.apply(v);
            return opt.map(t -> reverseGet.apply(f.apply(t)));
        };
        this.isEmpty = target -> !getOptional.apply(target)
                                             .isPresent();
        this.nonEmpty = target -> getOptional.apply(target)
                                             .isPresent();
        this.find = predicate -> v -> getOptional.apply(v)
                                                 .filter(predicate);

        this.exists = predicate -> v -> getOptional.apply(v)
                                                   .filter(predicate)
                                                   .isPresent();
        this.all = predicate -> v ->
        {
            final Optional<T> value = getOptional.apply(v);
            return value.map(predicate::test)
                        .orElse(true);
        };
    }

}
