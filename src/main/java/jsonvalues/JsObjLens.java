package jsonvalues;


/**
 Represent a Lens which focus is a json object located at a path is a Json

 @param <S> the type of the whole part, an array or an object */
public class JsObjLens<S extends Json<S>> extends Lens<S, JsObj> {
    JsObjLens(final JsPath path) {
        super(json -> json.getObj(path),
              o -> json -> json.set(path,
                                    o)
             );
    }
}
