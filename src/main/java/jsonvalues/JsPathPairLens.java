package jsonvalues;


/**
 Represent a Lens which focus is the path of a json pair
 */
public final class JsPathPairLens extends Lens<JsPair, JsPath> {
    JsPathPairLens() {
        super(pair -> pair.path,
              path -> pair -> JsPair.of(path,
                                        pair.value)
             );

    }
}
