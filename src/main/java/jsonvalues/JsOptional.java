package jsonvalues;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public class JsOptional<S extends Json<S>, T>
{
  public final Function<S, Optional<T>> get;

  public final Function<T,Function<S, S>> set;

  public final Function<Function<T, T>, Function<S,S>> modify;

  public final Function<Supplier<T>,Function<S,S>> setIfAbsent;

  public final Function< Supplier<T>, Function<S,S>> setIfPresent;




  JsOptional(final Function<S, Optional<T>> get,
             final Function<T, Function<S, S>> set
            )
  {
    this.get = get;
    this.set = set;

    this.modify =  f -> json ->
    {
      final Optional<T> value = get.apply(json);
      if (!value.isPresent()) return json;
      return set.apply(f.apply(value.get())).apply(json);
    };

    this.setIfAbsent =  supplier -> json ->
    {
      final Optional<T> value = get.apply(json);
      if (value.isPresent()) return json;
      return set.apply(supplier.get()).apply(json);
    };


    this.setIfPresent = supplier -> json ->
    {
      final Optional<T> value = get.apply(json);
      if (!value.isPresent()) return json;
      return set.apply(supplier.get()).apply(json);
    };

  }

}




