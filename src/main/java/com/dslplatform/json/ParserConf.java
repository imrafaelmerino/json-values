package com.dslplatform.json;

import jsonvalues.spec.JsError;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ParserConf {


    public static final String EXPECTING_FOR_MAP_START = "Expecting '{' for Json object start";
    public static final String EXPECTING_FOR_MAP_END = "Expecting '}' for Json object end";

    public static final IntFunction<String> A =
            min -> "empty array. Min size: " + min;

    public static final IntFunction<String> B =
            max -> "too long array. Max size: " + max;

    public static final IntFunction<String> C =
            min -> "too short array. Min size: " + min;

    public static final String INTEGRAL_NUMBER_EXPECTED = "Integral number expected.";

    public static final String EXPECTING_FOR_LIST_START = "Expecting '[' for Json array start";

    public static final String BOOL_EXPECTED = "Boolean expected";
    public static final String TRUE_EXPECTED = "True expected";
    public static final String FALSE_EXPECTED = "False expected";

    public static final Function<String,String> SPEC_NOT_FOUND = key -> "The key "+key+" has no spec associated.";

    public static final Function<String,String> REQUIRED_KEY_NOT_FOUND = key -> "The key "+key+" doesn't exist.";

    public static final Function<JsError,String> JS_ERROR_2_STR = e -> e.code.name();

    public static final String TOO_MANY_DIGITS = "Too many digits detected in number";
    
    public static final String LEADING_ZERO = "Leading zero is not allowed";


    public static final String ERROR_PARSING_NUMBER = "Error parsing number";

    public static final String DIGIT_NOT_FOUND = "Digit not found";

    public static final String  NUMBER_ENDS_DOT = "Number ends with a dot";

    public static final String EXPECTING_INT_DECIMAL_FOUND = "Expecting int but found decimal value";


    public static final String INTEGER_OVERFLOW = "Integer overflow";

    public static final String LONG_OVERFLOW = "Long overflow";

    public static final String EXPECTING_LONG_INSTEAD_OF_DECIMAL = "Expecting long, but found decimal value ";

    public static final String UNKNOWN_DIGIT = "Unknown digit";


}
