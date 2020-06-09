package jsonvalues;

class JsStrLens<S extends Json<S>> extends Lens<S, String> {
    JsStrLens(final JsPath path) {
        super(json -> json.getStr(path),
              str -> json -> json.set(path,
                                      JsStr.of(str))
             );
    }
}
