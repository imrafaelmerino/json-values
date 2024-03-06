package jsonvalues.spec;


import java.util.ArrayList;
import java.util.List;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

/**
 * Spec that implements this interface will stop after finding the first error and will return that error
 */
sealed interface JsOneErrorSpec extends JsSpec permits AnySpec, AnySuchThat, Cons, IsJsObj, JsArrayOfBigInt,
                                                       JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat,
                                                       JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfDouble,
                                                       JsArrayOfDoubleSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat,
                                                       JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfObj,
                                                       JsArrayOfObjSuchThat, JsArrayOfSpec, JsArrayOfStr,
                                                       JsArrayOfStrSuchThat, JsArrayOfTestedBigInt,
                                                       JsArrayOfTestedDecimal, JsArrayOfTestedDouble,
                                                       JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedObj,
                                                       JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue,
                                                       JsArraySuchThat, JsBigIntSpec, JsBigIntSuchThat, JsBinarySpec,
                                                       JsBinarySuchThat, JsBooleanSpec, JsDecimalSpec,
                                                       JsDecimalSuchThat, JsDoubleSpec, JsDoubleSuchThat, JsEnum,
                                                       JsFixedBinary, JsInstantSpec, JsInstantSuchThat, JsIntSpec,
                                                       JsIntSuchThat, JsLongSpec, JsLongSuchThat, JsMapOfBigInt,
                                                       JsMapOfBinary, JsMapOfBool, JsMapOfDec, JsMapOfDouble,
                                                       JsMapOfInstant, JsMapOfInt, JsMapOfLong, JsMapOfSpec, JsMapOfStr,
                                                       JsObjSuchThat, JsStrSpec, JsStrSuchThat {

  @Override
  default List<SpecError> test(final JsPath parentPath,
                               final JsValue value
                              ) {
    List<SpecError> errors = new ArrayList<>();
    JsError error = testValue(value);
    if (error != null) {
      errors.add(SpecError.of(parentPath,
                              error
                             )
                );
    }
    return errors;
  }

  JsError testValue(final JsValue value);

}
