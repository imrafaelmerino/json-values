package jsonvalues.spec;

import fun.gen.Gen;
import jsonvalues.JsArray;
import jsonvalues.gen.JsObjGen;

/**
 * Represents a specification of a JSON array
 */
public sealed interface JsArraySpec extends JsSpec permits JsArrayOfBigInt, JsArrayOfBigIntSuchThat, JsArrayOfBool,
                                                           JsArrayOfBoolSuchThat, JsArrayOfDecimal,
                                                           JsArrayOfDecimalSuchThat, JsArrayOfDouble,
                                                           JsArrayOfDoubleSuchThat, JsArrayOfInt, JsArrayOfIntSuchThat,
                                                           JsArrayOfLong, JsArrayOfLongSuchThat, JsArrayOfObj,
                                                           JsArrayOfSpec, JsArrayOfObjSuchThat, JsArrayOfStr,
                                                           JsArrayOfStrSuchThat, JsArrayOfTestedBigInt,
                                                           JsArrayOfTestedDecimal, JsArrayOfTestedDouble,
                                                           JsArrayOfTestedInt, JsArrayOfTestedLong, JsArrayOfTestedObj,
                                                           JsArrayOfTestedStr, JsArrayOfTestedValue, JsArrayOfValue,
                                                           JsArraySuchThat, JsTuple {



}
