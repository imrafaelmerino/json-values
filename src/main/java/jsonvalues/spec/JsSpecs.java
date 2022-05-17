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
     * spec that is conformed by any value
     */
    public static final JsSpec any = new AnySpec();
    /**
     * non-nullable string
     */
    public static final JsSpec str = new JsStrSpec(false);
    /**
     * non-nullable number
     */
    public static final JsSpec number = new JsNumberSpec(false);
    /**
     * non-nullable boolean
     */
    public static final JsSpec bool = new JsBooleanSpec(false);
    /**
     * non-nullable decimal number
     */
    public static final JsSpec decimal = new JsDecimalSpec(false);
    /**
     * non-nullable integral number
     */
    public static final JsSpec integral = new JsIntegralSpec(false);
    /**
     * non-nullable long number
     */
    public static final JsSpec longInteger = new JsLongSpec(false);
    /**
     * non-nullable integer number
     */
    public static final JsSpec integer = new JsIntSpec(false);
    /**
     * true constant spec
     */
    public static final JsSpec TRUE = new JsTrueConstantSpec(false);
    /**
     * false constant spec
     */
    public static final JsSpec FALSE = new JsFalseConstantSpec(false);
    /**
     * non-nullable json object spec
     */
    public static final JsSpec obj = new IsJsObjSpec(false);
    /**
     * non-nullable array spec
     */
    public static final JsArraySpec array = new JsArrayOfValueSpec(false);
    /**
     * non-nullable array spec
     */
    public static final JsSpec binary = new JsBinarySpec(false);
    /**
     * non-nullable array spec
     */
    public static final JsSpec instant = new JsInstantSpec(false);
    /**
     * non-nullable array of long numbers spec
     */
    public static final JsArraySpec arrayOfLong = new JsArrayOfLongSpec(false);
    /**
     * non-nullable array of integer numbers spec
     */
    public static final JsArraySpec arrayOfInt = new JsArrayOfIntSpec(false);
    /**
     * non-nullable array of strings spec
     */
    public static final JsArraySpec arrayOfStr = new JsArrayOfStrSpec(false);
    /**
     * non-nullable array of booleans spec
     */
    public static final JsArraySpec arrayOfBool = new JsArrayOfBoolSpec(false);
    /**
     * non-nullable array of decimal numbers spec
     */
    public static final JsArraySpec arrayOfDec = new JsArrayOfDecimalSpec(false);
    /**
     * non-nullable array of numbers spec
     */
    public static final JsArraySpec arrayOfNumber = new JsArrayOfNumberSpec(false);
    /**
     * non-nullable array of integral numbers spec
     */
    public static final JsArraySpec arrayOfIntegral = new JsArrayOfIntegralSpec(false);
    /**
     * non-nullable array of objects spec
     */
    public static final JsArraySpec arrayOfObj = new JsArrayOfObjSpec(false);

    private JsSpecs() {
    }

    /**
     * A required and none nullable spec that specifies an array of objects that conform the given spec
     *
     * @param spec the given spec that every object in the array has to conform
     * @return a spec
     */
    public static JsArraySpec arrayOfObjSpec(final JsObjSpec spec) {
        return new JsArrayOfJsObjSpec(false,
                                      requireNonNull(spec)
        );
    }

    /**
     * a required and non nullable spec that specifies a constant
     *
     * @param value the constant
     * @return a spec
     */
    public static JsSpec cons(JsValue value) {
        return new AnySuchThatSpec(v ->
                                           requireNonNull(value).equals(v) ?
                                           Optional.empty() :
                                           Optional.of(new JsError(v,
                                                                   CONSTANT_CONDITION
                                                       )
                                           )
        );
    }

    /**
     * non-nullable string that satisfies the given predicate
     *
     * @param predicate the predicate
     * @return a JsSpec
     */
    public static JsSpec str(final Predicate<String> predicate) {
        return new JsStrSuchThatSpec(
                false,
                s ->
                        requireNonNull(predicate).test(s) ?
                        Optional.empty() :
                        Optional.of(new JsError(JsStr.of(s),
                                                STRING_CONDITION
                        ))

        );
    }

    /**
     * non-nullable number that satisfies the given predicate
     *
     * @param predicate the predicate
     * @return a JsSpec
     */
    public static JsSpec number(final Predicate<JsNumber> predicate) {
        return new JsNumberSuchThatSpec(
                false,
                s ->
                        requireNonNull(predicate).test(s) ?
                        Optional.empty() :
                        Optional.of(new JsError(s,
                                                ERROR_CODE.NUMBER_CONDITION
                        ))
        );
    }

    /**
     * non-nullable array of numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntSuchThatSpec(s ->
                                                    requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(s,
                                                                            ARRAY_CONDITION
                                                    )),
                                            false
        );

    }


    /**
     * non-nullable array of decimal numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each decimal number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate) {

        return new JsArrayOfTestedDecimalSpec(s ->
                                                      requireNonNull(predicate).test(s) ?
                                                      Optional.empty() :
                                                      Optional.of(new JsError(JsBigDec.of(s),
                                                                              DECIMAL_CONDITION
                                                      )),
                                              false
        );
    }

    /**
     * non-nullable array of decimal numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate
    ) {
        return new JsArrayOfDecimalSuchThatSpec(s ->
                                                        requireNonNull(predicate).test(s) ?
                                                        Optional.empty() :
                                                        Optional.of(new JsError(s,
                                                                                ARRAY_CONDITION
                                                        )),
                                                false
        );
    }

    /**
     * non-nullable array of integral numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each integral number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntegral(final Predicate<BigInteger> predicate
    ) {
        return new JsArrayOfTestedIntegralSpec(s ->
                                                       requireNonNull(predicate).test(s) ?
                                                       Optional.empty() :
                                                       Optional.of(new JsError(JsBigInt.of(s),
                                                                               INTEGRAL_CONDITION
                                                       )),
                                               false
        );
    }

    /**
     * non-nullable array of integral numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntegralSuchThatSpec(s ->
                                                         requireNonNull(predicate).test(s) ?
                                                         Optional.empty() :
                                                         Optional.of(new JsError(s,
                                                                                 ARRAY_CONDITION
                                                                     )
                                                         ),
                                                 false
        );
    }

    /**
     * non-nullable array of numbers, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate) {
        return new JsArrayOfTestedNumberSpec(s ->
                                                     requireNonNull(predicate).test(s) ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(s,
                                                                             ERROR_CODE.NUMBER_CONDITION
                                                                 )
                                                     ),
                                             false
        );
    }

    /**
     * non-nullable array of numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfNumberSuchThatSpec(s ->
                                                       requireNonNull(predicate).test(s) ?
                                                       Optional.empty() :
                                                       Optional.of(new JsError(s,
                                                                               ARRAY_CONDITION
                                                                   )
                                                       ),
                                               false
        );
    }

    /**
     * non-nullable array of objects, where each element of the array satisfies
     * the given predicate
     *
     * @param predicate the predicate each object of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate
    ) {
        return new JsArrayOfTestedObjSpec(s ->
                                                  requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(s,
                                                                          OBJ_CONDITION
                                                              )
                                                  ),
                                          false
        );

    }

    /**
     * non-nullable array of objects that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfObjSuchThatSpec(s ->
                                                    requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(s,
                                                                            ARRAY_CONDITION
                                                                )
                                                    ),
                                            false
        );

    }


    /**
     * non-nullable integer number that satisfies the given predicate
     *
     * @param predicate the predicate the integer is tested on
     * @return a spec
     */
    public static JsSpec integer(final IntPredicate predicate) {
        return new JsIntSuchThatSpec(false,
                                     s ->
                                             requireNonNull(predicate).test(s) ?
                                             Optional.empty() :
                                             Optional.of(new JsError(JsInt.of(s),
                                                                     INT_CONDITION
                                                         )
                                             )
        );
    }

    /**
     * non-nullable array of strings that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfStrSuchThatSpec(s ->
                                                    requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(s,
                                                                            ARRAY_CONDITION
                                                    )),
                                            false
        );
    }

    /**
     * non-nullable array, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each value of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec array(final Predicate<JsValue> predicate) {
        return new JsArrayOfTestedValueSpec(s ->
                                                    requireNonNull(predicate).test(s) ?
                                                    Optional.empty() :
                                                    Optional.of(new JsError(s,
                                                                            VALUE_CONDITION
                                                    )),
                                            false
        );
    }


    /**
     * non-nullable array of long numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each long number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate) {
        return new JsArrayOfTestedLongSpec(s ->
                                                   requireNonNull(predicate).test(s) ?
                                                   Optional.empty() :
                                                   Optional.of(new JsError(JsLong.of(s),
                                                                           LONG_CONDITION
                                                               )
                                                   ),
                                           false
        );
    }

    /**
     * non-nullable array of booleans that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate
    ) {
        return new JsArrayOfBoolSuchThatSpec(s ->
                                                     requireNonNull(predicate).test(s) ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(s,
                                                                             ARRAY_CONDITION
                                                                 )
                                                     ),
                                             false
        );
    }

    /**
     * non-nullable long number that satisfies the given predicate
     *
     * @param predicate the predicate the long is tested on
     * @return a spec
     */
    public static JsSpec longInteger(final LongPredicate predicate) {
        return new JsLongSuchThatSpec(false,
                                      s ->
                                              requireNonNull(predicate).test(s) ?
                                              Optional.empty() :
                                              Optional.of(new JsError(JsLong.of(s),
                                                                      LONG_CONDITION
                                                          )
                                              )
        );
    }

    /**
     * non-nullable decimal number that satisfies the given predicate
     *
     * @param predicate the predicate the decimal is tested on
     * @return a spec
     */
    public static JsSpec decimal(final Predicate<BigDecimal> predicate) {
        return new JsDecimalSuchThatSpec(
                false,
                s ->
                        requireNonNull(predicate).test(s) ?
                        Optional.empty() :
                        Optional.of(new JsError(JsBigDec.of(s),
                                                DECIMAL_CONDITION
                                    )
                        )
        );
    }

    /**
     * non-nullable integral number that satisfies the given predicate
     *
     * @param predicate the predicate the integral number is tested on
     * @return a spec
     */
    public static JsSpec integral(final Predicate<BigInteger> predicate) {
        return new JsIntegralSuchThatSpec(false,
                                          s ->
                                                  requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(JsBigInt.of(s),
                                                                          INTEGRAL_CONDITION
                                                              )
                                                  )
        );
    }

    /**
     * non-nullable array of strings, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each string of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate) {

        return new JsArrayOfTestedStrSpec(s ->
                                                  requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(JsStr.of(s),
                                                                          STRING_CONDITION
                                                              )
                                                  ),
                                          false
        );
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
        return new AnySuchThatSpec(v ->
                                           requireNonNull(predicate).test(v) ?
                                           Optional.empty() :
                                           Optional.of(new JsError(v,
                                                                   VALUE_CONDITION
                                                       )
                                           )
        );
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec binary(final Predicate<byte[]> predicate) {
        return new JsBinarySuchThatSpec(false,
                                        s ->
                                                requireNonNull(predicate).test(s) ?
                                                Optional.empty() :
                                                Optional.of(new JsError(JsBinary.of(s),
                                                                        BINARY_CONDITION
                                                            )
                                                )
        );
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec instant(final Predicate<Instant> predicate) {
        return new JsInstantSuchThatSpec(false,
                                         s ->
                                                 requireNonNull(predicate).test(s) ?
                                                 Optional.empty() :
                                                 Optional.of(new JsError(JsInstant.of(s),
                                                                         INSTANT_CONDITION
                                                             )
                                                 )
        );
    }

    /**
     * non-nullable json object that satisfies the given predicate
     *
     * @param predicate the predicate the json object is tested on
     * @return a spec
     */
    public static JsSpec obj(final Predicate<JsObj> predicate) {
        return new JsObjSuchThatSpec(false,
                                     s ->
                                             requireNonNull(predicate).test(s) ?
                                             Optional.empty() :
                                             Optional.of(new JsError(s,
                                                                     OBJ_CONDITION
                                                         )
                                             )
        );
    }

    /**
     * non-nullable array of long numbers that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfLongSuchThatSpec(s ->
                                                     requireNonNull(predicate).test(s) ?
                                                     Optional.empty() :
                                                     Optional.of(new JsError(s,
                                                                             ARRAY_CONDITION
                                                                 )
                                                     ),
                                             false
        );
    }

    /**
     * non-nullable array that satisfies the given predicate
     *
     * @param predicate the predicate the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate) {
        return new JsArraySuchThatSpec(s ->
                                               requireNonNull(predicate).test(s) ?
                                               Optional.empty() :
                                               Optional.of(new JsError(s,
                                                                       ARRAY_CONDITION
                                                           )
                                               ),
                                       false
        );

    }

    /**
     * non-nullable array of integer numbers, where each element of the array satisfies the given predicate
     *
     * @param predicate the predicate each integer number of the array is tested on
     * @return an array spec
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate) {
        return new JsArrayOfTestedIntSpec(s ->
                                                  requireNonNull(predicate).test(s) ?
                                                  Optional.empty() :
                                                  Optional.of(new JsError(JsInt.of(s),
                                                                          INT_CONDITION
                                                              )
                                                  ),
                                          false
        );

    }

    /**
     * returns a tuple spec. Each nth-element of the tuple is specified by the nth given spec
     *
     * @param spec   the spec of the first element
     * @param others the rest of specs
     * @return a spec
     */
    public static JsTupleSpec tuple(JsSpec spec,
                                    JsSpec... others
    ) {
        return JsTupleSpec.of(spec,
                              others);
    }


    public static <O extends JsValue> JsSpec oneOf(final List<O> cons) {
        return any(cons::contains);
    }
}
