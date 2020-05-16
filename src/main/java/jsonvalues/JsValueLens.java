package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class JsValueLens<S extends Json<S>> extends JsAbstractLens<S,JsValue>
{

  JsValueLens(final JsPath path)
  {
    super(json -> requireNonNull(json).get(path),
          (json, value) -> requireNonNull(json).set(path,
                                                    requireNonNull(value)
                                                   )
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

  public final BiFunction<S, Supplier<JsValue>, S> setIfAbsent;

  public final BiFunction<S, Supplier<JsValue>, S> setIfPresent;

  public <T> JsOptional<S, T> compose(final JsPrism<T> prism)
  {
    return new JsOptional<>(json -> requireNonNull(prism).getOptional.apply(get.apply(json)),
                            (json, value) -> set.apply(json,
                                                      prism.reverseGet.apply(value)
                                                     )
    );
  }

  static JsValueLens<JsObj> of(final String key)
  {
    return new JsValueLens<>(JsPath.fromKey(requireNonNull(key)));
  }

  static JsValueLens<JsArray> of(int index)
  {
    return new JsValueLens<>(JsPath.fromIndex(index));
  }

  static <R extends Json<R>> JsValueLens<R> of(JsPath path)
  {
    return new JsValueLens<>(path);
  }



}
