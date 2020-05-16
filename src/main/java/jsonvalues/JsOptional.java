package jsonvalues;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public class JsOptional<S extends Json<S>, T>
{
  public final Function<S, Optional<T>> get;
  public final BiFunction<S, T, S> set;

  public final BiFunction<S, Function<T, T>, S> modify;

  public final BiFunction<S, Supplier<T>, S> setIfAbsent;

  public final BiFunction<S, Supplier<T>, S> setIfPresent;

  JsOptional(final Function<S, Optional<T>> get,
             final BiFunction<S, T, S> set
            )
  {
    this.get = get;
    this.set = set;

    this.modify = (json, f) ->
    {
      final Optional<T> value = get.apply(json);
      if (!value.isPresent()) return json;
      return set.apply(json,
                       f.apply(value.get())
                      );
    };

    this.setIfAbsent = (json, supplier) ->
    {
      final Optional<T> value = get.apply(json);
      if (value.isPresent()) return json;
      return set.apply(json,
                       supplier.get()
                      );
    };


    this.setIfPresent = (json, supplier) ->
    {
      final Optional<T> value = get.apply(json);
      if (!value.isPresent()) return json;
      return set.apply(json,
                       supplier.get()
                      );
    };

  }

}




