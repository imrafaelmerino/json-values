package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;
import static jsonvalues.spec.JsSpecs.tuple;

import fun.gen.Combinators;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsInstantGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpecToGenTests {


  @Test
  public void test_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            str(),
                            "b",
                            decimal(),
                            "c",
                            JsSpecs.doubleNumber(),
                            "d",
                            integer(),
                            "e",
                            JsSpecs.longInteger(),
                            "f",
                            JsSpecs.bool(),
                            "g",
                            JsSpecs.instant(),
                            "h",
                            JsObjSpec.of("a",
                                         str(),
                                         "b",
                                         decimal(),
                                         "c",
                                         JsSpecs.doubleNumber(),
                                         "d",
                                         integer(),
                                         "e",
                                         JsSpecs.longInteger(),
                                         "f",
                                         JsSpecs.bool(),
                                         "g",
                                         JsSpecs.instant(),
                                         "h",
                                         decimal(),
                                         "i",
                                         JsSpecs.bigInteger(),
                                         "j",
                                         JsSpecs.binary()
                                        )
                           );

    var gen = SpecToGen.DEFAULT.convert(spec);
    var parser = JsObjSpecParser.of(spec);

    gen.sample(100)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));

         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_primitives_with_constraints() {

    var spec = JsObjSpec.of("a",
                            str(StrSchema.withLength(0,
                                                     10)),
                            "b",
                            decimal(DecimalSchema.between(BigDecimal.ZERO,
                                                          BigDecimal.TEN)
                                   ),
                            "c",
                            JsSpecs.doubleNumber(DoubleSchema.between(0.0,
                                                                      10.0)),
                            "d",
                            integer(IntegerSchema.between(0,
                                                          10)),
                            "e",
                            JsSpecs.longInteger(LongSchema.between(0,
                                                                   10)),
                            "f",
                            JsSpecs.bool(),
                            "g",
                            JsSpecs.instant(InstantSchema.between(Instant.EPOCH,
                                                                  Instant.EPOCH.plus(365,
                                                                                     ChronoUnit.DAYS)
                                                                 )
                                           ),
                            "h",
                            JsObjSpec.of("a",
                                         str(StrSchema.withLength(0,
                                                                  10)),
                                         "b",
                                         decimal(DecimalSchema.between(BigDecimal.ZERO,
                                                                       BigDecimal.TEN)),
                                         "c",
                                         JsSpecs.doubleNumber(DoubleSchema.between(0.0,
                                                                                   10.0)),
                                         "d",
                                         integer(IntegerSchema.between(0,
                                                                       10)
                                                ),
                                         "e",
                                         JsSpecs.longInteger(LongSchema.between(0,
                                                                                10)),
                                         "f",
                                         JsSpecs.bool(),
                                         "g",
                                         JsSpecs.instant(InstantSchema.between(Instant.EPOCH,
                                                                               Instant.EPOCH.plus(365,
                                                                                                  ChronoUnit.DAYS)
                                                                              )),
                                         "h",
                                         decimal(DecimalSchema.between(BigDecimal.ZERO,
                                                                       BigDecimal.TEN)),
                                         "i",
                                         JsSpecs.bigInteger(BigIntSchema.between(new BigInteger("10000000000000000"),
                                                                                 new BigInteger("20000000000000000")
                                                                                )
                                                           ),
                                         "j",
                                         JsSpecs.binary()
                                        )
                           );

    var gen = SpecToGen.DEFAULT.convert(spec);
    var parser = JsObjSpecParser.of(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));

         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_nullable_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            str()
                                .nullable(),
                            "b",
                            decimal()
                                .nullable(),
                            "c",
                            JsSpecs.doubleNumber()
                                   .nullable(),
                            "d",
                            integer()
                                .nullable(),
                            "e",
                            JsSpecs.longInteger()
                                   .nullable(),
                            "f",
                            JsSpecs.bool()
                                   .nullable(),
                            "g",
                            JsSpecs.instant()
                                   .nullable(),
                            "h",
                            JsObjSpec.of("a",
                                         str()
                                             .nullable(),
                                         "b",
                                         decimal()
                                             .nullable(),
                                         "c",
                                         JsSpecs.doubleNumber()
                                                .nullable(),
                                         "d",
                                         integer()
                                             .nullable(),
                                         "e",
                                         JsSpecs.longInteger()
                                                .nullable(),
                                         "f",
                                         JsSpecs.bool()
                                                .nullable(),
                                         "g",
                                         JsSpecs.instant()
                                                .nullable(),
                                         "h",
                                         decimal()
                                             .nullable(),
                                         "i",
                                         JsSpecs.bigInteger()
                                                .nullable(),
                                         "j",
                                         JsSpecs.binary()
                                                .nullable()
                                        )
                                     .nullable()
                           );

    var gen = SpecToGen.DEFAULT.convert(spec);
    var parser = JsObjSpecParser.of(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));

         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_array_of_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            arrayOfStr(),
                            "b",
                            JsSpecs.arrayOfDec(),
                            "c",
                            JsSpecs.arrayOfDouble(),
                            "d",
                            JsSpecs.arrayOfInt(),
                            "e",
                            JsSpecs.arrayOfLong(),
                            "f",
                            JsSpecs.arrayOfBool(),
                            "g",
                            JsSpecs.arrayOfSpec(JsSpecs.instant()),
                            "h",
                            JsObjSpec.of("a",
                                         arrayOfStr(),
                                         "b",
                                         JsSpecs.arrayOfDec(),
                                         "c",
                                         JsSpecs.arrayOfDouble(),
                                         "d",
                                         JsSpecs.arrayOfInt(),
                                         "e",
                                         JsSpecs.arrayOfLong(),
                                         "f",
                                         JsSpecs.arrayOfBool(),
                                         "g",
                                         JsSpecs.arrayOfSpec(JsSpecs.instant()),
                                         "h",
                                         JsSpecs.arrayOfDec(),
                                         "i",
                                         JsSpecs.arrayOfBigInt(),
                                         "j",
                                         JsSpecs.arrayOfSpec(JsSpecs.binary()
                                                            )
                                        )
                           );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.DEFAULT.convert(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_array_of_primitives_with_constraints() {

    var spec = JsObjSpec.of("a",
                            arrayOfStr(StrSchema.withLength(0,
                                                            10)),
                            "b",
                            JsSpecs.arrayOfDec(DecimalSchema.between(BigDecimal.ZERO,
                                                                     BigDecimal.TEN)),
                            "c",
                            JsSpecs.arrayOfDouble(DoubleSchema.between(0.0,
                                                                       10.0)),
                            "d",
                            JsSpecs.arrayOfInt(IntegerSchema.between(0,
                                                                     10)),
                            "e",
                            JsSpecs.arrayOfLong(LongSchema.between(0,
                                                                   10)),
                            "f",
                            JsSpecs.arrayOfBool(),
                            "g",
                            JsSpecs.arrayOfSpec(JsSpecs.instant(InstantSchema.between(Instant.EPOCH,
                                                                                      Instant.EPOCH.plus(365,
                                                                                                         ChronoUnit.DAYS)
                                                                                     )
                                                               )
                                               ),
                            "h",
                            JsObjSpec.of("a",
                                         arrayOfStr(StrSchema.withLength(0,
                                                                         10)),
                                         "b",
                                         JsSpecs.arrayOfDec(DecimalSchema.between(BigDecimal.ZERO,
                                                                                  BigDecimal.TEN)),
                                         "c",
                                         JsSpecs.arrayOfDouble(DoubleSchema.between(0.0,
                                                                                    10.0)),
                                         "d",
                                         JsSpecs.arrayOfInt(IntegerSchema.between(0,
                                                                                  10)),
                                         "e",
                                         JsSpecs.arrayOfLong(LongSchema.between(0,
                                                                                10)),
                                         "f",
                                         JsSpecs.arrayOfBool(),
                                         "g",
                                         JsSpecs.arrayOfSpec(JsSpecs.instant()),
                                         "i",
                                         JsSpecs.arrayOfBigInt(BigIntSchema.between(new BigInteger("10000000000000000"),
                                                                                    new BigInteger("20000000000000000"))),
                                         "j",
                                         JsSpecs.arrayOfSpec(JsSpecs.binary()
                                                            )
                                        )
                           );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.DEFAULT.convert(spec);

    String xs = "{\"a\":[\"b4395Jz3\",\"u46QR5b\",\"PW70c5290\",\"o8P7cS\",\"7ZqL\",\"7Z9Y19400s\",\"\",\"3AA30F084\",\"64me5\",\"x\",\"9d0tZbeav\",\"1e2426rO9\",\"4iLjQ8P6U4\",\"2\",\"\",\"80x2X64\",\"338\",\"01J6omrCT\",\"\",\"1\",\"57F40Ps0\",\"B0lJB7A\",\"4lW\",\"5\",\"R3N\",\"i054GC28l0\",\"caq6\",\"gR\",\"p18Y084\",\"\",\"tH4D\",\"89Qf4\",\"pJL55I9\",\"J8\",\"\",\"v3WJ11O\",\"23SJ9936\",\"1i3\",\"Dj\",\"\",\"z12bd2h\",\"mT1ygV4P\",\"301y\",\"13\",\"V377j\",\"2\",\"vwsh85e\",\"s2Q\",\"7X60\",\"563pH102R7\",\"29\",\"o1yWIde\",\"0bKg47f\",\"0lT892x2\",\"64tc3\"],\"b\":[0,0,0,0,0,2.61,8.32],\"c\":[0.0,0.0,0.11759551430205017,0.0,3.242733496828464,3.907891010307485,5.40405791080767,10.0,4.211979371438392,0.4486362144427891,9.873575826724856,10.0,0.0,0.0,10.0,8.4250628917738,1.9979983179720662,7.6469851750609585,6.105687534570046,8.028618570967833],\"d\":[0,0,4,0],\"e\":[5,0,9],\"f\":[true,false,false,false,true,false,true,false,true,false,false,false,false,false,false,false,true,true,false,true,true,false,false,false,true,false,true,true,false,false,false,true,false,true,true,false,true,false,false,true,false,false,true,false,true,true,true,true,false,false,false,true,true,false,false,true,true,false,true,false,false,false,false,false,true],\"g\":[\"1970-10-04T21:44:35Z\",\"1971-01-01T00:00:00Z\",\"1971-01-01T00:00:00Z\",\"1971-01-01T00:00:00Z\",\"1970-01-01T00:00:00Z\",\"1970-12-20T05:13:56Z\",\"1970-01-01T00:00:00Z\",\"1970-09-10T16:56:39Z\",\"1970-03-13T02:19:17Z\",\"1970-01-01T00:00:00Z\",\"1970-08-22T16:44:40Z\",\"1970-01-01T00:00:00Z\",\"1970-01-01T00:00:00Z\",\"1970-01-01T00:00:00Z\",\"1970-05-31T19:44:58Z\",\"1971-01-01T00:00:00Z\",\"1970-01-01T00:00:00Z\"],\"h\":{\"a\":[],\"b\":[8.07,10,0,10,7.93,10,5.45,4.20,8.74,10,10],\"c\":[5.841745668525272,0.0,0.0,0.0,9.683044000845348,9.005224553493946,0.0,0.0,10.0,0.0,0.0,0.0,0.8493887708766223,0.0,10.0,0.0,0.0,3.4761138109612864,0.0,10.0,5.283830066678784,9.899566492509187,7.290568782574582,0.0],\"d\":[10,0,2,5,1,1,0,10,2,0,0,10,10,10,10,0,0,9,10,10,0,0,9,10,2,10,5,8,1,8,0,6,0,0,0,3],\"e\":[0,4,4,0,9,8,0,3,3,10,0,10,2,5,7,0,7,2,10,0,10,0,1,0,0,0,9,3,0,0,2,10,8,8,10,0,10,10,8,3,1,10,0,6,0,0,0,10,10,3,0,10,10,10,10,6,0,9,7,8],\"f\":[false,false,true,false,true,true,false,false,false,true,true,true,false,true,true,true,false,true,true,false,false,true,true,true,true,true,true,true,false,false,false,true,false,false,true,true,true,false,false,true,false,true,true,false,false,false,true,true,false,false,true,false,true,true,true,false,true,false,false,false,true,false,false,true,false,true,false,true,false,false,true,false,true,true,false,true,false,false,false],\"g\":[],\"i\":[20000000000000000,11921736416164099,11533176505135556,15999779660558580],\"j\":[\"goyxummv\",\"93Gug7kSXg==\",\"\",\"u7YYLm3kgOeA\",\"MAnqAlrcZOc=\",\"em8=\",\"Bv8=\",\"\",\"jZhWV11jig==\",\"hocSdn7h+4V8\",\"t5c=\",\"17o=\",\"TQ==\",\"sm6i5UQ=\",\"5FUIANVXikM=\",\"5ZQIjclqHuTI\",\"TR77tGki4w==\",\"Q4kZYZ9xavij\",\"Lj899pY=\",\"Dg==\",\"gJPCcg==\",\"mSZw\",\"NWeGIvpa\",\"cj/dKIbpb4qiGQ==\",\"cZuKV0dCgN7Eiw==\",\"gD7VgSIR\",\"SFEUx+8g\",\"CL4zyQ==\",\"TuEmkPw6/bXokQ==\",\"O9l1AhalmILz\",\"bb0=\",\"Tjaxa0gMNG62nA==\",\"dQ==\",\"ocmMQ1ClKA==\",\"8lFL5dUR4g+D+A==\",\"M9tS0w==\",\"Qsg=\",\"+YdfOe14j/OQ\",\"\",\"Sg==\"]}}\n";

    parser.parse(xs);

    gen.sample(1000)
       .forEach(generated -> {
         String json = generated.toString();
         Assertions.assertEquals(generated,
                                 parser.parse(json));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_nullable_array_of_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            arrayOfStr()
                                .nullable(),
                            "b",
                            JsSpecs.arrayOfDec()
                                   .nullable(),
                            "c",
                            JsSpecs.arrayOfDouble()
                                   .nullable(),
                            "d",
                            JsSpecs.arrayOfInt()
                                   .nullable(),
                            "e",
                            JsSpecs.arrayOfLong()
                                   .nullable(),
                            "f",
                            JsSpecs.arrayOfBool()
                                   .nullable(),
                            "g",
                            JsSpecs.arrayOfSpec(JsSpecs.instant())
                                   .nullable(),
                            "h",
                            JsObjSpec.of("a",
                                         arrayOfStr()
                                             .nullable(),
                                         "b",
                                         JsSpecs.arrayOfDec()
                                                .nullable(),
                                         "c",
                                         JsSpecs.arrayOfDouble()
                                                .nullable(),
                                         "d",
                                         JsSpecs.arrayOfInt()
                                                .nullable(),
                                         "e",
                                         JsSpecs.arrayOfLong()
                                                .nullable(),
                                         "f",
                                         JsSpecs.arrayOfBool()
                                                .nullable(),
                                         "g",
                                         JsSpecs.arrayOfSpec(JsSpecs.instant())
                                                .nullable(),
                                         "h",
                                         JsSpecs.arrayOfDec()
                                                .nullable(),
                                         "i",
                                         JsSpecs.arrayOfBigInt()
                                                .nullable(),
                                         "j",
                                         JsSpecs.arrayOfSpec(JsSpecs.binary()
                                                            )
                                                .nullable()
                                        )
                           );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.DEFAULT.convert(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_maps() {
    JsObjSpec spec =
        JsObjSpec.of("a",
                     JsSpecs.mapOfStr(),
                     "b",
                     JsSpecs.mapOfDecimal(),
                     "c",
                     JsSpecs.mapOfDouble(),
                     "d",
                     JsSpecs.mapOfInteger(),
                     "e",
                     JsSpecs.mapOfLong(),
                     "f",
                     JsSpecs.mapOfBool(),
                     "g",
                     JsSpecs.mapOfSpec(JsSpecs.instant()),
                     "h",
                     JsObjSpec.of("a",
                                  JsSpecs.mapOfStr(),
                                  "b",
                                  JsSpecs.mapOfDecimal(),
                                  "c",
                                  JsSpecs.mapOfDouble(),
                                  "d",
                                  JsSpecs.mapOfInteger(),
                                  "e",
                                  JsSpecs.mapOfLong(),
                                  "f",
                                  JsSpecs.mapOfBool(),
                                  "g",
                                  JsSpecs.mapOfSpec(JsSpecs.instant()),
                                  "h",
                                  JsSpecs.mapOfDecimal(),
                                  "i",
                                  JsSpecs.mapOfBigInteger(),
                                  "j",
                                  JsSpecs.mapOfSpec(JsSpecs.binary())
                                 )
                    );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.DEFAULT.convert(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });


  }

  @Test
  public void test_nullable_maps() {
    JsObjSpec spec =
        JsObjSpec.of("a",
                     JsSpecs.mapOfStr()
                            .nullable(),
                     "b",
                     JsSpecs.mapOfDecimal()
                            .nullable(),
                     "c",
                     JsSpecs.mapOfDouble()
                            .nullable(),
                     "d",
                     JsSpecs.mapOfInteger()
                            .nullable(),
                     "e",
                     JsSpecs.mapOfLong()
                            .nullable(),
                     "f",
                     JsSpecs.mapOfBool()
                            .nullable(),
                     "g",
                     JsSpecs.mapOfSpec(JsSpecs.instant())
                            .nullable(),
                     "h",
                     JsObjSpec.of("a",
                                  JsSpecs.mapOfStr()
                                         .nullable(),
                                  "b",
                                  JsSpecs.mapOfDecimal()
                                         .nullable(),
                                  "c",
                                  JsSpecs.mapOfDouble()
                                         .nullable(),
                                  "d",
                                  JsSpecs.mapOfInteger()
                                         .nullable(),
                                  "e",
                                  JsSpecs.mapOfLong()
                                         .nullable(),
                                  "f",
                                  JsSpecs.mapOfBool()
                                         .nullable(),
                                  "g",
                                  JsSpecs.mapOfSpec(JsSpecs.instant())
                                         .nullable(),
                                  "h",
                                  JsSpecs.mapOfDecimal()
                                         .nullable(),
                                  "i",
                                  JsSpecs.mapOfBigInteger()
                                         .nullable(),
                                  "j",
                                  JsSpecs.mapOfSpec(JsSpecs.binary())
                                         .nullable()
                                 )
                    );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.DEFAULT.convert(spec);

    gen.sample(1000)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });


  }

  @Test
  public void testOverride() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  tuple(str(),
                                        integer()
                                       )
                                 );

    JsPath path = JsPath.empty()
                        .key("a")
                        .index(0);
    var gen =
        SpecToGen.DEFAULT.convert(spec,
                                  Map.of(path,
                                         JsStrGen.alphabetic(1000,
                                                             2000))
                                 );

    Assertions.assertTrue(gen.sample(1000)
                             .allMatch(json -> {
                               String str = json.getStr(path);
                               int length = str
                                   .length();
                               return length >= 1000 && length <= 2000;
                             }));
  }

  @Test
  public void testCustomOptions() {
    JsObjSpec spec =
        JsObjSpec.of("name",
                     str(),
                     "languages",
                     arrayOfStr(),
                     "age",
                     integer(),
                     "address",
                     JsObjSpec.of("street",
                                  str(),
                                  "coordinates",
                                  tuple(decimal(),
                                        decimal()
                                       )
                                 )
                    )
                 .withOptKeys("address");

    var gen = SpecToGen.DEFAULT.convert(spec);

    var gen1 = SpecToGen.of(new SpecGenConfBuilder().withIntSize(10,
                                                                 100)
                                                    .withStringLength(10,
                                                                      100)
                                                    .withOptionalKeyProbability(4))
                        .convert(spec);

    var gen2 = SpecToGen.of(new SpecGenConfBuilder().withIntSize(10,
                                                                 100)
                                                    .withStringLength(10,
                                                                      100)
                                                    .withOptionalKeyProbability(4))
                        .convert(spec,
                                 Map.of(JsPath.path("/address/$0"),
                                        JsBigDecGen.arbitrary(-180,
                                                              180)));

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    gen.sample(1000)
       .forEach(json -> {

         Assertions.assertEquals(json,
                                 parser.parse(json.toPrettyString()));
         Assertions.assertTrue(spec.test(json)
                                   .isEmpty());

       });

    gen1.sample(1000)
        .forEach(json -> {

          Assertions.assertEquals(json,
                                  parser.parse(json.toPrettyString()));
          Assertions.assertTrue(spec.test(json)
                                    .isEmpty());

        });

    gen2.sample(1000)
        .forEach(json -> {

          Assertions.assertEquals(json,
                                  parser.parse(json.toPrettyString()));

          Assertions.assertTrue(spec.test(json)
                                    .isEmpty());

        });

    JsObj jsonSchema = SpecToJsonSchema.convert(spec);

    System.out.println(jsonSchema.toPrettyString());


  }


  @Test
  public void testConstraints() {
    var spec = JsObjSpec.of("a",
                            JsSpecs.str(StrSchema.withMinLength(10)
                                                 .setMaxLength(15)
                                       ),
                            "b",
                            JsSpecs.str(StrSchema.withMaxLength(15)
                                                 .setMinLength(5)
                                       ),
                            "c",
                            JsSpecs.decimal(DecimalSchema.withMinimum(BigDecimal.TEN)),
                            "d",
                            JsSpecs.decimal(DecimalSchema.withMaximum(BigDecimal.TEN)),
                            "e",
                            JsSpecs.doubleNumber(DoubleSchema.withMaximum(3.0d)),
                            "f",
                            JsSpecs.doubleNumber(DoubleSchema.withMinimum(3.0d)),
                            "g",
                            JsSpecs.integer(IntegerSchema.withMaximum(3)),
                            "h",
                            JsSpecs.integer(IntegerSchema.withMinimum(3)),
                            "i",
                            JsSpecs.longInteger(LongSchema.withMaximum(3)),
                            "j",
                            JsSpecs.longInteger(LongSchema.withMinimum(3)),
                            "k",
                            JsSpecs.instant(InstantSchema.withMaximum(Instant.MAX)),
                            "l",
                            JsSpecs.instant(InstantSchema.withMinimum(Instant.MIN))
                           );

    var gen = SpecToGen.DEFAULT.convert(spec);

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    gen.sample(1000)
       .peek(System.out::println)
       .forEach(json -> {

         Assertions.assertEquals(json,
                                 parser.parse(json.toPrettyString()));
         Assertions.assertTrue(spec.test(json)
                                   .isEmpty());

       });

  }

  @Test
  public void testErrorsConstraints() {
    var spec = JsObjSpec.of("a",
                            JsSpecs.str(StrSchema.withMinLength(10)
                                                 .setMaxLength(15)
                                                 .setPattern("^[a-z]+$")
                                       ),

                            "b",
                            JsSpecs.decimal(DecimalSchema.withMinimum(BigDecimal.TEN)),
                            "c",
                            JsSpecs.decimal(DecimalSchema.withMaximum(BigDecimal.TEN)),
                            "d",
                            JsSpecs.longInteger(LongSchema.withMaximum(10)),
                            "e",
                            JsSpecs.longInteger(LongSchema.withMinimum(10)),
                            "f",
                            JsSpecs.instant(InstantSchema.withMaximum(Instant.EPOCH)),
                            "g",
                            JsSpecs.instant(InstantSchema.withMinimum(Instant.EPOCH))
                           );

    var gen = JsObjGen.of("a",
                          Combinators.oneOf(JsStrGen.alphabetic(0,
                                                                9),
                                            JsStrGen.alphabetic(16,
                                                                20),
                                            JsStrGen.digits(10,15)
                                           ),
                          "b",
                          JsBigDecGen.biased(BigDecimal.ZERO,
                                             BigDecimal.valueOf(9)),
                          "c",
                          JsBigDecGen.biased(BigDecimal.valueOf(11),
                                             BigDecimal.valueOf(100)
                                            ),
                          "d",
                          JsLongGen.biased(11,20),
                          "e",
                          JsLongGen.biased(0,9),
                          "f",
                          JsInstantGen.biased(Instant.EPOCH.plus(1,
                                                                 ChronoUnit.DAYS),
                                              Instant.EPOCH.plus(10,
                                                                 ChronoUnit.DAYS)
                                             ),
                          "g",
                          JsInstantGen.biased(Instant.EPOCH.minus(10,
                                                                 ChronoUnit.DAYS),
                                              Instant.EPOCH.minus(1,
                                                                 ChronoUnit.DAYS)
                                             )

                          ).withAllOptKeys();
    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    gen.sample(1000)
       .peek(System.out::println)
       .forEach(json -> {
         try {
           parser.parse(json.toPrettyString());
           Assertions.assertFalse(false,
                                  "Should have thrown a parse exception");
         } catch (JsParserException e) {
           System.out.println(e.getMessage());
         }
         List<SpecError> errors = spec.test(json);
         System.out.println(errors);
         Assertions.assertFalse(errors.isEmpty());

       });

  }

}
