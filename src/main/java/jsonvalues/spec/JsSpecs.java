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

public class JsSpecs {

    private JsSpecs(){}
    /**
     spec that is conformed by any value
     */
    public static final JsSpec any = new AnySpec(true);
    /**
     non-nullable string
     */
    public static final JsSpec str = new JsStrSpec(true,
                                             false
    );
    /**
     non-nullable number
     */
    public static final JsSpec number = new JsNumberSpec(true,
                                                   false
    );
    /**
     non-nullable boolean
     */
    public static final JsSpec bool = new JsBooleanSpec(true,
                                                  false
    );
    /**
     non-nullable decimal number
     */
    public static final JsSpec decimal = new JsDecimalSpec(true,
                                                     false
    );
    /**
     non-nullable integral number
     */
    public static final JsSpec integral = new JsIntegralSpec(true,
                                                       false
    );
    /**
     non-nullable long number
     */
    public static final JsSpec longInteger = new JsLongSpec(true,
                                                      false
    );
    /**
     non-nullable integer number
     */
    public static final JsSpec integer = new JsIntSpec(true,
                                                 false
    );
    /**
     true constant spec
     */
    public static final JsSpec TRUE = new JsTrueConstantSpec(true,
                                                       false
    );
    /**
     false constant spec
     */
    public static final JsSpec FALSE = new JsFalseConstantSpec(true,
                                                         false
    );
    /**
     non-nullable json object spec
     */
    public static final JsSpec obj = new IsJsObjSpec(true,
                                               false
    );
    /**
     non-nullable array spec
     */
    public static final JsArraySpec array = new JsArrayOfValueSpec(true,
                                                             false
    );

    /**
     non-nullable array spec
     */
    public static final JsSpec binary = new JsBinarySpec(true,
                                                   false
    );

    /**
     non-nullable array spec
     */
    public static final JsSpec instant = new JsInstantSpec(true,
                                                     false
    );
    /**
     non-nullable array of long numbers spec
     */
    public static final JsArraySpec arrayOfLong = new JsArrayOfLongSpec(true,
                                                                  false
    );
    /**
     non-nullable array of integer numbers spec
     */
    public static final JsArraySpec arrayOfInt = new JsArrayOfIntSpec(true,
                                                                false
    );
    /**
     non-nullable array of strings spec
     */
    public static final JsArraySpec arrayOfStr = new JsArrayOfStrSpec(true,
                                                                false
    );
    /**
     non-nullable array of booleans spec
     */
    public static final JsArraySpec arrayOfBool = new JsArrayOfBoolSpec(true,
                                                                  false
    );
    /**
     non-nullable array of decimal numbers spec
     */
    public static final JsArraySpec arrayOfDec = new JsArrayOfDecimalSpec(true,
                                                                    false
    );
    /**
     non-nullable array of numbers spec
     */
    public static final JsArraySpec arrayOfNumber = new JsArrayOfNumberSpec(true,
                                                                      false
    );
    /**
     non-nullable array of integral numbers spec
     */
    public static final JsArraySpec arrayOfIntegral = new JsArrayOfIntegralSpec(true,
                                                                          false
    );
    /**
     non-nullable array of objects spec
     */
    public static final JsArraySpec arrayOfObj = new JsArrayOfObjSpec(true,
                                                                false
    );

    /**
     A required and none nullable spec that specifies an array of objects that conform the given spec

     @param spec the given spec that every object in the array has to conform
     @return a spec
     */
    public static JsArraySpec arrayOf(final JsObjSpec spec) {
        return new JsArrayOfJsObjSpec(false,
                                      true,
                                      requireNonNull(spec)
        );
    }

    /**
     a required and non nullable spec that specifies a constant
     @param value the constant
     @return a spec
     */
    public static JsSpec cons(JsValue value){
        return new AnySuchThatSpec(true,
                                   v ->
                                   {
                                       if (requireNonNull(value).equals(v)) return Optional.empty();
                                       return Optional.of(new Error(v,
                                                                    CONSTANT_CONDITION
                                                          )
                                                         );
                                   }
        );
    }

