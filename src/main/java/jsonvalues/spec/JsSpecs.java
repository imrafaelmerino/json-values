package jsonvalues.spec;

import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.*;

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
    private static final JsArraySpec arrayOfIntegral = new JsArrayOfIntegralSpec(false);
    private static final JsArraySpec arrayOfObj = new JsArrayOfObjSpec(false);
    private static final JsArraySpec arrayOfBool = new JsArrayOfBoolSpec(false);
    private static final JsSpec instant = new JsInstantSpec(false);
    private static final JsArraySpec arrayOfLong = new JsArrayOfLongSpec(false);
    private static final JsArraySpec arrayOfInt = new JsArrayOfIntSpec(false);
    private static final JsArraySpec arrayOfStr = new JsArrayOfStrSpec(false);
    private static final JsSpec binary = new JsBinarySpec(false);
    private static final JsSpec integral = new JsIntegralSpec(false);
    private static final JsSpec longInteger = new JsLongSpec(false);
    private static final JsSpec bool = new JsBooleanSpec(false);
    private static final JsSpec decimal = new JsDecimalSpec(false);
    private static final JsSpec integer = new JsIntSpec(false);
    private static final JsSpec obj = new IsJsObjSpec(false);
    private static final JsSpec any = new AnySpec();
    private static final JsSpec number = new JsNumberSpec(false);
    private static final JsSpec str = new JsStrSpec(false);
    private static final JsArraySpec array = new JsArrayOfValueSpec(false);
    private static final String MAX_LOWER_THAN_MIN_ERROR = "max < min";

    private JsSpecs() {
    }

    /**
     * non-nullable array of numbers spec
     */
    public static JsArraySpec arrayOfNumber() {
        return arrayOfNumber;
    }

    /**
     * non-nullable array of decimal numbers spec
     */
    public static JsArraySpec arrayOfDec() {
        return arrayOfDec;
    }

    /**
     * non-nullable array of integral numbers spec
     */
    public static JsArraySpec arrayOfIntegral() {
        return arrayOfIntegral;
    }


    /**
     * non-nullable array of objects spec
     */
    public static JsArraySpec arrayOfObj() {
        return arrayOfObj;
    }

    /**
     * non-nullable array of booleans spec
     */
    public static JsArraySpec arrayOfBool() {
        return arrayOfBool;
    }

    /**
     * non-nullable array spec
     */
    public static JsSpec instant() {
        return instant;
    }

    /**
     * non-nullable array of long numbers spec
     */
    public static JsArraySpec arrayOfLong() {
        return arrayOfLong;
    }

    /**
     * non-nullable array of integer numbers spec
     */
    public static JsArraySpec arrayOfInt() {
        return arrayOfInt;
    }

    /**
     * non-nullable array of strings spec
     */
    public static JsArraySpec arrayOfStr() {
        return arrayOfStr;
    }

    /**
     * non-nullable array spec
     */
    public static JsSpec binary() {
        return binary;
    }

    /**
     * non-nullable integral number
     */
    public static JsSpec integral() {
        return integral;
    }


    /**
     * non-nullable long number
     */
    public static JsSpec longInteger() {
        return longInteger;
    }

    /**
     * non-nullable boolean
     */
    public static JsSpec bool() {
        return bool;
    }

    /**
     * non-nullable decimal number
     */
    public static JsSpec decimal() {
        return decimal;
    }

    /**
     * non-nullable integer number
     */
    public static JsSpec integer() {
        return integer;
    }

    /**
     * non-nullable json object spec
     */
    public static JsSpec obj() {
        return obj;
    }

    /**
     * non-nullable array spec
     */
    public static JsArraySpec array() {
        return array;
    }

    /**
     * spec that is conformed by any value
     */
    public static JsSpec any() {
        return any;
    }

    /**
     * non-nullable string
     */
    public static JsSpec str() {
        return str;
    }

    /**
     * non-nullable number
     */
    public static JsSpec number() {
        return number;
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfInt(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfIntSpec(false,
                                    min,
                                    max);
    }


    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfIntegral(int min,
                                              int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfIntegralSpec(false,
                                         min,
                                         max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfNumber(int min,
                                            int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfNumberSpec(false,
                                       min,
                                       max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfObj(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfObjSpec(false,
                                    min,
                                    max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfDec(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfDecimalSpec(false,
                                        min,
                                        max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfBool(int min,
                                          int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfBoolSpec(false,
                                     min,
                                     max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfStr(int min,
                                         int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfStrSpec(false,
                                    min,
                                    max);
    }

    /**
     * @param min minimum length of the array  (inclusive)
     * @param max maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfLong(int min,
                                          int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfLongSpec(false,
                                     min,
                                     max);
    }

    /**
     * A required and none nullable spec that specifies an array of objects that conform the given spec
     *
     * @param spec the given spec that every object in the array has to conform
     * @return a spec
     */
    public static JsArraySpec arrayOfObjSpec(final JsObjSpec spec) {
        return new JsArrayOfJsObjSpec(false,
                                      requireNonNull(spec));
    }

    /**
     * A required and none nullable spec that specifies an array of objects that conform the given spec
     *
     * @param spec the given spec that every object in the array has to conform
     * @param min  minimum length of the array  (inclusive)
     * @param max  maximum length of the array  (inclusive)
     * @return a spec
     */
    public static JsArraySpec arrayOfObjSpec(final JsObjSpec spec,
                                             int min,
                                             int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);
        return new JsArrayOfJsObjSpec(false,
                                      requireNonNull(spec),
                                      min,
                                      max);
    }

    /**
     * a required and non nullable spec that specifies a constant
     *
     * @param value the constant
     * @return a spec
     */
    public static JsSpec cons(JsValue value) {
        return new AnySuchThatSpec(v -> requireNonNull(value).equals(v) ?
                                        Optional.empty() :
                                        Optional.of(new JsError(v,
                                                                CONSTANT_CONDITION)));
    }

    /**
     * non-nullable string that satisfies the given predicate
     *
     * @param predicate the predicate
     * @return a JsSpec
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
     * non-nullable number that satisfies the given predicate
     *
     * @param predicate the predicate
     * @return a JsSpec
     */
    public static JsSpec number(final Predicate<JsNumber> predicate) {
        return new JsNumberSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                             Optional.empty() :
                                             Optional.of(new JsError(s,
                                                                     ERROR_CODE.NUMBER_CONDITION)),
                                        false);
    }

    /**
     * non-nullable array of numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(s,
                                                                         ARRAY_CONDITION)),
                                            false);

    }


    /**
     * non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each decimal number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate) {

        return new JsArrayOfTestedDecimalSpec(s -> requireNonNull(predicate).test(s) ?
                                                   Optional.empty() :
                                                   Optional.of(new JsError(JsBigDec.of(s),
                                                                           DECIMAL_CONDITION)),
                                              false);
    }

    /**
     * non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each decimal number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate,
                                         final int min,
                                         final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedDecimalSpec(s -> requireNonNull(predicate).test(s) ?
                                                   Optional.empty() :
                                                   Optional.of(new JsError(JsBigDec.of(s),
                                                                           DECIMAL_CONDITION)),
                                              false,
                                              min,
                                              max);
    }

    /**
     * non-nullable array of decimal numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfDecimalSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(s,
                                                                             ARRAY_CONDITION)),
                                                false);
    }

    /**
     * non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each integral number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntegral(final Predicate<BigInteger> predicate) {
        return new JsArrayOfTestedIntegralSpec(s -> requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(JsBigInt.of(s),
                                                                            INTEGRAL_CONDITION)),
                                               false);
    }

    /**
     * non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each integral number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntegral(final Predicate<BigInteger> predicate,
                                              final int min,
                                              final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedIntegralSpec(s -> requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(JsBigInt.of(s),
                                                                            INTEGRAL_CONDITION)),
                                               false,
                                               min,
                                               max);
    }

    /**
     * non-nullable array of integral numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntegralSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                      Optional.empty() :
                                                      Optional.of(new JsError(s,
                                                                              ARRAY_CONDITION)),
                                                 false);
    }

    /**
     * non-nullable array of numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate) {
        return new JsArrayOfTestedNumberSpec(s -> requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(s,
                                                                          ERROR_CODE.NUMBER_CONDITION)),
                                             false);
    }

    /**
     * non-nullable array of numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate,
                                            final int min,
                                            final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedNumberSpec(s -> requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(s,
                                                                          ERROR_CODE.NUMBER_CONDITION)),
                                             false,
                                             min,
                                             max);
    }

    /**
     * non-nullable array of numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfNumberSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(s,
                                                                            ARRAY_CONDITION)),
                                               false);
    }

    /**
     * non-nullable array of objects, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each object of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate) {
        return new JsArrayOfTestedObjSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(s,
                                                                       OBJ_CONDITION)),
                                          false);

    }

    /**
     * non-nullable array of objects, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each object of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate,
                                         final int min,
                                         final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedObjSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(s,
                                                                       OBJ_CONDITION)),
                                          false,
                                          min,
                                          max);

    }

    /**
     * non-nullable array of objects that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfObjSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(s,
                                                                         ARRAY_CONDITION)),
                                            false);

    }


    /**
     * non-nullable integer number that satisfies the given predicate
     *
     * @param predicate the predicate the integer is tested on
     * @return a spec
     */
    public static JsSpec integer(final IntPredicate predicate) {
        return new JsIntSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                          Optional.empty() :
                                          Optional.of(new JsError(JsInt.of(s),
                                                                  INT_CONDITION)),
                                     false);
    }

    /**
     * non-nullable array of strings that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfStrSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(s,
                                                                         ARRAY_CONDITION)),
                                            false);
    }

    /**
     * non-nullable array, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each value of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec array(final Predicate<JsValue> predicate) {
        return new JsArrayOfTestedValueSpec(s -> requireNonNull(predicate).test(s) ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(s,
                                                                         VALUE_CONDITION)),
                                            false);
    }


    /**
     * non-nullable array of long numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each long number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate) {
        return new JsArrayOfTestedLongSpec(s -> requireNonNull(predicate).test(s) ?
                                                Optional.empty() :
                                                Optional.of(new JsError(JsLong.of(s),
                                                                        LONG_CONDITION)),
                                           false);
    }

    /**
     * non-nullable array of long numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each long number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate,
                                          final int min,
                                          final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedLongSpec(s -> requireNonNull(predicate).test(s) ?
                                                Optional.empty() :
                                                Optional.of(new JsError(JsLong.of(s),
                                                                        LONG_CONDITION)),
                                           false,
                                           min,
                                           max);
    }

    /**
     * non-nullable array of booleans that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfBoolSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(s,
                                                                          ARRAY_CONDITION)),
                                             false);
    }

    /**
     * non-nullable long number that satisfies the given predicate
     *
     * @param predicate the predicate the long is tested on
     * @return a spec
     */
    public static JsSpec longInteger(final LongPredicate predicate) {
        return new JsLongSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                           Optional.empty() :
                                           Optional.of(new JsError(JsLong.of(s),
                                                                   LONG_CONDITION)),
                                      false);
    }

    /**
     * non-nullable decimal number that satisfies the given predicate
     *
     * @param predicate the predicate the decimal is tested on
     * @return a spec
     */
    public static JsSpec decimal(final Predicate<BigDecimal> predicate) {
        return new JsDecimalSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                              Optional.empty() :
                                              Optional.of(new JsError(JsBigDec.of(s),
                                                                      DECIMAL_CONDITION)),
                                         false);
    }

    /**
     * non-nullable integral number that satisfies the given predicate
     *
     * @param predicate the predicate the integral number is tested on
     * @return a spec
     */
    public static JsSpec integral(final Predicate<BigInteger> predicate) {
        return new JsIntegralSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(JsBigInt.of(s),
                                                                       INTEGRAL_CONDITION)),
                                          false);
    }

    /**
     * non-nullable array of strings, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each string of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate) {

        return new JsArrayOfTestedStrSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(JsStr.of(s),
                                                                       STRING_CONDITION)),
                                          false);
    }


    /**
     * non-nullable array of strings, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each string of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate,
                                         final int min,
                                         final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedStrSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(JsStr.of(s),
                                                                       STRING_CONDITION)),
                                          false,
                                          min,
                                          max);
    }

    /**
     * returns a spec that conforms any value that is evaluated to true on the predicate.
     * When the type is not specified by the spec, positive numbers are parsed as Long by default,
     * which has to be taken into account in order to define any condition.
     *
     * @param predicate the predicate
     * @return a spec
     */
    public static JsSpec any(final Predicate<JsValue> predicate) {
        return new AnySuchThatSpec(v -> requireNonNull(predicate).test(v) ?
                                        Optional.empty() :
                                        Optional.of(new JsError(v,
                                                                VALUE_CONDITION)));
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec binary(final Predicate<byte[]> predicate) {
        return new JsBinarySuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                             Optional.empty() :
                                             Optional.of(new JsError(JsBinary.of(s),
                                                                     BINARY_CONDITION)),
                                        false);
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec instant(final Predicate<Instant> predicate) {
        return new JsInstantSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                              Optional.empty() :
                                              Optional.of(new JsError(JsInstant.of(s),
                                                                      INSTANT_CONDITION)),
                                         false);
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec obj(final Predicate<JsObj> predicate) {
        return new JsObjSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                          Optional.empty() :
                                          Optional.of(new JsError(s,
                                                                  OBJ_CONDITION)),
                                     false);
    }

    /**
     * non-nullable array of long numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfLongSuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(s,
                                                                          ARRAY_CONDITION)),
                                             false);
    }

    /**
     * non-nullable array that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate) {
        return new JsArraySuchThatSpec(s -> requireNonNull(predicate).test(s) ?
                                            Optional.empty() :
                                            Optional.of(new JsError(s,
                                                                    ARRAY_CONDITION)),
                                       false);

    }

    /**
     * non-nullable array of integer numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each integer number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate) {
        return new JsArrayOfTestedIntSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(JsInt.of(s),
                                                                       INT_CONDITION)),
                                          false);

    }

    /**
     * non-nullable array of integer numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each integer number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate,
                                         final int min,
                                         final int max) {
        if (max < min) throw new IllegalArgumentException(MAX_LOWER_THAN_MIN_ERROR);

        return new JsArrayOfTestedIntSpec(s -> requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(JsInt.of(s),
                                                                       INT_CONDITION)),
                                          false,
                                          min,
                                          max);

    }

    /**
     * returns a tuple spec. Each nth-element of the tuple is specified by the nth given spec
     *
     * @param spec   the spec of the first element
     * @param others the rest of specs
     * @return a spec
     */
    public static JsTupleSpec tuple(JsSpec spec,
                                    JsSpec... others) {
        return JsTupleSpec.of(requireNonNull(spec),
                              requireNonNull(others));
    }

    /**
     * returns an enum spec
     *
     * @param cons the list of possible values
     * @param <O>  the type of the possible values (subtype of JsValue)
     * @return a spec
     */

    public static <O extends JsValue> JsSpec oneOf(final List<O> cons) {
        return any(o -> requireNonNull(cons).contains(o));
    }


}
