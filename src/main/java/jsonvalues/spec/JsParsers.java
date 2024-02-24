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
                                    int min,
                                    int max
                                   ) {
    return nullable ?
           reader -> READERS.arrayOfObjReader.nullOrArrayEachSuchThat(reader,
                                                                      p,
                                                                      min,
                                                                      max
                                                                     ) :
           reader -> READERS.arrayOfObjReader.arrayEachSuchThat(reader,
                                                                p,
                                                                min,
                                                                max
                                                               );
  }


  JsParser ofArrayOfObj(boolean nullable,
                        int min,
                        int max
                       ) {
    return getParser(READERS.arrayOfObjReader,
                     nullable,
                     min,
                     max
                    );
  }

  private JsParser getParser(JsArrayReader parser,
                             boolean nullable,
                             int min,
                             int max
                            ) {
    return nullable ?
           reader -> parser.nullOrArray(reader,
                                        min,
                                        max
                                       ) :
           reader -> parser.array(reader,
                                  min,
                                  max
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
                         int min,
                         int max
                        ) {
    return getParser(new JsArrayOfSpecReader(parser),
                     nullable,
                     min,
                     max);
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
                          int min,
                          int max
                         ) {
    return getParser(READERS.arrayOfValueReader,
                     nullable,
                     min,
                     max
                    );
  }

  JsParser ofArrayOfValueEachSuchThat(Function<JsValue, JsError> p,
                                      boolean nullable,
                                      int min,
                                      int max
                                     ) {
    return nullable ?
           reader -> READERS.arrayOfValueReader.nullOrArrayEachSuchThat(reader,
                                                                        p,
                                                                        min,
                                                                        max
                                                                       ) :
           reader -> READERS.arrayOfValueReader.arrayEachSuchThat(reader,
                                                                  p,
                                                                  min,
                                                                  max
                                                                 );
  }

  JsParser ofValue() {
    return getParser(READERS.valueReader,
                     true
                    );
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
                         int min,
                         int max
                        ) {

    return nullable ?
           reader -> READERS.arrayOfBoolReader.nullOrArray(reader,
                                                           min,
                                                           max
                                                          ) :
           reader -> READERS.arrayOfBoolReader.array(reader,
                                                     min,
                                                     max
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
                                    int min,
                                    int max
                                   ) {
    return nullable ?
           reader ->
               READERS.arrayOfStringReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   min,
                                                                   max
                                                                  ) :
           reader ->
               READERS.arrayOfStringReader.arrayEachSuchThat(reader,
                                                             p,
                                                             min,
                                                             max
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
                        int min,
                        int max,
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
                                              min,
                                              max
                                             ).parse(reader) :
           getParser(READERS.arrayOfStringReader,
                     nullable,
                     min,
                     max
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
                             int min,
                             int max
                            ) {
    return getParser(READERS.arrayOfBigIntReader,
                     nullable,
                     min,
                     max
                    );
  }

  JsParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, JsError> p,
                                         boolean nullable,
                                         int min,
                                         int max
                                        ) {
    return nullable ?
           reader ->
               READERS.arrayOfBigIntReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   min,
                                                                   max
                                                                  ) :
           reader ->
               READERS.arrayOfBigIntReader.arrayEachSuchThat(reader,
                                                             p,
                                                             min,
                                                             max
                                                            );
  }

  JsParser ofArrayOfDoubleEachSuchThat(DoubleFunction<JsError> p,
                                       boolean nullable,
                                       int min,
                                       int max
                                      ) {
    return nullable ?
           reader ->
               READERS.arrayOfDoubleReader.nullOrArrayEachSuchThat(reader,
                                                                   p,
                                                                   min,
                                                                   max
                                                                  ) :
           reader ->
               READERS.arrayOfDoubleReader.arrayEachSuchThat(reader,
                                                             p,
                                                             min,
                                                             max
                                                            );
  }

  JsParser ofIntegral(boolean nullable) {
    return getParser(READERS.jsBigIntReader,
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
                            int min,
                            int max
                           ) {
    return getParser(READERS.arrayOfDecimalReader,
                     nullable,
                     min,
                     max
                    );
  }

  JsParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, JsError> p,
                                        boolean nullable,
                                        int min,
                                        int max
                                       ) {
    return nullable ?
           reader ->
               READERS.arrayOfDecimalReader.nullOrArrayEachSuchThat(reader,
                                                                    p,
                                                                    min,
                                                                    max
                                                                   ) :
           reader ->
               READERS.arrayOfDecimalReader.arrayEachSuchThat(reader,
                                                              p,
                                                              min,
                                                              max
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
                         int min,
                         int max
                        ) {
    return getParser(READERS.arrayOfLongReader,
                     nullable,
                     min,
                     max
                    );
  }

  JsParser ofArrayOfLongEachSuchThat(LongFunction<JsError> p,
                                     boolean nullable,
                                     int min,
                                     int max
                                    ) {
    return nullable ?
           reader ->
               READERS.arrayOfLongReader.nullOrArrayEachSuchThat(reader,
                                                                 p,
                                                                 min,
                                                                 max
                                                                ) :
           reader -> READERS.arrayOfLongReader.arrayEachSuchThat(reader,
                                                                 p,
                                                                 min,
                                                                 max
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


  JsParser ofDecimal(boolean nullable) {
    return getParser(READERS.decimalReader,
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

  JsParser ofMapOfLong(boolean nullable) {
    return getParser(READERS.mapOfLongReader,
                     nullable
                    );
  }

  JsParser ofMapOfDouble(boolean nullable) {
    return getParser(READERS.mapOfDoubleReader,
                     nullable
                    );
  }

  JsParser ofMapOfString(boolean nullable,
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

  JsParser ofMapOfInt(boolean nullable) {
    return getParser(READERS.mapOfIntegerReader,
                     nullable
                    );
  }

  JsParser ofMapOfInstant(boolean nullable) {
    return getParser(READERS.mapOfInstantReader,
                     nullable
                    );
  }

  JsParser ofMapOfDecimal(boolean nullable) {
    return getParser(READERS.mapOfDecimalReader,
                     nullable
                    );
  }

  JsParser ofMapOfBinary(boolean nullable) {
    return getParser(READERS.mapOfBinaryReader,
                     nullable
                    );
  }


  JsParser ofMapOfBigInt(boolean nullable) {
    return getParser(READERS.mapOfBigIntegerReader,
                     nullable
                    );
  }


  JsParser ofLong(boolean nullable) {
    return getParser(READERS.longReader,
                     nullable
                    );
  }

  JsParser ofDouble(boolean nullable) {
    return getParser(READERS.doubleReader,
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
                        int min,
                        int max
                       ) {
    return getParser(READERS.arrayOfIntReader,
                     nullable,
                     min,
                     max
                    );
  }

  JsParser ofArrayOfDouble(boolean nullable,
                           int min,
                           int max
                          ) {
    return getParser(READERS.arrayOfDoubleReader,
                     nullable,
                     min,
                     max
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
                                    int min,
                                    int max
                                   ) {

    return nullable ?
           reader -> READERS.arrayOfIntReader.nullOrArrayEachSuchThat(reader,
                                                                      p,
                                                                      min,
                                                                      max
                                                                     ) :
           reader -> READERS.arrayOfIntReader.arrayEachSuchThat(reader,
                                                                p,
                                                                min,
                                                                max
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

  JsParser ofInt(boolean nullable) {
    return getParser(READERS.intReader,
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


  JsParser ofInstant(boolean nullable) {
    return getParser(READERS.instantReader,
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
