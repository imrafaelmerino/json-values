package jsonvalues;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class JsAbstractLens<S,O>
{

  JsAbstractLens(final Function<S, O> get,
                 final Function<O,Function<S, S>> set
                )
  {

    this.modify =  f -> json -> set.apply(f.apply(get.apply(json))).apply(json);
    this.set = set;
    this.get = get;

  }

  public final Function<S, O> get;

  public final Function<O, Function<S, S>> set;

  public final Function<Function<O, O>,Function<S,S>> modify;

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
