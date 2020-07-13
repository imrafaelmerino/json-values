package jsonvalues;


import java.time.Instant;

/**
 Represent a Lens which focus is an Instant located at a path in a Json

 @param <S> the type of the whole part, an array or an object */
public class JsInstantLens<S extends Json<S>> extends Lens<S, Instant> {
    JsInstantLens(final JsPath path) {
        super(json -> json.getInstant(path),
              o -> json -> json.set(path,
                                    JsInstant.of(o))
             );
    }
}
