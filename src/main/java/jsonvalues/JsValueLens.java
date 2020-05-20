package jsonvalues;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 Represent a Lens which focus is the value of a Json

 @param <S> the type of the whole part, an array or an object */
public class JsValueLens<S extends Json<S>> extends Lens<S, JsValue> {

    public final BiFunction<S, Supplier<JsValue>, S> setIfAbsent;
    public final BiFunction<S, Supplier<JsValue>, S> setIfPresent;

    JsValueLens(final JsPath path) {
        super(json -> requireNonNull(json).get(path),
              value -> json -> requireNonNull(json).set(path,
                                                        requireNonNull(value)
                                                       )
             );

        this.setIfAbsent = (json, supplier) ->
        {
            if (get.apply(json)
                   .isNothing())
                return set.apply(supplier.get())
                          .apply(json);
            else return json;
        };


        this.setIfPresent = (json, supplier) ->
        {
            if (!get.apply(json)
                    .isNothing())
                return set.apply(supplier.get())
                          .apply(json);
            else return json;
        };

    }

    static JsValueLens<JsObj> of(final String key) {
        return new JsValueLens<>(JsPath.fromKey(requireNonNull(key)));
    }

    static JsValueLens<JsArray> of(int index) {
        return new JsValueLens<>(JsPath.fromIndex(index));
    }

    static <R extends Json<R>> JsValueLens<R> of(JsPath path) {
        return new JsValueLens<>(path);
    }

    public <T> Option<S, T> compose(final Prism<JsValue, T> prism) {
        return new Option<>(json -> requireNonNull(prism).getOptional.apply(get.apply(json)),
                            value -> json -> set.apply(prism.reverseGet.apply(value))
                                                .apply(json)
        );


    }


}
