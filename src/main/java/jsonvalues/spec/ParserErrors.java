package jsonvalues.spec;


import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

class ParserErrors {

  public static final String OBJ_CONDITION = "JSON Object was parsed but it doesn't conform the predicate specified with the spec method 'suchThat'";
  public static final String STR_LENGTH_LOWER_THAN_MINIMUM = "String length lower than minimum";
  public static final String STR_LENGTH_GREATER_THAN_MAXIMUM = "String length greater than maximum";
  public static final String STR_STRING_DOES_NOT_MATCH_PATTERN = "String does not match pattern";
  public static final String LONG_LOWER_THAN_MINIMUM = "Long number lower than minimum";
  public static final String LONG_GREATER_THAN_MAXIMUM = "Long number greater than maximum";
  public static final String LONG_NOT_MULTIPLE_OF = "Long number is not multiple of";
  public static final String INT_LOWER_THAN_MINIMUM = "Int number lower than minimum";
  public static final String INT_GREATER_THAN_MAXIMUM = "Int number greater than maximum";
  public static final String INT_NOT_MULTIPLE_OF = "Int number is not multiple of";
  public static final String DOUBLE_LOWER_THAN_MINIMUM = "Double number lower than minimum";
  public static final String DOUBLE_GREATER_THAN_MAXIMUM = "Double number greater than maximum";
  public static final String DOUBLE_NOT_MULTIPLE_OF = "Double number is not multiple of";
  public static final String BIGINT_LOWER_THAN_MINIMUM = "BigInteger number lower than minimum";
  public static final String BIGINT_GREATER_THAN_MAXIMUM = "BigInteger number greater than maximum";
  public static final String BIGINT_NOT_MULTIPLE_OF = "BigInteger number is not multiple of";
  public static final String DECIMAL_LOWER_THAN_MINIMUM = "BigDecimal number lower than minimum";
  public static final String DECIMAL_GREATER_THAN_MAXIMUM = "BigDecimal number greater than maximum";
  public static final String DECIMAL_NOT_MULTIPLE_OF = "BigDecimal number is not multiple of";
  public static final String OBJ_MAX_SIZE_EXCEEDED = "Object max size exceeded";
  public static final String OBJ_MIN_SIZE_NOT_MET = "Object min size not met";
  public static final String DUPLICATED_ARRAY_ITEM = "Duplicated array item";
  public static final String INSTANT_LOWER_THAN_MINIMUM = "Instant lower than minimum";
  public static final String INSTANT_GREATER_THAN_MAXIMUM = "Instant greater than maximum";
  static final String EXPECTING_FOR_OBJ_START = "Expecting '{' for Json object start but get %s";
  static final String EXPECTING_FOR_MAP_END = "Expecting '}' for Json object end but get %s";
  static final String ONE_OF_EXHAUSTED = "`OneOf` spec exhausted";

  static final String UNEXPECTED_END_OF_JSON = "Unexpected end of JSON";

  static final IntFunction<String> EMPTY_ARRAY = min -> "Empty array. Min size: " + min;
  static final IntFunction<String> TOO_LONG_ARRAY = max -> "Too long array. Max size: " + max;
  static final IntFunction<String> TOO_SHORT_ARRAY = min -> "Too short array. Min size: " + min;
  static final String INTEGRAL_NUMBER_EXPECTED = "Integral number expected";
  static final String EXPECTING_FOR_ARRAY_START = "Expecting '[' for Json array start";
  static final String BOOL_EXPECTED = "Boolean expected";
  static final String BIG_INTEGER_WITH_FRACTIONAL_PART = "`BigInteger` with fractional part";
  static final UnaryOperator<String> SPEC_NOT_FOUND =
      "The key '%s' has no spec associated to it. Strict specs don't allow this. Either declare de spec lenient or add a new spec for the missing key"::formatted;
  static final UnaryOperator<String> REQUIRED_KEY_NOT_FOUND =
      "The JSON doesn't conform the spec because the required key '%s' doesn't exist"::formatted;
  static final Function<JsError, String> JS_ERROR_2_STR =
      e -> "Error code: %s".formatted(e.code()
                                       .name());
  static final String TOO_MANY_DIGITS = "Too many digits detected in number: %d";
  static final String LEADING_ZERO = "Leading zero is not allowed";
  static final String NOT_VALID_NUMBER = "Invalid representation of a number";
  static final String DIGIT_NOT_FOUND = "Digit not found";
  static final String NUMBER_ENDS_DOT = "Number ends with a dot";
  static final String EXPECTING_INT_DECIMAL_FOUND = "Expecting int but found decimal value";
  static final String INTEGER_OVERFLOW = "Integer overflow";
  static final String LONG_OVERFLOW = "Long overflow";
  static final String EXPECTING_LONG_INSTEAD_OF_DECIMAL = "Expecting long, but found decimal value";
  static final String UNKNOWN_DIGIT = "Unknown digit";
  static final String INVALID_FIXED_BINARY_SIZE = "Invalid size for fixed type. Expected %s bytes but get %s";
  static final String EXPECTING_STRING_START = "Expecting '\"' for string start";
  static final String PREMATURE_END_OF_JSONSTRING = "Premature end of JSON string";
  static final String MAXIMUM_STRING_BUFFER_REACHED = "Maximum string buffer limit exceeded (%d)";
  static final String INVALID_ESCAPE_CHARACTER = "Invalid escape combination detected (%d)";
  static final String INVALID_UNICODE_CHARACTER = "Invalid unicode character detected";
  static final String STRING_NOT_CLOSED = "JSON string was not closed with a double quote";
  static final String INVALID_HEX = "Could not parse unicode escape, expected a hexadecimal digit";
  static final String EXPECTING_COLON = "Expecting ':' after attribute name";
  static final String INVALID_NULL = "Invalid null found";
  static final String INVALID_TRUE_CONSTANT = "Invalid true constant found";
  static final String INVALID_FALSE_CONSTANT = "Invalid false constant found";
  static final String UNEXPECTED_END_OF_ARRAY = "Unexpected end of JSON in collection";
  static final String EXPECTING_END_OF_ARRAY = "Expecting ']' as array end";

  private ParserErrors() {
  }


}
