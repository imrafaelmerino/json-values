package jsonvalues;


import fun.optic.Lens;

import static java.util.Objects.requireNonNull;

/**
 * Represent a Lens which focus is a JSON object located at a path is a Json
 *
 * @param <S> the type of the whole part, an array or an object
 */
class JsObjLens<S extends Json<S>> extends Lens<S, JsObj> {
    JsObjLens(final JsPath path) {
        super(json -> requireNonNull(json).getObj(path),
              o -> json -> requireNonNull(json).set(path,
                                                    o)
             );
    }

    @Override
    public <B> Lens<S, B> compose(final Lens<JsObj, B> other) {
        return new Lens<>(this.get.andThen(other.get),
                          b -> s -> {
                              JsObj o = this.get.apply(requireNonNull(s));
                              JsObj newO = other.set.apply(requireNonNull(b))
                                                    .apply(o == null ?
                                                                   JsObj.empty() :
                                                                   o);
                              return this.set.apply(newO)
                                             .apply(s);
                          }
        );
    }

}
