package jsonvalues.spec;


import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.*;

/**
 * The `JsSpecs` class provides a collection of static factory methods for creating JSON specifications (JsSpec).
 * These specifications are used to define validation rules and constraints for JSON data structures to ensure
 * that they conform to expected formats and patterns.
 * <p>
 * JSON specifications are essential for parsing and validating JSON data in applications, ensuring that the data
 * adheres to predefined rules before processing or storing it.
 * <p>
 * This class simplifies the creation of these specifications for various JSON data types, including numbers,
 * strings, arrays, objects, and more.
 * <p>
 * It provides factory methods for creating specifications for different JSON types, including numbers, strings,
 * arrays, and objects. Additionally, custom validation predicates can be specified to enforce specific
 * constraints on JSON data.
 * <p>
 * JSON specifications created with this class are immutable and can be reused for validating multiple JSON data instances.
 *
 * @see JsSpec
 * @see JsError
 */
public final class JsSpecs {


    /**
     * true constant spec
     */
    public static final JsSpec TRUE = new JsTrueConstantSpec(false);
    /**
     * false constant spec
     */
    public static final JsSpec FALSE = new JsFalseConstantSpec(false);

    private static final JsArraySpec arrayOfNumber = new JsArrayOfNumberSpec(false);
    private static final JsArraySpec arrayOfDec = new JsArrayOfDecimalSpec(false);
    private static final JsArraySpec arrayOfBigInt = new JsArrayOfBigIntSpec(false);
    private static final JsArraySpec arrayOfObj = new JsArrayOfObjSpec(false);
    private static final JsArraySpec arrayOfBool = new JsArrayOfBoolSpec(false);
    private static final JsSpec instant = new JsInstantSpec(false);
    private static final JsArraySpec arrayOfLong = new JsArrayOfLongSpec(false);
    private static final JsArraySpec arrayOfInt = new JsArrayOfIntSpec(false);
    private static final JsArraySpec arrayOfStr = new JsArrayOfStrSpec(false);
    private static final JsSpec binary = new JsBinarySpec(false);
    private static final JsSpec bigInteger = new JsBigIntSpec(false);
    private static final JsSpec longInteger = new JsLongSpec(false);
    private static final JsSpec bool = new JsBooleanSpec(false);
    private static final JsSpec decimal = new JsDecimalSpec(false);
    private static final JsSpec integer = new JsIntSpec(false);
    private static final JsSpec obj = new IsJsObjSpec(false);
    private static final JsSpec any = new AnySpec();
    private static final JsSpec number = new JsNumberSpec(false);
    private static final JsSpec str = new JsStrSpec(false);
    private static final JsArraySpec array = new JsArrayOfValueSpec(false);

    private static final JsSpec mapOfLongSpec = new JsMapOfLongSpec(false);

    private static final JsSpec mapOfStrSpec = new JsMapOfStrSpec(false);

    private static final JsSpec mapOfArraySpec = new JsMapOfArraySpec(false);


    private static final JsSpec mapOfIntSpec = new JsMapOfIntSpec(false);

    private static final JsSpec mapOfBoolSpec = new JsMapOfBoolSpec(false);

    private static final JsSpec mapOfDecimalSpec = new JsMapOfDecSpec(false);

    private static final JsSpec mapOfBigIntegerSpec = new JsMapOfBigIntSpec(false);

    private static final JsSpec mapOfInstantSpec = new JsMapOfInstantSpec(false);

    private static final JsSpec mapOfObjSpec = new JsMapOfObjSpec(false);


    private static final String MAX_LOWER_THAN_MIN_ERROR = "max < min";

    private JsSpecs() {
    }

    /**
     * non-nullable array of numbers spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfNumber() {
        return arrayOfNumber;
    }

    /**
     * non-nullable array of decimal numbers spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfDec() {
        return arrayOfDec;
    }

    /**
     * non-nullable array of integral numbers spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfBigInt() {
        return arrayOfBigInt;
    }


    /**
     * non-nullable array of objects spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfObj() {
        return arrayOfObj;
    }

    /**
     * non-nullable array of booleans spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfBool() {
        return arrayOfBool;
    }

    /**
     * non-nullable array spec
     *
     * @return a spec
     */
    public static JsSpec instant() {
        return instant;
    }

