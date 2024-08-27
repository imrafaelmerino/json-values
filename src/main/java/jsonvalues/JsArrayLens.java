package jsonvalues;


import static java.util.Objects.requireNonNull;

import fun.optic.Lens;

/**
 * Represent a Lens which focus is an array located at a path in a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsArrayLens<S extends Json<S>> extends Lens<S, JsArray> {

  JsArrayLens(final JsPath path) {
    super(json -> requireNonNull(json).getArray(path),
          a -> json -> requireNonNull(json).set(path,
                                                a
                                               )
         );
  }

  @Override
  public <B> Lens<S, B> compose(final Lens<JsArray, B> other) {
    return new Lens<>(this.get.andThen(other.get),
                      b -> s -> {
                        JsArray o = this.get.apply(requireNonNull(s));
                        JsArray newO = other.set.apply(requireNonNull(b))
                                                .apply(o == null ?
                                                       JsArray.empty() :
                                                       o);
                        return this.set.apply(newO)
                                       .apply(s);
                      }
    );
  }
}
