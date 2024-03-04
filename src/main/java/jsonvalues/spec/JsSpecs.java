package jsonvalues.spec;


import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.ARRAY_CONDITION;
import static jsonvalues.spec.ERROR_CODE.BINARY_CONDITION;
import static jsonvalues.spec.ERROR_CODE.CONSTANT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.DECIMAL_CONDITION;
import static jsonvalues.spec.ERROR_CODE.DOUBLE_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INSTANT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INTEGRAL_CONDITION;
import static jsonvalues.spec.ERROR_CODE.INT_CONDITION;
import static jsonvalues.spec.ERROR_CODE.LONG_CONDITION;
import static jsonvalues.spec.ERROR_CODE.OBJ_CONDITION;
import static jsonvalues.spec.ERROR_CODE.STRING_CONDITION;
import static jsonvalues.spec.ERROR_CODE.VALUE_CONDITION;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

/**
 * The `JsSpecs` class provides a collection of static factory methods for creating JSON specifications (JsSpec). These
 * specifications are used to define validation rules and constraints for JSON data structures to ensure that they
 * conform to expected formats and patterns.
 * <p>
 * JSON specifications are essential for parsing and validating JSON data in applications, ensuring that the data
 * adheres to predefined rules before processing or storing it.
 * <p>
 * This class simplifies the creation of these specifications for various JSON data types, including numbers, strings,
 * arrays, objects, and more.
 * <p>
 * It provides factory methods for creating specifications for different JSON types, including numbers, strings, arrays,
 * and objects. Additionally, custom validation predicates can be specified to enforce specific constraints on JSON
 * data.
 * <p>
 * JSON specifications created with this class are immutable and can be reused for validating multiple JSON data
 * instances.
 *
 * @see JsSpec
 * @see JsError
 */
public final class JsSpecs {

  private static final JsArraySpec arrayOfDec = new JsArrayOfDecimal(false);
  private static final JsArraySpec arrayOfDouble = new JsArrayOfDouble(false);
  private static final JsArraySpec arrayOfBigInt = new JsArrayOfBigInt(false);
  private static final JsArraySpec arrayOfObj = new JsArrayOfObj(false);
  private static final JsArraySpec arrayOfBool = new JsArrayOfBool(false);
  private static final JsSpec instant = new JsInstantSpec(false,
                                                          null);
  private static final JsArraySpec arrayOfLong = new JsArrayOfLong(false);
  private static final JsArraySpec arrayOfInt = new JsArrayOfInt(false);
  private static final JsArraySpec arrayOfStr = new JsArrayOfStr(false,
                                                                 null,
                                                                 null);
  private static final JsSpec binary = new JsBinarySpec(false);
  private static final JsSpec bigInteger = new JsBigIntSpec(false);
  private static final JsSpec longInteger = new JsLongSpec(false);
  private static final JsSpec doubleNumber = new JsDoubleSpec(false);
  private static final JsSpec bool = new JsBooleanSpec(false);
  private static final JsSpec decimal = new JsDecimalSpec(false);
  private static final JsSpec integer = new JsIntSpec(false);
  private static final JsSpec obj = new IsJsObj(false);
  private static final JsSpec any = new AnySpec();
  private static final JsSpec str = new JsStrSpec(false);
  private static final JsArraySpec array = new JsArrayOfValue(false);

  private static final JsSpec mapOfLongSpec = new JsMapOfLong(false);

  private static final JsSpec mapOfStrSpec = new JsMapOfStr(false,
                                                            null);


  private static final JsSpec mapOfIntSpec = new JsMapOfInt(false);

  private static final JsSpec mapOfBoolSpec = new JsMapOfBool(false);

  private static final JsSpec mapOfDecimalSpec = new JsMapOfDec(false);
  private static final JsSpec mapOfDoubleSpec = new JsMapOfDouble(false);
  private static final JsSpec mapOfBinarySpec = new JsMapOfBinary(false);

  private static final JsSpec mapOfBigIntegerSpec = new JsMapOfBigInt(false);

  private static final JsSpec mapOfInstantSpec = new JsMapOfInstant(false);


