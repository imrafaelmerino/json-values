package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import jsonvalues.*;
import jsonvalues.spec.Error;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.function.*;

import static com.dslplatform.json.parsers.JsParsers.PARSERS;

public class JsSpecParsers {

    public static final JsSpecParsers INSTANCE = new JsSpecParsers();
    private final BiFunction<JsonReader<?>, Error, JsParserException> newParseException;


    private JsSpecParsers() {
        newParseException =
                (reader, error) -> new JsParserException(reader.newParseError(error.toString()));

    }

    public JsSpecParser ofArrayOfObjSuchThat(Function<JsArray, Optional<Error>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfObjParser,
                         p,
                         nullable
                        );
    }

    private JsSpecParser getParser(JsArrayParser parser,
                                   Function<JsArray, Optional<Error>> p,
                                   boolean nullable
                                  ) {
        if (nullable)
            return reader -> parser.nullOrArraySuchThat(reader,
                                                        p
                                                       );

        else return reader -> parser.arraySuchThat(reader,
                                                   p
                                                  );
    }

    public JsSpecParser ofArrayOfObjEachSuchThat(Function<JsObj, Optional<Error>> p,
                                                 boolean nullable
                                                ) {
        if (nullable) return reader -> PARSERS.arrayOfObjParser.nullOrArrayEachSuchThat(reader,
                                                                                        p
                                                                                       );

        else return reader -> PARSERS.arrayOfObjParser.arrayEachSuchThat(reader,
                                                                         p
                                                                        );
    }

    public JsSpecParser ofArrayOfObjSpec(Vector<String> required,
                                         Map<String, JsSpecParser> keyDeserializers,
                                         boolean nullable,
                                         boolean strict
                                        ) {
        JsObjSpecParser f = (required.isEmpty()) ?
                            new JsObjSpecParser(strict,
                                                keyDeserializers
                            ) :
                            new JsObjSpecWithRequiredKeysParser(required,
                                                                keyDeserializers,
                                                                strict
                            );
        JsArrayOfObjSpecParser parser = new JsArrayOfObjSpecParser(f);
        if (nullable)
            return parser::nullOrArray;

        else
            return parser::array;

    }

    public JsSpecParser ofArrayOfObj(boolean nullable) {
        return getParser(PARSERS.arrayOfObjParser,
                         nullable
                        );
    }

    private JsSpecParser getParser(JsArrayParser parser,
                                   boolean nullable
                                  ) {
        if (nullable)
            return parser::nullOrArray;
        else return parser::array;
    }

    public JsSpecParser ofObjSuchThat(final Function<JsObj, Optional<Error>> predicate,
                                      final boolean nullable
                                     ) {

        if (nullable)
            return reader ->
            {
                JsValue value = PARSERS.objParser.nullOrValue(reader);
                if (value == JsNull.NULL) return value;
                else {
                    testTypeAndSpec(JsValue::isObj,
                                    JsValue::toJsObj,
                                    predicate,
                                    () -> new IllegalStateException("Internal error.JsObjDeserializer.nullOrValue didn't return wither null or a JsObj as expected.")
                                   ).apply(value)
                                    .ifPresent(e -> newParseException.apply(reader,
                                                                            e
                                                                           )
                                              );
                    return value;
                }
            };


        else return reader ->
        {
            JsObj                 value  = PARSERS.objParser.value(reader);
            final Optional<Error> result = predicate.apply(value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );
        };
    }

    /**
     @param typeCondition     condition to check if the value has the expected type
     @param converter         function to convert the value to the expected type
     @param spec              the specification that the value has to conform
     @param errorTypeSupplier if the value doesn't have the expected type,
     the error produced by this supplier is thrown. It's considered an internal error
     because if this happened, it would be because a development error
     @return a function to test that a value has the expected type and conforms a given spec
     */
    private <R> Function<JsValue, Optional<Error>> testTypeAndSpec(Predicate<JsValue> typeCondition,
                                                                   Function<JsValue, R> converter,
                                                                   Function<R, Optional<Error>> spec,
                                                                   Supplier<RuntimeException> errorTypeSupplier
                                                                  ) {
        return value ->
        {
            if (typeCondition.test(value)) return spec.apply(converter.apply(value));
            else throw errorTypeSupplier.get();
        };
    }

    public JsSpecParser ofArraySpec(Vector<JsSpecParser> keyDeserializers,
                                    boolean nullable
                                   ) {
        if (nullable)
            return reader -> new JsArraySpecParser(keyDeserializers).nullOrArray(reader);
        else
            return reader -> new JsArraySpecParser(keyDeserializers).array(reader);
    }

    public JsSpecParser ofObjSpec(Vector<String> required,
                                  Map<String, JsSpecParser> keyDeserializers,
                                  boolean nullable,
                                  boolean strict
                                 ) {
        return reader ->
        {
            if (required.isEmpty()) {
                JsObjSpecParser parser = new JsObjSpecParser(strict,
                                                             keyDeserializers
                );
                if (nullable) return parser.nullOrValue(reader);
                else return parser.value(reader);
            }
            else {
                JsObjSpecWithRequiredKeysParser parser = new JsObjSpecWithRequiredKeysParser(required,
                                                                                             keyDeserializers,
                                                                                             strict
                );
                if (nullable) return parser.nullOrValue(reader);
                else return parser.value(reader);
            }

        };
    }

    public JsSpecParser ofArrayOfValueSuchThat(Function<JsArray, Optional<Error>> p,
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
        if (nullable)
            return parser::nullOrValue;
        else
            return parser::value;
    }

    public JsSpecParser ofArrayOfValue(boolean nullable) {
        return getParser(PARSERS.arrayOfValueParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfValueEachSuchThat(Function<JsValue, Optional<Error>> p,
                                                   boolean nullable
                                                  ) {
        if (nullable) return reader -> PARSERS.arrayOfValueParser.nullOrArrayEachSuchThat(reader,
                                                                                          p
                                                                                         );

        else return reader -> PARSERS.arrayOfValueParser.arrayEachSuchThat(reader,
                                                                           p
                                                                          );
    }

    public JsSpecParser ofValue() {
        return getParser(PARSERS.valueParser,
                         true
                        );
    }

    public JsSpecParser ofValueSuchThat(Function<JsValue, Optional<Error>> predicate) {
        return reader ->
        {
            JsValue value = PARSERS.valueParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                final Optional<Error> result = predicate.apply(value);
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
        if (nullable) return PARSERS.boolParser::nullOrTrue;
        else return PARSERS.boolParser::True;
    }

    public JsSpecParser ofFalse(boolean nullable) {
        if (nullable) return PARSERS.boolParser::nullOrFalse;
        else return PARSERS.boolParser::False;
    }

    public JsSpecParser ofArrayOfBool(boolean nullable
                                     ) {
        return getParser(PARSERS.arrayOfBoolParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfBoolSuchThat(Function<JsArray, Optional<Error>> p,
                                              boolean nullable
                                             ) {
        return getParser(PARSERS.arrayOfBoolParser,
                         p,
                         nullable
                        );

    }

    public JsSpecParser ofArrayOfStrEachSuchThat(Function<String, Optional<Error>> p,
                                                 boolean nullable
                                                ) {
        if (nullable) return reader ->
                PARSERS.arrayOfStrParser.nullOrArrayEachSuchThat(reader,
                                                                 p
                                                                );

        else return reader ->
                PARSERS.arrayOfStrParser.arrayEachSuchThat(reader,
                                                           p
                                                          );
    }

    public JsSpecParser ofArrayOfStrSuchThat(Function<JsArray, Optional<Error>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfStrParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfStr(boolean nullable
                                    ) {
        return getParser(PARSERS.arrayOfStrParser,
                         nullable
                        );
    }


    public JsSpecParser ofStr(boolean nullable) {
        return getParser(PARSERS.strParser,
                         nullable
                        );
    }

    public JsSpecParser ofStrSuchThat(Function<String, Optional<Error>> predicate,
                                      boolean nullable
                                     ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.strParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(JsValue::isStr,
                                v -> v.toJsStr().value,
                                predicate,
                                () -> new IllegalStateException("Internal error.JsStrDeserializer.nullOrValue didn't return neither null or a JsStr as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );
                return value;
            }
        };
        else return reader ->
        {
            JsStr value = PARSERS.strParser.value(reader);

            final Optional<Error> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );
        };
    }

    public JsSpecParser ofArrayOfNumber(boolean nullable
                                       ) {
        return getParser(PARSERS.arrayOfNumberParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfNumberEachSuchThat(Function<JsNumber, Optional<Error>> p,
                                                    boolean nullable
                                                   ) {
        if (nullable) return reader -> PARSERS.arrayOfNumberParser.nullOrArrayEachSuchThat(reader,
                                                                                           p
                                                                                          );

        else return reader -> PARSERS.arrayOfNumberParser.arrayEachSuchThat(reader,
                                                                            p
                                                                           );
    }

    public JsSpecParser ofArrayOfNumberSuchThat(Function<JsArray, Optional<Error>> p,
                                                boolean nullable
                                               ) {
        return getParser(PARSERS.arrayOfNumberParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfIntegralSuchThat(Function<JsArray, Optional<Error>> p,
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

    public JsSpecParser ofNumberSuchThat(Function<JsNumber, Optional<Error>> predicate,
                                         boolean nullable
                                        ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.numberParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(JsValue::isNumber,
                                v -> value.toJsNumber(),
                                predicate,
                                () -> new IllegalStateException("Internal error.JsNumberDeserializer.nullOrValue didn't return neither null or a JsNumber as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );
                return value;
            }

        };
        else return reader ->
        {
            JsNumber              value  = PARSERS.numberParser.value(reader);
            final Optional<Error> result = predicate.apply(value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );
        };

    }


    public JsSpecParser ofArrayOfIntegral(boolean nullable
                                         ) {
        return getParser(PARSERS.arrayOfIntegralParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfIntegralEachSuchThat(Function<BigInteger, Optional<Error>> p,
                                                      boolean nullable
                                                     ) {
        if (nullable) return reader ->
                PARSERS.arrayOfIntegralParser.nullOrArrayEachSuchThat(reader,
                                                                      p
                                                                     );

        else return reader ->
                PARSERS.arrayOfIntegralParser.arrayEachSuchThat(reader,
                                                                p
                                                               );
    }

    public JsSpecParser ofIntegral(boolean nullable) {
        return getParser(PARSERS.integralParser,
                         nullable
                        );
    }

    public JsSpecParser ofIntegralSuchThat(Function<BigInteger, Optional<Error>> predicate,
                                           boolean nullable
                                          ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.integralParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(JsValue::isBigInt,
                                v -> v.toJsBigInt().value,
                                predicate,
                                () -> new IllegalStateException("Internal error.JsIntegralDeserializer.nullOrValue didn't return neither null or a JsBigInt as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );
                return value;
            }
        };
        else return reader ->
        {
            JsBigInt              integral = PARSERS.integralParser.value(reader);
            final Optional<Error> result   = predicate.apply(integral.value);
            if (!result.isPresent()) return integral;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );

        };
    }

    public JsSpecParser ofArrayOfDecimal(boolean nullable
                                        ) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfDecimalEachSuchThat(Function<BigDecimal, Optional<Error>> p,
                                                     boolean nullable
                                                    ) {
        if (nullable) return reader ->
                PARSERS.arrayOfDecimalParser.nullOrArrayEachSuchThat(reader,
                                                                     p
                                                                    );

        else return reader ->
                PARSERS.arrayOfDecimalParser.arrayEachSuchThat(reader,
                                                               p
                                                              );
    }

    public JsSpecParser ofArrayOfDecimalSuchThat(Function<JsArray, Optional<Error>> p,
                                                 boolean nullable
                                                ) {
        return getParser(PARSERS.arrayOfDecimalParser,
                         p,
                         nullable
                        );

    }


    public JsSpecParser ofArrayOfLong(boolean nullable
                                     ) {
        return getParser(PARSERS.arrayOfLongParser,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfLongEachSuchThat(LongFunction<Optional<Error>> p,
                                                  boolean nullable
                                                 ) {
        if (nullable) return reader ->
                PARSERS.arrayOfLongParser.nullOrArrayEachSuchThat(reader,
                                                                  p
                                                                 );

        else return reader -> PARSERS.arrayOfLongParser.arrayEachSuchThat(reader,
                                                                          p
                                                                         );
    }

    public JsSpecParser ofArrayOfLongSuchThat(Function<JsArray, Optional<Error>> p,
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

    public JsSpecParser ofDecimalSuchThat(Function<BigDecimal, Optional<Error>> predicate,
                                          boolean nullable
                                         ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.decimalParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(JsValue::isDecimal,
                                v -> v.toJsBigDec().value,
                                predicate,
                                () -> new IllegalStateException("Internal error.JsDecimalDeserializer.nullOrValue didn't return neither null or a JsBigDec as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );

                return value;
            }
        };
        else
            return reader ->
            {
                JsBigDec              decimal = PARSERS.decimalParser.value(reader);
                final Optional<Error> result  = predicate.apply(decimal.value);
                if (!result.isPresent()) return decimal;
                else throw newParseException.apply(reader,
                                                   result.get()
                                                  );

            };
    }


    public JsSpecParser ofLong(boolean nullable) {
        return getParser(PARSERS.longParser,
                         nullable
                        );
    }

    public JsSpecParser ofLongSuchThat(LongFunction<Optional<Error>> predicate,
                                       boolean nullable
                                      ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.longParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(JsValue::isLong,
                                v -> v.toJsLong().value,
                                predicate::apply,
                                () -> new IllegalStateException("Internal error.JsLongDeserializer.nullOrValue didn't return neither null or a JsLong as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );
                return value;
            }
        };
        else return reader ->
        {
            JsLong                value  = PARSERS.longParser.value(reader);
            final Optional<Error> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );

        };
    }


    public JsSpecParser ofArrayOfInt(boolean nullable
                                    ) {
        return getParser(PARSERS.arrayOfIntParser,
                         nullable
                        );
    }


    public JsSpecParser ofArrayOfIntSuchThat(Function<JsArray, Optional<Error>> p,
                                             boolean nullable
                                            ) {
        return getParser(PARSERS.arrayOfIntParser,
                         p,
                         nullable
                        );
    }

    public JsSpecParser ofArrayOfIntEachSuchThat(IntFunction<Optional<Error>> p,
                                                 boolean nullable
                                                ) {

        if (nullable) return reader -> PARSERS.arrayOfIntParser.nullOrArrayEachSuchThat(reader,
                                                                                        p
                                                                                       );

        else return reader -> PARSERS.arrayOfIntParser.arrayEachSuchThat(reader,
                                                                         p
                                                                        );
    }

    public JsSpecParser ofBinary(boolean nullable) {
        return getParser(PARSERS.binaryParser,
                         nullable
                        );
    }

    public JsSpecParser ofBinarySuchThat(Function<byte[], Optional<Error>> predicate,
                                         boolean nullable
                                        ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.binaryParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(v -> value.isBinary(),
                                v -> v.toJsBinary().value,
                                predicate::apply,
                                () -> new IllegalStateException("JsBinaryDeserializer.nullOrValue didn't return neither null or a byte[] as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );

                return value;
            }

        };
        else return reader ->
        {
            JsBinary              value  = PARSERS.binaryParser.value(reader);
            final Optional<Error> result = predicate.apply(value.value);
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

    public JsSpecParser ofIntSuchThat(IntFunction<Optional<Error>> predicate,
                                      boolean nullable
                                     ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.intParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(v -> value.isInt(),
                                v -> v.toJsInt().value,
                                predicate::apply,
                                () -> new IllegalStateException("JsIntDeserializer.nullOrValue didn't return neither null or a Int as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );

                return value;
            }

        };
        else return reader ->
        {
            JsInt                 value  = PARSERS.intParser.value(reader);
            final Optional<Error> result = predicate.apply(value.value);
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

    public JsSpecParser ofInstantSuchThat(Function<Instant, Optional<Error>> predicate,
                                          boolean nullable
                                         ) {

        if (nullable) return reader ->
        {
            JsValue value = PARSERS.instantParser.nullOrValue(reader);
            if (value == JsNull.NULL) return value;
            else {
                testTypeAndSpec(v -> value.isInstant(),
                                v -> v.toJsInstant().value,
                                predicate::apply,
                                () -> new IllegalStateException("JsInstantDeserializer.nullOrValue didn't return neither null or an instant as expected.")
                               ).apply(value)
                                .ifPresent(e -> newParseException.apply(reader,
                                                                        e
                                                                       )
                                          );

                return value;
            }

        };
        else return reader ->
        {
            JsInstant             value  = PARSERS.instantParser.value(reader);
            final Optional<Error> result = predicate.apply(value.value);
            if (!result.isPresent()) return value;
            else throw newParseException.apply(reader,
                                               result.get()
                                              );

        };
    }
}
