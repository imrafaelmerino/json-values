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
                                                         final boolean nullable
    ) {

        return value ->
        {
            final Optional<JsError> error = testFlags(nullable).apply(value);
            if (error.isPresent() || value.isNull()) return error;
            if (!elemCondition.test(value)) return Optional.of(new JsError(value,
                                                                           errorCode
                                                               )
            );
            return Optional.empty();
        };
    }

    private static Function<JsValue, Optional<JsError>> testFlags(boolean nullable
    ) {
        return value ->
        {

            if (value.isNull() && !nullable) return Optional.of(new JsError(value,
                                                                            NULL
                                                                )
            );
            return Optional.empty();
        };
    }

    static Function<JsValue, Optional<JsError>> testArrayOfTestedValue(final Function<JsValue, Optional<JsError>> elemCondition,
                                                                       final boolean nullable
    ) {

        return testArrayPredicate(nullable,
                                  array ->
                                  {
                                      for (final JsValue next : array) {
                                          final Optional<JsError> result = elemCondition.apply(next);
                                          if (result.isPresent()) return result;
                                      }
                                      return Optional.empty();
                                  }
        );
    }

    private static Function<JsValue, Optional<JsError>> testArrayPredicate(final boolean nullable,
                                                                           final Function<JsArray, Optional<JsError>> validation
    ) {
        return value ->
        {
            final Optional<JsError> errors = testArray(nullable
            ).apply(value);
            if (errors.isPresent() || value.isNull()) return errors;
            return validation.apply(value.toJsArray());
        };
    }

    static Function<JsValue, Optional<JsError>> testArray(boolean nullable
    ) {
        return value ->
        {
            final Optional<JsError> error = testFlags(nullable
            ).apply(value);
            if (error.isPresent()) return error;
            return value.isNull() || value.isArray() ?
                   Optional.empty() :
                   Optional.of(new JsError(value,
                                           ARRAY_EXPECTED
                   ));
        };

    }


}