    /**
     non-nullable string that satisfies the given predicate

     @param predicate the predicate
     @return a JsSpec
     */
    public static JsSpec str(final Predicate<String> predicate) {
        return new JsStrSuchThatSpec(true,
                                     false,
                                     s ->
                                     {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(JsStr.of(s),
                                                                      STRING_CONDITION
                                         ));
                                     }

        );
    }

    /**
     non-nullable number that satisfies the given predicate

     @param predicate the predicate
     @return a JsSpec
     */
    public static JsSpec number(final Predicate<JsNumber> predicate) {
        return new JsNumberSuchThatSpec(true,
                                        false,
                                        s ->
                                        {
                                            if (requireNonNull(predicate).test(s)) return Optional.empty();
                                            return Optional.of(new Error(s,
                                                                         ERROR_CODE.NUMBER_CONDITION
                                            ));
                                        }
        );
    }

    /**
     non-nullable array of numbers that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntSuchThatSpec(s ->
                                            {
                                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                return Optional.of(new Error(s,
                                                                             ARRAY_CONDITION
                                                ));
                                            },
                                            true,
                                            false
        );

    }


    /**
     non-nullable array of decimal numbers, where each element of the array satisfies
     the given predicate

     @param predicate the predicate each decimal number of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate) {

        return new JsArrayOfTestedDecimalSpec(s ->
                                              {
                                                  if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                  return Optional.of(new Error(JsBigDec.of(s),
                                                                               DECIMAL_CONDITION
                                                  ));
                                              },
                                              true,
                                              false
        );
    }

    /**
     non-nullable array of decimal numbers that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate
                                                ) {
        return new JsArrayOfDecimalSuchThatSpec(s ->
                                                {
                                                    if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                    return Optional.of(new Error(s,
                                                                                 ARRAY_CONDITION
                                                    ));
                                                },
                                                true,
                                                false
        );
    }

    /**
     non-nullable array of integral numbers, where each element of the array satisfies
     the given predicate

     @param predicate the predicate each integral number of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfIntegral(final Predicate<BigInteger> predicate
                                             ) {
        return new JsArrayOfTestedIntegralSpec(s ->
                                               {
                                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                   return Optional.of(new Error(JsBigInt.of(s),
                                                                                INTEGRAL_CONDITION
                                                   ));
                                               },
                                               true,
                                               false
        );
    }

    /**
     non-nullable array of integral numbers that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfIntegralSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfIntegralSuchThatSpec(s ->
                                                 {
                                                     if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                     return Optional.of(new Error(s,
                                                                                  ARRAY_CONDITION
                                                                        )
                                                                       );
                                                 },
                                                 true,
                                                 false
        );
    }

    /**
     non-nullable array of numbers, where each element of the array satisfies
     the given predicate

     @param predicate the predicate each number of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfNumber(final Predicate<JsNumber> predicate) {
        return new JsArrayOfTestedNumberSpec(s ->
                                             {
                                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                 return Optional.of(new Error(s,
                                                                              ERROR_CODE.NUMBER_CONDITION
                                                                    )
                                                                   );
                                             },
                                             true,
                                             false
        );
    }

    /**
     non-nullable array of numbers that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfNumberSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfNumberSuchThatSpec(s ->
                                               {
                                                   if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                   return Optional.of(new Error(s,
                                                                                ARRAY_CONDITION
                                                                      )
                                                                     );
                                               },
                                               true,
                                               false
        );
    }

    /**
     non-nullable array of objects, where each element of the array satisfies
     the given predicate

     @param predicate the predicate each object of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate
                                        ) {
        return new JsArrayOfTestedObjSpec(s ->
                                          {
                                              if (requireNonNull(predicate).test(s)) return Optional.empty();
                                              return Optional.of(new Error(s,
                                                                           OBJ_CONDITION
                                                                 )
                                                                );
                                          },
                                          true,
                                          false
        );

    }

    /**
     non-nullable array of objects that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfObjSuchThatSpec(s ->
                                            {
                                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                return Optional.of(new Error(s,
                                                                             ARRAY_CONDITION
                                                                   )
                                                                  );
                                            },
                                            true,
                                            false
        );

    }


    /**
     non-nullable integer number that satisfies the given predicate

     @param predicate the predicate the integer is tested on
     @return a spec
     */
    public static JsSpec integer(final IntPredicate predicate) {
        return new JsIntSuchThatSpec(true,
                                     false,
                                     s ->
                                     {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(JsInt.of(s),
                                                                      INT_CONDITION
                                                            )
                                                           );
                                     }
        );
    }

    /**
     non-nullable array of strings that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfStrSuchThatSpec(s ->
                                            {
                                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                return Optional.of(new Error(s,
                                                                             ARRAY_CONDITION
                                                ));
                                            },
                                            true,
                                            false
        );
    }

    /**
     non-nullable array, where each element of the array satisfies the given predicate

     @param predicate the predicate each value of the array is tested on
     @return an array spec
     */
    public static JsArraySpec array(final Predicate<JsValue> predicate) {
        return new JsArrayOfTestedValueSpec(s ->
                                            {
                                                if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                return Optional.of(new Error(s,
                                                                             VALUE_CONDITION
                                                ));
                                            },
                                            true,
                                            false
        );
    }


    /**
     non-nullable array of long numbers, where each element of the array satisfies the given predicate

     @param predicate the predicate each long number of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfLong(final LongPredicate predicate) {
        return new JsArrayOfTestedLongSpec(s ->
                                           {
                                               if (requireNonNull(predicate).test(s)) return Optional.empty();
                                               return Optional.of(new Error(JsLong.of(s),
                                                                            LONG_CONDITION
                                                                  )
                                                                 );
                                           },
                                           true,
                                           false
        );
    }

    /**
     non-nullable array of booleans that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate
                                                 ) {
        return new JsArrayOfBoolSuchThatSpec(s ->
                                             {
                                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                 return Optional.of(new Error(s,
                                                                              ARRAY_CONDITION
                                                                    )
                                                                   );
                                             },
                                             true,
                                             false
        );
    }

    /**
     non-nullable long number that satisfies the given predicate

     @param predicate the predicate the long is tested on
     @return a spec
     */
    public static JsSpec longInteger(final LongPredicate predicate) {
        return new JsLongSuchThatSpec(true,
                                      false,
                                      s ->
                                      {
                                          if (requireNonNull(predicate).test(s)) return Optional.empty();
                                          return Optional.of(new Error(JsLong.of(s),
                                                                       LONG_CONDITION
                                                             )
                                                            );
                                      }
        );
    }

    /**
     non-nullable decimal number that satisfies the given predicate

     @param predicate the predicate the decimal is tested on
     @return a spec
     */
    public static JsSpec decimal(final Predicate<BigDecimal> predicate) {
        return new JsDecimalSuchThatSpec(true,
                                         false,
                                         s ->
                                         {
                                             if (requireNonNull(predicate).test(s)) return Optional.empty();
                                             return Optional.of(new Error(JsBigDec.of(s),
                                                                          DECIMAL_CONDITION
                                                                )
                                                               );
                                         }
        );
    }

    /**
     non-nullable integral number that satisfies the given predicate

     @param predicate the predicate the integral number is tested on
     @return a spec
     */
    public static JsSpec integral(final Predicate<BigInteger> predicate) {
        return new JsIntegralSuchThatSpec(true,
                                          false,
                                          s ->
                                          {
                                              if (requireNonNull(predicate).test(s)) return Optional.empty();
                                              return Optional.of(new Error(JsBigInt.of(s),
                                                                           INTEGRAL_CONDITION
                                                                 )
                                                                );
                                          }
        );
    }

    /**
     non-nullable array of strings, where each element of the array satisfies the given predicate

     @param predicate the predicate each string of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfStr(final Predicate<String> predicate) {

        return new JsArrayOfTestedStrSpec(s ->
                                          {
                                              if (requireNonNull(predicate).test(s)) return Optional.empty();
                                              return Optional.of(new Error(JsStr.of(s),
                                                                           STRING_CONDITION
                                                                 )
                                                                );
                                          },
                                          true,
                                          false
        );
    }

    /**
     returns a spec that conforms any value that is evaluated to true on the predicate.
     When the type is not specified by the spec, positive numbers are parsed as Long by default,
     which has to be taken into account in order to define any condition.

     @param predicate the predicate
     @return a spec
     */
    public static JsSpec any(final Predicate<JsValue> predicate) {
        return new AnySuchThatSpec(true,
                                   v ->
                                   {
                                       if (requireNonNull(predicate).test(v)) return Optional.empty();
                                       return Optional.of(new Error(v,
                                                                    VALUE_CONDITION
                                                          )
                                                         );
                                   }
        );
    }

    /**
     non-nullable json object that satisfies the given predicate

     @param predicate the predicate the json object is tested on
     @return a spec
     */
    public static JsSpec binary(final Predicate<byte[]> predicate) {
        return new JsBinarySuchThatSpec(true,
                                        false,
                                        s ->
                                        {
                                            if (requireNonNull(predicate).test(s))
                                                return Optional.empty();
                                            return Optional.of(new Error(JsBinary.of(s),
                                                                         BINARY_CONDITION
                                                               )
                                                              );
                                        }
        );
    }

    /**
     non-nullable json object that satisfies the given predicate

     @param predicate the predicate the json object is tested on
     @return a spec
     */
    public static JsSpec instant(final Predicate<Instant> predicate) {
        return new JsInstantSuchThatSpec(true,
                                         false,
                                         s ->
                                         {
                                             if (requireNonNull(predicate).test(s))
                                                 return Optional.empty();
                                             return Optional.of(new Error(JsInstant.of(s),
                                                                          INSTANT_CONDITION
                                                                )
                                                               );
                                         }
        );
    }

    /**
     non-nullable json object that satisfies the given predicate

     @param predicate the predicate the json object is tested on
     @return a spec
     */
    public static JsSpec obj(final Predicate<JsObj> predicate) {
        return new JsObjSuchThatSpec(true,
                                     false,
                                     s ->
                                     {
                                         if (requireNonNull(predicate).test(s)) return Optional.empty();
                                         return Optional.of(new Error(s,
                                                                      OBJ_CONDITION
                                                            )
                                                           );
                                     }
        );
    }

    /**
     non-nullable array of long numbers that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate) {
        return new JsArrayOfLongSuchThatSpec(s ->
                                             {
                                                 if (requireNonNull(predicate).test(s)) return Optional.empty();
                                                 return Optional.of(new Error(s,
                                                                              ARRAY_CONDITION
                                                                    )
                                                                   );
                                             },
                                             true,
                                             false
        );
    }

    /**
     non-nullable array that satisfies the given predicate

     @param predicate the predicate the array is tested on
     @return an array spec
     */
    public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate) {
        return new JsArraySuchThatSpec(s ->
                                       {
                                           if (requireNonNull(predicate).test(s)) return Optional.empty();
                                           return Optional.of(new Error(s,
                                                                        ARRAY_CONDITION
                                                              )
                                                             );
                                       },
                                       true,
                                       false
        );

    }

    /**
     non-nullable array of integer numbers, where each element of the array satisfies the given predicate

     @param predicate the predicate each integer number of the array is tested on
     @return an array spec
     */
    public static JsArraySpec arrayOfInt(final IntPredicate predicate) {
        return new JsArrayOfTestedIntSpec(s ->
                                          {
                                              if (requireNonNull(predicate).test(s)) return Optional.empty();
                                              return Optional.of(new Error(JsInt.of(s),
                                                                           INT_CONDITION
                                                                 )
                                                                );
                                          },
                                          true,
                                          false
        );

    }

    /**
     returns a tuple spec. Each nth-element of the tuple is specified by the nth given spec

     @param spec   the spec of the first element
     @param others the rest of specs
     @return a spec
     */
    public static JsTupleSpec tuple(JsSpec spec,
                                    JsSpec... others
                                   ) {
        return JsTupleSpec.of(spec,
                              others
                             );
    }


    public static <O extends JsValue> JsSpec oneOf(final List<O> cons) {
        return any(cons::contains);
    }
}