  private JsSpecs() {
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
   * non-nullable array of double numbers spec
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble() {
    return arrayOfDouble;
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

  public static JsArraySpec arrayOfLong(LongSchema schema) {

    return new JsArrayOfLong(false,
                             schema.build());
  }

  public static JsArraySpec arrayOfLong(LongSchema schema,
                                        ArraySchema arraySchema) {

    return new JsArrayOfLong(false,
                             arraySchema.build(),
                             schema.build());
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

  public static JsSpec fixedBinary(int size) {
    return new JsFixedBinary(size);
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
   * non-nullable long number
   *
   * @return a spec
   */
  public static JsSpec doubleNumber() {
    return doubleNumber;
  }

  public static JsSpec doubleNumber(DoubleSchema schema) {
    return new JsDoubleSpec(false,
                            requireNonNull(schema).build());
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


  public static JsSpec integer(IntegerSchema schema) {
    return new JsIntSpec(false,
                         requireNonNull(schema).build());
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


  public static JsSpec str(StrSchema schema) {
    return new JsStrSpec(false,
                         schema.build());
  }


  /**
   * Returns a specification for an array of integers with a specified minimum and maximum length.
   *
   * @param schema The schema defining constraints for the array.
   * @return A specification for an array of integers with the specified length constraints.
   */
  public static JsArraySpec arrayOfInt(ArraySchema schema) {

    return new JsArrayOfInt(false,
                            schema.build());
  }

  public static JsArraySpec arrayOfInt(IntegerSchema schema) {

    return new JsArrayOfInt(false,
                            schema.build());
  }

  public static JsArraySpec arrayOfInt(IntegerSchema schema,
                                       ArraySchema arraySchema) {

    return new JsArrayOfInt(false,
                            arraySchema.build(),
                            schema.build());
  }


  /**
   * Returns a specification for an array of doubles with a specified minimum and maximum length.
   *
   * @param arraySchema The schema defining constraints for the array.
   * @return A specification for an array of doubles with the specified length constraints.
   */
  public static JsArraySpec arrayOfDouble(ArraySchema arraySchema) {

    return new JsArrayOfDouble(false,
                               arraySchema.build());
  }


  public static JsArraySpec arrayOfDouble(DoubleSchema schema) {

    return new JsArrayOfDouble(false,
                               schema.build(),
                               null);
  }

  public static JsArraySpec arrayOfDouble(DoubleSchema schema,
                                          ArraySchema arraySchema) {

    return new JsArrayOfDouble(false,
                               schema.build(),
                               arraySchema.build());
  }


  public static JsArraySpec arrayOfDouble(DoublePredicate predicate,
                                          ArraySchema arraySchema
                                         ) {

    return new JsArrayOfTestedDouble(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(JsDouble.of(s),
                                                      DOUBLE_CONDITION),
                                     false,
                                     arraySchema.build());
  }

  /**
   * Returns a specification for an array of integral numbers with a specified minimum and maximum length.
   *
   * @param arraySchema The schema defining constraints for the array.
   * @return A specification for an array of integral numbers with the specified length constraints.
   */
  public static JsArraySpec arrayOfBigInt(ArraySchema arraySchema
                                         ) {

    return new JsArrayOfBigInt(false,
                               arraySchema.build(),
                               null);
  }

  public static JsArraySpec arrayOfBigInt(BigIntSchema schema
                                         ) {

    return new JsArrayOfBigInt(false,
                               null,
                               schema.build());
  }

  public static JsArraySpec arrayOfBigInt(BigIntSchema schema,
                                          ArraySchema arraySchema
                                         ) {

    return new JsArrayOfBigInt(false,
                               arraySchema.build(),
                               schema.build());
  }


  /**
   * Returns a specification for an array of objects with a specified minimum and maximum length.
   *
   * @param schema The schema defining constraints for the array.
   * @return A specification for an array of objects with the specified length constraints.
   */
  public static JsArraySpec arrayOfObj(ArraySchema schema
                                      ) {

    return new JsArrayOfObj(false,
                            schema.build());
  }

  /**
   * Returns a specification for an array of decimal numbers with a specified minimum and maximum length.
   *
   * @param schema The schema defining constraints for the array.
   * @return A specification for an array of decimal numbers with the specified length constraints.
   */
  public static JsArraySpec arrayOfDec(ArraySchema schema
                                      ) {
    return new JsArrayOfDecimal(false,
                                schema.build());
  }

  public static JsArraySpec arrayOfDec(DecimalSchema schema
                                      ) {
    return new JsArrayOfDecimal(false,
                                schema.build(),
                                null);
  }

  public static JsArraySpec arrayOfDec(DecimalSchema schema,
                                       ArraySchema arraySchema
                                      ) {
    return new JsArrayOfDecimal(false,
                                schema.build(),
                                arraySchema.build());
  }

  /**
   * Returns a specification for an array of booleans with a specified minimum and maximum length.
   *
   * @param schema The schema defining constraints for the array.
   * @return A specification for an array of booleans with the specified length constraints.
   */
  public static JsArraySpec arrayOfBool(ArraySchema schema
                                       ) {

    return new JsArrayOfBool(false,
                             schema.build());
  }

  /**
   * Returns a specification for an array of strings with a specified minimum and maximum length.
   *
   * @param arraySchema The schema defining constraints for the array.
   * @return A specification for an array of strings with the specified length constraints.
   */
  public static JsArraySpec arrayOfStr(ArraySchema arraySchema
                                      ) {

    return new JsArrayOfStr(false,
                            arraySchema.build());
  }

  /**
   * Returns a specification for an array of strings with a specified minimum and maximum length, and each element
   * conforming to the given element schema.
   *
   * @param arraySchema The schema defining constraints for the array.
   * @param elemSchema  The schema defining constraints for each element in the array.
   * @return A specification for an array of strings with the specified length constraints and element schema.
   */
  public static JsArraySpec arrayOfStr(StrSchema elemSchema,
                                       ArraySchema arraySchema
                                      ) {
    return new JsArrayOfStr(false,
                            arraySchema.build(),
                            elemSchema.build());
  }


  /**
   * Returns a specification for an array of long numbers with a specified minimum and maximum length.
   *
   * @param schema The schema defining constraints for the array.
   * @return A specification for an array of long numbers with the specified length constraints.
   */
  public static JsArraySpec arrayOfLong(ArraySchema schema
                                       ) {
    return new JsArrayOfLong(false,
                             schema.build());
  }


  /**
   * Returns a required and non-nullable specification that specifies a constant value.
   *
   * @param value The constant value.
   * @return A specification that enforces the provided constant value.
   */
  public static JsSpec cons(final JsValue value) {
    return new AnySuchThat(v -> requireNonNull(value).equals(v) ?
                                null :
                                new JsError(v,
                                            CONSTANT_CONDITION));
  }

  /**
   * Returns a specification for a non-nullable string, where the string satisfies the given predicate.
   *
   * @param predicate The predicate that the string must satisfy.
   * @return A specification for strings based on the specified predicate.
   */
  public static JsSpec str(final Predicate<String> predicate) {
    return new JsStrSuchThat(s -> requireNonNull(predicate).test(s) ?
                                  null :
                                  new JsError(JsStr.of(s),
                                              STRING_CONDITION),
                             false

    );
  }


  /**
   * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that the entire array must satisfy.
   * @return An array specification for integer numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfIntSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfIntSuchThat(s -> requireNonNull(predicate).test(s) ?
                                         null :
                                         new JsError(s,
                                                     ARRAY_CONDITION),
                                    false);

  }


  /**
   * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each decimal number in the array must satisfy.
   * @return An array specification for decimal numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate) {

    return new JsArrayOfTestedDecimal(s -> requireNonNull(predicate).test(s) ?
                                           null :
                                           new JsError(JsBigDec.of(s),
                                                       DECIMAL_CONDITION),
                                      false);
  }

  /**
   * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each decimal number in the array must satisfy.
   * @return An array specification for decimal numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfDouble(final Predicate<Double> predicate) {

    return new JsArrayOfTestedDouble(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(JsDouble.of(s),
                                                      DOUBLE_CONDITION),
                                     false);
  }

  /**
   * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies the
   * given predicate. The array must have a size within the specified range.
   *
   * @param predicate   The predicate that each decimal number in the array must satisfy.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification for decimal numbers based on the specified predicate and size range.
   */
  public static JsArraySpec arrayOfDec(final Predicate<BigDecimal> predicate,
                                       ArraySchema arraySchema
                                      ) {

    return new JsArrayOfTestedDecimal(s -> requireNonNull(predicate).test(s) ?
                                           null :
                                           new JsError(JsBigDec.of(s),
                                                       DECIMAL_CONDITION),
                                      false,
                                      arraySchema.build());
  }

  /**
   * Returns a specification for a non-nullable array of decimal numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each decimal number in the array must satisfy.
   * @return An array specification for decimal numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfDecSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfDecimalSuchThat(s -> requireNonNull(predicate).test(s) ?
                                             null :
                                             new JsError(s,
                                                         ARRAY_CONDITION),
                                        false);
  }

  /**
   * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each integral number in the array must satisfy.
   * @return An array specification for integral numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfBigInt(final Predicate<BigInteger> predicate) {
    return new JsArrayOfTestedBigInt(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(JsBigInt.of(s),
                                                      INTEGRAL_CONDITION),
                                     false);
  }

  /**
   * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies the
   * given predicate. The array must have a minimum and maximum size as specified by the parameters.
   *
   * @param predicate   The predicate that each integral number in the array must satisfy.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification for integral numbers based on the specified predicate and size constraints.
   */
  public static JsArraySpec arrayOfBigInt(final Predicate<BigInteger> predicate,
                                          ArraySchema arraySchema
                                         ) {

    return new JsArrayOfTestedBigInt(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(JsBigInt.of(s),
                                                      INTEGRAL_CONDITION),
                                     false,
                                     arraySchema.build());
  }

  /**
   * Returns a specification for a non-nullable array of integral numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each integral number in the array must satisfy.
   * @return An array specification for integral numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfBigIntSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfBigIntSuchThat(s -> requireNonNull(predicate).test(s) ?
                                            null :
                                            new JsError(s,
                                                        ARRAY_CONDITION),
                                       false);
  }


  /**
   * Returns a specification for a non-nullable array of objects, where each element of the array satisfies the given
   * predicate.
   *
   * @param predicate The predicate that each object in the array must satisfy.
   * @return An array specification for objects based on the specified predicate.
   */
  public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate) {
    return new JsArrayOfTestedObj(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(s,
                                                   OBJ_CONDITION),
                                  false);

  }

  /**
   * Returns a specification for a non-nullable array of objects, where each element of the array satisfies the given
   * predicate.
   *
   * @param predicate   The predicate that each object in the array must satisfy.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification for objects based on the specified predicate and size constraints.
   */
  public static JsArraySpec arrayOfObj(final Predicate<JsObj> predicate,
                                       ArraySchema arraySchema
                                      ) {

    return new JsArrayOfTestedObj(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(s,
                                                   OBJ_CONDITION),
                                  false,
                                  arraySchema.build());

  }

  /**
   * Returns a specification for a non-nullable array of objects that satisfies the given predicate.
   *
   * @param predicate The predicate that the array must satisfy.
   * @return An array specification for objects based on the specified predicate.
   */
  public static JsArraySpec arrayOfObjSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfObjSuchThat(s -> requireNonNull(predicate).test(s) ?
                                         null :
                                         new JsError(s,
                                                     ARRAY_CONDITION),
                                    false);

  }


  /**
   * Returns a specification for a non-nullable integer number that satisfies the given predicate.
   *
   * @param predicate The predicate that the integer must satisfy.
   * @return A specification for integer numbers based on the specified predicate.
   */
  public static JsSpec integer(final IntPredicate predicate) {
    return new JsIntSuchThat(s -> requireNonNull(predicate).test(s) ?
                                  null :
                                  new JsError(JsInt.of(s),
                                              INT_CONDITION),
                             false);
  }

  /**
   * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given
   * predicate.
   *
   * @param predicate The predicate that the array must satisfy.
   * @return An array specification for strings based on the specified predicate.
   */
  public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfStrSuchThat(s -> requireNonNull(predicate).test(s) ?
                                         null :
                                         new JsError(s,
                                                     ARRAY_CONDITION),
                                    false);
  }

  /**
   * Returns a specification for a non-nullable array, where each element of the array satisfies the given predicate.
   *
   * @param predicate The predicate that each value in the array must satisfy.
   * @return An array specification for values based on the specified predicate.
   */
  public static JsArraySpec array(final Predicate<JsValue> predicate) {
    return new JsArrayOfTestedValue(s ->
                                        requireNonNull(predicate).test(s) ?
                                        null :
                                        new JsError(s,
                                                    VALUE_CONDITION),
                                    false);
  }


  /**
   * Returns a specification for a non-nullable array of long numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each long number in the array must satisfy.
   * @return An array specification for long numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfLong(final LongPredicate predicate) {
    return new JsArrayOfTestedLong(s -> requireNonNull(predicate).test(s) ?
                                        null :
                                        new JsError(JsLong.of(s),
                                                    LONG_CONDITION),
                                   false);
  }

  /**
   * Returns a specification for a non-nullable array of long numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate that each long number in the array must satisfy.
   * @param schema    The schema defining constraints for the array.
   * @return An array specification for long numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfLong(final LongPredicate predicate,
                                        final ArraySchema schema
                                       ) {

    return new JsArrayOfTestedLong(s -> requireNonNull(predicate).test(s) ?
                                        null :
                                        new JsError(JsLong.of(s),
                                                    LONG_CONDITION),
                                   false,
                                   schema.build());
  }

  /**
   * Returns a specification for a non-nullable array of booleans that satisfies the given predicate.
   *
   * @param predicate The predicate that the array must satisfy.
   * @return An array specification for booleans based on the specified predicate.
   */
  public static JsArraySpec arrayOfBoolSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfBoolSuchThat(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(s,
                                                      ARRAY_CONDITION),
                                     false);
  }

  /**
   * Returns a specification for a non-nullable array of double that satisfies the given predicate.
   *
   * @param predicate The predicate that the array must satisfy.
   * @return An array specification for double based on the specified predicate.
   */
  public static JsArraySpec arrayOfDoubleSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfDoubleSuchThat(s -> requireNonNull(predicate).test(s) ?
                                            null :
                                            new JsError(s,
                                                        ARRAY_CONDITION),
                                       false);
  }

  /**
   * Returns a specification for a non-nullable long number that satisfies the given predicate.
   *
   * @param predicate The predicate that the long number must satisfy.
   * @return A specification for long numbers based on the specified predicate.
   */
  public static JsSpec longInteger(final LongPredicate predicate) {
    return new JsLongSuchThat(s -> requireNonNull(predicate).test(s) ?
                                   null :
                                   new JsError(JsLong.of(s),
                                               LONG_CONDITION),
                              false);
  }

  public static JsSpec longInteger(LongSchema schema) {
    return new JsLongSpec(false,
                          requireNonNull(schema).build());
  }

  /**
   * Returns a specification for a non-nullable double number that satisfies the given predicate.
   *
   * @param predicate The predicate that the double number must satisfy.
   * @return A specification for long numbers based on the specified predicate.
   */
  public static JsSpec doubleNumber(final DoublePredicate predicate) {
    return new JsDoubleSuchThat(s -> requireNonNull(predicate).test(s) ?
                                     null :
                                     new JsError(JsDouble.of(s),
                                                 LONG_CONDITION),
                                false);
  }

  /**
   * Returns a specification for a non-nullable decimal number that satisfies the given predicate.
   *
   * @param predicate The predicate that the decimal number must satisfy.
   * @return A specification for decimal numbers based on the specified predicate.
   */
  public static JsSpec decimal(final Predicate<BigDecimal> predicate) {
    return new JsDecimalSuchThat(s -> requireNonNull(predicate).test(s) ?
                                      null :
                                      new JsError(JsBigDec.of(s),
                                                  DECIMAL_CONDITION),
                                 false);
  }

  public static JsSpec decimal(DecimalSchema schema) {
    return new JsDecimalSpec(false,
                             requireNonNull(schema).build());
  }

  /**
   * Returns a specification for a non-nullable integral number that satisfies the given predicate.
   *
   * @param predicate The predicate that the integral number must satisfy.
   * @return A specification for integral numbers based on the specified predicate.
   */
  public static JsSpec bigInteger(final Predicate<BigInteger> predicate) {
    return new JsBigIntSuchThat(s -> requireNonNull(predicate).test(s) ?
                                     null :
                                     new JsError(JsBigInt.of(s),
                                                 INTEGRAL_CONDITION),
                                false);
  }

  public static JsSpec bigInteger(BigIntSchema schema) {
    return new JsBigIntSpec(false,
                            requireNonNull(schema).build());
  }

  /**
   * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given
   * predicate.
   *
   * @param predicate The predicate that each string in the array must satisfy.
   * @return An array specification for strings based on the specified predicate.
   */
  public static JsArraySpec arrayOfStr(final Predicate<String> predicate) {

    return new JsArrayOfTestedStr(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(JsStr.of(s),
                                                   STRING_CONDITION
                                       ),
                                  false);
  }

  public static JsArraySpec arrayOfStr(final StrSchema schema) {

    return new JsArrayOfStr(false,
                            null,
                            schema.build());
  }


  /**
   * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given
   * predicate.
   *
   * @param predicate   The predicate that each string in the array must satisfy.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification for strings based on the specified predicate and size constraints.
   */
  public static JsArraySpec arrayOfStr(final Predicate<String> predicate,
                                       final ArraySchema arraySchema
                                      ) {

    return new JsArrayOfTestedStr(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(JsStr.of(s),
                                                   STRING_CONDITION),
                                  false,
                                  arraySchema.build());
  }


  /**
   * Returns a specification that accepts any JSON value for which the given predicate evaluates to true. When the type
   * is not specified by the spec, positive numbers are parsed as Long by default, which has to be taken into account
   * when defining the condition.
   *
   * @param predicate The predicate that determines whether a JSON value is accepted.
   * @return A specification that validates JSON values based on the specified predicate.
   */
  public static JsSpec any(final Predicate<JsValue> predicate) {
    return new AnySuchThat(v -> requireNonNull(predicate).test(v) ?
                                null :
                                new JsError(v,
                                            VALUE_CONDITION));
  }

  /**
   * Returns a specification for a non-nullable JSON binary data that satisfies the given predicate.
   *
   * @param predicate The predicate the JSON binary data is tested on.
   * @return A specification that enforces the specified condition for JSON binary data.
   * @see JsBinary
   */
  public static JsSpec binary(final Predicate<byte[]> predicate) {
    return new JsBinarySuchThat(s -> requireNonNull(predicate).test(s) ?
                                     null :
                                     new JsError(JsBinary.of(s),
                                                 BINARY_CONDITION),
                                false);
  }

  /**
   * Returns a specification for a non-nullable JSON instant that satisfies the given predicate.
   *
   * @param predicate The predicate the JSON instant is tested on.
   * @return A specification that enforces the specified condition for JSON instants.
   */
  public static JsSpec instant(final Predicate<Instant> predicate) {
    return new JsInstantSuchThat(s -> requireNonNull(predicate).test(s) ?
                                      null :
                                      new JsError(JsInstant.of(s),
                                                  INSTANT_CONDITION),
                                 false);
  }

  /**
   * Returns a specification for a non-nullable JSON instant that satisfies the given schema.
   *
   * @param schema The schema the instant must satisfy.
   * @return A specification that enforces the specified condition for JSON long numbers.
   */
  public static JsSpec instant(final InstantSchema schema) {
    return new JsInstantSpec(false,
                             requireNonNull(schema).build());
  }

  /**
   * Returns a specification for a non-nullable JSON object that satisfies the given predicate.
   *
   * @param predicate The predicate the JSON object is tested on.
   * @return A specification that enforces the specified condition for JSON objects.
   */
  public static JsSpec obj(final Predicate<JsObj> predicate) {
    return new JsObjSuchThat(s -> requireNonNull(predicate).test(s) ?
                                  null :
                                  new JsError(s,
                                              OBJ_CONDITION),
                             false);
  }

  /**
   * Returns a specification for a non-nullable array of long numbers that satisfies the given predicate.
   *
   * @param predicate The predicate the array is tested on.
   * @return An array specification that enforces the specified condition for arrays of long numbers.
   */
  public static JsArraySpec arrayOfLongSuchThat(final Predicate<JsArray> predicate) {
    return new JsArrayOfLongSuchThat(s -> requireNonNull(predicate).test(s) ?
                                          null :
                                          new JsError(s,
                                                      ARRAY_CONDITION),
                                     false);
  }

  /**
   * Returns a specification for a non-nullable array that satisfies the given predicate.
   *
   * @param predicate The predicate the array is tested on.
   * @return An array specification that enforces the specified condition for arrays.
   */
  public static JsArraySpec arraySuchThat(final Predicate<JsArray> predicate) {
    return new JsArraySuchThat(s -> requireNonNull(predicate).test(s) ?
                                    null :
                                    new JsError(s,
                                                ARRAY_CONDITION),
                               false);

  }

  /**
   * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate The predicate each integer number of the array is tested on.
   * @return An array specification that enforces the specified condition for integer elements.
   */
  public static JsArraySpec arrayOfInt(final IntPredicate predicate) {
    return new JsArrayOfTestedInt(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(JsInt.of(s),
                                                   INT_CONDITION),
                                  false);

  }

  /**
   * Returns a specification for a non-nullable array of integer numbers, where each element of the array satisfies the
   * given predicate.
   *
   * @param predicate   The predicate each integer number of the array is tested on.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification that enforces the specified conditions.
   */
  public static JsArraySpec arrayOfInt(final IntPredicate predicate,
                                       ArraySchema arraySchema
                                      ) {

    return new JsArrayOfTestedInt(s -> requireNonNull(predicate).test(s) ?
                                       null :
                                       new JsError(JsInt.of(s),
                                                   INT_CONDITION),
                                  false,
                                  arraySchema.build());

  }

  /**
   * Returns a tuple specification where each nth element of the tuple is specified by the nth given spec.
   *
   * @param spec   The specification for the first element.
   * @param others The specifications for the rest of the elements.
   * @return A specification for a tuple that enforces the specified structure.
   */
  @SuppressWarnings("varargs")
  public static JsArraySpec tuple(JsSpec spec,
                                  JsSpec... others
                                 ) {
    return JsTuple.of(requireNonNull(spec),
                      requireNonNull(others));
  }

  /**
   * Returns a specification that validates if a JSON value is one of the given possible values.
   *
   * @param cons The list of possible JSON values.
   * @param <O>  The type of the possible JSON values (subtype of JsValue).
   * @return A specification that checks if a JSON value matches one of the provided values.
   */
  public static <O extends JsValue> JsSpec oneValOf(final List<O> cons) {
    return any(o -> requireNonNull(cons).contains(o));
  }

  /**
   * Returns a specification that validates if a JSON value is one of the given possible values.
   *
   * @param elem   The first possible JSON value.
   * @param others Additional possible JSON values.
   * @param <O>    The type of the possible JSON values (subtype of JsValue).
   * @return A specification that checks if a JSON value matches one of the provided values.
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <O extends JsValue> JsSpec oneValOf(O elem,
                                                    O... others) {
    List<O> list = new ArrayList<>();
    list.add(requireNonNull(elem));
    Collections.addAll(list,
                       requireNonNull(others));
    return oneValOf(list);
  }

  /**
   * Returns a specification that validates if a JSON value is one of the given possible specifications.
   *
   * @param specs The list of possible JSON specifications.
   * @return A specification that checks if a JSON value matches one of the provided specifications.
   */
  public static JsSpec oneSpecOf(List<? extends JsSpec> specs) {
    return new OneOf(false,
                     requireNonNull(specs));
  }


  /**
   * Returns a specification that validates if a JSON value is one of the given possible specifications.
   *
   * @param elem   The first possible JSON specification.
   * @param others Additional possible JSON specifications.
   * @return A specification that checks if a JSON value matches one of the provided specifications.
   */
  @SuppressWarnings("varargs")
  public static JsSpec oneSpecOf(JsSpec elem,
                                 JsSpec... others) {
    List<JsSpec> list = new ArrayList<>();
    list.add(requireNonNull(elem));
    Collections.addAll(list,
                       requireNonNull(others));
    return oneSpecOf(list);
  }


  /**
   * Returns a specification that validates if a JSON value is one of the given possible symbols.
   *
   * @param cons The list of possible JSON values.
   * @return A specification that checks if a JSON value matches one of the provided values.
   */
  public static JsSpec oneStringOf(final List<String> cons) {
    return JsEnum.of(requireNonNull(cons));
  }

  /**
   * Returns a specification that validates if a JSON value is one of the given possible symbols.
   *
   * @param elem   The first possible symbol value.
   * @param others Additional possible symbol values.
   * @return A specification that checks if a JSON value matches one of the provided symbol values.
   */
  @SuppressWarnings("varargs")
  public static JsSpec oneStringOf(String elem,
                                   String... others) {
    List<String> list = new ArrayList<>();
    list.add(requireNonNull(elem));
    Collections.addAll(list,
                       requireNonNull(others));
    return JsEnum.of(list);
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

  public static JsSpec mapOfBigInteger(BigIntSchema schema) {
    return new JsMapOfBigInt(false,
                             requireNonNull(schema).build());
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a binary.
   *
   * @return A JSON specification for objects with binary values.
   */
  public static JsSpec mapOfBinary() {
    return mapOfBinarySpec;
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a double.
   *
   * @return A JSON specification for objects with double values.
   */
  public static JsSpec mapOfDouble() {
    return mapOfDoubleSpec;
  }

  public static JsSpec mapOfDouble(DoubleSchema doubleSchema) {
    return new JsMapOfDouble(false,
                             requireNonNull(doubleSchema).build());
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a decimal number.
   *
   * @return A JSON specification for objects with decimal number values.
   */
  public static JsSpec mapOfDecimal() {
    return mapOfDecimalSpec;
  }

  public static JsSpec mapOfDecimal(DecimalSchema decimalSchema) {
    return new JsMapOfDec(false,
                          requireNonNull(decimalSchema).build());
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
   * Returns a specification that validates that the JSON is an object, and the value of each key is an instant.
   *
   * @return A JSON specification for objects with instant values.
   */
  public static JsSpec mapOfInstant(final InstantSchema instantSchema) {
    return new JsMapOfInstant(false,
                              requireNonNull(instantSchema).build());
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is an object.
   *
   * @param spec the spec of the values of the map
   * @return A JSON specification for objects with object values.
   */
  public static JsSpec mapOfObj(JsObjSpec spec) {
    return new JsMapOfSpec(spec);
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a string.
   *
   * @return A JSON specification for objects with string values.
   */
  public static JsSpec mapOfStr() {
    return mapOfStrSpec;
  }

  public static JsSpec mapOfStr(StrSchema schema) {
    return new JsMapOfStr(false,
                          requireNonNull(schema).build());
  }

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a value that
   * conforms the given spec.
   *
   * @param spec the spec of the values
   * @return A JSON specification for maps.
   */
  public static JsSpec mapOfSpec(JsSpec spec) {

    return new JsMapOfSpec(false,
                           requireNonNull(spec));
  }

  /**
   * Returns a specification that validates that the JSON is an array, and the value of each element is a value that
   * conforms the given spec.
   *
   * @param spec the spec of the elements
   * @return A JSON specification for arrays.
   */
  public static JsArraySpec arrayOfSpec(JsSpec spec) {
    return new JsArrayOfSpec(false,
                             requireNonNull(spec));
  }

  /**
   * Returns a specification that validates that the JSON is an array within the limits of the specified bounds, and the
   * value of each element is a value that conforms to the given spec.
   *
   * @param spec        The spec of the elements.
   * @param arraySchema The schema defining constraints for the array.
   * @return A JSON specification for arrays.
   */
  public static JsArraySpec arrayOfSpec(JsSpec spec,
                                        ArraySchema arraySchema) {
    return new JsArrayOfSpec(false,
                             requireNonNull(spec),
                             arraySchema.build());
  }

  /**
   * Returns a named spec from the cache based on the provided name. Named specs are created using the
   * {@link JsObjSpecBuilder} and are cached for efficient retrieval and use in recursive specifications.
   * <p>
   * Named specs represent reusable specifications that can be referenced in other specs, allowing for the creation of
   * recursive data structures. Example usage:
   * <pre>
   * {@code
   * var spec = JsObjSpecBuilder.withName("person")
   *                            .build(JsObjSpec.of("name", JsSpecs.str(),
   *                                                "age", JsSpecs.integer(),
   *                                                "father", JsSpecs.ofNamedSpec("person")
   *                                               )
   *                                            .withOptKeys("father")
   *                                  );
   * }
   * </pre>
   *
   * @param name The name of the named spec to retrieve from the cache.
   * @return A named spec with the specified name.
   * @see JsObjSpecBuilder
   */
  public static JsSpec ofNamedSpec(final String name) {
    return new NamedSpec(name);
  }


  /**
   * Creates and caches a named spec.
   *
   * @param name The name of the named spec.
   * @param spec The JsSpec to be associated with the named spec.
   * @return A named spec with the specified name.
   */
  public static JsSpec ofNamedSpec(final String name,
                                   final JsSpec spec) {
    //builders already cache the specs, we need to create named specs with the builder
    //to create a metadata object used by avro-spec library. It's more common to
    //use the builder because there are a lot of metadata options not provided by this method
    //(only the name). This is more commonly used by other kind of specs like oneOf(obspec1, objspec2)
    if (requireNonNull(spec) instanceof JsObjSpec objSpec) {
      var unused = JsObjSpecBuilder.withName(name)
                                   .build(objSpec);
      assert unused != null;
    } else if (spec instanceof JsEnum enumSpec) {
      var unused = JsEnumBuilder.withName(name)
                                .build(enumSpec.symbols);
      assert unused != null;
    } else if (spec instanceof JsFixedBinary fixed) {
      var unused = JsFixedBuilder.withName(name)
                                 .build(fixed.getSize());
      assert unused != null;
    } else {
      JsSpecCache.put(name,
                      spec);
    }
    return new NamedSpec(name);
  }


}
