package jsonvalues.spec;


import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.ARRAY_CONDITION;
import static jsonvalues.spec.ERROR_CODE.BINARY_CONDITION;
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
import java.util.Objects;
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
   * non-nullable array of decimal numbers
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfDec() {
    return arrayOfDec;
  }

  /**
   * non-nullable array of integral numbers
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfBigInt() {
    return arrayOfBigInt;
  }


  /**
   * non-nullable array of double numbers
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble() {
    return arrayOfDouble;
  }


  /**
   * non-nullable array of objects
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfObj() {
    return arrayOfObj;
  }

  /**
   * non-nullable array of booleans
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfBool() {
    return arrayOfBool;
  }

  /**
   * non-nullable instant spec
   *
   * @return a spec
   */
  public static JsSpec instant() {
    return instant;
  }

  /**
   * non-nullable array of long numbers
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfLong() {
    return arrayOfLong;
  }

  /**
   * non-nullable array of long numbers. Each element of the array must satisfy the given schema.
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfLong(LongSchema schema) {

    return new JsArrayOfLong(false,
                             schema.build());
  }

  /**
   * non-nullable array of long numbers. Each element of the array must satisfy the given schema. The array must satisfy
   * the given array schema (min and max item, unique elements etc).
   *
   * @param schema      the schema that each element of the array must satisfy
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfLong(LongSchema schema,
                                        ArraySchema arraySchema) {

    return new JsArrayOfLong(false,
                             arraySchema.build(),
                             schema.build());
  }

  /**
   * non-nullable array of integer numbers
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfInt() {
    return arrayOfInt;
  }

  /**
   * non-nullable array of strings
   *
   * @return a spec
   */
  public static JsArraySpec arrayOfStr() {
    return arrayOfStr;
  }

  /**
   * non-nullable binary
   *
   * @return a spec
   * @see JsBinary
   */
  public static JsSpec binary() {
    return binary;
  }

  /**
   * non-nullable fixed-length binary
   *
   * @param length the length of the binary
   * @return a spec
   * @see JsBinary
   */
  public static JsSpec fixedBinary(int length) {
    return new JsFixedBinary(length);
  }

  /**
   * non-nullable big integer number
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
   * non-nullable double number
   *
   * @return a spec
   */
  public static JsSpec doubleNumber() {
    return doubleNumber;
  }

  /**
   * non-nullable double number. Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
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


  /**
   * non-nullable integer number Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsSpec integer(IntegerSchema schema) {
    return new JsIntSpec(false,
                         requireNonNull(schema).build());
  }

  /**
   * non-nullable json object
   *
   * @return a spec
   */
  public static JsSpec obj() {
    return obj;
  }

  /**
   * non-nullable json array
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
   * non-nullable string Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsSpec str(StrSchema schema) {
    return new JsStrSpec(false,
                         schema.build());
  }


  /**
   * non-nullable array of integer numbers. The array must satisfy the given schema (min and max item, unique elements
   * etc).
   *
   * @param schema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfInt(ArraySchema schema) {

    return new JsArrayOfInt(false,
                            schema.build());
  }

  /**
   * non-nullable array of integer numbers. Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfInt(IntegerSchema schema) {

    return new JsArrayOfInt(false,
                            schema.build());
  }

  /**
   * non-nullable array of integer numbers. Each element of the array must satisfy the given schema. The array must
   * satisfy the given schema (min and max item, unique elements etc).
   *
   * @param schema      the schema that each element of the array must satisfy
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfInt(IntegerSchema schema,
                                       ArraySchema arraySchema) {

    return new JsArrayOfInt(false,
                            arraySchema.build(),
                            schema.build());
  }


  /**
   * non-nullable array of double numbers. The array must satisfy the given schema (min and max item, unique elements
   * etc).
   *
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble(ArraySchema arraySchema) {

    return new JsArrayOfDouble(false,
                               arraySchema.build());
  }


  /**
   * non-nullable array of double numbers. Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble(DoubleSchema schema) {

    return new JsArrayOfDouble(false,
                               schema.build(),
                               null);
  }

  /**
   * non-nullable array of double numbers. Each element of the array must satisfy the given schema. The array must
   * satisfy the given schema (min and max item, unique elements etc).
   *
   * @param schema      the schema that each element of the array must satisfy
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble(DoubleSchema schema,
                                          ArraySchema arraySchema) {

    return new JsArrayOfDouble(false,
                               schema.build(),
                               arraySchema.build());
  }


  /**
   * non-nullable array of decimal numbers. Each element of the array must satisfy the given predicate. The array must
   * satisfy the given schema (min and max item, unique elements etc).
   *
   * @param predicate   The predicate that each number in the array must satisfy.
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDouble(DoublePredicate predicate,
                                          ArraySchema arraySchema
                                         ) {
    Objects.requireNonNull(predicate);
    Objects.requireNonNull(arraySchema);
    return new JsArrayOfTestedDouble(s -> predicate.test(s) ?
                                          null :
                                          new JsError(JsDouble.of(s),
                                                      DOUBLE_CONDITION),
                                     false,
                                     arraySchema.build());
  }

  /**
   * non-nullable array of big integer numbers. The array must satisfy the given schema (min and max item, unique
   * elements etc).
   *
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfBigInt(final ArraySchema arraySchema
                                         ) {
    Objects.requireNonNull(arraySchema);
    return new JsArrayOfBigInt(false,
                               arraySchema
                                   .build(),
                               null);
  }

  /**
   * non-nullable array of big integer numbers. Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfBigInt(BigIntSchema schema
                                         ) {
    Objects.requireNonNull(schema);
    return new JsArrayOfBigInt(false,
                               null,
                               schema.build());
  }

  /**
   * non-nullable array of big integer numbers. Each element of the array must satisfy the given schema. The array must
   * satisfy the given schema (min and max item, unique elements etc).
   *
   * @param schema      the schema that each element of the array must satisfy
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfBigInt(final BigIntSchema schema,
                                          final ArraySchema arraySchema
                                         ) {
    Objects.requireNonNull(arraySchema);
    Objects.requireNonNull(schema);
    return new JsArrayOfBigInt(false,
                               arraySchema
                                   .build(),
                               schema.build());
  }


  /**
   * non-nullable array of objects. The array must satisfy the given schema (min and max item, unique elements etc).
   *
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfObj(final ArraySchema arraySchema
                                      ) {
    Objects.requireNonNull(arraySchema);
    return new JsArrayOfObj(false,
                            arraySchema.build());
  }

  /**
   * non-nullable array of big decimal numbers. The array must satisfy the given schema (min and max item, unique
   * elements etc).
   *
   * @param arraySchema the schema that the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDec(ArraySchema arraySchema
                                      ) {
    Objects.requireNonNull(arraySchema);
    return new JsArrayOfDecimal(false,
                                arraySchema.build());
  }

  /**
   * non-nullable array of big decimal numbers. Each element of the array must satisfy the given schema.
   *
   * @param schema the schema that each element of the array must satisfy
   * @return a spec
   */
  public static JsArraySpec arrayOfDec(final DecimalSchema schema
                                      ) {
    Objects.requireNonNull(schema);
    return new JsArrayOfDecimal(false,
                                schema.build(),
                                null);
  }

  public static JsArraySpec arrayOfDec(final DecimalSchema schema,
                                       final ArraySchema arraySchema
                                      ) {
    Objects.requireNonNull(schema);
    Objects.requireNonNull(arraySchema);
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
  public static JsArraySpec arrayOfBool(final ArraySchema schema
                                       ) {
    Objects.requireNonNull(schema);
    return new JsArrayOfBool(false,
                             schema.build());
  }

  /**
   * Returns a specification for an array of strings with a specified minimum and maximum length.
   *
   * @param arraySchema The schema defining constraints for the array.
   * @return A specification for an array of strings with the specified length constraints.
   */
  public static JsArraySpec arrayOfStr(final ArraySchema arraySchema
                                      ) {
    Objects.requireNonNull(arraySchema);
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
    Objects.requireNonNull(arraySchema);
    Objects.requireNonNull(elemSchema);
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
    Objects.requireNonNull(schema);
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
    return new Cons(value);
  }

  public static JsSpec cons(final String name,
                            final JsValue value) {
    return new Cons(value,
                    name);
  }

  /**
   * Returns a specification for a non-nullable string, where the string satisfies the given predicate.
   *
   * @param predicate The predicate that the string must satisfy.
   * @return A specification for strings based on the specified predicate.
   */
  public static JsSpec str(final Predicate<String> predicate) {
    requireNonNull(predicate);
    return new JsStrSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfIntSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedDecimal(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedDouble(s -> predicate.test(s) ?
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
                                       final ArraySchema arraySchema
                                      ) {
    requireNonNull(predicate);
    requireNonNull(arraySchema);
    return new JsArrayOfTestedDecimal(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfDecimalSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedBigInt(s -> predicate.test(s) ?
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
                                          final ArraySchema arraySchema
                                         ) {
    requireNonNull(predicate);
    requireNonNull(arraySchema);
    return new JsArrayOfTestedBigInt(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfBigIntSuchThat(s -> predicate.test(s) ?
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
                                       final ArraySchema arraySchema
                                      ) {
    requireNonNull(predicate);
    requireNonNull(arraySchema);
    return new JsArrayOfTestedObj(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfObjSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsIntSuchThat(s -> predicate.test(s) ?
                                  null :
                                  new JsError(JsInt.of(s),
                                              INT_CONDITION),
                             false);
  }

  /**
   * Returns a specification for a non-nullable array of strings that satisfies the given predicate.
   *
   * @param predicate The predicate that the array must satisfy.
   * @return An array specification for strings based on the specified predicate.
   */
  public static JsArraySpec arrayOfStrSuchThat(final Predicate<JsArray> predicate) {
    requireNonNull(predicate);
    return new JsArrayOfStrSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedValue(s ->
                                        predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedLong(s -> predicate.test(s) ?
                                        null :
                                        new JsError(JsLong.of(s),
                                                    LONG_CONDITION),
                                   false);
  }

  /**
   * Returns a specification for a non-nullable array of long numbers, where each element of the array satisfies the
   * given predicate. The array must satisfy the given schema (min and max item, unique elements etc).
   *
   * @param predicate The predicate that each long number in the array must satisfy.
   * @param schema    The schema defining constraints for the array.
   * @return An array specification for long numbers based on the specified predicate.
   */
  public static JsArraySpec arrayOfLong(final LongPredicate predicate,
                                        final ArraySchema schema
                                       ) {
    requireNonNull(predicate);
    requireNonNull(schema);
    return new JsArrayOfTestedLong(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfBoolSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfDoubleSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsLongSuchThat(s -> predicate.test(s) ?
                                   null :
                                   new JsError(JsLong.of(s),
                                               LONG_CONDITION),
                              false);
  }

  /**
   * Returns a specification for a non-nullable long number that satisfies the given schema.
   *
   * @param schema The schema the long number must satisfy.
   * @return A specification for long numbers based on the specified schema.
   */
  public static JsSpec longInteger(final LongSchema schema) {
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
    requireNonNull(predicate);
    return new JsDoubleSuchThat(s -> predicate.test(s) ?
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

  /**
   * Returns a specification for a non-nullable decimal number that satisfies the given schema
   *
   * @param schema The schema defining constraints for the decimal number.
   * @return A specification for decimal numbers based on the specified schema.
   */
  public static JsSpec decimal(final DecimalSchema schema) {
    return new JsDecimalSpec(false,
                             requireNonNull(schema).build());
  }

  /**
   * Returns a specification for a non-nullable big integer number that satisfies the given predicate.
   *
   * @param predicate The predicate that the integral number must satisfy.
   * @return A specification for integral numbers based on the specified predicate.
   */
  public static JsSpec bigInteger(final Predicate<BigInteger> predicate) {
    requireNonNull(predicate);
    return new JsBigIntSuchThat(s -> predicate.test(s) ?
                                     null :
                                     new JsError(JsBigInt.of(s),
                                                 INTEGRAL_CONDITION),
                                false);
  }

  /**
   * Returns a specification for a non-nullable big integer number that satisfies the given schema.
   *
   * @param schema The schema defining constraints for the big integer number.
   * @return A specification for integral numbers based on the specified schema.
   */
  public static JsSpec bigInteger(final BigIntSchema schema) {
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
    requireNonNull(predicate);
    return new JsArrayOfTestedStr(s -> predicate.test(s) ?
                                       null :
                                       new JsError(JsStr.of(s),
                                                   STRING_CONDITION
                                       ),
                                  false);
  }

  /**
   * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given
   * schema.
   *
   * @param schema The schema that each element of the array must satisfy.
   * @return An array specification for strings based on the specified predicate and size constraints.
   */
  public static JsArraySpec arrayOfStr(final StrSchema schema) {

    return new JsArrayOfStr(false,
                            null,
                            requireNonNull(schema).build());
  }


  /**
   * Returns a specification for a non-nullable array of strings, where each element of the array satisfies the given
   * predicate. The array must satisfy the given schema (min and max item, unique elements etc).
   *
   * @param predicate   The predicate that each string in the array must satisfy.
   * @param arraySchema The schema defining constraints for the array.
   * @return An array specification for strings based on the specified predicate and size constraints.
   */
  public static JsArraySpec arrayOfStr(final Predicate<String> predicate,
                                       final ArraySchema arraySchema
                                      ) {
    requireNonNull(predicate);
    requireNonNull(arraySchema);
    return new JsArrayOfTestedStr(s -> predicate.test(s) ?
                                       null :
                                       new JsError(JsStr.of(s),
                                                   STRING_CONDITION),
                                  false,
                                  arraySchema.build());
  }


  /**
   * Returns a specification that accepts any JSON value for which the given predicate evaluates to true.
   *
   * @param predicate The predicate that determines whether a JSON value is accepted.
   * @return A specification that validates JSON values based on the specified predicate.
   */
  public static JsSpec any(final Predicate<JsValue> predicate) {
    requireNonNull(predicate);
    return new AnySuchThat(v -> predicate.test(v) ?
                                null :
                                new JsError(v,
                                            VALUE_CONDITION));
  }

  /**
   * Returns a specification for a non-nullable binary data that satisfies the given predicate.
   *
   * @param predicate The predicate the JSON binary data is tested on.
   * @return A specification that enforces the specified condition for JSON binary data.
   * @see JsBinary
   */
  public static JsSpec binary(final Predicate<byte[]> predicate) {
    requireNonNull(predicate);
    return new JsBinarySuchThat(s -> predicate.test(s) ?
                                     null :
                                     new JsError(JsBinary.of(s),
                                                 BINARY_CONDITION),
                                false);
  }

  /**
   * Returns a specification for a non-nullable instant that satisfies the given predicate.
   *
   * @param predicate The predicate the JSON instant is tested on.
   * @return A specification that enforces the specified condition for JSON instants.
   */
  public static JsSpec instant(final Predicate<Instant> predicate) {
    requireNonNull(predicate);
    return new JsInstantSuchThat(s -> predicate.test(s) ?
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
   * Returns a specification for a non-nullable object that satisfies the given predicate.
   *
   * @param predicate The predicate the object is tested on.
   * @return A specification that enforces the specified condition for JSON objects.
   */
  public static JsSpec obj(final Predicate<JsObj> predicate) {
    requireNonNull(predicate);
    return new JsObjSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfLongSuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArraySuchThat(s -> predicate.test(s) ?
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
    requireNonNull(predicate);
    return new JsArrayOfTestedInt(s -> predicate.test(s) ?
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
                                       final ArraySchema arraySchema
                                      ) {
    requireNonNull(predicate);
    requireNonNull(arraySchema);
    return new JsArrayOfTestedInt(s -> predicate.test(s) ?
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
  public static JsArraySpec tuple(final JsSpec spec,
                                  final JsSpec... others
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
    requireNonNull(cons);
    return any(o -> cons.contains(o));
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
  public static <O extends JsValue> JsSpec oneValOf(final O elem,
                                                    final O... others) {
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
  public static JsSpec oneSpecOf(final JsSpec elem,
                                 final JsSpec... others) {
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
  public static JsSpec oneStringOf(final String elem,
                                   final String... others) {
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

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key
   * is a big integer that satisfies the given schema.
   *
   * @param schema the schema that each element of the map must satisfy
   * @return A JSON specification for objects with big integer values.
   */
  public static JsSpec mapOfBigInteger(final BigIntSchema schema) {
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

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a double
   * that satisfies the given schema.
   * @param doubleSchema the schema that each element of the map must satisfy
   * @return A JSON specification for objects with double values.
   */
  public static JsSpec mapOfDouble(final DoubleSchema doubleSchema) {
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

  /**
   * Returns a specification that validates that the JSON is an object, and the value of each key is a decimal number
   * that satisfies the given schema.
   * @param decimalSchema the schema that each element of the map must satisfy
   * @return A JSON specification for objects with decimal number values.
   */
  public static JsSpec mapOfDecimal(final DecimalSchema decimalSchema) {
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
   * Returns a specification that validates that the JSON is an object, and the value of each key is an instant
   * that satifies the given schema.
   * @param instantSchema the schema that each element of the map must satisfy
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
  public static JsSpec mapOfObj(final JsObjSpec spec) {
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
  public static JsSpec mapOfSpec(final JsSpec spec) {

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
  public static JsArraySpec arrayOfSpec(final JsSpec spec) {
    return new JsArrayOfSpec(false,
                             requireNonNull(spec));
  }

  /**
   * Returns a specification that validates that the JSON is an array, and the value of each element is a value that
   * conforms the given spec.
   *
   * @param spec        the spec of the elements
   * @param arraySchema the schema that the array must satisfy
   * @return A JSON specification for arrays.
   */
  public static JsArraySpec arrayOfSpec(final JsSpec spec,
                                        final ArraySchema arraySchema) {
    return new JsArrayOfSpec(false,
                             requireNonNull(spec),
                             requireNonNull(arraySchema).build());
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
