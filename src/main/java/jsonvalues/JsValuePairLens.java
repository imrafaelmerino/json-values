package jsonvalues;


public final class JsValuePairLens extends JsAbstractLens<JsPair,JsValue> {
  JsValuePairLens() {
    super(pair-> pair.value, value -> pair -> JsPair.of(pair.path,value));

  }
}
