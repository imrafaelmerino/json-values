package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static jsonvalues.spec.ERROR_CODE.*;

class Functions {
    static Function<JsValue, Optional<Error>> testElem(final Predicate<JsValue> elemCondition,
                                                       final ERROR_CODE errorCode,
                                                       final boolean required,
                                                       final boolean nullable
                                                      ) {

        return value ->
        {
            final Optional<Error> error = testFlags(required,
                                                    nullable
                                                   ).apply(value);
            if (error.isPresent() || value.isNull()) return error;
            if (!elemCondition.test(value)) return Optional.of(new Error(value,
                                                                         errorCode
                                                               )
                                                              );
            return Optional.empty();
        };
    }

    private static Function<JsValue, Optional<Error>> testFlags(boolean required,
                                                                boolean nullable
                                                               ) {
        return value ->
        {
            if (value.isNothing() && required) return Optional.of(new Error(value,
                                                                            REQUIRED
                                                                  )
                                                                 );
            if (value.isNull() && !nullable) return Optional.of(new Error(value,
                                                                          NULL
                                                                )
                                                               );
            return Optional.empty();
        };
    }

    static Function<JsValue, Optional<Error>> testArrayOfTestedValue(final Function<JsValue, Optional<Error>> elemCondition,
                                                                     final boolean required,
                                                                     final boolean nullable
                                                                    ) {

        return testArrayPredicate(required,
                                  nullable,
                                  array ->
                                  {
                                      for (final JsValue next : array) {
                                          final Optional<Error> result = elemCondition.apply(next);
                                          if (result.isPresent()) return result;
                                      }
                                      return Optional.empty();
                                  }
                                 );
    }

    private static Function<JsValue, Optional<Error>> testArrayPredicate(final boolean required,
                                                                         final boolean nullable,
                                                                         final Function<JsArray, Optional<Error>> validation
                                                                        ) {
        return value ->
        {
            final Optional<Error> errors = testArray(required,
                                                     nullable
                                                    ).apply(value);
            if (errors.isPresent() || value.isNull()) return errors;
            return validation.apply(value.toJsArray());
        };
    }

    static Function<JsValue, Optional<Error>> testArray(boolean required,
                                                        boolean nullable
                                                       ) {
        return value ->
        {
            final Optional<Error> error = testFlags(required,
                                                    nullable
                                                   ).apply(value);
            if (error.isPresent()) return error;
            return value.isNull() || value.isArray() ? Optional.empty() : Optional.of(new Error(value,
                                                                                                ARRAY_EXPECTED
            ));
        };

    }


}
