package jsonvalues;


public final class JsPathPairLens extends JsAbstractLens<JsPair,JsPath> {
  JsPathPairLens() {
    super(pair-> pair.path, path -> pair -> JsPair.of(path,pair.value));

  }
}