    /**
     * non-nullable array of long numbers spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfLong() {
        return arrayOfLong;
    }

    /**
     * non-nullable array of integer numbers spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfInt() {
        return arrayOfInt;
    }

    /**
     * non-nullable array of strings spec
     *
     * @return a spec
     */
    public static JsArraySpec arrayOfStr() {
        return arrayOfStr;
    }

    /**
     * non-nullable binary spec
     *
     * @return a spec
     * @see JsBinary
     */
    public static JsSpec binary() {
        return binary;
    }

    /**
     * non-nullable integral number
     *
     * @return a spec
     */
    public static JsSpec bigInteger() {
        return bigInteger;
    }


    /**
     * non-nullable long number
     *
     * @return a spec
     */
    public static JsSpec longInteger() {
        return longInteger;
    }

    /**
     * non-nullable boolean
     *
     * @return a spec
     */
    public static JsSpec bool() {
        return bool;
    }

    /**
     * non-nullable decimal number
     *
     * @return a spec
     */
    public static JsSpec decimal() {
        return decimal;
    }

    /**
     * non-nullable integer number
     *
     * @return a spec
     */
    public static JsSpec integer() {
        return integer;
    }

    /**
     * non-nullable json object spec
     *
     * @return a spec
     */
    public static JsSpec obj() {
        return obj;
    }

    /**
     * non-nullable array spec
     *
     * @return a spec
     */
    public static JsArraySpec array() {
        return array;
    }

    /**
     * spec that is conformed by any value
     *
     * @return a spec
     */
    public static JsSpec any() {
        return any;
    }

    /**
     * non-nullable string
     *
     * @return a spec
     */
    public static JsSpec str() {
        return str;
    }

    /**
     * Returns a non-nullable string specification that validates strings based on their length within the specified range.
     *
     * @param min The minimum allowed length for strings (inclusive).
     * @param max The maximum allowed length for strings (inclusive).
     * @return A specification for strings that have a length within the specified range.
     * @throws IllegalArgumentException If the provided min is negative, max is non-positive, or min is greater than max.
     */
    public static JsSpec str(int min,
                             int max
                            ) {
        if (min < 0) throw new IllegalArgumentException("min < 0");
        if (max <= 0) throw new IllegalArgumentException("max <= 0");
        if (min > max) throw new IllegalArgumentException("min > max");
        return str(s -> s.length() >= min && s.length() <= max);
    }


    /**
     * Returns a non-nullable string specification with the specified regular expression pattern.
     *
     * @param pattern The regular expression pattern to match strings against.
     * @return A specification for strings that match the given pattern.
     * @throws NullPointerException If the provided pattern is null.
     */
    public static JsSpec str(final Pattern pattern) {
        Objects.requireNonNull(pattern);
        return str(s -> pattern.matcher(s).matches());
    }

    /**
     * Returns a non-nullable number specification.
     *
     * @return A specification for numbers.
     */
    public static JsSpec number() {
        return number;
    }

