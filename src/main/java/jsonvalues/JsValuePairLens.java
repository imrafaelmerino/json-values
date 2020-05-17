package jsonvalues;


public final class JsValuePairLens extends Lens<JsPair,JsValue> {
  JsValuePairLens() {
    super(pair-> pair.value, value -> pair -> JsPair.of(pair.path,value));

  }
}
