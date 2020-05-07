package jsonvalues;


import jsonvalues.gen.JsGens;
import jsonvalues.gen.state.JsObjStateGen;
import jsonvalues.gen.state.JsStateGens;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Test;


import java.util.Random;

import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.gen.JsGens.*;

import static jsonvalues.gen.state.JsStateGens.*;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsObjStateGen
{


  @Test
  public void test_gen()
  {


    JsObjStateGen gen = JsObjStateGen.of("a",
                                         o -> oneOf(choose(1,
                                                           2
                                                          ),
                                                    single(NOTHING)
                                                   ).optional(),
                                         "b",
                                         ifNotContains("a",
                                                       alphabetic.optional()
                                                      ),
                                         "c",
                                         JsObjStateGen.of("d",
                                                          o -> oneOf(choose(1,
                                                                            5
                                                                           ),
                                                                     single(NOTHING)
                                                                    ),
                                                          "e",
                                                          ifContains("d",
                                                                     JsGens::single
                                                                    )
                                                         )
                                        );


    TestGenerators.test(gen,
                        v -> JsObjSpec.lenient("a",
                                               JsSpecs.any.optional(),
                                               "b",
                                               JsSpecs.str.optional(),
                                               "c",
                                               JsObjSpec.strict("d",
                                                                JsSpecs.integer.optional(),
                                                                "e",
                                                                JsSpecs.integer.optional()
                                                               )
                                              )
                                      .test(v.toJsObj())
                                      .isEmpty(),
                        100
                       );


    JsObjStateGen a = JsObjStateGen.of("a",
                                       o -> alphabetic,
                                       "b",
                                       o -> alphanumeric,
                                       "c",
                                       o -> oneOf(NOTHING,
                                                  JsStr.of("a")
                                                 ),
                                       "d",
                                       JsStateGens.ifElse(b -> b.containsKey("c"),
                                                          JsGens.str,
                                                          JsGens.integer
                                                         ),
                                       "e",
                                       o -> choose(1,
                                                   10
                                                  ),
                                       "f",
                                       o -> JsGens.array(JsGens.integer,
                                                         10
                                                        )
                                      );

    TestGenerators.test(a,
                        v -> JsObjSpec.strict("a",
                                              JsSpecs.str,
                                              "b",
                                              JsSpecs.str,
                                              "c",
                                              JsSpecs.str.optional(),
                                              "d",
                                              any(i -> i.isStr() || i.isInt()),
                                              "f",
                                              arrayOfInt,
                                              "e",
                                              integer(i -> i > 0 && i < 11)
                                             )
                                      .test(v.toJsObj())
                                      .isEmpty(),
                        1000
                       );

  }


  @Test
  public void a()
  {
    JsObjStateGen gen = JsObjStateGen.of("a",
                                         o -> JsGens.oneOf(JsGens.alphabetic,
                                                           JsGens.single(NOTHING)
                                                          ),
                                         "b",
                                         JsStateGens.ifNotContains("a",
                                                                   JsGens.choose(0,10)
                                                                  ),
                                         "c",
                                         JsStateGens.ifContains("b",
                                                                v-> single(v.toJsInt()
                                                                             .map(i -> i + 1)
                                                                           )
                                                               )

                                        );

    for (int i = 0; i < 10; i++)
    {
      System.out.println(gen.apply(JsObj.empty()).apply(new Random()).get());
    }
  }

}
