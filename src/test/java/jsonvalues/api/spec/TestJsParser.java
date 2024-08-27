package jsonvalues.api.spec;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.spec.JsSpecs.array;
import static jsonvalues.spec.JsSpecs.arrayOfBigInt;
import static jsonvalues.spec.JsSpecs.arrayOfBigIntSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfBool;
import static jsonvalues.spec.JsSpecs.arrayOfBoolSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfDec;
import static jsonvalues.spec.JsSpecs.arrayOfDecSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfInt;
import static jsonvalues.spec.JsSpecs.arrayOfIntSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfLong;
import static jsonvalues.spec.JsSpecs.arrayOfLongSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfObj;
import static jsonvalues.spec.JsSpecs.arrayOfObjSuchThat;
import static jsonvalues.spec.JsSpecs.arrayOfSpec;
import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.arrayOfStrSuchThat;
import static jsonvalues.spec.JsSpecs.bigInteger;
import static jsonvalues.spec.JsSpecs.binary;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.instant;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.longInteger;
import static jsonvalues.spec.JsSpecs.obj;
import static jsonvalues.spec.JsSpecs.str;
import static jsonvalues.spec.JsSpecs.tuple;

import fun.gen.Combinators;
import fun.gen.Gen;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPrimitive;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsBigIntGen;
import jsonvalues.gen.JsBinaryGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsInstantGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.ArraySchema;
import jsonvalues.spec.JsArraySpecParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsParserException;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsParser {

  final JsObj example = JsObj.of("a",
                                 JsStr.of("001"),
                                 "b",
                                 JsInt.of(10),
                                 "c",
                                 JsBool.TRUE,
                                 "d",
                                 JsLong.of(10),
                                 "e",
                                 JsBigDec.of(BigDecimal.TEN),
                                 "f",
                                 JsArray.of(1.5,
                                            1.5
                                           ),
                                 "g",
                                 JsBigInt.of(BigInteger.TEN),
                                 "h",
                                 JsObj.empty(),
                                 "i",
                                 JsArray.empty(),
                                 "j",
                                 JsArray.of(JsObj.of("a",
                                                     JsStr.of("hi")
                                                    ))
                                );
  JsObjSpec objSpec = JsObjSpec.of("a",
                                   str(),
                                   "b",
                                   integer(),
                                   "c",
                                   bool(),
                                   "d",
                                   longInteger(),
                                   "e",
                                   decimal(),
                                   "f",
                                   tuple(decimal(),
                                         decimal()
                                        ),
                                   "g",
                                   bigInteger(),
                                   "h",
                                   obj(),
                                   "i",
                                   array(),
                                   "j",
                                   arrayOfSpec(JsObjSpec.of("a",
                                                            str()
                                                           )
                                                        .lenient())
                                  );

  @Test
  public void test_parse_string_into_obj() throws IOException {

    final JsObjSpecParser parser = JsObjSpecParser.of(objSpec);

    Assertions.assertEquals(parser.parse(example.toString()),
                            example
                           );

    byte[] bytes = example.toString()
                          .getBytes(StandardCharsets.UTF_8);
    Assertions.assertEquals(parser.parse(bytes),
                            example
                           );

    InputStream stream = new ByteArrayInputStream(bytes);

    Assertions.assertEquals(parser.parse(stream),
                            example
                           );

    stream.close();

  }

  @Test
  public void testJsArray() throws IOException {
    JsArray array = JsArray.of(example,
                               example);

    JsSpec spec = JsSpecs.arrayOfSpec(objSpec,
                                      ArraySchema.sizeBetween(1,
                                                              5));

    JsArraySpecParser parser = JsArraySpecParser.of(spec);

    Assertions.assertEquals(parser.parse(array.toString()),
                            array
                           );

    byte[] bytes = array.toString()
                        .getBytes(StandardCharsets.UTF_8);

    Assertions.assertEquals(parser.parse(bytes),
                            array
                           );

    InputStream stream = new ByteArrayInputStream(bytes);

    Assertions.assertEquals(parser.parse(stream),
                            array
                           );

    stream.close();


  }


  @Test
  public void testParseInstant() {

    JsObjSpecParser parser =
        JsObjSpecParser.of(JsObjSpec.of("a",
                                        instant(),
                                        "b",
                                        instant(i -> i.isAfter(Instant.now()
                                                                      .minus(Duration.ofDays(1))
                                                              )
                                               )));

    JsObj obj = JsObj.of("a",
                         JsInstant.of(Instant.now()),
                         "b",
                         JsInstant.of(Instant.now())
                        );
    JsObj parse = parser.parse(obj.toString());

    Assertions.assertEquals(JsInstant.class,
                            parse.get("a")
                                 .getClass()
                           );
    Assertions.assertEquals(JsInstant.class,
                            parse.get("b")
                                 .getClass()
                           );

    Assertions.assertEquals(obj,
                            parse);
  }


  @Test
  public void testParseBinary() {

    JsObjSpecParser parser =
        JsObjSpecParser.of(JsObjSpec.of("a",
                                        binary(),
                                        "b",
                                        binary(i -> i.length <= 1024)
                                       ));

    JsObj obj = JsObj.of("a",
                         JsStr.of("hola"),
                         "b",
                         JsBinary.of("foo".getBytes(StandardCharsets.UTF_8))
                        );
    JsObj parse = parser.parse(obj.toString());

    Assertions.assertEquals(JsBinary.class,
                            parse.get("a")
                                 .getClass()
                           );
    Assertions.assertEquals(JsBinary.class,
                            parse.get("b")
                                 .getClass()
                           );

    Assertions.assertEquals(obj,
                            parse);
  }

  @Test
  public void testPredicates() {

    JsObjSpec spec = JsObjSpec.of(
        "a",
        JsSpecs.longInteger(i -> i > Integer.MAX_VALUE)
               .nullable(),
        "b",
        JsSpecs.integer(i -> i > 2)
               .nullable(),
        "c",
        JsSpecs.str(s -> s.startsWith("h"))
               .nullable(),
        "d",
        JsSpecs.array(JsValue::isNumber)
               .nullable()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObj a = JsObj.of("a",
                       JsNull.NULL,
                       "b",
                       JsNull.NULL,
                       "c",
                       JsNull.NULL,
                       "d",
                       JsNull.NULL);
    JsObj b = JsObj.of("a",
                       JsLong.of(Long.MAX_VALUE),
                       "b",
                       JsInt.of(20),
                       "c",
                       JsStr.of("hola"),
                       "d",
                       JsArray.of(1,
                                  Long.MAX_VALUE));

    Assertions.assertEquals(a,
                            parser.parse(a.toString()));
    Assertions.assertEquals(b,
                            parser.parse(b.toString()));


  }

  @Test
  public void test_parsing_nullable_arrays() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.arrayOfDec(ArraySchema.withMaxSize(1))
                                         .nullable(),
                                  "b",
                                  JsSpecs.arrayOfInt(ArraySchema.withMaxSize(1))
                                         .nullable(),
                                  "c",
                                  JsSpecs.array()
                                         .nullable()
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObjGen gen = JsObjGen.of("a",
                               JsArrayGen.arbitrary(JsBigDecGen.arbitrary(),
                                                    0,
                                                    1),
                               "b",
                               JsArrayGen.arbitrary(JsIntGen.arbitrary(),
                                                    0,
                                                    1),
                               "c",
                               JsArrayGen.arbitrary(JsLongGen.arbitrary(),
                                                    0,
                                                    1)
                              )
                           .withNullValues("a",
                                           "b",
                                           "c");

    Assertions.assertTrue(gen.sample(10000)
                             .allMatch(obj -> parser.parse(obj.toString())
                                                    .equals(obj)));


  }

  @Test
  public void parsingString() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  str(s -> s.length() < 10).nullable(),
                                  "b",
                                  arrayOfStr(s -> s.length() < 10,
                                             ArraySchema.sizeBetween(1,
                                                                     10)).nullable(),
                                  "c",
                                  arrayOfStr(ArraySchema.sizeBetween(1,
                                                                     10)).nullable(),
                                  "d",
                                  arrayOfStrSuchThat(a -> a.size() < 11 && !a.isEmpty()).nullable()
                                 )
                              .withAllOptKeys();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsStr> strGen = JsStrGen.arbitrary(0,
                                           12);

    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(strGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               strGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> validGen = gen.suchThat(spec);

    Assertions.assertTrue(validGen.sample(10000)
                                  .allMatch(obj ->
                                                parser.parse(obj.toPrettyString())
                                                      .equals(obj)

                                           ));
  }

  @Test
  public void parseStringErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  str(s -> s.length() < 10),
                                  "b",
                                  arrayOfStr(s -> s.length() < 10,
                                             ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "c",
                                  arrayOfStr(ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "d",
                                  arrayOfStrSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 )
                              .withAllOptKeys();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsStr> strGen = JsStrGen.arbitrary(0,
                                           12);
    Gen<JsPrimitive> valueGen = Combinators.oneOf(strGen,
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsInt.of(10)));
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec);

    Assertions.assertTrue(invalidGen.sample(10000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }

  @Test
  public void parseIntErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  integer(s -> s < 10),
                                  "b",
                                  arrayOfInt(s -> s < 10,
                                             ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "c",
                                  arrayOfInt(ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "d",
                                  arrayOfIntSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsInt> intGen = JsIntGen.arbitrary(0,
                                           12);
    Gen<JsPrimitive> valueGen = Combinators.oneOf(intGen,
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsLong.of(Long.MAX_VALUE)),
                                                  Gen.cons(JsDouble.of(10.5)),
                                                  Gen.cons(JsBigDec.of(new BigDecimal("11111.1111"))),
                                                  Gen.cons(JsBigInt.of(new BigInteger("111111111111111111111111111111111111111111111111"))));
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec);

    Assertions.assertTrue(invalidGen.sample(100000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }

  @Test
  public void parsingInt() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.integer(s -> s < 10)
                                         .nullable(),
                                  "b",
                                  arrayOfInt(s -> s < 10,
                                             ArraySchema.sizeBetween(1,
                                                                     10)).nullable(),
                                  "c",
                                  arrayOfInt(ArraySchema.sizeBetween(1,
                                                                     10)).nullable(),
                                  "d",
                                  arrayOfIntSuchThat(a -> a.size() < 11 && !a.isEmpty()).nullable()
                                 )
                              .withAllOptKeys();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsInt> intGen = JsIntGen.arbitrary(0,
                                           12);

    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(intGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               intGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> validGen = gen.suchThat(spec);

    Assertions.assertTrue(validGen.sample(10000)
                                  .allMatch(obj ->
                                                parser.parse(obj.toPrettyString())
                                                      .equals(obj)

                                           ));
  }

  @Test
  public void parsingBool() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.oneValOf(List.of(TRUE)),
                                  "b",
                                  JsSpecs.oneValOf(List.of(FALSE)),
                                  "c",
                                  arrayOfBool(ArraySchema.sizeBetween(1,
                                                                      10)).nullable(),
                                  "d",
                                  arrayOfBoolSuchThat(a -> a.size() < 11 && !a.isEmpty()).nullable()
                                 )
                              .withAllOptKeys();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsBool> boolGen = JsBoolGen.arbitrary();

    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(boolGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               boolGen,
                               "b",
                               boolGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> validGen = gen.suchThat(spec);

    Assertions.assertTrue(validGen.sample(10000)
                                  .allMatch(obj ->
                                                parser.parse(obj.toPrettyString())
                                                      .equals(obj)

                                           ));
  }

  @Test
  public void parseBoolErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  bool(),
                                  "b",
                                  arrayOfBool(ArraySchema.sizeBetween(1,
                                                                      10)),
                                  "c",
                                  arrayOfBoolSuchThat(a -> a.size() < 11 && !a.isEmpty()),
                                  "d",
                                  JsSpecs.oneValOf(List.of(TRUE)),
                                  "e",
                                  JsSpecs.oneValOf(List.of(FALSE))
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsPrimitive> valueGen = Combinators.oneOf(JsBoolGen.arbitrary(),
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsLong.of(Long.MAX_VALUE)),
                                                  Gen.cons(JsDouble.of(10.5)),
                                                  Gen.cons(JsBigDec.of(new BigDecimal("11111.1111"))),
                                                  Gen.cons(JsBigInt.of(new BigInteger("111111111111111111111111111111111111111111111111"))));
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               JsBoolGen.arbitrary(),
                               "e",
                               JsBoolGen.arbitrary()
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec);

    Assertions.assertTrue(invalidGen.sample(10000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        System.out.println(obj);
                                        throw new RuntimeException("Should not parse: \n"+obj.toPrettyString());
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }


  @Test
  public void parseLongErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  longInteger(s -> s < 10),
                                  "b",
                                  arrayOfLong(s -> s < 10,
                                              ArraySchema.sizeBetween(1,
                                                                      10)),
                                  "c",
                                  arrayOfLong(ArraySchema.sizeBetween(1,
                                                                      10)),
                                  "d",
                                  arrayOfLongSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsLong> longGen = JsLongGen.arbitrary(0,
                                              12);
    Gen<JsPrimitive> valueGen = Combinators.oneOf(longGen,
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsDouble.of(10.5)),
                                                  Gen.cons(JsBigDec.of(new BigDecimal("11111.1111"))),
                                                  Gen.cons(JsBigInt.of(new BigInteger("111111111111111111111111111111111111111111111111"))));
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec,
                                           100);

    Assertions.assertTrue(invalidGen.sample(10000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }

  @Test
  public void parseInstantAndBinaryErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  instant(s -> s.isAfter(Instant.EPOCH)),
                                  "b",
                                  instant(),
                                  "c",
                                  binary(),
                                  "d",
                                  binary(b -> b.length <= 1024)
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsInstant> instantGen = JsInstantGen.biased(0,
                                                    Instant.MAX.getEpochSecond());

    Gen<JsBinary> binaryGen = JsBinaryGen.biased(0,
                                                 1025);

    Gen<JsPrimitive> valueGen = Combinators.oneOf(instantGen,
                                                  binaryGen,
                                                  Gen.cons(JsStr.of("a")));

    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               valueGen,
                               "c",
                               valueGen,
                               "d",
                               valueGen
                              )
                           .withAllNullValues()
                           .withAllOptKeys();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec,
                                           1000);

    Assertions.assertTrue(invalidGen.sample(1000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));


  }


  @Test
  public void parseDecErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  decimal(s -> s.compareTo(BigDecimal.TEN) < 0),
                                  "b",
                                  arrayOfDec(s -> s.compareTo(BigDecimal.TEN) < 0,
                                             ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "c",
                                  arrayOfDec(ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "d",
                                  arrayOfDecSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsBigDec> decGen = JsBigDecGen.arbitrary(BigDecimal.ZERO,
                                                 BigDecimal.valueOf(12));
    Gen<JsPrimitive> valueGen = Combinators.oneOf(decGen,
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsStr.of("10.5")),
                                                  Gen.cons(JsBool.TRUE)
                                                 );
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec,
                                           100);

    Assertions.assertTrue(invalidGen.sample(10000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }


  @Test
  public void parseBigIntErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  bigInteger(s -> s.compareTo(BigInteger.TEN) < 0),
                                  "b",
                                  arrayOfBigInt(s -> s.compareTo(BigInteger.TEN) < 0,
                                                ArraySchema.sizeBetween(1,
                                                                        10)),
                                  "c",
                                  arrayOfBigInt(ArraySchema.sizeBetween(1,
                                                                        10)),
                                  "d",
                                  arrayOfBigIntSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsBigInt> bigIntGen = JsBigIntGen.biased();
    Gen<JsPrimitive> valueGen = Combinators.oneOf(bigIntGen,
                                                  Gen.cons(JsNull.NULL),
                                                  Gen.cons(JsBool.TRUE),
                                                  Gen.cons(JsDouble.of(1.5)),
                                                  Gen.cons(JsStr.of("10.5")),
                                                  Gen.cons(JsBool.TRUE)
                                                 );
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec,
                                           100);

    Assertions.assertTrue(invalidGen.sample(10000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }

  @Test
  public void parseJsObErrors() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  obj(s -> s.containsKey("a")),
                                  "b",
                                  arrayOfObj(s -> s.containsKey("a"),
                                             ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "c",
                                  arrayOfObj(ArraySchema.sizeBetween(1,
                                                                     10)),
                                  "d",
                                  arrayOfObjSuchThat(a -> a.size() < 11 && !a.isEmpty())
                                 );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Gen<JsObj> objGen = JsObjGen.of("a",
                                    JsBoolGen.arbitrary(),
                                    "b",
                                    JsStrGen.alphabetic())
                                .withOptKeys("a");
    Gen<JsValue> valueGen = Combinators.oneOf(objGen,
                                              Gen.cons(JsNull.NULL),
                                              Gen.cons(JsBool.TRUE),
                                              Gen.cons(JsDouble.of(1.5)),
                                              Gen.cons(JsStr.of("10.5")),
                                              Gen.cons(JsBool.TRUE)
                                             );
    Gen<JsArray> arrayGen = JsArrayGen.arbitrary(valueGen,
                                                 0,
                                                 20);
    JsObjGen gen = JsObjGen.of("a",
                               valueGen,
                               "b",
                               arrayGen,
                               "c",
                               arrayGen,
                               "d",
                               arrayGen
                              )
                           .withAllOptKeys()
                           .withAllNullValues();

    Gen<JsObj> invalidGen = gen.suchThatNo(spec,
                                           100);

    Assertions.assertTrue(invalidGen.sample(1000)
                                    .allMatch(obj -> {
                                      try {
                                        parser.parse(obj.toPrettyString());
                                        return false;
                                      } catch (JsParserException e) {
                                        return true;
                                      }
                                    }));
  }

}
