package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpecToGenTests {


  @Test
  public void test_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            JsSpecs.str(),
                            "b",
                            JsSpecs.decimal(),
                            "c",
                            JsSpecs.doubleNumber(),
                            "d",
                            JsSpecs.integer(),
                            "e",
                            JsSpecs.longInteger(),
                            "f",
                            JsSpecs.bool(),
                            "g",
                            JsSpecs.instant(),
                            "h",
                            JsObjSpec.of("a",
                                         JsSpecs.str(),
                                         "b",
                                         JsSpecs.decimal(),
                                         "c",
                                         JsSpecs.doubleNumber(),
                                         "d",
                                         JsSpecs.integer(),
                                         "e",
                                         JsSpecs.longInteger(),
                                         "f",
                                         JsSpecs.bool(),
                                         "g",
                                         JsSpecs.instant(),
                                         "h",
                                         JsSpecs.decimal(),
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
                            JsSpecs.str(StrSchema.withLength(0,
                                                             10)),
                            "b",
                            JsSpecs.decimal(DecimalSchema.between(BigDecimal.ZERO,
                                                                  BigDecimal.TEN)
                                           ),
                            "c",
                            JsSpecs.doubleNumber(DoubleSchema.between(0.0,
                                                                      10.0)),
                            "d",
                            JsSpecs.integer(IntegerSchema.between(0,
                                                                  10)),
                            "e",
                            JsSpecs.longInteger(LongSchema.betweenInterval(0,
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
                                         JsSpecs.str(StrSchema.withLength(0,
                                                                          10)),
                                         "b",
                                         JsSpecs.decimal(DecimalSchema.between(BigDecimal.ZERO,
                                                                               BigDecimal.TEN)),
                                         "c",
                                         JsSpecs.doubleNumber(DoubleSchema.between(0.0,
                                                                                   10.0)),
                                         "d",
                                         JsSpecs.integer(IntegerSchema.between(0,
                                                                               10)
                                                        ),
                                         "e",
                                         JsSpecs.longInteger(LongSchema.betweenInterval(0,
                                                                                        10)),
                                         "f",
                                         JsSpecs.bool(),
                                         "g",
                                         JsSpecs.instant(InstantSchema.between(Instant.EPOCH,
                                                                               Instant.EPOCH.plus(365,
                                                                                                  ChronoUnit.DAYS)
                                                                              )),
                                         "h",
                                         JsSpecs.decimal(DecimalSchema.between(BigDecimal.ZERO,
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
                            JsSpecs.str()
                                   .nullable(),
                            "b",
                            JsSpecs.decimal()
                                   .nullable(),
                            "c",
                            JsSpecs.doubleNumber()
                                   .nullable(),
                            "d",
                            JsSpecs.integer()
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
                                         JsSpecs.str()
                                                .nullable(),
                                         "b",
                                         JsSpecs.decimal()
                                                .nullable(),
                                         "c",
                                         JsSpecs.doubleNumber()
                                                .nullable(),
                                         "d",
                                         JsSpecs.integer()
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
                                         JsSpecs.decimal()
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
                            JsSpecs.arrayOfStr(),
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
                                         JsSpecs.arrayOfStr(),
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
                            JsSpecs.arrayOfStr(StrSchema.withLength(0,
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
                            JsSpecs.arrayOfLong(LongSchema.betweenInterval(0,
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
                                         JsSpecs.arrayOfStr(StrSchema.withLength(0,
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
                                         JsSpecs.arrayOfLong(LongSchema.betweenInterval(0,
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
                            JsSpecs.arrayOfStr()
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
                                         JsSpecs.arrayOfStr()
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

  String NAME_FIELD = "name";
  String TYPE_FIELD = "type";
  String BUTTON_COUNT_FIELD = "buttonCount";
  String WHEEL_COUNT_FIELD = "wheelCount";
  String TRACKING_TYPE_FIELD = "trackingType";
  String KEY_COUNT_FIELD = "keyCount";
  String MEDIA_BUTTONS_FIELD = "mediaButtons";
  String CONNECTED_DEVICES_FIELD = "connectedDevices";
  String PERIPHERAL_FIELD = "my_peripheral";
  List<String> TRACKING_TYPE_ENUM = List.of("ball",
                                            "optical");

  @Test
  public void test() {

    var baseSpec = JsObjSpec.of(NAME_FIELD,
                                JsSpecs.str()
                               );

    var mouseSpec =
        JsObjSpec.of(TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("mouse")),
                     BUTTON_COUNT_FIELD,
                     JsSpecs.integer(),
                     WHEEL_COUNT_FIELD,
                     JsSpecs.integer(),
                     TRACKING_TYPE_FIELD,
                     JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                    )
                 .concat(baseSpec);

    var keyboardSpec =
        JsObjSpec.of(
                     TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("keyboard")),
                     KEY_COUNT_FIELD,
                     JsSpecs.integer(),
                     MEDIA_BUTTONS_FIELD,
                     JsSpecs.bool()
                    )
                 .concat(baseSpec);

    var usbHubSpec =
        JsObjSpec.of(TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("usb_hub")),
                     CONNECTED_DEVICES_FIELD,
                     JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                            .nullable()
                    )
                 .withOptKeys(CONNECTED_DEVICES_FIELD)
                 .concat(baseSpec);

    var peripheralSpec =
        JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                            oneSpecOf(mouseSpec,
                                      keyboardSpec,
                                      usbHubSpec));

    System.out.println(SpecToJsonSchema.convert(peripheralSpec)
                                       .toPrettyString());
    //to avoid stackoverflow errors
    var specToGen =
        SpecToGen.of(new SpecGenConfBuilder().withNullableObjProbability(4)
                                             .withOptionalObjFieldProbability(4));
    var peripheralGen = specToGen.convert(peripheralSpec);

    var parser = JsObjSpecParser.of(peripheralSpec);

    peripheralGen.sample(500)
                 .forEach(obj -> {
                            System.out.println(obj);
                            Assertions.assertEquals(obj,
                                                    parser.parse(obj.toString())
                                                   );

                            Assertions.assertTrue(peripheralSpec.test(obj)
                                                                .isEmpty());
                          }
                         );
  }

}
