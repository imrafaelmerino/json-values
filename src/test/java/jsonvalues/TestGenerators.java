package jsonvalues;

import io.vavr.Tuple2;
import jsonvalues.gen.*;
import jsonvalues.gen.state.JsStateGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static jsonvalues.gen.JsGens.*;
import static jsonvalues.spec.JsSpecs.*;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;

public class TestGenerators
{


  @Test
  public void test_js_array()
  {
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

    JsObjGen gen = JsObjGen.of(new Tuple2<>("a",
                                            JsGens.integer
                                           ),
                               new Tuple2<>("b",
                                            JsGens.str
                                           ),
                               new Tuple2<>("c",
                                            JsGens.bool
                                           ),
                               new Tuple2<>("d",
                                            alphabetic
                                           ),
                               new Tuple2<>("e",
                                            alphanumeric
                                           )
                              );

    test(gen,
         v -> JsObjSpec.strict("a",
                               integer,
                               "b",
                               str,
                               "c",
                               bool,
                               "d",
                               str,
                               "e",
                               str
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

    test(gen,
         a ->
           JsObjSpec.strict("a",
                            arrayOfStr,
                            "b",
                            tuple( str,
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
         1000
        );
  }


  @Test
  public void test_constructors()
  {
    final JsGen<JsBool> boolGen = JsGens.oneOf(Arrays.asList(JsBool.TRUE,
                                                             JsBool.FALSE
                                                            ));
    JsObjGen gen = JsObjGen.of("a",
                               JsGens.str.optional().nullable(),
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
                               JsGens.decimal.optional().nullable(),
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
                               frequency(new Tuple2<>(1,JsGens.str),new Tuple2<>(1,JsGens.longInteger)),
                               "l",
                               arrayDistinct(choose(1,10),5),
                               "m",
                               characterAlpha,
                               "n",
                               letter
                              );

    JsObjSpec spec = JsObjSpec.lenient("a",
                                       str.optional().nullable(),
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
                                       decimal.optional().nullable(),
                                       "h",
                                       bool,
                                       "i",
                                       any(v -> v.isStr() || v.isDecimal()),
                                       "j",
                                       any(v -> v.isStr() || v.isBool()),
                                       "k",
                                       any(i->i.isStr() || i.isIntegral()),
                                       "l",
                                       arraySuchThat(a->a.size()==5),
                                       "m",
                                       str(s->s.length()  == 1),
                                       "n",
                                       str(s->s.length()  == 1)
                                      );

    test(gen,
         v -> spec.test(v.toJsObj())
                  .isEmpty(),
         1000
        );
  }

  @Test
  public void testSamples(){

    JsObjGen gen = JsObjGen.of("a",JsGens.str,"b",JsGens.integer.optional().nullable());
    JsObjSpec spec = JsObjSpec.strict("a",str,"b",integer.optional().nullable());

    final Supplier<JsObj> supplier = gen.sample();

    Stream.generate(supplier).map(spec::test).limit(100).allMatch(Set::isEmpty);
  }

  @Test
  public void testMapNumbers(){
    final JsGen<JsInt> posInteger = JsGens.integer.map(i -> i.map(v ->
                                                           {
                                                             if (v >= 0) return v;
                                                             else return -v;
                                                           })
                                                      );

    final Supplier<JsInt> supplier = posInteger.sample(new Random());

    for (int i = 0; i < 100; i++)
    {
      Assertions.assertTrue(supplier.get().value>=0);
    }
  }

  @Test
  public void testSuchThat(){
    final JsGen<JsInt> negative = JsGens.integer.suchThat(i -> i.value < 0);

    final Supplier<JsInt> supplier = negative.sample(new Random());

    for (int i = 0; i < 100; i++)
    {
      Assertions.assertTrue(supplier.get().value<0);
    }

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

  @Test
  public void testDigits(){

    final JsGen<JsArray> array = JsGens.array(digit,
                                              10);

    test(array,it-> it.toJsArray().size()==10 ,100);

    test(array,value->JsSpecs.arrayOfStr(it->it.length()==1)
                             .test(value.toJsArray()).isEmpty(),
         100);


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
