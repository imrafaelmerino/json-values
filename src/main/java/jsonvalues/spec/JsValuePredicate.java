package jsonvalues.spec;


import fun.tuple.Pair;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

interface JsValuePredicate extends JsSpec {

    @Override
    default Set<SpecError> test(final JsPath parentPath,
                                final JsValue value) {
        Set<SpecError> errors = new HashSet<>();
        test(value).ifPresent(e -> errors.add(SpecError.of(parentPath,
                                                         e
                                              )
                              )
        );
        return errors;
    }

    Optional<Pair<JsValue,ERROR_CODE>> test(final JsValue value);

}
