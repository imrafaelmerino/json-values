package jsonvalues.optics;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.Json;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class Lens<S extends Json<S>>
{


  Lens(final JsPath path)
  {
    this.path = requireNonNull(path);
    this.get = json -> requireNonNull(json).get(path);
    this.set = (json, value) -> requireNonNull(json).put(path,
                                                         requireNonNull(value)
                                                        );
    this.modify = (json, f) -> requireNonNull(json).put(path,
                                                        requireNonNull(f)
                                                       );

  }

  private final JsPath path;

  public final Function<S, JsValue> get;
  public final BiFunction<S, JsValue, S> set;
  public final BiFunction<S, Function<JsValue, JsValue>, S> modify;


  public <T extends Json<T>> Lens<T> compose(final Lens<T> lens)
  {
    return new Lens<>(this.path.append(requireNonNull(lens).path));
  }

  public Function<S, Optional<JsValue>> find(final Predicate<JsValue> predicate)
  {
    return s -> predicate.test(get.apply(s)) ? Optional.of((get.apply(s))) : Optional.empty();
  }

  public Predicate<S> exists(final Predicate<JsValue> predicate)
  {
    return s -> predicate.test(get.apply(s));
  }


}
