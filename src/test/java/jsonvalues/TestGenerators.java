package jsonvalues;

import io.vavr.Tuple2;
import jsonvalues.gen.JsGen;
import jsonvalues.gen.JsGens;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.TestProperty;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static jsonvalues.gen.JsGens.oneOf;
import static jsonvalues.gen.JsGens.*;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;
import static jsonvalues.spec.JsSpecs.tuple;
import static jsonvalues.spec.JsSpecs.*;

public class TestGenerators {


    private static final <O> Consumer<O> failureConsumer() {
        return v -> {
            System.out.println(v);
            Assertions.fail("generated obj doesnt satisfy spec");
        };
    }

    @Test
    public void test_js_array() {
        final JsGen<JsObj> gen = JsObjGen.of("a",
                                             choose(0,
                                                    10
                                                   ).flatMap(n -> JsGens.array(alphabetic,
                                                                               n.value
                                                                              )
                                                            ),
                                             "b",
                                             choose(0,
                                                    10
                                                   ).flatMap(n -> JsGens.array(JsGens.integer,
                                                                               n.value
                                                                              )
                                                            )
                                            );

        TestProperty.test(gen,
                          v -> JsObjSpec.strict("a",
                                                arrayOfStrSuchThat(a -> a.size() <= 10).optional()
                                                                                       .nullable(),
                                                "b",
                                                arrayOfIntSuchThat(a -> a.size() <= 10).nullable()
                                                                                       .optional()
                                               )
                                        .test(v.toJsObj())
                                        .isEmpty()
                ,
                          v -> {
                              System.out.println(v);
                              Assertions.fail("generated obj doesn satisfy spec");
                          }
                         );
    }


    @Test
    public void test_js_obj() {

        final JsObjGen gen = JsObjGen.of("a",
                                         JsGens.integer,
                                         "b",
                                         JsGens.str,
                                         "c",
                                         alphabetic,
                                         "d",
                                         JsGens.tuple(JsGens.integer,
                                                      JsGens.str
                                                     )
                                        );

        TestProperty.test(gen,
                          v -> JsObjSpec.strict("a",
                                                integer,
                                                "b",
                                                str(s -> s.length() <= 10),
                                                "c",
                                                str(s -> s.length() <= 10),
                                                "d",
                                                tuple(integer,
                                                      str(s -> s.length() <= 10)
                                                     )
                                               )
                                        .test(v.toJsObj())
                                        .isEmpty(),
                          v -> {
                              System.out.println(v);
                              Assertions.fail("generated obj doesn satisfy spec");
                          }
                         );


    }

    @Test
    public void test_nested_gen() {
        JsObjGen gen = JsObjGen.of("a",
                                   JsGens.array(alphanumeric,
                                                5
                                               ),
                                   "b",
                                   JsGens.tuple(JsGens.str,
                                                JsGens.bool,
                                                JsGens.integer
                                               ),
                                   "c",
                                   JsObjGen.of("a",
                                               JsGens.oneOf(JsStr.of("a"),
                                                            JsBool.TRUE
                                                           )
                                              ),
                                   "d",
                                   JsGens.bool,
                                   "e",
                                   JsGens.oneOf(JsStr.of("hi"),
                                                JsNothing.NOTHING
                                               ),
                                   "f",
                                   oneOf(JsGens.str,
                                         JsGens.integer
                                        ),
                                   "g",
                                   single(JsStr.of("a"))
                                  );

        TestProperty.test(gen,
                          a ->
                                  JsObjSpec.strict("a",
                                                   arrayOfStr,
                                                   "b",
                                                   tuple(str,
                                                         bool,
                                                         integer
                                                        ),
                                                   "c",
                                                   JsObjSpec.strict("a",
                                                                    any(v -> v.isStr() || v.isBool())
                                                                   ),
                                                   "d",
                                                   bool,
                                                   "e",
                                                   str.optional(),
                                                   "f",
                                                   any(v -> v.isStr() || v.isIntegral()),
                                                   "g",
                                                   str(b -> b.equals("a"))
                                                  )
                                           .test(a.toJsObj())
                                           .isEmpty(),
                          v -> {
                              System.out.println(v);
                              Assertions.fail("generated obj doesn satisfy spec");
                          }
                         );
    }

