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

public class JsObjGenTest {

  @Test
  public void shouldSupportConstructorWith20Args() {

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
  public void shouldSupportConstructorWith19Args() {

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
  public void shouldSupportConstructorWith18Args() {

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
  public void shouldSupportConstructorWith17Args() {

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
  public void shouldSupportConstructorWith16Args() {

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
  public void shouldSupportConstructorWith15Args() {

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
  public void shouldSupportConstructorWith14Args() {

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
  public void shouldSupportConstructorWith13Args() {

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
  public void shouldSupportConstructorWith12Args() {

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
  public void shouldSupportConstructorWith11Args() {

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
  public void shouldSupportConstructorWith10Args() {

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
  public void shouldSupportConstructorWith9Args() {

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
  public void shouldSupportConstructorWith8Args() {

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
  public void shouldSupportConstructorWith7Args() {

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
  public void shouldSupportConstructorWith6Args() {

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
  public void shouldSupportConstructorWith5Args() {

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
  public void shouldSupportConstructorWith4Args() {

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
  public void shouldSupportConstructorWith3Args() {

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
  public void shouldSupportConstructorWith2Args() {

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
  public void shouldSupportConstructorWith1Arg() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("a",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(100)
                             .allMatch(it -> it.size() == 1));


  }

  @Test
  public void shouldSupportConstructorWith50Args() {

    Gen<JsInt> ONE = JsIntGen.biased();
    JsObjGen obj = JsObjGen.of("k01",
                               ONE,
                               "k02",
                               ONE,
                               "k03",
                               ONE,
                               "k04",
                               ONE,
                               "k05",
                               ONE,
                               "k06",
                               ONE,
                               "k07",
                               ONE,
                               "k08",
                               ONE,
                               "k09",
                               ONE,
                               "k10",
                               ONE,
                               "k11",
                               ONE,
                               "k12",
                               ONE,
                               "k13",
                               ONE,
                               "k14",
                               ONE,
                               "k15",
                               ONE,
                               "k16",
                               ONE,
                               "k17",
                               ONE,
                               "k18",
                               ONE,
                               "k19",
                               ONE,
                               "k20",
                               ONE,
                               "k21",
                               ONE,
                               "k22",
                               ONE,
                               "k23",
                               ONE,
                               "k24",
                               ONE,
                               "k25",
                               ONE,
                               "k26",
                               ONE,
                               "k27",
                               ONE,
                               "k28",
                               ONE,
                               "k29",
                               ONE,
                               "k30",
                               ONE,
                               "k31",
                               ONE,
                               "k32",
                               ONE,
                               "k33",
                               ONE,
                               "k34",
                               ONE,
                               "k35",
                               ONE,
                               "k36",
                               ONE,
                               "k37",
                               ONE,
                               "k38",
                               ONE,
                               "k39",
                               ONE,
                               "k40",
                               ONE,
                               "k41",
                               ONE,
                               "k42",
                               ONE,
                               "k43",
                               ONE,
                               "k44",
                               ONE,
                               "k45",
                               ONE,
                               "k46",
                               ONE,
                               "k47",
                               ONE,
                               "k48",
                               ONE,
                               "k49",
                               ONE,
                               "k50",
                               ONE
                              );

    Assertions.assertTrue(obj.sample(40)
                             .allMatch(it -> it.size() == 50));
  }

  @Test
  public void shouldConcatGen() {

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
