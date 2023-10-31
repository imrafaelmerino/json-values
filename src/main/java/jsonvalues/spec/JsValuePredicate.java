package jsonvalues.spec;


import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

sealed interface JsValuePredicate extends JsSpec permits AnySpec, AnySuchThat, IsJsObj, JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat, JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat, JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfNumber, JsArrayOfNumberSuchThat, JsArrayOfObj, JsArrayOfObjSuchThat, JsArrayOfStr, JsArrayOfStrSuchThat, JsArrayOfTestedBigInt, JsArrayOfTestedDecimal, JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedNumber, JsArrayOfTestedObj, JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue, JsArraySuchThat, JsBigIntSpec, JsBigIntSuchThat, JsBinarySpec, JsBinarySuchThat, JsBooleanSpec, JsDecimalSpec, JsDecimalSuchThat, JsFalseConstant, JsFixedBinarySpec, JsInstantSpec, JsInstantSuchThat, JsIntSpec, JsIntSuchThat, JsLongSpec, JsLongSuchThat, JsNumberSpec, JsNumberSuchThat, JsObjSuchThat, JsStrSpec, JsStrSuchThat, JsTrueConstant {

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
