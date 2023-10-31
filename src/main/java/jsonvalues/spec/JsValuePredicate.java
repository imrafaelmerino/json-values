package jsonvalues.spec;


import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

sealed interface JsValuePredicate extends JsSpec permits AnySpec, AnySuchThat, IsJsObj, JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat, JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat, JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfNumber, JsArrayOfNumberSuchThat, JsArrayOfObj, JsArrayOfObjSuchThat, JsArrayOfStr, JsArrayOfStrSuchThat, JsArrayOfTestedBigInt, JsArrayOfTestedDecimal, JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedNumber, JsArrayOfTestedObj, JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue, JsArraySuchThat, JsBigIntSpec, JsBigIntSuchThat, JsBinarySpec, JsBinarySuchThat, JsBooleanSpec, JsDecimalSpec, JsDecimalSuchThat, JsFalseConstant, JsInstantSpec, JsInstantSuchThat, JsIntSpec, JsIntSuchThat, JsLongSpec, JsLongSuchThat, JsNumberSpec, JsNumberSuchThat, JsObjSuchThat, JsStrSpec, JsStrSuchThat, JsTrueConstant {

    @Override
    default Set<SpecError> test(final JsPath parentPath,
                                final JsValue value) {
        Set<SpecError> errors = new HashSet<>();
        testValue(value).ifPresent(e -> errors.add(SpecError.of(parentPath,
                                                                e
                                              )
                              )
        );
        return errors;
    }

    Optional<JsError> testValue(final JsValue value);

}
