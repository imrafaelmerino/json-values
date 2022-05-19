package jsonvalues.spec;


import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

interface JsValuePredicate extends JsSpec {

    @Override
    default Set<JsErrorPair> test(final JsPath parentPath,
                                  final JsValue value) {
        Set<JsErrorPair> errors = new HashSet<>();
        test(value).ifPresent(e -> errors.add(JsErrorPair.of(parentPath,
                                                             e
                                              )
                              )
        );
        return errors;
    }

    Optional<JsError> test(final JsValue value);

}