    @Test
    public void test_constructors() {

        final JsGen<JsBool> boolGen = JsGens.oneOf(Arrays.asList(JsBool.TRUE,
                                                                 JsBool.FALSE
                                                                ));
        JsObjGen gen = JsObjGen.of("a",
                                   JsGens.str.optional()
                                             .nullable(),
                                   "b",
                                   JsGens.integer,
                                   "c",
                                   boolGen,
                                   "d",
                                   alphabetic,
                                   "e",
                                   alphanumeric,
                                   "f",
                                   JsGens.longInteger,
                                   "g",
                                   JsGens.decimal.optional()
                                                 .nullable(),
                                   "h",
                                   single(JsBool.TRUE),
                                   "i",
                                   oneOf(JsGens.str,
                                         JsGens.decimal
                                        ),
                                   "j",
                                   JsGens.oneOf(JsStr.of("a"),
                                                JsBool.TRUE
                                               ),
                                   "k",
                                   frequency(new Tuple2<>(1,
                                                          JsGens.str
                                             ),
                                             new Tuple2<>(1,
                                                          JsGens.longInteger
                                             )
                                            ),
                                   "l",
                                   arrayDistinct(choose(1,
                                                        10
                                                       ),
                                                 5
                                                ),
                                   "m",
                                   characterAlpha,
                                   "n",
                                   letter,
                                   "o",
                                   JsGens.binary.optional(),
                                   "p",
                                   JsGens.dateBetween(0,
                                                      1000
                                                     )
                                         .optional()
                                  );

        JsObjSpec spec = JsObjSpec.lenient("a",
                                           str.optional()
                                              .nullable(),
                                           "b",
                                           integer,
                                           "c",
                                           bool,
                                           "d",
                                           str,
                                           "e",
                                           str,
                                           "f",
                                           integral,
                                           "g",
                                           decimal.optional()
                                                  .nullable(),
                                           "h",
                                           bool,
                                           "i",
                                           any(v -> v.isStr() || v.isDecimal()),
                                           "j",
                                           any(v -> v.isStr() || v.isBool()),
                                           "k",
                                           any(i -> i.isStr() || i.isIntegral()),
                                           "l",
                                           arraySuchThat(a -> a.size() == 5),
                                           "m",
                                           str(s -> s.length() == 1),
                                           "n",
                                           str(s -> s.length() == 1),
                                           "o",
                                           JsSpecs.binary.optional(),
                                           "p",
                                           JsSpecs.instant.optional()
                                          );

        TestProperty.test(gen,
                          v -> spec.test(v.toJsObj())
                                   .isEmpty()
                ,
                          v -> {
                              System.out.println(v);
                              Assertions.fail("generated obj doesn satisfy spec");
                          }
                         );

        TestProperty.test(gen,
                          v -> {
                              String s = v.toString();
                              return v.equals(JsObj.parse(s));
                          },
                          v -> {
                              System.out.println(v);
                              Assertions.fail("generated obj doesn satisfy spec");
                          }
                         );
    }

    @Test
    public void testSamples() {

        JsObjGen gen = JsObjGen.of("a",
                                   JsGens.str,
                                   "b",
                                   JsGens.integer.optional()
                                                 .nullable()
                                  );
        JsObjSpec spec = JsObjSpec.strict("a",
                                          str,
                                          "b",
                                          integer.optional()
                                                 .nullable()
                                         );

        final Supplier<JsObj> supplier = gen.sample();

        Stream.generate(supplier)
              .map(spec::test)
              .limit(100)
              .allMatch(Set::isEmpty);
    }

    @Test
    public void testMapNumbers() {
        final JsGen<JsInt> posInteger = JsGens.integer.map(i -> i.map(v ->
                                                                      {
                                                                          if (v >= 0) return v;
                                                                          else return -v;
                                                                      })
                                                          );

        final Supplier<JsInt> supplier = posInteger.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value >= 0);
        }
    }

    @Test
    public void testSuchThat() {
        final JsGen<JsInt> negative = JsGens.integer.suchThat(i -> i.value < 0);

        final Supplier<JsInt> supplier = negative.sample(new Random());

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(supplier.get().value < 0);
        }

    }


    @Test
    public void testDigits() {

        final JsGen<JsArray> array = JsGens.array(digit,
                                                  10
                                                 );

        TestProperty.test(array,
                          it -> it.toJsArray()
                                  .size() == 10,
                          failureConsumer()
                         );

        TestProperty.test(array,
                          value -> JsSpecs.arrayOfStr(it -> it.length() == 1)
                                          .test(value.toJsArray())
                                          .isEmpty(),
                          failureConsumer()
                         );


    }

    @Test
    public void testDigitCode() {

        TestProperty.test(JsGens.digits(10),
                          value -> value.toJsStr().value
                                  .length() == 10,
                          failureConsumer()
                         );
    }

    @Test
    public void testDatesBetween() {

        TestProperty.test(JsGens.dateBetween(ZonedDateTime.now(),
                                             ZonedDateTime.now()
                                                          .plus(Duration.ofDays(2))
                                            ),
                          JsInstant::isInstant,
                          failureConsumer()
                         );
    }

    @Test
    public void testDates() {

        TestProperty.test(JsGens.date,
                          JsInstant::isInstant,
                          failureConsumer()
        );
    }

    @Test
    public void testBinary() {
        TestProperty.test(JsGens.binary,
                          value -> value.isBinary() & value.toJsBinary().value.length <= 1024,
                          failureConsumer()
                         );
    }

    @Test
    public void testConsGen() {

        TestProperty.test(JsGens.cons(JsStr.of("hi")),
                          v -> v.equals(JsStr.of("hi")),
                          failureConsumer()
                         );
    }

    public static void main(String[] args) {
        JsInstant instant = JsInstant.of(Instant.parse("1970-01-01T00:02:20Z"));
        JsStr     str     = JsStr.of("1970-01-01T00:02:20Z");

        System.out.println(instant.equals(str));
    }
}
