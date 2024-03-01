package jsonvalues.spec;


import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.NULL_NOT_EXPECTED;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsDouble;
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

  public static JsError testStrConstraints(final StrConstraints constraints,
                                           final JsStr jsStr) {
    String value = jsStr.value;
    int length = value.length();
    if (length < constraints.minLength) {
      return new JsError(jsStr,
                         ERROR_CODE.STR_SIZE_LOWER_THAN_MIN
      );
    }
    if (length > constraints.maxLength) {
      return new JsError(jsStr,
                         ERROR_CODE.STR_SIZE_GREATER_THAN_MAX
      );
    }
    if (constraints.pattern != null && !constraints.pattern.matcher(value)
                                                           .matches()) {
      return new JsError(jsStr,
                         ERROR_CODE.STR_PATTERN_NOT_MATCH
      );
    }

    return null;
  }

  public static JsError testLongConstraints(final LongSchemaConstraints constraints,
                                            final JsLong value) {
    if (value.value < constraints.minimum()) {
      return new JsError(value,
                         ERROR_CODE.LONG_LOWER_THAN_MIN
      );
    }
    if (value.value > constraints.maximum()) {
      return new JsError(value,
                         ERROR_CODE.LONG_GREATER_THAN_MAX
      );
    }
    if (constraints.multipleOf() != 0 && value.value % constraints.multipleOf() != 0) {
      return new JsError(value,
                         ERROR_CODE.LONG_NOT_MULTIPLE_OF
      );
    }
    return null;
  }

  public static JsError testIntConstraints(final IntegerSchemaConstraints constraints,
                                           final JsInt value) {
    if (value.value < constraints.minimum()) {
      return new JsError(value,
                         ERROR_CODE.INT_LOWER_THAN_MIN
      );
    }
    if (value.value > constraints.maximum()) {
      return new JsError(value,
                         ERROR_CODE.INT_GREATER_THAN_MAX
      );
    }
    if (constraints.multipleOf() != 0 && value.value % constraints.multipleOf() != 0) {
      return new JsError(value,
                         ERROR_CODE.INT_NOT_MULTIPLE_OF
      );
    }
    return null;
  }

  public static JsError testDoubleConstraints(final DoubleSchemaConstraints constraints,
                                              final JsDouble value) {
    if (value.value < constraints.minimum()) {
      return new JsError(value,
                         ERROR_CODE.DOUBLE_LOWER_THAN_MIN
      );
    }
    if (value.value > constraints.maximum()) {
      return new JsError(value,
                         ERROR_CODE.DOUBLE_GREATER_THAN_MAX
      );
    }
    if (constraints.multipleOf() != 0 && value.value % constraints.multipleOf() != 0) {
      return new JsError(value,
                         ERROR_CODE.DOUBLE_NOT_MULTIPLE_OF
      );
    }
    return null;
  }

  public static JsError testDecimalConstraints(final DecimalSchemaConstraints constraints,
                                               final JsBigDec jsBigDec) {
    if (constraints.minimum() != null
        && jsBigDec.value.compareTo(constraints.minimum()) < 0) {
      return new JsError(jsBigDec,
                         ERROR_CODE.DECIMAL_LOWER_THAN_MIN
      );
    }
    if (constraints.maximum() != null
        && jsBigDec.value.compareTo(constraints.maximum()) > 0) {
      return new JsError(jsBigDec,
                         ERROR_CODE.DECIMAL_GREATER_THAN_MAX
      );
    }
    if (constraints.multipleOf() != null &&
        jsBigDec.value.remainder(constraints.multipleOf())
                      .compareTo(BigDecimal.ZERO) != 0) {
      return new JsError(jsBigDec,
                         ERROR_CODE.DECIMAL_NOT_MULTIPLE_OF
      );
    }
    return null;
  }

  public static JsError testBigIntConstraints(final BigIntSchemaConstraints constraints,
                                              final JsBigInt jsBigInt) {
    if (constraints.minimum() != null && jsBigInt.value.compareTo(constraints.minimum()) < 0) {
      return new JsError(jsBigInt,
                         ERROR_CODE.BIGINT_LOWER_THAN_MIN
      );
    }
    if (constraints.maximum() != null && jsBigInt.value.compareTo(constraints.maximum()) > 0) {
      return new JsError(jsBigInt,
                         ERROR_CODE.BIGINT_GREATER_THAN_MAX
      );
    }
    if (constraints.multipleOf() != null
        && jsBigInt.value.remainder(constraints.multipleOf())
                         .compareTo(BigInteger.ZERO) != 0) {
      return new JsError(jsBigInt,
                         ERROR_CODE.BIGINT_NOT_MULTIPLE_OF
      );
    }

    return null;
  }
}
