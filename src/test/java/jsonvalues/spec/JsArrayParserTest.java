package jsonvalues.spec;

import fun.gen.Combinators;
import fun.gen.Gen;
import jsonvalues.*;
import jsonvalues.gen.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static jsonvalues.spec.JsSpecs.*;

public class JsArrayParserTest {


    @Test
    public void test_array_of_different_elements() {

        final JsTupleSpec spec = JsSpecs.tuple(str(),
                                               integer(),
                                               longInteger(),
                                               decimal(),
                                               bool(),
                                               bigInteger(),
                                               obj(),
                                               array(),
                                               integer(i -> i > 0),
                                               any(v -> v.isStr() || v.isInt()),
                                               str(s -> s.startsWith("1")),
                                               arrayOfBigIntSuchThat(a -> a.size() == 1).nullable(),
                                               arrayOfNumberSuchThat(a -> a.size() == 2),
                                               number(JsValue::isDecimal),
                                               arrayOfBigInt(a -> a.longValueExact() > 0),
                                               arrayOfBigInt(a -> a.longValueExact() > 0).nullable(),
                                               arrayOfObj(),
                                               arrayOfObj().nullable()
        );


        JsArray array = JsArray.of(JsStr.of("a"),
                                   JsInt.of(1),
                                   JsLong.of(1),
                                   JsBigDec.of(BigDecimal.TEN),
                                   JsBool.TRUE,
                                   JsBigInt.of(BigInteger.TEN),
                                   JsObj.empty(),
                                   JsArray.empty(),
                                   JsInt.of(10),
                                   JsStr.of("a"),
                                   JsStr.of("1aaa"),
                                   JsArray.of(1),
                                   JsArray.of(1.5,
                                              2.5
                                   ),
                                   JsDouble.of(1.5),
                                   JsArray.of(1,
                                              2
                                   ),
                                   JsNull.NULL,
                                   JsArray.of(JsObj.empty(),
                                              JsObj.empty()
                                   ),
                                   JsNull.NULL
        );

        Assertions.assertEquals(array,
                                new JsArraySpecParser(spec).parse(array.toPrettyString())
        );
    }

