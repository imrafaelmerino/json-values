package jsonvalues.api.gen;

import fun.gen.Gen;
import jsonvalues.JsInt;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecBuilder;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsObjGen {

  @Test
  public void test_20_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE,
                               "p",
                               ONE,
                               "q",
                               ONE,
                               "r",
                               ONE,
                               "s",
                               ONE,
                               "t",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 20));


  }

  @Test
  public void test_19_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE,
                               "p",
                               ONE,
                               "q",
                               ONE,
                               "r",
                               ONE,
                               "s",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 19));


  }

  @Test
  public void test_18_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE,
                               "p",
                               ONE,
                               "q",
                               ONE,
                               "r",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 18));


  }

  @Test
  public void test_17_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE,
                               "p",
                               ONE,
                               "q",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 17));


  }

  @Test
  public void test_16_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE,
                               "p",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 16));


  }

  @Test
  public void test_15_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE,
                               "o",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 15));


  }

  @Test
  public void test_14_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE,
                               "n",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 14));


  }

  @Test
  public void test_13_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE,
                               "m",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 13));


  }


  @Test
  public void test_12_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE,
                               "l",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 12));


  }

  @Test
  public void test_11_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE,
                               "k",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 11));


  }

  @Test
  public void test_10_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE,
                               "j",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 10));


  }

  @Test
  public void test_9_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE,
                               "i",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 9));


  }

  @Test
  public void test_8_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE,
                               "h",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 8));


  }

  @Test
  public void test_7_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE,
                               "g",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 7));


  }

  @Test
  public void test_6_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE,
                               "f",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 6));


  }

  @Test
  public void test_5_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE,
                               "e",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 5));


  }

  @Test
  public void test_4_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE,
                               "d",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 4));


  }

  @Test
  public void test_3_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE,
                               "c",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 3));


  }

  @Test
  public void test_2_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE,
                               "b",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 2));


  }

  @Test
  public void test_1_args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 1));


  }

  @Test
  public void testConcatGen() {

    JsObjGen xs = JsObjGen.of("a",
                              JsStrGen.alphabetic(),
                              "b",
                              JsStrGen.alphabetic())
                          .withAllOptKeys()
                          .withAllNullValues();

    JsObjSpec xsSpec = JsObjSpecBuilder.withName("xsSpec")
                                       .build(JsObjSpec.of("a",
                                                           JsSpecs.str()
                                                                  .nullable(),
                                                           "b",
                                                           JsSpecs.str()
                                                                  .nullable())
                                                       .withAllOptKeys()
                                             );

    JsObjGen ys = JsObjGen.of("c",
                              JsStrGen.alphabetic(),
                              "d",
                              JsStrGen.alphabetic())
                          .withAllOptKeys()
                          .withAllNullValues();

    JsObjSpec ysSpec = JsObjSpecBuilder.withName("ysSpec")
                                       .build(JsObjSpec.of("c",
                                                           JsSpecs.str()
                                                                  .nullable(),
                                                           "d",
                                                           JsSpecs.str()
                                                                  .nullable())
                                                       .withAllOptKeys());

    JsObjSpec zsSpec = xsSpec.concat(ysSpec);

    Assertions.assertTrue(xs.concat(ys)
                            .sample(1000)
                            .allMatch(obj -> zsSpec.test(obj)
                                                   .isEmpty()));


  }


}
