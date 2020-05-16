package jsonvalues;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class JsAbstractLens<S extends Json<S>,O>
{

  JsAbstractLens(final Function<S, O> get,
                 final BiFunction<S, O, S> set
                )
  {

    this.modify = (json, f) -> set.apply(json,
                                         f.apply(get.apply(json))
                                        );
    this.set = set;
    this.get = get;

  }

  public final Function<S, O> get;

  public final BiFunction<S, O, S> set;

  public final BiFunction<S, Function<O, O>, S> modify;

  public Function<S, Optional<O>> find(final Predicate<O> predicate)
  {
    return s -> predicate.test(get.apply(s)) ?
      Optional.of((get.apply(s))) :
      Optional.empty();
  }

  public Predicate<S> exists(final Predicate<O> predicate)
  {
    return s -> predicate.test(get.apply(s));
  }

}
