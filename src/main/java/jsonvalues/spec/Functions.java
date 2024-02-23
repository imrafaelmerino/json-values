package jsonvalues.spec;


import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.NULL_NOT_EXPECTED;

import java.util.function.Function;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

class Functions {

  private Functions() {
  }

  static JsError testValue(Predicate<JsValue> predicate,
                           ERROR_CODE errorCode,
                           boolean nullable,
                           JsValue value
                          ) {

    JsError error = testNullable(nullable,
                                 value);
    if (error != null || value.isNull()) {
      return error;
    }
    return predicate.test(value) ?
           null :
           new JsError(value,
                       errorCode
           );

  }

  private static JsError testNullable(boolean nullable,
                                      JsValue value) {
    return value.isNull() && !nullable ?
           new JsError(value,
                       NULL_NOT_EXPECTED) :
           null;
  }

  static JsError testArrayOfTestedValue(Function<JsValue, JsError> predicate,
                                        boolean nullable,
                                        int min,
                                        int max,
                                        JsValue value
                                       ) {
    return testArrayPredicate(nullable,
                              array -> {
                                if (array.size() < min) {
                                  return new JsError(array,
                                                     ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN
                                  );
                                }
                                if (array.size() > max) {
                                  return new JsError(array,
                                                     ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX
                                  );
                                }
                                for (JsValue next : array) {
                                  JsError result = predicate.apply(next);
                                  if (result != null) {
                                    return result;
                                  }
                                }
                                return null;
                              },
                              value
                             );
  }


  private static JsError testArrayPredicate(boolean nullable,
                                            Function<JsArray, JsError> validation,
                                            JsValue value
                                           ) {
    JsError errors = testArray(nullable,
                               value);
    return errors != null || value.isNull() ?
           errors :
           validation.apply(value.toJsArray());

  }

  static JsError testArray(boolean nullable,
                           JsValue value) {
    JsError error = testNullable(nullable,
                                 value);
    if (error != null) {
      return error;
    }
    return value.isNull() || value.isArray() ?
           null :
           new JsError(value,
                       ARRAY_EXPECTED
           );


  }

  static JsError testArray(boolean nullable,
                           int min,
                           int max,
                           JsValue value) {

    JsError error = testNullable(nullable,
                                 value);
    if (error != null) {
      return error;
    }
    if (value.isArray()) {
      JsArray array = value.toJsArray();
      if (array.size() < min) {
        return new JsError(array,
                           ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN
        );
      }
      if (array.size() > max) {
        return new JsError(array,
                           ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX
        );
      }
      return null;

    } else {
      return value.isNull() ?
             null :
             new JsError(value,
                         ARRAY_EXPECTED
             );
    }


  }

  public static JsError testStrConstraints(final StrConstraints constraints,
                                           final JsValue value) {
    return null;
  }
}