    /**
     * Returns a specification for an array of integers with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of integers with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfInt(int minLength,
                                         int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfIntSpec(false,
                                    minLength,
                                    maxLength);
    }


    /**
     * Returns a specification for an array of big integers with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of big integers with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfBigInt(int minLength,
                                            int maxLength
                                           ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfBigIntSpec(false,
                                       minLength,
                                       maxLength);
    }

    /**
     * Returns a specification for an array of numbers with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of numbers with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfNumber(int minLength,
                                            int maxLength
                                           ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfNumberSpec(false,
                                       minLength,
                                       maxLength);
    }

    /**
     * Returns a specification for an array of objects with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of objects with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfObj(int minLength,
                                         int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfObjSpec(false,
                                    minLength,
                                    maxLength);
    }

    /**
     * Returns a specification for an array of decimal numbers with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of decimal numbers with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfDec(int minLength,
                                         int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfDecimalSpec(false,
                                        minLength,
                                        maxLength);
    }

    /**
     * Returns a specification for an array of booleans with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of booleans with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfBool(int minLength,
                                          int maxLength
                                         ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfBoolSpec(false,
                                     minLength,
                                     maxLength);
    }

    /**
     * Returns a specification for an array of strings with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of strings with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfStr(int minLength,
                                         int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfStrSpec(false,
                                    minLength,
                                    maxLength);
    }

    /**
     * Returns a specification for an array of long numbers with a specified minimum and maximum length.
     *
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification for an array of long numbers with the specified length constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfLong(int minLength,
                                          int maxLength
                                         ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfLongSpec(false,
                                     minLength,
                                     maxLength);
    }

    /**
     * Returns a required and non-nullable specification for an array of objects that conform to the given object specification.
     *
     * @param spec The object specification that every object in the array must conform to.
     * @return A specification that enforces an array of objects conforming to the provided object specification.
     */
    public static JsArraySpec arrayOfObjSpec(final JsObjSpec spec) {
        return new JsArrayOfJsObjSpec(false,
                                      requireNonNull(spec));
    }

    /**
     * Returns a required and non-nullable specification for an array of objects that conform to the given object specification.
     *
     * @param spec      The object specification that every object in the array must conform to.
     * @param minLength The minimum length of the array (inclusive).
     * @param maxLength The maximum length of the array (inclusive).
     * @return A specification that enforces an array of objects conforming to the provided object specification.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     * @see JsObjSpec
     */
    public static JsArraySpec arrayOfObjSpec(final JsObjSpec spec,
                                             int minLength,
                                             int maxLength
                                            ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfJsObjSpec(false,
                                      requireNonNull(spec),
                                      minLength,
                                      maxLength);
    }

    /**
     * Returns a required and non-nullable specification that specifies a constant value.
     *
     * @param value The constant value.
     * @return A specification that enforces the provided constant value.
     */
    public static JsSpec cons(final JsValue value) {
        return new AnySuchThatSpec(v -> requireNonNull(value).equals(v) ?
                Optional.empty() :
                Optional.of(new JsError(v,
                                        CONSTANT_CONDITION)));
    }

