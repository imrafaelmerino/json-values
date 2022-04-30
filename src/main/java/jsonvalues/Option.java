package jsonvalues;

import java.util.Optional;
import java.util.function.Function;

/**
 An Optional is an optic that allows seeing into a structure and getting, setting, or modifying an optional focus.
 It combines the properties of a Lens (getting, setting, and modifying) with the properties of a Prism (an optional focus).
 An  Optional can be seen as a pair of functions:
 {@code
 - get: S      => Optional[T]
 - set: (T, S) => S
 }
 An Optional could also be defined as a weaker Lens and weaker Prism

 @param <S> the source of an optional
 @param <T> the target of an optional */
public class Option<S, T> {
    /**
     get the target of an Optional or nothing if there is no target
     */
    public final Function<S, Optional<T>> get;

    /**
     function to look into S, set a value for an optional focus T, and obtain the modified source
     */
    public final Function<T, Function<S, S>> set;

    /**
     modify the target of an optional with a function if it exists, returning the same source otherwise
     */
    public final Function<Function<T, T>, Function<S, S>> modify;

    public Option(final Function<S, Optional<T>> get,
                  final Function<T, Function<S, S>> set
                 ) {
        this.get = get;
        this.set = set;

        this.modify = f -> json ->
        {
            final Optional<T> value = get.apply(json);
            if (!value.isPresent()) return json;
            return set.apply(f.apply(value.get()))
                      .apply(json);
        };
    }


    /**
     compose this optional with the given as parameter

     @param other the other optional
     @param <F>   the type of the focus
     @return a new optional
     */
    public <F> Option<S, F> compose(final Option<T, F> other) {
        return new Option<>(s -> {
            Optional<T> t = this.get.apply(s);
            if (t.isPresent()) return other.get.apply(t.get());
            else return Optional.empty();
        },
                            f -> s -> {
                                Optional<T> t = this.get.apply(s);
                                if (t.isPresent()) return this.set.apply(other.set.apply(f)
                                                                                  .apply(t.get()))
                                                                  .apply(s);
                                else return s;
                            }
        );
    }

}




