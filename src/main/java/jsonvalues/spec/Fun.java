package jsonvalues.spec;


import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.NULL_NOT_EXPECTED;

import java.util.function.Function;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

class Fun {

  private Fun() {
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
                                        ArraySchemaConstraints arrayConstraints,
                                        JsValue value
                                       ) {
    return testArrayPredicate(nullable,
                              array -> {
                                if (arrayConstraints != null && array.size() < arrayConstraints.minItems()) {
                                  return new JsError(array,
                                                     ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN
                                  );
                                }
                                if (arrayConstraints != null && array.size() > arrayConstraints.maxItems()) {
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
                           ArraySchemaConstraints arrayConstraints,
                           JsValue value) {

    JsError error = testNullable(nullable,
                                 value);
    if (error != null) {
      return error;
    }
    if (value.isArray()) {

      JsArray array = value.toJsArray();
      if (arrayConstraints != null) {
        if (array.size() < arrayConstraints.minItems()) {
          return new JsError(array,
                             ERROR_CODE.ARR_SIZE_LOWER_THAN_MIN
          );
        }
        if (array.size() > arrayConstraints.maxItems()) {
          return new JsError(array,
                             ERROR_CODE.ARR_SIZE_GREATER_THAN_MAX
          );
        }
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

  public static ERROR_CODE testStrConstraints(final StrConstraints constraints,
                                              final JsStr jsStr) {
    String value = jsStr.value;
    int length = value.length();
    if (length < constraints.minLength) {
      return ERROR_CODE.STR_LENGTH_LOWER_THAN_MIN;
    }
    if (length > constraints.maxLength) {
      return ERROR_CODE.STR_LENGTH_GREATER_THAN_MAX;
    }
    if (constraints.pattern != null
        && !constraints.pattern.matcher(value)
                               .matches()) {
      return ERROR_CODE.STR_PATTERN_NOT_MATCH;
    }

    return null;
  }

  public static ERROR_CODE testLongConstraints(final LongSchemaConstraints constraints,
                                               final JsLong value) {
    if (value.value < constraints.minimum()) {
      return ERROR_CODE.LONG_LOWER_THAN_MIN;
    }
    if (value.value > constraints.maximum()) {
      return ERROR_CODE.LONG_GREATER_THAN_MAX;
    }

    return null;
  }

  public static ERROR_CODE testIntConstraints(final IntegerSchemaConstraints constraints,
                                              final JsInt value) {
    if (value.value < constraints.minimum()) {
      return ERROR_CODE.INT_LOWER_THAN_MIN;
    }
    if (value.value > constraints.maximum()) {
      return ERROR_CODE.INT_GREATER_THAN_MAX;
    }
    return null;
  }

  public static ERROR_CODE testInstantConstraints(final InstantSchemaConstraints constraints,
                                                  final JsInstant instant) {
    if (instant.value.isBefore(constraints.minimum())) {
      return ERROR_CODE.INSTANT_LOWER_THAN_MIN;
    }
    if (instant.value.isAfter(constraints.maximum())) {
      return ERROR_CODE.INSTANT_GREATER_THAN_MAX;
    }

    return null;
  }

  public static ERROR_CODE testDoubleConstraints(final DoubleSchemaConstraints constraints,
                                                 final JsDouble value) {
    if (value.value < constraints.minimum()) {
      return ERROR_CODE.DOUBLE_LOWER_THAN_MIN;
    }
    if (value.value > constraints.maximum()) {
      return ERROR_CODE.DOUBLE_GREATER_THAN_MAX;
    }
    return null;
  }

  public static ERROR_CODE testDecimalConstraints(final DecimalSchemaConstraints constraints,
                                                  final JsBigDec jsBigDec) {
    if (constraints.minimum() != null
        && jsBigDec.value.compareTo(constraints.minimum()) < 0) {
      return ERROR_CODE.DECIMAL_LOWER_THAN_MIN;
    }
    if (constraints.maximum() != null
        && jsBigDec.value.compareTo(constraints.maximum()) > 0) {
      return ERROR_CODE.DECIMAL_GREATER_THAN_MAX;
    }

    return null;
  }

  public static ERROR_CODE testBigIntConstraints(final BigIntSchemaConstraints constraints,
                                                 final JsBigInt jsBigInt) {
    if (constraints.minimum() != null && jsBigInt.value.compareTo(constraints.minimum()) < 0) {
      return ERROR_CODE.BIGINT_LOWER_THAN_MIN;
    }
    if (constraints.maximum() != null && jsBigInt.value.compareTo(constraints.maximum()) > 0) {
      return ERROR_CODE.BIGINT_GREATER_THAN_MAX;
    }
    return null;
  }
}
