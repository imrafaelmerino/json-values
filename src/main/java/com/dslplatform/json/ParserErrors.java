package com.dslplatform.json;

import jsonvalues.spec.JsError;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

class ParserErrors {


    private ParserErrors(){}
    static final String EXPECTING_FOR_MAP_START = "Expecting '{' for Json object start";
    static final String EXPECTING_FOR_MAP_END = "Expecting '}' for Json object end";

    static final IntFunction<String> EMPTY_ARRAY = min -> "empty array. Min size: " + min;

    static final IntFunction<String> TOO_LONG_ARRAY = max -> "too long array. Max size: " + max;

    static final IntFunction<String> TOO_SHORT_ARRAY = min -> "too short array. Min size: " + min;

    static final String INTEGRAL_NUMBER_EXPECTED = "Integral number expected.";

    static final String EXPECTING_FOR_LIST_START = "Expecting '[' for Json array start";

    static final String BOOL_EXPECTED = "Boolean expected";
    static final String TRUE_EXPECTED = "True expected";
    static final String FALSE_EXPECTED = "False expected";

    static final UnaryOperator<String> SPEC_NOT_FOUND = key -> "The key " + key + " has no spec associated.";

    static final UnaryOperator<String> REQUIRED_KEY_NOT_FOUND = key -> "The key " + key + " doesn't exist.";

    static final Function<JsError, String> JS_ERROR_2_STR = e -> e.code.name();

    static final String TOO_MANY_DIGITS = "Too many digits detected in number";

    static final String LEADING_ZERO = "Leading zero is not allowed";

    static final String ERROR_PARSING_NUMBER = "Error parsing number";

    static final String DIGIT_NOT_FOUND = "Digit not found";

    static final String NUMBER_ENDS_DOT = "Number ends with a dot";

    static final String EXPECTING_INT_DECIMAL_FOUND = "Expecting int but found decimal value";

    static final String INTEGER_OVERFLOW = "Integer overflow";

    static final String LONG_OVERFLOW = "Long overflow";

    static final String EXPECTING_LONG_INSTEAD_OF_DECIMAL = "Expecting long, but found decimal value ";

    static final String UNKNOWN_DIGIT = "Unknown digit";


}
