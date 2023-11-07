package jsonvalues.spec;

/**
 * The `ERROR_CODE` enum represents various code codes that can be associated with validation errors when using JSON
 * value specifications (`JsSpecs`). Each code corresponds to a specific type of validation failure.
 */
public enum ERROR_CODE {
    /**
     * Indicates that a string value was expected but a different type of value was encountered.
     */
    STRING_EXPECTED,

    /**
     * Represents a failed condition for a string value.
     */
    STRING_CONDITION,

    /**
     * Indicates that an integer value was expected but a different type of value was encountered.
     */
    INT_EXPECTED,

    /**
     * Indicates that a binary (byte array) value was expected but a different type of value was encountered.
     */
    BINARY_EXPECTED,

    /**
     * Indicates that a binary (byte array) value of a specified size was expected.
     */
    BINARY_FIXED_LENGTH_EXPECTED,

    /**
     * Indicates that an instant (date/time) value was expected but a different type of value was encountered.
     */
    INSTANT_EXPECTED,

    /**
     * Represents a failed condition for an integer value.
     */
    INT_CONDITION,

    /**
     * Indicates that a long integer value was expected but a different type of value was encountered.
     */
    LONG_EXPECTED,

    /**
     * Indicates that a double number value was expected but a different type of value was encountered.
     */
    DOUBLE_EXPECTED,

    /**
     * Represents a failed condition for a long integer value.
     */
    LONG_CONDITION,

    /**
     * Represents a failed condition for a double value.
     */
    DOUBLE_CONDITION,

    /**
     * Indicates that a boolean value was expected but a different type of value was encountered.
     */
    BOOLEAN_EXPECTED,

    /**
     * Represents a failed condition for a boolean value.
     */
    BOOLEAN_CONDITION,

    /**
     * Indicates that an integral number (integer or long) was expected but a different type of value was encountered.
     */
    INTEGRAL_EXPECTED,

    /**
     * Represents a failed condition for an integral number (integer or long).
     */
    INTEGRAL_CONDITION,

    /**
     * Indicates that a decimal number value was expected but a different type of value was encountered.
     */
    DECIMAL_EXPECTED,

    /**
     * Represents a failed condition for a decimal number value.
     */
    DECIMAL_CONDITION,

    /**
     * Indicates that a `true` boolean value was expected but a different boolean value was encountered.
     */
    TRUE_EXPECTED,

    /**
     * Indicates that a `false` boolean value was expected but a different boolean value was encountered.
     */
    FALSE_EXPECTED,

    /**
     * Indicates that an object (JSON object) value was expected but a different type of value was encountered.
     */
    OBJ_EXPECTED,

    /**
     * Represents a failed condition for an object value.
     */
    OBJ_CONDITION,

    /**
     * Represents a failed condition for a binary (byte array) value.
     */
    BINARY_CONDITION,

    /**
     * Indicates that an array value was expected but a different type of value was encountered.
     */
    ARRAY_EXPECTED,

    /**
     * Represents a failed condition for an array value.
     */
    ARRAY_CONDITION,

    /**
     * Indicates that a numeric value was expected but a different type of value was encountered.
     */
    NUMBER_EXPECTED,

    /**
     * Represents a failed condition for a numeric value.
     */
    NUMBER_CONDITION,

    /**
     * Represents a failed condition for an instant (date/time) value.
     */
    INSTANT_CONDITION,

    /**
     * Indicates that a `null` value was encountered.
     */
    NULL_NOT_EXPECTED,

    /**
     * Indicates that a `null` value was expected but a non-null value was encountered.
     */
    NULL_EXPECTED,

    /**
     * Indicates that a required value is missing.
     */
    REQUIRED,

    /**
     * Represents a failed condition for a generic JSON value.
     */
    VALUE_CONDITION,

    /**
     * Indicates that a specification is missing for validation.
     */
    SPEC_MISSING,

    /**
     * Represents a failed condition for a constant value.
     */
    CONSTANT_CONDITION,

    /**
     * Indicates that an array size is lower than the specified minimum length.
     */
    ARR_SIZE_LOWER_THAN_MIN,

    /**
     * Indicates that an array size is greater than the specified maximum length.
     */
    ARR_SIZE_GREATER_THAN_MAX,

    /**
     * A value from an enumeration is expected
     */
    ENUM_SYMBOL_EXPECTED,

    /**
     * All the specs from the `OneOf` spec was tried but the value doesn't confirm none of them
     */
    ONE_OF_SPEC_EXHAUSTED


}

