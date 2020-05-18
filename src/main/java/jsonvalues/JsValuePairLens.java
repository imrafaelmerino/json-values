package jsonvalues;


/**
 Represent a Lens which focus is the value of a json pair
 */
public final class JsValuePairLens extends Lens<JsPair, JsValue> {
    JsValuePairLens() {
        super(pair -> pair.value,
              value -> pair -> JsPair.of(pair.path,
                                         value)
             );

    }
}
