package jsonvalues.optics;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.Json;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class JsLens<S extends Json<S>>
{



  JsLens(final JsPath path)
  {
    this.path = requireNonNull(path);
    this.get = json -> requireNonNull(json).get(path);
    this.set = (json, value) -> requireNonNull(json).set(path,
                                                         requireNonNull(value)
                                                        );
    this.modify = (json, f) -> requireNonNull(json).set(path,
                                                        f.apply(json.get(path))
                                                       );

    this.setIfAbsent = (json,supplier) -> {
      if(json.get(path).isNothing()) return json.set(path, supplier.get());
      else return json;
    };


    this.setIfPresent = (json,supplier) -> {
      if(!json.get(path).isNothing()) return json.set(path, supplier.get());
      else return json;
    };

  }

  private final JsPath path;

  public final Function<S, JsValue> get;
  public final BiFunction<S, JsValue, S> set;
  public final BiFunction<S, Function<JsValue, JsValue>, S> modify;
  public final BiFunction<S, Supplier<JsValue>, S> setIfAbsent;
  public final BiFunction<S, Supplier<JsValue>, S> setIfPresent;


  public <T extends Json<T>> JsLens<T> compose(final JsLens<T> lens)
  {
    return new JsLens<>(this.path.append(requireNonNull(lens).path));
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
