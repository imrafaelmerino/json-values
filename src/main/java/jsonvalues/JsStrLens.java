package jsonvalues;


import static java.util.Objects.requireNonNull;

import fun.optic.Lens;

class JsStrLens<S extends Json<S>> extends Lens<S, String> {

  JsStrLens(JsPath path) {
    super(json -> requireNonNull(json).getStr(path),
          str -> json -> requireNonNull(json)
              .set(path,
                   JsStr.of(str)
                  )
         );
  }


}
