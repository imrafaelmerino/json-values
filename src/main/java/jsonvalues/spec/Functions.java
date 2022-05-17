package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.NULL;

class Functions {

    private Functions() {
    }

    static Function<JsValue, Optional<JsError>> testElem(final Predicate<JsValue> elemCondition,
                                                         final ERROR_CODE errorCode,
                                                         final boolean nullable) {

        return value -> {
            final Optional<JsError> error = testFlags(nullable).apply(value);
            if (error.isPresent() || value.isNull()) return error;
            return elemCondition.test(value) ?
                   Optional.empty() :
                   Optional.of(new JsError(value,
                                           errorCode));
        };
    }

    private static Function<JsValue, Optional<JsError>> testFlags(boolean nullable) {
        return value -> value.isNull() && !nullable ?
                        Optional.of(new JsError(value,
                                                NULL)) :
                        Optional.empty();
    }

    static Function<JsValue, Optional<JsError>> testArrayOfTestedValue(Function<JsValue, Optional<JsError>> elemCondition,
                                                                       boolean nullable) {

        return testArrayPredicate(nullable,
                                  array -> {
                                      for (final JsValue next : array) {
                                          final Optional<JsError> result = elemCondition.apply(next);
                                          if (result.isPresent()) return result;
                                      }
                                      return Optional.empty();
                                  });
    }

    private static Function<JsValue, Optional<JsError>> testArrayPredicate(boolean nullable,
                                                                           Function<JsArray, Optional<JsError>> validation) {
        return value -> {
            Optional<JsError> errors = testArray(nullable).apply(value);
            return errors.isPresent() || value.isNull() ?
                   errors :
                   validation.apply(value.toJsArray());
        };
    }

    static Function<JsValue, Optional<JsError>> testArray(boolean nullable) {
        return value -> {
            Optional<JsError> error = testFlags(nullable).apply(value);
            if (error.isPresent()) return error;
            return value.isNull() || value.isArray() ?
                   Optional.empty() :
                   Optional.of(new JsError(value,
                                           ARRAY_EXPECTED));
        };

    }


}
