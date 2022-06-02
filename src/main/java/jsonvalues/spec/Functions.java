package jsonvalues.spec;

import fun.tuple.Pair;
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

    static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testElem(Predicate<JsValue> predicate,
                                                                           ERROR_CODE errorCode,
                                                                           boolean nullable) {

        return value -> {
            Optional<Pair<JsValue, ERROR_CODE>> error = testNullable(nullable).apply(value);
            if (error.isPresent() || value.isNull()) return error;
            return predicate.test(value) ?
                   Optional.empty() :
                   Optional.of(Pair.of(value,
                                          errorCode));
        };
    }

    private static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testNullable(boolean nullable) {
        return value -> value.isNull() && !nullable ?
                        Optional.of(Pair.of(value,
                                               NULL)) :
                        Optional.empty();
    }

    static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testArrayOfTestedValue(Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                                                                         boolean nullable,
                                                                                         int min,
                                                                                         int max) {
        return testArrayPredicate(nullable,
                                  array -> {
                                      if (array.size() < min)
                                          return Optional.of(Pair.of(array,
                                                                        ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN));
                                      if (array.size() > max)
                                          return Optional.of(Pair.of(array,
                                                                        ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX));
                                      for (JsValue next : array) {
                                          Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(next);
                                          if (result.isPresent()) return result;
                                      }
                                      return Optional.empty();
                                  });
    }


    private static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testArrayPredicate(boolean nullable,
                                                                                             Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> validation) {
        return value -> {
            Optional<Pair<JsValue, ERROR_CODE>> errors = testArray(nullable).apply(value);
            return errors.isPresent() || value.isNull() ?
                   errors :
                   validation.apply(value.toJsArray());
        };
    }

    static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testArray(boolean nullable) {
        return value -> {
            Optional<Pair<JsValue, ERROR_CODE>> error = testNullable(nullable).apply(value);
            if (error.isPresent()) return error;
            return value.isNull() || value.isArray() ?
                   Optional.empty() :
                   Optional.of(Pair.of(value,
                                          ARRAY_EXPECTED));
        };

    }

    static Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testArray(boolean nullable,
                                                                            int min,
                                                                            int max) {
        return value -> {
            Optional<Pair<JsValue, ERROR_CODE>> error = testNullable(nullable).apply(value);
            if (error.isPresent()) return error;
            if (value.isArray()) {
                JsArray array = value.toJsArray();
                if (array.size() < min)
                    return Optional.of(Pair.of(array,
                                                  ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN));
                if (array.size() > max)
                    return Optional.of(Pair.of(array,
                                                  ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX));
                return Optional.empty();

            } else
                return value.isNull() ?
                       Optional.empty() :
                       Optional.of(Pair.of(value,
                                              ARRAY_EXPECTED));
        };

    }


}
