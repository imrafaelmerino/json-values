package jsonvalues.spec;


import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

import static jsonvalues.spec.JsParsers.PARSERS;

/**
 * set of factory methods to create parsers from specs. Internal class that will be hidden when migrating
 * json-values to java 9 and modules
 */
 final class JsSpecParsers {

    public static final JsSpecParsers INSTANCE = new JsSpecParsers();
    private final BiFunction<JsReader, JsError, JsParserException> newParseException;

    private JsSpecParsers() {
        newParseException = (reader, error) ->
                JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(error),
                                           reader.getPositionInStream()
                                          );
    }

    public JsSpecParser ofArrayOfObjSuchThat(Function<JsArray, Optional<JsError>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfObjParser,
                         p,
                         nullable
                        );
    }

    private JsSpecParser getParser(JsArrayReader parser,
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

    public JsSpecParser ofArrayOfObjEachSuchThat(Function<JsObj, Optional<JsError>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
                                                ) {
        return nullable ?
                reader -> PARSERS.arrayOfObjParser.nullOrArrayEachSuchThat(reader,
                                                                           p,
                                                                           min,
                                                                           max
                                                                          ) :
                reader -> PARSERS.arrayOfObjParser.arrayEachSuchThat(reader,
                                                                     p,
                                                                     min,
                                                                     max
                                                                    );
    }

    public JsSpecParser ofArrayOfObjSpec(List<String> required,
                                         Map<String, JsSpecParser> parsers,
                                         Predicate<JsObj> predicate,
                                         boolean strict,
                                         boolean nullable,
                                         int min,
                                         int max
                                        ) {
        JsObjSpecReader f = required.isEmpty() ?
                new JsObjSpecReader(strict,
                                    parsers,
                                    predicate
                ) :
                new JsObjSpecWithRequiredKeysReader(required,
                                                    parsers,
                                                    strict,
                                                    predicate
                );
        JsArrayOfObjSpecReader parser = new JsArrayOfObjSpecReader(f);
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

    public JsSpecParser ofArrayOfObj(boolean nullable,
                                     int min,
                                     int max
                                    ) {
        return getParser(PARSERS.arrayOfObjParser,
                         nullable,
                         min,
                         max
                        );
    }

    private JsSpecParser getParser(JsArrayReader parser,
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


    public JsSpecParser ofObjSuchThat(final Function<JsObj, Optional<JsError>> predicate,
                                      final boolean nullable
                                     ) {

        if (nullable)
            return reader ->
            {
                JsValue value = PARSERS.objParser.nullOrValue(reader);
                if (value == JsNull.NULL) return value;
                Optional<JsError> opErr = predicate.apply(value.toJsObj());
                if (opErr.isEmpty()) return value;
                throw newParseException.apply(reader,
                                              opErr.get()
                                             );
            };


        else return reader ->
        {
            JsObj value = PARSERS.objParser.value(reader);
            Optional<JsError> result = predicate.apply(value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );
        };
    }


    public JsSpecParser ofArraySpec(List<JsSpecParser> keyDeserializers,
                                    boolean nullable
                                   ) {
        return nullable ?
                reader -> new JsArraySpecReader(keyDeserializers).nullOrArray(reader) :
                reader -> new JsArraySpecReader(keyDeserializers).array(reader);
    }

    public JsSpecParser ofObjSpec(List<String> required,
                                  Map<String, JsSpecParser> keyDeserializers,
                                  Predicate<JsObj> predicate,
                                  boolean nullable,
                                  boolean strict
                                 ) {
        return reader ->
        {
            if (required.isEmpty()) {
                JsObjSpecReader parser =
                        new JsObjSpecReader(strict,
                                            keyDeserializers,
                                            predicate
                        );
                return nullable ?
                        parser.nullOrValue(reader) :
                        parser.value(reader);
            }
            JsObjSpecWithRequiredKeysReader parser =
                    new JsObjSpecWithRequiredKeysReader(required,
                                                        keyDeserializers,
                                                        strict,
                                                        predicate
                    );
            return nullable ?
                    parser.nullOrValue(reader) :
                    parser.value(reader);

        };
    }

    public JsSpecParser ofArrayOfValueSuchThat(Function<JsArray, Optional<JsError>> p,
                                               boolean nullable
                                              ) {
        return getParser(PARSERS.arrayOfValueParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofObj(boolean nullable) {
        return getParser(PARSERS.objParser,
                         nullable
                        );
    }

    private JsSpecParser getParser(AbstractReader parser,
                                   boolean nullable
                                  ) {
        return nullable ?
                parser::nullOrValue :
                parser::value;
    }

    public JsSpecParser ofArrayOfValue(boolean nullable,
                                       int min,
                                       int max
                                      ) {
        return getParser(PARSERS.arrayOfValueParser,
                         nullable,
                         min,
                         max
                        );
    }

    public JsSpecParser ofArrayOfValueEachSuchThat(Function<JsValue, Optional<JsError>> p,
                                                   boolean nullable,
                                                   int min,
                                                   int max
                                                  ) {
        return nullable ?
                reader -> PARSERS.arrayOfValueParser.nullOrArrayEachSuchThat(reader,
                                                                             p,
                                                                             min,
                                                                             max
                                                                            ) :
                reader -> PARSERS.arrayOfValueParser.arrayEachSuchThat(reader,
                                                                       p,
                                                                       min,
                                                                       max
                                                                      );
    }

    public JsSpecParser ofValue() {
        return getParser(PARSERS.valueParser,
                         true
                        );
    }

    public JsSpecParser ofValueSuchThat(Function<JsValue, Optional<JsError>> predicate) {
        return reader ->
        {
            JsValue value = PARSERS.valueParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> result = predicate.apply(value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );
        };
    }


    public JsSpecParser ofBool(boolean nullable) {
        return getParser(PARSERS.boolParser,
                         nullable
                        );
    }

    public JsSpecParser ofTrue(boolean nullable) {
        return nullable ?
                PARSERS.boolParser::nullOrTrue :
                PARSERS.boolParser::True;
    }

    public JsSpecParser ofFalse(boolean nullable) {
        return nullable ?
                PARSERS.boolParser::nullOrFalse :
                PARSERS.boolParser::False;
    }

    public JsSpecParser ofArrayOfBool(boolean nullable,
                                      int min,
                                      int max
                                     ) {

        return nullable ?
                reader -> PARSERS.arrayOfBoolParser.nullOrArray(reader,
                                                                min,
                                                                max
                                                               ) :
                reader -> PARSERS.arrayOfBoolParser.array(reader,
                                                          min,
                                                          max
                                                         );
    }

    public JsSpecParser ofArrayOfBoolSuchThat(Function<JsArray, Optional<JsError>> p,
                                              boolean nullable
                                             ) {
        return getParser(PARSERS.arrayOfBoolParser,
                         p,
                         nullable
                        );

    }

    public JsSpecParser ofArrayOfStrEachSuchThat(Function<String, Optional<JsError>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
                                                ) {
        return nullable ?
                reader ->
                        PARSERS.arrayOfStrParser.nullOrArrayEachSuchThat(reader,
                                                                         p,
                                                                         min,
                                                                         max
                                                                        ) :
                reader ->
                        PARSERS.arrayOfStrParser.arrayEachSuchThat(reader,
                                                                   p,
                                                                   min,
                                                                   max
                                                                  );
    }

    public JsSpecParser ofArrayOfStrSuchThat(Function<JsArray, Optional<JsError>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfStrParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfStr(boolean nullable,
                                     int min,
                                     int max
                                    ) {
        return getParser(PARSERS.arrayOfStrParser,
                         nullable,
                         min,
                         max
                        );
    }


    public JsSpecParser ofStr(boolean nullable) {
        return getParser(PARSERS.strParser,
                         nullable
                        );
    }

    public JsSpecParser ofStrSuchThat(Function<String, Optional<JsError>> predicate,
                                      boolean nullable
                                     ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.strParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsStr().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );
        };
        else return reader ->
        {
            JsStr value = PARSERS.strParser.value(reader);

            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );
        };
    }

    public JsSpecParser ofArrayOfNumber(boolean nullable,
                                        int min,
                                        int max
                                       ) {
        return getParser(PARSERS.arrayOfNumberParser,
                         nullable,
                         min,
                         max
                        );
    }

    public JsSpecParser ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<JsError>> p,
                                                    boolean nullable,
                                                    int min,
                                                    int max
                                                   ) {
        return nullable ?
                reader -> PARSERS.arrayOfNumberParser.nullOrArrayEachSuchThat(reader,
                                                                              p,
                                                                              min,
                                                                              max
                                                                             ) :
                reader -> PARSERS.arrayOfNumberParser.arrayEachSuchThat(reader,
                                                                        p,
                                                                        min,
                                                                        max
                                                                       );
    }

    public JsSpecParser ofArrayOfNumberSuchThat(Function<JsArray, Optional<JsError>> p,
                                                boolean nullable
                                               ) {
        return getParser(PARSERS.arrayOfNumberParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfIntegralSuchThat(Function<JsArray, Optional<JsError>> p,
                                                  boolean nullable
                                                 ) {
        return getParser(PARSERS.arrayOfIntegralParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofNumber(boolean nullable) {
        return getParser(PARSERS.numberParser,
                         nullable
                        );
    }

    public JsSpecParser ofNumberSuchThat(Function<JsNumber, Optional<JsError>> predicate,
                                         boolean nullable
                                        ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.numberParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsNumber());
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsNumber value = PARSERS.numberParser.value(reader);
            Optional<JsError> result = predicate.apply(value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );
        };

    }


    public JsSpecParser ofArrayOfIntegral(boolean nullable,
                                          int min,
                                          int max
                                         ) {
        return getParser(PARSERS.arrayOfIntegralParser,
                         nullable,
                         min,
                         max
                        );
    }

    public JsSpecParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<JsError>> p,
                                                      boolean nullable,
                                                      int min,
                                                      int max
                                                     ) {
        return nullable ?
                reader ->
                        PARSERS.arrayOfIntegralParser.nullOrArrayEachSuchThat(reader,
                                                                              p,
                                                                              min,
                                                                              max
                                                                             ) :
                reader ->
                        PARSERS.arrayOfIntegralParser.arrayEachSuchThat(reader,
                                                                        p,
                                                                        min,
                                                                        max
                                                                       );
    }

    public JsSpecParser ofIntegral(boolean nullable) {
        return getParser(PARSERS.integralParser,
                         nullable
                        );
    }

    public JsSpecParser ofIntegralSuchThat(Function<BigInteger, Optional<JsError>> predicate,
                                           boolean nullable
                                          ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.integralParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsBigInt().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );
        };
        else return reader ->
        {
            JsBigInt integral = PARSERS.integralParser.value(reader);
            Optional<JsError> result = predicate.apply(integral.value);
            if (result.isEmpty()) return integral;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }

    public JsSpecParser ofArrayOfDecimal(boolean nullable,
                                         int min,
                                         int max
                                        ) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         nullable,
                         min,
                         max
                        );
    }

    public JsSpecParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<JsError>> p,
                                                     boolean nullable,
                                                     int min,
                                                     int max
                                                    ) {
        return nullable ?
                reader ->
                        PARSERS.arrayOfDecimalParser.nullOrArrayEachSuchThat(reader,
                                                                             p,
                                                                             min,
                                                                             max
                                                                            ) :
                reader ->
                        PARSERS.arrayOfDecimalParser.arrayEachSuchThat(reader,
                                                                       p,
                                                                       min,
                                                                       max
                                                                      );
    }

    public JsSpecParser ofArrayOfDecimalSuchThat(Function<JsArray, Optional<JsError>> p,
                                                 boolean nullable
                                                ) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         p,
                         nullable
                        );

    }


    public JsSpecParser ofArrayOfLong(boolean nullable,
                                      int min,
                                      int max
                                     ) {
        return getParser(PARSERS.arrayOfLongParser,
                         nullable,
                         min,
                         max
                        );
    }

    public JsSpecParser ofArrayOfLongEachSuchThat(LongFunction<Optional<JsError>> p,
                                                  boolean nullable,
                                                  int min,
                                                  int max
                                                 ) {
        return nullable ?
                reader ->
                        PARSERS.arrayOfLongParser.nullOrArrayEachSuchThat(reader,
                                                                          p,
                                                                          min,
                                                                          max
                                                                         ) :
                reader -> PARSERS.arrayOfLongParser.arrayEachSuchThat(reader,
                                                                      p,
                                                                      min,
                                                                      max
                                                                     );
    }

    public JsSpecParser ofArrayOfLongSuchThat(Function<JsArray, Optional<JsError>> p,
                                              boolean nullable
                                             ) {
        return getParser(PARSERS.arrayOfLongParser,
                         p,
                         nullable
                        );
    }


    public JsSpecParser ofDecimal(boolean nullable) {
        return getParser(PARSERS.decimalParser,
                         nullable
                        );
    }

    public JsSpecParser ofDecimalSuchThat(Function<BigDecimal, Optional<JsError>> predicate,
                                          boolean nullable
                                         ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.decimalParser.nullOrValue(reader);
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
                JsBigDec decimal = PARSERS.decimalParser.value(reader);
                Optional<JsError> result = predicate.apply(decimal.value);
                if (result.isEmpty()) return decimal;
                throw newParseException.apply(reader,
                                              result.get()
                                             );

            };
    }

    public JsSpecParser ofMapOfLong(boolean nullable) {
        return getParser(PARSERS.mapOfLongParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfString(boolean nullable) {
        return getParser(PARSERS.mapOfStringParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfBool(boolean nullable) {
        return getParser(PARSERS.mapOfBoolParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfInt(boolean nullable) {
        return getParser(PARSERS.mapOfIntegerParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfInstant(boolean nullable) {
        return getParser(PARSERS.mapOfInstantParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfDecimal(boolean nullable) {
        return getParser(PARSERS.mapOfDecimalParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfBinary(boolean nullable) {
        return getParser(PARSERS.mapOfBinaryParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfObj(boolean nullable) {
        return getParser(PARSERS.mapOfObjParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfBigInt(boolean nullable) {
        return getParser(PARSERS.mapOfBigIntegerParser,
                         nullable
                        );
    }

    public JsSpecParser ofMapOfArray(boolean nullable) {
        return getParser(PARSERS.mapOfArrayParser,
                         nullable
                        );
    }

    public JsSpecParser ofLong(boolean nullable) {
        return getParser(PARSERS.longParser,
                         nullable
                        );
    }

    public JsSpecParser ofLongSuchThat(LongFunction<Optional<JsError>> predicate,
                                       boolean nullable
                                      ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.longParser.nullOrValue(reader);
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
            JsLong value = PARSERS.longParser.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }


    public JsSpecParser ofArrayOfInt(boolean nullable,
                                     int min,
                                     int max
                                    ) {
        return getParser(PARSERS.arrayOfIntParser,
                         nullable,
                         min,
                         max
                        );
    }


    public JsSpecParser ofArrayOfIntSuchThat(Function<JsArray, Optional<JsError>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfIntParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfIntEachSuchThat(IntFunction<Optional<JsError>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
                                                ) {

        return nullable ?
                reader -> PARSERS.arrayOfIntParser.nullOrArrayEachSuchThat(reader,
                                                                           p,
                                                                           min,
                                                                           max
                                                                          ) :
                reader -> PARSERS.arrayOfIntParser.arrayEachSuchThat(reader,
                                                                     p,
                                                                     min,
                                                                     max
                                                                    );
    }

    public JsSpecParser ofBinary(boolean nullable) {
        return getParser(PARSERS.binaryParser,
                         nullable
                        );
    }

    public JsSpecParser ofBinarySuchThat(Function<byte[], Optional<JsError>> predicate,
                                         boolean nullable
                                        ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.binaryParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsBinary().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsBinary value = PARSERS.binaryParser.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }

    public JsSpecParser ofInt(boolean nullable) {
        return getParser(PARSERS.intParser,
                         nullable
                        );
    }

    public JsSpecParser ofIntSuchThat(IntFunction<Optional<JsError>> predicate,
                                      boolean nullable
                                     ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.intParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsInt().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsInt value = PARSERS.intParser.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }


    public JsSpecParser ofInstant(boolean nullable) {
        return getParser(PARSERS.instantParser,
                         nullable
                        );
    }

    public JsSpecParser ofInstantSuchThat(Function<Instant, Optional<JsError>> predicate,
                                          boolean nullable
                                         ) {
        if (nullable) return reader ->
        {
            JsValue value = PARSERS.instantParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            Optional<JsError> opErr = predicate.apply(value.toJsInstant().value);
            if (opErr.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          opErr.get()
                                         );

        };
        else return reader ->
        {
            JsInstant value = PARSERS.instantParser.value(reader);
            Optional<JsError> result = predicate.apply(value.value);
            if (result.isEmpty()) return value;
            throw newParseException.apply(reader,
                                          result.get()
                                         );

        };
    }
}
