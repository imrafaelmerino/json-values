package jsonvalues.spec;

import jsonvalues.JsValue;

sealed interface AvroSpec permits JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat, JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat, JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfNumber, JsArrayOfNumberSuchThat, JsArrayOfObjSpec, JsArrayOfStr, JsArrayOfStrSuchThat, JsArrayOfTestedBigInt, JsArrayOfTestedDecimal, JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedNumber, JsArrayOfTestedStr, JsBigIntSpec, JsBigIntSuchThat, JsBinarySpec, JsBinarySuchThat, JsBooleanSpec, JsDecimalSpec, JsDecimalSuchThat, JsEnum, JsFalseConstant, JsFixedBinarySpec, JsInstantSpec, JsInstantSuchThat, JsIntSpec, JsIntSuchThat, JsLongSpec, JsLongSuchThat, JsMapOfArraySpec, JsMapOfBigInt, JsMapOfBool, JsMapOfDec, JsMapOfInstant, JsMapOfInt, JsMapOfLong, JsMapOfObjSpec, JsMapOfStr, JsNumberSpec, JsNumberSuchThat, JsObjSpec, JsStrSpec, JsStrSuchThat, JsTrueConstant, OneOf, OneOfObjSpec {


    JsValue toAvroSchema() throws SpecNotSupportedInAvro;
}


