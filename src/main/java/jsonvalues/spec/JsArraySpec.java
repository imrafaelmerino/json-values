package jsonvalues.spec;

/**
 * Represents a specification of a JSON array
 */
public sealed interface JsArraySpec extends JsSpec permits JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool, JsArrayOfBoolSuchThat, JsArrayOfDecimal, JsArrayOfDecimalSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat, JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfNumber, JsArrayOfNumberSuchThat, JsArrayOfObj, JsArrayOfObjSpec, JsArrayOfObjSuchThat, JsArrayOfStr, JsArrayOfStrSuchThat, JsArrayOfTestedBigInt, JsArrayOfTestedDecimal, JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedNumber, JsArrayOfTestedObj, JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue, JsArraySuchThat, JsTuple {}