    /**
     * Returns a specification for a non-nullable string, where the string satisfies the given predicate.
     *
     * @param predicate The predicate that the string must satisfy.
     * @return A specification for strings based on the specified predicate.
     */
    public static JsSpec str(final Predicate<String> predicate) {
        return new JsStrSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsStr.of(s),
                                        STRING_CONDITION)),
                                     false

        );
    }

    /**
     * Returns a specification for a non-nullable number, where the number satisfies the given predicate.
     *
     * @param predicate The predicate that the number must satisfy.
     * @return A specification for numbers based on the specified predicate.
     */
    public static JsSpec number(final Predicate<JsNumber> predicate) {
        return new JsNumberSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s, ERROR_CODE.NUMBER_CONDITION)),
                                        false);
    }

    /**
     * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that the entire array must satisfy.
     * @return An array specification for integer numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                            false);

    }


    /**
     * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each decimal number in the array must satisfy.
     * @return An array specification for decimal numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate) {

        return new JsArrayOfTestedDecimalSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigDec.of(s),
                                        DECIMAL_CONDITION)),
                                              false);
    }

    /**
     * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate. The array must have a size within the specified range.
     *
     * @param predicate The predicate that each decimal number in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for decimal numbers based on the specified predicate and size range.
     * @throws IllegalArgumentException If the maximum length is lower than the minimum length.
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate,
                                         final int minLength,
                                         final int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedDecimalSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigDec.of(s),
                                        DECIMAL_CONDITION)),
                                              false,
                                              minLength,
                                              maxLength);
    }

    /**
     * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each decimal number in the array must satisfy.
     * @return An array specification for decimal numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfDecimalSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                                false);
    }

    /**
     * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each integral number in the array must satisfy.
     * @return An array specification for integral numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfBigInt(final Predicate<BigInteger> predicate) {
        return new JsArrayOfTestedBigIntSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigInt.of(s),
                                        INTEGRAL_CONDITION)),
                                             false);
    }

    /**
     * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate. The array must have a minimum and maximum size as specified by the parameters.
     *
     * @param predicate The predicate that each integral number in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for integral numbers based on the specified predicate and size constraints.
     * @throws IllegalArgumentException If the maximum size is lower than the minimum size.
     */
    public static JsArraySpec arrayOfBigInt(final Predicate<BigInteger> predicate,
                                            final int minLength,
                                            final int maxLength
                                           ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedBigIntSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigInt.of(s),
                                        INTEGRAL_CONDITION)),
                                             false,
                                             minLength,
                                             maxLength);
    }

    /**
     * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each integral number in the array must satisfy.
     * @return An array specification for integral numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfBigIntSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfBigIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                               false);
    }

    /**
     * Returns a specification for a non-nullable array of numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each number in the array must satisfy.
     * @return An array specification for numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate) {
        return new JsArrayOfTestedNumberSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ERROR_CODE.NUMBER_CONDITION)),
                                             false);
    }

    /**
     * Returns a specification for a non-nullable array of numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each number in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for numbers based on the specified predicate.
     * @throws IllegalArgumentException If maxLength is less than minLength.
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate,
                                            final int minLength,
                                            final int maxLength
                                           ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedNumberSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ERROR_CODE.NUMBER_CONDITION)),
                                             false,
                                             minLength,
                                             maxLength);
    }

    /**
     * Returns a specification for a non-nullable array of numbers, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each number in the array must satisfy.
     * @return An array specification for numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfNumberSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                               false);
    }

    /**
     * Returns a specification for a non-nullable array of objects, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each object in the array must satisfy.
     * @return An array specification for objects based on the specified predicate.
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate) {
        return new JsArrayOfTestedObjSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        OBJ_CONDITION)),
                                          false);

    }

    /**
     * Returns a specification for a non-nullable array of objects, where each element of the array satisfies
     * the given predicate.
     *
     * @param predicate The predicate that each object in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for objects based on the specified predicate and size constraints.
     * @throws IllegalArgumentException If the maximum length is less than the minimum length.
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate,
                                         final int minLength,
                                         final int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedObjSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        OBJ_CONDITION)),
                                          false,
                                          minLength,
                                          maxLength);

    }

    /**
     * Returns a specification for a non-nullable array of objects that satisfies the given predicate.
     *
     * @param predicate The predicate that the array must satisfy.
     * @return An array specification for objects based on the specified predicate.
     */
    public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfObjSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                            false);

    }


    /**
     * Returns a specification for a non-nullable integer number that satisfies the given predicate.
     *
     * @param predicate The predicate that the integer must satisfy.
     * @return A specification for integer numbers based on the specified predicate.
     */
    public static JsSpec integer(final IntPredicate predicate) {
        return new JsIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsInt.of(s),
                                        INT_CONDITION)),
                                     false);
    }

    /**
     * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate that the array must satisfy.
     * @return An array specification for strings based on the specified predicate.
     */
    public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfStrSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                            false);
    }

    /**
     * Returns a specification for a non-nullable array, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate that each value in the array must satisfy.
     * @return An array specification for values based on the specified predicate.
     */
    public static JsArraySpec array(final Predicate<JsValue> predicate) {
        return new JsArrayOfTestedValueSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        VALUE_CONDITION)),
                                            false);
    }


    /**
     * Returns a specification for a non-nullable array of long numbers, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate that each long number in the array must satisfy.
     * @return An array specification for long numbers based on the specified predicate.
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate) {
        return new JsArrayOfTestedLongSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsLong.of(s),
                                        LONG_CONDITION)),
                                           false);
    }

    /**
     * Returns a specification for a non-nullable array of long numbers, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate that each long number in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for long numbers based on the specified predicate.
     * @throws IllegalArgumentException If maxLength is less than minLength.
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate,
                                          final int minLength,
                                          final int maxLength
                                         ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedLongSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsLong.of(s),
                                        LONG_CONDITION)),
                                           false,
                                           minLength,
                                           maxLength);
    }

    /**
     * Returns a specification for a non-nullable array of booleans that satisfies the given predicate.
     *
     * @param predicate The predicate that the array must satisfy.
     * @return An array specification for booleans based on the specified predicate.
     */
    public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfBoolSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                             false);
    }

    /**
     * Returns a specification for a non-nullable long number that satisfies the given predicate.
     *
     * @param predicate The predicate that the long number must satisfy.
     * @return A specification for long numbers based on the specified predicate.
     */
    public static JsSpec longInteger(final LongPredicate predicate) {
        return new JsLongSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsLong.of(s),
                                        LONG_CONDITION)),
                                      false);
    }

    /**
     * Returns a specification for a non-nullable decimal number that satisfies the given predicate.
     *
     * @param predicate The predicate that the decimal number must satisfy.
     * @return A specification for decimal numbers based on the specified predicate.
     */
    public static JsSpec decimal(final Predicate<BigDecimal> predicate) {
        return new JsDecimalSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigDec.of(s),
                                        DECIMAL_CONDITION)),
                                         false);
    }

    /**
     * Returns a specification for a non-nullable integral number that satisfies the given predicate.
     *
     * @param predicate The predicate that the integral number must satisfy.
     * @return A specification for integral numbers based on the specified predicate.
     */
    public static JsSpec bigInteger(final Predicate<BigInteger> predicate) {
        return new JsBigIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBigInt.of(s),
                                        INTEGRAL_CONDITION)),
                                        false);
    }

    /**
     * Returns a specification for a non-nullable array of strings, where each element of the array
     * satisfies the given predicate.
     *
     * @param predicate The predicate that each string in the array must satisfy.
     * @return An array specification for strings based on the specified predicate.
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate) {

        return new JsArrayOfTestedStrSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsStr.of(s),
                                        STRING_CONDITION
                            )
                           ),
                                          false);
    }


    /**
     * Returns a specification for a non-nullable array of strings, where each element of the array
     * satisfies the given predicate.
     *
     * @param predicate The predicate that each string in the array must satisfy.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification for strings based on the specified predicate and size constraints.
     * @throws IllegalArgumentException If maxLength is less than minLength.
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate,
                                         final int minLength,
                                         final int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedStrSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsStr.of(s),
                                        STRING_CONDITION)),
                                          false,
                                          minLength,
                                          maxLength);
    }

    /**
     * Returns a specification that accepts any JSON value for which the given predicate evaluates to true.
     * When the type is not specified by the spec, positive numbers are parsed as Long by default,
     * which has to be taken into account when defining the condition.
     *
     * @param predicate The predicate that determines whether a JSON value is accepted.
     * @return A specification that validates JSON values based on the specified predicate.
     */
    public static JsSpec any(final Predicate<JsValue> predicate) {
        return new AnySuchThatSpec(v -> requireNonNull(predicate).test(v) ?
                Optional.empty() :
                Optional.of(new JsError(v,
                                        VALUE_CONDITION)));
    }

    /**
     * Returns a specification for a non-nullable JSON binary data that satisfies the given predicate.
     *
     * @param predicate The predicate the JSON binary data is tested on.
     * @return A specification that enforces the specified condition for JSON binary data.
     * @see JsBinary
     */
    public static JsSpec binary(final Predicate<byte[]> predicate) {
        return new JsBinarySuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsBinary.of(s),
                                        BINARY_CONDITION)),
                                        false);
    }

    /**
     * Returns a specification for a non-nullable JSON instant that satisfies the given predicate.
     *
     * @param predicate The predicate the JSON instant is tested on.
     * @return A specification that enforces the specified condition for JSON instants.
     */
    public static JsSpec instant(final Predicate<Instant> predicate) {
        return new JsInstantSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsInstant.of(s),
                                        INSTANT_CONDITION)),
                                         false);
    }

    /**
     * Returns a specification for a non-nullable JSON object that satisfies the given predicate.
     *
     * @param predicate The predicate the JSON object is tested on.
     * @return A specification that enforces the specified condition for JSON objects.
     */
    public static JsSpec obj(final Predicate<JsObj> predicate) {
        return new JsObjSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        OBJ_CONDITION)),
                                     false);
    }

    /**
     * Returns a specification for a non-nullable array of long numbers that satisfies the given predicate.
     *
     * @param predicate The predicate the array is tested on.
     * @return An array specification that enforces the specified condition for arrays of long numbers.
     */
    public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfLongSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                             false);
    }

    /**
     * Returns a specification for a non-nullable array that satisfies the given predicate.
     *
     * @param predicate The predicate the array is tested on.
     * @return An array specification that enforces the specified condition for arrays.
     */
    public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate) {
        return new JsArraySuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(s,
                                        ARRAY_CONDITION)),
                                       false);

    }

    /**
     * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate each integer number of the array is tested on.
     * @return An array specification that enforces the specified condition for integer elements.
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate) {
        return new JsArrayOfTestedIntSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsInt.of(s),
                                        INT_CONDITION)),
                                          false);

    }

    /**
     * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies the given predicate.
     *
     * @param predicate The predicate each integer number of the array is tested on.
     * @param minLength The minimum size of the array (inclusive).
     * @param maxLength The maximum size of the array (inclusive).
     * @return An array specification that enforces the specified conditions.
     * @throws IllegalArgumentException If maxLength is less than minLength.
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate,
                                         final int minLength,
                                         final int maxLength
                                        ) {
        if (maxLength < minLength) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedIntSpec(s -> requireNonNull(predicate).test(s) ?
                Optional.empty() :
                Optional.of(new JsError(JsInt.of(s),
                                        INT_CONDITION)),
                                          false,
                                          minLength,
                                          maxLength);

    }

    /**
     * Returns a tuple specification where each nth element of the tuple is specified by the nth given spec.
     *
     * @param spec   The specification for the first element.
     * @param others The specifications for the rest of the elements.
     * @return A specification for a tuple that enforces the specified structure.
     */
    public static JsTupleSpec tuple(JsSpec spec,
                                    JsSpec... others
                                   ) {
        return JsTupleSpec.of(requireNonNull(spec),
                              requireNonNull(others));
    }

    /**
     * Returns a specification that validates if a JSON value is one of the given possible values.
     *
     * @param cons The list of possible JSON values.
     * @param <O>  The type of the possible JSON values (subtype of JsValue).
     * @return A specification that checks if a JSON value matches one of the provided values.
     */
    public static <O extends JsValue> JsSpec oneOf(final List<O> cons) {
        return any(o -> requireNonNull(cons).contains(o));
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is a long number.
     *
     * @return A JSON specification for objects with long number values.
     */
    public static JsSpec mapOfLong() {
        return mapOfLongSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is an integer.
     *
     * @return A JSON specification for objects with integer values.
     */
    public static JsSpec mapOfInteger() {
        return mapOfIntSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is a big integer.
     *
     * @return A JSON specification for objects with big integer values.
     */
    public static JsSpec mapOfBigInteger() {
        return mapOfBigIntegerSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is a decimal number.
     *
     * @return A JSON specification for objects with decimal number values.
     */
    public static JsSpec mapOfDecimal() {
        return mapOfDecimalSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is a boolean.
     *
     * @return A JSON specification for objects with boolean values.
     */
    public static JsSpec mapOfBool() {
        return mapOfBoolSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is an instant.
     *
     * @return A JSON specification for objects with instant values.
     */
    public static JsSpec mapOfInstant() {
        return mapOfInstantSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is an object.
     *
     * @return A JSON specification for objects with object values.
     */
    public static JsSpec mapOfObj() {
        return mapOfObjSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is a string.
     *
     * @return A JSON specification for objects with string values.
     */
    public static JsSpec mapOfStr() {
        return mapOfStrSpec;
    }

    /**
     * Returns a specification that validates that the JSON is an object, and the value of each key is an array.
     *
     * @return A JSON specification for objects with array values.
     */
    public static JsSpec mapOfArray() {
        return mapOfArraySpec;
    }


}
