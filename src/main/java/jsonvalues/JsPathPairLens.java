package jsonvalues;


public final class JsPathPairLens extends Lens<JsPair,JsPath> {
  JsPathPairLens() {
    super(pair-> pair.path, path -> pair -> JsPair.of(path,pair.value));

  }
}
