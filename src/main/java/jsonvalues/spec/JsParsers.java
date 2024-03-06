package jsonvalues.spec;


import static jsonvalues.spec.JsReaders.READERS;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

/**
 * set of factory methods to create parsers from specs. Internal class that will be hidden when migrating json-values to
 * java 9 and modules
 */
final class JsParsers {

  static final JsParsers INSTANCE = new JsParsers();
  private final BiFunction<DslJsReader, JsError, JsParserException> newParseException;

  private JsParsers() {
    newParseException = (reader, error) ->
        JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(error),
                                   reader.getPositionInStream()
                                  );
  }

  JsParser ofArrayOfObjSuchThat(Function<JsArray, JsError> p,
                                boolean nullable
                               ) {
    return getParser(READERS.arrayOfObjReader,
                     p,
                     nullable
                    );
  }

  private JsParser getParser(JsArrayReader parser,
                             Function<JsArray, JsError> p,
                             boolean nullable
                            ) {
    return nullable ?
           reader -> parser.nullOrArraySuchThat(reader,
                                                p
                                               ) :
           reader -> parser.arraySuchThat(reader,
                                          p
                                         );
  }

  JsParser ofArrayOfObjEachSuchThat(Function<JsObj, JsError> p,
                                    boolean nullable,
                                    ArraySchemaConstraints arrayConstraints
                                   ) {
    return nullable ?
           reader -> READERS.arrayOfObjReader.nullOrArrayEachSuchThat(reader,
                                                                      p,
                                                                      arrayConstraints
                                                                     ) :
           reader -> READERS.arrayOfObjReader.arrayEachSuchThat(reader,
                                                                p,
                                                                arrayConstraints
                                                               );
  }


  JsParser ofArrayOfObj(boolean nullable,
                        ArraySchemaConstraints arrayConstraints
                       ) {
    return getParser(READERS.arrayOfObjReader,
                     nullable,
                     arrayConstraints
                    );
  }

  private JsParser getParser(JsArrayReader parser,
                             boolean nullable,
                             ArraySchemaConstraints arrayConstraints
                            ) {
    return nullable ?
           reader -> parser.nullOrArray(reader,
                                        arrayConstraints
                                       ) :
           reader -> parser.array(reader,
                                  arrayConstraints
                                 );
  }


  JsParser ofObjSuchThat(final Function<JsObj, JsError> predicate,
                         final boolean nullable
                        ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.objReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsObj());
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );
      };
    } else {
      return reader ->
      {
        JsObj value = READERS.objReader.value(reader);
        JsError result = predicate.apply(value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );
      };
    }
  }


  JsParser ofTuple(List<JsParser> parser,
                   boolean nullable
                  ) {
    return nullable ?
           reader -> new JsTupleReader(parser).nullOrArray(reader) :
           reader -> new JsTupleReader(parser).array(reader);
  }

  JsParser ofArrayOfSpec(JsParser parser,
                         boolean nullable,
                         ArraySchemaConstraints arrayConstraints
                        ) {
    return getParser(new JsArrayOfSpecReader(parser),
                     nullable,
                     arrayConstraints);
  }

  JsParser ofMapOfSpec(JsParser parser,
                       boolean nullable
                      ) {
    return reader ->
    {

      JsMapOfSpecReader mapReader = new JsMapOfSpecReader(parser);

      return nullable ?
             mapReader.nullOrValue(reader) :
             mapReader.value(reader);

    };
  }


  JsParser ofObjSpec(List<String> required,
                     Map<String, JsParser> keyDeserializers,
                     Predicate<JsObj> predicate,
                     boolean nullable,
                     boolean strict,
                     MetaData metaData
                    ) {
    return reader ->
    {
      if (required.isEmpty()) {
        JsObjSpecReader objSpecReader =
            new JsObjSpecReader(strict,
                                keyDeserializers,
                                predicate,
                                metaData
            );
        return nullable ?
               objSpecReader.nullOrValue(reader) :
               objSpecReader.value(reader);
      }
      JsObjSpecWithRequiredKeysReader parser =
          new JsObjSpecWithRequiredKeysReader(required,
                                              keyDeserializers,
                                              strict,
                                              predicate,
                                              metaData
          );
      return nullable ?
             parser.nullOrValue(reader) :
             parser.value(reader);

    };
  }

  JsParser ofArrayOfValueSuchThat(Function<JsArray, JsError> p,
                                  boolean nullable
                                 ) {
    return getParser(READERS.arrayOfValueReader,
                     p,
                     nullable
                    );
  }

  JsParser ofObj(boolean nullable) {
    return getParser(READERS.objReader,
                     nullable
                    );
  }

  private JsParser getParser(AbstractReader reader,
                             boolean nullable
                            ) {
    return nullable ?
           reader::nullOrValue :
           reader::value;
  }

  JsParser ofArrayOfValue(boolean nullable,
                          ArraySchemaConstraints arrayConstraints
                         ) {
    return getParser(READERS.arrayOfValueReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfValueEachSuchThat(Function<JsValue, JsError> p,
                                      boolean nullable,
                                      ArraySchemaConstraints arrayConstraints
                                     ) {
    return nullable ?
           reader -> READERS.arrayOfValueReader.nullOrArrayEachSuchThat(reader,
                                                                        p,
                                                                        arrayConstraints
                                                                       ) :
           reader -> READERS.arrayOfValueReader.arrayEachSuchThat(reader,
                                                                  p,
                                                                  arrayConstraints
                                                                 );
  }

  JsParser ofValue() {
    return getParser(READERS.valueReader,
                     true
                    );
  }

  JsParser ofConstant(JsValue cons) {

    return reader ->
    {

      JsValue value = READERS.valueReader.nullOrValue(reader);
      if (value == JsNull.NULL) {
        if (cons.isNull()) {
          return value;
        }
        throw newParseException.apply(reader,
                                      new JsError(value,
                                                  ERROR_CODE.NULL_NOT_EXPECTED)
                                     );
      }
      if (value.equals(cons)) {
        return value;
      }
      throw newParseException.apply(reader,
                                    new JsError(value,
                                                ERROR_CODE.CONSTANT_CONDITION)
                                   );

    };


  }

  JsParser ofValueSuchThat(Function<JsValue, JsError> predicate) {
    return reader ->
    {
      JsValue value = READERS.valueReader.nullOrValue(reader);
      if (value == JsNull.NULL) {
        return value;
      }
      JsError result = predicate.apply(value);
      if (result == null) {
        return value;
      }
      throw newParseException.apply(reader,
                                    result
                                   );
    };
  }

  JsParser ofBool(boolean nullable) {
    return getParser(READERS.boolReader,
                     nullable
                    );
  }

  JsParser ofArrayOfBool(boolean nullable,
                         ArraySchemaConstraints arrayConstraints
                        ) {

    return nullable ?
           reader -> READERS.arrayOfBoolReader.nullOrArray(reader,
                                                           arrayConstraints
                                                          ) :
           reader -> READERS.arrayOfBoolReader.array(reader,
                                                     arrayConstraints
                                                    );
  }

  JsParser ofArrayOfBoolSuchThat(Function<JsArray, JsError> p,
                                 boolean nullable
                                ) {
    return getParser(READERS.arrayOfBoolReader,
                     p,
                     nullable
                    );

  }

  JsParser ofArrayOfStrEachSuchThat(Function<String, JsError> p,
                                    boolean nullable,
                                    ArraySchemaConstraints arrayConstraints
                                   ) {
    return nullable ?
           reader ->
               READERS.arrayOfStringReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   arrayConstraints
                                                                  ) :
           reader ->
               READERS.arrayOfStringReader.arrayEachSuchThat(reader,
                                                             p,
                                                             arrayConstraints
                                                            );
  }

  JsParser ofArrayOfStrSuchThat(Function<JsArray, JsError> p,
                                boolean nullable
                               ) {
    return getParser(READERS.arrayOfStringReader,
                     p,
                     nullable
                    );
  }

  JsParser ofArrayOfStr(boolean nullable,
                        ArraySchemaConstraints arrayConstraints,
                        final StrConstraints schema) {
    return schema != null ?
           reader -> ofArrayOfStrEachSuchThat(s -> {
                                                validateStr(schema,
                                                            s,
                                                            reader
                                                           );
                                                return null;
                                              },
                                              nullable,
                                              arrayConstraints
                                             ).parse(reader) :
           getParser(READERS.arrayOfStringReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofStr(boolean nullable,
                 final StrConstraints constraints) {
    return nullable ?
           reader -> {
             JsValue value = READERS.strReader.nullOrValue(reader);
             if (value == JsNull.NULL) {
               return value;
             }
             JsStr jsStr = value.toJsStr();
             if (constraints != null) {
               validateStr(constraints,
                           jsStr.value,
                           reader
                          );
             }
             return jsStr;
           } :
           reader -> {
             JsStr jsStr = READERS.strReader.value(reader);
             if (constraints != null) {
               validateStr(constraints,
                           jsStr.value,
                           reader
                          );
             }
             return jsStr;
           };

  }

  private void validateStr(final StrConstraints constraints,
                           final String str,
                           final DslJsReader reader) {
    if (constraints.minLength > 0
        && str.length() < constraints.minLength) {
      throw JsParserException.reasonAt(ParserErrors.STR_LENGTH_LOWER_THAN_MINIMUM,
                                       reader.getPositionInStream()
                                      );
    }

    if (constraints.maxLength < Integer.MAX_VALUE
        && str.length() > constraints.maxLength) {
      throw JsParserException.reasonAt(ParserErrors.STR_LENGTH_GREATER_THAN_MAXIMUM,
                                       reader.getPositionInStream()
                                      );
    }
    if (constraints.pattern != null
        && !constraints.pattern.matcher(str)
                               .matches()) {
      throw JsParserException.reasonAt(ParserErrors.STR_STRING_DOES_NOT_MATCH_PATTERN,
                                       reader.getPositionInStream()
                                      );
    }

  }

  JsParser ofStrSuchThat(Function<String, JsError> predicate,
                         boolean nullable
                        ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.strReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsStr().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );
      };
    } else {
      return reader ->
      {
        JsStr value = READERS.strReader.value(reader);

        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        } else {
          throw newParseException.apply(reader,
                                        result
                                       );
        }
      };
    }
  }

  JsParser ofArrayOfIntegralSuchThat(Function<JsArray, JsError> p,
                                     boolean nullable
                                    ) {
    return getParser(READERS.arrayOfBigIntReader,
                     p,
                     nullable
                    );
  }

  JsParser ofArrayOfIntegral(boolean nullable,
                             ArraySchemaConstraints arrayConstraints,
                             BigIntSchemaConstraints schema
                            ) {
    return schema != null ?
           reader -> ofArrayOfIntegralEachSuchThat(i -> {
                                                     validateBigInteger(schema,
                                                                        i,
                                                                        reader
                                                                       );
                                                     return null;
                                                   },
                                                   nullable,
                                                   arrayConstraints
                                                  ).parse(reader) :
           getParser(READERS.arrayOfBigIntReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, JsError> p,
                                         boolean nullable,
                                         ArraySchemaConstraints arrayConstraints
                                        ) {
    return nullable ?
           reader ->
               READERS.arrayOfBigIntReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   arrayConstraints
                                                                  ) :
           reader ->
               READERS.arrayOfBigIntReader.arrayEachSuchThat(reader,
                                                             p,
                                                             arrayConstraints
                                                            );
  }

  JsParser ofArrayOfDoubleEachSuchThat(DoubleFunction<JsError> p,
                                       boolean nullable,
                                       ArraySchemaConstraints arrayConstraints
                                      ) {
    return nullable ?
           reader ->
               READERS.arrayOfDoubleReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   arrayConstraints
                                                                  ) :
           reader ->
               READERS.arrayOfDoubleReader.arrayEachSuchThat(reader,
                                                             p,
                                                             arrayConstraints
                                                            );
  }

  JsParser ofIntegral(boolean nullable,
                      final BigIntSchemaConstraints schema) {
    return schema != null ?
           reader -> ofIntegralSuchThat(i -> {
                                          validateBigInteger(schema,
                                                             i,
                                                             reader
                                                            );
                                          return null;
                                        },
                                        nullable
                                       ).parse(reader) :
           getParser(READERS.jsBigIntReader,
                     nullable
                    );
  }

  JsParser ofIntegralSuchThat(Function<BigInteger, JsError> predicate,
                              boolean nullable
                             ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.jsBigIntReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsBigInt().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );
      };
    } else {
      return reader ->
      {
        JsBigInt integral = READERS.jsBigIntReader.value(reader);
        JsError result = predicate.apply(integral.value);
        if (result == null) {
          return integral;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofArrayOfDecimal(boolean nullable,
                            ArraySchemaConstraints arrayConstraints
                           ) {
    return getParser(READERS.arrayOfDecimalReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, JsError> p,
                                        boolean nullable,
                                        ArraySchemaConstraints arrayConstraints
                                       ) {
    return nullable ?
           reader ->
               READERS.arrayOfDecimalReader.nullOrArrayEachSuchThat(reader,
                                                                    p,
                                                                    arrayConstraints
                                                                   ) :
           reader ->
               READERS.arrayOfDecimalReader.arrayEachSuchThat(reader,
                                                              p,
                                                              arrayConstraints
                                                             );
  }

  JsParser ofArrayOfDecimalSuchThat(Function<JsArray, JsError> p,
                                    boolean nullable
                                   ) {
    return getParser(READERS.arrayOfDecimalReader,
                     p,
                     nullable
                    );

  }

  JsParser ofArrayOfDoubleSuchThat(Function<JsArray, JsError> p,
                                   boolean nullable
                                  ) {
    return getParser(READERS.arrayOfDoubleReader,
                     p,
                     nullable
                    );

  }

  JsParser ofArrayOfLong(boolean nullable,
                         ArraySchemaConstraints arrayConstraints
                        ) {
    return getParser(READERS.arrayOfLongReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfLongEachSuchThat(LongFunction<JsError> p,
                                     boolean nullable,
                                     ArraySchemaConstraints arrayConstraints
                                    ) {
    return nullable ?
           reader ->
               READERS.arrayOfLongReader.nullOrArrayEachSuchThat(reader,
                                                                 p,
                                                                 arrayConstraints
                                                                ) :
           reader -> READERS.arrayOfLongReader.arrayEachSuchThat(reader,
                                                                 p,
                                                                 arrayConstraints
                                                                );
  }

  JsParser ofArrayOfLongSuchThat(Function<JsArray, JsError> p,
                                 boolean nullable
                                ) {
    return getParser(READERS.arrayOfLongReader,
                     p,
                     nullable
                    );
  }

  JsParser ofDecimal(boolean nullable,
                     final DecimalSchemaConstraints schema) {
    return schema != null ?
           reader -> ofDecimalSuchThat(d -> {
                                         validateDecimal(schema,
                                                         d,
                                                         reader
                                                        );
                                         return null;
                                       },
                                       nullable
                                      ).parse(reader) :
           getParser(READERS.decimalReader,
                     nullable
                    );
  }

  JsParser ofDecimalSuchThat(Function<BigDecimal, JsError> predicate,
                             boolean nullable
                            ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.decimalReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsBigDec().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );

      };
    } else {
      return reader ->
      {
        JsBigDec decimal = READERS.decimalReader.value(reader);
        JsError result = predicate.apply(decimal.value);
        if (result == null) {
          return decimal;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofMapOfLong(boolean nullable,
                       final LongSchemaConstraints schema) {
    return schema != null ?
           dslJsReader ->
               READERS.mapOfLongReader.eachEntrySuchThat(dslJsReader,
                                                         e -> validateLong(schema,
                                                                           e.toJsLong().value,
                                                                           dslJsReader),
                                                         nullable) :
           getParser(READERS.mapOfLongReader,
                     nullable
                    );
  }

  private void validateLong(final LongSchemaConstraints schema,
                            final long value,
                            final DslJsReader dslJsReader) {
    if (schema.minimum() > value) {
      throw JsParserException.reasonAt(ParserErrors.LONG_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
    if (schema.maximum() < value) {
      throw JsParserException.reasonAt(ParserErrors.LONG_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }

  }

  private void validateBigInteger(final BigIntSchemaConstraints schema,
                                  final BigInteger value,
                                  final DslJsReader dslJsReader) {
    if (schema.minimum() != null && schema.minimum()
                                          .compareTo(value) > 0) {
      throw JsParserException.reasonAt(ParserErrors.BIGINT_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
    if (schema.maximum() != null && schema.maximum()
                                          .compareTo(value) < 0) {
      throw JsParserException.reasonAt(ParserErrors.BIGINT_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
  }

  private void validateInteger(final IntegerSchemaConstraints schema,
                               final int value,
                               final DslJsReader dslJsReader) {
    if (schema.minimum() > value) {
      throw JsParserException.reasonAt(ParserErrors.INT_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
    if (schema.maximum() < value) {
      throw JsParserException.reasonAt(ParserErrors.INT_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }


  }

  private void validateInstant(final InstantSchemaConstraints schema,
                               final Instant value,
                               final DslJsReader dslJsReader) {
    if (schema.minimum()
              .isAfter(value)) {
      throw JsParserException.reasonAt(ParserErrors.INSTANT_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
    if (schema.maximum()
              .isBefore(value)) {
      throw JsParserException.reasonAt(ParserErrors.INSTANT_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }


  }

  private void validateDecimal(final DecimalSchemaConstraints schema,
                               final BigDecimal value,
                               final DslJsReader dslJsReader) {
    if (schema.minimum() != null && schema.minimum()
                                          .compareTo(value) > 0) {
      throw JsParserException.reasonAt(ParserErrors.DECIMAL_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream());
    }
    if (schema.maximum() != null && schema.maximum()
                                          .compareTo(value) < 0) {
      throw JsParserException.reasonAt(ParserErrors.DECIMAL_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream());
    }
  }

  private void validateDouble(final DoubleSchemaConstraints schema,
                              final double value,
                              final DslJsReader dslJsReader) {
    if (schema.minimum() > value) {
      throw JsParserException.reasonAt(ParserErrors.DOUBLE_LOWER_THAN_MINIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }
    if (schema.maximum() < value) {
      throw JsParserException.reasonAt(ParserErrors.DOUBLE_GREATER_THAN_MAXIMUM,
                                       dslJsReader.getPositionInStream()
                                      );
    }

  }

  JsParser ofMapOfDouble(boolean nullable,
                         final DoubleSchemaConstraints schema) {
    return
        schema != null ?
        dslJsReader ->
            READERS.mapOfDoubleReader.eachEntrySuchThat(dslJsReader,
                                                        e -> validateDouble(schema,
                                                                            e.toJsDouble().value,
                                                                            dslJsReader),
                                                        nullable) :
        getParser(READERS.mapOfDoubleReader,
                  nullable
                 );
  }

  JsParser ofMapOfString(final boolean nullable,
                         final StrConstraints schema) {
    return schema != null
           ? dslJsReader ->
               READERS.mapOfStringReader.eachEntrySuchThat(dslJsReader,
                                                           e -> validateStr(schema,
                                                                            e.toJsStr().value,
                                                                            dslJsReader),
                                                           nullable) :
           getParser(READERS.mapOfStringReader,
                     nullable);

  }

  JsParser ofMapOfBool(boolean nullable) {
    return getParser(READERS.mapOfBoolReader,
                     nullable
                    );
  }

  JsParser ofMapOfInt(final boolean nullable,
                      final IntegerSchemaConstraints schema) {

    return
        schema != null ?
        dslJsReader ->
            READERS.mapOfIntegerReader.eachEntrySuchThat(dslJsReader,
                                                         e -> validateInteger(schema,
                                                                              e.toJsInt().value,
                                                                              dslJsReader),
                                                         nullable) :
        getParser(READERS.mapOfIntegerReader,
                  nullable
                 );
  }

  JsParser ofMapOfInstant(boolean nullable,
                          final InstantSchemaConstraints valuesConstraints) {
    return valuesConstraints != null ?
           dslJsReader ->
               READERS.mapOfInstantReader.eachEntrySuchThat(dslJsReader,
                                                            e -> validateInstant(valuesConstraints,
                                                                                 e.toJsInstant().value,
                                                                                 dslJsReader),
                                                            nullable) :
           getParser(READERS.mapOfInstantReader,
                     nullable
                    );
  }

  JsParser ofMapOfDecimal(final boolean nullable,
                          final DecimalSchemaConstraints schema) {
    return schema != null ?
           dslJsReader ->
               READERS.mapOfDecimalReader.eachEntrySuchThat(dslJsReader,
                                                            e -> validateDecimal(schema,
                                                                                 e.toJsBigDec().value,
                                                                                 dslJsReader),
                                                            nullable) :
           getParser(READERS.mapOfDecimalReader,
                     nullable
                    );
  }

  JsParser ofMapOfBinary(boolean nullable) {
    return getParser(READERS.mapOfBinaryReader,
                     nullable
                    );
  }

  JsParser ofMapOfBigInt(boolean nullable,
                         final BigIntSchemaConstraints schema) {
    return schema != null ?
           dslJsReader ->
               READERS.mapOfBigIntegerReader.eachEntrySuchThat(dslJsReader,
                                                               e -> validateBigInteger(schema,
                                                                                       e.toJsBigInt().value,
                                                                                       dslJsReader),
                                                               nullable) :
           getParser(READERS.mapOfBigIntegerReader,
                     nullable
                    );
  }

  JsParser ofLong(boolean nullable,
                  final LongSchemaConstraints schema) {
    return schema != null ?
           reader -> ofLongSuchThat(l -> {
                                      validateLong(schema,
                                                   l,
                                                   reader
                                                  );
                                      return null;
                                    },
                                    nullable
                                   ).parse(reader) :
           getParser(READERS.longReader,
                     nullable
                    );
  }

  JsParser ofDouble(boolean nullable,
                    final DoubleSchemaConstraints schema) {
    return schema != null ?
           reader -> ofDoubleSuchThat(d -> {
                                        validateDouble(schema,
                                                       d,
                                                       reader
                                                      );
                                        return null;
                                      },
                                      nullable
                                     ).parse(reader) :
           getParser(READERS.doubleReader,
                     nullable
                    );
  }

  JsParser ofLongSuchThat(LongFunction<JsError> predicate,
                          boolean nullable
                         ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.longReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        } else {
          JsError optErr = predicate.apply(value.toJsLong().value);
          if (optErr == null) {
            return value;
          }
          throw newParseException.apply(reader,
                                        optErr
                                       );

        }
      };
    } else {
      return reader ->
      {
        JsLong value = READERS.longReader.value(reader);
        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofDoubleSuchThat(DoubleFunction<JsError> predicate,
                            boolean nullable
                           ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.doubleReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        } else {
          JsError optErr = predicate.apply(value.toJsDouble().value);
          if (optErr == null) {
            return value;
          }
          throw newParseException.apply(reader,
                                        optErr
                                       );

        }
      };
    } else {
      return reader ->
      {
        JsDouble value = READERS.doubleReader.value(reader);
        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofArrayOfInt(boolean nullable,
                        ArraySchemaConstraints arrayConstraints
                       ) {
    return getParser(READERS.arrayOfIntReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfDouble(boolean nullable,
                           ArraySchemaConstraints arrayConstraints
                          ) {
    return getParser(READERS.arrayOfDoubleReader,
                     nullable,
                     arrayConstraints
                    );
  }

  JsParser ofArrayOfIntSuchThat(Function<JsArray, JsError> p,
                                boolean nullable
                               ) {
    return getParser(READERS.arrayOfIntReader,
                     p,
                     nullable
                    );
  }

  JsParser ofArrayOfIntEachSuchThat(IntFunction<JsError> p,
                                    boolean nullable,
                                    ArraySchemaConstraints arrayConstraints
                                   ) {

    return nullable ?
           reader -> READERS.arrayOfIntReader.nullOrArrayEachSuchThat(reader,
                                                                      p,
                                                                      arrayConstraints
                                                                     ) :
           reader -> READERS.arrayOfIntReader.arrayEachSuchThat(reader,
                                                                p,
                                                                arrayConstraints
                                                               );
  }

  JsParser ofBinary(boolean nullable) {
    return getParser(READERS.binaryReader,
                     nullable
                    );
  }

  JsParser ofFixedBinary(int size,
                         boolean nullable) {
    return getParser(new JsFixedBinaryReader(size),
                     nullable
                    );
  }

  JsParser ofBinarySuchThat(Function<byte[], JsError> predicate,
                            boolean nullable
                           ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.binaryReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsBinary().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );

      };
    } else {
      return reader ->
      {
        JsBinary value = READERS.binaryReader.value(reader);
        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofInt(boolean nullable,
                 final IntegerSchemaConstraints schema) {
    return schema != null ?
           reader -> ofIntSuchThat(i -> {
                                     validateInteger(schema,
                                                     i,
                                                     reader
                                                    );
                                     return null;
                                   },
                                   nullable
                                  ).parse(reader) :
           getParser(READERS.intReader,
                     nullable
                    );
  }

  JsParser ofIntSuchThat(IntFunction<JsError> predicate,
                         boolean nullable
                        ) {

    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.intReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsInt().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );

      };
    } else {
      return reader ->
      {
        JsInt value = READERS.intReader.value(reader);
        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }

  JsParser ofInstant(boolean nullable,
                     final InstantSchemaConstraints schema
                    ) {
    return schema != null ?
           reader -> ofInstantSuchThat(i -> {
                                         validateInstant(schema,
                                                         i,
                                                         reader
                                                        );
                                         return null;
                                       },
                                       nullable
                                      ).parse(reader) :
           getParser(READERS.instantReader,
                     nullable
                    );
  }

  JsParser ofInstantSuchThat(Function<Instant, JsError> predicate,
                             boolean nullable
                            ) {
    if (nullable) {
      return reader ->
      {
        JsValue value = READERS.instantReader.nullOrValue(reader);
        if (value == JsNull.NULL) {
          return value;
        }
        JsError opErr = predicate.apply(value.toJsInstant().value);
        if (opErr == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      opErr
                                     );

      };
    } else {
      return reader ->
      {
        JsInstant value = READERS.instantReader.value(reader);
        JsError result = predicate.apply(value.value);
        if (result == null) {
          return value;
        }
        throw newParseException.apply(reader,
                                      result
                                     );

      };
    }
  }
}
