package jsonvalues.spec;


import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

import static jsonvalues.spec.JsReaders.READERS;

/**
 * set of factory methods to create parsers from specs. Internal class that will be hidden when migrating json-values to
 * java 9 and modules
 */
final class JsParsers {

    static final JsParsers INSTANCE = new JsParsers();
    private final BiFunction<JsReader, JsError, JsParserException> newParseException;

    private JsParsers() {
        newParseException = (reader, error) ->
                JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(error),
                                           reader.getPositionInStream()
                                          );
    }

    JsParser ofArrayOfObjSuchThat(Function<JsArray, Optional<JsError>> p,
                                  boolean nullable
                                 ) {
        return getParser(READERS.arrayOfObjReader,
                         p,
                         nullable
                        );
    }

    private JsParser getParser(JsArrayReader parser,
                               Function<JsArray, Optional<JsError>> p,
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

    JsParser ofArrayOfObjEachSuchThat(Function<JsObj, Optional<JsError>> p,
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


    JsParser ofObjSuchThat(final Function<JsObj, Optional<JsError>> predicate,
                           final boolean nullable
                          ) {

        if (nullable)
            return reader ->
            {
                JsValue value = READERS.objReader.nullOrValue(reader);
                if (value == JsNull.NULL) return value;
                Optional<JsError> opErr = predicate.apply(value.toJsObj());
                if (opErr.isEmpty()) return value;
                throw newParseException.apply(reader,
                                              opErr.get()
                                             );
            };


        else return reader ->
        {
            JsObj value = READERS.objReader.value(reader);
            Optional<JsError> result = predicate.apply(value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );
        };
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
        return getParser(new JsArrayOfSpecReader(parser), nullable, min, max);
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

    JsParser ofArrayOfValueSuchThat(Function<JsArray, Optional<JsError>> p,
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

    private JsParser getParser(AbstractReader parser,
                               boolean nullable
                              ) {
        return nullable ?
                parser::nullOrValue :
                parser::value;
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

    JsParser ofArrayOfValueEachSuchThat(Function<JsValue, Optional<JsError>> p,
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

    JsParser ofValueSuchThat(Function<JsValue, Optional<JsError>> predicate) {
        return reader ->
        {
            JsValue value = READERS.valueReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> result = predicate.apply(value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
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

    JsParser ofArrayOfBoolSuchThat(Function<JsArray, Optional<JsError>> p,
                                   boolean nullable
                                  ) {
        return getParser(READERS.arrayOfBoolReader,
                         p,
                         nullable
                        );

    }

    JsParser ofArrayOfStrEachSuchThat(Function<String, Optional<JsError>> p,
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

    JsParser ofArrayOfStrSuchThat(Function<JsArray, Optional<JsError>> p,
                                  boolean nullable
                                 ) {
        return getParser(READERS.arrayOfStringReader,
                         p,
                         nullable
                        );
    }

    JsParser ofArrayOfStr(boolean nullable,
                          int min,
                          int max
                         ) {
        return getParser(READERS.arrayOfStringReader,
                         nullable,
                         min,
                         max
                        );
    }


    JsParser ofStr(boolean nullable) {
        return getParser(READERS.strReader,
                         nullable
                        );
    }

    JsParser ofStrSuchThat(Function<String, Optional<JsError>> predicate,
                           boolean nullable
                          ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.strReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsStr().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );
        };
        else return reader ->
        {
            JsStr value = READERS.strReader.value(reader);

            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );
        };
    }


    JsParser ofArrayOfIntegralSuchThat(Function<JsArray, Optional<JsError>> p,
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

    JsParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<JsError>> p,
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

    JsParser ofArrayOfDoubleEachSuchThat(DoubleFunction<Optional<JsError>> p,
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

    JsParser ofIntegralSuchThat(Function<BigInteger, Optional<JsError>> predicate,
                                boolean nullable
                               ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.jsBigIntReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsBigInt().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );
        };
        else return reader ->
        {
            JsBigInt integral = READERS.jsBigIntReader.value(reader);
            Optional<JsError> result = predicate.apply(integral.value);
            if (result.isEmpty()) return integral;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
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

    JsParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<JsError>> p,
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

    JsParser ofArrayOfDecimalSuchThat(Function<JsArray, Optional<JsError>> p,
                                      boolean nullable
                                     ) {
        return getParser(READERS.arrayOfDecimalReader,
                         p,
                         nullable
                        );

    }

    JsParser ofArrayOfDoubleSuchThat(Function<JsArray, Optional<JsError>> p,
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

    JsParser ofArrayOfLongEachSuchThat(LongFunction<Optional<JsError>> p,
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

    JsParser ofArrayOfLongSuchThat(Function<JsArray, Optional<JsError>> p,
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

    JsParser ofDecimalSuchThat(Function<BigDecimal, Optional<JsError>> predicate,
                               boolean nullable
                              ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.decimalReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsBigDec().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };

        else
            return reader ->
            {
                JsBigDec decimal = READERS.decimalReader.value(reader);
                Optional<JsError> result = predicate.apply(decimal.value);
                if (result.isEmpty()) return decimal;
                throw newParseException.apply(reader,
                                              result.get()
                                             );

            };
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

    JsParser ofMapOfString(boolean nullable) {
        return getParser(READERS.mapOfStringReader,
                         nullable
                        );
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

    JsParser ofLongSuchThat(LongFunction<Optional<JsError>> predicate,
                            boolean nullable
                           ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.longReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<JsError> optErr = predicate.apply(value.toJsLong().value);
                if (optErr.isEmpty()) return value;
                throw newParseException.apply(reader,
                                              optErr.get()
                                             );

            }
        };
        else return reader ->
        {
            JsLong value = READERS.longReader.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }

    JsParser ofDoubleSuchThat(DoubleFunction<Optional<JsError>> predicate,
                              boolean nullable
                             ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.doubleReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<JsError> optErr = predicate.apply(value.toJsDouble().value);
                if (optErr.isEmpty()) return value;
                throw newParseException.apply(reader,
                                              optErr.get()
                                             );

            }
        };
        else return reader ->
        {
            JsDouble value = READERS.doubleReader.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
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


    JsParser ofArrayOfIntSuchThat(Function<JsArray, Optional<JsError>> p,
                                  boolean nullable
                                 ) {
        return getParser(READERS.arrayOfIntReader,
                         p,
                         nullable
                        );
    }


    JsParser ofArrayOfIntEachSuchThat(IntFunction<Optional<JsError>> p,
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

    JsParser ofFixedBinary(int size, boolean nullable) {
        return getParser(new JsFixedBinaryReader(size),
                         nullable
                        );
    }

    JsParser ofBinarySuchThat(Function<byte[], Optional<JsError>> predicate,
                              boolean nullable
                             ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.binaryReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsBinary().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsBinary value = READERS.binaryReader.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }

    JsParser ofInt(boolean nullable) {
        return getParser(READERS.intReader,
                         nullable
                        );
    }

    JsParser ofIntSuchThat(IntFunction<Optional<JsError>> predicate,
                           boolean nullable
                          ) {

        if (nullable) return reader ->
        {
            JsValue value = READERS.intReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsInt().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsInt value = READERS.intReader.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }


    JsParser ofInstant(boolean nullable) {
        return getParser(READERS.instantReader,
                         nullable
                        );
    }

    JsParser ofInstantSuchThat(Function<Instant, Optional<JsError>> predicate,
                               boolean nullable
                              ) {
        if (nullable) return reader ->
        {
            JsValue value = READERS.instantReader.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsInstant().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsInstant value = READERS.instantReader.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }
}
