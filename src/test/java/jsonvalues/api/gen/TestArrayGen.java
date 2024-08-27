package jsonvalues.api.gen;

import fun.gen.Gen;
import java.util.Map;
import jsonvalues.JsArray;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestArrayGen {


  @Test
  public void arbitrary() {

    Gen<JsArray> gen = JsArrayGen.arbitrary(JsStrGen.letter(),
                                            0,
                                            3);

    Assertions.assertTrue(gen.sample(10000)
                             .allMatch(it -> it.size() < 4));

    Map<Integer, Long> count = TestFun.generate(100000,
                                                gen.map(JsArray::size));

    TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                     TestFun.list(0,
                                                                  1,
                                                                  2,
                                                                  3),
                                                     0.1);
  }

  @Test
  public void biased() {

    Gen<JsArray> gen = JsArrayGen.biased(JsStrGen.letters(0,
                                                          10),
                                         0,
                                         3);

    int TIMES = 1000000;
    Assertions.assertTrue(gen.sample(TIMES)
                             .allMatch(it -> it.size() < 4));

    Map<Integer, Long> count = TestFun.generate(TIMES,
                                                gen.map(JsArray::size));

    Assertions.assertTrue(TestFun.isInMargin(count.get(0),
                                             0.1)
                                 .test(count.get(3)));
    Assertions.assertTrue(TestFun.isInMargin(count.get(1),
                                             0.1)
                                 .test(count.get(2)));


  }


}
