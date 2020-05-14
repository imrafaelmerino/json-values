package jsonvalues;

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

    this.setIfAbsent = (json, supplier) ->
    {
      if (get.apply(json)
              .isNothing()) return set.apply(json,
                                            supplier.get()
                                           );
      else return json;
    };


    this.setIfPresent = (json, supplier) ->
    {
      if (!get.apply(json)
               .isNothing()) return set.apply(json,
                                             supplier.get()
                                            );
      else return json;
    };

  }

  private final JsPath path;

  public final Function<S, JsValue> get;

  public final BiFunction<S, JsValue, S> set;

  public final BiFunction<S, Function<JsValue, JsValue>, S> modify;

  public final BiFunction<S, Supplier<JsValue>, S> setIfAbsent;

  public final BiFunction<S, Supplier<JsValue>, S> setIfPresent;

  public JsLens<S> compose(final JsLens<?> lens)
  {
    return new JsLens<>(path.append(lens.path));
  }

  public <T> JsOptional<S, T> compose(JsPrism<T> prism)
  {
    return new JsOptional<>(path,
                            json -> prism.getOptional.apply(get.apply(json)),
                            (json, value) -> json.set(path,
                                                      prism.reverseGet.apply(value)
                                                     )
    );
  }

  public Function<S, Optional<JsValue>> find(final Predicate<JsValue> predicate)
  {
    return s -> predicate.test(get.apply(s)) ? Optional.of((get.apply(s))) : Optional.empty();
  }

  public Predicate<S> exists(final Predicate<JsValue> predicate)
  {
    return s -> predicate.test(get.apply(s));
  }

  static JsLens<JsObj> of(final String key)
  {
    return new JsLens<>(JsPath.fromKey(requireNonNull(key)));
  }

  static JsLens<JsArray> of(int index)
  {
    return new JsLens<>(JsPath.fromIndex(index));
  }

  static <R extends Json<R>> JsLens<R> of(JsPath path)
  {
    return new JsLens<>(path);
  }

}
