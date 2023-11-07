package jsonvalues.spec;


import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spec that implements this interface will stop after finding the first code and will return that code
 */
sealed interface JsOneErrorSpec extends JsSpec permits AnySpec, AnySuchThat, IsJsObj, JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat, JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfDouble, JsArrayOfDoubleSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat, JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfObj, JsArrayOfObjSuchThat, JsArrayOfSpec, JsArrayOfStr, JsArrayOfStrSuchThat, JsArrayOfTestedBigInt, JsArrayOfTestedDecimal, JsArrayOfTestedDouble, JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedObj, JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue, JsArraySuchThat, JsBigIntSpec, JsBigIntSuchThat, JsBinarySpec, JsBinarySuchThat, JsBooleanSpec, JsDecimalSpec, JsDecimalSuchThat, JsDoubleSpec, JsDoubleSuchThat, JsEnum, JsFixedBinary, JsInstantSpec, JsInstantSuchThat, JsIntSpec, JsIntSuchThat, JsLongSpec, JsLongSuchThat, JsMapOfBigInt, JsMapOfBinary, JsMapOfBool, JsMapOfDec, JsMapOfDouble, JsMapOfInstant, JsMapOfInt, JsMapOfLong, JsMapOfSpec, JsMapOfStr, JsObjSuchThat, JsStrSpec, JsStrSuchThat {

    @Override
    default List<SpecError> test(final JsPath parentPath,
                                 final JsValue value
                                ) {
        List<SpecError> errors = new ArrayList<>();
        testValue(value).ifPresent(e -> errors.add(SpecError.of(parentPath,
                                                                e
                                                               )
                                                  )
                                  );
        return errors;
    }

    Optional<JsError> testValue(final JsValue value);

}
