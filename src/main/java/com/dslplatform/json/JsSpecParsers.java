package com.dslplatform.json;


import fun.tuple.Pair;
import jsonvalues.*;
import jsonvalues.spec.ERROR_CODE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

import static com.dslplatform.json.JsParsers.PARSERS;

/**
 * set of factory methods to create parsers from specs. Internal class that will be hidden when migrating
 * json-values to java 9 and modules
 */
public final class JsSpecParsers {

    public static final JsSpecParsers INSTANCE = new JsSpecParsers();
    private final BiFunction<JsonReader<?>, Pair<JsValue, ERROR_CODE>, JsParserException> newParseException;

    private JsSpecParsers() {
        newParseException = (reader, error) ->
                new JsParserException(ParserErrors.JS_ERROR_2_STR.apply(error),
                                      reader.getCurrentIndex());
    }

    public JsSpecParser ofArrayOfObjSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                             boolean nullable
    ) {
        return getParser(PARSERS.arrayOfObjParser,
                         p,
                         nullable);
    }

    private JsSpecParser getParser(JsArrayParser parser,
                                   Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                   boolean nullable
    ) {
        return nullable ?
               (reader -> parser.nullOrArraySuchThat(reader,
                                                     p)) :
               (reader -> parser.arraySuchThat(reader,
                                               p));
    }

    public JsSpecParser ofArrayOfObjEachSuchThat(Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
    ) {
        return nullable ?
               (reader -> PARSERS.arrayOfObjParser.nullOrArrayEachSuchThat(reader,
                                                                           p,
                                                                           min,
                                                                           max)) :
               (reader -> PARSERS.arrayOfObjParser.arrayEachSuchThat(reader,
                                                                     p,
                                                                     min,
                                                                     max));
    }

    public JsSpecParser ofArrayOfObjSpec(List<String> required,
                                         Map<String, JsSpecParser> parsers,
                                         Predicate<JsObj> predicate,
                                         boolean strict,
                                         boolean nullable,
                                         int min,
                                         int max
    ) {
        JsObjSpecParser f = required.isEmpty() ?
                            new JsObjSpecParser(strict,
                                                parsers,
                                                predicate
                            ) :
                            new JsObjSpecWithRequiredKeysParser(required,
                                                                parsers,
                                                                strict,
                                                                predicate
                            );
        JsArrayOfObjSpecParser parser = new JsArrayOfObjSpecParser(f);
        return nullable ?
               reader -> parser.nullOrArray(reader,
                                            min,
                                            max) :
               reader -> parser.array(reader,
                                      min,
                                      max);

    }

    public JsSpecParser ofArrayOfObj(boolean nullable,
                                     int min,
                                     int max) {
        return getParser(PARSERS.arrayOfObjParser,
                         nullable,
                         min,
                         max
        );
    }

    private JsSpecParser getParser(JsArrayParser parser,
                                   boolean nullable,
                                   int min,
                                   int max) {
        return nullable ?
               reader -> parser.nullOrArray(reader,
                                            min,
                                            max) :
               reader -> parser.array(reader,
                                      min,
                                      max);
    }


    public JsSpecParser ofObjSuchThat(final Function<JsObj, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                      final boolean nullable
    ) {

        if (nullable)
            return reader ->
            {
                JsValue value = PARSERS.objParser.nullOrValue(reader);
                if (value == JsNull.NULL) return value;
                else {
                    Optional<Pair<JsValue, ERROR_CODE>> opErr =
                            testTypeAndSpec(JsValue::isObj,
                                            JsValue::toJsObj,
                                            predicate,
                                            () -> new IllegalStateException("Internal error. JsObjDeserializer.nullOrValue didn't return wither null or a JsObj as expected.")
                            ).apply(value);
                    if (!opErr.isPresent()) return value;
                    else throw newParseException.apply(reader,
                                                       opErr.get());
                }
            };


        else return reader ->
        {
            JsObj value = PARSERS.objParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );
        };
    }

    /**
     * @param typeCondition     condition to check if the value has the expected type
     * @param converter         function to convert the value to the expected type
     * @param spec              the specification that the value has to conform
     * @param errorTypeSupplier if the value doesn't have the expected type,
     *                          the error produced by this supplier is thrown. It's considered an internal error
     *                          because if this happened, it would be because a development error
     * @return a function to test that a value has the expected type and conforms a given spec
     */
    private <R> Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> testTypeAndSpec(Predicate<JsValue> typeCondition,
                                                                                       Function<JsValue, R> converter,
                                                                                       Function<R, Optional<Pair<JsValue, ERROR_CODE>>> spec,
                                                                                       Supplier<RuntimeException> errorTypeSupplier
    ) {
        return value ->
        {
            if (typeCondition.test(value)) return spec.apply(converter.apply(value));
            else throw errorTypeSupplier.get();
        };
    }

    public JsSpecParser ofArraySpec(List<JsSpecParser> keyDeserializers,
                                    boolean nullable
    ) {
        return nullable ?
               (reader -> new JsArraySpecParser(keyDeserializers).nullOrArray(reader)) :
               (reader -> new JsArraySpecParser(keyDeserializers).array(reader));
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
                JsObjSpecParser parser =
                        new JsObjSpecParser(strict,
                                            keyDeserializers,
                                            predicate
                        );
                return nullable ?
                       parser.nullOrValue(reader) :
                       parser.value(reader);
            } else {
                JsObjSpecWithRequiredKeysParser parser =
                        new JsObjSpecWithRequiredKeysParser(required,
                                                            keyDeserializers,
                                                            strict,
                                                            predicate
                        );
                return nullable ?
                       parser.nullOrValue(reader) :
                       parser.value(reader);
            }

        };
    }

    public JsSpecParser ofArrayOfValueSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
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

    private JsSpecParser getParser(AbstractParser parser,
                                   boolean nullable
    ) {
        return nullable ?
               parser::nullOrValue :
               parser::value;
    }

    public JsSpecParser ofArrayOfValue(boolean nullable,
                                       int min,
                                       int max) {
        return getParser(PARSERS.arrayOfValueParser,
                         nullable,
                         min,
                         max
        );
    }

    public JsSpecParser ofArrayOfValueEachSuchThat(Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                   boolean nullable,
                                                   int min,
                                                   int max
    ) {
        return nullable ?
               (reader -> PARSERS.arrayOfValueParser.nullOrArrayEachSuchThat(reader,
                                                                             p,
                                                                             min,
                                                                             max)) :
               (reader -> PARSERS.arrayOfValueParser.arrayEachSuchThat(reader,
                                                                       p,
                                                                       min,
                                                                       max));
    }

    public JsSpecParser ofValue() {
        return getParser(PARSERS.valueParser,
                         true
        );
    }

    public JsSpecParser ofValueSuchThat(Function<JsValue, Optional<Pair<JsValue, ERROR_CODE>>> predicate) {
        return reader ->
        {
            JsValue value = PARSERS.valueParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value);
                if (!result.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   result.get()
                );
            }
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
                                      int max) {

        return nullable ?
               reader -> PARSERS.arrayOfBoolParser.nullOrArray(reader,
                                                               min,
                                                               max) :
               reader -> PARSERS.arrayOfBoolParser.array(reader,
                                                         min,
                                                         max);
    }

    public JsSpecParser ofArrayOfBoolSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                              boolean nullable
    ) {
        return getParser(PARSERS.arrayOfBoolParser,
                         p,
                         nullable
        );

    }

    public JsSpecParser ofArrayOfStrEachSuchThat(Function<String, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
    ) {
        return nullable ?
               (reader ->
                       PARSERS.arrayOfStrParser.nullOrArrayEachSuchThat(reader,
                                                                        p,
                                                                        min,
                                                                        max)) :
               (reader ->
                       PARSERS.arrayOfStrParser.arrayEachSuchThat(reader,
                                                                  p,
                                                                  min,
                                                                  max));
    }

    public JsSpecParser ofArrayOfStrSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                             boolean nullable
    ) {
        return getParser(PARSERS.arrayOfStrParser,
                         p,
                         nullable
        );
    }

    public JsSpecParser ofArrayOfStr(boolean nullable,
                                     int min,
                                     int max) {
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

    public JsSpecParser ofStrSuchThat(Function<String, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                      boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.strParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(JsValue::isStr,
                                        v -> v.toJsStr().value,
                                        predicate,
                                        () -> new IllegalStateException("Internal error.JsStrDeserializer.nullOrValue didn't return neither null or a JsStr as expected.")
                        ).apply(value);

                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());
            }
        };
        else return reader ->
        {
            JsStr value = PARSERS.strParser.value(reader);

            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );
        };
    }

    public JsSpecParser ofArrayOfNumber(boolean nullable,
                                        int min,
                                        int max) {
        return getParser(PARSERS.arrayOfNumberParser,
                         nullable,
                         min,
                         max
        );
    }

    public JsSpecParser ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                    boolean nullable,
                                                    int min,
                                                    int max
    ) {
        return nullable ?
               (reader -> PARSERS.arrayOfNumberParser.nullOrArrayEachSuchThat(reader,
                                                                              p,
                                                                              min,
                                                                              max)) :
               (reader -> PARSERS.arrayOfNumberParser.arrayEachSuchThat(reader,
                                                                        p,
                                                                        min,
                                                                        max));
    }

    public JsSpecParser ofArrayOfNumberSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                boolean nullable
    ) {
        return getParser(PARSERS.arrayOfNumberParser,
                         p,
                         nullable
        );
    }

    public JsSpecParser ofArrayOfIntegralSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
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

    public JsSpecParser ofNumberSuchThat(Function<JsNumber, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                         boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.numberParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(JsValue::isNumber,
                                        v -> value.toJsNumber(),
                                        predicate,
                                        () -> new IllegalStateException("Internal error.JsNumberDeserializer.nullOrValue didn't return neither null or a JsNumber as expected.")
                        ).apply(value);
                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());
            }

        };
        else return reader ->
        {
            JsNumber value = PARSERS.numberParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );
        };

    }


    public JsSpecParser ofArrayOfIntegral(boolean nullable,
                                          int min,
                                          int max) {
        return getParser(PARSERS.arrayOfIntegralParser,
                         nullable,
                         min,
                         max
        );
    }

    public JsSpecParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                      boolean nullable,
                                                      int min,
                                                      int max
    ) {
        return nullable ?
               (reader ->
                       PARSERS.arrayOfIntegralParser.nullOrArrayEachSuchThat(reader,
                                                                             p,
                                                                             min,
                                                                             max)) :
               (reader ->
                       PARSERS.arrayOfIntegralParser.arrayEachSuchThat(reader,
                                                                       p,
                                                                       min,
                                                                       max));
    }

    public JsSpecParser ofIntegral(boolean nullable) {
        return getParser(PARSERS.integralParser,
                         nullable
        );
    }

    public JsSpecParser ofIntegralSuchThat(Function<BigInteger, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                           boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.integralParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(JsValue::isBigInt,
                                        v -> v.toJsBigInt().value,
                                        predicate,
                                        () -> new IllegalStateException("Internal error.JsIntegralDeserializer.nullOrValue didn't return neither null or a JsBigInt as expected.")
                        ).apply(value);
                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());
            }
        };
        else return reader ->
        {
            JsBigInt integral = PARSERS.integralParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(integral.value);
            if (!result.isPresent()) return integral;
            else throw newParseException.apply(reader,
                                               result.get()
            );

        };
    }

    public JsSpecParser ofArrayOfDecimal(boolean nullable,
                                         int min,
                                         int max) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         nullable,
                         min,
                         max
        );
    }

    public JsSpecParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                     boolean nullable,
                                                     int min,
                                                     int max
    ) {
        return nullable ?
               (reader ->
                       PARSERS.arrayOfDecimalParser.nullOrArrayEachSuchThat(reader,
                                                                            p,
                                                                            min,
                                                                            max)) :
               (reader ->
                       PARSERS.arrayOfDecimalParser.arrayEachSuchThat(reader,
                                                                      p,
                                                                      min,
                                                                      max));
    }

    public JsSpecParser ofArrayOfDecimalSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                 boolean nullable
    ) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         p,
                         nullable
        );

    }


    public JsSpecParser ofArrayOfLong(boolean nullable,
                                      int min,
                                      int max) {
        return getParser(PARSERS.arrayOfLongParser,
                         nullable,
                         min,
                         max
        );
    }

    public JsSpecParser ofArrayOfLongEachSuchThat(LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                  boolean nullable,
                                                  int min,
                                                  int max
    ) {
        return nullable ?
               (reader ->
                       PARSERS.arrayOfLongParser.nullOrArrayEachSuchThat(reader,
                                                                         p,
                                                                         min,
                                                                         max)) :
               (reader -> PARSERS.arrayOfLongParser.arrayEachSuchThat(reader,
                                                                      p,
                                                                      min,
                                                                      max));
    }

    public JsSpecParser ofArrayOfLongSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
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

    public JsSpecParser ofDecimalSuchThat(Function<BigDecimal, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                          boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.decimalParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(JsValue::isDecimal,
                                        v -> v.toJsBigDec().value,
                                        predicate,
                                        () -> new IllegalStateException("Internal error.JsDecimalDeserializer.nullOrValue didn't return neither null or a JsBigDec as expected.")
                        ).apply(value);
                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());

            }
        };

        else
            return reader ->
            {
                JsBigDec decimal = PARSERS.decimalParser.value(reader);
                Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(decimal.value);
                if (!result.isPresent()) return decimal;
                else throw newParseException.apply(reader,
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

    public JsSpecParser ofLongSuchThat(LongFunction<Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                       boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.longParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> optErr =
                        testTypeAndSpec(JsValue::isLong,
                                        v -> v.toJsLong().value,
                                        predicate::apply,
                                        () -> new IllegalStateException("Internal error. JsLongDeserializer.nullOrValue didn't return neither null or a JsLong as expected.")
                        ).apply(value);
                if (!optErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   optErr.get()
                );

            }
        };
        else return reader ->
        {
            JsLong value = PARSERS.longParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );

        };
    }


    public JsSpecParser ofArrayOfInt(boolean nullable,
                                     int min,
                                     int max) {
        return getParser(PARSERS.arrayOfIntParser,
                         nullable,
                         min,
                         max
        );
    }


    public JsSpecParser ofArrayOfIntSuchThat(Function<JsArray, Optional<Pair<JsValue, ERROR_CODE>>> p,
                                             boolean nullable
    ) {
        return getParser(PARSERS.arrayOfIntParser,
                         p,
                         nullable
        );
    }

    public JsSpecParser ofArrayOfIntEachSuchThat(IntFunction<Optional<Pair<JsValue, ERROR_CODE>>> p,
                                                 boolean nullable,
                                                 int min,
                                                 int max
    ) {

        return nullable ?
               (reader -> PARSERS.arrayOfIntParser.nullOrArrayEachSuchThat(reader,
                                                                           p,
                                                                           min,
                                                                           max)) :
               (reader -> PARSERS.arrayOfIntParser.arrayEachSuchThat(reader,
                                                                     p,
                                                                     min,
                                                                     max));
    }

    public JsSpecParser ofBinary(boolean nullable) {
        return getParser(PARSERS.binaryParser,
                         nullable
        );
    }

    public JsSpecParser ofBinarySuchThat(Function<byte[], Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                         boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.binaryParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(v -> value.isBinary(),
                                        v -> v.toJsBinary().value,
                                        predicate,
                                        () -> new IllegalStateException("Internal error. JsBinaryDeserializer.nullOrValue didn't return neither null or a byte[] as expected.")
                        ).apply(value);

                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());

            }

        };
        else return reader ->
        {
            JsBinary value = PARSERS.binaryParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );

        };
    }

    public JsSpecParser ofInt(boolean nullable) {
        return getParser(PARSERS.intParser,
                         nullable
        );
    }

    public JsSpecParser ofIntSuchThat(IntFunction<Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                      boolean nullable
    ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.intParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(v -> value.isInt(),
                                        v -> v.toJsInt().value,
                                        predicate::apply,
                                        () -> new IllegalStateException("Internal error. JsIntDeserializer.nullOrValue didn't return neither null or a Int as expected.")
                        ).apply(value);
                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());

            }

        };
        else return reader ->
        {
            JsInt value = PARSERS.intParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );

        };
    }


    public JsSpecParser ofInstant(boolean nullable) {
        return getParser(PARSERS.instantParser,
                         nullable
        );
    }

    public JsSpecParser ofInstantSuchThat(Function<Instant, Optional<Pair<JsValue, ERROR_CODE>>> predicate,
                                          boolean nullable
    ) {
        if (nullable) return reader ->
        {
            JsValue value = PARSERS.instantParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                Optional<Pair<JsValue, ERROR_CODE>> opErr =
                        testTypeAndSpec(v -> value.isInstant(),
                                        v -> v.toJsInstant().value,
                                        predicate,
                                        () -> new IllegalStateException("Internal error. JsInstantDeserializer.nullOrValue didn't return neither null or an instant as expected.")
                        ).apply(value);
                if (!opErr.isPresent()) return value;
                else throw newParseException.apply(reader,
                                                   opErr.get());

            }

        };
        else return reader ->
        {
            JsInstant value = PARSERS.instantParser.value(reader);
            Optional<Pair<JsValue, ERROR_CODE>> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
            );

        };
    }
}