    @Test
    public void testNullable() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      arrayOfStr().nullable()
        );

        JsObjSpecParser parser = new JsObjSpecParser(spec);
        final JsObj a = JsObj.of("a",
                                 JsArray.of("a",
                                            "b"
                                 )
        );
        Assertions.assertTrue(spec.test(a)
                                  .isEmpty());
        Assertions.assertEquals(parser.parse(a.toString()),
                                a);


        final JsObj b = JsObj.of("a",
                                 JsNull.NULL
        );
        Assertions.assertTrue(spec.test(b)
                                  .isEmpty());
        Assertions.assertEquals(parser.parse(b.toString()),
                                b);


        JsObjSpec specST = JsObjSpec.of("a",
                                        arrayOfStrSuchThat(it -> it.size() % 2 == 0).nullable()
        );
        JsObjSpecParser parserST = new JsObjSpecParser(specST);

        Assertions.assertTrue(specST.test(a)
                                    .isEmpty());
        Assertions.assertEquals(parserST.parse(a.toString()),
                                a);

        Assertions.assertTrue(specST.test(b)
                                    .isEmpty());
        Assertions.assertEquals(parserST.parse(b.toString()),
                                b);

    }

    @Test
    public void testArrayOfObject() {
        JsObjSpec spec = JsObjSpec.of("a",
                                      JsSpecs.arrayOfObjSpec(JsObjSpec.of("a",
                                                                          str(),
                                                                          "b",
                                                                          integer()
                                      ))
        );

        final JsObj a = JsObj.of("a",
                                 JsArray.of(JsObj.of("a",
                                                     JsStr.of("a"),
                                                     "b",
                                                     JsInt.of(1)
                                 ))
        );


        Assertions.assertEquals(a,
                                new JsObjSpecParser(spec).parse(a.toString())
        );

        JsObjSpec specNullable = JsObjSpec.of("a",
                                              JsSpecs.arrayOfObjSpec(JsObjSpec.of("a",
                                                                                  str(),
                                                                                  "b",
                                                                                  integer()
                                                     ))
                                                     .nullable()
        );

        final JsObj b = JsObj.of("a",
                                 JsNull.NULL
        );
        Assertions.assertEquals(b,
                                new JsObjSpecParser(specNullable).parse(b.toString())
        );

        JsObjSpec specTested = JsObjSpec.of("a",
                                            arrayOfObj(o -> o.containsKey("a")
                                            )
                                                    .nullable()
        ).withOptKeys("a");

        final JsObj c = JsObj.of("a",
                                 JsArray.of(JsObj.of("a",
                                                     JsBool.TRUE
                                            ),
                                            JsObj.of("a",
                                                     JsInt.of(1)
                                            )
                                 )
        );
        Assertions.assertEquals(c,
                                new JsObjSpecParser(specTested).parse(c.toString())
        );

        JsObjSpec specSuchThat = JsObjSpec.of("a",
                                              arrayOfObjSuchThat(arr -> arr.size() > 1).nullable(),
                                              "b",
                                              arrayOfBoolSuchThat(arr -> arr.size() > 2)
                                                      .nullable(),
                                              "c",
                                              arrayOfNumberSuchThat(arr -> arr.head()
                                                                              .equals(JsInt.of(1)))
                                                      .nullable(),
                                              "d",
                                              arrayOfDecSuchThat(arr -> arr.head()
                                                                           .equals(JsBigDec.of(BigDecimal.TEN)))
                                                      .nullable(),
                                              "e",
                                              arrayOfLongSuchThat(arr -> arr.size() == 3)
                                                      .nullable(),
                                              "f",
                                              arrayOfIntSuchThat(arr -> arr.size() == 3).nullable(),
                                              "g",
                                              arrayOfDec(i -> i.longValueExact() % 2 == 0).nullable(),
                                              "i",
                                              arrayOfStr(i -> i.length() > 2).nullable(),
                                              "j",
                                              arrayOfNumber(JsValue::isDecimal).nullable()

        ).withAllOptKeys();


        JsObjGen gen = JsObjGen.of("a",
                                   JsArrayGen.arbitrary(Gen.cons(JsObj.empty()),
                                                        2,
                                                        10),
                                   "b",
                                   JsArrayGen.arbitrary(JsBoolGen.arbitrary(),
                                                        3,
                                                        10),
                                   "c",
                                   JsTupleGen.of(Gen.cons(JsInt.of(1)),
                                                 JsDoubleGen.arbitrary()),
                                   "d",
                                   JsTupleGen.of(Gen.cons(JsBigDec.of(BigDecimal.TEN)),
                                                 JsBigDecGen.arbitrary()),
                                   "e",
                                   JsArrayGen.arbitrary(JsLongGen.arbitrary(),
                                                        3,
                                                        3),
                                   "f",
                                   JsArrayGen.arbitrary(JsIntGen.arbitrary(),
                                                        3),
                                   "g",
                                   JsArrayGen.arbitrary(seed -> () -> {
                                                            long l = seed.nextLong();
                                                            if (l % 2 == 0) return JsBigDec.of(BigDecimal.valueOf(l));
                                                            return JsBigDec.of(BigDecimal.valueOf(l + 1));
                                                        },
                                                        1,
                                                        10),
                                   "i",
                                   JsArrayGen.arbitrary(JsStrGen.arbitrary(3).peek(i -> {
                                                            if (i.value.length() < 3)
                                                                System.out.println(i);
                                                        }),
                                                        0,
                                                        10),
                                   "j",
                                   JsArrayGen.arbitrary(JsBigDecGen.arbitrary(),
                                                        0,
                                                        10)

                               )
                               .withAllOptKeys()
                               .withAllNullValues();


        Assertions.assertTrue(gen.sample(100000)
                                 .allMatch(d -> new JsObjSpecParser(specSuchThat).parse(d.toString()).equals(d)
                                 ));


    }

    @Test
    public void testArrayOfValue() {
        Gen<JsValue> valueGen = Combinators.oneOf(JsBigIntGen.biased(10),
                                                  JsStrGen.biased(10));
        JsObjGen gen = JsObjGen.of("a",
                                   JsArrayGen.arbitrary(JsStrGen.arbitrary(10),
                                                        10),
                                   "b",
                                   JsArrayGen.arbitrary(JsStrGen.biased(10),
                                                        10),
                                   "d",
                                   JsArrayGen.biased(valueGen,
                                                     5,
                                                     10),
                                   "e",
                                   JsArrayGen.biased(valueGen,
                                                     5,
                                                     10),
                                   "f",
                                   JsArrayGen.biased(valueGen,
                                                     5,
                                                     10)
                               )
                               .withAllNullValues()
                               .withAllOptKeys();

        JsObjSpec spec = JsObjSpec.of("a",
                                      array(),
                                      "b",
                                      array().nullable(),
                                      "d",
                                      array(v -> v.isIntegral() || v.isStr())
                                              .nullable(),
                                      "e",
                                      array(v -> v.isIntegral() || v.isStr()).nullable(),
                                      "f",
                                      array(v -> v.isIntegral() || v.isStr())
                                              .nullable()
        ).lenient().withOptKeys("c",
                                "d",
                                "f");

        JsObjSpecParser parser = new JsObjSpecParser(spec);


        Assertions.assertTrue(gen.suchThat(spec).sample(10000)
                                 .allMatch(d -> parser.parse(d.toString()).equals(d)
                                 ));

        Assertions.assertTrue(gen.suchThatNo(spec).sample(10000)
                                 .allMatch(d -> {
                                               try {
                                                   parser.parse(d.toString());
                                                   return false;
                                               } catch (Exception e) {
                                                   System.out.println(e.getMessage());
                                                   return true;
                                               }
                                           }
                                 ));

    }
}
