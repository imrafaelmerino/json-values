package jsonvalues;

import jsonvalues.gen.*;
import jsonvalues.gen.state.JsStateGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

import static jsonvalues.gen.JsGens.*;
import static jsonvalues.spec.JsSpecs.*;
import static jsonvalues.spec.JsSpecs.integer;

public class TestGenerators
{


  @Test
  public void test_js_array()
  {
    final JsGen<JsObj> gen = JsObjGen.of("a",
                                         choose(0,
                                                10
                                               ).flatMap(n -> JsGens.arrayOf(alphabetic,
                                                                               n.value
                                                                              )
                                                           ),
                                         "b",
                                         choose(0,
                                                10
                                               ).flatMap(n -> JsGens.arrayOf(JsGens.integer,
                                                                               n.value
                                                                              )
                                                           )
                                        );

    test(gen,
         v -> JsObjSpec.strict("a",
                               arrayOfStrSuchThat(a -> a.size() <= 10).optional()
                                                                      .nullable(),
                               "b",
                               arrayOfIntSuchThat(a -> a.size() <= 10).nullable()
                                                                      .optional()
                              )
                       .test(v.toJsObj())
                       .isEmpty(),
         100
        );
  }


  @Test
  public void test_pair_gen()
  {

    JsObjGen gen = JsObjGen.of(JsGenPair.of("a",
                                            JsGens.integer
                                           ),
                               JsGenPair.of("b",
                                            JsGens.str
                                           ),
                               JsGenPair.of("c",
                                            JsGens.bool
                                           ),
                               JsGenPair.of("d",
                                            alphabetic
                                           ),
                               JsGenPair.of("e",
                                            alphanumeric
                                           )
                              );

    test(gen,
         v -> JsObjSpec.strict("a",
                               integer,
                               "b",
                               JsSpecs.str,
                               "c",
                               JsSpecs.bool,
                               "d",
                               JsSpecs.str,
                               "e",
                               JsSpecs.str
                              )
                       .test(v.toJsObj())
                       .isEmpty(),
         1000
        );
  }

  @Test
  public void test_js_obj()
  {

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

    test(gen,
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
         100
        );


  }

  @Test
  public void test_nested_gen()
  {
    JsObjGen gen = JsObjGen.of("a",
                               JsGens.arrayOf(alphanumeric,
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

    test(gen,
         a ->
           JsObjSpec.strict("a",
                            arrayOfStr,
                            "b",
                            tuple( JsSpecs.str,
                                JsSpecs.bool,
                               integer
                              ),
                            "c",
                            JsObjSpec.strict("a",
                                             any(v -> v.isStr() || v.isBool())
                                            ),
                            "d",
                            JsSpecs.bool,
                            "e",
                            JsSpecs.str.optional(),
                            "f",
                            any(v -> v.isStr() || v.isIntegral()),
                            "g",
                            str(b -> b.equals("a"))
                           )
                    .test(a.toJsObj())
                    .isEmpty(),
         1000
        );
  }


  @Test
  public void test_constructors()
  {
    final JsGen<JsBool> listGen = JsGens.oneOf(Arrays.asList(JsBool.TRUE,
                                                             JsBool.FALSE
                                                            ));
    JsObjGen gen = JsObjGen.of("a",
                               JsGens.str,
                               "b",
                               JsGens.integer,
                               "c",
                               listGen,
                               "d",
                               alphabetic,
                               "e",
                               alphanumeric,
                               "f",
                               JsGens.longInteger,
                               "g",
                               JsGens.decimal,
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
                               JsGens.str
                              );

    JsObjSpec spec = JsObjSpec.lenient("a",
                                       JsSpecs.str,
                                       "b",
                                       integer,
                                       "c",
                                       JsSpecs.bool,
                                       "d",
                                       JsSpecs.str,
                                       "e",
                                       JsSpecs.str,
                                       "f",
                                       integral,
                                       "g",
                                       JsSpecs.decimal,
                                       "h",
                                       JsSpecs.bool,
                                       "i",
                                       any(v -> v.isStr() || v.isDecimal()),
                                       "j",
                                       any(v -> v.isStr() || v.isBool()),
                                       "k",
                                       JsSpecs.str
                                      );

    test(gen,
         v -> spec.test(v.toJsObj())
                  .isEmpty(),
         1000
        );
  }

  /**

   @param gen generator to produce randomized input data
   @param condition the property to be tested
   @param times number of times an input is produced and tested on the property
   */
  public static void test(JsGen<?> gen,
                          Predicate<JsValue> condition,
                          int times
                         )
  {
    for (int i = 0; i < times; i++)
    {

      final JsValue value = gen.apply(new Random())
                               .get();
      Assertions.assertTrue(condition.test(value));
    }
  }

  public static void test(JsStateGen gen,
                          Predicate<JsValue> condition,
                          int times
                         )
  {
    for (int i = 0; i < times; i++)
    {

      final JsValue value = gen.apply(JsObj.empty()).apply(new Random())
                               .get();
      Assertions.assertTrue(condition.test(value));
    }
  }
}
